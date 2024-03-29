package com.xin.portal.system.config.quartz;

import java.io.IOException;
import java.util.Properties;

import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
@EnableScheduling
@ConditionalOnProperty(name = "system.quartz.enabled")
public class QuartzConfig  {
	
	@Autowired
	private MyJobFactory myJobFactory;
	
    @Bean(name="SchedulerFactory")
    public SchedulerFactoryBean schedulerFactoryBean(DruidDataSource dataSource) throws IOException {
    	
//    	DBConnectionManager.getInstance().addConnectionProvider(
//    			"druidDataSource",
//                new MyConnectionProvider(dataSource)
//        );
    	
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setStartupDelay(10);
        factory.setJobFactory(myJobFactory);
        factory.setAutoStartup(true);
        factory.setDataSource(dataSource);
        factory.setQuartzProperties(quartzProperties());
        return factory;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();
        Properties quartzProp = propertiesFactoryBean.getObject();
        return quartzProp;
    }
  
    /*
     * quartz初始化监听器
     */
//    @Bean
//    public QuartzInitializerListener executorListener() {
//       return new QuartzInitializerListener();
//    }
    
//    /*
//     * 通过SchedulerFactoryBean获取Scheduler的实例
//     */
//    @Bean(name="Scheduler")
//    public Scheduler scheduler(@Qualifier("druidDataSource") DruidDataSource dataSource) throws IOException {
//        return schedulerFactoryBean(dataSource).getScheduler();
//    }
}
