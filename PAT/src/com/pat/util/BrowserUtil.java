package com.pat.util;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * 	���������ļ�������һ����Ĭ������Ķ���
 * 	ָ����Ŀ�ύ�б�ĵ�һҳ������ָ��ָ����url
 * 	���ߣ��ر�һ����Ĭ������Ķ����ͷ��ڴ�
 * @author xian yuehui
 * @version 1.0
 */
public class BrowserUtil {
	/**
	 * 	�����������
	 */
	private static String browserDriverName;
	/**
	 * 	�����������ַ������·��
	 */
	private static String browserDriverPath;
	/**
	 * 	Ĭ��PAT�ύ�б�·����ַ
	 */
	private static String defaultUrl;
	/**
	 * 	����������в���
	 */
	private static String browserOptions;
	
	static {
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		browserDriverName	= pros.getProperty("browserDriverName");
		browserDriverPath	= pros.getProperty("browserDriverPath");
		defaultUrl			= pros.getProperty("defaultUrl");
		browserOptions		= "--headless";
		/*
		 * ��������
		 */
		System.setProperty(browserDriverName, browserDriverPath);
	}
	
	/**
	 * 	˽�л���������˵������һ��������
	 */
	private BrowserUtil() {
		
	}
	
	/**
	 * 	�������ļ��е���Ϣ����һ����Ĭ������Ķ���
	 * @return һ����Ĭ������Ķ���
	 */
	public static WebDriver getConnection() {
		/*
		 * �����������Ĭִ��
		 */
		ChromeOptions options = new ChromeOptions();
		options.addArguments(browserOptions);
		/*
		 * ������������ƶ��󣬴��ݾ�Ĭִ�в���
		 */
		WebDriver browser = new ChromeDriver(options);
		return browser;
	}
	
	/**
	 * 	�������ļ��е���Ϣ����һ����Ĭ������Ķ���ָ����ĿĬ�ϵļ׼�1001�ύ�б�ĵ�һҳ
	 * @return һ����Ĭ������Ķ���
	 */
	public static WebDriver getDefaultConnection() {
		WebDriver browser = getConnection();
		browser.get(defaultUrl);
		return browser;
	}
	
	/**
	 * 	�������ļ��е���Ϣ����һ����Ĭ������Ķ��󣬳�ʼʱ��ָ��ָ����url
	 * @param patUrl ��Ҫ��ʼ������url
	 * @return һ����Ĭ������Ķ���
	 */
	public static WebDriver getDefalultConnection(String url) {
		WebDriver browser = getConnection();
		browser.get(url);
		return browser;
	}
	
	/**
	 * 	�ر�һ����Ĭ������Ķ����ͷ��ڴ�
	 * @param browser
	 */
	public static void close(WebDriver browser) {
		browser.close();
		browser.quit();
		/*
		 * ÿ���ڴ��е����ݶ������壬������
		 */
		Runtime rt = Runtime.getRuntime();
		try {
			System.out.println(rt.exec("taskkill /f /IM chrome.exe").toString());
			System.out.println(rt.exec("taskkill /f /IM chromedriver.exe").toString());
			System.out.println(rt.exec("taskkill /f /IM conhost.exe").toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
