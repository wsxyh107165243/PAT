package com.pat.pool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

import com.pat.util.BrowserUtil;

/**
 * 	�о������������������
 * @author xian yuehui
 * @version 1.0
 */
public class BrowserPool {
	/**
	 * 	���ӳ�
	 */
	private List<WebDriver> pool;
	
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
	 * 	�����ӳ��еı����������������������Сֵ
	 */
	private void reload() {
		while(pool.size() < POOL_MIN_SIZE)
			pool.add(BrowserUtil.getConnection());
		try {
//			�ȴ���������Ӧ
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			
		}
	}
	
	
	/**
	 * 	�����ӳ��л�ȡһ�����Ӷ���
	 * @return һ�����Ӷ���
	 */
	public synchronized WebDriver getConnection() {
		if(pool.isEmpty())
			this.reload();
		return pool.remove(pool.size()-1);
	}
	
	/**
	 * 	��һ�����Ӷ��󷵻ظ����ӳ�
	 * @param conn ��Ҫ���������Ӷ���
	 */
	public synchronized void close(WebDriver conn) {
		if(pool.size() >= POOL_MAX_SIZE)
			BrowserUtil.close(conn);
		else
			pool.add(conn);
	}
	
	/**
	 * 	�رճ������е������
	 */
	public synchronized void closeAll() {
		for(WebDriver browser : pool) {
			BrowserUtil.close(browser);
		}
	}
}
