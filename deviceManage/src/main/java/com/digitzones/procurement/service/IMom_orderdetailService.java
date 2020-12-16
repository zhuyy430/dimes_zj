package com.digitzones.procurement.service;
import com.digitzones.procurement.model.Mom_recorddetail;
import com.digitzones.service.ICommonService;

import java.util.List;

/**
 * ERP生产订单
 * @author zhuyy430
 *
 */
public interface IMom_orderdetailService extends ICommonService<Mom_recorddetail> {
    /**
     * 根据生产订单号、行号查询
     * @param code
     * @param seq
     * @return
     */
    Mom_recorddetail queryMom_recorddetailByCodeAndSeq(String code,String seq);
}
