package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IRelatedDocumentDao;
import com.digitzones.dao.IRelatedDocumentTypeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.RelatedDocumentType;
import com.digitzones.service.IRelatedDocumentTypeService;
@Service
public class RelatedDocumentTypeServiceImpl implements IRelatedDocumentTypeService {
	@Autowired
	private IRelatedDocumentTypeDao relatedDocumentTypeDao;
	@Autowired
	private IRelatedDocumentDao relatedDocumentDao;
	@Override
	public Pager<RelatedDocumentType> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return relatedDocumentTypeDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(RelatedDocumentType obj) {
		relatedDocumentTypeDao.update(obj);
	}
	@Override
	public RelatedDocumentType queryByProperty(String name, String value) {
		return relatedDocumentTypeDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(RelatedDocumentType obj) {
		return relatedDocumentTypeDao.save(obj);
	}
	@Override
	public RelatedDocumentType queryObjById(Serializable id) {
		return relatedDocumentTypeDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		relatedDocumentTypeDao.deleteById(id);
	}
	@Override
	public List<RelatedDocumentType> queryRelatedDocumentTypeByModuleCode(String moduleCode) {
		return relatedDocumentTypeDao.findByHQL("from RelatedDocumentType type where type.disabled=?0 and type.moduleCode=?1", new Object[] {false,moduleCode});
	}
	@Override
	public boolean isInUse(Long typeId) {
		Long count = relatedDocumentDao.findCount("from RelatedDocument doc where doc.relatedDocumentType.id=?0", new Object[] {typeId});
		return (count==null || count == 0)?false:true;
	}
}
