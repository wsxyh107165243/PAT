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
 * 	ά��һ����Mysql�е�SingleRecord����صĴ������
 * @author xian yuehui
 * @version 1.0
 */
public class SingleRecordImpl implements Runnable{
	/**
	 * 	���ݿ����ӳ�
	 */
	private DbConnectionPool dbPool;
	/**
	 * 	��������ӳ�
	 */
	private BrowserPool brPool;
	/**
	 *  ��Ŀ��
	 */
	private ProblemPool pPool;
	/**
	 * 	��¼
	 */
	private Logger logger;
	
	/**
	 *  ȫ�ι�����
	 */
	public SingleRecordImpl(DbConnectionPool dbPool, BrowserPool brPool, ProblemPool pPool) {
		this.dbPool = dbPool;
		this.brPool = brPool;
		this.pPool = pPool;
		this.logger = Logger.getLogger(SingleRecordImpl.class);
	}

	/**
	 * 	����׼��SingleRecord����д��PreparedStatement������ĵ�������
	 * @param ps ���ݿ����ӵ�Ԥ��������PreparedStatement
	 * @param sr ��Ҫд�����ݿ��SingleRecord����
	 */
	public void insertSingleRecord(PreparedStatement ps, SingleRecord sr) {
		try {
			/*
			 * �������
			 */
			ps.setObject(1, sr.getSubmitTime());
			ps.setObject(2, sr.getStatus().getCode());
			ps.setObject(3, sr.getScore());
			ps.setObject(4, Integer.parseInt(sr.getProblemId()));
			ps.setObject(5, sr.getLang().getCode());
			ps.setObject(6, sr.getRunTime());
			ps.setObject(7, sr.getAuthor());
			/*
			 * �ύ
			 */
			ps.executeUpdate();
		} catch (SQLException e) {
			this.logger.error(e.getMessage(),e);
			this.logger.error("����д�����ݿ�ʧ�� : " + sr);
		}
	}
	
