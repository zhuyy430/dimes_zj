package com.digitzones.dao;

import com.digitzones.model.RoleMCPower;

public interface IRoleMCPowerDao extends ICommonDao<RoleMCPower>{
	/**
	 * 根据角色id删除MC端权限
	 * @param roleId
	 */
	void deleteByRoleId(Long roleId);

}
