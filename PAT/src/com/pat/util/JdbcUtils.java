package com.pat.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.pat.pojo.Configuration;

/**
 * 	连接池对象管理中心，
 * 	对于数据库预处理命令来说，不应该在这里出现
 * 	因为这个类主要是为了提供连接而存在的
 *  同时，兼顾关闭连接的功能
 * @author xian yuehui
 *
 */
public class JdbcUtils {
	//静态存放配置信息
	private static Configuration conf;
	//静态初始化
	static {
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties"));
		}catch (Exception e) {
			//补上log信息
			e.printStackTrace();
		}
		conf = new Configuration();
		conf.setChromeDriver(	pros.getProperty("chromeDriver"));
		conf.setPatUrl(			pros.getProperty("patUrl"));
		conf.setDbDriver(		pros.getProperty("dbDriver"));
		conf.setDbUrl(			pros.getProperty("dbUrl"));
		conf.setUser(			pros.getProperty("user"));
		conf.setPwd(			pros.getProperty("pwd"));
		
//		加载数据库驱动
		try {
			Class.forName(conf.getDbDriver());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 	创建一个连接
	 * @return 一个新的连接
	 */
	public static Connection createConnection() {
		try {
			return DriverManager.getConnection(conf.getDbUrl(), conf.getUser(), conf.getPwd());
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 	关闭一个连接
	 * @param conn 连接对象引用
	 */
	public static void close(Connection conn) {
		try {
			if(conn != null)
				conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
