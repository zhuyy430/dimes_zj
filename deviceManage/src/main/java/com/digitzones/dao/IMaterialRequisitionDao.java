package com.digitzones.dao;
import com.digitzones.model.MaterialRequisition;

import java.util.Date;

/**
 * 领用单dao
 */
public interface IMaterialRequisitionDao extends ICommonDao<MaterialRequisition> {
    /**
     * 根据领用日期查找最大领用单号
     * @param now
     * @return
     */
  public  String queryMaxFormNoByPickingDate(Date now);
}
