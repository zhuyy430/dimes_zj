package com.digitzones.service;

import java.util.List;

import com.digitzones.model.NGReason;
/**
 * 不良原因service
 * @author zdq
 * 2018年6月13日
 */
public interface INGReasonService extends ICommonService<NGReason> {
	/**
	 * 根据不合格类型id查找不合格原因
	 * @param ngReasonTypeId
	 * @return
	 */
	public List<NGReason> queryNGReasonsByNGReasonTypeId(Long ngReasonTypeId);
	/**
	 * 根据工序ID查找ng原因
	 * @param processId
	 * @return
	 */
	public List<NGReason> queryNGReasonByProcessId(Long processId);
	/**
	 * 根据工序Code查找ng原因
	 * @param processCode
	 * @return
	 */
	public List<NGReason> queryNGReasonByProcessCode(String processCode);
	/**
	 * 查找所有不合格原因
	 * @return
	 */
	public List<NGReason> queryAllNGReasons();
	/**
	 * 查找所有不良原因大类
	 * @return
	 */
   	public List<String> queryAllCategories();
}
