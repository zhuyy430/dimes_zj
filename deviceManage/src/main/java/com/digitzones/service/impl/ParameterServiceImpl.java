package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IParameterDao;
import com.digitzones.model.Pager;
import com.digitzones.model.Parameters;
import com.digitzones.service.IParameterService;
@Service
public class ParameterServiceImpl implements IParameterService {
	private IParameterDao parameterDao;
	@Autowired
	public void setParameterDao(IParameterDao parameterDao) {
		this.parameterDao = parameterDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return parameterDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(Parameters obj) {
		parameterDao.update(obj);
	}

	@Override
	public Parameters queryByProperty(String name, String value) {
		return parameterDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Parameters obj) {
		return parameterDao.save(obj);
	}

	@Override
	public Parameters queryObjById(Serializable id) {
		return parameterDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		parameterDao.deleteById(id);
	}

	@Override
	public List<Parameters> queryAllParameters() {
		return parameterDao.findAll();
	}

	@Override
	public List<Parameters> queryOtherParametersByDeviceSiteId(Long deviceSiteId) {
		String hql = "from Parameters p where p.id not in ("
				+ "select dspm.parameter.id from DeviceSiteParameterMapping dspm where dspm.deviceSite.id=?0)" 
				+ " and p.baseCode=?1";
		return this.parameterDao.findByHQL(hql, new Object[] {deviceSiteId,Constant.ParameterType.DEVICE});
	}

	@Override
	public List<Parameters> queryOtherParametersByDeviceId(Long deviceId) {
		String hql = "from Parameters p where p.id not in ("
				+ "select dspm.parameter.id from DeviceSiteParameterMapping dspm where dspm.deviceSite.device.id=?0)" 
				+ " and p.baseCode=?1";
		return this.parameterDao.findByHQL(hql, new Object[] {deviceId,Constant.ParameterType.DEVICE});
	}

}
