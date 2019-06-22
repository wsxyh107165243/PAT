package com.pat.util;

import com.pat.tool.PropertyFileTool;

public class DateUtil {
	
	private static PropertyFileTool pros = new PropertyFileTool();
	
	public static String getLatestSubmission(String problemId) {
		return pros.getProperty(problemId + "_latest");
	}
	
	public static void setLatestSubmission(String problemId, String submissionTime) {
		pros.setProperty(problemId + "_latest", submissionTime);
	}
	
	public static String patTimeToMysqlTime(String patTime) {
		String[] s1 = patTime.split(",");
		String[] s2 = s1[0].split("/");
		String year = String.format("%04d", Integer.parseInt(s2[2]));
		String month = String.format("%02d", Integer.parseInt(s2[0]));
		String day = String.format("%02d", Integer.parseInt(s2[1]));
		return year + '/' + month + '/' + day + s1[1];
	}
	
	public static void main(String[] args) {
		System.out.println(patTimeToMysqlTime("5/1/2019, 12:02:22"));
	}
}
