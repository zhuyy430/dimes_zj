package com.digitzones.service;

import com.digitzones.model.WorkSheetDetailParametersRecord;

import java.util.List;

/**
 * 工单详情参数记录service
 * @author zdq
 * 2018年7月2日
 */
public interface IWorkSheetDetailParametersRecordService extends ICommonService<WorkSheetDetailParametersRecord> {
	/**
	 * 根据工件详情id和参数记录编码查找参数记录
	 * @param workSheetDetailId 工件详情id
	 * @param parameterCode 参数编码
	 * @return WorkSheetDetailParametersRecord
	 */
	public WorkSheetDetailParametersRecord queryByWorkSheetDetailIdAndParameterCode(Long workSheetDetailId,String parameterCode);
	/**
	 * 根据工单单号和工序编码查找工单详情参数信息
	 * @param no 工单单号
	 * @param processCode 工序代码
	 * @return
	 */
   public  List<WorkSheetDetailParametersRecord> queryByNoAndProcessCode(String no, String processCode);
}
