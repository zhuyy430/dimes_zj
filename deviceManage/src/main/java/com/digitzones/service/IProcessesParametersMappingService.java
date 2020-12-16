package com.digitzones.service;

import com.digitzones.model.ProcessesParametersMapping;
/**
 * 工序和设备站点关联service
 * @author zdq
 * 2018年6月14日
 */
public interface IProcessesParametersMappingService extends ICommonService<ProcessesParametersMapping> {
	/**
	 * 根据工序id和参数id删除关联
	 * @param processesId 
	 * @param parameterId 
	 */
	public void deleteByProcessesIdAndParameterId(Long processesId,Long parameterId);
}
