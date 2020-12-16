package com.digitzones.service.impl;

import com.digitzones.dao.IMaterialRequisitionDao;
import com.digitzones.dao.IMaterialRequisitionDetailDao;
import com.digitzones.model.*;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.Warehouse;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IWarehouseService;
import com.digitzones.procurement.service.IWarehousingApplicationFormDetailService;
import com.digitzones.service.*;
import com.digitzones.util.StringUtil;
import com.digitzones.xml.MaterialRequisitionSlipUtil;
import com.digitzones.xml.model.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MaterialRequisitionServiceImpl implements IMaterialRequisitionService {
    @Autowired
    private IMaterialRequisitionDao materialRequisitionDao;
    @Autowired
    private IMaterialRequisitionDetailDao materialRequisitionDetailDao;
    @Autowired
    private IWorkSheetService workSheetService;
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IWarehousingApplicationFormDetailService warehousingApplicationFormDetailService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    private static final String FORMNO_PREFIX = "SCLL-";
    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    @Autowired
    private MaterialRequisitionSlipUtil materialRequisitionSlipUtil;
    @Autowired
    private ILocationService locationService;
    /**
     * 分页查询对象
     * @param hql
     * @param pageNo
     * @param pageSize
     * @param values
     * @return
     */
    @Override
    public Pager<MaterialRequisition> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return materialRequisitionDao.findByPage(hql, pageNo, pageSize, values);
    }

    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(MaterialRequisition obj) {
        materialRequisitionDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public MaterialRequisition queryByProperty(String name, String value) {
        return materialRequisitionDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(MaterialRequisition obj) {
        return materialRequisitionDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public MaterialRequisition queryObjById(Serializable id) {
        return materialRequisitionDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        materialRequisitionDao.deleteById(id);
    }

    /**
     * 根据领用日期查找最大领用单号
     *
     * @param now 领用日期
     * @return
     */
    @Override
    public String queryMaxFormNoByPickingDate(Date now) {
        return materialRequisitionDao.queryMaxFormNoByPickingDate(now);
    }

    /**
     * 新增领用工单
     * @param form    领用工单对象
     * @param details 领用工单详情
     */
    @Override
    public void addMaterialRequisition(MaterialRequisition form, List<MaterialRequisitionDetail> details) {
        materialRequisitionDao.save(form);
        for(MaterialRequisitionDetail detail : details){
            detail.setMaterialRequisition(form);
            detail.setSurplusNum(detail.getAmount());
            materialRequisitionDetailDao.save(detail);
        }
    }

    /**
     * 更新领用工单
     * @param form       领用工单对象
     * @param details    领用工单详情
     * @param deletedIds 删除的id
     */
    @Override
    public void addMaterialRequisition(MaterialRequisition form, List<MaterialRequisitionDetail> details, List<String> deletedIds) {
        //更新领料单对象
        MaterialRequisition waf = materialRequisitionDao.findById(form.getFormNo());
        copyProperties4MaterialRequisition(form,waf);
        materialRequisitionDao.update(waf);
        //删除领料单详情
        if(!CollectionUtils.isEmpty(deletedIds)){
            for(String detailId : deletedIds){
                MaterialRequisitionDetail mdetail=materialRequisitionDetailDao.findById(detailId);
                BoxBar boxBar=boxBarService.queryBoxBarBybarCode(Long.valueOf(mdetail.getBarCode()));
                boxBar.setSurplusNum(boxBar.getSurplusNum()+mdetail.getAmount());
                boxBarService.updateObj(boxBar);
                materialRequisitionDetailDao.deleteById(detailId);
                List<MaterialRequisitionDetail> list=materialRequisitionDetailDao.findByHQL("from MaterialRequisitionDetail m where m.barCode=?0",new Object[]{boxBar.getBarCode().toString()});
                if(list!=null&&list.size()>0){

                }else{
                    JobBookingFormDetail jobBookingFormDetail=jobBookingFormDetailService.queryObjById(boxBar.getFkey());
                    jobBookingFormDetail.setHaveRequisition(false);
                    jobBookingFormDetail.setRequisitionDate(null);
                    jobBookingFormDetailService.updateObj(jobBookingFormDetail);
                    boxBar.setHaveRequisition(false);
                    boxBar.setRequisitionDate(null);
                }
                boxBarService.updateObj(boxBar);
            }
        }
        //更新领料单详情
        if(!CollectionUtils.isEmpty(details)){
            for (MaterialRequisitionDetail detail : details){
            	detail.setSurplusNum(detail.getAmount());
                MaterialRequisitionDetail wafd = materialRequisitionDetailDao.findById(detail.getId());
                if(wafd==null){
                    detail.setMaterialRequisition(waf);
                    materialRequisitionDetailDao.save(detail);
                }else {
                    BoxBar boxBar=boxBarService.queryBoxBarBybarCode(Long.valueOf(wafd.getBarCode()));
                    if(detail.getAmount()>wafd.getAmount()){
                        boxBar.setSurplusNum(boxBar.getSurplusNum()-(detail.getAmount()-wafd.getAmount()));
                    }else{
                        boxBar.setSurplusNum(boxBar.getSurplusNum()+(wafd.getAmount()-detail.getAmount()));
                    }
                    boxBarService.updateObj(boxBar);
                    copyProperties4MaterialRequisitionDetail(detail,wafd);
                    materialRequisitionDetailDao.update(wafd);
                }
            }
        }
    }
    /**
     * 根据领料单号删除领料单及详情
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        materialRequisitionDao.deleteById(formNo);
        materialRequisitionDetailDao.deleteByFormNo(formNo);
    }

    @Override
    public void outWarehouse(User user, String workSheetNo, String BarCodes, String receiveAmounts,String posCodes, String warehouseCode) {
        Date now = new Date();
        Warehouse warehouse=warehouseService.queryByProperty("cWhCode",warehouseCode);
        String formNo = queryMaxFormNoByPickingDate(now);
        String requestNo = "";
        if(!StringUtils.isEmpty(formNo)){
            requestNo = StringUtil.increment(formNo);
        }else{
            requestNo = FORMNO_PREFIX + format.format(now) + "000";
        }

        if(receiveAmounts!=null && receiveAmounts.contains("'")) {
            receiveAmounts = receiveAmounts.replace("'", "");
        }
        String[] ramounts= receiveAmounts.split(",");

        if(BarCodes!=null && BarCodes.contains("'")) {
            BarCodes = BarCodes.replace("'", "");
        }
        String[] barcodes= BarCodes.split(",");

        if(posCodes!=null && posCodes.contains("'")) {
            posCodes = posCodes.replace("'", "");
        }
        String[] poscodes= posCodes.split(",");

        WorkSheet workSheet=workSheetService.queryByProperty("no",workSheetNo);
        MaterialRequisition materialRequisition=new MaterialRequisition();
        materialRequisition.setFormNo(requestNo);
        materialRequisition.setPickingDate(new Date());
        materialRequisition.setPickerCode(user.getEmployee().getCode());
        materialRequisition.setPickerName(user.getEmployee().getName());
        materialRequisition.setWorkSheet(workSheet);
        materialRequisition.setWarehouseCode(warehouse.getcWhCode());
        materialRequisition.setWarehouseName(warehouse.getcWhName());
        materialRequisition.setStatus("已生成");
        List<MaterialRequisitionDetail> list=new ArrayList<>();
        for(int i=0;i<barcodes.length;i++) {
            BoxBar b = boxBarService.queryBoxBarBybarCode(Long.valueOf(barcodes[i]));
            Location location = null;
            if (poscodes != null && poscodes.length > 0){
                location = locationService.queryByProperty("cPosCode", poscodes[i]);
            }
            MaterialRequisitionDetail mrd=new MaterialRequisitionDetail();
            if(b.getSurplusNum()<Double.parseDouble(ramounts[i])){
                throw new RuntimeException(b.getBarCode()+"箱余量改变,请修改后重新提交");
            }
            if(b.getBoxBarType().equals("采购入库单")){
                WarehousingApplicationFormDetail detail = warehousingApplicationFormDetailService.queryObjById(b.getFkey());
                mrd.setSpecificationType(detail.getSpecificationType());
                mrd.setBatchNumber(detail.getBatchNumber());
                mrd.setFurnaceNumber(detail.getFurnaceNumber());
            }else if(b.getBoxBarType().equals("报工单")){
                JobBookingFormDetail jobBookingFormDetail=jobBookingFormDetailService.queryObjById(b.getFkey());
                mrd.setSpecificationType(jobBookingFormDetail.getSpecificationType());
                mrd.setBatchNumber(jobBookingFormDetail.getBatchNumber());
                mrd.setFurnaceNumber(jobBookingFormDetail.getFurnaceNumber());
                jobBookingFormDetail.setHaveRequisition(true);
                if(jobBookingFormDetail.getRequisitionDate()==null){
                    jobBookingFormDetail.setRequisitionDate(new Date());
                }

                jobBookingFormDetailService.updateObj(jobBookingFormDetail);
            }

            b.setSurplusNum(b.getSurplusNum()-Double.valueOf(ramounts[i]));
            b.setHaveRequisition(true);
            if(b.getRequisitionDate()==null){
                b.setRequisitionDate(new Date());
            }
            if("20".equals(warehouse.getcWhCode())) {
                if(location!=null) {
                    b.setPositonCode(location.getcPosCode());
                    b.setPositonName(location.getcPosName());

                    mrd.setPositionCode(location.getcPosCode());
                    mrd.setPositionName(location.getcPosName());
                }
            }
            mrd.setBarCode(b.getBarCode().toString());
            mrd.setInventoryCode(b.getInventoryCode());
            mrd.setInventoryName(b.getInventoryName());
            mrd.setAmountOfBoxes(b.getAmountOfBoxes());
            mrd.setAmount(Double.valueOf(ramounts[i]));
            mrd.setBoxNum(b.getBoxNum());
            mrd.setBatchNumber(b.getBatchNumber());
            list.add(mrd);
            boxBarService.updateObj(b);
        }
        addMaterialRequisition(materialRequisition,list);
        List<MaterialRequisitionDetail> details = materialRequisitionDetailDao.findByHQL("from MaterialRequisitionDetail detail where detail.materialRequisition.formNo=?0",new Object[]{materialRequisition.getFormNo()});
        if(workSheet!=null && materialRequisition!=null) {
            Result result = materialRequisitionSlipUtil.generateMaterialRequisitionSlip(workSheet, materialRequisition, details);
            if(result.getStatusCode()!=200){
                throw new RuntimeException(result.getMessage());
            }else{
                materialRequisition.setStatus("已生成");
                materialRequisitionDao.update(materialRequisition);
            }
        }
    }
    /**
     * 生成ERP领料单
     * @param formNo 领料单号
     * @param no     工单单号
     */
    @Override
    public void generateERPMaterialRequisition(String formNo, String no) {
        WorkSheet workSheet = workSheetService.queryByProperty("no",no);
        MaterialRequisition materialRequisition = materialRequisitionDao.findById(formNo);
        List<MaterialRequisitionDetail> details = materialRequisitionDetailDao.findByHQL("from MaterialRequisitionDetail detail where detail.materialRequisition.formNo=?0",new Object[]{formNo});
        if(workSheet!=null && materialRequisition!=null) {
            Result result = materialRequisitionSlipUtil.generateMaterialRequisitionSlip(workSheet, materialRequisition, details);
            if(result.getStatusCode()!=200){
                throw new RuntimeException(result.getMessage());
            }else{
                materialRequisition.setStatus("已生成");
                materialRequisitionDao.update(materialRequisition);
            }
        }
    }

    private void copyProperties4MaterialRequisition(MaterialRequisition form, MaterialRequisition waf) {
        waf.setPickingDate(form.getPickingDate());
        waf.setWorkSheet(form.getWorkSheet());
    }
    private void copyProperties4MaterialRequisitionDetail(MaterialRequisitionDetail form, MaterialRequisitionDetail waf) {
    	Double surplusNum = waf.getSurplusNum()+(form.getAmount()-waf.getAmount());
    	if(surplusNum<0) {
    		throw new RuntimeException("剩余数量不合法!");
    	}
    	waf.setSurplusNum(surplusNum);
        waf.setAmount(form.getAmount());
        waf.setNote(form.getNote());
    }
}
