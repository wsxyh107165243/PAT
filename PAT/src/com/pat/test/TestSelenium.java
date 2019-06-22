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
		 * ˢ��ҳ�棬�ȴ�1��3��5��....�����Ѱ��Ԫ��
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
//		���������hello
//		browser.findElement(By.id("kw")).sendKeys(new String[] {"hello"});
//		browser.findElement(new ByClassName("s_ipt")).sendKeys(new String[] {"hello"});
//		ģ�����ύ
//		browser.findElement(By.id("su")).click();
//		browser.findElement(new ByClassName("icon_1c0XA")).click();
//		��ʾ��ҳԴ��
//		System.out.println(browser.getPageSource());
		/*
		 * ʹ�óػ�ȡ���������
		 */
//		BrowserPool bPool = new BrowserPool(1);
//		WebDriver browser = bPool.getConnection();
//		browser.get(new ProblemPool().getUrl());
		
		/*
		 * ����ChromeDriver�����ַ
		 */
		System.setProperty("webdriver.chrome.driver",
				"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
		/*
		 * �����������Ĭִ��
		 */
		ChromeOptions cos = new ChromeOptions();
//		cos.addArguments("--headless");
		/*
		 * ������������ƶ��󣬴��ݾ�Ĭִ�в���
		 */
		WebDriver browser = new ChromeDriver(cos);
		
		/*
		 * ��Ŀ��ҳ
		 */
		browser.get(new ProblemPool().getUrl());
		
		try {
			Thread.sleep(1000);
			System.out.println(browser.getTitle());
//			List<WebElement> findElements = browser.findElement(By.tagName("table")).findElements(By.tagName("td"));
//			browser.switchTo();
//			List<WebElement> findElements = browser.findElement(new ByClassName("DataTable_mkVjd")).findElements(By.tagName("td"));
			for(int i=0;true;i++) {
				System.out.println("��" + i + "ҳ");
//				List<WebElement> trs = browser.findElement(new ByClassName("main_2UDel")).findElements(By.tagName("tr"));
				findElement(browser, browser.getCurrentUrl(), new ByClassName("main_2UDel")).findElements(By.tagName("tr"));				
				/*
				 * ��ʽ����ӡ��ȡ������
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
				 * PATˢ����վ���ظ��������ƣ���������һ��������ʱ��������֤���ִ��,��4s��ȡ5ҳ�����Ǹ������ѡ��
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
