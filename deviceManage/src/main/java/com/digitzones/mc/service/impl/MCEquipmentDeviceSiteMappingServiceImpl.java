package com.digitzones.mc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.mc.dao.IMCEquipmentDeviceSiteMappingDao;
import com.digitzones.mc.service.IMCEquipmentDeviceSiteMappingService;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.EquipmentDeviceSiteMapping;

@Service
public class MCEquipmentDeviceSiteMappingServiceImpl implements IMCEquipmentDeviceSiteMappingService{
	@Autowired
	private IMCEquipmentDeviceSiteMappingDao IMCEDao;
	@Override
	public List<EquipmentDeviceSiteMapping> findEquipmentDeviceSiteMappinglist(String deviceSiteCode,String deviceSiteName) {
		// TODO Auto-generated method stub
		String hql="from EquipmentDeviceSiteMapping edsm where edsm.deviceSite.code=?0 and edsm.unbind=?1 and edsm.equipment.baseCode=?2";
		/*if(deviceSiteName!=null&&!deviceSiteName.trim().equals("")) {
			hql+="and edsm.equipment.name like '%%"+deviceSiteName+"%%'";
		}*/
		return IMCEDao.findEquipmentDeviceSiteMappinglist(deviceSiteCode, hql);
	}
	@Override
	public Long findDeviceSite(String deviceSiteCode) {
		// TODO Auto-generated method stub
		List<DeviceSite> ds = IMCEDao.findDeviceSite(deviceSiteCode);
		if(!ds.isEmpty()&&ds.size()>0) {
			return ds.get(0).getId();
		}
		return null;
	}

}
