package com.digitzones.mc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.mc.dao.IMCLostTimeDao;
import com.digitzones.mc.service.IMCLostTimeService;
import com.digitzones.model.LostTimeRecord;

@Service
public class MCLostTimeServiceImpl implements IMCLostTimeService{

	@Autowired
	private IMCLostTimeDao iMCLostTimeDao;
	@Override
	public List<LostTimeRecord> findLostTimelist(String hql,List<Object> param) {
		return iMCLostTimeDao.findByHQL(hql, param.toArray());
	}
	@Override
	public Double getAllLostTimelist(String untreated, String notSure, String historicalRecords, Long deviceSiteid) {
		return iMCLostTimeDao.getAllLostTimelist(untreated, notSure, historicalRecords, deviceSiteid);
	}
	@Override
	public int getCountLostTimelist(String untreated, String notSure, String historicalRecords, Long deviceSiteid) {
		return iMCLostTimeDao.getCountLostTimelist(untreated, notSure, historicalRecords, deviceSiteid);
	}
}
