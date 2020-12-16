package com.digitzones.procurement.dao;
import com.digitzones.dao.ICommonDao;
import com.digitzones.procurement.model.WarehousingApplicationForm;

import java.util.Date;

/**
 * 入库申请单dao
 */
public interface IWarehousingApplicationFormDao extends ICommonDao<WarehousingApplicationForm> {
    /**
     * 根据收料日期查找最大入库申请单单号
     * @param date 收料日期
     * @return 最大入库申请单单号
     */
    public String queryMaxRequestNoByReceivingDate(Date date);

    /**
     * 根据箱号条码查找入库申请单
     * @param barCode
     * @return
     */
    public WarehousingApplicationForm queryByBarCode(String barCode);
}
