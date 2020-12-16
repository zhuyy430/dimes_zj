package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.MaintainProject;
import com.digitzones.service.ICommonService;

/**
 * 设备关联项目接口
 */
public interface IMaintainProjectService extends ICommonService<MaintainProject>{
	/**
	 * 根据报修单查询维修项目
	 */
	public List<MaintainProject> queryMaintainProject(Long deviceRepairOrderId);
}
