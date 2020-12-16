package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import com.digitzones.model.PressLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IPressLightDao;
import com.digitzones.dao.IPressLightTypeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.PressLightType;
import com.digitzones.service.IPressLightTypeService;
@Service 
public class PressLightTypeServiceImpl implements IPressLightTypeService {
	private IPressLightTypeDao pressLightTypeDao;
	@Autowired
	private IPressLightDao pressLightDao;
	@Autowired
	public void setPressLightTypeDao(IPressLightTypeDao pressLightTypeDao) {
		this.pressLightTypeDao = pressLightTypeDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return pressLightTypeDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(PressLightType obj) {
		pressLightTypeDao.update(obj);
	}

	@Override
	public PressLightType queryByProperty(String name, String value) {
		return pressLightTypeDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(PressLightType obj) {
		return pressLightTypeDao.save(obj);
	}

	@Override
	public PressLightType queryObjById(Serializable id) {
		return pressLightTypeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		/*List<PressLight> list = pressLightDao.findByHQL("from PressLight p inner join p.pressLightType pt where pt.id=?0", new Object[]{id});
		for (PressLight pressLight : list) {
			pressLightDao.delete(pressLight);
		}*/
		pressLightTypeDao.deleteById(id);
	}

	@Override
	public Long queryCountOfSubPressLightType(Serializable pid) {
		return pressLightTypeDao.findCount("from PressLightType d inner join d.parent p where p.id=?0", new Object[] {pid});
	}

	@Override
	public List<PressLightType> queryTopPressLightType(String type) {
		return  pressLightTypeDao.findByHQL("from PressLightType p where p.parent is null and p.typeName=?0", new Object[] {type});
	}

	@Override
	public List<PressLightType> queryFirstLevelType(String type) {
		return pressLightTypeDao.findByHQL("from PressLightType p where p.level=?0 and p.typeName=?1", new Object[] {1,type});
	}

	@Override
	public List<PressLightType> queryAllPressLightTypesByParentId(Long pid,String type) {
		return pressLightTypeDao.findByHQL("from PressLightType p where p.parent.id=?0 and p.typeName=?1", new Object[] {pid,type});
	}

	@Override
	public List<PressLightType> queryAllPressLightTypes(String type) {
		return pressLightTypeDao.findAll();
	}

	@Override
	public boolean isExistPressLights(Long id) {
		return pressLightDao.findCount("from PressLight pl where pl.pressLightType.id=?0",new Object[] {id})>0?true:false;
	}
}
