package com.digitzones.procurement.service;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import com.digitzones.service.ICommonService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 入库申请单详情业务逻辑接口
 */

public interface IWarehousingApplicationFormDetailService extends ICommonService<WarehousingApplicationFormDetail> {
    /**
     * 根据入库申请单编号查找入库申请单详情信息
     * @param requestNo 入库申请单编号
     * @return
     */
    List<WarehousingApplicationFormDetail> queryByWarehouseingApplicationFormNo(String requestNo);
    /**
     * 根据入库申请单号查找入库数大于0的记录数
     * @param formNo
     * @return
     */
    public Long queryCountOfAmountOfInWarehouseBigThenZero(String formNo);
}
