package com.digitzones.service;

import com.digitzones.model.DeliveryPlan;
import com.digitzones.model.DeliveryPlanDetail;

import java.util.Date;
import java.util.List;

/**
 * 发货单service
 */
public interface IDeliveryPlanService extends ICommonService<DeliveryPlan> {
    /**
     * 根据发货日期查找最大发货单单号
     * @param date 发货日期
     * @return 最大发货单单号
     */
    public String queryMaxFormNoByDeliverDate(Date date);

    /**
     * 保存发货单(新增)
     * @param form
     * @param details
     */
    public void addDeliveryPlan(DeliveryPlan form, List<DeliveryPlanDetail> details);
    /**
     * 保存发货单(查看)
     * @param form 发货单对象
     * @param details 新增或更新的发货单详情列表
     * @param deletedIds 删除的发货单详情id
     */
    public void addDeliveryPlan(DeliveryPlan form, List<DeliveryPlanDetail> details, List<String> deletedIds);
    /**
     * 根据发货单号删除发货单及详情信息
     * @param formNo
     */
    public void deleteByFormNo(String formNo);
    /**
     * 审核发货单
     * @param form 发货单对象
     */
    public void audit(DeliveryPlan form);
    /**
     * 反审核
     * @param form 发货单对象
     */
   public  void unaudit(DeliveryPlan form);
}
