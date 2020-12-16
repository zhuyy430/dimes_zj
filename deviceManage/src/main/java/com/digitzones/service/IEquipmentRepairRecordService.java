package com.digitzones.service;

import com.digitzones.model.EquipmentRepairRecord;

import java.util.List;

/**
 * 装备维修记录service
 */
public interface IEquipmentRepairRecordService extends ICommonService<EquipmentRepairRecord> {
	/**
	 * 查询所有装备
	 * @return
	 */
	public List<EquipmentRepairRecord> queryAllEquipmentRepairRecords();
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public boolean deleteEquipmentRepairRecords(String[] ids);
}
