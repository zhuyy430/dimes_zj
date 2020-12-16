package com.digitzones.service;
import com.digitzones.model.RawMaterial;

import java.util.List;

/**
 * 原材料业务逻辑接口
 */
public interface IRawMaterialService extends ICommonService<RawMaterial> {
    /**
     * 根据报工详情id查找原材料信息
     * @param jobBookingDetailId
     * @return
     */
    List<RawMaterial> queryByJobBookingDetailId(String jobBookingDetailId);
}
