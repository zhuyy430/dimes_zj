package com.digitzones.mc.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IWorkSheetDetailDao;
import com.digitzones.mc.dao.IMCWorkSheetDao;
import com.digitzones.mc.model.MCWorkSheet;
import com.digitzones.mc.service.IMCWorkSheetService;
import com.digitzones.model.WorkSheet;
import com.digitzones.model.WorkSheetDetail;
import com.digitzones.service.IWorkSheetDetailService;

@Service
public class MCWorkSheetServiceImpl implements IMCWorkSheetService{
	@Autowired
	private IMCWorkSheetDao mcWorkSheetDao;
	@Autowired
	private IWorkSheetDetailDao workSheetDetailDao;
	@Autowired
	private IWorkSheetDetailService workSheetDetailService;
	@Override
	public List<WorkSheetDetail> findWorkSheetDetailList(String deviceSiteCode) {
		return null;
	}

	@Override
	public List<MCWorkSheet> queryNotCompleteWorkSheetByDeviceCode(String deviceSiteCode) {
		return mcWorkSheetDao.queryNotCompleteWorkSheetByDeviceCode(deviceSiteCode);
	}

	@Override
	public List<MCWorkSheet> queryProcessingWorkSheetsByDeviceCode(String deviceSiteCode) {
		return mcWorkSheetDao.queryProcessingWorkSheetsByDeviceCode(deviceSiteCode);
	}

	@Override
	public List<MCWorkSheet> queryProcessingWorkSheets(String clientIp) {
		return mcWorkSheetDao.queryProcessingWorkSheets(clientIp);
	}

	@Override
	public void updateWorkSheetDetail(Map<Long, Integer> params) {
		Set<Entry<Long, Integer>> entries = params.entrySet();
		for(Entry<Long,Integer> entry : entries) {
			WorkSheetDetail detail = workSheetDetailDao.findById(entry.getKey());
			detail.setReportCount(entry.getValue());
			detail.setQualifiedCount(entry.getValue()-detail.getUnqualifiedCount());
			workSheetDetailDao.update(detail);
			//单个工单详情完工
			workSheetDetailService.complete(detail.getId());
		}
	}
	@Override
	public WorkSheet queryWorkSheetIdByNo(String No) {
		List<WorkSheet> list=mcWorkSheetDao.queryWorkSheetByNo(No);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
}
