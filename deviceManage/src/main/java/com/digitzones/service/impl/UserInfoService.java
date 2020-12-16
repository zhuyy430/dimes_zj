package com.digitzones.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.digitzones.model.Power;
import com.digitzones.model.User;
import com.digitzones.service.IUserService;
@Service("userInfoService")
public class UserInfoService implements UserDetailsService {
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.queryUserByUsername(username);
		List<GrantedAuthority> list = new ArrayList<>();
		List<Power> powers = userService.queryPowersByUsername(username);
		if(powers!=null && powers.size()>0) {
			for(Power p : powers) {
				list.add(new SimpleGrantedAuthority(p.getPowerCode()));
			}
		}
		UserDetails ud = new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
		return ud;
	}
}
