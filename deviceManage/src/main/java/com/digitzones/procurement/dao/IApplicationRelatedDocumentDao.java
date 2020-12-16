package com.digitzones.procurement.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.procurement.model.ApplicationRelatedDocument;
/**
 * 入库申请单文档dao
 * @author zdq
 * 2018年6月11日
 */
public interface IApplicationRelatedDocumentDao extends ICommonDao<ApplicationRelatedDocument> {
	
	public List<ApplicationRelatedDocument> queryApplicationRelatedDocumentByDetailId(String id);
}
