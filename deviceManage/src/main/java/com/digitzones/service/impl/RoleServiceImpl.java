package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IRoleDao;
import com.digitzones.dao.IRoleModuleDao;
import com.digitzones.dao.IRolePowerDao;
import com.digitzones.dao.IUserRoleDao;
import com.digitzones.model.Module;
import com.digitzones.model.Pager;
import com.digitzones.model.Power;
import com.digitzones.model.Role;
import com.digitzones.model.RoleMCPower;
import com.digitzones.model.RoleModule;
import com.digitzones.model.RolePower;
import com.digitzones.model.User;
import com.digitzones.model.UserRole;
import com.digitzones.service.IRoleMCPowerService;
import com.digitzones.service.IRoleService;
@Service
public class RoleServiceImpl implements IRoleService {
	private IRoleDao roleDao;
	private IRolePowerDao rolePowerDao;
	@Autowired
	private IRoleModuleDao roleModuleDao;
	@Autowired
	private IUserRoleDao userRoleDao;
	@Autowired
	private IRoleMCPowerService roleMCPowerService;
	@Autowired
	public void setRolePowerDao(IRolePowerDao rolePowerDao) {
		this.rolePowerDao = rolePowerDao;
	}
	@Autowired
	public void setRoleDao(IRoleDao roleDao) {
		this.roleDao = roleDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return roleDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(Role obj) {
		roleDao.update(obj);
	}
	@Override
	public Role queryByProperty(String name, String value) {
		return roleDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(Role obj) {
		return roleDao.save(obj);
	}

	@Override
	public Role queryObjById(Serializable id) {
		return roleDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		//删除角色下的权限
		List<RolePower> rolePowers = rolePowerDao.findByHQL("from RolePower rp where rp.role.id=?0", new Object[] {id});
		if(rolePowers!=null) {
			for(RolePower rolePower : rolePowers) {
				rolePowerDao.delete(rolePower);
			}
		}
		roleDao.deleteById(id);
	}
	@Override
	public List<Role> queryAllRoles() {
		return roleDao.findByHQL("from Role r where (r.disable!=?0 or r.disable is null) and roleName!=?1 order by r.roleName", new Object[] {true,Constant.ADMIN});
	}
	@Override
	public List<Role> queryRolesByUserId(Long userId) {
		return roleDao.findByHQL("select ur.role from UserRole ur where ur.user.id=?0", new Object[] {userId});
	}
	@Override
	public void addPowersForRole(Long roleId, String[] powerIds) {
		//为角色添加新权限
		Role role = new Role();
		role.setId(roleId);
		for(String powerId : powerIds) {
			RolePower rolePower = new RolePower();
			Power power = new Power();
			power.setId(Long.valueOf(powerId));
			rolePower.setPower(power);
			rolePower.setRole(role);
			List<RolePower> list = rolePowerDao.findByHQL("from RolePower ur where ur.power.id=?0 and ur.role.id=?1",new Object[] {Long.valueOf(powerId),roleId});
			if(CollectionUtils.isEmpty(list)) {
				rolePowerDao.save(rolePower);
			}
		}
	}
	@Override
	public void addMenusForRole(Long roleId, String[] menuIds) {
		//为角色添加新模块
		Role role = new Role();
		role.setId(roleId);
		for(String moduleId : menuIds) {
			RoleModule roleModule = new RoleModule();
			Module module = new Module();
			module.setId(Long.valueOf(moduleId));
			roleModule.setModule(module);
			roleModule.setRole(role);
			List<RoleModule> list = roleModuleDao.findByHQL("from RoleModule ur where ur.module.id=?0 and ur.role.id=?1",new Object[] {Long.valueOf(moduleId),roleId});
			if(CollectionUtils.isEmpty(list)) {
				roleModuleDao.save(roleModule);
			}
		}
	}
	@Override
	public void addUsersForRole(Long roleId, String[] userIds) {
		//为角色添加用户
		Role role = new Role();
		role.setId(roleId);
		for(String userId : userIds) {
			UserRole userRole = new UserRole();
			User user = new User();
			user.setId(Long.valueOf(userId));
			userRole.setUser(user);
			userRole.setRole(role);
			List<UserRole> list = userRoleDao.findByHQL("from UserRole ur where ur.user.id=?0 and ur.role.id=?1",new Object[] {Long.valueOf(userId),roleId});
			if(CollectionUtils.isEmpty(list)) {
				userRoleDao.save(userRole);
			}
		}
	}
	@Override
	public void addMCPowersForRole(Long roleId, String[] powerNames) {
		//为角色添加新权限
		Role role = new Role();
		role.setId(roleId);
		//根据角色id删除MC端权限
		roleMCPowerService.deleteByRoleId(roleId);
		for(String powerName : powerNames) {
			RoleMCPower rolePower = new RoleMCPower();
			rolePower.setMcpower(powerName);
			rolePower.setRole(role);
			roleMCPowerService.addObj(rolePower);
		}
	}
}
