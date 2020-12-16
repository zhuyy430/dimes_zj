package com.digitzones.service;

import com.digitzones.model.Equipment;

import java.io.File;
import java.io.Serializable;
import java.util.List;
/**
 * 装备service
 * @author zdq
 * 2018年6月11日
 */
public interface IEquipmentService extends ICommonService<Equipment> {
	/**
	 * 查询所有装备
	 * @return
	 */
	public List<Equipment> queryAllEquipments();
	/**
	 * 查询所有未被停用的装备
	 */
	public List<Equipment> queryAllEquipmentsByNotStopUse();
	/**
	 * 根据量具编码或名称或规格型号查询装备信息
	 * @param value
	 * @return
	 */
	public List<Equipment> queryEquipmentsByCodeOrNameOrUnity(String value);
	/**
	 * 根据量具编码查询装备信息
	 * @param code
	 * @return
	 */
	public List<Equipment> queryEquipmentsByCode(String code);
	/**
	 * 添加装备
	 * @param equipment
	 * @param pic 装备图片文件
	 * @return
	 */
	public Serializable addEquipment(Equipment equipment,File pic);
	/**
	 * 更新装备
	 * @param equipment
	 * @param pic
	 */
	public void updateEquipment(Equipment equipment,File pic);
	/**
	 * 批量删除装备
	 * @param ids
	 * @return
	 */
	public boolean deleteEquipments(String[] ids);
	/**
	 * 批量停用装备
	 * @param ids
	 */
	public void disableEquipments(String[] ids);
}
