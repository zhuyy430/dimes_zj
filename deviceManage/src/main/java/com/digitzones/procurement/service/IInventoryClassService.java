package com.digitzones.procurement.service;
import java.util.List;

import com.digitzones.procurement.model.InventoryClass;
import com.digitzones.service.ICommonService;
/**
 * 物料service
 * @author zhuyy430
 *
 */
public interface IInventoryClassService extends ICommonService<InventoryClass> {
	
	public List<InventoryClass> queryTopInventoryClass();

	/**
	 * 根据编号和层级查找物料类别子类别
	 * @param code
	 * @param level
	 * @return
	 */
	public List<InventoryClass> queryChildrenInventoryClass(String code,int level);
}
