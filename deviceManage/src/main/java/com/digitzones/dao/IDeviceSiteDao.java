package com.digitzones.dao;

import java.math.BigInteger;
import java.util.List;

import com.digitzones.model.DeviceSite;
/**
 * 设备站点dao
 * @author zdq
 * 2018年6月11日
 */
public interface IDeviceSiteDao extends ICommonDao<DeviceSite> {
	/**
	 * 查询生产单元内的站点数目
	 * @return
	 */
	public Long queryCountOfDeviceSite();
	/**
	 * 根据站点状态查询
	 * @param status
	 * @return
	 */
	public Long queryCountOfDeviceSiteByStatus(String status);
	/**
	 * 根据班次id查询设备站点
	 * @param classesId
	 * @return
	 */
	public List<DeviceSite> queryDeviceSitesByClassesId(Long classesId);
	/**
	 * 根据设备站点ID查找损时记录数
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryLostTimeCountByDeviceSiteId(Long deviceSiteId);
	/**
	 * 根据设备站点查找按灯记录数
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryPressLightCountByDeviceSiteId(Long deviceSiteId);
	/**
	 * 根据设备站点ID查找工序和设备站点的关联数
	 * @param deviceSiteId
	 * @return
	 */
	public Integer queryProcessDeviceSiteMappingCountByDeviceSiteId(Long deviceSiteId);
	/**
	 * 根据生产单元ID查找设备站点
	 * @param productionUnitId
	 * @return
	 */
	public List<BigInteger> queryDeviceSiteIdsByProductionUnitId(Long productionUnitId);
	/**
	 * 查找所有生产单元的ID
	 * @return
	 */
	public List<BigInteger> queryAllDeviceSiteIds();
	/**
	 * 根据ID查找设备站点
	 * @param delids  排除的id
	 * @param queids  指定的id
	 * @return
	 */
	public List<DeviceSite> queryAllDeviceSiteByIds(String delids,String queids);
	/**
	 * 根据设备站点的代码和名称查询站点信息
	 * @param deviceSiteCode
	 * @param deviceSiteName
	 * @return
	 */
	public List<DeviceSite> queryDeviceSiteByCordAndName(String deviceSiteCode,String deviceSiteName);
	/**
	 * 根据生产单元ID查找瓶颈设备站点id
	 * @param productionUnitId
	 * @return
	 */
	public List<BigInteger> queryBottleneckDeviceSiteIdsByProductionUnitId(Long productionUnitId);
	/**
	 * 查找瓶颈站点的id
	 * @return
	 */
	public List<BigInteger> queryBottleneckDeviceSiteIds();
	/**
	 * 查找瓶颈站点数量
	 * @return
	 */
	public Long queryCountOfBottleneckDeviceSite();
	/**
	 * 根据设备id删除设备站点
	 * @param deviceId
	 */
	public void deleteByDeviceId(Long deviceId);
}
