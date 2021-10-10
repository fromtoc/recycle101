package com.xin.portal.system.config.quartz;

import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.utils.ConnectionProvider;

import com.alibaba.druid.pool.DruidDataSource;
/**
 * @ClassPath: com.xin.portal.system.config.quartz.MyConnectionProvider 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年4月9日 上午10:12:43
 */
public class MyConnectionProvider implements ConnectionProvider{

    private DruidDataSource dataSource;
	  
    /** 
     * 无参构造，必须要有[没有其他构造的话也可以不写] 
     */  
    public MyConnectionProvider(DruidDataSource dataSource) {  
    	this.dataSource = dataSource;
    }  
  
    public Connection getConnection() throws SQLException {  
        return dataSource.getConnection();  
    }  
  
    public void shutdown() throws SQLException {
    	dataSource = null;
  
    }  
	@Override
	public void initialize() throws SQLException {
		
		
	}

}
