package com.xin.portal.system.util.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.xin.portal.system.model.Datasource;
import com.xin.portal.system.model.Dict;

public class DataSourceDaoUtil {
	
	public static List<Map<String, Object>> getListBySql(Datasource datasource,String sql){//DataSourceInfo
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = JdbcUtil.getConnection(datasource);//DataSourceInfo
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			List<Map<String, Object>> result = JdbcUtil.convertList(rs);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeAll(con, ps, rs);
		}
		return null;
	}
	/**
	 * 获取SQL中的值，全部字段都放到dict中
	 * @param datasource
	 * @param sql
	 * @return
	 */
	public static List<Dict> getListDictBySql(Datasource datasource,String sql){//DataSourceInfo
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = JdbcUtil.getConnection(datasource);//DataSourceInfo
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			List<Dict> result = JdbcUtil.convertToDictValue(rs);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeAll(con, ps, rs);
		}
		return null;
	}
	/**
	 * 获取SQL中的值，只获取第一个和第二个字段最为itemName 和 itemValue 放到dict中
	 * @param datasource
	 * @param sql
	 * @return
	 */
	public static List<Dict> getListValueAsDictBySql(Datasource datasource,String sql){//DataSourceInfo
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = JdbcUtil.getConnection(datasource);//DataSourceInfo
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			List<Dict> result = JdbcUtil.convertToDictValue(rs);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeAll(con, ps, rs);
		}
		return null;
	}
	
	public static Map<String, Object> getMapBySql(Datasource datasource,String sql){//DataSourceInfo
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = JdbcUtil.getConnection(datasource);//DataSourceInfo
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			Map<String, Object> result = JdbcUtil.convertMap(rs);
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeAll(con, ps, rs);
		}
		return null;
	}
	
	public static String checkDatasourceConn(Datasource datasource){//DataSourceInfo
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		try {
			con = JdbcUtil.getConnection(datasource);//DataSourceInfo
			ps = con.prepareStatement("select 1 from dual");
			rs = ps.executeQuery();
			Map<String, Object> result = JdbcUtil.convertMap(rs);
			return result.keySet().iterator().next();
		} catch (SQLException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return e.getMessage();
		} finally {
			closeAll(con, ps, rs);
		}
	}
	
	public static List<Map<String, Object>> getAllTablesOfSource(Datasource datasource){//DataSourceInfo
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		String sql = "select 1 from dual";
		try {
			con = JdbcUtil.getConnection(datasource);//DataSourceInfo
			if(datasource.getDatabaseType() != null && datasource.getDatabaseType() == 1){
				sql = "SELECT table_name as tableName FROM information_schema.tables WHERE table_schema='"+ datasource.getDatabaseName() +"' AND table_type='base table';";
			}else if(datasource.getDatabaseType() != null && datasource.getDatabaseType() == 2){
				sql = "select TABLE_NAME as tableName from all_tables where OWNER = '"+ datasource.getDatabaseUsername().toUpperCase() +"' ORDER BY table_name";
			}
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			return JdbcUtil.convertList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeAll(con, ps, rs);
		}
		return null;
	}
	
	public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(ps != null) {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
}
