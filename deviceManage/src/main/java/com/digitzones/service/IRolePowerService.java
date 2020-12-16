package com.digitzones.service;

import com.digitzones.model.RolePower;
/**
 * 角色权限
 * @author zdq
 * 2018年7月18日
 */
public interface IRolePowerService extends ICommonService<RolePower> {
	/**
	 * 根据权限id查询关联的角色数量
	 * @param powerId
	 * @return
	 */
	public Long queryCountByPowerId(Long powerId);
}
