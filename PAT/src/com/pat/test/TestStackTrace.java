package com.pat.test;

import java.sql.Connection;

import org.apache.log4j.Logger;

public class TestStackTrace {
	@SuppressWarnings("null")
	public static void main(String[] args) {
		Logger logger = Logger.getLogger(TestStackTrace.class);
		Connection conn = null;
		try {
			conn.close();
		}catch (Exception e) {
			/*
			 * 将错误的堆栈信息打印到log4j中
			 */
			logger.info(e.getMessage(),e);
		}
	}
}
