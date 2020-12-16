package com.digitzones.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.digitzones.constants.Constant;
import com.digitzones.service.IUserService;
/**
 * 判断用户数拦截器
 * @author zdq
 * 2018年10月15日
 */
public class ControllInterceptor implements Filter {
	private IUserService userService;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
		userService = (IUserService)context.getBean("userService");
		Constant.isOpenUserCountFilter = true;
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		int count = userService.queryCount();
		if(count>Constant.USER_COUNT) {
			try {
				resp.sendRedirect(req.getContextPath() + "/userCountExceed.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			chain.doFilter(request, response);
		}
	}
	
	@Override
	public void destroy() {
	}
}
