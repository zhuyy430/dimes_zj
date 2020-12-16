package com.digitzones.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IRelatedDocumentDao;
import com.digitzones.model.RelatedDocument;
@Repository
public class RelatedDocumentDaoImpl extends CommonDaoImpl<RelatedDocument> implements IRelatedDocumentDao {
	public RelatedDocumentDaoImpl() {
		super(RelatedDocument.class);
	}
	@Override
	public int queryCountByFilenameAndRelatedType(String filename, Long docTypeId,Long relatedId) {
		return (int) getSession().createNativeQuery("select count(id) from RELATEDDOCUMENT rd where rd.srcName=?0 and rd.TYPE_ID=?1 and rd.relatedId=?2")
							.setParameter(0, filename)
							.setParameter(1, docTypeId)
							.setParameter(2, relatedId)
							.uniqueResult();
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<RelatedDocument> queryRelatedDocumentByDeviceId(Long deviceId) {
		String sql = "select * from RELATEDDOCUMENT doc where doc.relatedId=?0";
		List res = getSession().createSQLQuery(sql).addEntity(RelatedDocument.class).setParameter(0, deviceId.toString()).list();
		return res;
	}
}
