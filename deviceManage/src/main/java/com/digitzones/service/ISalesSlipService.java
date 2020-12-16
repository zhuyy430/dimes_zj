package com.digitzones.service;
import com.digitzones.model.SalesSlip;
import java.util.List;
/**
 * 销售订单视图service
 */
public interface ISalesSlipService extends ICommonService<SalesSlip> {
    /**
     * 根据销售订单号查找销售订单
     * @param idsList
     * @return
     */
    public List<SalesSlip> queryByIds(List<Integer> idsList);
}
