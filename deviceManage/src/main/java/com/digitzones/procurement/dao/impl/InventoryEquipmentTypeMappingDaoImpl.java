package com.digitzones.procurement.dao.impl;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IInventoryEquipmentTypeMappingDao;
import com.digitzones.procurement.model.InventoryEquipmentTypeMapping;
import org.springframework.stereotype.Repository;

@Repository
public class InventoryEquipmentTypeMappingDaoImpl extends CommonDaoImpl<InventoryEquipmentTypeMapping> implements IInventoryEquipmentTypeMappingDao {

    public InventoryEquipmentTypeMappingDaoImpl(){
        super(InventoryEquipmentTypeMapping.class);
    }
}
