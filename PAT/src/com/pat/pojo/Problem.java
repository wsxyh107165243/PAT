package com.pat.pojo;

/**
 * 问题对象
 * @author xian yuehui
 * @version 1.0
 */
public class Problem {
	/**
	 * 	问题的id
	 */
	private String id;
	/**
	 * 	问题的网址
	 */
	private String url;
	/**
	 * 	数据库中该问题的最新一次提交时间
	 * 	"2019/05/01 12:02:22"
	 */
	private String latestRecordTime;
	
	public Problem(String id, String url, String latestRecordTime) {
		super();
		this.id = id;
		this.url = url;
		this.latestRecordTime = latestRecordTime;
	}
	
	public Problem() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLatestRecordTime() {
		return latestRecordTime;
	}

	public void setLatestRecordTime(String latestRecordTime) {
		this.latestRecordTime = latestRecordTime;
	}

	@Override
	public String toString() {
		return "Problem [id=" + id + ", url=" + url + ", latestRecordTime=" + latestRecordTime + "]";
	}

}
