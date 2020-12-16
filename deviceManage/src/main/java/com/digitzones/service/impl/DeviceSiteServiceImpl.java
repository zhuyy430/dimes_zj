package com.digitzones.service.impl;

import com.digitzones.dao.IDeviceSiteDao;
import com.digitzones.dao.IDeviceSiteParameterMappingDao;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.DeviceSiteParameterMapping;
import com.digitzones.model.Pager;
import com.digitzones.service.IDeviceSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
@Service
public class DeviceSiteServiceImpl implements IDeviceSiteService {
	private IDeviceSiteDao deviceSiteDao;
	@Autowired
	private IDeviceSiteParameterMappingDao deviceSiteParameterMappingDao;
	@Autowired
	public void setDeviceSiteDao(IDeviceSiteDao deviceSiteDao) {
		this.deviceSiteDao = deviceSiteDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceSiteDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceSite obj) {
		deviceSiteDao.update(obj);
	}

	@Override
	public DeviceSite queryByProperty(String name, String value) {
		return deviceSiteDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceSite obj) {
		return deviceSiteDao.save(obj);
	}

	@Override
	public DeviceSite queryObjById(Serializable id) {
		return deviceSiteDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		List<DeviceSiteParameterMapping> list = deviceSiteParameterMappingDao.findByHQL("from DeviceSiteParameterMapping dpm where dpm.deviceSite.id=?0", new Object[] {id});
		//删除与之关联的参数记录
		if(!CollectionUtils.isEmpty(list)) {
			for(DeviceSiteParameterMapping dpm : list) {
				deviceSiteParameterMappingDao.delete(dpm);
			}
		}
		deviceSiteDao.deleteById(id);
	}

	@Override
	public Long queryCountOfDeviceSite() {
		return deviceSiteDao.queryCountOfDeviceSite();
	}

	@Override
	public Long queryCountOfDeviceSiteByStatus(String status) {
		return deviceSiteDao.queryCountOfDeviceSiteByStatus(status);
	}

	@Override
	public List<DeviceSite> queryDeviceSitesByProductionUnitId(Long productionUnitId) {
		String hql = "select ds from DeviceSite ds inner join ds.device d inner join d.productionUnit p where p.id=?0"
				+ " and d.isDimesUse=?1";
		return deviceSiteDao.findByHQL(hql, new Object[] {productionUnitId,true});
	}

	@Override
	public List<DeviceSite> queryAllDeviceSites() {
		return deviceSiteDao.findByHQL("from DeviceSite ds where ds.device.isDimesUse=?0 ", new Object[] {true});
	}

	@Override
	public List<DeviceSite> queryDeviceSitesByShow(boolean isShow) {
		return this.deviceSiteDao.findByHQL("from DeviceSite ds where ds.device.isDimesUse=?0 and ds.show=?1", new Object[] {true,isShow});
	}

	@Override
	public List<DeviceSite> queryAllDeviceSitesByDeviceId(Long deviceId) {
		return this.deviceSiteDao.findByHQL("from DeviceSite ds where ds.device.id=?0 "
				+ " and ds.id not in (select p.deviceSite.id from DeviceSiteParameterMapping p)", new Object[] {deviceId});
	}
	@Override
	public List<DeviceSite> queryDeviceSitesByClassesId(Long classesId) {
		return this.deviceSiteDao.queryDeviceSitesByClassesId(classesId);
	}

	@Override
	public Integer queryLostTimeCountByDeviceSiteId(Long deviceSiteId) {
		return deviceSiteDao.queryLostTimeCountByDeviceSiteId(deviceSiteId);
	}

	@Override
	public Integer queryPressLightCountByDeviceSiteId(Long deviceSiteId) {
		return deviceSiteDao.queryPressLightCountByDeviceSiteId(deviceSiteId);
	}

	@Override
	public Integer queryProcessDeviceSiteMappingCountByDeviceSiteId(Long deviceSiteId) {
		return deviceSiteDao.queryProcessDeviceSiteMappingCountByDeviceSiteId(deviceSiteId);
	}

	@Override
	public List<BigInteger> queryDeviceSiteIdsByProductionUnitId(Long productionUnitId) {
		return deviceSiteDao.queryDeviceSiteIdsByProductionUnitId(productionUnitId);
	}

	@Override
	public List<BigInteger> queryAllDeviceSiteIds() {
		return deviceSiteDao.queryAllDeviceSiteIds();
	}

	@Override
	public DeviceSite queryDeviceSiteByCordAndName(String deviceSiteCord, String deviceSiteName) {
		List<DeviceSite> dsList = deviceSiteDao.queryDeviceSiteByCordAndName(deviceSiteCord,deviceSiteName);
		if(!dsList.isEmpty()&&dsList.size()>0){
			return dsList.get(0);
		}
		return null;
	}

	@Override
	public List<BigInteger> queryBottleneckDeviceSiteIdsByProductionUnitId(Long productionUnitId) {
		return deviceSiteDao.queryBottleneckDeviceSiteIdsByProductionUnitId(productionUnitId);
	}

	@Override
	public List<BigInteger> queryBottleneckDeviceSiteIds() {
		return deviceSiteDao.queryBottleneckDeviceSiteIds();
	}

	@Override
	public Long queryCountOfBottleneckDeviceSite() {
		return deviceSiteDao.queryCountOfBottleneckDeviceSite();
	}

	@Override
	public List<DeviceSite> queryDeviceSiteByProcessId(Long processesId) {
		return deviceSiteDao.findByHQL("select pdm.deviceSite from ProcessDeviceSiteMapping pdm where pdm.processes.id=?0",new Object[]{processesId});
	}

	@Override
	public List<DeviceSite> queryDeviceSiteByInventoryCodeMappingDeviceSite(String InventoryCode) {
		return deviceSiteDao.findByHQL("select pdm.deviceSite from ProcessDeviceSiteMapping pdm left join InventoryProcessMapping ipm on ipm.process.id=pdm.processes.id where ipm.inventory.code=?0",new Object[]{InventoryCode});
	}
}
