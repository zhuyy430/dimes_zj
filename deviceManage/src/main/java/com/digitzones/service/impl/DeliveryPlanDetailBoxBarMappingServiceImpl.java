package com.digitzones.service.impl;

import com.digitzones.dao.IDeliveryPlanDetailBoxBarMappingDao;
import com.digitzones.model.DeliveryPlanDetailBoxBarMapping;
import com.digitzones.model.Pager;
import com.digitzones.service.IDeliveryPlanDetailBoxBarMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class DeliveryPlanDetailBoxBarMappingServiceImpl implements IDeliveryPlanDetailBoxBarMappingService {

    @Autowired
    private IDeliveryPlanDetailBoxBarMappingDao deliveryPlanDetailBoxBarMappingDao;


    @Override
    public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return deliveryPlanDetailBoxBarMappingDao.findByPage(hql, pageNo, pageSize, values);
    }

    @Override
    public void updateObj(DeliveryPlanDetailBoxBarMapping obj) {
        deliveryPlanDetailBoxBarMappingDao.update(obj);
    }

    @Override
    public DeliveryPlanDetailBoxBarMapping queryByProperty(String name, String value) {
        return deliveryPlanDetailBoxBarMappingDao.findSingleByProperty(name,value);
    }

    @Override
    public Serializable addObj(DeliveryPlanDetailBoxBarMapping obj) {
        return deliveryPlanDetailBoxBarMappingDao.save(obj);
    }

    @Override
    public DeliveryPlanDetailBoxBarMapping queryObjById(Serializable id) {
        return deliveryPlanDetailBoxBarMappingDao.findById(id);
    }

    @Override
    public void deleteObj(Serializable id) {
         deliveryPlanDetailBoxBarMappingDao.deleteById(id);
    }

    @Override
    public List<DeliveryPlanDetailBoxBarMapping> queryDeliveryPlanDetailBoxBarMappingsBybarCode(String barCode) {
        return deliveryPlanDetailBoxBarMappingDao.findByHQL("from DeliveryPlanDetailBoxBarMapping e where e.boxBar.barCode=?0",new Object[]{Long.valueOf(barCode)});
    }
}
