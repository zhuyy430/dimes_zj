package com.digitzones.service;

import com.digitzones.model.Classes;
import com.digitzones.model.JobBookingFormDetail;

import java.util.Date;
import java.util.List;

/**
 * 报工单详情业务逻辑接口
 */

public interface IJobBookingFormDetailService extends ICommonService<JobBookingFormDetail> {
    /**
     * 根据报工单编号查找报工单详情信息
     * @param formNo 报工单编号
     * @return
     */
    List<JobBookingFormDetail> queryByJobBookingFormNo(String formNo);
    /**
     * 根据报工单号查找入库数大于0的记录数
     * @param formNo
     * @return
     */
    public Long queryCountOfAmountOfInWarehouseBigThenZero(String formNo);
    /**
     * 根据班次，日期，生产单元id查找产量
     * @param c
     * @param d
     * @param productionUnitId
     * @return
     */
    public double queryBottleneckCountByClassesIdAndDay(Classes c, Date d, Long productionUnitId);
    /**
     * 查询生产数量和总标准节拍
     * @param currentClass
     * @param deviceSiteCode
     * @param date
     * @return
     */
    Object[] queryCountAndSumOfStandardBeat4CurrentClass(Classes currentClass,String deviceSiteCode, Date date);
    /**
     * 根据工单单号查找最大箱号
     * @param no
     * @return
     */
     public int queryMaxBoxNumByNoAndProcessCode(String no,String processCode);

    /**
     * 根据工单号查询报工单
     * @param no
     * @return
     */
    List<JobBookingFormDetail> queryByWorksheetNo(String no);
}
