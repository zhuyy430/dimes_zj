package com.digitzones.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.ITraceDao;
import com.digitzones.model.TraceParameter;
import com.digitzones.model.TracePositive;
import com.digitzones.model.TraceReverse;
import com.digitzones.service.ITraceService;
@Service
public class TraceServiceImpl implements ITraceService {

	@Autowired
	ITraceDao traceDao;
	@Override
	public List<TraceReverse> queryTracereverse(String serialNo, String batchNumber, String deviceSiteCode,
			Integer start, Integer end) {
		return traceDao.findTracereverse(serialNo, batchNumber, deviceSiteCode, start, end);
	}

	@Override
	public int getTracereverseCount(String serialNo, String batchNumber, String deviceSiteCode) {
		return traceDao.findTracereverseCount(serialNo, batchNumber, deviceSiteCode);
	}

	@Override
	public List<TracePositive> queryTracepositive(String batchNumber, String stoveNumber,
			String deviceSiteCode,String ng, Integer start, Integer end) {
		return traceDao.findTracepositive(batchNumber, stoveNumber, deviceSiteCode,ng, start, end);
	}

	@Override
	public int getTracepositiveCount(String batchNumber, String stoveNumber, String deviceSiteCode,String ng) {
		return traceDao.findTracepositiveCount(batchNumber, stoveNumber, deviceSiteCode,ng);
	}

	@Override
	public List<TraceParameter> queryTraceparameter(String parameterCode,String worksheetNo,String minValue,String maxValue, Integer start, Integer end) {
		return traceDao.findTraceparameter(parameterCode, worksheetNo, minValue, maxValue, start, end);
	}

	@Override
	public int getTraceparameterCount(String parameterCode,String worksheetNo,String batchNum,String minValue,String maxValue) {
		return traceDao.findTraceparameterCount(parameterCode, worksheetNo,batchNum, minValue, maxValue);
	}

	@Override
	public List<TraceReverse> queryTracereverse(String serialNo, String batchNumber, String deviceSiteCode) {
		return traceDao.findTracereverse(serialNo, batchNumber, deviceSiteCode);
	}

	@Override
	public List<TracePositive> queryTracepositive(String batchNumber, String stoveNumber, String deviceSiteCode,String ng) {
		return traceDao.findTracepositive(batchNumber, stoveNumber, deviceSiteCode,ng);
	}

	@Override
	public List<TraceParameter> queryTraceparameter(String parameterCode, String worksheetNo, String batchNum,
			String minValue, String maxValue) {
		return traceDao.findTraceparameter(parameterCode, worksheetNo, batchNum, minValue, maxValue);
	}
}
