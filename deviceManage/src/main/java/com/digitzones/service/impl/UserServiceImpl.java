package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import com.digitzones.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IUserDao;
import com.digitzones.dao.IUserRoleDao;
import com.digitzones.service.IUserService;
@Service("userService")
public class UserServiceImpl implements IUserService {
	private IUserDao userDao;
	private IUserRoleDao userRoleDao;
	@Autowired
	public void setUserRoleDao(IUserRoleDao userRoleDao) {
		this.userRoleDao = userRoleDao;
	}
	@Autowired
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return userDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(User obj) {
		userDao.update(obj);
	}
	@Override
	public User queryByProperty(String name, String value) {
		return userDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(User obj) {
		return userDao.save(obj);
	}

	@Override
	public User queryObjById(Serializable id) {
		return userDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		//根据用户id删除关联的角色信息
		List<UserRole> userRoles = userRoleDao.findByHQL("from UserRole ur where ur.user.id=?0", new Object[] {id});
		if(userRoles!=null) {
			for(UserRole userRole : userRoles) {
				userRoleDao.delete(userRole);
			}
		}
		userDao.deleteById(id);
	}
	@Override
	public User login(String username, String password) {
		List<User> users = this.userDao.findByHQL("from User u where u.username=?0 and u.password=?1", new Object[] {username,password});
		return (users!=null&&users.size()>0)?users.get(0):null;
	}
	@Override
	public User queryUserByUsername(String username) {
		//return userDao.findSingleByProperty("username", username);
		List<User> users = this.userDao.findByHQL("from User u where u.username=?0 ", new Object[] {username});
		return (users!=null&&users.size()>0)?users.get(0):null;
	}
	@Override
	public List<User> queryNotCurrentUsers(Long currentUserId) {
		return userDao.findByHQL("from User u where u.id!=?0 and u.employee is not null", new Object[] {currentUserId});
	}
	@Override
	public List<User> queryAllUsers() {
		return userDao.findByHQL("from User where username!=?0", new Object[] {Constant.ADMIN});
	}

	@Override
	public List<User> queryUsersByEmployeeId(Long employeeId) {
		return userDao.findByHQL("from User u where u.employee.id=?0", new Object[] {employeeId});
	}

	@Override
	public void addRolesForUser(Long userId, String[] roleIds) {
		//根据用户id删除关联的角色信息
		List<UserRole> userRoles = userRoleDao.findByHQL("from UserRole ur where ur.user.id=?0", new Object[] {userId});
		if(userRoles!=null) {
			for(UserRole userRole : userRoles) {
				userRoleDao.delete(userRole);
			}
		}
		//为用户添加新角色
		User user = new User();
		user.setId(userId);
		for(String roleId : roleIds) {
			UserRole userRole = new UserRole();
			Role role = new Role();
			role.setId(Long.valueOf(roleId));
			
			userRole.setRole(role);
			userRole.setUser(user);
			
			userRoleDao.save(userRole);
		}
	}

	@Override
	public User queryUserByEmployeeCode(String employeeCode) {
		String hql = "from User u where u.employee.code=?0";
		List<User> users = userDao.findByHQL(hql, new Object[] {employeeCode});
		if(users!=null && users.size()>0) {
			return users.get(0);
		}
		return null;
	}
	@Override
	public User queryUserByEmployeeName(String employeeName) {
		String hql = "from User u where u.employee.name=?0";
		List<User> users = userDao.findByHQL(hql, new Object[] {employeeName});
		if(users!=null && users.size()>0) {
			return users.get(0);
		}
		return null;
	}
	@Override
	public List<User> queryUsersByRolename(String roleName) {
		String hql = " select ur.user from UserRole ur where ur.role.roleName=?0";
		return userDao.findByHQL(hql, new Object[] {roleName});
	}

	@Override
	public List<Power> queryPowersByUsername(String username) {
		return userDao.queryPowersByUsername(username);
	}

	@Override
	public int queryCount() {
		return userDao.queryCount();
	}

	@Override
	public List<User> queryUsersByRoleId(Long roleId) {
		String hql = " select ur.user from UserRole ur where ur.role.id=?0";
		return userDao.findByHQL(hql, new Object[] {roleId});
	}

	/**
	 * 查询用户中的所有员工编码
	 * @return
	 */
	@Override
	public List<String> queryAllEmployeeCodes() {
		return userDao.queryAllEmployeeCodes();
	}

	/**
	 * 将用户的员工编码置为null
	 *
	 * @param employeeCode
	 */
	@Override
	public void updateUsersEmployeeNull(String employeeCode) {
		userDao.updateUsersEmployeeNull(employeeCode);
	}
}
