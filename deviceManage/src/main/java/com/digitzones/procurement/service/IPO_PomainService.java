package com.digitzones.procurement.service;
import com.digitzones.procurement.model.PO_Pomain;
import com.digitzones.service.ICommonService;

import java.util.List;

/**
 * 采购订单service
 * @author zdq
 * 2018年6月11日
 */
public interface IPO_PomainService extends ICommonService<PO_Pomain> {
    /**
     * 根据入库申请单单号查找采购订单
     * @param formNo
     * @return
     */
    List<PO_Pomain> queryByWarehousingApplicationFormNoAndBarCode(String formNo,String barCode);
}
