package com.digitzones.dao.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;
import com.digitzones.dao.IModuleDao;
import com.digitzones.model.Module;
@SuppressWarnings({ "unchecked" })
@Repository
public class ModuleDaoImpl extends CommonDaoImpl<Module> implements IModuleDao {
	public ModuleDaoImpl() {
		super(Module.class);
	}
	@Override
	public List<Module> queryTopModule(String username) {
		String sql = "select distinct m.* from MODULE m inner join ROLE_Module rm on m.id = rm.Module_ID " + 
				"inner join ROLE r on r.id = rm.ROLE_ID " + 
				"inner join USER_ROLE ur on ur.ROLE_ID=r.id " + 
				"inner join T_USER u on u.id = ur.USER_ID " + 
				"where m.PARENT_ID is null and  u.username=:username";
		return getSession().createNativeQuery(sql).setParameter("username", username).addEntity(Module.class).list();
	}
	@Override
	public List<Module> querySubModule(Serializable id, String username) {
		String sql = "select distinct m.* from MODULE m inner join ROLE_Module rm on m.id = rm.Module_ID " + 
				"inner join ROLE r on r.id = rm.ROLE_ID " + 
				"inner join USER_ROLE ur on ur.ROLE_ID=r.id " + 
				"inner join T_USER u on u.id = ur.USER_ID " + 
				"where m.PARENT_ID=:parentId and  u.username=:username order by m.priority";
		return getSession().createNativeQuery(sql).setParameter("username", username).setParameter("parentId", id).addEntity(Module.class).list();
	}
	@Override
	public List<Module> queryAllModuleByRoleId(Long roleId) {
		String sql = "select m.* from MODULE m inner join ROLE_Module rm on m.id = rm.Module_ID " + 
				"where rm.role_id=:roleId";
		return getSession().createNativeQuery(sql).setParameter("roleId", roleId).addEntity(Module.class).list();
	}
	@Override
	public List<Module> queryAllTopModule() {
		String sql = "select * from MODULE m " + 
				"where m.PARENT_ID is null";
		return getSession().createNativeQuery(sql).addEntity(Module.class).list();
	}
	@Override
	public List<Module> queryAllTopModuleByUserId(Long userId) {
		String sql = "select m.* from MODULE m inner join ROLE_Module rm on m.id = rm.Module_ID inner join USER_ROLE ur on rm.ROLE_ID=ur.ROLE_ID " + 
				"where m.PARENT_ID is null and ur.USER_ID=:userId";
		return getSession().createNativeQuery(sql).setParameter("userId", userId).addEntity(Module.class).list();
	}
	@Override
	public List<Module> queryAllModule() {
		String sql = "select * from MODULE m ";
		return getSession().createNativeQuery(sql).addEntity(Module.class).list();
	}
	@Override
	public List<Module> querySubModuleByParentId(Serializable parentId) {
		String sql = "select * from MODULE m " + 
				"where m.PARENT_ID = :parentId";
		return getSession().createNativeQuery(sql).setParameter("parentId", parentId).addEntity(Module.class).list();
	}
}
