package com.digitzones.devmgr.service;

import com.digitzones.devmgr.model.MaintenanceRelatedDocument;
import com.digitzones.service.ICommonService;

public interface IMaintenanceRelatedDocumentService extends ICommonService<MaintenanceRelatedDocument>{
	
	/**
	 * 根据文件名称和关联类型查找是否存在该文件
	 * @param filename
	 * @param recordId
	 * @return
	 */
	public boolean isExistFile(String filename,Long recordId);
}
