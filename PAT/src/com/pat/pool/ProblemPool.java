package com.pat.pool;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.pat.pojo.Problem;

public class ProblemPool {
	
	private int total;
	
	List<Problem> problems;
	
	public ProblemPool() {
		Properties pros = new Properties();
		try {
			pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("conf.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * ��Ŀ��
		 */
		this.total = Integer.parseInt(pros.getProperty("total"));
		/*
		 * ���е���Ŀ��url
		 */
		problems = new ArrayList<>();
		for(int i=1; i<=total ;i++) {
			String problemId = String.format("%04d", (1000 + i));
			Problem p = new Problem(problemId, pros.getProperty(problemId), null);
			problems.add(p);
		}
	}
	
	/**
	 * 	�ж���Ŀ����url�����Ƿ�������
	 * @return
	 */
	public boolean isEmpty() {
		return problems.isEmpty();
	}
	
	/**
	 * 	����һ����Ŀ��url
	 * @return һ����Ŀ��url
	 */
	public synchronized String getUrl() {
		return getProblem().getUrl();
	}
	
	/**
	 * ����һ����Ŀ�Ķ���
	 * @return һ����Ŀ��url
	 */
	public synchronized Problem getProblem() {
		if(!problems.isEmpty())
			return problems.remove(0);
		else
			return new Problem();
	}
}