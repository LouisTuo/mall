package com.crazy.demo.gencoode.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.springframework.util.Assert;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取数据表的元数据信息
 *
 * @author Linsk
 */
public class GetTablesData {

    private static String QUERY_SQL = "select {0} from {1} where {2} = {3}";
    private static String DELETE_SQL = "delete from {0} where {1} = {2};";
    private static String UPDATE_SQL = "update {0} set {1} where {2} = {3}";

    /**
     * 封装表的元数据，按表为单位，逐个封装。
     *
     * @param tableName            表名
     * @param data                 元数据
     * @param BASE_PACKAGE         包路径
     * @param SERVICE_PACKAGE      service接口的包路径
     * @param SERVICE_IMPL_PACKAGE service接口实现类的包路径
     * @param MODEL_PACKAGE        dto的包路径
     * @param DAO_PACKAGE          dao的包路径
     * @param CONTROLLER_PACKAGE   controller的包路径
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> getTablesData(String tableName, DatabaseMetaData data,
                                                          String BASE_PACKAGE, String SERVICE_PACKAGE, String SERVICE_IMPL_PACKAGE, String MODEL_PACKAGE,
                                                          String DAO_PACKAGE, String CONTROLLER_PACKAGE) throws SQLException {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        ResultSet rs = data.getTables(null, null, tableName == "" ? null : tableName, new String[]{"TABLE"});
        while (rs.next()) {
            System.out.println("表名:" + rs.getString("TABLE_NAME").toUpperCase());
            System.out.println("表类型：" + rs.getString("TABLE_TYPE"));
            System.out.println("表所属数据库：" + rs.getString("TABLE_CAT"));
            System.out.println("表备注：" + rs.getString("REMARKS"));
            Map<String, Object> map = getDataMap(rs.getString("TABLE_NAME").toUpperCase(), rs.getString("REMARKS"),
                    data, BASE_PACKAGE, SERVICE_PACKAGE, SERVICE_IMPL_PACKAGE, MODEL_PACKAGE,
                    DAO_PACKAGE, CONTROLLER_PACKAGE);
            list.add(map);
        }
        rs.close();
        return list;
    }

    /**
     * 封装表数据
     *
     * @param tableName            表名
     * @param reMarks              表备注
     * @param data                 DatabaseMetaData data 对象
     * @param BASE_PACKAGE
     * @param SERVICE_PACKAGE
     * @param SERVICE_IMPL_PACKAGE
     * @param MODEL_PACKAGE
     * @param DAO_PACKAGE
     * @param CONTROLLER_PACKAGE
     * @return Map<String, Object> 封装表信息数据
     * @throws SQLException
     */
    public static Map<String, Object> getDataMap(String tableName, String reMarks, DatabaseMetaData data,
                                                 String BASE_PACKAGE, String SERVICE_PACKAGE, String SERVICE_IMPL_PACKAGE, String MODEL_PACKAGE,
                                                 String DAO_PACKAGE, String CONTROLLER_PACKAGE) throws SQLException {
        Assert.hasText(tableName, "表名称不能为空！");
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        /**
         * 基本信息，作者、时间、版本
         */
        map.put("author", "sys");
        map.put("date", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        map.put("version", "1.0.0");
        /**
         * 模板的变量信息
         */
        map.put("tableName", tableName);//表名
        map.put("claszName", GeneratorUtils.underlineCamel(tableName, false));//首字母大写
        map.put("entityName", GeneratorUtils.underlineCamel(tableName, false));//实体类class名称，首字母大写
        map.put("entityVarName", GeneratorUtils.underlineCamel(tableName, true));//实体类成员变量名称，首字母小写
        if (StringUtils.isEmpty(reMarks)) {
            reMarks = tableName;
        }
        map.put("reMarks", reMarks);//表备注说明
        map.put("basePackage", BASE_PACKAGE);//基础包路径
        map.put("basePackageService", SERVICE_PACKAGE);//service接口的包路径名称
        map.put("basePackageServiceImpl", SERVICE_IMPL_PACKAGE);//service实现类的包路径名称
        map.put("basePackageEntity", MODEL_PACKAGE);//entity实体类的包路径名称
        map.put("basePackageDao", DAO_PACKAGE);//DAO接口的包路径名称
        map.put("basePackageController", CONTROLLER_PACKAGE);//controller类的包路径名称
        map.put("baseRequestMapping", GeneratorUtils.underlineCamel(tableName, true));//实体类的请求路径前缀
        ResultSet rs = data.getColumns(null, "%", tableName, "%");

        StringBuilder insert1 = new StringBuilder().append("insert into ").append(tableName).append("  (");
        StringBuilder insert2 = new StringBuilder().append("  values( ");

        List<String> columns = new ArrayList<>();

        ResultSet pk = data.getPrimaryKeys(null, null, tableName);
        while (pk.next()) {
			/*System.out.println("表");
			System.out.println("PKTABLE_CAT:" + pk.getObject(1));
			System.out.println("PKTABLE_SCHEM:" + pk.getObject(2));
			System.out.println("PKTABLE_NAME:" + pk.getObject(3));
			System.out.println("COLUMN_NAME:" + pk.getObject(4));
			System.out.println("KEY_SEQ:" + pk.getObject(5));
			System.out.println("PK_NAME:" + pk.getObject(6));*/
            map.put("pk_name", pk.getObject(4));
        }

        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");//数据库字段名称
            String fieldName = GeneratorUtils.underlineCamel(columnName, true);//java字段名称，驼峰形式
            String javaType = GeneratorUtils.jdbcTojava(rs.getInt("DATA_TYPE"));//java类型
            //System.out.println("columnName="+columnName+"-javaType="+javaType+",jdbcType="+rs.getInt("DATA_TYPE"));
            Assert.hasText(javaType, "未能获取到对应的java变量类型，请检查【" + columnName + "】数据库中对应类型！");
            //System.out.println(rs.getInt("DATA_TYPE"));
            //System.out.println(JdbcType.valueOf(rs.getInt("DATA_TYPE")));
            //System.out.println((JdbcType.valueOf(rs.getInt("DATA_TYPE")).name()));
            //System.out.println(GeneratorUtils.getJdbcTypeSame((JdbcType.valueOf(rs.getInt("DATA_TYPE")).name())));

            JdbcType data_type = JdbcType.forCode(rs.getInt("DATA_TYPE"));
            String jdbcType = GeneratorUtils.getJdbcTypeSame(data_type.name()); //字段对应jdbc的类型


            String remarks = rs.getString("REMARKS");//字段描述
            Map<String, String> col = new HashMap<String, String>();
            col.put("fieldName", fieldName);//实体类成员变量名称
            col.put("javaType", javaType);//字段java类型
            col.put("jdbcType", jdbcType);//字段jdbc类型
            col.put("filedComment", remarks);//字段描述
            col.put("remarks", remarks);//字段描述
            list.add(col);

            insert1.append(columnName).append(",");
            insert2.append(" #{").append(columnName).append("} ,");

            columns.add(columnName);
        }
        rs.close();
        insert1.deleteCharAt(insert1.lastIndexOf(","));
        insert2.deleteCharAt(insert2.lastIndexOf(","));
        insert1.append(")").append(insert2).append(")");
        map.put("insert", insert1);

        String columnStr = String.join(",", columns);
        String pkName = (String) map.get("pk_name");
        String queryStr = MessageFormat.format(QUERY_SQL, columnStr, tableName, pkName, getValueStr(pkName));
        String delStr = MessageFormat.format(DELETE_SQL, tableName, pkName, getValueStr(pkName));
        String updateStr = MessageFormat.format(UPDATE_SQL, tableName, getUpdateCols(columns), pkName, getValueStr(pkName));

        map.put("select", queryStr);
        map.put("delete", delStr);
        map.put("update", updateStr);

        map.put("columns", list); //字段列表集合
        return map;
    }

    // 根据key获取 #{key}字符串
    private static String getValueStr(String key) {
        String keyStr = GeneratorUtils.underlineCamel(key, true);
        return "#{" + keyStr + "}";
    }

    private static String getFileName(String columnName) {
        return GeneratorUtils.underlineCamel(columnName, true);
    }

    private static String getUpdateCols(List<String> columns) {
        StringBuilder sb = new StringBuilder();
        columns.forEach(column -> {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(column).append("= #{").append(getFileName(column)).append("}");
        });
        return sb.toString();
    }
}
