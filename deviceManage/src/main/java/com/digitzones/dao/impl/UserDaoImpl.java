package com.digitzones.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IUserDao;
import com.digitzones.model.Power;
import com.digitzones.model.User;
@Repository
public class UserDaoImpl extends CommonDaoImpl<User> implements IUserDao {
	public UserDaoImpl() {
		super(User.class);
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<User> queryAllUser(String usernames) {
		String sql = "select u.id,u.username,u.password,u.realName,u.email,u.tel,u.createDate,u.note,u.disable,u.createUserid,"
				+ "u.createUsername,u.modifyUserId,u.modifyUsername,u.modifyDate,u.EMPLOYEE_ID,u.picName,u.pic,u.allowDelete"
				+ " from T_USER u where u.username not in (?0) and u.disable=?1";
		return getSession().createSQLQuery(sql).setParameter(0, usernames).setParameter(1, false).addEntity(User.class).list();
	}
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<Power> queryPowersByUsername(String username) {
		String sql = "select distinct p.* from T_USER u inner join USER_ROLE ur on u.id = ur.USER_ID " + 
				"inner join ROLE r on r.id = ur.ROLE_ID " + 
				"inner join ROLE_POWER rp on r.id = rp.ROLE_ID " + 
				"inner join POWER p on p.id=rp.POWER_ID where u.disable=0 and u.username=:username";
		return getSession().createNativeQuery(sql).setParameter("username",username).addEntity(Power.class).list();
	}
	@Override
	public int queryCount() {
		String sql = "select count(1) from T_USER";
		int count = (int) getSession().createNativeQuery(sql).uniqueResult();
		return count;
	}

	/**
	 * 查询所有用户中的员工编码
	 *
	 * @return
	 */
	@Override
	public List<String> queryAllEmployeeCodes() {
		return getSession().createNativeQuery("select distinct EMPLOYEE_CODE from T_USER").list();
	}

	/**
	 * 将用户的员工编码置为null
	 *
	 * @param employeeCode
	 */
	@Override
	public void updateUsersEmployeeNull(String employeeCode) {
		getSession().createNativeQuery("update T_USER set EMPLOYEE_CODE=null WHERE EMPLOYEE_CODE=?0").setParameter(0,employeeCode).executeUpdate();
	}
}
