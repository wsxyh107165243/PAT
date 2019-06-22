package com.pat.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.pat.pojo.Language;
import com.pat.pojo.SingleRecord;
import com.pat.pojo.Status;
import com.pat.pool.BrowserPool;
import com.pat.pool.DbConnectionPool;
import com.pat.pool.ProblemPool;
import com.pat.util.DateUtil;

/**
 * 	维护一个和Mysql中的SingleRecord表相关的处理对象
 * @author xian yuehui
 * @version 1.0
 */
public class SingleRecordImplBackUp implements Runnable{
	private DbConnectionPool dbPool;
	private BrowserPool brPool;
	private ProblemPool pPool;
	
	public SingleRecordImplBackUp(DbConnectionPool dbPool, BrowserPool brPool, ProblemPool pPool) {
		this.dbPool = dbPool;
		this.brPool = brPool;
		this.pPool = pPool;
	}

	public void insertSingleRecord(PreparedStatement ps, SingleRecord sr) {
		try {
//			数据填充
			ps.setObject(1, DateUtil.patTimeToMysqlTime(sr.getSubmitTime()));
			ps.setObject(2, sr.getStatus().getCode());
			ps.setObject(3, sr.getScore());
			ps.setObject(4, Integer.parseInt(sr.getProblemId()));
			ps.setObject(5, sr.getLang().getCode());
			ps.setObject(6, sr.getRunTime());
			ps.setObject(7, sr.getAuthor());
//			提交
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		WebDriver browser = null;
		Connection conn = null;
		String url = null;
		while(true) {
//			获取资源
			url = pPool.getUrl();
			browser = brPool.getConnection();
			conn = dbPool.getConnection();
//			不存在url
			if(url == null || browser == null || conn == null)
				break;
//			预处理对象
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("insert into singlerecord values(default, ?, ?, ?, ?, ?, ?, ?);");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			try {
				try {
//					该问题的根页面
					browser.get(url);
					Thread.sleep(3000);
//					转英文网站数据
					browser.findElement(By.linkText("English")).click();
					Thread.sleep(3000);
				}catch(Exception e) {
//				不处理
				}
//				对于这个问题号，所有的正确的数据
				boolean isRunning = true;
				for(int i=1;isRunning;++i) {
					System.out.println("T_NAME:" + Thread.currentThread().getName() + " Page"+ i );
//					表格
					List<WebElement> trs = null;
					try {
						trs = browser.findElement(new ByClassName("main_2UDel")).findElements(By.tagName("tr"));					
					}catch (Exception e) {
						System.out.println("T_NAME:" + Thread.currentThread().getName() + " url=" + url);
						e.printStackTrace();
//					数据不存在
						break;
					}
//				处理多行
					for(WebElement tr: trs) {
//					处理单行
						List<WebElement> tds = null;
						try {
//							pat网页数据设计不合理，只能以这个作为标准
//							browser.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
							tds = tr.findElements(By.tagName("td"));
						}catch(Exception e) {
							isRunning = false;
							break;
						}
//						空列或答案未全部正确，跳过
						if(tds.isEmpty() || !"Accepted".equals(tds.get(1).getText())) {
							continue;
						}
//						创建SingleRecord，考虑utilize
						String submitTime 	= tds.get(0).getText();
						Status status 		= Status.getStatusByDesc(tds.get(1).getText());
						short score 		= Short.parseShort(tds.get(2).getText());
						String problemId	= tds.get(3).getText();
						Language lang		= Language.getLanguageByDesc(tds.get(4).getText());
						short runTime		= Short.parseShort((tds.get(5).getText().split(" "))[0]);
						String author		= tds.get(6).getText();
						SingleRecord sr = new SingleRecord(submitTime, status, score, problemId, lang, runTime, author);
//						插入数据库
						insertSingleRecord(ps, sr);
//						System.out.println("T_NAME:" + Thread.currentThread().getName() + " Page"+ i +":" + sr);
					}
					try{
//						下一页
						browser.findElement(By.className("nextBtn_xlCFd")).click();
						Thread.sleep(1000);
					}catch(Exception e) {
//						如果下一页不存在
						System.out.println("T_NAME:" + Thread.currentThread().getName() + " url=" + url);
						e.printStackTrace();
						break;
					}
					if(i%5 == 0)
						Thread.sleep(4000);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
//				永远记得关闭资源
				browser.close();
				System.out.println("T_NAME:" + Thread.currentThread().getName() + " : completed!");
			}
		}
	}
}
