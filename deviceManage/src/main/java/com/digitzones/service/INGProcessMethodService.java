package com.digitzones.service;

import java.util.List;

import com.digitzones.model.NGProcessMethod;
/**
 * 不合格品处理详情service
 * @author zdq
 * 2018年7月11日
 */
public interface INGProcessMethodService extends ICommonService<NGProcessMethod> {
	/**
	 * 根据不合格记录id查找不合格处理详情
	 * @param ngRecordId
	 * @return
	 */
	public List<NGProcessMethod> queryNGProcessMethodsByNGRecordId(Long ngRecordId);
}
