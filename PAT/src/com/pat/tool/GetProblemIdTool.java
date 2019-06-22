package com.pat.tool;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.pat.pool.BrowserPool;

public class GetProblemIdTool {
	public static void main(String[] args) {
		
		BrowserPool brPool = new BrowserPool(1);
		WebDriver browser = brPool.getConnection();
//		根页面
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties"));
			browser.get(pros.getProperty("patUrl_1001"));
			Thread.sleep(300);
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//定位到下拉框
		WebElement select = browser.findElement(By.className("submissionFilter_2Mxxn")).findElement(By.className("form-control"));
		Select downList = new Select(select);
		//Properties文件
		String configPath = ".\\src\\conf.properties";
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration(configPath);
		} catch (ConfigurationException e1) {
			e1.printStackTrace();
		}
		config.setAutoSave(true);
		for(WebElement e: downList.getOptions()) {
			try {
				Integer.parseInt(e.getText());
				if(!"".equals(e.getText()))
					config.setProperty(e.getText(), "https://pintia.cn/problem-sets/994805342720868352/submissions?" + e.getAttribute("value").replace("-", "="));
			}catch (Exception e1) {
				
			}
		}
		//关闭浏览器
		brPool.closeAll();
	}
}
