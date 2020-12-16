package com.digitzones.mc.service.impl;

import com.digitzones.mc.dao.IMCWarehouseDao;
import com.digitzones.mc.model.MCWarehouse;
import com.digitzones.mc.service.IMCWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class MCWarehouseServiceImpl implements IMCWarehouseService {
	@Autowired
	private IMCWarehouseDao mcWarehouseDao;
	@Override
	public MCWarehouse queryMCWarehouse(String clientIp) {
		List<MCWarehouse> list = mcWarehouseDao.findByHQL("from MCWarehouse where clientIp=?0", new Object[]{clientIp});
		if(CollectionUtils.isEmpty(list)){
			return null;
		}
		return list.get(0);
	}

	@Override
	public void addMCWarehouse(MCWarehouse mcWarehouse) {
		mcWarehouseDao.save(mcWarehouse);
	}
	@Override
	public void updateMCWarehouse(MCWarehouse mcWarehouse) {
		mcWarehouseDao.update(mcWarehouse);
	}

}
