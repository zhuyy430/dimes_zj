package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IModuleDao;
import com.digitzones.model.Module;
import com.digitzones.model.Pager;
import com.digitzones.service.IModuleService;
@Service
public class ModuleServiceImpl implements IModuleService {
	private IModuleDao moduleDao;
	@Autowired
	public void setModuleDao(IModuleDao moduleDao) {
		this.moduleDao = moduleDao;
	}

	@Override
	public Serializable addModule(Module module) {
		return moduleDao.save(module);
	}

	@Override
	public void updateModule(Module module) {
		moduleDao.update(module);
	}

	@Override
	public void deleteModule(Serializable id) {
		moduleDao.deleteById(id);
	}

	@Override
	public List<Module> queryTopModule(String username) {
		return moduleDao.queryTopModule(username);
	}
		@Override
	public List<Module> querySubModule(Serializable id,String username) {
		return moduleDao.querySubModule( id, username);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return moduleDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(Module obj) {
		moduleDao.update(obj);
	}

	@Override
	public Module queryByProperty(String name, String value) {
		return moduleDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Module obj) {
		return moduleDao.save(obj);
	}

	@Override
	public Module queryObjById(Serializable id) {
		return moduleDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		moduleDao.deleteById(id);
	}

	@Override
	public List<Module> queryAllModuleByRoleId(Long roleId) {
		return moduleDao.queryAllModuleByRoleId(roleId);
	}

	@Override
	public List<Module> queryAllTopModule() {
		return moduleDao.queryAllTopModule();
	}

	@Override
	public List<Module> queryAllTopModuleByUserId(Long userId) {
		return moduleDao.queryAllTopModuleByUserId(userId);
	}
	
	@Override
	public List<Module> querySubModuleByParentId(Serializable parentId) {
		return moduleDao.querySubModuleByParentId(parentId);
	}

	@Override
	public List<Module> queryTopModule() {
		return moduleDao.findByHQL("from Module m where m.parent is null", new Object[] {});
	}

	@Override
	public List<Module> querySubModule(Serializable id) {
		return moduleDao.findByHQL("from Module m where m.parent.id=?0", new Object[] {id});
	}

	@Override
	public List<Module> queryAllModule() {
		return moduleDao.queryAllModule();
	}
}
