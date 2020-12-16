package com.digitzones.procurement.service.impl;
import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.IApplicationRelatedDocumentDao;
import com.digitzones.procurement.model.ApplicationRelatedDocument;
import com.digitzones.procurement.service.IApplicationRelatedDocumentService;
@Service
public class ApplicationRelatedDocumentServiceImpl implements IApplicationRelatedDocumentService {
	@Autowired
	private IApplicationRelatedDocumentDao applicationRelatedDocumentDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return applicationRelatedDocumentDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(ApplicationRelatedDocument obj) {
		applicationRelatedDocumentDao.update(obj);
	}

	@Override
	public ApplicationRelatedDocument queryByProperty(String name, String value) {
		return applicationRelatedDocumentDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ApplicationRelatedDocument obj) {
		return applicationRelatedDocumentDao.save(obj);
	}

	@Override
	public ApplicationRelatedDocument queryObjById(Serializable id) {
		return applicationRelatedDocumentDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		applicationRelatedDocumentDao.deleteById(id);
	}

	@Override
	public List<ApplicationRelatedDocument> queryApplicationRelatedDocumentByDetailId(String id) {
		return applicationRelatedDocumentDao.queryApplicationRelatedDocumentByDetailId(id);
	}

}
