package com.digitzones.mc.dao;

import com.digitzones.dao.ICommonDao;
import com.digitzones.mc.model.MCWarehouse;

import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
public interface IMCWarehouseDao extends ICommonDao<MCWarehouse>{

	/**
	 * 根据IP查找对应的仓库信息
	 * @param IP
	 * @return
	 */
	List<MCWarehouse> findMCWarehouseByIP(String IP);

}
