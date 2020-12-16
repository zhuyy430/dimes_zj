package com.digitzones.service;

import com.digitzones.model.MaterialRequisitionDetail;

import java.util.List;

public interface IMaterialRequisitionDetailService extends ICommonService<MaterialRequisitionDetail> {
    /**
     * 根据领料单号查找领料单详情
     * @param formNo 领料单号
     * @return
     */
    public List<MaterialRequisitionDetail> queryByFormNo(String formNo);
    /**
     * 根据单号查找领料单详情
     * @param formNo 工单单号
     * @return
     */
    public List<MaterialRequisitionDetail> queryByWorkSheetNo(String workSheetNo);
    /**
     * 根据工单号和箱条码查找领料单详情
     * @param workSheetNo 工单单号
     * @param barCode 箱条码
     * @return
     */
    public List<MaterialRequisitionDetail> queryByWorkSheetNoAndBarCode(String workSheetNo,String barCode);
    /**
     * 根据箱条码领料单详情
     * @param barCode 箱条码
     * @return
     */
    public List<MaterialRequisitionDetail> queryByBarCode(String barCode);
}
