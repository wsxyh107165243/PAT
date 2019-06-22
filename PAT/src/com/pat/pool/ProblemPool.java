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
		 * 题目数
		 */
		this.total = Integer.parseInt(pros.getProperty("total"));
		/*
		 * 所有的题目的url
		 */
		problems = new ArrayList<>();
		for(int i=1; i<=total ;i++) {
			String problemId = String.format("%04d", (1000 + i));
			Problem p = new Problem(problemId, pros.getProperty(problemId), null);
			problems.add(p);
		}
	}
	
	/**
	 * 	判断题目集的url集合是否遍历完毕
	 * @return
	 */
	public boolean isEmpty() {
		return problems.isEmpty();
	}
	
	/**
	 * 	返回一个题目的url
	 * @return 一个题目的url
	 */
	public synchronized String getUrl() {
		return getProblem().getUrl();
	}
	
	/**
	 * 返回一个题目的对象
	 * @return 一个题目的url
	 */
	public synchronized Problem getProblem() {
		if(!problems.isEmpty())
			return problems.remove(0);
		else
			return new Problem();
	}
}