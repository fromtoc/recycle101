package com.xin;


import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching // 开启缓存使用
@SpringBootApplication
public class Application extends  SpringBootServletInitializer {

	    @Override
	    protected SpringApplicationBuilder configure(
	            SpringApplicationBuilder application) {
	        return application.sources(Application.class);
	    }

	    public static void main(String[] args) {
	        SpringApplication application = new SpringApplication(Application.class);
	        application.setBannerMode(Banner.Mode.OFF);
	        application.run(args);
	    }
	    
	}
//public class Application{
//
//    public static void main(String[] args) {
//        SpringApplication application = new SpringApplication(Application.class);
//        application.setBannerMode(Banner.Mode.OFF);
//        application.run(args);
//    }
//    
//}
