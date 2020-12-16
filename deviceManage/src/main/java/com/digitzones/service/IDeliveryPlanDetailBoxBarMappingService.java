package com.digitzones.service;

import com.digitzones.model.DeliveryPlanDetailBoxBarMapping;

import java.util.List;


/**
 * 发货单详情与箱子关联
 */
public interface IDeliveryPlanDetailBoxBarMappingService extends ICommonService<DeliveryPlanDetailBoxBarMapping>{

    public List<DeliveryPlanDetailBoxBarMapping> queryDeliveryPlanDetailBoxBarMappingsBybarCode(String barCode);
}
