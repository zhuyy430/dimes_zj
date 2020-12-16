package com.digitzones.mc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.mc.dao.IMCDeviceSiteDao;
import com.digitzones.mc.model.MCDeviceSite;
import com.digitzones.mc.service.IMCDeviceSiteService;
@Service
public class MCDeviceSiteServiceImpl implements IMCDeviceSiteService {
	@Autowired
	private IMCDeviceSiteDao mcDeviceSiteDao;
	@Override
	public List<MCDeviceSite> getAllMCDeviceSite(String clientIp) {
		return mcDeviceSiteDao.findByHQL("from MCDeviceSite where clientIp=?0", new Object[] {clientIp});
	}
	@Override
	public List<MCDeviceSite> queryAvailableDeviceSites(String clientIp) {
		return mcDeviceSiteDao.queryAvailableDeviceSites(clientIp);
	}
	@Override
	public List<MCDeviceSite> queryAvailableDeviceSitesByCondition(String clientIp,String deviceName,String deviceCode,String productionUnitCode) {
		return mcDeviceSiteDao.queryAvailableDeviceSitesByCondition(clientIp,deviceName,deviceCode,productionUnitCode);
	}
	@Override
	public void addMCDeviceSites(List<MCDeviceSite> mcDeviceSites) {
		for(int i = 0;i<mcDeviceSites.size();i++) {
			mcDeviceSiteDao.save(mcDeviceSites.get(i));
		}
	}
	@Override
	public void deleteMCDeviceSite(List<Long> ids) {
		for(Long id : ids) {
			mcDeviceSiteDao.deleteById(id);
		}
	}
	@Override
	public MCDeviceSite queryByDeviceSiteCode(String deviceSiteCode) {
		return mcDeviceSiteDao.findSingleByProperty("deviceSiteCode", deviceSiteCode);
	}
}
