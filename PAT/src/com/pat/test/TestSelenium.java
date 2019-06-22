package com.pat.test;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.pat.pool.ProblemPool;

public class TestSelenium {
	
	private static WebElement findElement(WebDriver browser, String url, By selector) {
		try {
			WebElement element = browser.findElement(selector);
			return element;				
		} catch (NoSuchElementException e) {
		}
		/*
		 * 刷新页面，等待1、3、5、....秒后再寻找元素
		 */
		try {
			Thread.sleep( 4000 );
			for(int i=1;i<=4;i++) {
				browser.get(url);
				Thread.sleep( ((i<<1)-1)*1000 );
				try {
					WebElement element = browser.findElement(selector);
					return element;				
				} catch (NoSuchElementException e) {
				}
			}
		} catch (InterruptedException e) {
		}
		throw new NoSuchElementException("NoSuchElementException : " + "url = " + url + " selector = " + selector);
	}
	
	public static void main(String[] args) {
//		输入框输入hello
//		browser.findElement(By.id("kw")).sendKeys(new String[] {"hello"});
//		browser.findElement(new ByClassName("s_ipt")).sendKeys(new String[] {"hello"});
//		模拟点击提交
//		browser.findElement(By.id("su")).click();
//		browser.findElement(new ByClassName("icon_1c0XA")).click();
//		显示网页源码
//		System.out.println(browser.getPageSource());
		/*
		 * 使用池获取浏览器对象
		 */
//		BrowserPool bPool = new BrowserPool(1);
//		WebDriver browser = bPool.getConnection();
//		browser.get(new ProblemPool().getUrl());
		
		/*
		 * 声明ChromeDriver服务地址
		 */
		System.setProperty("webdriver.chrome.driver",
				"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		/*
		 * 设置浏览器静默执行
		 */
		ChromeOptions cos = new ChromeOptions();
//		cos.addArguments("--headless");
		/*
		 * 声明浏览器控制对象，传递静默执行参数
		 */
		WebDriver browser = new ChromeDriver(cos);
		
		/*
		 * 题目首页
		 */
		browser.get(new ProblemPool().getUrl());
		
		try {
			Thread.sleep(1000);
			System.out.println(browser.getTitle());
//			List<WebElement> findElements = browser.findElement(By.tagName("table")).findElements(By.tagName("td"));
//			browser.switchTo();
//			List<WebElement> findElements = browser.findElement(new ByClassName("DataTable_mkVjd")).findElements(By.tagName("td"));
			for(int i=0;true;i++) {
				System.out.println("第" + i + "页");
//				List<WebElement> trs = browser.findElement(new ByClassName("main_2UDel")).findElements(By.tagName("tr"));
				findElement(browser, browser.getCurrentUrl(), new ByClassName("main_2UDel")).findElements(By.tagName("tr"));				
				/*
				 * 格式化打印获取的数据
				 */
//				for(WebElement tr: trs) {
//					List<WebElement> tds = tr.findElements(By.tagName("td"));
//					for(WebElement td : tds) {
//						System.out.print(td.getText()+"\t");
//					}
//					System.out.println();
//				}
//			List<WebElement> divs= browser.findElement(By.tagName("body")).findElements(By.tagName("div"));
//			WebElement body= browser.findElement(By.id("sparkling-daydream")).findElement(new ByClassName("container_2t4Na"));
//			System.out.println(divs.size());
//			for(WebElement node: divs) {
//				try {
//					System.out.println(node.getAttribute("class"));
//				}catch(Exception e){
//					
//				}
//			}
				try{
//					browser.findElement(By.className("nextBtn_xlCFd")).click();
					findElement(browser, browser.getCurrentUrl(), By.className("nextBtn_xlCFd")).click();
					Thread.sleep(100);
				}catch(Exception e) {
					e.printStackTrace();
					break;
				}
				/*
				 * PAT刷题网站有重复访问限制，建议设置一个定期延时余量，保证间断执行,隔4s获取5页数据是个不错的选择
				 */
//				if(i%5 == 0) {
//					Thread.sleep(4000);
//				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
//			browser.quit();
//			bPool.closeAll();
		}
	}
}
