package com.digitzones.procurement.service.impl;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IInventoryEquipmentTypeMappingDao;
import com.digitzones.procurement.model.InventoryEquipmentTypeMapping;
import com.digitzones.procurement.service.IInventoryEquipmentTypeMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class InventoryEquipmentTypeMappingServiceImpl implements IInventoryEquipmentTypeMappingService {

    @Autowired
    private IInventoryEquipmentTypeMappingDao iInventoryEquipmentTypeMappingDao;

    @Override
    public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return iInventoryEquipmentTypeMappingDao.findByPage(hql, pageNo, pageSize, values);
    }

    @Override
    public void updateObj(InventoryEquipmentTypeMapping obj) {
        iInventoryEquipmentTypeMappingDao.update(obj);
    }

    @Override
    public InventoryEquipmentTypeMapping queryByProperty(String name, String value) {
        return iInventoryEquipmentTypeMappingDao.findSingleByProperty(name, value);
    }

    @Override
    public Serializable addObj(InventoryEquipmentTypeMapping obj) {
        return iInventoryEquipmentTypeMappingDao.save(obj);
    }

    @Override
    public InventoryEquipmentTypeMapping queryObjById(Serializable id) {
        return iInventoryEquipmentTypeMappingDao.findById(id);
    }

    @Override
    public void deleteObj(Serializable id) {
        iInventoryEquipmentTypeMappingDao.deleteById(id);
    }

    @Override
    public List<InventoryEquipmentTypeMapping> queryListByInventoryCode(String inventoryCode) {
        return iInventoryEquipmentTypeMappingDao.findByHQL("from InventoryEquipmentTypeMapping ie where ie.inventory.code=?0",new Object[]{inventoryCode});
    }

    @Override
    public List<InventoryEquipmentTypeMapping> queryByInventoryCodeAndEquipmentTypeId(String inventoryCode, Long equipmentTypeId) {
        return iInventoryEquipmentTypeMappingDao.findByHQL("from InventoryEquipmentTypeMapping ie where ie.inventory.code=?0 and ie.equipmentType.id=?1",new Object[]{inventoryCode,equipmentTypeId});
    }
}
