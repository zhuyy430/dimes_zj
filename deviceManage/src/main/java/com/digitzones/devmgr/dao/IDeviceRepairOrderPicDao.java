package com.digitzones.devmgr.dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.DeviceRepairPic;
/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
public interface IDeviceRepairOrderPicDao extends ICommonDao<DeviceRepairPic> {
	public Serializable addOrderPic(DeviceRepairPic deviceRepairPic,File file);
	/**
	 * 根据报修单ID获取图片信息
	 * @param deviceRepairId
	 * @return
	 */
	public List<DeviceRepairPic> findByTaskId(Long deviceRepairId) ;
	
	/**
	 * 添加图片信息
	 * @param file
	 * @return
	 */
	public Serializable addDeviceRepairOrderPic(Long orderId,File file);
	
}
