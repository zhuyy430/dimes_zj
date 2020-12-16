package com.digitzones.dao;

import com.digitzones.model.ProcessesParametersMapping;
/**
 * 工序和参数关联dao
 * @author zdq
 * 2018年6月14日
 */
public interface IProcessesParametersMappingDao extends ICommonDao<ProcessesParametersMapping> {
	/**
	 * 根据工序id和参数id删除关联
	 * @param processesId
	 * @param parameterId
	 */
	public void deleteByProcessesIdAndParameterId(Long processesId, Long parameterId);
}
