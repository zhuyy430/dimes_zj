package com.digitzones.mc.service;

import com.digitzones.mc.model.MCDeviceSite;
import com.digitzones.mc.model.MCWarehouse;

import java.util.List;

/**
 * mc端设备站点service
 * @author Administrator
 *
 */
public interface IMCWarehouseService {
	/**
	 * 查询仓库信息(用于mc端本地站点设置)
	 * @return
	 */
	public MCWarehouse queryMCWarehouse(String clientIp);
	/**
	 * 新增
	 * @return
	 */
	public void addMCWarehouse(MCWarehouse mcWarehouse);
	/**
	 * 修改
	 * @return
	 */
	public void updateMCWarehouse(MCWarehouse mcWarehouse);
}
