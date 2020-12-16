package com.digitzones.service;

import com.digitzones.model.ProcessParameterRecord;

/**
 * 加工记录service
 * @author zdq
 * 2018年6月20日
 */
public interface IProcessParameterRecordService extends ICommonService<ProcessParameterRecord> {
	/**
	 * 查询给定执行记录下，是否存在参数信息
	 * @param processRecordId 执行记录id
	 * @return
	 */
	public boolean queryIsExistByProcessRecordId(Long processRecordId);
	/**
	 * 根据设备执行记录id和参数代码查询参数记录 
	 * @param processRecordId 设备执行记录ID
	 * @param parameterCode 参数代码
	 * @return
	 */
	public ProcessParameterRecord queryByProcessRecordIdAndParamterCode(Long processRecordId, String parameterCode);
}
