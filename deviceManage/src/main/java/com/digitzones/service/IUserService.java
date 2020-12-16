package com.digitzones.service;

import java.util.List;

import com.digitzones.model.Power;
import com.digitzones.model.User;
public interface IUserService extends ICommonService<User> {
	/**
	 * 用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	public User login(String username,String password);
	/**
	 * 根据用户名查找用户
	 * @param username
	 * @return
	 */
	public User queryUserByUsername(String username);
	/**
	 * 查询非当前用户
	 * @return
	 */
	public List<User> queryNotCurrentUsers(Long currentUserId);
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<User> queryAllUsers();
	/**
	 * 根据员工id查找用户信息
	 * @param employeeId
	 * @return
	 */
	public List<User> queryUsersByEmployeeId(Long employeeId);
	/**
	 * 为用户添加角色
	 * @param userId
	 * @param roleIds
	 */
	public void addRolesForUser(Long userId,String[] roleIds);
	/**
	 * 根据员工代码查找用户信息
	 * @param employeeCode
	 * @return
	 */
	public User queryUserByEmployeeCode(String employeeCode);
	/**
	 * 根据员工名称查找用户信息
	 * @param employeeCode
	 * @return
	 */
	public User queryUserByEmployeeName(String employeeName);
	/**
	 * 根据角色名称查找用户信息
	 * @param roleName
	 * @return
	 */
	public List<User> queryUsersByRolename(String roleName);
	/**
	 * 根据用户名查找权限
	 * @param username
	 * @return
	 */
	public List<Power> queryPowersByUsername(String username);
	/**
	 * 查询用户数
	 * @return
	 */
	public int queryCount() ;
	/**
	 * 根据角色id查找用户
	 * @param roleId
	 * @return
	 */
	public List<User> queryUsersByRoleId(Long roleId);

	/**
	 * 查询用户中的所有员工编码
	 * @return
	 */
    List<String> queryAllEmployeeCodes();

	/**
	 * 将用户的员工编码置为null
	 * @param employeeCode
	 */
	void updateUsersEmployeeNull(String employeeCode);
}
