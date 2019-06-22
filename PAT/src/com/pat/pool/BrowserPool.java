package com.pat.pool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.pat.util.BrowserUtil;

/**
 * 	感觉后面可以养泛型来搞
 * @author xian yuehui
 * @version 1.0
 */
public class BrowserPool {
	/**
	 * 	连接池
	 */
	private List<WebDriver> pool;
	
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
	public BrowserPool() {
		if(pool == null)
			pool = new ArrayList<WebDriver>();
		this.reload();
	}
	
	public BrowserPool(int minSize) {
		POOL_MIN_SIZE = 1;
		if(pool == null)
			pool = new ArrayList<WebDriver>();
		this.reload();
	}
	
	/**
	 * 	将连接池中的保留对象数更新至允许的最小值
	 */
	private void reload() {
		while(pool.size() < POOL_MIN_SIZE)
			pool.add(BrowserUtil.getConnection());
		try {
//			等待服务器相应
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			
		}
	}
	
	
	/**
	 * 	从连接池中获取一个连接对象
	 * @return 一个连接对象
	 */
	public synchronized WebDriver getConnection() {
		if(pool.isEmpty())
			this.reload();
		return pool.remove(pool.size()-1);
	}
	
	/**
	 * 	将一个连接对象返回给连接池
	 * @param conn 需要返还的连接对象
	 */
	public synchronized void close(WebDriver conn) {
		if(pool.size() >= POOL_MAX_SIZE)
			BrowserUtil.close(conn);
		else
			pool.add(conn);
	}
	
	/**
	 * 	关闭池中所有的浏览器
	 */
	public synchronized void closeAll() {
		for(WebDriver browser : pool) {
			BrowserUtil.close(browser);
		}
	}
}
