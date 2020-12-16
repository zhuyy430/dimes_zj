package com.digitzones.service.impl;

import com.digitzones.dao.*;
import com.digitzones.model.*;
import com.digitzones.procurement.controllers.BoxBarController;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.model.Warehouse;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.procurement.service.IWarehouseService;
import com.digitzones.service.IEmployeeService;
import com.digitzones.service.IJobBookingFormDetailService;
import com.digitzones.service.IJobBookingFormService;
import com.digitzones.xml.JobBookingSlipUtil;
import com.digitzones.xml.model.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

@Service
public class JobBookingFormServiceImpl implements IJobBookingFormService {
    @Autowired
    private IJobBookingFormDao jobBookingFormDao;
    @Autowired
    private IJobBookingFormDetailDao jobBookingFormDetailDao;
    @Autowired
    private IBoxBarService boxBarService;
    @Autowired
    private IEquipmentDao equipmentDao;
    @Autowired
    private IEquipmentDeviceSiteMappingDao equipmentDeviceSiteMappingDao;
    @Autowired
    private IRawMaterialDao rawMaterialDao;
    @Autowired
    private IWorkSheetDetailDao workSheetDetailDao;
    @Autowired
    private IWarehouseService warehouseService;
    @Autowired
    private IJobBookingFormDetailService jobBookingFormDetailService;
    @Autowired
    private JobBookingSlipUtil jobBookingSlipUtil;
    @Autowired
    private IWorkSheetDao workSheetDao;
    @Autowired
    private IMaterialRequisitionDetailDao materialRequisitionDetailDao;
    @Autowired
    private IEmployeeService employeeService;
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
        return jobBookingFormDao.findByPage(hql,pageNo,pageSize,values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(JobBookingForm obj) {
        jobBookingFormDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public JobBookingForm queryByProperty(String name, String value) {
        return jobBookingFormDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     *
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(JobBookingForm obj) {
        return jobBookingFormDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public JobBookingForm queryObjById(Serializable id) {
        return jobBookingFormDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        jobBookingFormDao.deleteById(id);
    }
    /**
     * 保存报工单(新增)
     * @param form
     * @param details
     */
    @Override
    public void addJobBookingForm(JobBookingForm form, List<JobBookingFormDetail> details) {
        jobBookingFormDao.save(form);
        double amountOfJobBooking = 0;
        for(JobBookingFormDetail detail : details){
            copyJobBookingForm2JobBookingFormDetail(form,detail);
            detail.setAmountOfPerBox(detail.getAmountOfJobBooking());
            detail.setJobBookingForm(form);
            jobBookingFormDetailDao.save(detail);
            amountOfJobBooking+=detail.getAmountOfJobBooking();
            List<RawMaterial> list = detail.getRawMaterialList();
            if(!CollectionUtils.isEmpty(list)){
                for(RawMaterial rawMaterial:list){
                    rawMaterial.setJobBookingFormDetail(detail);
                    rawMaterial.setMaterialRequisitionDetailId(rawMaterial.getId());
                    rawMaterialDao.save(rawMaterial);



                    MaterialRequisitionDetail m=materialRequisitionDetailDao.findById(rawMaterial.getMaterialRequisitionDetailId());
                    m.setSurplusNum(m.getSurplusNum()-rawMaterial.getAmountOfUsed());
                    materialRequisitionDetailDao.save(m);
                }
            }
        }
        form.setAmountOfBoxes((double)details.size());
        form.setAmountOfJobBooking(amountOfJobBooking);
        //jobBookingFormDao.update(form);
        addBoxBar(form);
        //反填工单详情的报工数
        fillWorkSheetDetailReportCount(form);

    }

    public void fillWorkSheetDetailReportCount(JobBookingForm form){
        List<WorkSheetDetail> workSheetDetails = workSheetDetailDao.findByHQL("from WorkSheetDetail detail where detail.processCode=?0" +
                " and detail.deviceSiteCode=?1 and detail.workSheet.no=?2",new Object[]{form.getProcessesCode(),form.getDeviceSiteCode(),form.getWorkSheetNo()});
        if(!CollectionUtils.isEmpty(workSheetDetails)){
            for(WorkSheetDetail workSheetDetail : workSheetDetails){
                workSheetDetail.setReportCount(workSheetDetail.getReportCount ()+(int)((double)form.getAmountOfJobBooking()));

                workSheetDetailDao.update(workSheetDetail);
            }
        }
    }

    /**
     * 将报工单中的属性值拷贝到报工详情
     * @param form
     * @param detail
     */
    private void copyJobBookingForm2JobBookingFormDetail(JobBookingForm form,JobBookingFormDetail detail){
        detail.setProcessCode(form.getProcessesCode());
        detail.setProcessName(form.getProcessesName());
        detail.setInventoryCode(form.getInventoryCode());
        detail.setInventoryName(form.getInventoryName());
        detail.setSpecificationType(form.getUnitType());
        detail.setClassCode(form.getClassCode());
        detail.setClassName(form.getClassName());
        detail.setFurnaceNumber(form.getStoveNumber());
        detail.setBatchNumber(form.getBatchNumber());
        detail.setUnitName(form.getUnit());
        detail.setNo(form.getWorkSheetNo());
        detail.setDeviceSiteCode(form.getDeviceSiteCode());
        detail.setDeviceSiteName(form.getDeviceSiteName());
    }
    /**
     * 保存报工单(新增)原材料信息
     * @param form
     * @param details
     */
    @Override
    public void addJobBookingFormAndRawMaterial(JobBookingForm form, List<JobBookingFormDetail> details,String[] values,String[] codes,String[] ids) {
        jobBookingFormDao.save(form);
        double amountOfJobBooking = 0;
        for(JobBookingFormDetail detail : details){
            detail.setJobBookingForm(form);
            Serializable jobBookingFormDetailId = jobBookingFormDetailDao.save(detail);
            amountOfJobBooking+=detail.getAmountOfJobBooking();
            for(int i=0;i<codes.length;i++){
                detail.setId((String)jobBookingFormDetailId);
                if(codes[i]==null||"".equals(codes[i].trim())){
                   // throw new RuntimeException("原材料不能为空!");
                    continue;
                }
                BoxBar boxbar = boxBarService.queryBoxBarBybarCode(Long.parseLong(codes[i]));
                RawMaterial  rawm = new RawMaterial();
                rawm.setJobBookingFormDetail(detail);
                rawm.setBarCode(boxbar.getBarCode()+"");
                rawm.setInventoryCode(boxbar.getInventoryCode());
                rawm.setInventoryName(boxbar.getInventoryName());
                rawm.setSpecificationType(boxbar.getSpecificationType());
                rawm.setUnitCode(boxbar.getUnitCode());
                rawm.setUnitName(boxbar.getUnitName());
                rawm.setBatchNumber(boxbar.getBatchNumber());
                rawm.setFurnaceNumber(boxbar.getFurnaceNumber());
                rawm.setAmount(boxbar.getAmount());
                rawm.setAmountOfUsed(Double.parseDouble(values[i]));
                rawm.setMaterialRequisitionDetailId(ids[i]);

                MaterialRequisitionDetail m = materialRequisitionDetailDao.findById(ids[i]);
    			m.setSurplusNum(m.getSurplusNum()-Double.parseDouble(values[i]));
    			materialRequisitionDetailDao.update(m);
                rawMaterialDao.save(rawm);
            }
        }

        form.setAmountOfBoxes((double)details.size());
        form.setAmountOfJobBooking(amountOfJobBooking);
        addBoxBar(form);
        //反填工单详情的报工数
        fillWorkSheetDetailReportCount(form);
    }

    /**
     * 保存报工单(查看)
     * @param form       报工单对象
     * @param details    新增或更新的报工单详情列表
     * @param deletedIds 删除的报工单详情id
     */
    @Override
    public void addJobBookingForm(JobBookingForm form, List<JobBookingFormDetail> details, List<String> deletedIds,List<String> deletedRawMaterialIds) {
        //更新报工单对象
        JobBookingForm waf = jobBookingFormDao.findById(form.getFormNo());
        copyProperties4JobBookingForm(form,waf);
       // BeanUtils.copyProperties(form,waf);
        jobBookingFormDao.update(waf);
        //删除报工单详情
        if(!CollectionUtils.isEmpty(deletedIds)){
            for(String detailId : deletedIds){
                //根据报工单详情删除原材料


                List<RawMaterial> rmList=rawMaterialDao.findByHQL("from RawMaterial em where em.jobBookingFormDetail.id=?0",new Object[]{detailId});
                for(RawMaterial rm:rmList){
                    MaterialRequisitionDetail materialRequisitionDetail=materialRequisitionDetailDao.findById(rm.getMaterialRequisitionDetailId());
                    materialRequisitionDetail.setSurplusNum(materialRequisitionDetail.getSurplusNum()+rm.getAmountOfUsed());
                    materialRequisitionDetailDao.save(materialRequisitionDetail);
                }

                boxBarService.deleteByJobBookingFormDetailId(detailId);
                rawMaterialDao.deleteByJobBookingFormDetailId(detailId);
                jobBookingFormDetailDao.deleteById(detailId);
            }
        }
        //删除原材料信息
        if(!CollectionUtils.isEmpty(deletedRawMaterialIds)){
            for(String detailId : deletedRawMaterialIds){
                RawMaterial rm=rawMaterialDao.findById(detailId);

                MaterialRequisitionDetail materialRequisitionDetail=materialRequisitionDetailDao.findById(rm.getMaterialRequisitionDetailId());
                materialRequisitionDetail.setSurplusNum(materialRequisitionDetail.getSurplusNum()+rm.getAmountOfUsed());
                materialRequisitionDetailDao.save(materialRequisitionDetail);


                rawMaterialDao.deleteById(detailId);
            }
        }
        double amountOfJobBooking = 0;

        //更新报工单
        if(!CollectionUtils.isEmpty(details)){
            for (JobBookingFormDetail detail : details){
                detail.setAmountOfPerBox(detail.getAmountOfJobBooking());
                JobBookingFormDetail wafd = jobBookingFormDetailDao.findById(detail.getId());
                if(wafd==null){
                    wafd=new JobBookingFormDetail();
                    copyJobBookingForm2JobBookingFormDetail(form,wafd);
                    detail.setJobBookingForm(form);
                    jobBookingFormDetailDao.save(detail);
                    amountOfJobBooking+=detail.getAmountOfJobBooking();
                    List<RawMaterial> list = detail.getRawMaterialList();
                    if(!CollectionUtils.isEmpty(list)){
                        for(RawMaterial rawMaterial:list){
                            rawMaterial.setJobBookingFormDetail(detail);
                            rawMaterial.setMaterialRequisitionDetailId(rawMaterial.getId());
                            MaterialRequisitionDetail m=materialRequisitionDetailDao.findById(rawMaterial.getMaterialRequisitionDetailId());

                            m.setSurplusNum(m.getSurplusNum()-rawMaterial.getAmountOfUsed());
                            materialRequisitionDetailDao.save(m);
                            rawMaterialDao.save(rawMaterial);
                        }
                    }
                    waf.setAmountOfBoxes((double)details.size());
                    waf.setAmountOfJobBooking(amountOfJobBooking);
                }else {
                    BeanUtils.copyProperties(detail,wafd);
                    //修改箱号条码
                  BoxBar boxBar =  boxBarService.queryBoxBarBybarCode(Long.valueOf(wafd.getBarCode()));
                  if(boxBar!=null){
                      boxBar.setAmountOfPerBox(wafd.getAmountOfJobBooking());
                      boxBar.setAmount(wafd.getAmountOfJobBooking());
                      boxBarService.updateObj(boxBar);
                  }
                    jobBookingFormDetailDao.update(wafd);
                    amountOfJobBooking+=detail.getAmountOfJobBooking();
                    List<RawMaterial> list = detail.getRawMaterialList();
                    if(!CollectionUtils.isEmpty(list)){
                        for(RawMaterial rawMaterial:list){
                            RawMaterial material = rawMaterialDao.findById(rawMaterial.getId());
                            if(material!=null){
                                MaterialRequisitionDetail m=materialRequisitionDetailDao.findById(material.getMaterialRequisitionDetailId());
                                if(rawMaterial.getAmountOfUsed()>material.getAmountOfUsed()){
                                    m.setSurplusNum(m.getSurplusNum()-(rawMaterial.getAmountOfUsed()-material.getAmountOfUsed()));
                                }else{
                                    m.setSurplusNum(m.getSurplusNum()+(material.getAmountOfUsed()-rawMaterial.getAmountOfUsed()));
                                }
                                materialRequisitionDetailDao.save(m);
                                material.setAmountOfUsed(rawMaterial.getAmountOfUsed());
                                rawMaterialDao.update(material);
                            }else {
                                rawMaterial.setJobBookingFormDetail(detail);
                                rawMaterial.setMaterialRequisitionDetailId(rawMaterial.getId());
                                MaterialRequisitionDetail m=materialRequisitionDetailDao.findById(rawMaterial.getMaterialRequisitionDetailId());
                                m.setSurplusNum(m.getSurplusNum()-rawMaterial.getAmountOfUsed());
                                materialRequisitionDetailDao.save(m);
                                rawMaterialDao.save(rawMaterial);
                            }
                        }
                    }
                    waf.setAmountOfBoxes((double)details.size());
                    waf.setAmountOfJobBooking(amountOfJobBooking);
                }
            }
        }
        //jobBookingFormDao.update(form);
        addBoxBar(form);
        //反填工单详情的报工数
        fillWorkSheetDetailReportCount(waf);
    }
    /**
     * 根据报工日期查找最大报工单单号
     * @param date 报工日期
     * @return 最大报工单单号
     */
    @Override
    public String queryMaxFormNoByJobBookingDate(Date date) {
        return jobBookingFormDao.queryMaxFormNoByJobBookingDate(date);
    }
    /**
     * 查找最大报工单单号
     * @return 最大报工单单号
     */
    @Override
    public String queryMaxFormNo() {
    	return jobBookingFormDao.queryMaxFormNo();
    }
    /**
     * 根据报工单号删除报工单及详情信息
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        JobBookingForm form = jobBookingFormDao.findById(formNo);
        if(form!=null){
            form.setAmountOfJobBooking(-form.getAmountOfJobBooking());
            fillWorkSheetDetailReportCount(form);
        }
        List<JobBookingFormDetail> list = jobBookingFormDetailDao.findByHQL("from JobBookingFormDetail j where j.jobBookingForm.formNo=?0", new Object[]{formNo});
        for(JobBookingFormDetail l:list){
        	List<RawMaterial> rawList = rawMaterialDao.findByHQL("from RawMaterial r where r.jobBookingFormDetail.id=?0", new Object[]{l.getId()});
        	for(RawMaterial r:rawList){
        		MaterialRequisitionDetail m = materialRequisitionDetailDao.findById(r.getMaterialRequisitionDetailId());
    			m.setSurplusNum(m.getSurplusNum()+r.getAmountOfUsed());
    			materialRequisitionDetailDao.update(m);
        	}

        	materialRequisitionDetailDao.updateSurplusNum(l.getId());
        }
        jobBookingFormDao.deleteById(formNo);
        jobBookingFormDetailDao.deleteByFormNo(formNo);
        boxBarService.deleteByFormNo(formNo);
    }
    /**
     * 审核报工单
     * @param form 报工单对象
     */
    @Override
    public void audit(JobBookingForm form) {
        jobBookingFormDao.update(form);
        String hql = "from JobBookingFormDetail d where d.jobBookingForm.formNo=?0 ";
        List<JobBookingFormDetail> details = jobBookingFormDetailDao.findByHQL(hql,new Object[]{form.getFormNo()});
        if(!CollectionUtils.isEmpty(details)){
            for(JobBookingFormDetail detail : details){
                modifyEquipmentAndMeasuringToolByDeviceSiteCode(detail,"audit");
            }

        }

    }

    /**
     * 生成箱号条码
     * @param form
     */
    private void addBoxBar(JobBookingForm form){
        //产生箱号条码
        String hql = "from JobBookingFormDetail d where d.jobBookingForm.formNo=?0 and barCode is null";
        List<JobBookingFormDetail> details = jobBookingFormDetailDao.findByHQL(hql,new Object[]{form.getFormNo()});

        //查询最大箱号条码
        Integer maxBoxBarCode = boxBarService.queryMaxBoxBarCode();
        if(maxBoxBarCode==null || maxBoxBarCode ==0){
            maxBoxBarCode = (int)BoxBarController.START_BOXBAR_CODE-1;
        }
        if(!CollectionUtils.isEmpty(details)){
            for(JobBookingFormDetail detail : details){
                //修改装备和量具的计量累计和计量差异
                //modifyEquipmentAndMeasuringToolByDeviceSiteCode(detail,"audit");
                BoxBar boxBar = new BoxBar();
                boxBar.setEmployeeCode(detail.getJobBookingForm().getJobBookerCode());
                boxBar.setEmployeeName(detail.getJobBookingForm().getJobBookerName());
                boxBar.setAmount(form.getAmountOfJobBooking());
                boxBar.setAmountOfPerBox(detail.getAmountOfJobBooking());
                boxBar.setAmountOfBoxes(form.getAmountOfBoxes());
                boxBar.setBoxNum(detail.getBoxNum());
                boxBar.setFormNo(form.getFormNo());
                boxBar.setFurnaceNumber(detail.getFurnaceNumber());
                boxBar.setBatchNumber(detail.getBatchNumber());
                boxBar.setUnitName(detail.getUnitName());
                boxBar.setUnitCode(detail.getUnitCode());
                boxBar.setSpecificationType(detail.getSpecificationType());
                boxBar.setPurchasingNo(detail.getNo());
                boxBar.setClassName(detail.getClass().getName());
                boxBar.setTableName("JobBookingFormDetail");
                boxBar.setFkey(detail.getId());
                boxBar.setBoxBarType("报工单");
                boxBar.setInventoryCode(detail.getInventoryCode());
                boxBar.setInventoryName(detail.getInventoryName());
                boxBar.setSource(form.getProductionUnitName());
                boxBar.setBarCode(Long.valueOf(++maxBoxBarCode));
                boxBar.setSurplusNum(detail.getAmountOfPerBox());
                detail.setBarCode(maxBoxBarCode+"");
                boxBarService.addObj(boxBar);
            }
        }
    }
    /**
     * 修改装备或量具的计量累计和计量差异
     * @param detail
     * @param auditType audit:审核   unaudit:反审核
     */
    private void modifyEquipmentAndMeasuringToolByDeviceSiteCode(JobBookingFormDetail detail,String auditType){
        //根据设备站点编码查找所有类型为'计数型'的装备和量具
        List<EquipmentDeviceSiteMapping> mappingList =  equipmentDeviceSiteMappingDao.findByHQL("select map from EquipmentDeviceSiteMapping map " +
                " inner join fetch map.equipment e where e.equipmentType.measurementType=?0 and map.deviceSite.code=?1",new Object[]{"计数型",detail.getDeviceSiteCode()});
        //报工数
        Double jobBookingCount = detail.getAmountOfJobBooking();
        if(jobBookingCount==null){
            jobBookingCount = 0d;
        }
        if(!CollectionUtils.isEmpty(mappingList)){
            for(EquipmentDeviceSiteMapping mapping : mappingList){
                Equipment equipment = mapping.getEquipment();
                //使用频次
                float frequency = mapping.getUsageRate();
                if(frequency>0) {
                    //计算使用次数
                    int usedCount = (int) Math.ceil(jobBookingCount / frequency);
                    if(auditType.equals("audit")) {
                        equipment.setCumulation((equipment.getCumulation() == null ? 0 : equipment.getCumulation()) + usedCount);
                    }
                    if(auditType.equals("unaudit")){
                        equipment.setCumulation((equipment.getCumulation() == null ? 0 : equipment.getCumulation()) - usedCount);
                    }
                    if (equipment.getEquipmentType().getMeasurementObjective() != null && equipment.getEquipmentType().getMeasurementObjective() > 0) {
                        equipment.setMeasurementDifference(equipment.getEquipmentType().getMeasurementObjective() - equipment.getCumulation());
                    }
                    equipmentDao.update(equipment);
                }
            }
        }
    }
    /**
     * 反审核
     * @param form 报工单对象
     */
    @Override
    public void unaudit(JobBookingForm form) {
        jobBookingFormDao.update(form);
        String hql = "from JobBookingFormDetail d where d.jobBookingForm.formNo=?0 ";
        List<JobBookingFormDetail> details = jobBookingFormDetailDao.findByHQL(hql,new Object[]{form.getFormNo()});
        if(!CollectionUtils.isEmpty(details)){
            for(JobBookingFormDetail detail : details){
                modifyEquipmentAndMeasuringToolByDeviceSiteCode(detail,"unaudit");
            }

        }

    }
    /**
     *入库
     */
    @Override
    public void intoWarehouse(User user,String no, String warehouseCode, String Amounts, String BarCodes,String workSheetNo) {
        Warehouse warehouse=warehouseService.queryByProperty("cWhCode",warehouseCode);
        if(BarCodes!=null && BarCodes.contains("'")) {
            BarCodes = BarCodes.replace("'", "");
        }
        String[] barcodes= BarCodes.split(",");
        if(Amounts!=null && Amounts.contains("'")) {
            Amounts = Amounts.replace("'", "");
        }
        String[] ramounts= Amounts.split(",");

        //总报工数
        Double amount = 0d;
        String detailBatchNumber = "";
        for(int i=0;i<barcodes.length;i++){
            BoxBar b=boxBarService.queryBoxBarBybarCode(Long.valueOf(barcodes[i]));
            List<BoxBar> blist=boxBarService.queryBoxBarByFormNo("JobBookingFormDetail",b.getFormNo());
            b.setEnterWarehouse(true);
            JobBookingFormDetail jbfd= jobBookingFormDetailService.queryObjById(b.getFkey());
            if(i==0){
                detailBatchNumber = jbfd.getBatchNumber();
            }
            jbfd.setWarehouseCode(warehouse.getcWhCode());
            jbfd.setWarehouseName(warehouse.getcWhName());

            if(b.getAmountOfPerBox()>Double.valueOf(ramounts[i])){
                jbfd.setAmountOfJobBooking(jbfd.getAmountOfJobBooking()-(b.getAmountOfPerBox()-Double.valueOf(ramounts[i])));
            }else if(b.getAmountOfPerBox()<Double.valueOf(ramounts[i])){
                jbfd.setAmountOfJobBooking(jbfd.getAmountOfJobBooking()+(Double.valueOf(ramounts[i])-b.getAmountOfPerBox()));
            }
            for(BoxBar box:blist){
                box.setAmount(jbfd.getAmountOfJobBooking());
                boxBarService.updateObj(box);
            }
            b.setAmount(jbfd.getAmountOfJobBooking());
            b.setAmountOfPerBox(Double.valueOf(ramounts[i]));
            b.setSurplusNum(Double.valueOf(ramounts[i]));
            b.setWarehouseDate(new Date());
            b.setWarehouseNo(no);
            b.setEmployeeName(user.getEmployee().getName());
            b.setEmployeeCode(user.getEmployee().getCode());
            b.setWarehouseCode(warehouse.getcWhCode());
            b.setWarehouseName(warehouse.getcWhName());

            if(jbfd.getAmountOfInWarehouse()!=null){
                jbfd.setAmountOfInWarehouse(jbfd.getAmountOfInWarehouse()+Double.valueOf(ramounts[i]));
            }else{
                jbfd.setAmountOfInWarehouse(Double.valueOf(ramounts[i]));
            }
            boxBarService.updateObj(b);
            jobBookingFormDetailService.updateObj(jbfd);

            amount+=ramounts[i]==null?0:Double.valueOf(ramounts[i]);
        }
        //生成ERP入库单
        WorkSheet workSheet = workSheetDao.findSingleByProperty("no",workSheetNo);
        if(workSheet!=null &&workSheet.getFromErp()!=null&& workSheet.getFromErp()) {
            Map<String, Object> map = new HashMap<>();
            map.put("warehouseCode", warehouseCode);
            map.put("warehouseDate", new Date());
            map.put("warehouseNo", no);
            map.put("detailBatchNumber",detailBatchNumber);
            Employee employee = user.getEmployee();
            if (employee != null) {
                map.put("makerName", employee.getName());
                map.put("makerCode", employee.getCode());
            }
            map.put("amount", amount);
          Result result =  jobBookingSlipUtil.generateJobBookingSlip(workSheet, map);
          if(result.getStatusCode()!=200){
              throw  new RuntimeException(result.getMessage());
          }
        }
    }

    @Override
    public Double queryNumberByJobBookingForm(String no) {
        return jobBookingFormDao.queryNumberByJobBookingForm(no);
    }

    @Override
    public Double queryNumberByJobBookingFormAndClassesCode(String no, String classesCode) {
        return jobBookingFormDao.queryNumberByJobBookingFormAndClassesCode(no,classesCode);
    }

    /**
     * 拆箱
     * @param unpackingDetail
     * @return
     */
    private List<JobBookingFormDetail> unpacking(JobBookingFormDetail unpackingDetail){
        List<JobBookingFormDetail> list = new ArrayList<>();
        //计算要拆的箱数
        int boxesCount =unpackingDetail.getAmountOfBoxes().intValue(); //(int)Math.floor(unpackingDetail.getAmountOfJobBooking()/unpackingDetail.getAmountOfPerBox());
        double lastBoxCount = unpackingDetail.getAmountOfJobBooking() % unpackingDetail.getAmountOfPerBox();
        if(boxesCount>1){
            if(lastBoxCount>0) {
                for(int i = 0;i<boxesCount-1;i++){
                    JobBookingFormDetail d = new JobBookingFormDetail();
                    BeanUtils.copyProperties(unpackingDetail,d);
                    d.setAmountOfJobBooking(unpackingDetail.getAmountOfPerBox());
                    d.setAmountOfBoxes(Double.valueOf(boxesCount));
                    d.setAmountOfPerBox(unpackingDetail.getAmountOfPerBox());
                    list.add(d);
                }
                JobBookingFormDetail d = new JobBookingFormDetail();
                BeanUtils.copyProperties(unpackingDetail, d);
                d.setAmountOfJobBooking(lastBoxCount);
                d.setAmountOfBoxes(Double.valueOf(boxesCount));
                d.setAmountOfPerBox(lastBoxCount);
                list.add(d);
            }else{
                for(int i = 0;i<boxesCount;i++){
                    JobBookingFormDetail d = new JobBookingFormDetail();
                    BeanUtils.copyProperties(unpackingDetail,d);
                    d.setAmountOfJobBooking(unpackingDetail.getAmountOfPerBox());
                    d.setAmountOfBoxes(Double.valueOf(boxesCount));
                    d.setAmountOfPerBox(unpackingDetail.getAmountOfPerBox());
                    list.add(d);
                }
            }
        }else{
            list.add(unpackingDetail);
        }
        return list;
    }
    /**
     * 报工单详情属性拷贝
     * @param src
     * @param dest
     */
    private void copyProperties4JobBookingFormDetail(JobBookingFormDetail src,JobBookingFormDetail dest){
        dest.setAmountOfJobBooking(src.getAmountOfJobBooking());
        dest.setBatchNumber(src.getBatchNumber());
        dest.setFurnaceNumber(src.getFurnaceNumber());
        dest.setAmountOfPerBox(src.getAmountOfPerBox());
        dest.setAmountOfBoxes(src.getAmountOfBoxes());
        dest.setNo(src.getNo());
        dest.setInventoryCode(src.getInventoryCode());
        dest.setInventoryName(src.getInventoryName());
    }
    /**
     * 报工单属性拷贝
     * @param src
     * @param dest
     */
    private void copyProperties4JobBookingForm(JobBookingForm src,JobBookingForm dest){
        dest.setJobBookingDate(src.getJobBookingDate());
        dest.setProductionUnitCode(src.getProductionUnitCode());
        dest.setProductionUnitName(src.getProductionUnitName());
        dest.setClassCode(src.getClassCode());
        dest.setClassName(src.getClassName());
        dest.setProcessesCode(src.getProcessesCode());
        dest.setProcessesName(src.getProcessesName());
        dest.setDeviceSiteCode(src.getDeviceSiteCode());
        dest.setDeviceSiteName(src.getDeviceSiteName());
        dest.setUnitType(src.getUnitType());
        dest.setBatchNumber(src.getBatchNumber());
        dest.setStoveNumber(src.getStoveNumber());
        dest.setInventoryCode(src.getInventoryCode());
        dest.setInventoryName(src.getInventoryName());
        dest.setWorkSheetNo(src.getWorkSheetNo());
    }


    @Override
    public void updateJobBookingNumberAndBoxbarNumber(String barcode, String boxbarNumber, String materialIds, String materialNumbers) {
        BoxBar boxBar=boxBarService.queryBoxBarBybarCode(Long.valueOf(barcode));
        JobBookingFormDetail jobBookingFormDetail=jobBookingFormDetailService.queryObjById(boxBar.getFkey());
        jobBookingFormDetail.setAmountOfPerBox(Double.valueOf(boxbarNumber));
        jobBookingFormDetail.setAmountOfJobBooking(Double.valueOf(boxbarNumber));
        if(jobBookingFormDetail.getAmountOfInWarehouse()!=null&&jobBookingFormDetail.getAmountOfInWarehouse()!=0d){
                jobBookingFormDetail.setAmountOfInWarehouse(Double.valueOf(boxbarNumber));
        }
        jobBookingFormDetailService.updateObj(jobBookingFormDetail);
        JobBookingForm form=jobBookingFormDetail.getJobBookingForm();
        if(boxBar.getAmount()>Double.valueOf(boxbarNumber)){
            form.setAmountOfJobBooking(form.getAmountOfJobBooking()-(boxBar.getAmount()-Double.valueOf(boxbarNumber)));
        }else{
            form.setAmountOfJobBooking(form.getAmountOfJobBooking()+(Double.valueOf(boxbarNumber)-boxBar.getAmount()));
        }
        updateObj(form);
        List<WorkSheetDetail> workSheetDetails = workSheetDetailDao.findByHQL("from WorkSheetDetail detail where detail.processCode=?0" +
                " and detail.deviceSiteCode=?1 and detail.workSheet.no=?2",new Object[]{form.getProcessesCode(),form.getDeviceSiteCode(),form.getWorkSheetNo()});
        if(!CollectionUtils.isEmpty(workSheetDetails)){
            WorkSheetDetail workSheetDetail=workSheetDetails.get(0);
            if(boxBar.getAmount()>Double.valueOf(boxbarNumber)){
                workSheetDetail.setReportCount(workSheetDetail.getReportCount ()-(int)(boxBar.getAmount()-Double.valueOf(boxbarNumber)));
            }else{
                workSheetDetail.setReportCount(workSheetDetail.getReportCount ()+(int)(Double.valueOf(boxbarNumber)-boxBar.getAmount()));
            }
            workSheetDetailDao.update(workSheetDetail);

        }
        boxBar.setSurplusNum(Double.valueOf(boxbarNumber));
        boxBar.setAmount(Double.valueOf(boxbarNumber));
        boxBar.setAmountOfPerBox(Double.valueOf(boxbarNumber));
        boxBarService.updateObj(boxBar);

        if(materialIds!=null && materialIds.contains("'")) {
            materialIds = materialIds.replace("'", "");
        }
        String[] materialIdList= materialIds.split(",");
        if(materialNumbers!=null && materialNumbers.contains("'")) {
            materialNumbers = materialNumbers.replace("'", "");
        }
        String[] materialNumberList= materialNumbers.split(",");
        for(int i=0;i<materialIdList.length;i++){
            RawMaterial material=rawMaterialDao.findById(materialIdList[i]);
            if(material.getAmountOfUsed()>Double.valueOf(materialNumberList[i])){
                MaterialRequisitionDetail m=materialRequisitionDetailDao.findById(material.getMaterialRequisitionDetailId());
                m.setSurplusNum(m.getSurplusNum()+(material.getAmountOfUsed()-Double.valueOf(materialNumberList[i])));
                materialRequisitionDetailDao.save(m);
                material.setAmountOfUsed(Double.valueOf(materialNumberList[i]));
            }else{
                MaterialRequisitionDetail m=materialRequisitionDetailDao.findById(material.getMaterialRequisitionDetailId());
                m.setSurplusNum(m.getSurplusNum()-(Double.valueOf(materialNumberList[i])-material.getAmountOfUsed()));
                materialRequisitionDetailDao.save(m);
                material.setAmountOfUsed(Double.valueOf(materialNumberList[i]));
            }
        }
    }
}
