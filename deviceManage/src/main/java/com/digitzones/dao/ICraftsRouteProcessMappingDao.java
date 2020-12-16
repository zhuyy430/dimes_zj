package com.digitzones.dao;

import java.util.List;

import com.digitzones.model.CraftsRouteProcessMapping;
/**
 * 工件工序关联dao
 * @author zdq
 * 2018年6月15日
 */
public interface ICraftsRouteProcessMappingDao extends ICommonDao<CraftsRouteProcessMapping> {
	/**
	 * 根据工艺路线id和工序id删除关联
	 * @param craftsId
	 * @param processId
	 */
	public void deleteByCraftsIdAndProcessId(Long processId);
	/**
	 * 根据工艺路线id查询'工件 工序'
	 * @param craftsId
	 * @return
	 */
	public List<CraftsRouteProcessMapping> queryByCraftsId(String craftsId);
}
