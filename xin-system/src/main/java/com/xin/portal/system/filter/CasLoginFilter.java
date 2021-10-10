package com.xin.portal.system.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.xin.portal.system.bean.AllBean;
import com.xin.portal.system.model.User;
import com.xin.portal.system.service.impl.UserServiceImpl;

public class CasLoginFilter implements Filter {
	
	private Logger logger = LoggerFactory.getLogger(CasLoginFilter.class);
	
	private UserServiceImpl userSerivce;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		userSerivce = (UserServiceImpl) AllBean.getBean("userServiceImpl");;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, javax.servlet.FilterChain chain)
			throws IOException, ServletException {
		Subject currentUser = SecurityUtils.getSubject();
		if(!currentUser.isAuthenticated()){
			HttpServletRequest req = (HttpServletRequest) request;
			HttpSession session = req.getSession(false);
			try {
				Assertion assertion = (Assertion) (session == null
						? request.getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION)
								: session.getAttribute(AuthenticationFilter.CONST_CAS_ASSERTION));
				if (assertion == null) {
					throw new Exception("单点登录失败!");
					//response 重定向到登录页面
				}
				String loginName = assertion.getPrincipal().getName();
				if (!StringUtils.isEmpty(loginName)) {
				} else {
					throw new Exception("用户名[ " + loginName + " ]不存在");
					//response 重定向到登录页面
				}
				User user = userSerivce.findByUsernameAndTenantIds(loginName, "1");
				if (user != null) {
					UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword(),true,"1");
					currentUser.login(token);
					//SessionUtil.addCookie(user, (HttpServletRequest)request, (HttpServletResponse)response);
				} else {
					throw new Exception("用户名[ " + loginName + " ]不存在");
					//response 重定向到登录页面
				}
				AssertionHolder.setAssertion(assertion);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("单点失败，msg：{}, stack: {}",e.getMessage() , e.getStackTrace());
			} finally {
				AssertionHolder.clear();
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
	
}
