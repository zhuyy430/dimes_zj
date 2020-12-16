package com.digitzones.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IReportDao;
import com.digitzones.model.Pager;
import com.digitzones.service.IReportService;
@Service(" ReportService")
public class ReportServiceImpl implements IReportService {
	@Autowired
	private IReportDao reportDao;

	@Override
	public Pager<List<Object[]>> queryLostTimeCountReport(Map<String, String> params, int rows, int page) {
		return reportDao.queryLostTimeCountReport(params, rows, page);
	}

	@Override
	public Pager<List<Object[]>> queryOutputBatchCountReport(Map<String, String> params, int rows, int page) {
		return reportDao.queryOutputBatchCountReport(params, rows, page);
	}

	@Override
	public Pager<List<Object[]>> queryProductionUnitOutputCountReport(Map<String, String> params, int rows, int page) {
		return reportDao.queryProductionUnitOutputCountReport(params, rows, page);
	}
}
