package com.pat.pojo;

/**
 * 	和properties文件一一对应
 * @author xian yuehui
 * @version 1.0
 */
public class Configuration {
	private String chromeDriver;
	private String patUrl;
	private String dbDriver;
	private String dbUrl;
	private String user;
	private String pwd;
	public Configuration(String chromeDriver, String patUrl, String dbDriver, String dbUrl, String user, String pwd) {
		super();
		this.chromeDriver = chromeDriver;
		this.patUrl = patUrl;
		this.dbDriver = dbDriver;
		this.dbUrl = dbUrl;
		this.user = user;
		this.pwd = pwd;
	}
	public Configuration() {
		super();
	}
	public String getChromeDriver() {
		return chromeDriver;
	}
	public void setChromeDriver(String chromeDriver) {
		this.chromeDriver = chromeDriver;
	}
	public String getPatUrl() {
		return patUrl;
	}
	public void setPatUrl(String patUrl) {
		this.patUrl = patUrl;
	}
	public String getDbDriver() {
		return dbDriver;
	}
	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	
}
