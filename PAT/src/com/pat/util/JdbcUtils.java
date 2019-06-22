package com.pat.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.pat.pojo.Configuration;

/**
 * 	���ӳض���������ģ�
 * 	�������ݿ�Ԥ����������˵����Ӧ�����������
 * 	��Ϊ�������Ҫ��Ϊ���ṩ���Ӷ����ڵ�
 *  ͬʱ����˹ر����ӵĹ���
 * @author xian yuehui
 *
 */
public class JdbcUtils {
	//��̬���������Ϣ
	private static Configuration conf;
	//��̬��ʼ��
	static {
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties"));
		}catch (Exception e) {
			//����log��Ϣ
			e.printStackTrace();
		}
		conf = new Configuration();
		conf.setChromeDriver(	pros.getProperty("chromeDriver"));
		conf.setPatUrl(			pros.getProperty("patUrl"));
		conf.setDbDriver(		pros.getProperty("dbDriver"));
		conf.setDbUrl(			pros.getProperty("dbUrl"));
		conf.setUser(			pros.getProperty("user"));
		conf.setPwd(			pros.getProperty("pwd"));
		
//		�������ݿ�����
		try {
			Class.forName(conf.getDbDriver());
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 	����һ������
	 * @return һ���µ�����
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
	 * 	�ر�һ������
	 * @param conn ���Ӷ�������
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
