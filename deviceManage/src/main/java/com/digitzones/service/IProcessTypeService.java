package com.digitzones.service;

import java.util.List;

import com.digitzones.model.ProcessType;
/**
 * 工序类别service
 * @author zdq
 * 2018年6月14日
 */
public interface IProcessTypeService extends ICommonService<ProcessType> {
	/**
	 * 查找顶层工序类别
	 * @return
	 */
	public List<ProcessType> queryTopProcessTypes();
	/**
	 * 查找所有工序类别
	 * @return
	 */
	public List<ProcessType> queryAllProcessTypes();
}
