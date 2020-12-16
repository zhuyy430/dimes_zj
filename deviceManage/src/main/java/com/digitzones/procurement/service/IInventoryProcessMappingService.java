package com.digitzones.procurement.service;

import java.util.List;

import com.digitzones.procurement.model.InventoryProcessMapping;
import com.digitzones.service.ICommonService;
/**
 * 工件工序关联service
 * @author zdq
 * 2018年6月15日
 */
public interface IInventoryProcessMappingService extends ICommonService<InventoryProcessMapping> {
	/**
	 * 根据工件id和工序id删除关联
	 * @param workpieceId
	 * @param processId
	 */
	public void deleteByInventoryCodeAndProcessId(String InventoryCode,Long processId);
	/**
	 * 根据工件id删除关联
	 * @param InventoryCode
	 */
	public void deleteByInventoryCode(String InventoryCode);
	/**
	 * 根据工序和工件id查找关联对象
	 * @param processId
	 * @return
	 */
	public InventoryProcessMapping queryByInventoryCodeAndProcessId(String InventoryCode,Long processId);
	/**
	 * 根据工件id查找关联对象
	 * @return
	 */
	public List<InventoryProcessMapping> queryByInventoryCode(String InventoryCode);
	/**
	 * 按工艺路线顺序添加工件和工序关联对象
	 * @return
	 */
	public void addInventoryProcessMapping(String InventoryCode,String processesId, Boolean isCraftsRoute);
	/**
	 * 上移工件工序关联的工艺路线
	 * @return
	 */
	public void updateShiftUpProcessRoute(String InventoryCode,Long processId);
	/**
	 * 下移工件工序关联的工艺路线
	 * @return
	 */
	public void updateShiftDownProcessRoute(String InventoryCode,Long processId);
	/**
	 * 根据工单单号查找工序编码和名称
	 * @param no
	 * @return
	 */
	public List<Object[]> queryProcessCodeAndNameByNo(String no);
	/**
	 * 根据工序id查找关联对象
	 * @return
	 */
	public List<InventoryProcessMapping> queryByProcessId(Long id);
	/**
	 * 根据工件编码和工序编码查找
	 * @param workpieceCode
	 * @param processCode
	 * @return
	 */
    InventoryProcessMapping queryByInventoryCodeAndProcessCode(String workpieceCode, String processCode);
}
