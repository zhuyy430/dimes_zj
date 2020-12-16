package com.digitzones.procurement.service.impl;

import com.digitzones.model.Pager;
import com.digitzones.procurement.controllers.BoxBarController;
import com.digitzones.procurement.dao.ITemporaryBarcodeDao;
import com.digitzones.procurement.dao.ITemporaryBarcodeDetailDao;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.TemporaryBarcode;
import com.digitzones.procurement.model.TemporaryBarcodeDetail;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.ITemporaryBarcodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TemporaryBarcodeServiceImpl implements ITemporaryBarcodeService {
    @Autowired
    private ITemporaryBarcodeDao temporaryBarcodeDao;
    @Autowired
    private ITemporaryBarcodeDetailDao temporaryBarcodeDetailDao;
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
        return temporaryBarcodeDao.findByPage(hql,pageNo,pageSize,values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(TemporaryBarcode obj) {
        temporaryBarcodeDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public TemporaryBarcode queryByProperty(String name, String value) {
        return temporaryBarcodeDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     *
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(TemporaryBarcode obj) {
        return temporaryBarcodeDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public TemporaryBarcode queryObjById(Serializable id) {
        return temporaryBarcodeDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        temporaryBarcodeDao.deleteById(id);
    }
    /**
     * 根据录入日期查找最大临时条码单号
     * @param date 录入日期
     * @return 最大临时条码单号
     */
    @Override
    public String queryMaxRequestNoByBillDate(Date date) {
        return temporaryBarcodeDao.queryMaxRequestNoByBillDate(date);
    }
    /**
     * 保存临时条码(新增)
     * @param form
     * @param details
     */
    @Override
    public void addTemporaryBarcode(TemporaryBarcode form, List<TemporaryBarcodeDetail> details) {
        temporaryBarcodeDao.save(form);
        for(TemporaryBarcodeDetail detail : details){
            detail.setTemporaryBarcode(form);
            temporaryBarcodeDetailDao.save(detail);
        }
    }

    /**
     * 保存临时条码(查看)
     * @param form       临时条码对象
     * @param details    新增或更新的临时条码详情列表
     * @param deletedIds 删除的临时条码详情id
     */
    @Override
    public void addTemporaryBarcode(TemporaryBarcode form, List<TemporaryBarcodeDetail> details, List<String> deletedIds) {
        //更新临时条码对象
        TemporaryBarcode waf = temporaryBarcodeDao.findById(form.getFormNo());
        copyProperties4TemporaryBarcode(form,waf);
        temporaryBarcodeDao.update(waf);
        //删除临时条码详情
        if(!CollectionUtils.isEmpty(deletedIds)){
            for(String detailId : deletedIds){
                temporaryBarcodeDetailDao.deleteById(detailId);
            }
        }
        //更新临时条码
        if(!CollectionUtils.isEmpty(details)){
            for (TemporaryBarcodeDetail detail : details){
                TemporaryBarcodeDetail wafd = temporaryBarcodeDetailDao.findById(detail.getId());
                if(wafd==null){
                    detail.setTemporaryBarcode(form);
                    temporaryBarcodeDetailDao.save(detail);
                }else {
                    copyProperties4TemporaryBarcodeDetail(detail,wafd);
                    temporaryBarcodeDetailDao.update(wafd);
                }
            }
        }
    }
    /**
     * 根据临时条码号删除临时条码及详情信息
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        temporaryBarcodeDao.deleteById(formNo);
        temporaryBarcodeDetailDao.deleteByFormNo(formNo);
    }
    /**
     * 审核临时条码
     * @param form 临时条码对象
     */
    @Override
    public void audit(TemporaryBarcode form) {
        temporaryBarcodeDao.update(form);
        //产生箱号条码
        String hql = "from TemporaryBarcodeDetail d where d.temporaryBarcode.formNo=?0";
        List<TemporaryBarcodeDetail> details = temporaryBarcodeDetailDao.findByHQL(hql,new Object[]{form.getFormNo()});
        //查询最大箱号条码
        Integer maxBoxBarCode = boxBarService.queryMaxBoxBarCode();
        if(maxBoxBarCode==null || maxBoxBarCode ==0){
            maxBoxBarCode = (int)BoxBarController.START_BOXBAR_CODE-1;
        }
        if(!CollectionUtils.isEmpty(details)){
            for(TemporaryBarcodeDetail detail : details){
                //箱号初始值
                int i = 1;
                //拆箱后的list
                List<TemporaryBarcodeDetail> list = unpacking(detail);
                for(TemporaryBarcodeDetail d : list) {
                    BoxBar boxBar = new BoxBar();
                    boxBar.setBoxBarType("临时条码单");
                    boxBar.setAmount(detail.getAmount());
                    boxBar.setAmountOfPerBox(d.getAmount());
                    boxBar.setAmountOfBoxes(d.getAmountOfBoxes());
                    boxBar.setBoxNum(i++);
                    boxBar.setFormNo(form.getFormNo());
                    boxBar.setFurnaceNumber(detail.getFurnaceNumber());
                    boxBar.setBatchNumber(detail.getBatchNumber());
                    boxBar.setStoveNumber(detail.getStoveNumber());
                    boxBar.setManufacturer(detail.getManufacturer());
                    boxBar.setPositonCode(detail.getPositonCode());
                    boxBar.setPositonName(detail.getPositonCode());
                    boxBar.setUnitName(detail.getUnit());
                    boxBar.setSpecificationType(detail.getSpecificationType());
                    boxBar.setClassName(detail.getClass().getName());
                    boxBar.setTableName("TemporaryBarcodeDetail");
                    boxBar.setFkey(detail.getId());
                    boxBar.setInventoryCode(detail.getInventoryCode());
                    boxBar.setInventoryName(detail.getInventoryName());
                    boxBar.setSource("临时条码");
                    boxBar.setBarCode(Long.valueOf(++maxBoxBarCode));
                    boxBar.setEnterWarehouse(true);
                    boxBar.setSurplusNum(d.getAmount());
                    boxBarService.addObj(boxBar);
                }
            }
        }
    }
    /**
     * 反审核
     * @param form 临时条码对象
     */
    @Override
    public void unaudit(TemporaryBarcode form) {
        temporaryBarcodeDao.update(form);
        boxBarService.deleteByFormNo(form.getFormNo());
    }

    /**
     * 拆箱
     * @param unpackingDetail
     * @return
     */
    private List<TemporaryBarcodeDetail> unpacking(TemporaryBarcodeDetail unpackingDetail){
        List<TemporaryBarcodeDetail> list = new ArrayList<>();
        //计算要拆的箱数
        int boxesCount = (int)Math.floor(unpackingDetail.getAmount()/unpackingDetail.getAmountOfPerBox());
        double lastBoxCount = unpackingDetail.getAmount() % unpackingDetail.getAmountOfPerBox();
        if(lastBoxCount>0){
            boxesCount++;
        }
        if(boxesCount>1){
            int boxCount = boxesCount;
            if(lastBoxCount>0){
                boxCount--;
            }
            for(int i = 0;i<boxCount;i++){
                TemporaryBarcodeDetail d = new TemporaryBarcodeDetail();
                BeanUtils.copyProperties(unpackingDetail,d);
                d.setAmount(unpackingDetail.getAmountOfPerBox());
                d.setAmountOfBoxes(Double.valueOf(boxesCount));
                d.setAmountOfPerBox(unpackingDetail.getAmountOfPerBox());
                list.add(d);
            }
            if(lastBoxCount>0) {
                TemporaryBarcodeDetail d = new TemporaryBarcodeDetail();
                BeanUtils.copyProperties(unpackingDetail, d);
                d.setAmount(lastBoxCount);
                d.setAmountOfBoxes(Double.valueOf(boxesCount));
                d.setAmountOfPerBox(lastBoxCount);
                list.add(d);
            }
        }else{
            list.add(unpackingDetail);
        }
        return list;
    }
    /**
     * 临时条码详情属性拷贝
     * @param src
     * @param dest
     */
    private void copyProperties4TemporaryBarcodeDetail(TemporaryBarcodeDetail src,TemporaryBarcodeDetail dest){
        dest.setAmount(src.getAmount());
        dest.setBatchNumber(src.getBatchNumber());
        dest.setFurnaceNumber(src.getFurnaceNumber());
        dest.setStoveNumber(src.getStoveNumber());
        dest.setAmountOfPerBox(src.getAmountOfPerBox());
        dest.setAmountOfBoxes(src.getAmountOfBoxes());
        dest.setManufacturer(src.getManufacturer());
        dest.setPositonCode(src.getPositonCode());
    }
    /**
     * 临时条码属性拷贝
     * @param src
     * @param dest
     */
    private void copyProperties4TemporaryBarcode(TemporaryBarcode src,TemporaryBarcode dest){
        dest.setInputDate(src.getInputDate());
    }
}
