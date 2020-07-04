package com.crazy.demo.gencoode.utils;

import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.JDBCType;
import java.sql.SQLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 代码生成器工具类
 *
 * @author Linsk
 */
public class GeneratorUtils {

    /**
     * 下划线转驼峰法
     *
     * @param line       源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰,true:首字母 小写，false：首字母大写
     * @return 转换后的字符串
     */
    public static String underlineCamel(String line, boolean smallCamel) {
        if (line == null || "".equals(line)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0))
                    : Character.toUpperCase(word.charAt(0)));
            int index = word.lastIndexOf('_');
            if (index > 0) {
                sb.append(word.substring(1, index).toLowerCase());
            } else {
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }

    /**
     * @param type java.sql.Types 对应的值
     * @return
     * @author AnkaRebirth
     */
    public static String jdbcTojava(int type) {
        String temp = null;
        switch (type) {
            case -7:// BIT
                temp = "Boolean";
                break;
            case -6:// TINYINT
                temp = "Byte";
                break;
            case 5:// SMALLINT
                temp = "Short";
                break;
            case 4:// INTEGER
                temp = "Integer";
                break;
            case -5:// BIGINT
                temp = "Integer";
                break;
            case 6:// FLOAT
                temp = "Double";
                break;
            case 7:// REAL
                temp = "Float";
                break;
            case 8:// DOUBLE
                temp = "Double";
                break;
            case 2:// NUMERIC
                temp = "java.math.BigDecimal";
                break;
            case 3:// DECIMAL
                temp = "java.math.BigDecimal";
                break;
            case 1:// CHAR
                temp = "String";
                break;
            case 12:// VARCHAR
                temp = "String";
                break;
            case -1:// LONGVARCHAR
                temp = "String";
                break;
            case 91:// DATE
                temp = "Date";
                break;
            case 92:// TIME
                temp = "java.sql.Time";
                break;
            case 93:// TIMESTAMP
                temp = "Date";
                break;
            case -2:// BINARY
                temp = "Byte[]";
                break;
            case -3:// VARBINARY
                temp = "Byte[]";
                break;
            case -4:// LONGVARBINARY
                temp = "Byte[]";
                break;
            case 0:// NULL
                temp = null;
                break;
            case 1111:// OTHER
                temp = null;
                break;
            case 2000:// JAVA_OBJECT
                temp = null;
                break;
            case 2001:// DISTINCT
                temp = null;
                break;
            case 2002:// STRUCT
                temp = "java.sql.Struct";
                break;
            case 2003:// ARRAY
                temp = "java.sql.Array";
                break;
            case 2004:// BLOB
                temp = "java.sql.Blob";
                break;
            case 2005:// CLOB
                temp = "String";
                break;
            case 2006:// REF
                temp = "java.sql.Ref";
                break;
            case 70:// DATALINK
                temp = "java.net.URL";
                break;
            case 16:// BOOLEAN
                temp = "Boolean";
                break;
            // -------------------------JDBC 4.0 ------------------------------
            case -8:// ROWID
                temp = "java.sql.RowId";
                break;
            case -15:// NCHAR
                temp = "String";
                break;
            case -9:// NVARCHAR
                temp = "String";
                break;
            case -16:// LONGNVARCHAR
                temp = "String";
                break;
            case 2011:// NCLOB
                temp = "java.sql.NClob";
                break;
            case 2009:// SQLXML
                temp = "java.sql.SQLXML";
                break;
            // --------------------------JDBC 4.2 -----------------------------
            case 2012:// REF_CURSOR
                temp = null;
                break;
            case 2013:// TIME_WITH_TIMEZONE
                temp = "Date";
                break;
            case 2014:// TIMESTAMP_WITH_TIMEZONE
                temp = "Date";
                break;
            default:
                break;
        }
        return temp;
    }

    /**
     * DATETIME定义TIMESTAMP
     * DATE 定义DATE
     * CLOB转VARCHAR
     *
     * @param jdbcType
     * @return
     */
    public static String getJdbcTypeSame(String jdbcType) {
        String temp = jdbcType;
        if (jdbcType.equals(JDBCType.DATE.getName())) {
            temp = JDBCType.DATE.getName();
        }
        if (jdbcType.equals(JDBCType.TIMESTAMP.getName())) {
            temp = JDBCType.TIMESTAMP.getName();
        }
        if (jdbcType.equals(JDBCType.CLOB.getName())) {
            temp = JDBCType.VARCHAR.getName();
        }
        return temp;
    }

    public static void main(String[] args) {
        System.out.println(getJdbcTypeSame("DATE"));
    }

    /**
     * 关闭数据库连接
     *
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取freemarker cfg
     *
     * @return
     * @throws IOException
     */
    public static freemarker.template.Configuration getConfiguration(String TEMPLATE_FILE_PATH) {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.getVersion());
        try {
            cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }

    /**
     * 路径转换，.替换为/
     *
     * @param packageName
     * @return
     */
    public static String packageConvertPath(String packageName) {
        return String.format("/%s/", packageName.contains(".") ? packageName.replaceAll("\\.", "/") : packageName);
    }

    /**
     * 代码生成
     *
     * @param tableData          表数据
     * @param path               生成路径
     * @param ftl                模版
     * @param TEMPLATE_FILE_PATH 模版路径
     */
    public static void generatorCode(Map<String, Object> tableData, String path, String ftl, String TEMPLATE_FILE_PATH) {
        freemarker.template.Configuration cfg = getConfiguration(TEMPLATE_FILE_PATH);
        try {

            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try {
                cfg.getTemplate(ftl).process(tableData, new FileWriter(file));
            } catch (TemplateException e) {
                e.printStackTrace();
            }
            System.out.println("路径：" + path + " 生成成功");
            System.out.println("绝对路径：" + file.getAbsolutePath() + " 生成成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


