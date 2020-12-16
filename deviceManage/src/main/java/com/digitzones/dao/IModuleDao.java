package com.digitzones.dao;

import java.io.Serializable;
import java.util.List;

import com.digitzones.model.Module;
/**
 * 功能模块数据访问接口
 * @author zdq
 * 2018年6月6日
 */
public interface IModuleDao extends ICommonDao<Module>{
	/**
	 * 查找顶级模块
	 * @return
	 */
	public List<Module> queryTopModule(String username);
	/**
	 * 查找子模块
	 * @param id
	 * @param username
	 * @return
	 */
	public List<Module> querySubModule(Serializable id,String username);
	/**
	 * 根据角色id查找模块
	 * @param roleId
	 * @return
	 */
	public List<Module> queryAllModuleByRoleId(Long roleId);
	/**
	 * 查询所有顶级模块
	 * @return
	 */
	public List<Module> queryAllTopModule();
	/**
	 * 查询所有顶级模块
	 * @return
	 */
	public List<Module> queryAllTopModuleByUserId(Long userId);
	/**
	 * 查询所有模块
	 * @return
	 */
	public List<Module> queryAllModule();
	/**
	 * 根据父模块id查找子模块
	 * @param id
	 * @return
	 */
	public List<Module> querySubModuleByParentId(Serializable parentId);
}
