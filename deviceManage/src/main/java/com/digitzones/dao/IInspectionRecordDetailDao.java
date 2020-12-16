package com.digitzones.dao;
import com.digitzones.model.InspectionRecordDetail;
import com.digitzones.model.MaterialRequisitionDetail;
/**
 * 检验单详情
 */
public interface IInspectionRecordDetailDao extends ICommonDao<InspectionRecordDetail> {
    /**
     * 根据检验单号删除检验单详情
     * @param formNo
     */
   public void deleteByFormNo(String formNo);
}
