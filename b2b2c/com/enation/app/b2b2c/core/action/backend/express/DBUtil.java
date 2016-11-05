package com.enation.app.b2b2c.core.action.backend.express;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtil {
	private static String className = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://localhost:3306/ofbiz";
	private static String user = "root";
	private static String password = "123456";

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(className);
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void realseConn(Connection conn, PreparedStatement pstmt,
			ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static List<Map<String, Object>> queryAll(String sql,
			Map<Integer, Object> conditionMap) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		try {
			conn = getConnection();
			pstmt = conn.prepareStatement(sql);

			if (conditionMap != null && conditionMap.size() != 0) {

				int paramNum = conditionMap.size();
				for (int i = 1; i <= paramNum; i++) {

					Object paramValue = conditionMap.get(i);

					if ("java.lang.Integer".equalsIgnoreCase(paramValue
							.getClass().getName())) {
						pstmt.setInt(i, Integer.parseInt(paramValue.toString()));
					} else if ("java.lang.String".equalsIgnoreCase(paramValue
							.getClass().getName())) {
						pstmt.setString(i, paramValue.toString());
					}
				}
			}

			rs = pstmt.executeQuery();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnNum = rsmd.getColumnCount();
			while (rs.next()) {
				Map<String, Object> dataMap = new HashMap<String, Object>(0);
				for (int i = 1; i <= columnNum; i++) {
					dataMap.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				resultList.add(dataMap);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			realseConn(conn, pstmt, rs);
		}

		return resultList;
	}

	public static void main(String[] args) {
		test1();
		test2();
		test3();
	}

	public static void test1() {
		String sql = "select * from test where id=? and name=?";
		Map<Integer, Object> conditionMap = new HashMap<Integer, Object>();
		conditionMap.put(1, 5);
		conditionMap.put(2, "e");
		List<Map<String, Object>> resultList = DBUtil.queryAll(sql,
				conditionMap);
		//System.out.println(resultList);
	}

	public static void test2() {
		String sql = "select * from user where id=? and username=?";
		Map<Integer, Object> conditionMap = new HashMap<Integer, Object>();
		conditionMap.put(1, 1);
		conditionMap.put(2, "admin");
		List<Map<String, Object>> resultList = DBUtil.queryAll(sql,
				conditionMap);
		//System.out.println(resultList);
	}

	public static void test3() {
		String sql = "select * from user";
		List<Map<String, Object>> resultList = DBUtil.queryAll(sql, null);
		//System.out.println(resultList);
	}
}