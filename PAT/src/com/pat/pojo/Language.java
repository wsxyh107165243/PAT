package com.pat.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 	Languageö�ٶ���
 * @author xian yuehui
 * @version 1.0
 */
public enum Language {
	CGCC			(0, "C (gcc)"),
	GPLUSPLUS		(1, "C++ (g++)"),
	CLANG 			(2, "C (clang)"),
	CLANGPLUSPLUS	(3, "C++ (clang++)"),
	OPENJDK			(4, "Java (openjdk)"),
	PYTHON2			(5, "Python (python 2)"),
	PYTHON3			(6, "Python (python 3)"),
	RUBY			(7, "Ruby (ruby)"),
	BASH			(8, "Bash (bash)"),
	CAT				(9, "Plaintext (cat)"),
	CLISP			(10, "Common Lisp (clisp)"),
	FPC				(11, "Pascal (fpc)"),
	GO				(12, "Go (go)"),
	GHC				(13, "Haskell (ghc)"),
	LUA				(14, "Lua (lua)"),
	LUAJIT			(15, "Lua (luajit)"),
	MSC				(16, "C# (mcs)"),
	NODE			(17, "JavaScript (node)"),
	OCAMLC			(18, "OCaml (ocamlc)"),
	PHP				(19, "PHP (php)"),
	PERL			(20, "Perl (perl)"),
	AWK				(21, "AWK (awk)"),
	DMD				(22, "D (dmd)"),
	RACKET			(23, "Racket (racket)"),
	VALAC			(24, "Vala (valac)"),
	VBNC			(25, "Visual Basic (vbnc)"),
	KOTLINC			(26, "Kotlin (kotlinc)"),
	SWITFTC			(27, "Swift (swiftc)"),
	OGCC			(28, "Objective-C (gcc)"),
	GFORTRAN		(29, "Fortran95 (gfortran)"),
	OCTAVE			(30, "Octave (octave)"),
	DEFAULT			(31, "No Such Language");
	
	private int code;
	private String desc;
	
	/**
	 * 	Ϊ�˼������ݽ�����ʹ�õ�ͼ�ͱ�
	 */
	private static Map<String, Language> map = null;
	private static List<Language> list = null;
	
	static {
//		�������ʱ����ͼ��desc->Language
		map = new HashMap<String, Language>();
//		�������ʱ������code->Language
		list = new ArrayList<Language>();
		for(Language l : Language.values()) {
			map.put(l.desc, l);
			list.add(l.code, l);
		}
	}
	
	/**
	 * 	������
	 * @param code ������
	 * @param desc ״̬����
	 */
	private Language(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	/**
	 * 	���������뷵������ö�ٶ���
	 * @param code ������
	 * @return ���Զ���,Ĭ�Ϸ��ز�����
	 */
	public static Language getLanguageByCode(int code) {
		if(code>=0 && code<=list.size())
			return list.get(code);
		return Language.DEFAULT;
	}
	
	/**
	 * 	�������Ե������������Ե�ö�ٶ���
	 * @param desc �����Ե�����
	 * @return ���Զ���Ĭ�Ϸ��ز�����
	 */
	public static Language getLanguageByDesc(String desc) {
		if(map.containsKey(desc))
			return map.get(desc);
		return Language.DEFAULT;
	}
	
	/**
	 * 	���������뷵������ö�ٶ��������
	 * @param code ������
	 * @return ���Զ��������,Ĭ�Ϸ��ز�����
	 */
	public static String getDescByCode(int code) {
		return getLanguageByCode(code).desc;
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
