package com.pat.pojo;

/**
 * 	PAT������������ص���Ŀ״̬����POJO
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
	 * 	������
	 * @param code ״̬��
	 * @param desc ״̬����
	 */
	private Status(int code,String desc){
		this.code = code;
		this.desc = desc;
	}
	
	/**	
	 * 	����ת̨�뷵��״̬����
	 * @param code
	 * @return ״̬����
	 */
	public static Status getStatusByCode(int code) {
		for(Status s : Status.values()) {
			if(code == s.code)
				return s;
		}
		return Status.Default;
	}
	
	/**
	 * 	����״̬�����������״̬����
	 * @param desc ״̬�������
	 * @return ״̬����
	 */
	public static Status getStatusByDesc(String desc) {
		for(Status s : Status.values()) {
			if(s.desc.equals(desc))
				return s;
		}
		return Status.Default;
	}
	
	/**
	 * 	����ת̨�뷵��״̬����
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
