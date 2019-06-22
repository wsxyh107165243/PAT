package com.pat.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.pat.core.SingleRecordImpl;
import com.pat.pojo.Language;
import com.pat.pojo.SingleRecord;
import com.pat.pojo.Status;
import com.pat.pool.DbConnectionPool;
import com.pat.pool.ProblemPool;

public class Crawler {
	public static void main(String[] args) {
//		创建数据库连接
		DbConnectionPool pool = new DbConnectionPool();
		ProblemPool problemPool = new ProblemPool();
		Connection conn = new DbConnectionPool().getConnection();
		SingleRecordImpl sri = new SingleRecordImpl(pool, null, problemPool);
		
//		声明ChromeDriver服务地址
		System.setProperty("webdriver.chrome.driver",
				"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
//		设置浏览器静默执行
		ChromeOptions cos = new ChromeOptions();
//		cos.addArguments("--headless");
//		声明浏览器控制对象，传递静默执行参数
		WebDriver browser = new ChromeDriver(cos);
		
		while(!problemPool.isEmpty()) {
//			打开浏览器
			browser.get(problemPool.getUrl());
			try {
//				等待服务器相应
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				
			}
//			预处理对象
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("insert into singlerecord values(default, ?, ?, ?, ?, ?, ?, ?);");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
//			妈卖批的中英文格式不一致
			try {
//				browser.findElement(By.className("linkButton_bB6Ec")).click();
				browser.findElement(By.linkText("English")).click();
				Thread.sleep(200);
			}catch(Exception e) {
				
			}
			
			try {
				System.out.println(browser.getTitle());
				for(int i=0;i<4;i++) {
//					怎么也要有点输出表示一下到哪了吧
					System.out.println("第" + i + "页");
//					获取表格数据
					List<WebElement> trs = browser.findElement(new ByClassName("main_2UDel")).findElements(By.tagName("tr"));
//				--------------------------------------
//					格式化打印获取的数据
					for(WebElement tr: trs) {
//						获取行数据
						List<WebElement> tds = tr.findElements(By.tagName("td"));
//						如果该列是空数据，跳过这一行,如果该条记录没有被接受，跳过
						if(tds.isEmpty() || !"Accepted".equals(tds.get(1).getText())) {
							continue;
						}
//						别忘了utilize
						String submitTime 	= tds.get(0).getText();
						Status status 		= Status.getStatusByDesc(tds.get(1).getText());
						short score 		= Short.parseShort(tds.get(2).getText());
						String problemId	= tds.get(3).getText();
						Language lang		= Language.getLanguageByDesc(tds.get(4).getText());
						short runTime		= Short.parseShort((tds.get(5).getText().split(" "))[0]);
						String author		= tds.get(6).getText();
//						新single record
						SingleRecord sr = new SingleRecord(submitTime, status, score, problemId, lang, runTime, author);
//						插入数据库
						sri.insertSingleRecord(ps, sr);
						System.out.println(sr);
					}
//				---------------------------------------
					try{
//						尝试点击下一页
						browser.findElement(By.className("nextBtn_xlCFd")).click();
						Thread.sleep(100);
					}catch(Exception e) {
						e.printStackTrace();
//						如果下一页不存在，则退出该题的数据的查取
						break;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
//				无论如何，别忘了关闭浏览器资源
//				browser.close();
			}
		}
	}
}
