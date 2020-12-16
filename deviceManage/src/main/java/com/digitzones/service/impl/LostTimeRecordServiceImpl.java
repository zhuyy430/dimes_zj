package com.digitzones.service.impl;

import com.digitzones.dao.ILostTimeRecordDao;
import com.digitzones.model.Classes;
import com.digitzones.model.LostTimeRecord;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IDeviceSiteService;
import com.digitzones.service.ILostTimeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
@Service("lostTimeRecordService")
public class LostTimeRecordServiceImpl implements ILostTimeRecordService {
	private ILostTimeRecordDao lostTimeRecordDao;
	@Autowired
	private IDeviceSiteService deviceSiteService;
	@Autowired
	public void setLostTimeRecordDao(ILostTimeRecordDao lostTimeRecordDao) {
		this.lostTimeRecordDao = lostTimeRecordDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return lostTimeRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(LostTimeRecord obj) {
		lostTimeRecordDao.update(obj);
	}

	@Override
	public LostTimeRecord queryByProperty(String name, String value) {
		return lostTimeRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(LostTimeRecord obj) {
		return lostTimeRecordDao.save(obj);
	}

	@Override
	public LostTimeRecord queryObjById(Serializable id) {
		return lostTimeRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		lostTimeRecordDao.deleteById(id);
	}

	@Override
	public List<Object[]> queryLostTimeRecordByYearAndMonth(Integer year,Integer month) {
		return this.lostTimeRecordDao.queryLostTimeRecordByYearAndMonth(year, month);
	}

	@Override
	public List<Object[]> queryLostTimeRecordByProductionIdsAndYearAndMonth(Integer year,Integer month,Integer day,String productionUnitIds) {
		return this.lostTimeRecordDao.queryLostTimeRecordByProductionIdsAndYearAndMonth(year, month,day,productionUnitIds);
	}

	@Override
	public Double queryHoursOfLostTimeRecordByYearAndMonth(Integer year, Integer month) {
		return this.lostTimeRecordDao.queryHoursOfLostTimeRecordByYearAndMonth(year, month);
	}

	@Override
	public Double queryHoursOfPlanHaltByYearAndMonth(Integer year, Integer month) {
		return lostTimeRecordDao.queryHoursOfPlanHaltByYearAndMonth(year, month);
	}

	@Override
	public Double queryLostTime(Classes c,Long deviceSiteId,Date date) {
		Double result = lostTimeRecordDao.queryLostTime(c,deviceSiteId,date);
		return result==null?0:result;
	}
	@Override
	public Double queryLostTimeByClassesAndProductionUnit(Classes c,Long productionUnitId,Date date) {
		Double result = lostTimeRecordDao.queryLostTimeByClassesAndProductionUnit(c,productionUnitId,date);
		return result==null?0:result;
	}

	@Override
	public Double queryPlanHaltTime(Classes c, Long deviceSiteId,Date date) {
		Double result = lostTimeRecordDao.queryPlanHaltTime(c, deviceSiteId,date);
		return result ==null?0:result;
	}
	@Override
	public Double queryPlanHaltTimeByClassesAndProductionUnit(Classes c, Long productionUnitId,Date date) {
		Double result = lostTimeRecordDao.queryPlanHaltTimeByClassesAndProductionUnit(c, productionUnitId,date);
		return result ==null?0:result;
	}

	@Override
	public Double queryLostTime4PerDay(Classes c, Long deviceSiteId, Date date) {
		Double result = lostTimeRecordDao.queryLostTime4PerDay(c, deviceSiteId, date);
		return result ==null?0:result;
	}

	@Override
	public Integer queryLostTime4RealTime(Date date) {
		return lostTimeRecordDao.queryLostTime4RealTime(date);
	}

	@Override
	public void confirm(LostTimeRecord lostTimeRecord,User user,Map<String,Object> args) {
		if(user!=null) {
			lostTimeRecord.setConfirmUserId(user.getId());
			lostTimeRecord.setConfirmUserName(user.getUsername());
		}
		this.updateObj(lostTimeRecord);
	}

	@Override
	public Serializable addLostTimeRecord(LostTimeRecord lostTimeRecord, User user,Map<String,Object> args) {
		if(user!=null) {
			lostTimeRecord.setRecordUserId(user.getId());
			lostTimeRecord.setRecordUserName(user.getUsername());
		}else {
			lostTimeRecord.setRecordUserName("系统");
		}
		return this.addObj(lostTimeRecord);
	}

	@Override
	public void deleteLostTimeRecord(LostTimeRecord lostTimeRecord) {
		lostTimeRecord.setDeleted(true);
		this.updateObj(lostTimeRecord);
	}

	@Override
	public Integer queryLostTimeFromBeginOfMonthUntilTheDate(Date date, Boolean halt) {
		return lostTimeRecordDao.queryLostTimeFromBeginOfMonthUntilTheDate(date, halt);
	}

	@Override
	public Long queryLostTime4TheDate(Date date) {
		Long  count = lostTimeRecordDao.queryLostTime4TheDate(date);
		return count==null?0:count;
	}

	@Override
	public List<Object[]> queryLostTimePerLosttimeType(Classes c,String deviceSiteCode) {
		Long deviceSiteId = deviceSiteService.queryByProperty("code", deviceSiteCode).getId();
		return lostTimeRecordDao.queryLostTimePerLosttimeType(c,deviceSiteId);
	}

	@Override
	public Double queryLostTime4Classes(Classes c,String deviceSiteCode) {
		Long deviceSiteId = deviceSiteService.queryByProperty("code", deviceSiteCode).getId();
		return lostTimeRecordDao.queryLostTime4Classes(c,deviceSiteId);
	}

	@Override
	public long queryUnhandledLostTimeRecordCount4Classes(Classes c) {
		return lostTimeRecordDao.queryUnhandledLostTimeRecordCount4Classes(c);
	}

	@Override
	public LostTimeRecord queryUnEndLastLostTimeRecord(Long deviceSiteId) {
		return lostTimeRecordDao.queryUnEndLastLostTimeRecord(deviceSiteId);
	}

	@Override
	public List<LostTimeRecord> queryLostTime4RealTime() {
		return lostTimeRecordDao.queryLostTime4RealTime();
	}

	@Override
	public List<Object[]> queryOneMonthLostTime(Date date) {
		return lostTimeRecordDao.queryOneMonthLostTime(date);
	}

	@Override
	public List<Object[]> queryOneMonthPlanHaltTime(Date date) {
		return lostTimeRecordDao.queryOneMonthPlanHaltTime(date);
	}

	@Override
	public Double queryLostTime4ProductionUnit(Classes c, Long productionUnitId) {
		return lostTimeRecordDao.queryLostTime4ProductionUnit(c, productionUnitId);
	}

	@Override
	public List<Object[]> queryLostTimePerLosttimeType4ProductionUnit(Classes c, Long productionUnitId) {
		return lostTimeRecordDao.queryLostTimePerLosttimeType4ProductionUnit(c, productionUnitId);
	}

	@Override
	public Pager<List<Object[]>> queryLostTimeCountReport(Map<String, String> params, int rows, int page) {
		return lostTimeRecordDao.queryLostTimeCountReport(params, rows, page);
	}

	@Override
	public List<Object[]> queryLostTimeDetail(String typeName, int month) {
		return lostTimeRecordDao.queryLostTimeDetail(typeName, month);
	}

	@Override
	public Double queryLostTime4PerDay(Classes c, Date date) {
		return lostTimeRecordDao.queryLostTime4PerDay(c, date);
	}

	@Override
	public List<Object[]> queryLostTimeAndPlanHaltTime(Classes c, Date now) {
		return lostTimeRecordDao.queryLostTimeAndPlanHaltTime(c,now);
	}
	@Override
	public List<Object[]> queryLostTime4PerMonth(Classes c, Date date) {
		return lostTimeRecordDao.queryLostTime4PerMonth(c, date);
	}
	@Override
	public Integer queryLostTimeForPerDay(Classes c, Date date, Long productionUnitId) {
		return lostTimeRecordDao.queryLostTimeForPerDay(c, date,productionUnitId);
	}
	@Override
	public List<Object[]> queryAllMonthLostTimeRecordFor1Year(Integer year) {
		return this.lostTimeRecordDao.queryAllMonthLostTimeRecordFor1Year(year);
	}
	@Override
	public List<?> queryHoursOfLostTimeRecord(Integer year) {
		return this.lostTimeRecordDao.queryHoursOfLostTimeRecord(year);
	}

	@Override
	public List<Object[]> queryLostTime(Classes c, Date date) {
		return lostTimeRecordDao.queryLostTime(c, date);
	}

	@Override
	public List<Object[]> queryPlanHaltTime(Classes c, Date date) {
		return lostTimeRecordDao.queryPlanHaltTime(c, date);
	}

	@Override
	public List<LostTimeRecord> queryUnEndLastLostTimeRecords(Long deviceSiteId) {
		return lostTimeRecordDao.findByHQL("from LostTimeRecord record where record.deviceSite.id=?0 and record.endTime is null and record.deleted=?1",
				new Object[] {deviceSiteId,false});
	}
}
