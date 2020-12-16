package com.digitzones.dao;

import java.util.List;

import com.digitzones.model.ProductionUnit;
/**
 * 生产单元数据访问接口
 * @author zdq
 * 2018年6月11日
 */
public interface IProductionUnitDao extends ICommonDao<ProductionUnit> {
	/**
	 * 根据产线id查找目标产量
	 * @param productionUnitId
	 * @return
	 */
	public Integer queryGoalOutputByProductionUnitId(Long productionUnitId);
	/**
	 * 根据生产单元id查找最大目标oee
	 * @param productionUnitId
	 * @return
	 */
	public double queryOeeByProductionUnitId(Long productionUnitId);
	/**
	 * 查找生产单元损时目标
	 * @param productionUnitId
	 * @return
	 */
	public double queryGoalLostTimeByProductionUnitId(Long productionUnitId);
	/**
	 * 查询所有叶子节点生产单元
	 * @return
	 */
	public List<ProductionUnit> queryAllLeafProductionUnits();
	/**
	 * 根据生产单元id查找ngGoal
	 * @param id
	 * @return ngGoal
	 */
	public double queryNgGoalById(Long id);
	/**
	 * 根据生产单元id查找设备数
	 * @param id 生产单元id
	 * @return 设备数量
	 */
	public int queryDevicesCount(Long id);
	/**
	 * 根据生产单元id查找员工数
	 * @param id 生产单元id
	 * @return 员工数量
	 */
	public int queryEmployeesCount(Long id);
	/**
	 * 查询顶层生产单元
	 * @return
	 */
	public ProductionUnit queryParentProductionUnit();
	/**
	 * 根据父生产单元id查找子生产单元
	 * @param id
	 * @return
	 */
	public List<ProductionUnit> queryProductionUnitsByParentId(Long id);
}
