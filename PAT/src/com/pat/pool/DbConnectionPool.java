package com.pat.pool;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.pat.util.JdbcUtils;

public class DbConnectionPool {
	/**
	 * 	连接池
	 */
	private List<Connection> pool;
	
	/**
	 * 	最大连接数和最小连接数
	 */
	private static int POOL_MAX_SIZE;
	private static int POOL_MIN_SIZE;
	
	static {
//		初始化最大连接数和最小连接数
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
	 * 	连接池初始化
	 */
	public DbConnectionPool() {
		if(pool == null)
			pool = new ArrayList<Connection>();
		this.reload();
	}
	
	/**
	 * 	将连接池中的保留对象数更新至允许的最小值
	 */
	private void reload() {
		while(pool.size() < POOL_MIN_SIZE)
			pool.add(JdbcUtils.createConnection());
	}
	
	/**
	 * 	从连接池中获取一个连接对象
	 * @return 一个连接对象
	 */
	public synchronized Connection getConnection() {
		if(pool.isEmpty())
			this.reload();
		return pool.remove(pool.size()-1);
	}
	
	/**
	 * 	将一个连接对象返回给连接池
	 * @param conn 需要返还的连接对象
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
