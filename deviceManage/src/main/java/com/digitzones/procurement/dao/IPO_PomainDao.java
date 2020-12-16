package com.digitzones.procurement.dao;

import com.digitzones.dao.ICommonDao;
import com.digitzones.procurement.model.PO_Pomain;

import java.util.List;

/**
 * 采购订单dao
 * @author zdq
 * 2018年6月11日
 */
public interface IPO_PomainDao extends ICommonDao<PO_Pomain> {
    /**
     * 根据入库申请单单号查找采购订单
     * @param formNo
     * @return
     */
    List<PO_Pomain> queryByWarehousingApplicationFormNoAndBarCode(String formNo,String barCode);
}
