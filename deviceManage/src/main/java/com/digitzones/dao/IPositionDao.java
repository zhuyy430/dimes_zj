package com.digitzones.dao;

import com.digitzones.model.Position;
/**
 * 岗位数据访问接口
 * @author zdq
 * 2018年6月7日
 */
public interface IPositionDao extends ICommonDao<Position>{
	/**
	 * 根据岗位id查找员工数
	 * @param id 岗位id
	 * @return
	 */
	public int queryEmployeeCountById(Long id);
}
