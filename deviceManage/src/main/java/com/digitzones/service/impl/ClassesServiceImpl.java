package com.digitzones.service.impl;

import com.digitzones.dao.IClassesDao;
import com.digitzones.dao.IClassesDeviceMappingDao;
import com.digitzones.model.Classes;
import com.digitzones.model.ClassesDeviceMapping;
import com.digitzones.model.Pager;
import com.digitzones.service.IClassesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Service
public class ClassesServiceImpl implements IClassesService {
	private IClassesDao  classesDao;
	@Autowired
	private IClassesDeviceMappingDao classesDeviceMappingDao;
	@Autowired
	public void setClassesDao(IClassesDao classesDao) {
		this.classesDao = classesDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return classesDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Classes obj) {
		classesDao.update(obj);
	}

	@Override
	public Classes queryByProperty(String name, String value) {
		return classesDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Classes obj) {
		return classesDao.save(obj);
	}

	@Override
	public Classes queryObjById(Serializable id) {
		return classesDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		List<ClassesDeviceMapping> list = classesDeviceMappingDao.findByHQL("from ClassesDeviceMapping cdm where cdm.classes.id=?0", new Object[] {id});
		if(!CollectionUtils.isEmpty(list)) {
			for(ClassesDeviceMapping cdm : list) {
				classesDeviceMappingDao.delete(cdm);
			}
		}
		classesDao.deleteById(id);
	}

	@Override
	public Classes queryCurrentClasses() {
		return classesDao.queryCurrentClasses();
	}

	@Override
	public List<Classes> queryAllClasses() {
		return classesDao.findAll();
	}

	@Override
	public Classes queryClassesByTime(Date date) {
		return classesDao.queryClassesByTime(date);
	}
	@Override
	public Classes queryClassesByTimeAndClassTypeName(Date date,String classTypeName) {
		return classesDao.queryClassesByTimeAndClassTypeName(date,classTypeName);
	}

	@Override
	public List<Classes> queryAllClassesByproductionUnitCode(String code) {
		return classesDao.findByHQL("from Classes c where c.classType.id in(select p.classType.id from ProductionUnit p where p.code=?0)",new Object[]{code});
	}
	@Override
	public Classes queryClassesByDeviceSiteCode(String code) {
		return classesDao.queryClassesByDeviceSiteCode(code);
	}

	@Override
	public Classes queryCurrentClassesByClassesTypeName(String classesTypeName) {
		return classesDao.queryCurrentClassesByClassTypeName(classesTypeName);
	}
}
