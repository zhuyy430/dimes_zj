package com.digitzones.mc.service;
import java.util.List;
import com.digitzones.mc.model.MCUser;
import com.digitzones.model.RoleMCPower;
import com.digitzones.model.User;
import com.digitzones.model.UserRole;
/**
 * mc端设备站点service
 * @author Administrator
 */
public interface IMCUserService {
	/**
	 * 获取关联的用户信息
	 * @return
	 */
	public List<MCUser> getAllMCUser(String clientIp);
	/**
	 * 删除权限用户
	 * @return
	 */
	public void deleteMCUser(List<Long> ids);
	/**
	 * 根据用户名、密码和IP地址查找用户数
	 * @param user
	 * @param ip
	 * @return
	 */
	public boolean login(User user,String classCode,String className,String ip);
	/**
	 * 查询当前主机上可用的用户
	 * @return
	 */
	public List<MCUser> queryAvailableUsers(String clientIp);
	/**
	 * 批量添加mcuser对象
	 * @param mcUsers
	 */
	public void addMCUsers(List<MCUser> mcUsers);
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
	 * 根据员工代码查询MC端用户信息
	 * @param employeeCode
	 * @return
	 */
	public MCUser queryMCUserByEmployeeCode(String employeeCode);
	/**
	 * 更新MCUser
	 * @param user
	 */
	public void updateMCUser(MCUser user);
	/**
	 * 查询用户权限
	 */
	public List<UserRole> queryUserExistPowerWithChexkLostTime(Long userid);
	/**
	 * 人员签退
	 * @param mcUser
	 */
	public void logout(MCUser mcUser) ;
	/**
	 * 查询所有已登录用户
	 * @return
	 */
	public List<MCUser> queryAllLoginUser();
	/**
	 * 根据id查找MC端用户
	 * @param id
	 * @return
	 */
	public MCUser queryMCUserById(Long id);
	/**
	 * 根据用户名查找MC端用户
	 * @param username
	 * @return
	 */
	public MCUser queryMCUserByUsername(String username);
	/**
	 * 根据设备站点代码查找MC端登录用户信息
	 * @param deviceSiteCode
	 * @return
	 */
	public MCUser queryLoginMCUserByDeviceSiteCode(String deviceSiteCode);
	/**
	 * 根据用户名查找MC端权限
	 * @param username
	 * @return
	 */
	public List<RoleMCPower> queryMCPowers(String username);
	
}