	/**
	 * 	��browser������ĵ������ָ��rootUrl������ĵ�ҳ��󣬵ȴ�3��
	 * @param browser �������WebDriver����
	 * @param rootUrl ��ҳ���url
	 * @throws InterruptedException �̱߳����
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
	 * 	������browser������ĵ������������ҳ�����ҵ���Ӣת��button��
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
	 * 	�ж�browser����ָ���ҳ�����Ƿ����selector��ָ���Ԫ��
	 * @param browser ���������
	 * @param selector ��Ҫ���ҵ�Ԫ��
	 * @return ���Ԫ�ش��ھͷ�����
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
	 * 	���ɲ������ݿ��Ԥ��������
	 * @param conn ���ݿ������
	 * @return ���������Ԥ�����������
	 */
	private PreparedStatement buildInsertPreparedStatement(Connection conn) {
		try {
			return conn.prepareStatement("insert into singlerecord values(default, ?, ?, ?, ?, ?, ?, ?);");
		} catch (SQLException e) {			
			this.logger.error("д�����ݿ�ʧ��  : " + conn);
			this.logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	/**
	 *  ����PAT��վ�ϵ�һ�б����������һ��SingleRecord����
	 * @param tds һ�б�����ݣ�����7��WebElement����
	 * @return ��װ��SingleRecord����
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
	 * 	��������ҳ�ϲ��ҡ���һҳ����ť��������
	 * @param browser ��Ҫ��ת��һҳ�����������
	 * @throws NoSuchElementException �����һҳ��ťû���ҵ�
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
	 * 	����ʹ��ѭ��ˢ��Ӧ�԰����Ʒ�DDos�����ֶ�
	 * 	��Ҫ���ڲ�����һҳ��ť
	 * @param browser ����Ԫ�ص�ҳ��
	 * @param url ˢ�µ�ҳ��
	 * @param selector Ѱ�ҵ�Ԫ��
	 * @return
	 */
	private WebElement tryFindElement(WebDriver browser, String url, By selector) {
		try {
			WebElement element = browser.findElement(selector);
			return element;				
		} catch (NoSuchElementException e) {
		}
		/*
		 * �����Ƶķ�ddos����Ӧ��
		 * ����
		 * 		�ȴ�4s
		 * �ظ���
		 * 		ˢ��ҳ�棬�ȴ�1��3��5��....�����Ѱ��Ԫ��
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
	 * 	�ðɣ�����Ǹ���������Ԫ���õ�
	 * @param browser �����ҳ��
	 * @param url ˢ�µ�ҳ��
	 * @param fatherSelector ���Ԫ��
	 * @param childSelector ����ڵ���Ԫ��
	 * @return
	 */
	private List<WebElement> tryFindElements(WebDriver browser, String url, By fatherSelector, By childSelector){
		try {
			List<WebElement> elements = browser.findElement(fatherSelector).findElements(childSelector);
			return elements;				
		} catch (NoSuchElementException e) {
		}
		/*
		 * �����Ƶķ�ddos����Ӧ��
		 * ����
		 * 		�ȴ�4s
		 * �ظ���
		 * 		ˢ��ҳ�棬�ȴ�1��3��5��....�����Ѱ��Ԫ��
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
			 * ��ȡ��Դ
			 */
			Problem problem = pPool.getProblem();
			WebDriver browser = brPool.getConnection();
			Connection conn = dbPool.getConnection();
			/*
			 * ��ʼ���������ر���
			 */
			String url = problem.getUrl();
			String problemId = problem.getId();
			String oldLatestSubmission = DateUtil.getLatestSubmission(problemId);
			String newLatestSubmission = oldLatestSubmission;
			boolean evaluated = false;
			/*
			 * url�ؿ���
			 */
			if(url == null || browser == null || conn == null)
				break;
			/*
			 * Ԥ�������
			 */
			PreparedStatement ps = buildInsertPreparedStatement(conn);
			try {
				/*
				 * ������ĸ�ҳ��
				 */
				rootPage(browser, url);
				/*
				 * ����ת��Ӣ����վ
				 */
				tryEnglishPage(browser);
				/*
				 * �����������ţ����е���ȷ������
				 */
				boolean isRunning = true;
				outterIteration : for(int i=1;isRunning;++i) {
					System.out.println("T_NAME:" + Thread.currentThread().getName() + " problemId : " + problemId + " Page"+ i );
					/*
					 * ���
					 * ���������ݲ����ڣ�ֻ���˳���������¼��ѭ��������������try����
					 * �׳�����null pointer exception
					 */
					List<WebElement> trs = tryFindElements(browser, browser.getCurrentUrl(), By.className("main_2UDel"), By.tagName("tr"));
					/*
					 * ����ڵĴ������
					 */
					for(WebElement tr: trs) {
						/*
						 * ������
						 * ����ĳ����Ŀ�������һҳ�ļ�¼�ϴ��ڡ���һҳ���İ�ť��
						 * ���ǣ������ԣ����һҳ��¼����һҳ�ǲ������κμ�¼��
						 * ��������������������������ϣ���˳���������¼��ѭ��������������try����
						 */
						List<WebElement> tds = tryFindElements(tr, By.tagName("td"));
						/*
						 * ���л��δȫ����ȷ������
						 */
						if(tds.isEmpty() || !"Accepted".equals(tds.get(1).getText())) {
							continue;
						}
						/*
						 * ����������¼
						 */
						SingleRecord sr = buildSingleRecordUsingRowCells(tds);
						/*
						 * ���������ü�¼��ʱ��
						 */
						if(!evaluated && sr.getSubmitTime().compareTo(oldLatestSubmission) > 0) {
							newLatestSubmission = sr.getSubmitTime();
						}else if(sr.getSubmitTime().compareTo(oldLatestSubmission) <= 0) {
							break outterIteration;
						}
						/*
						 * ���м�¼�������ݿ�
						 */
						insertSingleRecord(ps, sr);
						/*
						 * �����������ļ�¼��������Ϊ��,��������
						 */
						evaluated = true;
					}
					/*
					 * ����ת����һҳ
					 * �����һҳ�����ڣ����˳���������¼��ѭ��������������try����
					 */
					nextPage(browser);
					/*
					 * Ӧ�԰����Ʒ������ķ�DDos��ʩ
					 */
					if(i%5 == 0)
						Thread.sleep(4000);
				}
			}catch(Exception e) {
				System.out.println("T_NAME:" + Thread.currentThread().getName() + " problemId : " + problemId  + " url=" + url);
				logger.info(e.getMessage(), e);
			}finally {
				/*
				 * ���������ύʱ��
				 */
				DateUtil.setLatestSubmission(problemId, newLatestSubmission);
				/*
				 * ��Զ�ǵùر���Դ
				 */
				browser.close();
				System.out.println("T_NAME:" + Thread.currentThread().getName() + " problemId : " + problemId  + " : completed!");
			}
		}
	}

}
