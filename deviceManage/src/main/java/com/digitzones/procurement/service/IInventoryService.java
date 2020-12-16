package com.digitzones.procurement.service;
import com.digitzones.procurement.model.Inventory;
import com.digitzones.service.ICommonService;

import java.util.List;

/**
 * 物料service
 * @author zhuyy430
 *
 */
public interface IInventoryService extends ICommonService<Inventory> {
    public List<Inventory> queryAllInventory();
    /**
     * 根据条件查找工件信息
     * @param q
     * @return
     */
    List<Inventory> queryAllWorkpieces(String q);
    /**
     * 查找所有工件信息
     * @return
     */
    List<Inventory> queryAllWorkpieces();
    /**
     * 根据物料编码查找物料
     * @param codesList
     * @return
     */
    List<Inventory> queryByCodes(List<String> codesList);
}
