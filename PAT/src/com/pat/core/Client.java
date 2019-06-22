package com.pat.core;

import com.pat.pool.BrowserPool;
import com.pat.pool.DbConnectionPool;
import com.pat.pool.ProblemPool;

public class Client {
	public static void main(String[] args) {
//		��Դ��
		BrowserPool brPool = new BrowserPool();
		DbConnectionPool dbPool = new DbConnectionPool();
		ProblemPool pPool = new ProblemPool();
//		4�߳�
		for(int i=0; i<4; i++) {
			new Thread(new SingleRecordImpl(dbPool, brPool, pPool), Integer.toString(i)).start();
//			��ʱ���������ؾ���
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
