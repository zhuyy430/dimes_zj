package com.digitzones.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.cors.CorsUtils;
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	@Qualifier("userInfoService")
	private UserDetailsService userInfoService;
	/*@Autowired
	private AuthenticationSuccessHandler successHandler;*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.formLogin()
				.loginPage("/authentication/requireAuthenticate.do")
				.loginProcessingUrl("/login")//.successHandler(successHandler)
				.defaultSuccessUrl("/console/jsp/console.jsp")
				.and().logout().logoutUrl("/logout").logoutSuccessUrl("/index.jsp")
				.and().authorizeRequests()
				.antMatchers("/authentication/requireAuthenticate.do").permitAll()
				.antMatchers("/*/query*").permitAll()
				.antMatchers("/user/login.do").permitAll()
				.antMatchers("/login","/logout","/druid/*","/paperless/*").permitAll()
				.anyRequest().authenticated()
				.and().csrf().disable().cors();
		http.headers().frameOptions().sameOrigin();
		//解决跨域访问问题
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
		registry.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();//让Spring security放行所有preflight
		registry.antMatchers("/**").permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/*.js","/**/*.css","/**/*.jpg","/**/*.JPG","/**/*.png","/**/*.PNG","/console/js/**/*"
				,"/console/css/**/*","/common/**/*","/**/*.pdf","/**/*.PDF",
				"/**/*.doc","/**/*.DOC","/**/*.docx","/**/*.DOCX","/**/*.ppt","/**/*.PPT","/**/*.pptx","/**/*.PPTX",
				"/**/*.xls","/**/*.XLS","/**/*.xlsx","/**/*.XLSX","/**/*.gif","/**/*.GIF");
		web.ignoring().antMatchers("/*.jsp");
		web.ignoring().antMatchers("/workshop/*.jsp");
		//mc端全部放行
		web.ignoring().antMatchers("/mc/**/*","/mc*/*.do");
		web.ignoring().antMatchers("/paperless/**/*","/paperless*/*.do");
		web.ignoring().antMatchers("/paperLess/dm/**/*","/paperLess*/dm/*.do");
		web.ignoring().antMatchers("/App/**/*","/App*/*.do");
		web.ignoring().antMatchers("/productionUnitBoard/**/*","/productionUnitBoard*/*.do");
		web.ignoring().antMatchers("/productionUnit/queryAllLeafProductionUnits.do");
		web.ignoring().antMatchers("/equipmentRepairRecord/*.do");
		web.ignoring().antMatchers("/relatedDoc/uploadimg.do");
		web.ignoring().antMatchers("/userCountExceed.html");
		web.ignoring().antMatchers("/AppTask/upload.do");
		web.ignoring().antMatchers("/front/**/*");
		web.ignoring().antMatchers("/generateZxingCode/**/*","/generateZxingCode*/*.do");
		web.ignoring().antMatchers("/deviceRepairOrder/queryDeviceRepairOrderSerialNumber.do");
		web.ignoring().antMatchers("/console/jsp/barCode_print.jsp");
		web.ignoring().antMatchers("/boxBar/printJobBookingQr.do");
		web.ignoring().antMatchers("/processRecord/addProcessRecordWithExternal.do");
		web.ignoring().antMatchers("/workshop/*");
		web.ignoring().antMatchers("/kanBan/**/*","/kanBan*/*.do");
	}
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		ReflectionSaltSource rss = new ReflectionSaltSource();
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		rss.setUserPropertyToUse("username");
		provider.setSaltSource(rss);
		provider.setPasswordEncoder(new Md5PasswordEncoder());
		provider.setUserDetailsService(userInfoService);
		auth.authenticationProvider(provider);
	}
}
