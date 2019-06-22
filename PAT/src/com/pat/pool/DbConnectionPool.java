package com.pat.pool;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.pat.util.JdbcUtils;

public class DbConnectionPool {
	/**
	 * 	���ӳ�
	 */
	private List<Connection> pool;
	
	/**
	 * 	�������������С������
	 */
	private static int POOL_MAX_SIZE;
	private static int POOL_MIN_SIZE;
	
	static {
//		��ʼ���������������С������
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		POOL_MAX_SIZE = Integer.parseInt(pros.getProperty("poolMaxSize"));
		POOL_MIN_SIZE = Integer.parseInt(pros.getProperty("poolMinSize"));
	}
	
	/**
	 * 	���ӳس�ʼ��
	 */
	public DbConnectionPool() {
		if(pool == null)
			pool = new ArrayList<Connection>();
		this.reload();
	}
	
	/**
	 * 	�����ӳ��еı����������������������Сֵ
	 */
	private void reload() {
		while(pool.size() < POOL_MIN_SIZE)
			pool.add(JdbcUtils.createConnection());
	}
	
	/**
	 * 	�����ӳ��л�ȡһ�����Ӷ���
	 * @return һ�����Ӷ���
	 */
	public synchronized Connection getConnection() {
		if(pool.isEmpty())
			this.reload();
		return pool.remove(pool.size()-1);
	}
	
	/**
	 * 	��һ�����Ӷ��󷵻ظ����ӳ�
	 * @param conn ��Ҫ���������Ӷ���
	 */
	public synchronized void close(Connection conn) {
		if(pool.size() >= POOL_MAX_SIZE)
			JdbcUtils.close(conn);
		else
			pool.add(conn);
	}
	
	public static void main(String[] args) {
		DbConnectionPool pool = new DbConnectionPool();
		Connection conn = pool.getConnection();
		System.out.println(conn);
	}
}
