package com.digitzones.procurement.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.procurement.model.InventoryProcessMapping;
/**
 * 工件工序关联dao
 * @author zdq
 * 2018年6月15日
 */
public interface IInventoryProcessMappingDao extends ICommonDao<InventoryProcessMapping> {
	/**
	 * 根据工件id和工序id删除关联
	 * @param processId
	 */
	public void deleteByInventoryCodeAndProcessId(Long processId);
	/**
	 * 根据工件id删除关联
	 * @param InventoryCode
	 */
	public void deleteByInventoryCode(String InventoryCode);
	/**
	 * 根据工件id查询'工件 工序'
	 * @return
	 */
	public List<InventoryProcessMapping> queryByInventoryCode(String InventoryCode);
	/**
	 * 工具工单单号查找工序编码和名称
	 * @param no
	 * @return
	 */
    public List<Object[]> queryProcessCodeAndNameByNo(String no);
}
