package com.digitzones.mc.dao.impl;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.mc.dao.IMCDeviceSiteDao;
import com.digitzones.mc.dao.IMCWarehouseDao;
import com.digitzones.mc.model.MCDeviceSite;
import com.digitzones.mc.model.MCWarehouse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MCWarehouseDaoImpl extends CommonDaoImpl<MCWarehouse> implements IMCWarehouseDao {
	public MCWarehouseDaoImpl() {
		super(MCWarehouse.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<MCWarehouse> findMCWarehouseByIP(String IP) {
		String sql = "select w.id,w.clientIp,w.cWhCode,w.cWhName "
				+ "from MC_Warehouse w  where w.clientIp=?0";
		return getSession().createSQLQuery(sql).setParameter(0,IP).addEntity(MCDeviceSite.class).list();
	}
}
