package com.digitzones.service;

import com.digitzones.model.DeliveryPlanDetail;

import java.util.List;

/**
 * 发货单详情业务逻辑接口
 */

public interface IDeliveryPlanDetailService extends ICommonService<DeliveryPlanDetail> {
    /**
     * 根据发货单编号查找发货单详情信息
     * @param formNo 发货单编号
     * @return
     */
    List<DeliveryPlanDetail> queryByDeliveryPlanNo(String formNo);
    /**
     * 销售出库
     */
   public void outWarehouse(String[] barCodes,String[] ramounts,String deliverPlanDetailId,String warehouseCode);
}
