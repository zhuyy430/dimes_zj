package com.digitzones.dao;

import com.digitzones.model.DeliveryPlanDetail;

/**
 *发货计划详情
 */
public interface IDeliveryPlanDetailDao extends ICommonDao<DeliveryPlanDetail> {
   public void deleteByFormNo(String formNo);
}
