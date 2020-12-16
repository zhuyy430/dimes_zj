package com.digitzones.procurement.dao;
import com.digitzones.dao.ICommonDao;
import com.digitzones.procurement.model.TemporaryBarcodeDetail;
/**
 * 临时条码详情
 */
public interface ITemporaryBarcodeDetailDao extends ICommonDao<TemporaryBarcodeDetail> {
   public void deleteByFormNo(String formNo);
}
