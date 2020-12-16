package com.digitzones.service;

import java.util.List;
import java.util.Map;

import com.digitzones.model.Power;
/**
 * 权限管理service
 * @author zdq
 * 2018年6月11日
 */
public interface IPowerService extends ICommonService<Power> {
	/**
	 * 查询所有权限
	 * @return
	 */
	public List<Power> queryAllPowers();
	/**
	 * 查询所有权限
	 * @return
	 */
	public List<Power> queryAllPowersByUserId(Long userId);
	/**
	 * 根据角色id查找权限
	 * @param roleId
	 * @return
	 */
	public List<Power> queryPowersByRoleId(Long roleId);
	/**
	 * 根据角色id查找权限
	 * @param roleId
	 * @return
	 */
	public List<Power> queryPowersByRoleIdAndGroup(Long roleId,String group);
	/**
	 * 根据组查找权限
	 * @param roleId
	 * @return
	 */
	public List<Power> queryPowersByGroup(String group);
	/**
	 * 查询所有权限模块
	 * @return
	 */
	public List<Map<String, Object>> queryAllGroup();
}
