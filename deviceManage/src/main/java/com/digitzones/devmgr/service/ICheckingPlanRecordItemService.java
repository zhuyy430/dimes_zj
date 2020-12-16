package com.digitzones.devmgr.service;

import java.util.List;

import com.digitzones.devmgr.model.CheckingPlanRecordItem;
import com.digitzones.service.ICommonService;

public interface ICheckingPlanRecordItemService extends ICommonService<CheckingPlanRecordItem> {
	/**
	 * 根据点检记录id查找点检记录项
	 * @param checkingPlanRecordId
	 * @return
	 */
	public List<CheckingPlanRecordItem> queryByCheckingPlanRecordId(Long checkingPlanRecordId);
	/**
	 * 根据点检记录id查找点检过的记录项数量
	 */
	public int queryResultCountByCheckingPlanRecordId(Long checkingPlanRecordId);
}
