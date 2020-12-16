package com.digitzones.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	/**
	 * 身份认证跳转的方法
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/authentication/requireAuthenticate.do")
	@ResponseStatus(code=HttpStatus.UNAUTHORIZED)
	public ModelMap requireAuthenticate(HttpServletRequest request,HttpServletResponse response) throws IOException {
		//用户数超出 系统上限
		/*if(!Constant.isOpenUserCountFilter) {
			throw new RuntimeException(Constant.USER_EXCEED_MSG);
		}*/
		
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		boolean isAjax = false;
		if(savedRequest != null) {
			List<String> requestTypeList = savedRequest.getHeaderValues("X-Requested-With");  //.getHeader("X-Requested-With");
			if(requestTypeList != null && requestTypeList.size()>0) {
				for(String requestType : requestTypeList) {
					if("XMLHttpRequest".equals(requestType)){
						isAjax = true;
						break;
					}
				}
			}
		}
		if(isAjax) {
			ModelMap modelMap = new ModelMap();
			modelMap.addAttribute("content", "该资源需要登录访问!");
			return modelMap;
		}
		redirectStrategy.sendRedirect(request, response, "/index.jsp");
		return null;
	}
}
