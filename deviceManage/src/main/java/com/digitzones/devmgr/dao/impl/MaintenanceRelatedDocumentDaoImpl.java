package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenanceRelatedDocumentDao;
import com.digitzones.devmgr.model.MaintenanceRelatedDocument;
@Repository
public class MaintenanceRelatedDocumentDaoImpl extends CommonDaoImpl<MaintenanceRelatedDocument> implements IMaintenanceRelatedDocumentDao{
	public MaintenanceRelatedDocumentDaoImpl() {
		super(MaintenanceRelatedDocument.class);
	}

	@Override
	public int queryCountByFilenameAndRelatedType(String filename, Long recordId) {
		return (int) getSession().createNativeQuery("select count(id) from MaintenanceRelatedDocument rd where rd.srcName=?0 and rd.Record_ID=?1 ")
				.setParameter(0, filename)
				.setParameter(1, recordId)
				.uniqueResult();
	}
}
