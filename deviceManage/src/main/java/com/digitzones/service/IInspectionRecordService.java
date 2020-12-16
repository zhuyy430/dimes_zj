package com.digitzones.service;

import com.digitzones.model.InspectionRecord;
import com.digitzones.model.InspectionRecordDetail;

import java.util.Date;
import java.util.List;

/**
 * 检验单service
 */
public interface IInspectionRecordService extends ICommonService<InspectionRecord>{
    /**
     * 根据检验日期查找最大检验单号
     * @param now 检验日期
     * @return
     */
  public  String queryMaxFormNoByInspectionDate(Date now);
    /**
     * 新增检验工单
     * @param form 检验工单对象
     * @param details 检验工单详情
     */
   public void addInspectionRecord(InspectionRecord form, List<InspectionRecordDetail> details);
    /**
     * 新增检验工单
     * @param form 检验工单对象
     * @param details 检验工单详情
     */
    public void updateInspectionRecord(InspectionRecord form, List<InspectionRecordDetail> details);
    /**
     * 根据领料单号删除领料单及详情
     * @param formNo
     */
   public void deleteByFormNo(String formNo);
   /**
    * 条件查询检验记录
    */
   public List<InspectionRecord> queryInspectionRecordsByDeviceSite(String hql, List<Object> param);
}
