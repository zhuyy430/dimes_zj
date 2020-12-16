package com.digitzones.service;

import java.io.Serializable;
import java.util.List;

import com.digitzones.model.ProductionUnit;
/**
 * 生产单元业务逻辑接口
 * @author zdq
 * 2018年6月11日
 */
public interface IProductionUnitService extends ICommonService<ProductionUnit>{
	/**
	 * 查询所有生产单元，用于树形结构
	 * @return
	 */
	public List<ProductionUnit> queryTopProductionUnits();
	/**
	 * 根据父生产单元id查询子生产单元数量
	 * @param pid
	 * @return
	 */
	public Long queryCountOfSubProductionUnit(Serializable pid);
	/**
	 * 查询所有的生产单元(去除最顶层生产单元)
	 * @return
	 */
	public List<ProductionUnit> queryAllProductionUnits();
	/**
	 * 维修人员查询所有可添加的生产单元
	 * @param id
	 * @return
	 */
	public List<ProductionUnit> queryAllProductionUnitsByMaintenanceStaffId(String code);
	/**
	 * 根据产线id查找目标产量
	 * @param productionUnitId
	 * @return
	 */
	public Integer queryGoalOutputByProductionUnitId(Long productionUnitId);
	/**
	 * 根据生产单元id查找目标oee值
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
	 * 查询是否存在子生产单元
	 * @param id
	 * @return
	 */
	public boolean queryExistSubProductionUnit(Long id);
	/**
	 * 根据id查找生产单元中的ngGoal
	 * @param id
	 * @return ngGoal
	 */
	public double queryNgGoalById(Long id);
	/**
	 * 根据生产单元id查找是否存在设备信息
	 * @param id 生产单元id
	 * @return
	 */
	public boolean isExistDevices(Long id);
	/**
	 * 根据生产单元id查找是否存在员工信息
	 * @param id 生产单元id
	 * @return
	 */
	public boolean isExistEmployees(Long id);
	/**
	 * 查询顶层生产单元
	 * @return
	 */
	public ProductionUnit queryParentProductionUnit();
	/**
	 * 根据父id查询子生产单元信息
	 * @param parentId
	 * @return
	 */
	public List<ProductionUnit> queryByParentId(Long parentId);
}
