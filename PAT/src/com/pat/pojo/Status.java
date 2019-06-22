package com.pat.pojo;

/**
 * 	PAT判题服务器返回的题目状态对象POJO
 * @author xian yuehui
 * @version 1.0
 */
public enum Status {
	Accepted			(0, "Accepted"),
	PartiallyAccepted	(1, "Partially Accepted"),
	WrongAnswer			(2, "Wrong Answer"),
	CompileError		(3, "Compile Error"),
	MultipleErrors		(4, "Multiple Errors"),
	NonZeroExitCode		(5, "Non-zero Exit Code"),
	Default				(6, "No Such Status");
	
	private int code;
	private String desc;
	
	/**
	 * 	构造器
	 * @param code 状态码
	 * @param desc 状态描述
	 */
	private Status(int code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
	/**	
	 * 	根据转台码返回状态对象
	 * @param code
	 * @return 状态对象
	 */
	public static Status getStatusByCode(int code) {
		for(Status s : Status.values()) {
			if(code == s.code)
				return s;
		}
		return Status.Default;
	}
	
	/**
	 * 	根据状态码的描述返回状态对象
	 * @param desc 状态码的描述
	 * @return 状态对象
	 */
	public static Status getStatusByDesc(String desc) {
		for(Status s : Status.values()) {
			if(s.desc.equals(desc))
				return s;
		}
		return Status.Default;
	}
	
	/**
	 * 	根据转台码返回状态对象
	 * @param code
	 * @return
	 */
	public static String getDescByCode(int code) {
		return getStatusByCode(code).desc;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
}
