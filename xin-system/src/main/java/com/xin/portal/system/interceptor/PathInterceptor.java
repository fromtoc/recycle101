package com.xin.portal.system.interceptor;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.mapper.ConfigMapper;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassPath: com.xin.portal.system.interceptor.PathInterceptor 
 * @Description: TODO
 * @author zj
 * @date 2017-7-13 下午2:39:41
 */
public class PathInterceptor  implements HandlerInterceptor {
	
	private static Logger logger = LoggerFactory.getLogger(PathInterceptor.class);
	
	@Autowired
    private CacheManager cacheManager;

	@Autowired
	private ConfigMapper configMapper;


	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		Object cacheRecord = cacheManager.getCache(Constant.CACHE_DEFAULT).get(Constant.ConfigKeys.SYS_THEME);
		if (cacheRecord==null) {
			EntityWrapper<Config> ew = new EntityWrapper<>();
			List<Config> records = configMapper.selectList(ew);
			Cache cache = cacheManager.getCache(Constant.CACHE_DEFAULT);
			for (Config record : records) {
				cache.put(record.getCode(), record.getValue());
				logger.info("put cache [{}]:[{}]",record.getCode(),record.getValue());

			}
		}
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		setPath(request);
		return true;
	}
	

	 private void setPath(HttpServletRequest request){
		 String scheme = request.getScheme();
		 String serverName = request.getServerName();
		 int port = request.getServerPort();
		 String path = request.getContextPath();
		 String basePath = scheme + "://" + serverName + ":" + port + path;
		 request.setAttribute("basePath", basePath);
		 request.setAttribute("staticPath", basePath);
		 
//		 request.setAttribute(ConfigKeys.SYS_LOGO,cacheManager.getCache(Constant.CACHE_DEFAULT).get(ConfigKeys.SYS_LOGO,String.class));
//		 request.setAttribute(ConfigKeys.SYS_NAME,cacheManager.getCache(Constant.CACHE_DEFAULT).get(ConfigKeys.SYS_NAME,String.class));
//		 request.setAttribute(ConfigKeys.SYS_COPYRIGHT,cacheManager.getCache(Constant.CACHE_DEFAULT).get(ConfigKeys.SYS_COPYRIGHT,String.class));
	 }
}