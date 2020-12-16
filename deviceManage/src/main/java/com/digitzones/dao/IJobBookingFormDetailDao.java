package com.digitzones.dao;
import com.digitzones.model.Classes;
import com.digitzones.model.JobBookingFormDetail;

import java.util.Date;
/**
 *报工单详情
 */
public interface IJobBookingFormDetailDao extends ICommonDao<JobBookingFormDetail> {
    /**
     * 根据报工单编码删除报工单信息
     * @param formNo 报工单编码
     */
   public void deleteByFormNo(String formNo);
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
    public Object[] queryCountAndSumOfStandardBeat4CurrentClass(Classes currentClass, String deviceSiteCode, Date date);
    /**
     * 根据工单单号和工序代码查找最大箱号
     * @param no
     * @return
     */
   public int queryMaxBoxNumByNoAndProcessCode(String no,String processCode);
}
