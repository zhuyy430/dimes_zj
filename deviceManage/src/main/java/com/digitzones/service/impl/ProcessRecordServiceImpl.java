package com.digitzones.service.impl;

import com.digitzones.dao.IDeviceSiteDao;
import com.digitzones.dao.IProcessRecordDao;
import com.digitzones.dao.IWorkSheetDetailDao;
import com.digitzones.model.*;
import com.digitzones.service.ILostTimeRecordService;
import com.digitzones.service.IProcessRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProcessRecordServiceImpl implements IProcessRecordService {
	private IProcessRecordDao processRecordDao;
	private IWorkSheetDetailDao workSheetDetailDao;
	@Autowired
	private ILostTimeRecordService lostTimeRecordService;
	private IDeviceSiteDao deviceSiteDao;
	@Autowired
	public void setDeviceSiteDao(IDeviceSiteDao deviceSiteDao) {
		this.deviceSiteDao = deviceSiteDao;
	}

	@Autowired
	public void setWorkSheetDetailDao(IWorkSheetDetailDao workSheetDetailDao) {
		this.workSheetDetailDao = workSheetDetailDao;
	}

	@Autowired
	public void setProcessRecordDao(IProcessRecordDao processRecordDao) {
		this.processRecordDao = processRecordDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return processRecordDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(ProcessRecord obj) {
		processRecordDao.update(obj);
	}

	@Override
	public ProcessRecord queryByProperty(String name, String value) {
		return processRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(ProcessRecord obj) {
		Object[] values = {obj.getWorkSheetId(),obj.getProcessId(),obj.getDeviceSiteId()};
		//完工数和合格品数增1 
		List<WorkSheetDetail> details = workSheetDetailDao.findByHQL("from WorkSheetDetail wsd where wsd.workSheet.id=?0 and wsd.processId=?1 and wsd.deviceSiteId=?2", values);
		for(WorkSheetDetail d : details) {
			d.setQualifiedCount(d.getQualifiedCount()+1);
			d.setCompleteCount(d.getCompleteCount()+1);
			workSheetDetailDao.update(d);
		}
		return processRecordDao.save(obj);
	}

	@Override
	public ProcessRecord queryObjById(Serializable id) {
		return processRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		processRecordDao.deleteById(id);
	}

	@Override
	public Serializable addProcessRecord(ProcessRecord obj, User user, Map<String,Object> args) {
		//是否为班次开始计划任务添加的执行记录
		Boolean classBegin = (Boolean) args.get("classBegin");
		//根据工单id，工序id，站点id更新工单详情信息
		Object[] values = {obj.getWorkSheetId(),obj.getProcessId(),obj.getDeviceSiteId()};
		//查找上一条执行记录
		ProcessRecord pr = processRecordDao.queryLastProcessRecord(obj.getDeviceSiteId());
		if(pr != null) {
			Date lastRecordCollectionDate = pr.getCollectionDate();
			Date collectionDate = obj.getCollectionDate();
			//查询损时阀值(阈值)
			//int threshold = sysConfig.getThreshold();
			DeviceSite ds = deviceSiteDao.findSingleByProperty("code", pr.getDeviceSiteCode());
			Float threshold = ds.getDevice().getProductionUnit().getThreshold();
			//两个工件之间的生产时间间隔，即时节拍，单位秒
			long interval = (collectionDate.getTime()-lastRecordCollectionDate.getTime())/1000;
			obj.setRealBeat(interval);
			//两条记录间隔大于阈值
			if(interval>threshold) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//班次开始计划任务添加的执行记录
						if(classBegin!=null && classBegin) {
							List<LostTimeRecord> list = lostTimeRecordService.queryUnEndLastLostTimeRecords(obj.getDeviceSiteId());
							if(!CollectionUtils.isEmpty(list)) {
								for(LostTimeRecord lostTimeRecord : list) {
									lostTimeRecord.setEndTime(collectionDate);
									lostTimeRecord.setSumOfLostTime(((double) ((collectionDate.getTime() - lostTimeRecord.getBeginTime().getTime()) / 1000))/3600);
									lostTimeRecordService.updateObj(lostTimeRecord);
								}
							}
						}else {
							//查找当前设备站点下的最后一条损时，将损时结束时间更新为当前时间
							List<LostTimeRecord> lostTimeRecordList = (List<LostTimeRecord>) lostTimeRecordService.queryUnEndLastLostTimeRecords(obj.getDeviceSiteId());
							if(!CollectionUtils.isEmpty(lostTimeRecordList)) {
								for (LostTimeRecord lostTimeRecord : lostTimeRecordList) {
									if (lostTimeRecord != null && lostTimeRecord.getEndTime() == null) {
										lostTimeRecord.setEndTime(collectionDate);
										lostTimeRecord.setSumOfLostTime(((double) ((collectionDate.getTime() - lostTimeRecord.getBeginTime().getTime()) / 1000))/3600);
										lostTimeRecordService.updateObj(lostTimeRecord);
									}
								}
							}
						}
					}
				}).start();
				
			}else {
				long shortHalt = interval-(int)(obj.getStandardBeat());
				obj.setShortHalt(shortHalt<0?0:shortHalt);
			}
		}
		if(obj.getRealRecord()) {
			//完工数和合格数增1
			List<WorkSheetDetail> details = workSheetDetailDao.findByHQL("from WorkSheetDetail wsd where wsd.workSheet.id=?0 and wsd.processId=?1 and wsd.deviceSiteId=?2", values);
			for(WorkSheetDetail d : details) {
				d.setCompleteCount(d.getCompleteCount()+1);
				d.setQualifiedCount(d.getQualifiedCount()+1);
				workSheetDetailDao.update(d);
			}
		}
		Serializable id = processRecordDao.save(obj);
		return id;
	}

	@Override
	public Long queryCurrentDayCountByDeviceSiteId(Long deviceSiteId) {
		return processRecordDao.queryCurrentDayCountByDeviceSiteId(deviceSiteId);
	}

	@Override
	public List<Long[]> queryByDay(Long deviceSiteId, String status, Date now) {
		return processRecordDao.queryByDay(deviceSiteId, status, now);
	}
	@Override
	public Integer queryOutput4EmployeePerMonth(Date date, Long empId) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return processRecordDao.queryOutput4EmployeePerMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,empId);
	}

	@Override
	public Integer queryOutput4ProcessPerMonth(Date date, Long processId) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return processRecordDao.queryOutput4ProcessPerMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,processId);
	}

	@Override
	public int queryOutput4DeviceSitePerMonth(Date date, Long deviceSiteId) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return processRecordDao.queryOutput4DeviceSitePerMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,deviceSiteId);
	}

	@Override
	public Integer queryWorkSheetNGCountPerMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer result = processRecordDao.queryWorkSheetNGCountPerMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);
		return result==null?0:result;
	}

	@Override
	public Integer queryWorkSHeetNotNGCountPerMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer result = processRecordDao.queryWorkSHeetNotNGCountPerMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);
		return result==null?0:result;
	}

	@Override
	public Integer queryCountByClassesIdAndDay(Classes c, Date day, Long productionUnitId) {
		Integer result = processRecordDao.queryCountByClassesIdAndDay(c, day,productionUnitId);
		return result==null?0:result;
	}

	/*@Override
	public Integer queryWorkSheetNGCountPerClasses4ProductionUnit(Date date, Long classId, Long productionUnitId) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer result = processRecordDao.queryWorkSheetNGCountPerClasses4ProductionUnit(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,c.get(Calendar.DATE), classId, productionUnitId);
		return result==null?0:result;
	}*/

	@Override
	public Integer querySumCountPerClasses4ProductionUnit(Date date, Classes classes, Long productionUnitId) {
		return processRecordDao.querySumCountPerClasses4ProductionUnit(date, classes, productionUnitId);
	}
	/*	@Override
	public Integer querySumCountPerClasses4ProductionUnit(Date date, Long classId, Long productionUnitId) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer result = processRecordDao.querySumCountPerClasses4ProductionUnit(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,c.get(Calendar.DATE), classId, productionUnitId);
		return result==null?0:result;
	}
	 */
	@Override
	public Integer queryWorkSheetScrapCountPerMonth(Date date, Long ngTypeId) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer result = processRecordDao.queryWorkSheetScrapCountPerMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1,ngTypeId);
		return result==null?0:result;
	}

	@Override
	public Integer queryWorkSheetScrapCountPerMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer result = processRecordDao.queryWorkSheetScrapCountPerMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);
		return result==null?0:result;
	}

	@Override
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(Classes c, Long deviceSiteId, Date date) {
		return processRecordDao.queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(c, deviceSiteId,date);
	}
	@Override
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClassByClassesAndProductionUnit(Classes c, Long productionUnitId, Date date) {
		return processRecordDao.queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClassByClassesAndProductionUnit(c, productionUnitId,date);
	}

	@Override
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHaltFromBeginOfMonthUntilTheDate(Date date) {
		return processRecordDao.queryCountAndSumOfStandardBeatAndSumOfShortHaltFromBeginOfMonthUntilTheDate(date);
	}

	@Override
	public Integer queryShortHalt(Classes c, String deviceSiteCode) {
		Integer shortHalt = processRecordDao.queryShortHalt(c,deviceSiteCode);
		return shortHalt==null?0:shortHalt;
	}

	@Override
	public ProcessRecord queryLastProcessRecord(Long deviceSiteId) {
		return processRecordDao.queryLastProcessRecord(deviceSiteId);
	}

	@Override
	public Integer queryCountBetween(Date begin, Date end, Long deviceSiteId) {
		return processRecordDao.queryCountBetween(begin, end, deviceSiteId);
	}

	@Override
	public Object[] queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClassFromClassesBegin2now(Classes c,
                                                                                                     Long deviceSiteId) {
		return processRecordDao.queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClassFromClassesBegin2now(c, deviceSiteId);
	}

	@Override
	public Integer queryCountTheDate(Date date) {
		return processRecordDao.queryCountTheDate(date);
	}

	@Override
	public Integer queryCountTheMonth(Date date) {
		return processRecordDao.queryCountTheMonth(date);
	}

	@Override
	public int queryShortHalt4ProductionUnit(Classes c, Long productionUnitId) {
		return processRecordDao.queryShortHalt4ProductionUnit(c, productionUnitId);
	}

	@Override
	public List<ProcessRecord> queryAll() {
		return processRecordDao.findAll();
	}

	@Override
	public ProcessRecord queryByProductNumAndDeviceSiteCode(String productNum, String deviceSiteCode) {
		List<ProcessRecord> list = processRecordDao.findByHQL("from ProcessRecord pr where pr.productNum=?0 and pr.deviceSiteCode=?1", new Object[] {productNum,deviceSiteCode});
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Object[]> queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(Classes c, Date date) {
		return processRecordDao.queryCountAndSumOfStandardBeatAndSumOfShortHalt4CurrentClass(c,date);
	}

	@Override
	public List<Object[]> queryCountByClassesIdAndMonth(Long classesId, Date day, Long productionUnitId) {
		List<Object[]> result= processRecordDao.queryCountByClassesIdAndMonth(classesId,day,productionUnitId);
		return result;
	}

	@Override
	public Integer queryWorkSheetNGCountPerClasses4ProductionUnit(Date now, Classes classes,
                                                                  Long productionUnitId) {
		return processRecordDao.queryWorkSheetNGCountPerClasses4ProductionUnit(now,classes,productionUnitId);
	}

	@Override
	public List<Object[]> queryWorkSheetScrapCount4Year(Date date) {
		return processRecordDao.queryWorkSheetScrapCount4Year(date);
	}

	@Override
	public List<Object[]> queryCountTheYear(Date date) {
		return processRecordDao.queryCountTheYear(date);
	}

	@Override
	public List<Object[]> queryAllMonthOutput4EmployeePerYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return processRecordDao.queryAllMonthOutput4EmployeePerYear(c.get(Calendar.YEAR));
	}

	@Override
	public List<Object[]> queryAllMonthOutput4ProcessPerYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return processRecordDao.queryAllMonthOutput4ProcessPerYear(c.get(Calendar.YEAR));
	}

	@Override
	public List<Object[]> queryAllMonthOutput4DeviceSitePerYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return processRecordDao.queryAllMonthOutput4DeviceSitePerYear(c.get(Calendar.YEAR));
	}

	@Override
	public List<Object[]> queryAllDayWorkSheetNGCountPerByMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		List<Object[]> list = processRecordDao.queryAllDayWorkSheetNGCountPerByMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);
		return list;
	}

	@Override
	public List<Object[]> queryAllDayCountByTheMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return  processRecordDao.queryAllDayCountByTheMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1);
	}

	@Override
	public List<Object[]> queryCountAndSumOfStandardBeatAndSumOfShortHalt4RealTime(Classes c, Date date) {
		return processRecordDao.queryCountAndSumOfStandardBeatAndSumOfShortHalt4RealTime(c, date);
	}

	@Override
	public List<ProcessRecord> queryByOpcNoAndDeviceSiteCode(String opcNo, String deviceSiteCode) {
		List<ProcessRecord> list = processRecordDao.findByHQL("from ProcessRecord pr where pr.deviceSiteCode=?0 and pr.opcNo=?1",new Object[] {deviceSiteCode,opcNo});
		return list;
	}

	@Override
	public ProcessRecord queryLastProcessRecord(String deviceSiteCode) {
		String hql = "from ProcessRecord pr where pr.id = (select max(p.id) from ProcessRecord p where p.deviceSiteCode=?0)";
		List<ProcessRecord> list = processRecordDao.findByHQL(hql, new Object[] {deviceSiteCode});
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Object[]> queryBottleneckCountByClassesIdAndMonth(Long id, Date now, Long productionUnitId) {
		return processRecordDao.queryBottleneckCountByClassesIdAndMonth(id, now, productionUnitId);
	}

	@Override
	public long queryBottleneckCountByClassesIdAndDay(Classes c, Date d, Long productionUnitId) {
		return processRecordDao.queryBottleneckCountByClassesIdAndDay(c,d,productionUnitId);
	}

	@Override
	public int queryNGCount(String batchNumber, String stoveNumber, String deviceSiteCode) {
		return processRecordDao.queryNGCount(batchNumber,stoveNumber,deviceSiteCode);
	}

	@Override
	public List<Object[]> queryCount(String batchNumber, String stoveNumber, String deviceSiteCode, String ng) {
		return processRecordDao.queryCount(batchNumber,stoveNumber,deviceSiteCode,ng);
	}

	@Override
	public int queryReverseCount(String serialNo, String batchNumber, String deviceSiteCode) {
		return processRecordDao.queryReverseCount(serialNo,batchNumber,deviceSiteCode);
	}

	@Override
	public int queryParameterCount(String parameterCode, String worksheetNo, String batchNum, String minValue, String maxValue) {
		return processRecordDao.queryParameterCount(parameterCode, worksheetNo,batchNum, minValue, maxValue);
	}

	@Override
	public List<Object[]> queryDeviceSiteNGCount(String batchNumber, String stoveNumber, String deviceSiteCode) {
		return processRecordDao.queryDeviceSiteNGCount(batchNumber,stoveNumber,deviceSiteCode);
	}

	@Override
	public void deleteByOpcNo(String opcNo) {
		processRecordDao.deleteByOpcNo(opcNo);
	}

	/**
	 * 根据日期，班次，设备站点分组查找产量
	 * @param begin 开始时间
	 * @param end  结束时间
	 * @return
	 */
	@Override
	public List<Object[]> queryOutput4DeviceSitePerDay(String begin, String end, List<String> codeList) {
		return processRecordDao.queryOutput4DeviceSitePerDay(begin,end,codeList);
	}
}
