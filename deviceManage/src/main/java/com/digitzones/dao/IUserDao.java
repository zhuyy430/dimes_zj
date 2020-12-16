package com.digitzones.dao;

import java.util.List;

import com.digitzones.model.Power;
import com.digitzones.model.User;

/**
 * 用户管理dao
 * @author zdq
 * 2018年6月11日
 */
public interface IUserDao extends ICommonDao<User> {
	public List<User> queryAllUser(String usernames);
	/**
	 * 根据用户名查找权限
	 * @param username
	 * @return
	 */
	public List<Power> queryPowersByUsername(String username);
	/**
	 * 查询用户数
	 * @return int 用户数
	 */
	public int queryCount();
	/**
	 * 查询所有用户中的员工编码
	 * @return
	 */
    List<String> queryAllEmployeeCodes();

	/**
	 * 将用户的员工编码置为null
	 * @param employeeCode
	 */
	void updateUsersEmployeeNull(String employeeCode);
}