package com.digitzones.dao;
import com.digitzones.model.InspectionRecord;
import com.digitzones.model.MaterialRequisition;

import java.util.Date;

/**
 * 检验单dao
 */
public interface IInspectionRecordDao extends ICommonDao<InspectionRecord> {
    /**
     * 根据检验日期查找最大检验单号
     * @param now
     * @return
     */
  public  String queryMaxFormNoByInspectionDate(Date now);
}
