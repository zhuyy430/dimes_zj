package com.digitzones.service;

import com.digitzones.model.Classes;

import java.util.Date;
import java.util.List;
/**
 * 班次业务逻辑接口
 * @author zdq
 * 2018年6月11日
 */
public interface IClassesService extends ICommonService<Classes> {
	/**
	 * 查询当前时间的班次 
	 * @return
	 */
	public Classes queryCurrentClasses();
	/**
	 * 查询该班次类别当前时间的班次 
	 * @return
	 */
	public Classes queryCurrentClassesByClassesTypeName(String classesTypeName);
	/**
	 * 查询所有班次
	 * @return
	 */
	public List<Classes> queryAllClasses();
	/**
	 * 根据时间查找班次信息
	 * @param date
	 * @return
	 */
	public Classes queryClassesByTime(Date date);
	/**
	 * 根据时间和班次类别查找班次信息
	 * @param date
	 * @return
	 */
	public Classes queryClassesByTimeAndClassTypeName(Date date,String classTypeName);

	/**
	 * 通过生产单元查询班次
	 */
	public List<Classes> queryAllClassesByproductionUnitCode(String code);
	
	/**
	 * 根据设备站点代码获取班次信息
	 * @param code
	 * @return
	 */
	public Classes queryClassesByDeviceSiteCode(String code);
}

