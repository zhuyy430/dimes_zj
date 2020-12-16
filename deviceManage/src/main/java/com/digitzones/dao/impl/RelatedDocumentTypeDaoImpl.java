package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IRelatedDocumentTypeDao;
import com.digitzones.model.RelatedDocumentType;
@Repository
public class RelatedDocumentTypeDaoImpl extends CommonDaoImpl<RelatedDocumentType> implements IRelatedDocumentTypeDao {
	public RelatedDocumentTypeDaoImpl() {
		super(RelatedDocumentType.class);
	}
}
