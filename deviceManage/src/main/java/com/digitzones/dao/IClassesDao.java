package com.digitzones.dao;

import java.util.Date;

import com.digitzones.model.Classes;
/**
 * 班次数据访问接口
 * @author zdq
 * 2018年6月11日
 */
public interface IClassesDao extends ICommonDao<Classes> {
	/**
	 * 查询当前时间的班次 
	 * @return
	 */
	public Classes queryCurrentClasses();
	/**
	 * 查询当前时间下的班次类别下的班次 
	 * @return
	 */
	public Classes queryCurrentClassesByClassTypeName(String classTypeName);
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
	 * 根据设备站点代码查找班次信息
	 * @param date
	 * @return
	 */
	public Classes queryClassesByDeviceSiteCode(String deviceSiteCode);
}
