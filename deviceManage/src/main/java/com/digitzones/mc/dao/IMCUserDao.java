package com.digitzones.mc.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.mc.model.MCUser;
import com.digitzones.model.RoleMCPower;
import com.digitzones.model.User;
import com.digitzones.model.UserRole;

/**
 * 
 * @author Administrator
 *
 */
public interface IMCUserDao extends ICommonDao<MCUser>{
	
	/**
	 * 根据IP查找对应的用户信息
	 * @param IP
	 * @return
	 */
	List<MCUser> selectAllMCMCUserByIP(String IP);
	/**
	 * 根据用户名、密码和IP地址查找用户数
	 * @param user
	 * @param ip
	 * @return
	 */
	public int queryMCUserCountByUserAndIp(User user,String ip);
	/**
	 * 查询当前主机上可用的用户
	 * @return
	 */
	public List<MCUser> queryAvailableUsers(String clientIp);
	/**
	 * 根据ip地址查询登录用户
	 * @param clientIp 客户端IP
	 * @return
	 */
	public MCUser queryLoginUserByClientIp(String clientIp);
	/**
	 * 根据员工代码和客户端IP查询登录用户
	 * @param employeeCode
	 * @param clientIp
	 * @return
	 */
	public MCUser queryMCUserByEmployeeCodeAndClientIp(String employeeCode,String clientIp);
	/**
	 * 根据员工代码和客户端IP查询已经登录用户
	 * @param employeeCode
	 * @param clientIp
	 * @return 
	 */
	public MCUser queryMCUserByEmployeeCodeAndClientIpAndsign_in(String employeeCode, String clientIp);
	/**
	 * 根据员工姓名和客户端IP查询已经登录用户
	 * @param employeeName
	 * @param clientIp
	 * @return 
	 */
	public MCUser queryMCUserByEmployeeNameAndClientIpAndsign_in(String employeeName, String clientIp);
	/**
	 * 判断用户是否存在损时确认人权限
	 * @param user
	 * @return
	 */
	public List<UserRole> queryUserExistPowerWithChexkLostTime(Long userid);
	/**
	 * 根据用户名和IP地址查找用户信息
	 * @param username
	 * @param clientIp
	 * @return
	 */
	public MCUser queryMCUserByUsernameAndClientIp(String username,String clientIp);
	/**
	 * 根据设备站点代码查找mc端登录用户
	 * @param deviceSiteCode
	 * @return
	 */
	public MCUser queryLoginMCUserByDeviceSiteCode(String deviceSiteCode);
	/**
	 * 根据用户名查找MC端权限
	 * @param username
	 * @return
	 */
	List<RoleMCPower> queryMCPowersByUsername(String username);
}
