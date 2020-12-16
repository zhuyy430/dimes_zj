package com.digitzones.devmgr.service;

import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.model.User;
import com.digitzones.service.ICommonService;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 设备维修单业务逻辑接口
 */
public interface IDeviceRepairOrderService extends ICommonService<DeviceRepair>{
	
	/**
	 * 根据属性和属性值获取数据
	 * @param name
	 * @param value
	 * @return
	 */
	public List<DeviceRepair> queryListByProperty(String name, String value);
	
	/**
	 * 添加报修单记录
	 * @return
	 */
	public Serializable addDeviceRepair(DeviceRepair deviceRepair, String operator);
	/**
	 * 确认报修单
	 * @return
	 */
	public void confirmDeviceRepair(DeviceRepair deviceRepair, String operator,String status);
	/**
	 * 接收报修单
	 * @param user
	 * @param args
	 * @return
	 */
	public void receiveDeviceRepair(DeviceRepair deviceRepair, User user,Map<String,Object> args);

	 public List<DeviceRepair> queryAllDeviceRepair();
	 
	 /**
	  * 获取新增报警报修单
	  * @param alarmedIds
	  * @return
	  */
	 public List<DeviceRepair> queryAlarmsCount(String alarmedIds);
	 /**
	  * 获取最早的未分配的报修单
	  * @return
	  */
	 public DeviceRepair queryFirstDeviceRepair();
	 
	 /**
	  * 根据设备编码获取报修单记录
	  * @param deviceId
	  * @return
	  */
	 public List<DeviceRepair> queryDeviceRepairOrderByDeviceId(Long deviceId);

	 /**
	  * 根据设备编码获取报修单记录
	  * @param deviceCode
	  * @return
	  */
	 public List<DeviceRepair> queryDeviceRepairOrderByDeviceCode2(String deviceCode);
	 /**
	  * 根据生产单元获取报修单记录
	  * @return
	  */
	 public List<DeviceRepair> queryDeviceRepairOrderByProductionUnitId(Long productionUnitId);
	 /**
	  * 根据维修状态获取当前用户的维修单
	  */
	 public List<DeviceRepair> queryDeviceRepairWithUserandStatus(String status,String username);

	/**
	 * 根据维修状态获取维修单
	 */
	public List<DeviceRepair> queryDeviceRepairWithStatusForScreen(String status,String productionLineCode,String maintainerCode,String deviceCode,
																   String ngFaultTypeonCode,String startDate,String endDate);
	 /**
	  * 根据维修状态获取维修单
	  */
	 public List<DeviceRepair> queryDeviceRepairWithStatus(String status);
	 /**
	  * 获取当前人员的待接单维修单
	  */
	public List<DeviceRepair>  queryReceiptDeviceRepairWithUser(String code);
	/**
	 * 获取当前人员的维修中维修单
	 */
	public List<DeviceRepair>  queryMaintenanceDeviceRepairWithUser(String code);
	/**
	 * 获取当前人员的已完成的维修单
	 */
	public List<DeviceRepair>  queryCompletedDeviceRepairWithUser(String code);
	 
	 /**
	  * 获取报修人为当前用户的待确认单的报修单
	  */
	 public List<DeviceRepair> queryWorkshopcomfirmWithInformant(String InformantCode);
	 /**
	  * 返修
	  */
	 public Boolean updateDeviceRepairForConfirmAndReword(Long id,String status,String username);
	 /**
	  * 车间确认或维修完成(接收报修单)
	 * @throws Exception 
	  */
	 public Boolean updateDeviceRepairOrderStatusById(Long id,String status,String username) throws Exception;
	 /**
	  * 新增报修单
	  */
	 public Boolean addDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String username,
				HttpServletRequest request);
	 /**
	  * 无纸化新增报修单
	  */
	 public Boolean addPaperLessDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String employeeCode,
			 HttpServletRequest request);
	 /**
	     * 根据日期查找维修单数
	     * @param date
	     * @return
	     */
	   public long queryDeviceRepairOrderCountByDate(Date date);
	   /**
		 * 自动派单
		 */
		public boolean addAutoSendDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String name,
				HttpServletRequest request);
		/**
		 * 手动派单
		 */
		public void addManualSendDeviceRepairOrder(DeviceRepair deviceRepairOrder, String idList, String code,
				HttpServletRequest request);
		/**
		 * 查询角标对应的数据数量
		 */
		public int queryBadgeWithDeviceRepair(String hql);
	/**
	 * 根据DeviceProject的id查询是否有对应的故障类别
	 */
	public List<DeviceRepair> queryDeviceRepairNumByDeviceProjectId(Long id);
}
