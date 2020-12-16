package com.digitzones.service;

import java.util.List;

import com.digitzones.model.CraftsRouteProcessMapping;
/**
 * 工艺路线工序关联service
 * @author zdq
 * 2018年6月15日
 */
public interface ICraftsRouteProcessMappingService extends ICommonService<CraftsRouteProcessMapping> {
	/**
	 * 根据工艺路线id和工序id删除关联
	 * @param workpieceId
	 * @param processId
	 */
	public void deleteByCraftsIdAndProcessId(String CraftsId,Long processId);
	/**
	 * 根据工序和工艺路线id查找关联对象
	 * @param workpieceId
	 * @param processId
	 * @return
	 */
	public CraftsRouteProcessMapping queryByCraftsIdAndProcessId(String CraftsId,Long processId);
	/**
	 * 根据工艺路线id查找关联对象
	 * @param workpieceId
	 * @return
	 */
	public List<CraftsRouteProcessMapping> queryByCraftsId(String CraftsId);
	/**
	 * 按工艺路线顺序添加工件和工序关联对象
	 * @param workpieceId
	 * @return
	 */
	public void addCraftsProcessMapping(String CraftsId,String processesId);
	/**
	 * 上移工艺路线工序关联
	 * @param workpieceId
	 * @return
	 */
	public void updateShiftUpProcessRoute(String CraftsId,Long processId,String c_pMappingId);
	/**
	 * 下移工艺路线工序关联
	 * @param workpieceId
	 * @return
	 */
	public void updateShiftDownProcessRoute(String CraftsId,Long processId,String c_pMappingId);

	/**
	 * 根据工序id查找关联对象
	 * @param workpieceId
	 * @return
	 */
	public List<CraftsRouteProcessMapping> queryByProcessId(Long ProcessId);
}
