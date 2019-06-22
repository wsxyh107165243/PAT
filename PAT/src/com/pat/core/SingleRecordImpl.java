package com.pat.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.pat.pojo.Language;
import com.pat.pojo.Problem;
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
public class SingleRecordImpl implements Runnable{
	/**
	 * 	数据库连接池
	 */
	private DbConnectionPool dbPool;
	/**
	 * 	浏览器连接池
	 */
	private BrowserPool brPool;
	/**
	 *  题目池
	 */
	private ProblemPool pPool;
	/**
	 * 	记录
	 */
	private Logger logger;
	
	/**
	 *  全参构造器
	 */
	public SingleRecordImpl(DbConnectionPool dbPool, BrowserPool brPool, ProblemPool pPool) {
		this.dbPool = dbPool;
		this.brPool = brPool;
		this.pPool = pPool;
		this.logger = Logger.getLogger(SingleRecordImpl.class);
	}

	/**
	 * 	将标准的SingleRecord对象写入PreparedStatement所代表的的连接中
	 * @param ps 数据库连接的预编译命令PreparedStatement
	 * @param sr 需要写入数据库的SingleRecord引用
	 */
	public void insertSingleRecord(PreparedStatement ps, SingleRecord sr) {
		try {
			/*
			 * 数据填充
			 */
			ps.setObject(1, sr.getSubmitTime());
			ps.setObject(2, sr.getStatus().getCode());
			ps.setObject(3, sr.getScore());
			ps.setObject(4, Integer.parseInt(sr.getProblemId()));
			ps.setObject(5, sr.getLang().getCode());
			ps.setObject(6, sr.getRunTime());
			ps.setObject(7, sr.getAuthor());
			/*
			 * 提交
			 */
			ps.executeUpdate();
		} catch (SQLException e) {
			this.logger.error(e.getMessage(),e);
			this.logger.error("对象写入数据库失败 : " + sr);
		}
	}
	
	/**
	 * 	将browser所代表的的浏览器指向rootUrl所代表的的页面后，等待3秒
	 * @param browser 浏览器的WebDriver对象
	 * @param rootUrl 根页面的url
	 * @throws InterruptedException 线程被打断
	 */
	private void rootPage(WebDriver browser, String rootUrl){
		browser.get(rootUrl);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			this.logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 	尝试在browser所代表的的浏览器的现有页面中找到中英转换button并
	 * @param browser
	 * @throws InterruptedException
	 */
	private void tryEnglishPage(WebDriver browser) {
		if(isWebElementExist(browser, By.linkText("English"))){
			browser.findElement(By.linkText("English")).click();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				this.logger.error(e.getMessage(),e);
			}			
		}
	}
	
	/**
	 * 	判断browser现在指向的页面中是否存在selector所指向的元素
	 * @param browser 浏览器对象
	 * @param selector 需要查找的元素
	 * @return 如果元素存在就返回真
	 */
	private boolean isWebElementExist(WebDriver browser, By selector) {
		try {
			browser.findElement(selector);
			return true;
		}catch (NoSuchElementException e) {
			return false;
		}
	}
	
