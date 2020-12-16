package com.digitzones.dao;
import com.digitzones.model.RawMaterial;
/**
 * 每道工序，上一道工序的半成品或原始材料(从仓库中领到的料)
 */
public interface IRawMaterialDao extends ICommonDao<RawMaterial> {
    /**
     * 根据报工单详情id删除原材料信息
     * @param detailId
     */
    void deleteByJobBookingFormDetailId(String detailId);
}
