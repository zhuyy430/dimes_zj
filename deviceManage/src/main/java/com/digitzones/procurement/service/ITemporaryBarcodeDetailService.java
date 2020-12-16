package com.digitzones.procurement.service;
import com.digitzones.procurement.model.TemporaryBarcodeDetail;
import com.digitzones.service.ICommonService;

import java.util.List;

/**
 * 临时条码详情业务逻辑接口
 */

public interface ITemporaryBarcodeDetailService extends ICommonService<TemporaryBarcodeDetail> {
    /**
     * 根据临时条码编号查找临时条码详情信息
     * @param requestNo 临时条码编号
     * @return
     */
    List<TemporaryBarcodeDetail> queryByTemporaryBarcodeNo(String requestNo);
}
