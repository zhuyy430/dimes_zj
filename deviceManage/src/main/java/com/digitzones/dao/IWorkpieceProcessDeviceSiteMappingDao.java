package com.digitzones.dao;

import com.digitzones.model.WorkpieceProcessDeviceSiteMapping;
/**
 * 工件工序站点关联dao
 * @author zdq
 * 2018年6月15日
 */
public interface IWorkpieceProcessDeviceSiteMappingDao extends ICommonDao<WorkpieceProcessDeviceSiteMapping> {
	/**
	 * 根据工件id查询'工件工序站点'数量
	 * @param workpieceCode
	 * @return
	 */
	public Long queryCountByWorkpieceId(String workpieceCode);
	/**
	 * 根据工件ID，工序ID和设备站点ID查找加工节拍
	 * @param workPieceCode
	 * @param processId
	 * @param deviceSiteId
	 * @return
	 */
	public Float queryProcessingBeat(String workPieceCode,Long processId,Long deviceSiteId);
	/**
	 * 根据生产单元id查询该生产单元下的加工节拍
	 * @param productionUnitId
	 * @return
	 */
	public Float queryProcessingBeatByProductionUnitId(Long productionUnitId);
	/**
	 * 根据班次id查询该班次下的加工节拍
	 * @param classesId
	 * @return
	 */
	public Float queryProcessingBeatByClassesId(Long classesId);
}
