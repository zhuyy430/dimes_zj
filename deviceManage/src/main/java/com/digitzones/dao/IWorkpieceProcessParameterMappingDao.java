package com.digitzones.dao;

import com.digitzones.model.WorkpieceProcessParameterMapping;

import java.util.List;

/**
 * 工件工序参数关联dao
 * @author zdq
 * 2018年6月15日
 */
public interface IWorkpieceProcessParameterMappingDao extends ICommonDao<WorkpieceProcessParameterMapping> {

	/**
	 * 根据工单单号和工序代码查找工件工序参数想信息
	 * @param no
	 * @param processCode
	 * @return
	 */
    public List<WorkpieceProcessParameterMapping> queryByNoAndProcessCode(String no, String processCode);
}
