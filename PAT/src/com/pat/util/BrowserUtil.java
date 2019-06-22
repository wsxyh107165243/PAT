package com.pat.util;

import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * 	根据配置文件，返回一个静默浏览器的对象，
 * 	指向题目提交列表的第一页，或者指向指定的url
 * 	或者，关闭一个静默浏览器的对象，释放内存
 * @author xian yuehui
 * @version 1.0
 */
public class BrowserUtil {
	/**
	 * 	浏览器驱动名
	 */
	private static String browserDriverName;
	/**
	 * 	浏览器驱动地址，绝对路径
	 */
	private static String browserDriverPath;
	/**
	 * 	默认PAT提交列表路径地址
	 */
	private static String defaultUrl;
	/**
	 * 	浏览器命令行参数
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
		 * 驱动设置
		 */
		System.setProperty(browserDriverName, browserDriverPath);
	}
	
	/**
	 * 	私有化构造器，说明这是一个工具类
	 */
	private BrowserUtil() {
		
	}
	
	/**
	 * 	以配置文件中的信息返回一个静默浏览器的对象
	 * @return 一个静默浏览器的对象
	 */
	public static WebDriver getConnection() {
		/*
		 * 设置浏览器静默执行
		 */
		ChromeOptions options = new ChromeOptions();
		options.addArguments(browserOptions);
		/*
		 * 声明浏览器控制对象，传递静默执行参数
		 */
		WebDriver browser = new ChromeDriver(options);
		return browser;
	}
	
	/**
	 * 	以配置文件中的信息返回一个静默浏览器的对象，指向题目默认的甲级1001提交列表的第一页
	 * @return 一个静默浏览器的对象
	 */
	public static WebDriver getDefaultConnection() {
		WebDriver browser = getConnection();
		browser.get(defaultUrl);
		return browser;
	}
	
	/**
	 * 	以配置文件中的信息返回一个静默浏览器的对象，初始时，指向指定的url
	 * @param patUrl 需要初始开启的url
	 * @return 一个静默浏览器的对象
	 */
	public static WebDriver getDefalultConnection(String url) {
		WebDriver browser = getConnection();
		browser.get(url);
		return browser;
	}
	
	/**
	 * 	关闭一个静默浏览器的对象，释放内存
	 * @param browser
	 */
	public static void close(WebDriver browser) {
		browser.close();
		browser.quit();
		/*
		 * 每次内存中的内容都清理不清，很难受
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
