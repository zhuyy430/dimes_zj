package com.digitzones.devmgr.dao;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.MaintenanceRelatedDocument;

public interface IMaintenanceRelatedDocumentDao extends ICommonDao<MaintenanceRelatedDocument>{
	/**
	 * 根据文件名称和关联类型查询文件数量
	 * @param filename
	 * @param recordId
	 * @return
	 */
	public int queryCountByFilenameAndRelatedType(String filename,Long recordId);
}
