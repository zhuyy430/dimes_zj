package com.digitzones.devmgr.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.digitzones.constants.Constant;
import com.digitzones.devmgr.dao.ICheckingPlanRecordDao;
import com.digitzones.devmgr.model.CheckingPlanRecord;
import com.digitzones.devmgr.model.CheckingPlanRecordItem;
import com.digitzones.devmgr.model.DeviceProjectRecord;
import com.digitzones.devmgr.service.ICheckingPlanRecordItemService;
import com.digitzones.devmgr.service.ICheckingPlanRecordService;
import com.digitzones.devmgr.service.IDeviceProjectRecordService;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Service
public class CheckingPlanRecordServiceImpl implements ICheckingPlanRecordService {
	@Autowired
	private ICheckingPlanRecordDao checkingPlanRecordDao;
	@Autowired
	private IDeviceProjectRecordService deviceProjectRecordService;
	@Autowired
	private ICheckingPlanRecordItemService checkingPlanRecordItemService;
	@Autowired
	private IUserService userService;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return checkingPlanRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(CheckingPlanRecord obj) {
		checkingPlanRecordDao.update(obj);
	}

	@Override
	public CheckingPlanRecord queryByProperty(String name, String value) {
		return checkingPlanRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(CheckingPlanRecord obj) {
		return checkingPlanRecordDao.save(obj);
	}

	@Override
	public CheckingPlanRecord queryObjById(Serializable id) {
		return checkingPlanRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		checkingPlanRecordDao.deleteById(id);
	}
	@Override
	public void deleteChekingPlanRecords(String[] idArray) {
		for(String id : idArray) {
			deleteObj(Long.valueOf(id));
		}
	}

	@Override
	public void updateStatus2Uncomplete() {
		checkingPlanRecordDao.updateStatus2Uncomplete();
	}

	@Override
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCodeAndMonth(String deviceCodce, int year, int month) {
		return checkingPlanRecordDao.queryCheckingPlanRecordByDeviceCodeAndMonth(deviceCodce, year, month);
	}

	@Override
	public void updateCheckingPlanRecord(CheckingPlanRecord record, String itemIds, String results,String notes,String checkValue) {
		String status =record.getStatus();
		if(null!=record.getCheckedDate()){
			record.setStatus(Constant.Status.CHECKINGPLAN_COMPLETE);
		}
		checkingPlanRecordDao.update(record);
		//添加点检项目
		if(!StringUtils.isEmpty(itemIds)) {
			String[] ids = itemIds.split(",");
			String[] resultArray = results.split(",");
			String[] checkValueArray = checkValue.split(",");
			String[] noteArray =notes.split("@");
			//查找点检项目
			List<CheckingPlanRecordItem> list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(record.getId());
			if(CollectionUtils.isEmpty(list)) {
				for(int i = 0;i<ids.length;i++) { 
					DeviceProjectRecord deviceProjectRecord = deviceProjectRecordService.queryObjById(Long.valueOf(ids[i]));
					CheckingPlanRecordItem item = new CheckingPlanRecordItem();
					item.setResult(resultArray[i]);
					item.setCheckingPlanRecord(record);
					item.setCode(deviceProjectRecord.getCode());
					item.setName(deviceProjectRecord.getName());
					item.setFrequency(deviceProjectRecord.getFrequency());
					item.setMethod(deviceProjectRecord.getMethod());
					item.setStandard(deviceProjectRecord.getStandard());
					item.setNote(noteArray[i]);
					item.setUpperLimit(deviceProjectRecord.getUpperLimit());
					item.setLowerLimit(deviceProjectRecord.getLowerLimit());
					item.setCheckValue(checkValueArray[i]);
					checkingPlanRecordItemService.addObj(item);
				}
			}else {
				for(int i = 0;i<ids.length;i++) { 
					for(CheckingPlanRecordItem item:list) {
						if(null!=record.getCheckedDate()&&!status.equals(Constant.Status.CHECKINGPLAN_COMPLETE)&&item.getId()==Long.valueOf(ids[i])){
							item.setCheckValue(checkValueArray[i]);
						}
						if(item.getId()==Long.valueOf(ids[i]) && !resultArray[i].equals(item.getResult())) {
							item.setResult(resultArray[i]);
						}
						if(item.getId()==Long.valueOf(ids[i]) && !noteArray[i].equals(item.getNote()) ) {
							item.setNote(noteArray[i]);
						}
						checkingPlanRecordItemService.updateObj(item);
					}
				}
			}
		}
	}
	@Override
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCode(String deviceCode) {
		return checkingPlanRecordDao.findByHQL("from CheckingPlanRecord r where r.deviceCode=?0 order by r.checkingDate desc",
				new Object[] {deviceCode});
	}
	@Override
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCodeAndDateTime(String deviceCode,int year,int month,int day,String classCode) {
		String hql="from CheckingPlanRecord c where c.deviceCode=?0  and year(checkingDate)=?1 and month(checkingDate)=?2 and day(checkingDate)=?3";
		return checkingPlanRecordDao.findByHQL(hql, new Object[] {deviceCode,year,month,day});
	}
	@Override
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCodeAndDate(String deviceCode,int year,int month,int day) {
		String hql="from CheckingPlanRecord c where c.deviceCode=?0 and year(c.checkingDate)=?1 and month(c.checkingDate)=?2 and day(c.checkingDate)<=?3 order by c.checkingDate desc";
		return checkingPlanRecordDao.findByHQL(hql, new Object[] {deviceCode,year,month,day});
	}

	@Override
	public List<CheckingPlanRecord> queryCheckingPlanRecordByProductionUnitIdAndDate(String productionUnitId,int year,int month,int day) {
		String hql="from CheckingPlanRecord c where c.deviceCode in "
				+ " (select d.code from Device d inner join d.productionUnit p where p.id=?0 and d.isDeviceManageUse=?1) and year(c.checkingDate)=?2 and month(c.checkingDate)=?3 and day(c.checkingDate)<=?4 order by c.checkingDate desc";
		return checkingPlanRecordDao.findByHQL(hql, new Object[] {productionUnitId==null?null:Long.parseLong(productionUnitId),true,year,month,day});
	}
	@Override
	public void updateCheckingPlanRecordByOne(String result,Long id,String username,String note,String checkValue) {
		CheckingPlanRecordItem item= checkingPlanRecordItemService.queryObjById(id);
		User user=userService.queryByProperty("username", username);
		item.setResult(result);
		item.setNote(note);
		item.setCheckValue(checkValue);
		checkingPlanRecordItemService.updateObj(item);
		/*CheckingPlanRecordItem item= checkingPlanRecordItemService.queryObjById(id);
		User user=userService.queryByProperty("username", username);
		item.setResult(result);
		item.setNote(note);
		checkingPlanRecordItemService.updateObj(item);
		List<CheckingPlanRecordItem> list = checkingPlanRecordItemService.queryByCheckingPlanRecordId(item.getCheckingPlanRecord().getId());
		int count=checkingPlanRecordItemService.queryResultCountByCheckingPlanRecordId(item.getCheckingPlanRecord().getId());
		if(list.size()==count) {
			CheckingPlanRecord record=checkingPlanRecordDao.findById(item.getCheckingPlanRecord().getId());
			record.setStatus("已完成");
			if(user.getEmployee().getCode().equals("")||user.getEmployee().getCode()==null||user.getEmployee()==null) {
				record.setEmployeeCode(user.getUsername());
				record.setEmployeeName(user.getUsername());
			}else {
				record.setEmployeeCode(user.getEmployee().getCode());
				record.setEmployeeName(user.getEmployee().getName());
			}
			
			record.setCheckedDate(new Date());
			checkingPlanRecordDao.update(record);
		}*/
		
	}

	@Override
	public List<CheckingPlanRecord> queryCheckingPlanRecordByDeviceCodeAndSelectTime(String deviceCode, int year,int month, int day, String classCode,String status) {
		String hql="from CheckingPlanRecord c where c.deviceCode=?0 and year(checkingDate)=?1 and month(checkingDate)=?2 and day(checkingDate)=?3 ";
		List<Object> list=new ArrayList<Object>();
		list.add(deviceCode);
		list.add(year);
		list.add(month);
		list.add(day);
		int i=3;
		if(!classCode.equals("")&&classCode!=null) {
			i=i+1;
			hql+=" and c.classCode=?"+i;
			list.add(classCode);
		}
		if(!status.equals("")&&status!=null) {
			i=i+1;
			hql+=" and c.status=?"+i;
			list.add(status);
		}
		
		Object[] obj =  list.toArray(new Object[1]);   
		return checkingPlanRecordDao.findByHQL(hql, obj);
	}

	@Override
	public List<Object[]> queryNotSpotcheckDeviceBytime(String startTime,String endTime) {
		return checkingPlanRecordDao.queryNotSpotcheckDeviceBytime(startTime,endTime);
	}

	@Override
	public List<Object[]> queryNotSpotcheckRecordBytime(String startTime, String endTime) {
		return checkingPlanRecordDao.queryNotSpotcheckRecordBytime(startTime,endTime);
	}

	@Override
	public List<CheckingPlanRecord> queryAllCheckingPlanRecords() {
		return checkingPlanRecordDao.findByHQL("from CheckingPlanRecord r order by r.checkingDate desc", new Object[] {});
	}

	@Override
	public List<CheckingPlanRecord> queryCheckingPlanRecordByProductionUnitId(Long productionUnitId) {
		String hql = "from CheckingPlanRecord r where r.deviceCode in "
				+ " (select d.code from Device d inner join d.productionUnit p where p.id=?0 and d.isDeviceManageUse=?1)"
				+ " order by r.checkingDate desc";
		return checkingPlanRecordDao.findByHQL(hql, new Object[] {productionUnitId,true});
	}

	@Override
	public List<String[]> queryStatisticsData(String from, String to, String cycle) {
		return checkingPlanRecordDao.queryStatisticsData(from,to,cycle);
	}

	@Override
	public List<String[]> queryOverviewData(String from, String to) {
		return checkingPlanRecordDao.queryOverviewData(from,to);
	}
}
