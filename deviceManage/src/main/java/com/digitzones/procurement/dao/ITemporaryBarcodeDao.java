package com.digitzones.procurement.dao;

import com.digitzones.dao.ICommonDao;
import com.digitzones.procurement.model.TemporaryBarcode;

import java.util.Date;
/**
 * 临时条码dao
 */
public interface ITemporaryBarcodeDao extends ICommonDao<TemporaryBarcode> {
    /**
     * 根据制单日期查找最大单号
     * @param date 制单日期
     * @return
     */
    public String queryMaxRequestNoByBillDate(Date date);
}
