package com.digitzones.service.impl;

import com.digitzones.dao.IERPEquipmentDao;
import com.digitzones.model.ERPEquipment;
import com.digitzones.model.Pager;
import com.digitzones.service.IERPEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * @author xuxf
 * @date 2020-5-6
 * @time 12:52
 */
@Service
public class ERPEquipmentServiceImpl implements IERPEquipmentService {
    @Autowired
    private IERPEquipmentDao erpEquipmentDao;

    @Override
    public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return erpEquipmentDao.findByPage(hql, pageNo, pageSize, values);
    }

    @Override
    public void updateObj(ERPEquipment obj) {
        erpEquipmentDao.update(obj);
    }

    @Override
    public ERPEquipment queryByProperty(String name, String value) {
        return erpEquipmentDao.findSingleByProperty(name, value);
    }

    @Override
    public Serializable addObj(ERPEquipment obj) {
        return erpEquipmentDao.save(obj);
    }

    @Override
    public ERPEquipment queryObjById(Serializable id) {
        return erpEquipmentDao.findById(id);
    }

    @Override
    public void deleteObj(Serializable id) {
        erpEquipmentDao.deleteById(id);
    }

    @Override
    public ERPEquipment queryERPEquipmentByCodeAndBatchNo(String code, String batchNo) {
        List<ERPEquipment> list= erpEquipmentDao.findByHQL("from ERPEquipment ee where ee.code=?0 and ee.batchNo=?1",new Object[]{code,batchNo});
        if(list!=null&&list.size()>0){
            return list.get(0);
        }else{
            return null;
        }
    }
}
