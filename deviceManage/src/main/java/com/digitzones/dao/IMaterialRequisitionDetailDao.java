package com.digitzones.dao;

import com.digitzones.model.MaterialRequisitionDetail;
/**
 * 领用单详情
 */
public interface IMaterialRequisitionDetailDao extends ICommonDao<MaterialRequisitionDetail> {
    /**
     * 根据领料单号删除领料单详情
     * @param formNo
     */
   public void deleteByFormNo(String formNo);

    /**
     * 更新领料数量到初始状态
     * @param id   报工详情id
     */
    void updateSurplusNum(String id);
}
