package com.pat.pojo;

/**
 * �������
 * @author xian yuehui
 * @version 1.0
 */
public class Problem {
	/**
	 * 	�����id
	 */
	private String id;
	/**
	 * 	�������ַ
	 */
	private String url;
	/**
	 * 	���ݿ��и����������һ���ύʱ��
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
