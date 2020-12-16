package com.digitzones.service.impl;

import com.digitzones.dao.IEquipmentDao;
import com.digitzones.dao.IEquipmentDeviceSiteMappingDao;
import com.digitzones.dao.IDeliveryPlanDao;
import com.digitzones.dao.IDeliveryPlanDetailDao;
import com.digitzones.model.*;
import com.digitzones.procurement.controllers.BoxBarController;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.service.IDeliveryPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeliveryPlanServiceImpl implements IDeliveryPlanService {
    @Autowired
    private IDeliveryPlanDao deliveryPlanDao;
    @Autowired
    private IDeliveryPlanDetailDao deliveryPlanDetailDao;
    @Autowired
    private IBoxBarService boxBarService;
    /**
     * 分页查询对象
     * @param hql
     * @param pageNo
     * @param pageSize
     * @param values
     * @return
     */
    @Override
    public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return deliveryPlanDao.findByPage(hql,pageNo,pageSize,values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(DeliveryPlan obj) {
        deliveryPlanDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public DeliveryPlan queryByProperty(String name, String value) {
        return deliveryPlanDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     *
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(DeliveryPlan obj) {
        return deliveryPlanDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public DeliveryPlan queryObjById(Serializable id) {
        return deliveryPlanDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        deliveryPlanDao.deleteById(id);
    }
    /**
     * 保存发货单(新增)
     * @param form
     * @param details
     */
    @Override
    public void addDeliveryPlan(DeliveryPlan form, List<DeliveryPlanDetail> details) {
        deliveryPlanDao.save(form);
        for(DeliveryPlanDetail detail : details){
            detail.setDeliveryPlan(form);
            deliveryPlanDetailDao.save(detail);
        }
    }
    /**
     * 保存发货单(查看)
     * @param form       发货单对象
     * @param details    新增或更新的发货单详情列表
     * @param deletedIds 删除的发货单详情id
     */
    @Override
    public void addDeliveryPlan(DeliveryPlan form, List<DeliveryPlanDetail> details, List<String> deletedIds) {
        //更新发货单对象
        DeliveryPlan waf = deliveryPlanDao.findById(form.getFormNo());
        waf.setDeliverDate(form.getDeliverDate());
        deliveryPlanDao.update(waf);
        //删除发货单详情
        if(!CollectionUtils.isEmpty(deletedIds)){
            for(String detailId : deletedIds){
                deliveryPlanDetailDao.deleteById(detailId);
            }
        }
        //更新发货单
        if(!CollectionUtils.isEmpty(details)){
            for (DeliveryPlanDetail detail : details){
                DeliveryPlanDetail wafd = deliveryPlanDetailDao.findById(detail.getId());
                if(wafd==null){
                    detail.setDeliveryPlan(form);
                    deliveryPlanDetailDao.save(detail);
                }else {
                    BeanUtils.copyProperties(detail,wafd);
                    wafd.setDeliveryPlan(form);
                    deliveryPlanDetailDao.update(wafd);
                }
            }
        }
    }
    /**
     * 根据发货日期查找最大发货单单号
     * @param date 发货日期
     * @return 最大发货单单号
     */
    @Override
    public String queryMaxFormNoByDeliverDate(Date date) {
        return deliveryPlanDao.queryMaxFormNoByDeliverDate(date);
    }
    /**
     * 根据发货单号删除发货单及详情信息
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        deliveryPlanDao.deleteById(formNo);
        deliveryPlanDetailDao.deleteByFormNo(formNo);
    }
    /**
     * 审核发货单
     * @param form 发货单对象
     */
    @Override
    public void audit(DeliveryPlan form) {
        deliveryPlanDao.update(form);
    }
    /**
     * 反审核
     * @param form 发货单对象
     */
    @Override
    public void unaudit(DeliveryPlan form) {
        deliveryPlanDao.update(form);
    }
}
