package com.digitzones.procurement.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IInventoryClassDao;
import com.digitzones.procurement.model.InventoryClass;
import com.digitzones.procurement.service.IInventoryClassService;
@Service
public class InventoryClassServiceImpl implements IInventoryClassService {
	@Autowired
	private IInventoryClassDao inventoryClassDao;

	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return inventoryClassDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(InventoryClass obj) {
		inventoryClassDao.update(obj);
	}

	@Override
	public InventoryClass queryByProperty(String name, String value) {
		return inventoryClassDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(InventoryClass obj) {
		return inventoryClassDao.save(obj);
	}

	@Override
	public InventoryClass queryObjById(Serializable id) {
		return inventoryClassDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		inventoryClassDao.deleteById(id);
	}

	@Override
	public List<InventoryClass> queryTopInventoryClass() {
		List<InventoryClass> list = inventoryClassDao.findByHQL("from InventoryClass ic where ic.iInvCGrade=1", new Object[] {});
		 return list;
	}

	@Override
	public List<InventoryClass> queryChildrenInventoryClass(String code,int level) {
			return inventoryClassDao.findByHQL("from InventoryClass ic where ic.iInvCGrade=?0 and ic.cInvCCode like ?1", new Object[] {level+"",code+"%"});
	}
}
