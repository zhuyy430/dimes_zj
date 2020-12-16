package com.digitzones.service;

import com.digitzones.model.RelatedDocument;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
/**
 * 相关文档操作业务接口
 * @author zdq
 * 2018年6月8日
 */
public interface IRelatedDocumentService extends ICommonService<RelatedDocument> {
	/**
	 * 添加相关文档
	 * @param relatedDocument
	 * @return
	 */
	public Serializable addRelatedDocument(RelatedDocument relatedDocument);
	/**
	 * 删除相关文档
	 * @param id
	 */
	public void deleteRelatedDocument(Serializable id);
	/**
	 * 更新相关文档
	 * @param relatedDocument
	 */
	public void updateRelatedDocument(RelatedDocument relatedDocument);
	/**
	 * 根据文件名称和关联类型查找是否存在该文件
	 * @param filename
	 * @param docTypeId
	 * @param relatedId
	 * @return
	 */
	public boolean isExistFile(String filename,Long docTypeId,Long relatedId);
	/**
	 * 根据文档类型模块编码，文档类型编码，关联id查找文档
	 * @param docTypeModuleCode
	 * @param docTypeCode
	 * @param relatedId
	 * @return
	 */
	public List<RelatedDocument> queryRelatedDocumentByDocTypeCodeAndDocTypeModuleCodeAndRelatedId(String docTypeModuleCode,String docTypeCode,Long relatedId);
	/**
	 * 根据设备id查找文档
	 * @return
	 */
	public List<RelatedDocument> queryRelatedDocumentByDeviceId(Long deviceId);
	/**
	 * 根据条件查找工件文档
	 * @param param
	 * @return
	 */
	public List<RelatedDocument> queryWorkpieceDocsByConditions(Map<String, String> param);
	/**
	 * 根据模块代码和关联实体的id查找文档
	 * @param relatedId
	 * @param moduleCode
	 * @return
	 */
	public List<RelatedDocument> queryDocsByConditionsByDocTypeModuleCodeAndRelatedId(String relatedId,String moduleCode);

	/**
	 * 根据生产单元查询当前加工中的工件的文档
	 */
	public List<RelatedDocument> queryDimesDo(Long productionUnitId);
}
