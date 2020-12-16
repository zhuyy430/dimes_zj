package com.digitzones.service;

import java.util.List;

import com.digitzones.model.RoleMCPower;

public interface IRoleMCPowerService extends ICommonService<RoleMCPower> {
	/**
	 * 根据角色id查找mc端权限
	 * @param roleId
	 * @return
	 */
	List<RoleMCPower> queryByRoleId(Long roleId);
	/**
	 * 跟据角色id删除mc端权限
	 * @param roleId
	 */
	void deleteByRoleId(Long roleId);

}
