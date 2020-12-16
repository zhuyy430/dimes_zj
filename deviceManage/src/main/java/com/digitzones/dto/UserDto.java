package com.digitzones.dto;

public class UserDto {
	private String username;
	private String password;
	private String clientid;
	private String verson;
	public String getVerson() {
		return verson;
	}
	public void setVerson(String verson) {
		this.verson = verson;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	
}
