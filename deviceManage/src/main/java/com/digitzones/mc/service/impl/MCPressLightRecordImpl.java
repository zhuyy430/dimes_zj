package com.digitzones.mc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.mc.dao.IMCPressLightRecordDao;
import com.digitzones.mc.service.IMCPressLightRecodeService;
import com.digitzones.model.PressLightRecord;
@Service
public class MCPressLightRecordImpl implements IMCPressLightRecodeService {

	@Autowired
	private IMCPressLightRecordDao mcPressLightRecordDao;

	@Override
	public List<PressLightRecord> queryPressLightRecordByBasicCode(String typeCode,String deviceSiteCode) {
		return mcPressLightRecordDao.queryPressLightRecordByBasicCode(typeCode,deviceSiteCode);
	}
}
