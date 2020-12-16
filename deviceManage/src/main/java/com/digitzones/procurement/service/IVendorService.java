package com.digitzones.procurement.service;
import java.util.List;

import com.digitzones.procurement.model.Vendor;
import com.digitzones.service.ICommonService;
/**
 * 供应商service
 * @author zdq
 * 2018年6月11日
 */
public interface IVendorService extends ICommonService<Vendor> {
	/**
	 * 获取所有供应商信息
	 * @return
	 */
	public List<Vendor> queryAllVendor();
}
