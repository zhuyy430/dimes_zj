package com.digitzones.procurement.dao;
import com.digitzones.dao.ICommonDao;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
/**
 * 入库申请单详情
 */
public interface IWarehousingApplicationFormDetailDao extends ICommonDao<WarehousingApplicationFormDetail> {
   public void deleteByFormNo(String formNo);
}
