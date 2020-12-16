package com.digitzones.procurement.service;
import java.util.List;

import com.digitzones.procurement.model.ApplicationRelatedDocument;
import com.digitzones.service.ICommonService;
/**
 * 申请入库详情service
 * @author zdq
 * 2018年6月11日
 */
public interface IApplicationRelatedDocumentService extends ICommonService<ApplicationRelatedDocument> {
	
	/**
	 * 根据单号获取文档
	 * @param id
	 * @return
	 */
	public List<ApplicationRelatedDocument> queryApplicationRelatedDocumentByDetailId(String id);
}
