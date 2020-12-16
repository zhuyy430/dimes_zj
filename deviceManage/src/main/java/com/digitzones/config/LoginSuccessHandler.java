package com.digitzones.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.digitzones.model.AppClientMap;
import com.digitzones.service.IAppClientMapService;
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private IAppClientMapService appClientMapService;
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String username = authentication.getName();
		String clientId = request.getParameter("clientid");
		AppClientMap appClientMap = appClientMapService.queryByProperty("username", username);
		if(appClientMap != null) {
			if(clientId!=null && !"".equals(clientId.trim())) {
				if(!clientId.equals(appClientMap.getCid())) {
					appClientMap.setCid(clientId);

					appClientMapService.updateObj(appClientMap);
				}
			}
		}else {
			if(!StringUtils.isEmpty(clientId)) {
				AppClientMap map = new AppClientMap();
				map.setUsername(username);
				map.setCid(clientId);

				appClientMapService.addObj(map);
			}
		}
		
		//request.getRequestDispatcher("/console/jsp/console.jsp").forward(request, response);
		response.sendRedirect(request.getContextPath() + "/console/jsp/console.jsp");
	}
}
