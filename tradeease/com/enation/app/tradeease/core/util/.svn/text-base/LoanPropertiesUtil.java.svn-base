package com.enation.app.tradeease.core.util;

import java.util.ResourceBundle;

/**
 * ��ȡ��Դ�ļ��е�Ĭ������
 * 
 * @author yanpeng
 * 
 */
public class LoanPropertiesUtil {
	private static ResourceBundle bundle = ResourceBundle.getBundle("loan");

	/**
	 * ��ȡ��ݿ���
	 * 
	 * @return
	 */
	public static String getJdbcClassName() {
		String value = bundle.getString("className");
		if (value == null) {
			return "com.mysql.jdbc.Driver";
		}
		return value;
	}

	/**
	 * ��ȡ��ݿ�����url
	 * 
	 * @return
	 */
	public static String getJdbcUrl() {
		String value = bundle.getString("jdbc.url");
		if (value == null)
			return "jdbc:mysql://192.168.3.45:3306/ofbiz";
		return value;
	}

	/**
	 * ��ȡ��ݿ�����user
	 * 
	 * @return
	 */
	public static String getJdbcUser() {
		String value = bundle.getString("jdbc.user");
		if (value == null)
			return "root";
		return value;
	}

	/**
	 * ��ȡ��ݿ�����password
	 * 
	 * @return
	 */
	public static String getJdbcPasswd() {
		String value = bundle.getString("jdbc.passwd");
		if (value == null)
			return "123456";
		return value;
	}

	/**
	 * ��ȡ���˷�ʽ
	 * 
	 * @return
	 */
	public static String getProductCode() {
		String value = bundle.getString("productCode");
		if (value == null)
			return "A2";
		return value;
	}

	/**
	 * ��ȡ�û�token
	 * 
	 * @return
	 */
	public static String getToken() {
		String value = bundle.getString("token");
		if (value == null)
			return "";
		return value;
	}

	
	/**
	 * 获取ftp服务器ip地址
	 * @return
	 */
	public static String getFtpIp() {
		String value = bundle.getString("ftpIp");
		if (value == null)
			return "";
		return value;
	}

	/**
	 * 获取ftp服务器密码
	 * @return
	 */
	public static String getFtpUserPwd() {
		String value = bundle.getString("userPwd");
		if (value == null)
			return "";
		return value;
	}

	/**
	 * 获取ftp服务器用户名
	 * @return
	 */
	public static String getFtpUserName() {
		String value = bundle.getString("userName");
		if (value == null)
			return "";
		return value;
	}
	
	/**
	 * 获取ftp服务器端口
	 * @return
	 */
	public static int getFtpPort() {
		String value = bundle.getString("port");
		if (value == null)
			return 0;
		return Integer.parseInt(value);
	}

	/**
	 * 获取ftp服务器文件路径
	 * @return
	 */
	public static String getFtpPath() {
		String value = bundle.getString("path");
		if (value == null)
			return "/";
		return value;
	}

	/**
	 * 获取文件名
	 * @return
	 */
	public static String getFtpFileName() {
		String value = bundle.getString("fileName");
		if (value == null)
			return "";
		return value;
	}

	/**
	 * 获取编码格式
	 * @return
	 */
	public static String getFtpEncoding() {
		String value = bundle.getString("encoding");
		if (value == null)
			return "utf-8";
		return value;
	}

	public static String getRuston() {
		String value = bundle.getString("Ruston");
		if (value == null)
			return "";
		return value;
	}

}