	/**
	 * 	生成插入数据库的预处理命令
	 * @param conn 数据库的连接
	 * @return 插入命令的预处理命令对象
	 */
	private PreparedStatement buildInsertPreparedStatement(Connection conn) {
		try {
			return conn.prepareStatement("insert into singlerecord values(default, ?, ?, ?, ?, ?, ?, ?);");
		} catch (SQLException e) {			
			this.logger.error("写入数据库失败  : " + conn);
			this.logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	/**
	 *  根据PAT网站上的一行表格数据生成一个SingleRecord对象
	 * @param tds 一行表格数据，包含7个WebElement对象
	 * @return 包装的SingleRecord对象
	 */
	private SingleRecord buildSingleRecordUsingRowCells(List<WebElement> tds) {
		String submitTime 	= DateUtil.patTimeToMysqlTime(tds.get(0).getText());
		Status status 		= Status.getStatusByDesc(tds.get(1).getText());
		short score 		= Short.parseShort(tds.get(2).getText());
		String problemId	= tds.get(3).getText();
		Language lang		= Language.getLanguageByDesc(tds.get(4).getText());
		short runTime		= Short.parseShort((tds.get(5).getText().split(" "))[0]);
		String author		= tds.get(6).getText();
		return new SingleRecord(submitTime, status, score, problemId, lang, runTime, author);
	}
	
	/**
	 * 	尝试在网页上查找“下一页”按钮，并按下
	 * @param browser 需要跳转下一页的浏览器对象
	 * @throws NoSuchElementException 如果下一页按钮没有找到
	 */
	private void nextPage(WebDriver browser) throws NoSuchElementException {
		tryFindElement(browser, browser.getCurrentUrl(), By.className("nextBtn_xlCFd")).click();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			this.logger.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 	尝试使用循环刷新应对阿里云防DDos攻击手段
	 * 	主要用于查找下一页按钮
	 * @param browser 查找元素的页面
	 * @param url 刷新的页面
	 * @param selector 寻找的元素
	 * @return
	 */
	private WebElement tryFindElement(WebDriver browser, String url, By selector) {
		try {
			WebElement element = browser.findElement(selector);
			return element;				
		} catch (NoSuchElementException e) {
		}
		/*
		 * 阿里云的防ddos攻击应对
		 * 首先
		 * 		等待4s
		 * 重复：
		 * 		刷新页面，等待1、3、5、....秒后再寻找元素
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
	
	/**
	 * 	好吧，这就是给查找行内元素用的
	 * @param browser 浏览器页面
	 * @param url 刷新的页面
	 * @param fatherSelector 表格元素
	 * @param childSelector 表格内的行元素
	 * @return
	 */
	private List<WebElement> tryFindElements(WebDriver browser, String url, By fatherSelector, By childSelector){
		try {
			List<WebElement> elements = browser.findElement(fatherSelector).findElements(childSelector);
			return elements;				
		} catch (NoSuchElementException e) {
		}
		/*
		 * 阿里云的防ddos攻击应对
		 * 首先
		 * 		等待4s
		 * 重复：
		 * 		刷新页面，等待1、3、5、....秒后再寻找元素
		 */
		try {
			Thread.sleep( 4000 );
			for(int i=1;i<=4;i++) {
				browser.get(url);
				Thread.sleep( ((i<<1)-1)*1000 );
				try {
					List<WebElement> elements = browser.findElement(fatherSelector).findElements(childSelector);
					return elements;				
				} catch (NoSuchElementException e) {
				}
			}
		} catch (InterruptedException e) {
		}
		throw new NoSuchElementException("NoSuchElementException : " + "url = " + url + " fatherSelector = " + fatherSelector + " childSelector = " + childSelector);
	}
	
	private List<WebElement> tryFindElements(WebElement tr, By selector) {
		for(int i=1;i<=10;i++) {
			try{
				List<WebElement> elements = tr.findElements(selector);
				return elements;
			}catch (NoSuchElementException e) {
			}
			try {
				Thread.sleep(1000);
			}catch (Exception e) {
			}
		}
		throw new NoSuchElementException("Cannot find cell instances : " + selector);
	}
	
	@Override
	public void run() {
		while(true) {
			/*
			 * 获取资源
			 */
			Problem problem = pPool.getProblem();
			WebDriver browser = brPool.getConnection();
			Connection conn = dbPool.getConnection();
			/*
			 * 初始化问题号相关变量
			 */
			String url = problem.getUrl();
			String problemId = problem.getId();
			String oldLatestSubmission = DateUtil.getLatestSubmission(problemId);
			String newLatestSubmission = oldLatestSubmission;
			boolean evaluated = false;
			/*
			 * url池空了
			 */
			if(url == null || browser == null || conn == null)
				break;
			/*
			 * 预处理对象
			 */
			PreparedStatement ps = buildInsertPreparedStatement(conn);
			try {
				/*
				 * 该问题的根页面
				 */
				rootPage(browser, url);
				/*
				 * 尝试转向英文网站
				 */
				tryEnglishPage(browser);
				/*
				 * 对于这个问题号，所有的正确的数据
				 */
				boolean isRunning = true;
				outterIteration : for(int i=1;isRunning;++i) {
					System.out.println("T_NAME:" + Thread.currentThread().getName() + " problemId : " + problemId + " Page"+ i );
					/*
					 * 表格
					 * 如果表格数据不存在，只能退出这个问题记录的循环，即被最外层的try捕获
					 * 抛出的是null pointer exception
					 */
					List<WebElement> trs = tryFindElements(browser, browser.getCurrentUrl(), By.className("main_2UDel"), By.tagName("tr"));
					/*
					 * 表格内的处理多行
					 */
					for(WebElement tr: trs) {
						/*
						 * 处理单行
						 * 对于某个题目，其最后一页的记录上存在“下一页”的按钮，
						 * 但是，很明显，最后一页记录的下一页是不存在任何记录的
						 * 如果出现上述情况，很明显我们希望退出这个问题记录的循环，即被最外层的try捕获
						 */
						List<WebElement> tds = tryFindElements(tr, By.tagName("td"));
						/*
						 * 空列或答案未全部正确，跳过
						 */
						if(tds.isEmpty() || !"Accepted".equals(tds.get(1).getText())) {
							continue;
						}
						/*
						 * 建立单条记录
						 */
						SingleRecord sr = buildSingleRecordUsingRowCells(tds);
						/*
						 * 并且评估该记录的时间
						 */
						if(!evaluated && sr.getSubmitTime().compareTo(oldLatestSubmission) > 0) {
							newLatestSubmission = sr.getSubmitTime();
						}else if(sr.getSubmitTime().compareTo(oldLatestSubmission) <= 0) {
							break outterIteration;
						}
						/*
						 * 将行记录插入数据库
						 */
						insertSingleRecord(ps, sr);
						/*
						 * 存在评估过的记录，即更新为真,加速评估
						 */
						evaluated = true;
					}
					/*
					 * 尝试转向下一页
					 * 如果下一页不存在，则退出这个问题记录的循环，即被最外层的try捕获
					 */
					nextPage(browser);
					/*
					 * 应对阿里云服务器的防DDos措施
					 */
					if(i%5 == 0)
						Thread.sleep(4000);
				}
			}catch(Exception e) {
				System.out.println("T_NAME:" + Thread.currentThread().getName() + " problemId : " + problemId  + " url=" + url);
				logger.info(e.getMessage(), e);
			}finally {
				/*
				 * 更新最新提交时间
				 */
				DateUtil.setLatestSubmission(problemId, newLatestSubmission);
				/*
				 * 永远记得关闭资源
				 */
				browser.close();
				System.out.println("T_NAME:" + Thread.currentThread().getName() + " problemId : " + problemId  + " : completed!");
			}
		}
	}

}
