package com.digitzones.service;

import com.digitzones.model.PackageCode;
import com.digitzones.model.User;
import com.digitzones.procurement.model.BoxBar;

import java.util.List;

/**
 * 设备service
 * @author zdq
 * 2018年6月11日
 */
public interface IPackageCodeService extends ICommonService<PackageCode> {
	/**
	 * 查询最新的数据
	 * @return
	 */
	public PackageCode queryMaxPackageCode();
	/**
	 * 查询最新的数据
	 * @return
	 */
	public List<PackageCode> queryPackageCodeByIds(List<Long> ids);
	/**
	 * 根据包装条码查找BoxBar
	 * @param packageCode
	 * @return
	 */
	public List<BoxBar> queryBoxBarByPackageCode(String packageCode);


	/**
	 * 包装
	 * @param packNo  包装箱号
	 * @param barCodes
	 * @param ramounts
	 * @param user
	 */
	public void outWarehouse(String packNo,String[] barCodes, String[] ramounts, User user);
}
