package com.digitzones.filters;

import com.auth0.jwt.interfaces.Claim;
import com.digitzones.controllers.RoleController;
import com.digitzones.util.JwtTokenUnit;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;





/**
 * token拦截器
 */
public class TokenInterceptor implements HandlerInterceptor{
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		response.setCharacterEncoding("utf-8");
		String token =request.getHeader("accessToken");
		
		HttpServletResponse httpResponse=(HttpServletResponse) response;
		httpResponse.setCharacterEncoding("UTF-8");
		httpResponse.setContentType("application/json;charset=UTF-8");
		//token存在
		if(null!=token) {
			//验证token是否正确
			boolean result=JwtTokenUnit.tokenTrue(token);
			if(result) {
				Map<String, Claim> claims = JwtTokenUnit.verifyToken(token);
				Claim user_username_claim = claims.get("username");
				String username =String.valueOf(user_username_claim.asString());
				Set<String> reLoginUser=RoleController.reLoginUser;
				if(reLoginUser.contains(username)){
					String json = "{\"statusCode\":\"401\",\"msg\":\"权限更改，请重新登录\"}";
					httpResponse.getWriter().write(json);
					return false;
				} 
				return true;
			}
		}
		String json = "{\"statusCode\":\"401\",\"msg\":\"验证失效，请重新登录\"}";
		httpResponse.getWriter().write(json);
		return false;
	}
}
