/**   
* @Title: DruidConfiguration.java 
* @Package com.xin.portal.system.config 
* @Description: TODO
* @author zhoujun 
* @date 2018年12月21日 下午6:15:53 
* @version V1.0   
*/
package com.xin.portal.system.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/** 
 * @ClassPath: com.xin.portal.system.config.DruidConfiguration 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年12月21日 下午6:15:53 
 */
@Configuration
public class DruidConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(DruidConfiguration.class);
//    @Bean
//    public ServletRegistrationBean<HttpServlet> druidServlet() {
//        ServletRegistrationBean<HttpServlet> reg = new ServletRegistrationBean<HttpServlet>();
//        reg.setServlet(new StatViewServlet());
//        reg.addUrlMappings("/druid/*");
//        reg.addInitParameter("resetEnable","false");
//        return reg;
//    }
//
//    @Bean
//    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
//        FilterRegistrationBean<WebStatFilter> filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>();
//        filterRegistrationBean.setFilter(new WebStatFilter());
//        filterRegistrationBean.addUrlPatterns("/*");
//        Map<String, String> initParams = new HashMap<String, String>();
//        //设置忽略请求
//        initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
//        filterRegistrationBean.setInitParameters(initParams);
//        filterRegistrationBean.addUrlPatterns("/*");
//        return filterRegistrationBean;
//    }

    @Bean(name="druidDataSource")
    @Primary
    @ConfigurationProperties(prefix="spring.datasource.druid")
    public DruidDataSource dataSource(){
    	return DruidDataSourceBuilder.create().build();
    }

    // 配置事物管理器
    @Bean(name="transactionManager")
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }
}
