package com.digitzones.mc.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.digitzones.constants.Constant;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCUserDao;
import com.digitzones.mc.model.MCUser;
import com.digitzones.model.RoleMCPower;
import com.digitzones.model.User;
import com.digitzones.model.UserRole;
@Repository
public class MCUserDaoImpl extends CommonDaoImpl<MCUser> implements IMCUserDao{
	public MCUserDaoImpl() {
		super(MCUser.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<MCUser> selectAllMCMCUserByIP(String IP) {
		String sql = "select u.id,u.clientIp,u.username,u.employeeName,u.employeeCode "
				+ "from MC_USER u  where u.clientIp=?0";
		return getSession().createSQLQuery(sql).setParameter(0,IP).addEntity(MCUser.class).list();
	}

	@SuppressWarnings("deprecation")
	@Override
	public int queryMCUserCountByUserAndIp(User user, String ip) {
		String sql = "select COUNT(mcUser.id) from T_USER u inner join MC_User mcUser on u.username=mcUser.username " + 
				"where u.username=?0 and u.password=?1 and mcUser.clientIp=?2 ";
		return (int) getSession().createSQLQuery(sql).setParameter(0, user.getUsername())
												.setParameter(1, user.getPassword())
												.setParameter(2, ip)
												.uniqueResult();
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<MCUser> queryAvailableUsers(String clientIp) {
		String sql = "select u.id id,u.username username,null classCode,null className,u.username employeeICNo,e.cPsn_Num employeeCode,e.cPsn_Name employeeName,'' clientIp,0  sign_in "
				+ " from T_USER u INNER JOIN View_Person e on u.EMPLOYEE_CODE=e.cPsn_Num where u.disable=0  and u.username!=?1 and u.username not in "
				+ "(select username from MC_USER where clientIp=?0)";
		return getSession().createSQLQuery(sql).setParameter(0,clientIp)
				.setParameter(1, Constant.User.ADMIN).addEntity(MCUser.class).list();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public MCUser queryLoginUserByClientIp(String clientIp) {
		String sql = "select * from mc_user where clientIp=?0 and sign_in=?1";
		List<MCUser> list = getSession().createSQLQuery(sql).setParameter(0,clientIp).setParameter(1, true).addEntity(MCUser.class).list();
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public MCUser queryMCUserByEmployeeCodeAndClientIp(String employeeCode, String clientIp) {
		String sql = "select * from mc_user where clientIp=?0 and (employeeCode=?1 or employeeICNo=?1)";
		return (MCUser) getSession().createSQLQuery(sql).setParameter(0,clientIp).setParameter(1, employeeCode).addEntity(MCUser.class).uniqueResult();
	}

	
	public MCUser queryMCUserByEmployeeCodeAndClientIpAndsign_in(String employeeCode, String clientIp) {
		String sql = "select * from mc_user where clientIp=?0 and (employeeCode=?1 or employeeICNo=?1)  and sign_in=?2";
		return (MCUser) getSession().createNativeQuery(sql).setParameter(0,clientIp).setParameter(1, employeeCode).setParameter(2,true).addEntity(MCUser.class).uniqueResult();
	}
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<UserRole> queryUserExistPowerWithChexkLostTime(Long userid) {
		String sql = "select ur.id,ur.USER_ID,ur.ROLE_ID from USER_ROLE ur where ur.USER_ID=?0";
		List<UserRole> list = getSession().createSQLQuery(sql).setParameter(0,userid).addEntity(UserRole.class).list();
		return list;
	}

	@SuppressWarnings("deprecation")
	@Override
	public MCUser queryMCUserByUsernameAndClientIp(String username, String clientIp) {
		String sql = "select * from MC_User where username=?0 and clientIp=?1";
		return (MCUser) getSession().createSQLQuery(sql).setParameter(0, username).setParameter(1, clientIp).addEntity(MCUser.class).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public MCUser queryLoginMCUserByDeviceSiteCode(String deviceSiteCode) {
		String sql = "select * from MC_User mu inner join MC_DEVICESITE md on mu.clientIp=md.clientIp where md.deviceSiteCode=?0 and mu.sign_in=1";
		 List<MCUser> list = getSession().createNativeQuery(sql).addEntity(MCUser.class).setParameter(0, deviceSiteCode).list();
		 if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		 }
		return null;
	}

	@Override
	public MCUser queryMCUserByEmployeeNameAndClientIpAndsign_in(String employeeName, String clientIp) {
		String sql = "select * from mc_user where clientIp=?0 and employeeName=?1  and sign_in=?2";
		return (MCUser) getSession().createNativeQuery(sql).setParameter(0,clientIp).setParameter(1, employeeName).setParameter(2,true).addEntity(MCUser.class).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleMCPower> queryMCPowersByUsername(String username) {
		String sql = "select distinct p.* from role_mcpower p inner join ROLE r on r.id=p.role_id " + 
				" inner join USER_ROLE ur on ur.ROLE_ID = r.id " + 
				" inner join T_USER u on ur.USER_ID=u.id where u.username=?0";
		return getSession().createNativeQuery(sql).setParameter(0,username).list();
	}
}
