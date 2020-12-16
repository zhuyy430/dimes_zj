package com.digitzones.mc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.mc.dao.IMCPressLightDao;
import com.digitzones.mc.service.IMCPressLightService;
import com.digitzones.model.PressLight;
import com.digitzones.model.PressLightType;
@Service
public class MCPressLightImpl implements IMCPressLightService {
	@Autowired
	IMCPressLightDao mcPressLightDao;
	@Override
	public List<PressLightType> queryPressLightRecordByBasicCode(String pcode) {
		return mcPressLightDao.queryAllTypeByParentCode(pcode);
	}
	@Override
	public List<PressLight> queryAllPressLight() {
		return mcPressLightDao.queryAllPressLight();
	}

}
