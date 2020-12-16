package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IDeviceSiteDao;
import com.digitzones.dao.IDeviceSiteParameterMappingDao;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.DeviceSiteParameterMapping;
import com.digitzones.model.Pager;
import com.digitzones.service.IDeviceSiteParameterMappingService;
@Service
public class DeviceSiteParameterMappingServiceImpl implements IDeviceSiteParameterMappingService {
	private IDeviceSiteParameterMappingDao deviceSiteParameterMappingDao;
	@Autowired
	private IDeviceSiteDao deviceSiteDao;
	@Autowired
	public void setDeviceSiteParameterMappingDao(IDeviceSiteParameterMappingDao deviceSiteParameterMappingDao) {
		this.deviceSiteParameterMappingDao = deviceSiteParameterMappingDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceSiteParameterMappingDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceSiteParameterMapping obj) {
		deviceSiteParameterMappingDao.update(obj);
	}

	@Override
	public DeviceSiteParameterMapping queryByProperty(String name, String value) {
		return deviceSiteParameterMappingDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceSiteParameterMapping obj) {
		return deviceSiteParameterMappingDao.save(obj);
	}

	@Override
	public DeviceSiteParameterMapping queryObjById(Serializable id) {
		return deviceSiteParameterMappingDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceSiteParameterMappingDao.deleteById(id);
	}

	@Override
	public List<DeviceSiteParameterMapping> queryByDeviceSiteId(Long deviceSiteId) {
		return deviceSiteParameterMappingDao.findByHQL("from DeviceSiteParameterMapping dspm where dspm.deviceSite.id=?0", new Object[] {deviceSiteId});
	}

	@Override
	public List<DeviceSiteParameterMapping> queryByDeviceSiteIds(List<Long> deviceSiteIdList) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeviceSiteParameterMapping.class);
		criteria.add(Restrictions.in("deviceSite.id", deviceSiteIdList));
		return deviceSiteParameterMappingDao.findByCriteria(criteria);
	}

	@Override
	public void addDeviceSiteParameterMappings(Long deviceId, DeviceSiteParameterMapping deviceSiteParameter) {
		List<DeviceSite> deviceSiteList = deviceSiteDao.findByHQL("from DeviceSite ds where ds.device.id=?0", new Object[] {deviceId});
		for(DeviceSite ds : deviceSiteList) {
			DeviceSiteParameterMapping mapping = new DeviceSiteParameterMapping();
			BeanUtils.copyProperties(deviceSiteParameter, mapping);
			mapping.setDeviceSite(ds);
			mapping.setUpdateDate(new Date());
			deviceSiteParameterMappingDao.save(mapping);
		}
	}
}
