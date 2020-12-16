package com.digitzones.dao;

import java.util.List;

import com.digitzones.model.RelatedDocument;
/**
 * 相关文档数据访问接口
 * @author zdq
 * 2018年6月7日
 */
public interface IRelatedDocumentDao extends ICommonDao<RelatedDocument> {
	/**
	 * 根据文件名称和关联类型查询文件数量
	 * @param filename
	 * @param relatedType
	 * @param relatedId
	 * @return
	 */
	public int queryCountByFilenameAndRelatedType(String filename,Long docTypeId,Long relatedId);
	
	/**
	 * 根据设备id查找文档
	 * @return
	 */
	public List<RelatedDocument> queryRelatedDocumentByDeviceId(Long deviceId);
}
