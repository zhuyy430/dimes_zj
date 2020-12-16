package com.digitzones.dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.digitzones.model.Equipment;
/**
 * 装备dao
 * @author zdq
 * 2018年6月11日
 */
public interface IEquipmentDao extends ICommonDao<Equipment> {
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
	 * @return
	 */
	public void updateEquipment(Equipment equipment,File pic);
}
