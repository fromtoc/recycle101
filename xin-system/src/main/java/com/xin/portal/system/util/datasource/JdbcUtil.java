package com.xin.portal.system.util.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.xin.portal.system.model.Datasource;
import com.xin.portal.system.model.Dict;

public class JdbcUtil {
	
	private String driver;// 驱动地址
	private String url;// 数据库连接串
	private String name;// 账号
	private String password;// 密码
	
	public String getDriver() {
		return driver;
	}
	public String getUrl() {
		return url;
	}
	public String getName() {
		return name;
	}
	public String getPassword() {
		return password;
	}
	public static class Builder {
		
		private static String MYSQL_URL_FORMAT = "jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=utf-8";//jdbc:mysql://localhost:3306/datawindow?useUnicode=true&characterEncoding=utf-8
		private static String ORACLE_URL_FORMAT = "jdbc:oracle:thin:@%s:%s:%s";//jdbc:oracle:thin:@<host>:<port>:<SID>
		//private static String ORACLE_URL_FORMAT = "jdbc:oracle:thin:@//%s:%s/%s";//jdbc:oracle:thin:@//<host>:<port>/<service_name>
		
		private static String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
		private static String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
		
		private String driver;// 驱动地址
		private String url;// ip地址
		private Integer isCustom; // 0 预定（系统拼接URL）； 1自定义（自己指定URL）
		private String ip;
		private String port;
		private String sourceName;
		private Integer sourceType;
		private String name;// 账号
		private String password;// 密码
		
		public Builder ip(String ip) {
			this.ip = ip;
			return this;
		}
		public Builder sourceName(String sourceName) {
			this.sourceName = sourceName;
			return this;
		}
		public Builder sourceType(Integer sourceType) {
			this.sourceType = sourceType;
			return this;
		}
		public Builder port(String port) {
			this.port = port;
			return this;
		}
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		public Builder isCustom(Integer isCustom) {
			this.isCustom = isCustom;
			return this;
		}
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		public JdbcUtil build() {
			//配置url
			buildDriver();
			buildUrl();
			return new JdbcUtil(this);
		}
		private Builder buildDriver() {
			String buildDriver = "";
			switch(sourceType){
				case 1: //mysql
					buildDriver = MYSQL_DRIVER;
					break;
				case 2: //oracle
					buildDriver = ORACLE_DRIVER;
					break;
				default:
					buildDriver = MYSQL_DRIVER;
					break;
			}
			this.driver = buildDriver;
			return this;
		}
		private Builder buildUrl(){
			String buildUrl = "";
			if(isCustom == 1){//自定义
				buildUrl = ip;
			}else{//预定义
				switch(sourceType){
				case 1: //mysql
					buildUrl = String.format(MYSQL_URL_FORMAT, this.ip, this.port, this.sourceName);
					break;
				case 2: //oracle
					buildUrl = String.format(ORACLE_URL_FORMAT, this.ip, this.port, this.sourceName);
					break;
				default:
					buildUrl = String.format(MYSQL_URL_FORMAT, this.ip, this.port, this.sourceName);
					break;
				}
			}
			this.url = buildUrl;
			return this;
		}
		
	}
	
	private JdbcUtil(Builder builder) {
		this.driver = builder.driver;
		this.url = builder.url;
		this.name = builder.name;
		this.password = builder.password;
	}
	
	private Connection getConn() throws SQLException,ClassNotFoundException {
		Class.forName(driver);//反射注册数据库驱动
		return DriverManager.getConnection(url, name, password);
	}
	
	public static Connection getConnection(Datasource datasource) throws SQLException, ClassNotFoundException{//DataSourceInfo  判断空
		return new Builder().ip(datasource.getDatabasePath())
				.port(datasource.getDatabasePort())
				.sourceName(datasource.getDatabaseName())
				.isCustom(datasource.getIsCustom())
				.sourceType(datasource.getDatabaseType())
				.name(datasource.getDatabaseUsername())
				.password(datasource.getDatabasePassword()).build().getConn();
	}
	
	public static void main(String[] args) {
		Connection con = null;
		try {
			con = new Builder().ip("192.168.1.190")
					.port("3306")
					.sourceName("dataondemand-uat-sn")
					.sourceType(1)
					.name("root")
					.password("root").build().getConn();
			String sql = "SELECT id AS id , username AS VALUE FROM t_user ";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			List<Dict> list = convertToDictValue(rs);
			for (int i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getItemName()+"--"+list.get(i).getItemValue());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(con!=null){
					con.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	public static List<Map<String, Object>> convertList(ResultSet rs) {
	    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	    try {
	        ResultSetMetaData md = rs.getMetaData();
	        int columnCount = md.getColumnCount();
	        while (rs.next()) {
	            Map<String, Object> rowData = new LinkedHashMap<String, Object>();
	            for (int i = 1; i <= columnCount; i++) {
	                rowData.put(md.getColumnName(i), rs.getObject(i));
	            }
	            list.add(rowData);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null)
	            rs.close();
	            rs = null;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return list;
	}
	
	public static Map<String, Object> convertMap(ResultSet rs){
	    Map<String, Object> map = new TreeMap<String, Object>();
	    try{
	        ResultSetMetaData md = rs.getMetaData();
	        int columnCount = md.getColumnCount();
	        while (rs.next()) {
	            for (int i = 1; i <= columnCount; i++) {
	                map.put(md.getColumnName(i), rs.getObject(i));
	            }
	        }
	    } catch (SQLException e){
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null)
	            rs.close();
	            rs = null;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return map;
	}
	public static List<Dict> convertToDict(ResultSet rs) {
		List<Dict> list = new ArrayList<Dict>();
	    try {
	        ResultSetMetaData md = rs.getMetaData();
	        int columnCount = md.getColumnCount();
	        while (rs.next()) {
	        	for (int i = 1; i <= columnCount; i++) {
	        		Dict rowData = new Dict();
	        		rowData.setItemName(md.getColumnName(i));
	        		rowData.setItemValue(rs.getString(i));
	        		list.add(rowData);
	        	}
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null)
	            rs.close();
	            rs = null;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return list;
	}
	
	public static List<Dict> convertToDictValue(ResultSet rs) {//第一个值为key，第二个值为显示name
		List<Dict> list = new ArrayList<Dict>();
	    try {
	    	ResultSetMetaData md = rs.getMetaData();
	        int columnCount = md.getColumnCount();
	        while (rs.next()) {
	        	Dict dict = new Dict();
	        	if (columnCount == 1) {
	        		dict.setItemName(md.getColumnName(1));
	        		dict.setItemValue(rs.getString(1));
	        		list.add(dict);
	        	}else if(columnCount > 1) {
	            	dict.setItemName(rs.getString(2));
	            	dict.setItemValue(rs.getString(1));
		            list.add(dict);
	        	}
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null)
	            rs.close();
	            rs = null;
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    return list;
	}
	
}
