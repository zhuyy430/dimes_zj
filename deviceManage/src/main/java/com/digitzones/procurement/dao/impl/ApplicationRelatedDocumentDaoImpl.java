package com.digitzones.procurement.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IApplicationRelatedDocumentDao;
import com.digitzones.procurement.model.ApplicationRelatedDocument;
@Repository
public class ApplicationRelatedDocumentDaoImpl extends CommonDaoImpl<ApplicationRelatedDocument> implements IApplicationRelatedDocumentDao {

	public ApplicationRelatedDocumentDaoImpl() {
		super(ApplicationRelatedDocument.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<ApplicationRelatedDocument> queryApplicationRelatedDocumentByDetailId(String id) {
		String sql = "select * from ApplicationRelatedDocument doc where doc.relatedId=?0";
		return getSession().createSQLQuery(sql).addEntity(ApplicationRelatedDocument.class).setParameter(0, id).list();
	}
}
