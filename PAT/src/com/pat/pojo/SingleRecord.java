package com.pat.pojo;

/**
 *	����PAT��¼
 *	@author xian yuehui
 *	@version 1.0
 */
public class SingleRecord {
	/**
	 *	�ύʱ��
	 */
	private String submitTime;
	/**
	 * 	PAT������������ص���Ŀ״̬
	 */
	private Status status;
	/**
	 * 	����
	 */
	private short score;
	/**
	 * 	��Ŀ��
	 */
	private String problemId;
	/**
	 * 	ʹ�õı������	
	 */
	private Language lang;
	/**
	 * 	�����ʱ
	 */
	private short runTime;
	/**
	 * 	�û�������
	 */
	private String author;
	
	@Override
	public String toString() {
		return "SingleRecord [submitTime=" + submitTime + ", status=" + status.getDesc() + ", score=" + score + ", problemId="
				+ problemId + ", lang=" + lang.getDesc() + ", runTime=" + runTime + ", author=" + author + "]";
	}

	/**
	 * 	ȫ�ι�����
	 * @param submitTime
	 * @param status
	 * @param score
	 * @param problemId
	 * @param lang
	 * @param runTime
	 * @param author
	 */
	public SingleRecord(String submitTime, Status status, short score, String problemId, Language lang, short runTime,
			String author) {
		super();
		this.submitTime = submitTime;
		this.status = status;
		this.score = score;
		this.problemId = problemId;
		this.lang = lang;
		this.runTime = runTime;
		this.author = author;
	}
	
	/**
	 * 	�޲ι�����
	 */
	public SingleRecord() {
		
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public short getScore() {
		return score;
	}

	public void setScore(short score) {
		this.score = score;
	}

	public String getProblemId() {
		return problemId;
	}

	public void setProblemId(String problemId) {
		this.problemId = problemId;
	}

	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	public short getRunTime() {
		return runTime;
	}

	public void setRunTime(short runTime) {
		this.runTime = runTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	
}
