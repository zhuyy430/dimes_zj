package com.digitzones.service;

import java.util.List;

import com.digitzones.model.User;
import com.digitzones.model.UserRole;
/**
 * 用户角色关联service
 * @author zdq
 * 2018年7月18日
 */
public interface IUserRoleService extends ICommonService<UserRole> {
	/**
	 *  根据角色id查找记录数
	 * @param roleId
	 * @return
	 */
	public Long queryCountByRoleId(Long roleId);
	
	/**
	 * 通过角色id查找用户
	 */
	public List<User> queryUsersByRoleId(Long roleId);
}
