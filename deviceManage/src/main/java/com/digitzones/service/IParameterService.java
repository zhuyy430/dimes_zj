package com.digitzones.service;

import java.util.List;

import com.digitzones.model.Parameters;
/**
 * 参数service
 * @author zdq
 * 2018年6月12日
 */
public interface IParameterService extends ICommonService<Parameters> {
	/**
	 * 查询所有 参数
	 * @return
	 */
	public List<Parameters> queryAllParameters();
	/**
	 * 根据设备站点id查询非当前设备站点的参数
	 * @param deviceId
	 * @return
	 */
	public List<Parameters> queryOtherParametersByDeviceSiteId(Long deviceSiteId);
	/**
	 * 根据设备id查询非当前设备站点的参数
	 * @param deviceId
	 * @return
	 */
	public List<Parameters> queryOtherParametersByDeviceId(Long deviceId);
}
