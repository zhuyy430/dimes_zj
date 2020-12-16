package com.digitzones.mc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.mc.dao.IMCUserDao;
import com.digitzones.mc.model.MCUser;
import com.digitzones.mc.service.IMCUserService;
import com.digitzones.model.RoleMCPower;
import com.digitzones.model.User;
import com.digitzones.model.UserRole;
@Service
public class MCUserServiceImpl implements IMCUserService {
	@Autowired
	private IMCUserDao mcUserDao;
	@Override
	public List<MCUser> getAllMCUser(String clientIp) {
		return mcUserDao.findByHQL("from MCUser u where u.clientIp=?0", new Object[] {clientIp});
	}
	@Override
	public void deleteMCUser(List<Long> ids) {
		for(Long id : ids) {
			mcUserDao.deleteById(id);
		}
	}

	@Override
	public boolean login(User user,String classCode,String className, String ip) {
		int count = mcUserDao.queryMCUserCountByUserAndIp(user, ip);
		if(count > 0) {
			MCUser mcUser = mcUserDao.queryMCUserByUsernameAndClientIp(user.getUsername(), ip);
			mcUser.setSign_in(true);
			mcUser.setClassCode(classCode);
			mcUser.setClassName(className);
			mcUserDao.update(mcUser);
			return true;
		}
		return false;
	}
	@Override
	public List<MCUser> queryAvailableUsers(String clientIp) {
		return mcUserDao.queryAvailableUsers(clientIp);
	}
	@Override
	public void addMCUsers(List<MCUser> mcUsers) {
		for(MCUser user : mcUsers) {
			mcUserDao.save(user);
		}
	}
	@Override
	public MCUser queryLoginUserByClientIp(String clientIp) {
		return mcUserDao.queryLoginUserByClientIp(clientIp);
	}
	@Override
	public MCUser queryMCUserByEmployeeCodeAndClientIp(String employeeCode, String clientIp) {
		return mcUserDao.queryMCUserByEmployeeCodeAndClientIp(employeeCode, clientIp);
	}
	@Override
	public MCUser queryMCUserByEmployeeCodeAndClientIpAndsign_in(String employeeCode, String clientIp) {
		return mcUserDao.queryMCUserByEmployeeCodeAndClientIpAndsign_in(employeeCode, clientIp);
	}
	@Override
	public void updateMCUser(MCUser user) {
		mcUserDao.update(user);
	}
	@Override
	public MCUser queryMCUserByEmployeeCode(String employeeCode) {
		String hql = "from MCUser mcu where mcu.employeeCode=?0";
		List<MCUser> list = mcUserDao.findByHQL(hql, new Object[] {employeeCode});
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<UserRole> queryUserExistPowerWithChexkLostTime(Long userid) {
		return mcUserDao.queryUserExistPowerWithChexkLostTime(userid);
	}
	@Override
	public void logout(MCUser mcUser) {
		mcUser.setSign_in(false);
		mcUserDao.update(mcUser);
	}
	@Override
	public List<MCUser> queryAllLoginUser() {
		return mcUserDao.findByHQL("from MCUser u where u.sign_in=?0", new Object[] {true});
	}
	@Override
	public MCUser queryMCUserById(Long id) {
		return mcUserDao.findById(id);
	}
	@Override
	public MCUser queryMCUserByUsername(String username) {
		return mcUserDao.findSingleByProperty("username", username);
	}
	@Override
	public MCUser queryLoginMCUserByDeviceSiteCode(String deviceSiteCode) {
		return mcUserDao.queryLoginMCUserByDeviceSiteCode(deviceSiteCode);
	}
	@Override
	public MCUser queryMCUserByEmployeeNameAndClientIpAndsign_in(String employeeName, String clientIp) {
		return mcUserDao.queryMCUserByEmployeeNameAndClientIpAndsign_in(employeeName, clientIp);
	}
	@Override
	public List<RoleMCPower> queryMCPowers(String username) {
		return mcUserDao.queryMCPowersByUsername(username);
	}
}
