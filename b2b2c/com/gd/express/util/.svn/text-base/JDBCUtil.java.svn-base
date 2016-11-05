package com.gd.express.util;

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
import java.util.Set;

import com.gd.express.exception.BaseException;

/**
 * JDBC
 * 
 * @author zks
 * 
 */
public class JDBCUtil {
	private static String className = PropertiesUtil.getJdbcClassName();
	private static String url = PropertiesUtil.getJdbcUrl();
	private static String user = PropertiesUtil.getJdbcUser();
	private static String password = PropertiesUtil.getJdbcPasswd();

	/**
	 * Connection
	 * 
	 * @return
	 */
	public static Connection getCon() {
		Connection con = null;
		try {
			Class.forName(className);
			con = DriverManager.getConnection(url, user, password);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;

	}

	/**
	 * 
	 * 
	 * @param con
	 * @param stmt
	 * @throws BaseException
	 */
	private static void close(Connection con, PreparedStatement stmt)
			throws BaseException {
		try {
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		}

	}

	/**
	 * 
	 * 
	 * @param sql
	 * @return Map
	 * @throws BaseException
	 */
	public static Map<String, Object> queryForMap(String sql)
			throws BaseException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			con = getCon();
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columns; i++) {
					map.put(metaData.getColumnName(i), rs.getObject(i));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		} finally {
			close(con, stmt);
		}
		return map;
	}

	/**
	 * 
	 * 
	 * @param sql
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> queryForMap(String sql,
			Map<String, Object> condition) throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (condition == null || condition.isEmpty()) {
				throw new BaseException("sql");
			}
			con = getCon();
			stmt = con.prepareStatement(sql);
			Set<String> set = condition.keySet();
			for (String key : set) {
				String value = (String) condition.get(key);
				if ("java.lang.Integer".equalsIgnoreCase(value.getClass()
						.getName())) {
					stmt.setInt(Integer.parseInt(key), Integer.parseInt(value));
					continue;
				}
				stmt.setString(Integer.parseInt(key), value);
			}
			rs = stmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();
			while (rs.next()) {
				for (int i = 1; i <= columns; i++) {
					map.put(metaData.getColumnName(i), rs.getObject(i));
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		} catch (BaseException e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		} finally {
			close(con, stmt);
		}
		return map;
	}

	/**
	 * 
	 * 
	 * @param sql
	 * @return List<Map<String, Object>>
	 * @throws BaseException
	 */
	public static List<Map<String, Object>> queryForList(String sql)
			throws BaseException {
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			con = getCon();
			stmt = con.prepareStatement(sql);
			rs = stmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i <= columns; i++) {

					map.put(metaData.getColumnName(i), rs.getObject(i));
				}
				list.add(map);
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		} finally {
			close(con, stmt);
		}
		return list;
	}

	/**
	 * 
	 * 
	 * @param sql
	 * @param condition
	 * @throws BaseException
	 */
	public static List<Map<String, Object>> queryForList(String sql,
			Map<String, Object> condition) throws BaseException {
		Connection con = null;
		PreparedStatement stmt = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		try {
			con = getCon();
			stmt = con.prepareStatement(sql);
			ResultSet rs = null;

			if (condition != null && !condition.isEmpty()) {
				Set<String> set = condition.keySet();
				for (String key : set) {
					String value = (String) condition.get(key);
					if ("java.lang.Integer".equalsIgnoreCase(value.getClass()
							.getName())) {
						stmt.setInt(Integer.parseInt(key),
								Integer.parseInt(value));
						continue;
					}
					stmt.setString(Integer.parseInt(key), value);
				}

				rs = stmt.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				int columns = metaData.getColumnCount();

				while (rs.next()) {
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 1; i <= columns; i++) {
						map.put(metaData.getColumnName(i), rs.getObject(i));
					}
					list.add(map);
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		} finally {
			close(con, stmt);
		}
		return list;
	}

	public static Object queryForObject(String sql, Object obj) {
		return null;
	}

	public static int update(String sql) {

		return 10;
	}

	/**
	 * 
	 * 
	 * @param sql
	 * @param condition
	 * @return i
	 * @throws Exception
	 */
	public static int update(String sql, Map<String, Object> condition)
			throws Exception {
		Connection con = null;
		PreparedStatement stmt = null;
		int i = 0;
		try {
			con = getCon();
			stmt = con.prepareStatement(sql);
			if (condition != null && !condition.isEmpty()) {
				Set<String> set = condition.keySet();
				for (String key : set) {
					String value = (String) condition.get(key);
					if ("java.lang.Integer".equalsIgnoreCase(value.getClass()
							.getName())) {
						stmt.setInt(Integer.parseInt(key),
								Integer.parseInt(value));
						continue;
					}
					stmt.setString(Integer.parseInt(key), value);
				}
			}
			i = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		} finally {
			close(con, stmt);
		}
		return i;

	}

	public static int update(Object obj) {

		return 10;
	}

	public static void insert(String sql, Map<String, Object> condition)
			throws Exception {
		update(sql, condition);
	}

	/**
	 * 
	 * @param con
	 * @param sql
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public static int update(Connection con, String sql,
			Map<String, Object> condition) throws Exception {
		PreparedStatement stmt = null;
		int i = 0;
		try {
			stmt = con.prepareStatement(sql);
			if (condition != null && !condition.isEmpty()) {
				Set<String> set = condition.keySet();
				for (String key : set) {
					String value = (String) condition.get(key);
					if ("java.lang.Integer".equalsIgnoreCase(value.getClass()
							.getName())) {
						stmt.setInt(Integer.parseInt(key),
								Integer.parseInt(value));
						continue;
					}
					stmt.setString(Integer.parseInt(key), value);
				}
			}
			i = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException("系统错误");
		}
		return i;

	}

}
