package com.digitzones.service;

import com.digitzones.model.Pager;
import com.digitzones.model.WorkpieceProcessDeviceSiteMapping;
/**
 * 工件工序站点service
 * @author zdq
 * 2018年6月15日
 */
public interface IWorkpieceProcessDeviceSiteMappingService extends ICommonService<WorkpieceProcessDeviceSiteMapping> {
	/**
	 * 根据工件id查询'工件工序站点',如果'工件工序站点'中不存在该工件信息，则将该工件相关的工序站点，添加到'工件工序站点'中
	 * @param workpieceId
	 * @param rows
	 * @param page
	 * @return
	 */
	public Pager<WorkpieceProcessDeviceSiteMapping> queryOrAddWorkpieceProcessDeviceSiteMappingByWorkpieceId(Long workpieceId,Integer rows,Integer page);
	/**
	 * 根据工件id，工序id，站点id查询映射信息
	 * @param workPieceId
	 * @param processId
	 * @param deviceSiteId
	 * @return
	 */
	public WorkpieceProcessDeviceSiteMapping queryByWorkPieceIdAndProcessIdAndDeviceSiteId(Long workPieceId,Long processId,Long deviceSiteId);

	/**
	 * 根据生产单元id查询该生产单元下的加工节拍
	 * @param productionUnitId
	 * @return
	 */
	public Float queryProcessingBeatByProductionUnitId(Long productionUnitId);
	/**
	 * 根据工件ID，工序ID和设备站点ID查找加工节拍
	 * @param workPieceCode
	 * @param processId
	 * @param deviceSiteId
	 * @return
	 */
	public Float queryProcessingBeat(String workPieceCode,Long processId,Long deviceSiteId);
	/**
	 * 根据班次id查询该班次下的加工节拍
	 * @param classesId
	 * @return
	 */
	public Float queryProcessingBeatByClassesId(Long classesId);
}
