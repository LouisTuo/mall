package com.crazy.demo.gencoode.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * JDBC 连接获取枚举类，单例模式
 * @author Linsk
 *
 */
public enum JDBCConnection {
	INSTANCE;

	public Connection getConnection(String driver, String url, String userName, String passWord){
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			System.out.println("加载数据库驱动失败！");
			e.printStackTrace();
		}
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, userName, passWord);
		} catch (SQLException e) {
			System.out.println("数据库连接失败！");
			e.printStackTrace();
		}
		return conn;
	}
}
