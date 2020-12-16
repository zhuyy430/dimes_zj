package com.digitzones.procurement.service;
import java.util.List;

import com.digitzones.procurement.model.PO_Podetails;
import com.digitzones.service.ICommonService;
/**
 * 采购订单service
 * @author zdq
 * 2018年6月11日
 */
public interface IPO_PodetailsService extends ICommonService<PO_Podetails> {
	
	public List<PO_Podetails> queryPO_PodetailsByPOID(String POID);
	/**
	 * 查找ids表示的订单详情
	 * @param ids 多个订单详情id列表
	 * @return
	 */
	public List<PO_Podetails> queryByIds(List<Integer> ids);
	
}
