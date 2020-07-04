package com.crazy.demo.gencoode.config;

import com.crazy.demo.gencoode.utils.GeneratorUtils;
import lombok.Data;
import net.bytebuddy.description.field.FieldList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

/**
 * 代码自动生成器，基于Freemarker 2.3.28
 * @author Linsk
 *
 */
@Data
@Configuration
//指定所读取的配置文件的路径
@PropertySource(value = "classpath:generatorCfg.properties")
//指定配置的一些属性,其中的prefix表示前缀
@ConfigurationProperties(prefix = "hyit.ci.codegenerator")
public class CodeGenerator {
	private String driver;
	private String url;
	private String username;
	private String password;

	// 表名称，不限大小写，可包含单字符通配符("_"),或多字符通配符("%");
	private static String TALBE_NAME = "neps_question";
	// 项目基础包名称，根据自己公司的项目修改
	private String base_package;
	// Model所在包
	private String entity_package;
	// Dao所在包
	private String dao_package;
	// Service所在包
	private String service_package;
	// ServiceImpl所在包
	private String service_impl_package;
	// Controller所在包
	private String controller_package;
	// java文件路径
	private String java_path;
	// resource文件路径
	private String resources_path;
	// mapper所在路径
	private String mapper_path;
	// 模板位置
    private String template_file_path;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Properties pro = 
		try {
			new CodeGenerator().generCode(TALBE_NAME);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/**
	 * 自动化生成代码，包括：entity、mapping文件、dao、service、serviceimpl、controller
	 * @param tableName
	 * @throws Exception
	 */
	public File generCode(String tableName) throws Exception{

		if(StringUtils.isEmpty(driver) || StringUtils.isEmpty(url) ||StringUtils.isEmpty(username) ||StringUtils.isEmpty(password) ) {
			driver="com.mysql.jdbc.Driver";
			url="jdbc:mysql://127.0.0.1:3306/jeecg-boot?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
			username="root";
			password="123456";
			// String base_path = "D:\\BetterMe\\jeecg-boot2.1.3\\jeecg-boot-2.1.3\\";
			base_package=  "com.crazy.demo.gencoode";
			entity_package=base_package+".entity";
			dao_package=base_package+".mapper";
			service_package=base_package+".service";
			service_impl_package=service_package+".impl";
			controller_package=base_package+".controller";
			java_path="src/main/java";
			resources_path="src/main/resources";
			mapper_path=dao_package+".xml";

			// 模板路径设置绝对路径
			// template_file_path= "src/main/resources/templates/generator";
			template_file_path = "D:\\idea\\mall\\gen-coode\\src\\main\\resources\\ftlTemplate";
		}
		Connection con = JDBCConnection.INSTANCE.getConnection(driver, url, username, password);
		DatabaseMetaData data = con.getMetaData();
		System.out.println(data.getDriverName());
		System.out.println(data.getURL());
		System.out.println(data.getUserName());
		System.out.println("");
		List<Map<String, Object>> list = GetTablesData.getTablesData(tableName,
				data, base_package, service_package, service_impl_package, entity_package,
				dao_package, controller_package);
		GeneratorUtils.closeConnection(con);

		System.out.println("生成表"+list.size()+"个！SUCCESS");

		//********************代码生成开始*****************************
		for (Map<String, Object> map : list) {
			//model 生成
			genModel(map, GeneratorUtils.packageConvertPath(entity_package));
			//Mapper 生成
			genMapper(map,GeneratorUtils.packageConvertPath(mapper_path));
			//Dao 生成
			genDao(map, GeneratorUtils.packageConvertPath(dao_package));
			//Service 生成
			genService(map, GeneratorUtils.packageConvertPath(service_package));
			//ServiceImpl 生成
			genServiceImpl(map, GeneratorUtils.packageConvertPath(service_impl_package));
			// Controller 生成
			genController(map, GeneratorUtils.packageConvertPath(controller_package));
		}
		//********************************结束*****************************
		return new File(java_path + File.separator+GeneratorUtils.packageConvertPath(base_package));
	}
	/**
	 * 生成Model
	 * @param tableData
	 * @param PACKAGE_PATH_MODEL
	 */
	private void genModel(Map<String, Object> tableData, String PACKAGE_PATH_MODEL) {
		String path = java_path + PACKAGE_PATH_MODEL + tableData.get("claszName") + ".java";
		GeneratorUtils.generatorCode(tableData, path, "entity.ftl", template_file_path);
	}
	/**
	 * 生成Mapper
	 * @param tableData
	 */
	private void genMapper(Map<String, Object> tableData,String MAPPER_PATH) {
		String path = java_path + MAPPER_PATH + tableData.get("claszName") + "Mapper.xml";
		GeneratorUtils.generatorCode(tableData, path, "mapper.ftl", template_file_path);
	}
	/**
	 * 生成Dao
	 * @param tableData
	 * @param PACKAGE_PATH_DAO
	 */
	private void genDao(Map<String, Object> tableData,  String PACKAGE_PATH_DAO) {
		String path = java_path + PACKAGE_PATH_DAO + tableData.get("claszName") + "Mapper.java";
		GeneratorUtils.generatorCode(tableData, path, "dao.ftl", template_file_path);
	}
	/**
	 * 生成Service
	 * @param tableData
	 * @param PACKAGE_PATH_SERVICE
	 */
	private void genService(Map<String, Object> tableData, String PACKAGE_PATH_SERVICE) {
		String path = java_path + PACKAGE_PATH_SERVICE + "I"+tableData.get("claszName") + "Service.java";
		GeneratorUtils.generatorCode(tableData, path, "service.ftl", template_file_path);
	}
	/**
	 * 生成ServiceImpl
	 * @param tableData
	 * @param PACKAGE_PATH_SERVICE_IMPL
	 */
	private void genServiceImpl(Map<String, Object> tableData, String PACKAGE_PATH_SERVICE_IMPL) {
		String path = java_path + PACKAGE_PATH_SERVICE_IMPL + tableData.get("claszName") + "ServiceImpl.java";
		GeneratorUtils.generatorCode(tableData, path, "service-impl.ftl", template_file_path);
	}
	/**
	 * 生成Controller
	 * @param tableData
	 * @param PACKAGE_PATH_CONTROLLER
	 */
	private void genController(Map<String, Object> tableData, String PACKAGE_PATH_CONTROLLER) {
		String path = java_path + PACKAGE_PATH_CONTROLLER + tableData.get("claszName") + "Controller.java";
		GeneratorUtils.generatorCode(tableData, path, "controller.ftl", template_file_path);
	}
}
