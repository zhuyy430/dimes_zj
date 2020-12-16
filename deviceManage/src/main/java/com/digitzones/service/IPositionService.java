package com.digitzones.service;
/**
 * 岗位业务逻辑接口
 * @author zdq
 * 2018年6月7日
 */

import java.io.Serializable;
import java.util.List;

import com.digitzones.model.Position;

public interface IPositionService extends ICommonService<Position> {
	/**
	 * 添加岗位信息
	 * @param position
	 * @return
	 */
	public Serializable addPosition(Position position);
	/**
	 * 根据id删除岗位
	 * @param id
	 */
	public void deletePosition(Serializable id);
	/**
	 * 更新岗位信息
	 * @param position
	 */
	public void updatePosition(Position position);
	/**
	 * 根据部门id查找岗位信息
	 * @param deptId
	 * @return
	 */
	public List<Position> queryPositionByDepartmentId(Serializable  deptId);
	/**
	 * 根据id查找职位信息
	 * @param id
	 * @return
	 */
	public Position queryPositionById(Serializable id);
	/**
	 * 根据hql查找所有岗位
	 * @param hql
	 * @param values
	 * @return
	 */
	public List<Position> queryAllByHql(String hql,Object... values);
	/**
	 * 查询当前岗位下是否存在员工
	 * @param id 部门id
	 * @return boolean true:存在  false:不存在
	 */
	public boolean isExistEmployees(Long id);
}
