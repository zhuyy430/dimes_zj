package com.digitzones.devmgr.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.model.Pager;
import com.digitzones.devmgr.dao.IMaintenanceRelatedDocumentDao;
import com.digitzones.devmgr.model.MaintenanceRelatedDocument;
import com.digitzones.devmgr.service.IMaintenanceRelatedDocumentService;

@Service
public class MaintenanceRelatedDocumentServiceImpl implements IMaintenanceRelatedDocumentService{
	@Autowired
	private IMaintenanceRelatedDocumentDao maintenanceRelatedDocumentDao;
	
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return maintenanceRelatedDocumentDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MaintenanceRelatedDocument obj) {
		 maintenanceRelatedDocumentDao.update(obj);
	}

	@Override
	public MaintenanceRelatedDocument queryByProperty(String name, String value) {
		return maintenanceRelatedDocumentDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MaintenanceRelatedDocument obj) {
		return maintenanceRelatedDocumentDao.save(obj);
	}

	@Override
	public MaintenanceRelatedDocument queryObjById(Serializable id) {
		return maintenanceRelatedDocumentDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		maintenanceRelatedDocumentDao.deleteById(id);
	}

	@Override
	public boolean isExistFile(String filename, Long recordId) {
		return maintenanceRelatedDocumentDao.queryCountByFilenameAndRelatedType(filename, recordId)>0?true:false;
	}

}
