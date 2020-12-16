package com.digitzones.service;

import com.digitzones.model.InspectionRecordDetail;

import java.util.List;

public interface IInspectionRecordDetailService extends ICommonService<InspectionRecordDetail> {
    /**
     * 根据检验单号查找检验单详情
     * @param formNo 检验单号
     * @return
     */
    public List<InspectionRecordDetail> queryByFormNo(String formNo);
}
