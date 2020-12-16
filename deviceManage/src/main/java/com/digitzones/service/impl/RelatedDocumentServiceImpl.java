package com.digitzones.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.dao.IRelatedDocumentDao;
import com.digitzones.model.Pager;
import com.digitzones.model.RelatedDocument;
import com.digitzones.service.IRelatedDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@SuppressWarnings("rawtypes")
@Service
public class RelatedDocumentServiceImpl  implements IRelatedDocumentService {
	private IRelatedDocumentDao relatedDocumentDao;
	@Autowired
	public void setRelatedDocumentDao(IRelatedDocumentDao relatedDocumentDao) {
		this.relatedDocumentDao = relatedDocumentDao;
	}
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return relatedDocumentDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public Serializable addRelatedDocument(RelatedDocument relatedDocument) {
		return relatedDocumentDao.save(relatedDocument);
	}
	@Override
	public void deleteRelatedDocument(Serializable id) {
		relatedDocumentDao.deleteById(id);
	}
	@Override
	public void updateRelatedDocument(RelatedDocument relatedDocument) {
		relatedDocumentDao.update(relatedDocument);
	}
	@Override
	public void updateObj(RelatedDocument obj) {
		this.relatedDocumentDao.update(obj);
	}
	@Override
	public RelatedDocument queryByProperty(String name, String value) {
		return relatedDocumentDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(RelatedDocument obj) {
		return relatedDocumentDao.save(obj);
	}
	@Override
	public RelatedDocument queryObjById(Serializable id) {
		return relatedDocumentDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		relatedDocumentDao.deleteById(id);
	}
	@Override
	public boolean isExistFile(String filename, Long docTypeId,Long relatedId) {
		return relatedDocumentDao.queryCountByFilenameAndRelatedType(filename, docTypeId,relatedId)>0?true:false;
	}
	@Override
	public List<RelatedDocument> queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(String moduleCode,
			String docTypeCode, Long relatedId) {
		String hql = "from RelatedDocument doc where doc.relatedDocumentType.moduleCode=?0 and doc.relatedDocumentType.code=?1 and doc.relatedId=?2";
		return relatedDocumentDao.findByHQL(hql, new Object[] {moduleCode,docTypeCode,relatedId.toString()});
	}
	@Override
	public List<RelatedDocument> queryRelatedDocumentByDeviceId(Long deviceId) {
		return relatedDocumentDao.queryRelatedDocumentByDeviceId(deviceId);
	}
	@Override
	public List<RelatedDocument> queryWorkpieceDocsByConditions(Map<String, String> param) {
		String hql = "select doc from RelatedDocument doc join doc.relatedDocumentType type "
				+ " where type.moduleCode=?0 ";
		int i = 1;
		List<Object> values = new ArrayList<>();
		//文档类别
		String docType = param.get("docType");
		if(!StringUtils.isEmpty(docType)) {
			hql += " and type.id=?"+(i++);
			values.add(Long.valueOf(docType));
		}
		//编码类别：工件编码、客户图号、产品图号
		String codeType = param.get("codeType");
		String code = param.get("code");
		if(!StringUtils.isEmpty(code)) {
			if(!StringUtils.isEmpty(codeType)){
				switch(codeType) {
					//工件编码
					case "workpiece":{
						hql +=" and doc.relatedId=(select code from Inventory w where w.code=?"+(i++)+")";
						break;
					}
					//产品图号
					case "graphNumber":{
						hql +=" and doc.relatedId in (select code from Inventory w where w.graphNumber=?"+(i++)+")";
						break;
					}
					//客户图号
			/*case "customerGraphNumber":{
				hql +=" and doc.relatedId in (select id from Inventory w where w.customerGraphNumber=?"+(i++)+")";
				break;
			}*/
				}
				values.add(code);
			}else{

				hql +=" and doc.relatedId in (select code from Inventory w where w.graphNumber=?"+i+" or w.code=?"+i+" )";
				i++;
				values.add(code);
			}

		}
		
		Object[] params = new Object[values.size()+1];
		params[0] = Constant.RelatedDoc.WORKPIECE;
		for(int j = 0;j<values.size();j++) {
			params[j+1]=values.get(j);
		}
		return relatedDocumentDao.findByHQL(hql,params);
	}
	@Override
	public List<RelatedDocument> queryDocsByConditionsByDocTypeModuleCodeAndRelatedId(String relatedId,
			String moduleCode) {
		return relatedDocumentDao.findByHQL("from RelatedDocument rd where rd.relatedDocumentType.moduleCode=?0 and rd.relatedId=?1",new Object[] {moduleCode,relatedId});
		 
	}

	@Override
	public List<RelatedDocument> queryDimesDo(Long productionUnitId) {
		return relatedDocumentDao.findByHQL("from RelatedDocument rd where rd.relatedId  in " +
				"(select w.workPieceCode from WorkSheet w where w.productionUnitId=?0 and w.status=?1 )",new Object[] {productionUnitId,Constant.WorkSheet.Status.PROCESSING});
	}
}
