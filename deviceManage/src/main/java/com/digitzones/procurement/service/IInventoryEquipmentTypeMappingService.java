package com.digitzones.procurement.service;

import com.digitzones.procurement.model.InventoryEquipmentTypeMapping;
import com.digitzones.service.ICommonService;

import java.util.List;

public interface IInventoryEquipmentTypeMappingService extends ICommonService<InventoryEquipmentTypeMapping> {

    /**
     * 通过物料代码和装备id查询是否有关联
     */
    public List<InventoryEquipmentTypeMapping> queryByInventoryCodeAndEquipmentTypeId(String inventoryCode,Long equipmentId);

    /**
     * 根据工件代码查询关联
     * @param inventoryCode
     * @return
     */
    public List<InventoryEquipmentTypeMapping> queryListByInventoryCode(String inventoryCode);
}
