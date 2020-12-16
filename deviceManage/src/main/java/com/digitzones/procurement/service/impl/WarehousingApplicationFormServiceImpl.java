package com.digitzones.procurement.service.impl;

import com.digitzones.model.Pager;
import com.digitzones.procurement.controllers.BoxBarController;
import com.digitzones.procurement.dao.IWarehousingApplicationFormDao;
import com.digitzones.procurement.dao.IWarehousingApplicationFormDetailDao;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.WarehousingApplicationForm;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IWarehousingApplicationFormService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class WarehousingApplicationFormServiceImpl implements IWarehousingApplicationFormService {
    @Autowired
    private IWarehousingApplicationFormDao warehousingApplicationFormDao;
    @Autowired
    private IWarehousingApplicationFormDetailDao warehousingApplicationFormDetailDao;
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
        return warehousingApplicationFormDao.findByPage(hql,pageNo,pageSize,values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(WarehousingApplicationForm obj) {
        warehousingApplicationFormDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public WarehousingApplicationForm queryByProperty(String name, String value) {
        return warehousingApplicationFormDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     *
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(WarehousingApplicationForm obj) {
        return warehousingApplicationFormDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public WarehousingApplicationForm queryObjById(Serializable id) {
        return warehousingApplicationFormDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        warehousingApplicationFormDao.deleteById(id);
    }

    /**
     * 根据收料日期查找最大入库申请单单号
     * @param date 收料日期
     * @return 最大入库申请单单号
     */
    @Override
    public String queryMaxRequestNoByReceivingDate(Date date) {
        return warehousingApplicationFormDao.queryMaxRequestNoByReceivingDate(date);
    }
    /**
     * 保存入库申请单(新增)
     * @param form
     * @param details
     */
    @Override
    public void addWarehousingApplicationForm(WarehousingApplicationForm form, List<WarehousingApplicationFormDetail> details) {
        warehousingApplicationFormDao.save(form);
        for(WarehousingApplicationFormDetail detail : details){
            detail.setWarehousingApplicationForm(form);
            warehousingApplicationFormDetailDao.save(detail);
        }
    }

    /**
     * 保存入库申请单(查看)
     * @param form       入库申请单对象
     * @param details    新增或更新的入库申请单详情列表
     * @param deletedIds 删除的入库申请单详情id
     */
    @Override
    public void addWarehousingApplicationForm(WarehousingApplicationForm form, List<WarehousingApplicationFormDetail> details, List<String> deletedIds) {
        //更新入库申请单对象
        WarehousingApplicationForm waf = warehousingApplicationFormDao.findById(form.getFormNo());
        copyProperties4WarehousingApplicationForm(form,waf);
        warehousingApplicationFormDao.update(waf);
        //删除入库申请单详情
        if(!CollectionUtils.isEmpty(deletedIds)){
            for(String detailId : deletedIds){
                warehousingApplicationFormDetailDao.deleteById(detailId);
            }
        }
        //更新入库申请单
        if(!CollectionUtils.isEmpty(details)){
            for (WarehousingApplicationFormDetail detail : details){
                WarehousingApplicationFormDetail wafd = warehousingApplicationFormDetailDao.findById(detail.getId());
                if(wafd==null){
                    detail.setWarehousingApplicationForm(form);
                    warehousingApplicationFormDetailDao.save(detail);
                }else {
                    copyProperties4WarehousingApplicationFormDetail(detail,wafd);
                    warehousingApplicationFormDetailDao.update(wafd);
                }
            }
        }
    }
    /**
     * 根据入库申请单号删除入库申请单及详情信息
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        warehousingApplicationFormDao.deleteById(formNo);
        warehousingApplicationFormDetailDao.deleteByFormNo(formNo);
    }
    /**
     * 审核入库申请单
     * @param form 入库申请单对象
     */
    @Override
    public void audit(WarehousingApplicationForm form) {
        warehousingApplicationFormDao.update(form);
        //产生箱号条码
        String hql = "from WarehousingApplicationFormDetail d where d.warehousingApplicationForm.formNo=?0";
        List<WarehousingApplicationFormDetail> details = warehousingApplicationFormDetailDao.findByHQL(hql,new Object[]{form.getFormNo()});
        //查询最大箱号条码
        Integer maxBoxBarCode = boxBarService.queryMaxBoxBarCode();
        if(maxBoxBarCode==null || maxBoxBarCode ==0){
            maxBoxBarCode = (int)BoxBarController.START_BOXBAR_CODE-1;
        }
        if(!CollectionUtils.isEmpty(details)){
            for(WarehousingApplicationFormDetail detail : details){
                //箱号初始值
                int i = 1;
                //拆箱后的list
                List<WarehousingApplicationFormDetail> list = unpacking(detail);
                for(WarehousingApplicationFormDetail d : list) {
                    BoxBar boxBar = new BoxBar();
                    boxBar.setAmount(detail.getAmount());
                    boxBar.setAmountOfPerBox(d.getAmount());
                    boxBar.setSurplusNum(boxBar.getAmountOfPerBox());
                    boxBar.setAmountOfBoxes(d.getAmountOfBoxes());
                    boxBar.setBoxNum(i++);
                    boxBar.setPositonCode(d.getLocationCode());
                    boxBar.setPositonName(d.getLocationName());
                    boxBar.setFormNo(form.getFormNo());
                    boxBar.setFurnaceNumber(detail.getFurnaceNumber());
                    boxBar.setBatchNumber(detail.getBatchNumber());
                    boxBar.setUnitName(detail.getUnit());
                    boxBar.setSpecificationType(detail.getSpecificationType());
                    boxBar.setPurchasingNo(detail.getPurchasingNo());
                    boxBar.setClassName(detail.getClass().getName());
                    boxBar.setTableName("WarehousingApplicationFormDetail");
                    boxBar.setFkey(detail.getId());
                    boxBar.setInventoryCode(detail.getInventoryCode());
                    boxBar.setInventoryName(detail.getInventoryName());
                    boxBar.setSource(form.getVendorName());
                    boxBar.setBarCode(Long.valueOf(++maxBoxBarCode));
                    boxBar.setStoveNumber(detail.getStoveNumber());
                    boxBar.setManufacturer(detail.getManufacturer());
                    boxBarService.addObj(boxBar);
                }
            }
        }
    }

    /**
     * 反审核
     * @param form 入库申请单对象
     */
    @Override
    public void unaudit(WarehousingApplicationForm form) {
        warehousingApplicationFormDao.update(form);
        boxBarService.deleteByFormNo(form.getFormNo());
    }

    /**
     * 根据箱号条码查找入库申请单
     * @param barCode
     * @return
     */
    @Override
    public WarehousingApplicationForm queryByBarCode(String barCode) {
        return warehousingApplicationFormDao.queryByBarCode(barCode);
    }

    /**
     * 拆箱
     * @param unpackingDetail
     * @return
     */
    private List<WarehousingApplicationFormDetail> unpacking(WarehousingApplicationFormDetail unpackingDetail){
        List<WarehousingApplicationFormDetail> list = new ArrayList<>();
        //计算要拆的箱数
        int boxesCount = unpackingDetail.getAmountOfBoxes().intValue();
        //int boxesCount = (int)Math.floor(unpackingDetail.getAmount()/unpackingDetail.getAmountOfPerBox());
       // double lastBoxCount = unpackingDetail.getAmount() % unpackingDetail.getAmountOfPerBox();
       /* if(lastBoxCount>0){
            boxesCount+=1;
        }*/
        if(boxesCount>1){
            int boxCount = boxesCount;
           /* if(lastBoxCount>0){
                boxCount--;
            }*/
            for(int i = 0;i<boxCount;i++){
                WarehousingApplicationFormDetail d = new WarehousingApplicationFormDetail();
                BeanUtils.copyProperties(unpackingDetail,d);
                d.setAmount(unpackingDetail.getAmountOfPerBox());
                d.setAmountOfBoxes(Double.valueOf(boxesCount));
                d.setAmountOfPerBox(unpackingDetail.getAmountOfPerBox());
                list.add(d);
            }
            /*if(lastBoxCount>0) {
                WarehousingApplicationFormDetail d = new WarehousingApplicationFormDetail();
                BeanUtils.copyProperties(unpackingDetail, d);
                d.setAmount(lastBoxCount);
                d.setAmountOfBoxes(Double.valueOf(boxesCount));
                d.setAmountOfPerBox(lastBoxCount);
                list.add(d);
            }*/
        }else{
            list.add(unpackingDetail);
        }
        return list;
    }
    /**
     * 入库申请单详情属性拷贝
     * @param src
     * @param dest
     */
    private void copyProperties4WarehousingApplicationFormDetail(WarehousingApplicationFormDetail src,WarehousingApplicationFormDetail dest){
        dest.setAmount(src.getAmount());
        dest.setBatchNumber(src.getBatchNumber());
        dest.setFurnaceNumber(src.getFurnaceNumber());
        dest.setAmountOfPerBox(src.getAmountOfPerBox());
        dest.setAmountOfBoxes(src.getAmountOfBoxes());
        dest.setWarehouseCode(src.getWarehouseCode());
        dest.setWarehouseName(src.getWarehouseName());
        dest.setManufacturer(src.getManufacturer());
        dest.setStoveNumber(src.getStoveNumber());
    }
    /**
     * 入库申请单属性拷贝
     * @param src
     * @param dest
     */
    private void copyProperties4WarehousingApplicationForm(WarehousingApplicationForm src,WarehousingApplicationForm dest){
        dest.setReceivingDate(src.getReceivingDate());
        dest.setVendorCode(src.getVendorCode());
        dest.setVendorName(src.getVendorName());
        dest.setWarehouseCode(src.getWarehouseCode());
        dest.setWarehouseName(src.getWarehouseName());
    }
}
