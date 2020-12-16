package com.digitzones.dao;
import com.digitzones.model.JobBookingForm;
import com.digitzones.procurement.model.WarehousingApplicationForm;

import java.util.Date;

/**
 * 报工单dao
 */
public interface IJobBookingFormDao extends ICommonDao<JobBookingForm> {
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
     * 查询工单累计报工数量
     * @param no
     */
    public  Double queryNumberByJobBookingForm(String no);
    /**
     * 查询工单当班累计报工数量
     * @param no
     */
    public  Double queryNumberByJobBookingFormAndClassesCode(String no,String classesCode);
}
