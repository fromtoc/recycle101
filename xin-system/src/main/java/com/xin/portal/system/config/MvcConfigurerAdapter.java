package com.xin.portal.system.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xin.portal.system.interceptor.LicenseInterceptor;
import com.xin.portal.system.interceptor.LineBotServerInterceptor;
import com.xin.portal.system.interceptor.PathInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassPath: com.xin.portal.system.config.MvcConfigurerAdapter 
 * @Description: TODO
 * @author zj
 * @date 2017-7-13 下午2:41:41
 */
@Configuration
public class MvcConfigurerAdapter extends WebMvcConfigurationSupport {
	
	private static Logger logger = LoggerFactory.getLogger(MvcConfigurerAdapter.class);
	
	@Autowired
    protected CacheManager cacheManager;
//	  @Autowired//LIne功能注释
//    private LineBotServerInterceptor lineBotServerInterceptor;
//    @Autowired
//    private LineBotServerArgumentProcessor lineBotServerArgumentProcessor;
	
	
    public void addCorsMappings(CorsRegistry registry) {  
        registry.addMapping("/**")  
                .allowedOrigins("*")  
                .allowCredentials(true)  
                .allowedMethods("GET", "POST", "DELETE", "PUT")  
                .maxAge(3600);  
    } 

	@Bean
	public PathInterceptor pathInterceptor() {
		return new PathInterceptor();
	}
	
	@Bean
	public LicenseInterceptor licenseInterceptor() {
		return new LicenseInterceptor();
	}
	
//	@Bean("oauth2Authc")//LIne功能注释
//    public OAuth2AuthenticationFilter remoteIpFilter() {
//        return new OAuth2AuthenticationFilter();
//    }
	

	/**
	 * 增加拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(lineBotServerInterceptor);//LIne功能注释
		registry.addInterceptor(pathInterceptor()).addPathPatterns("/**");
		registry.addInterceptor(licenseInterceptor()).addPathPatterns("/**")
		.excludePathPatterns("/error","/errors/**","/js/**","/css/**","/plugins/**","/activate","/activate/**","/license/**");
	}

//	@Override//LIne功能注释
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//        argumentResolvers.add(lineBotServerArgumentProcessor);
//    }

	@Bean
    public HttpMessageConverters responseBodyConverter(List<HttpMessageConverter<?>> converters) {
		StringHttpMessageConverter stringConverter= new StringHttpMessageConverter();
        stringConverter.setDefaultCharset(Charset.forName("UTF-8"));
        converters.add(stringConverter);
        
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        //日期格式化
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        MediaType text_plain = new MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"));
        MediaType text_html = new MediaType(MediaType.TEXT_HTML, Charset.forName("UTF-8"));
        MediaType x_www_form_urlencoded_utf8 = new MediaType(MediaType.APPLICATION_FORM_URLENCODED, Charset.forName("UTF-8"));
        supportedMediaTypes.add(text_html);
        supportedMediaTypes.add(text_plain);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        supportedMediaTypes.add(x_www_form_urlencoded_utf8);

        fastConverter.setSupportedMediaTypes(supportedMediaTypes);

        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
        return new HttpMessageConverters(converters);
    }
	
	
	/*@Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/resources/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
        String[] uploadPathArr = new String[2];
    	String upPath = (new StringBuilder("file:")).append(getServletContext().getRealPath("upload")).append(File.separator).toString();
    	uploadPathArr[0]=upPath.replace("\\", "/");
    	String applicationPath = getServletContext().getRealPath("/");
    	String webappPath,path;
    	if (applicationPath.endsWith("/")) {
    		webappPath=applicationPath.substring(0, applicationPath.lastIndexOf("/"));
    		path = webappPath.substring(0, webappPath.lastIndexOf("/"));
        }else{
        	webappPath = applicationPath.substring(0, applicationPath.lastIndexOf("\\"));
        	path = webappPath.substring(0, webappPath.lastIndexOf("\\"));
        }
//    	registry.addResourceHandler(new String[] {"/upload/**"}).addResourceLocations(new String[] {"file:"+path + File.separator + "upload"+ File.separator.replace("\\", "/")});
    	
    	uploadPathArr[1]=("file:"+path + File.separator + "upload"+ File.separator).replace("\\", "/");
    	//System.out.println("uploadPathArr[1]:"+uploadPathArr[1]);
    	for (String p : uploadPathArr) {
    		logger.info("===upload path: {} ",p);
    		
    	}
    	registry.addResourceHandler(new String[] {"/upload/**"}).addResourceLocations(uploadPathArr);
        super.addResourceHandlers(registry);
    }*/
	
