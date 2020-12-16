package com.digitzones.service;

import java.util.List;

import com.digitzones.model.Role;
/**
 * 角色管理service
 * @author zdq
 * 2018年6月11日
 */
public interface IRoleService extends ICommonService<Role> {
	/**
	 * 查询所有角色
	 * @return
	 */
	public List<Role> queryAllRoles();
	/**
	 * 根据用户id查询角色
	 * @param userId
	 * @return
	 */
	public List<Role> queryRolesByUserId(Long userId);
	/**
	 * 为角色分配权限
	 * @param roleId
	 * @param powerIds
	 */
	public void addPowersForRole(Long roleId,String[] powerIds);
	/**
	 * 为角色分配 菜单
	 * @param roleId
	 * @param menuIds
	 */
	public void addMenusForRole(Long roleId,String[] menuIds);
	/**
	 * 为角色分配用户
	 * @param roleId
	 * @param menuIds
	 */
	public void addUsersForRole(Long roleId,String[] userIds);
	/**
	 * 为角色添加MC端权限
	 * @param roleId
	 * @param idArray
	 */
	public void addMCPowersForRole(Long roleId, String[] idArray);
}
