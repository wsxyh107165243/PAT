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
//		�������ݿ�����
		DbConnectionPool pool = new DbConnectionPool();
		ProblemPool problemPool = new ProblemPool();
		Connection conn = new DbConnectionPool().getConnection();
		SingleRecordImpl sri = new SingleRecordImpl(pool, null, problemPool);
		
//		����ChromeDriver�����ַ
		System.setProperty("webdriver.chrome.driver",
				"C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
//		�����������Ĭִ��
		ChromeOptions cos = new ChromeOptions();
//		cos.addArguments("--headless");
//		������������ƶ��󣬴��ݾ�Ĭִ�в���
		WebDriver browser = new ChromeDriver(cos);
		
		while(!problemPool.isEmpty()) {
//			�������
			browser.get(problemPool.getUrl());
			try {
//				�ȴ���������Ӧ
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				
			}
//			Ԥ�������
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("insert into singlerecord values(default, ?, ?, ?, ?, ?, ?, ?);");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
//			����������Ӣ�ĸ�ʽ��һ��
			try {
//				browser.findElement(By.className("linkButton_bB6Ec")).click();
				browser.findElement(By.linkText("English")).click();
				Thread.sleep(200);
			}catch(Exception e) {
				
			}
			
			try {
				System.out.println(browser.getTitle());
				for(int i=0;i<4;i++) {
//					��ôҲҪ�е������ʾһ�µ����˰�
					System.out.println("��" + i + "ҳ");
//					��ȡ�������
					List<WebElement> trs = browser.findElement(new ByClassName("main_2UDel")).findElements(By.tagName("tr"));
//				--------------------------------------
//					��ʽ����ӡ��ȡ������
					for(WebElement tr: trs) {
//						��ȡ������
						List<WebElement> tds = tr.findElements(By.tagName("td"));
//						��������ǿ����ݣ�������һ��,���������¼û�б����ܣ�����
						if(tds.isEmpty() || !"Accepted".equals(tds.get(1).getText())) {
							continue;
						}
//						������utilize
						String submitTime 	= tds.get(0).getText();
						Status status 		= Status.getStatusByDesc(tds.get(1).getText());
						short score 		= Short.parseShort(tds.get(2).getText());
						String problemId	= tds.get(3).getText();
						Language lang		= Language.getLanguageByDesc(tds.get(4).getText());
						short runTime		= Short.parseShort((tds.get(5).getText().split(" "))[0]);
						String author		= tds.get(6).getText();
//						��single record
						SingleRecord sr = new SingleRecord(submitTime, status, score, problemId, lang, runTime, author);
//						�������ݿ�
						sri.insertSingleRecord(ps, sr);
						System.out.println(sr);
					}
//				---------------------------------------
					try{
//						���Ե����һҳ
						browser.findElement(By.className("nextBtn_xlCFd")).click();
						Thread.sleep(100);
					}catch(Exception e) {
						e.printStackTrace();
//						�����һҳ�����ڣ����˳���������ݵĲ�ȡ
						break;
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
//				������Σ������˹ر��������Դ
//				browser.close();
			}
		}
	}
}
