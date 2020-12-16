package com.digitzones.service.impl;

import com.digitzones.dao.IEquipmentPrintDao;
import com.digitzones.model.EquipmentPrint;
import com.digitzones.model.Pager;
import com.digitzones.service.IEquipmentPrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxf
 * @date 2020-5-6
 * @time 13:36
 */
@Service
public class EquipmentPrintServiceImpl implements IEquipmentPrintService {
    @Autowired
    private IEquipmentPrintDao equipmentPrintDao;

    @Override
    public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return equipmentPrintDao.findByPage(hql, pageNo, pageSize, values);
    }

    @Override
    public void updateObj(EquipmentPrint obj) {
        equipmentPrintDao.update(obj);
    }

    @Override
    public EquipmentPrint queryByProperty(String name, String value) {
        return equipmentPrintDao.findSingleByProperty(name, value);
    }

    @Override
    public Serializable addObj(EquipmentPrint obj) {
        return equipmentPrintDao.save(obj);
    }

    @Override
    public EquipmentPrint queryObjById(Serializable id) {
        return equipmentPrintDao.findById(id);
    }

    @Override
    public void deleteObj(Serializable id) {
        equipmentPrintDao.deleteById(id);
    }

    @Override
    public List<EquipmentPrint> queryEquipmentPrintByCodeAndBatchNo(String code, String batchNo) {
        return equipmentPrintDao.findByHQL("from EquipmentPrint ep where ep.code=?0 and ep.batchNo=?1 ",new Object[]{code,batchNo});
    }
}
