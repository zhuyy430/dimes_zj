package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.service.ICommonService;
/**
 * 备品备件service
 * @author zdq
 * 2018年12月5日
 */
public interface ISparepartService extends ICommonService<Sparepart>{
	/**
	 * 根据设备代码获取备品备件信息
	 * @param code
	 * @return
	 */
	public List<Sparepart> querySparepartsByDeviceCode(String code);
	/**
	 * 根据设备代码获取备品备件信息
	 * @param code
	 * @return
	 */
	public List<Sparepart> querySparepartsByDeviceCode(String code,Long devicerepairId);
	/**
	 * 根据设备代码和保养计划记录id查找备件信息
	 * @param deviceCode
	 * @param maintenancePlanRecordId
	 * @return
	 */
	public List<Sparepart> queryMaintenanceSparepartsByDeviceCode(String deviceCode,Long maintenancePlanRecordId);
	/**
	 * 查询所有备品备件
	 * @return
	 */
	public List<Sparepart> queryAllSpareparts();
	/**
	 * 查询未使用的备件信息
	 * @return
	 */
	public List<Sparepart> queryAllSparepartsBymaintenancePlanRecordId(Long maintenancePlanRecordId);
	/**
	 * 查询未使用的备件信息
	 * @return
	 */
	public List<Sparepart> queryOtherSpareparts(String hql, List<Object> args);
	/**
	 * 查询未使用的备件信息
	 * @return
	 */
	public List<Sparepart> queryAllSparepartsByDeviceRepairId(Long deviceRepairId);
}
