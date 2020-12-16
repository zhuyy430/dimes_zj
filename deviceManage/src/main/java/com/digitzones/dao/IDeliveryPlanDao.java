package com.digitzones.dao;
import com.digitzones.model.DeliveryPlan;

import java.util.Date;
/**
 * 发货计划dao
 */
public interface IDeliveryPlanDao extends ICommonDao<DeliveryPlan> {
    /**
     * 根据发货日期查找最大发货单单号
     * @param date 发货日期
     * @return 最大发货单单号
     */
    public String queryMaxFormNoByDeliverDate(Date date);
}
