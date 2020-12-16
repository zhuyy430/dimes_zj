package com.digitzones.service;
import java.io.Serializable;
import java.util.List;

import com.digitzones.model.Module;
/**
 * 功能模块操作
 * @author zdq
 * 2018年6月6日
 */
public interface IModuleService extends ICommonService<Module> {
	/**
	 * 添加一个功能模块
	 * @param module
	 * @return
	 */
	public Serializable addModule(Module module);
	/**
	 * 更新功能模块
	 * @param module
	 */
	public void updateModule(Module module);
	/**
	 * 删除模块
	 * @param id
	 */
	public void deleteModule(Serializable id);
	/**
	 * 查找顶级模块
	 * @return
	 */
	public List<Module> queryTopModule(String username);
	/**
	 * 查找顶级模块
	 * @return
	 */
	public List<Module> queryTopModule();
	/**
	 * 查询所有顶级模块
	 * @return
	 */
	public List<Module> queryAllTopModuleByUserId(Long userId);
	/**
	 * 根据角色id查找模块
	 * @param roleId
	 * @return
	 */
	public List<Module> queryAllModuleByRoleId(Long roleId);
	/**
	 * 根据父模块id查找子模块
	 * @param id
	 * @return
	 */
	public List<Module> querySubModule(Serializable id,String username);
	/**
	 * 根据父模块id查找子模块
	 * @param id
	 * @return
	 */
	public List<Module> querySubModule(Serializable id);
	/**
	 * 根据父模块id查找子模块
	 * @param id
	 * @return
	 */
	public List<Module> querySubModuleByParentId(Serializable parentId);
	/**
	 * 查询所有顶级模块
	 * @return
	 */
	public List<Module> queryAllTopModule();
	/**
	 * 查询所有模块
	 * @return
	 */
	public List<Module> queryAllModule();
}
