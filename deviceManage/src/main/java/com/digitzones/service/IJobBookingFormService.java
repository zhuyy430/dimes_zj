package com.digitzones.service;
import com.digitzones.model.JobBookingForm;
import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.model.User;

import java.util.Date;
import java.util.List;
/**
 * 报工单service
 */
public interface IJobBookingFormService extends ICommonService<JobBookingForm> {
    /**
     * 根据报工日期查找最大报工单单号
     * @param date 报工日期
     * @return 最大报工单单号
     */
    public String queryMaxFormNoByJobBookingDate(Date date);

    /**
     * 查找最大报工单单号
     * @return 最大报工单单号
     */
    public String queryMaxFormNo();

    /**
     * 保存报工单(新增)
     * @param form
     * @param details
     */
    public void addJobBookingForm(JobBookingForm form, List<JobBookingFormDetail> details);
    /**
     * 保存报工单and原材料信息(新增)
     * @param form
     * @param details
     */
    public void addJobBookingFormAndRawMaterial(JobBookingForm form, List<JobBookingFormDetail> details, String[] values, String[] codes, String[] ids);
    /**
     * 保存报工单(查看)
     * @param form 报工单对象
     * @param details 新增或更新的报工单详情列表
     * @param deletedIds 删除的报工单详情id
     */
    public void addJobBookingForm(JobBookingForm form, List<JobBookingFormDetail> details, List<String> deletedIds, List<String> deletedRawMaterialIds);
    /**
     * 根据报工单号删除报工单及详情信息
     * @param formNo
     */
    public void deleteByFormNo(String formNo);
    /**
     * 审核报工单
     * @param form 报工单对象
     */
    public void audit(JobBookingForm form);
    /**
     * 反审核
     * @param form 报工单对象
     */
   public  void unaudit(JobBookingForm form);

    /**
     * 报工入库
     * @param user
     * @param no 入库单号
     * @param warehouseCode
     * @param Amounts
     * @param BarCodes
     * @param workSheetNo 工单单号
     */
    public void intoWarehouse(User user, String no, String warehouseCode, String Amounts, String BarCodes, String workSheetNo);

    /**
     * 查询工单累计报工数量
     * @param no
     */
    public  Double queryNumberByJobBookingForm(String no);
    /**
     * 查询工单当班累计报工数量
     * @param no
     */
    public  Double queryNumberByJobBookingFormAndClassesCode(String no,String classesCode);


    void updateJobBookingNumberAndBoxbarNumber(String barcode,String boxbarNumber,String materialIds,String materialNumbers);

}
