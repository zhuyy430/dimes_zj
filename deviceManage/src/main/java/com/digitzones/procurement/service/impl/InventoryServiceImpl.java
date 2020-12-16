package com.digitzones.procurement.service.impl;
import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IInventoryDao;
import com.digitzones.procurement.model.Inventory;
import com.digitzones.procurement.service.IInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serializable;
import java.util.List;
@Service
public class InventoryServiceImpl implements IInventoryService {
	@Autowired
	private IInventoryDao inventoryDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return inventoryDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Inventory obj) {
		inventoryDao.update(obj);
	}

	@Override
	public Inventory queryByProperty(String name, String value) {
		return inventoryDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Inventory obj) {
		return inventoryDao.save(obj);
	}

	@Override
	public Inventory queryObjById(Serializable id) {
		return inventoryDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		inventoryDao.deleteById(id);
	}

	@Override
	public List<Inventory> queryAllInventory() {
		return inventoryDao.findByHQL("from Inventory i",new Object[] {});
	}
	@Override
	public List<Inventory> queryAllWorkpieces(String q) {
		String hql = "from Inventory w where w.code like ?0 or w.name like ?0 or w.unitType like ?0 or w.graphNumber like ?0";
		return this.inventoryDao.findByHQL(hql, new Object[] {"%" + q + "%"});
	}
	@Override
	public List<Inventory> queryAllWorkpieces() {
		String hql = "from Inventory w ";
		return this.inventoryDao.findByHQL(hql, new Object[] {});
	}

	/**
	 * 根据物料编码查找物料
	 *
	 * @param codesList
	 * @return
	 */
	@Override
	public List<Inventory> queryByCodes(List<String> codesList) {
		return inventoryDao.findByHQL("from Inventory where code in ?0",new Object[]{codesList});
	}
}