	@Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/resources/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/public/");
    	registry.addResourceHandler(new String[] {"/upload/**"}).addResourceLocations(getResourceLocation("upload"));
    	registry.addResourceHandler(new String[] {"/wxPushFile/**"}).addResourceLocations(getResourceLocation("wxPushFile"));
        super.addResourceHandlers(registry);
    }
	
	private String[] getResourceLocation(String type){
		String[] resourcePathArr = new String[2];
		String upPath = (new StringBuilder("file:")).append(getServletContext().getRealPath(type)).append(File.separator).toString();
		resourcePathArr[0] = upPath.replace("\\", "/");
    	String applicationPath = getServletContext().getRealPath("/");
    	String webappPath,path;
    	if (applicationPath.endsWith("/")) {
    		webappPath=applicationPath.substring(0, applicationPath.lastIndexOf("/"));
    		path = webappPath.substring(0, webappPath.lastIndexOf("/"));
        }else{
        	webappPath = applicationPath.substring(0, applicationPath.lastIndexOf("\\"));
        	path = webappPath.substring(0, webappPath.lastIndexOf("\\"));
        }
    	resourcePathArr[1]=("file:"+path + File.separator + type + File.separator).replace("\\", "/");
    	for (String p : resourcePathArr) {
    		logger.info("=== {} path: {} ", path, p);
    	}
    	return resourcePathArr;
	}

    @Bean
    public LocaleResolver localeResolver() {
        return new NativeLocaleResolver();
    }
    
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(getHandlerExceptionResolver());
    }
    
    @Bean
	public MultipartConfigElement multipartConfigElement(
			@Value("${multipart.maxFileSize}") String maxFileSize,
			@Value("${multipart.maxRequestSize}") String maxRequestSize) {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 单个文件最大
		factory.setMaxFileSize(maxFileSize);
		// 设置总上传数据总大小
		factory.setMaxRequestSize(maxRequestSize);
		return factory.createMultipartConfig();
	}
    
    /**
     * 创建异常处理
     * @return
     */
    private HandlerExceptionResolver getHandlerExceptionResolver(){
        HandlerExceptionResolver handlerExceptionResolver = new HandlerExceptionResolver(){
            @Override
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
                                                 Object handler, Exception e) {
                return new ModelAndView(getResuleByHeandleException(request, handler, e));
            }
        };
        return handlerExceptionResolver;
    }
    
    /**
     * 根据异常类型确定返回数据
     * @param request
     * @param handler
     * @param e
     * @return
     */
    private String getResuleByHeandleException(HttpServletRequest request, Object handler, Exception e){
        String message;
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s", request.getRequestURI(),
                    handlerMethod.getBean().getClass().getName(), handlerMethod.getMethod() .getName(), e.getMessage());
        } else {
            message = e.getMessage();
        }
        e.printStackTrace();
        logger.error(message, e);
//        if (e instanceof RuntimeException ) {
//        	return BaseApi.error(HttpStatus.OK.value(), e.getMessage());
//        }
//        if (e instanceof NoHandlerFoundException) {
//        	return BaseApi.error(HttpStatus.NOT_FOUND.value(), "接口 [" + request.getRequestURI() + "] 不存在");
//        }
        request.setAttribute("msg", e.getMessage());
        return "errors/500";
    }

}
