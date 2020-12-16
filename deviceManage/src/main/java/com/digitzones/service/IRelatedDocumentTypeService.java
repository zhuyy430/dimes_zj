package com.digitzones.service;

import java.util.List;

import com.digitzones.model.RelatedDocumentType;
/**
 * 关联文档类别service
 * @author zdq
 * 2019年1月2日
 */
public interface IRelatedDocumentTypeService extends ICommonService<RelatedDocumentType> {
	/**
	 * 根据模块代码查找文档类型
	 * @param moduleCode 所属模块编码
	 * @return
	 */
	public List<RelatedDocumentType> queryRelatedDocumentTypeByModuleCode(String moduleCode);
	/**
	 * 查询当前关联文档类别是否被使用
	 * @param typeId
	 * @return
	 */
	public boolean isInUse(Long typeId);
}
