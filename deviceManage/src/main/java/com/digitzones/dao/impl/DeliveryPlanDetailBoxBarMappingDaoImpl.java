package com.digitzones.dao.impl;

import com.digitzones.dao.IDeliveryPlanDetailBoxBarMappingDao;
import com.digitzones.model.DeliveryPlanDetailBoxBarMapping;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryPlanDetailBoxBarMappingDaoImpl extends CommonDaoImpl<DeliveryPlanDetailBoxBarMapping> implements IDeliveryPlanDetailBoxBarMappingDao{
    public DeliveryPlanDetailBoxBarMappingDaoImpl() {
        super(DeliveryPlanDetailBoxBarMapping.class);
    }
}
