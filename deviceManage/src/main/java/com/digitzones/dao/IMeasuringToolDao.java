package com.digitzones.dao;

import java.io.File;
import java.io.Serializable;

import com.digitzones.model.Equipment;
/**
 * 量具dao
 * @author zdq
 * 2018年6月11日
 */
public interface IMeasuringToolDao extends ICommonDao<Equipment> {
	/**
	 * 添加量具
	 * @param equipment
	 * @param pic 装备图片文件
	 * @return
	 */
	public Serializable addMeasuringTool(Equipment equipment,File pic);
	/**
	 * 更新量具	 
	 * @param equipment
	 * @param pic
	 */
	public void updateMeasuringTool(Equipment equipment,File pic);
}
