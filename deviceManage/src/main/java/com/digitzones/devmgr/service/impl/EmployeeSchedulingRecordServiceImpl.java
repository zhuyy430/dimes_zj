package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.devmgr.dao.IEmployeeSchedulingRecordDao;
import com.digitzones.devmgr.dto.EmployeeSchedulingDto;
import com.digitzones.devmgr.model.EmployeeSchedulingRecord;
import com.digitzones.devmgr.service.IEmployeeSchedulingRecordService;
import com.digitzones.model.Pager;
@Service
public class EmployeeSchedulingRecordServiceImpl implements IEmployeeSchedulingRecordService {
	@Autowired
	private IEmployeeSchedulingRecordDao employeeSchedulingRecordDao ;
	@Override
	public Pager<EmployeeSchedulingRecord> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return employeeSchedulingRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(EmployeeSchedulingRecord obj) {
		employeeSchedulingRecordDao.update(obj);
	}

	@Override
	public EmployeeSchedulingRecord queryByProperty(String name, String value) {
		return employeeSchedulingRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(EmployeeSchedulingRecord obj) {
		return employeeSchedulingRecordDao.save(obj);
	}

	@Override
	public EmployeeSchedulingRecord queryObjById(Serializable id) {
		return employeeSchedulingRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		employeeSchedulingRecordDao.deleteById(id);
	}

	@Override
	public void addEmployeeSchedulings(EmployeeSchedulingDto employeeSchedulingDto) {
		String employeeCodes = employeeSchedulingDto.getEmployeeCodes();
		String employeeNames = employeeSchedulingDto.getEmployeeNames();

		if(employeeCodes!=null&&!"".equals(employeeCodes.trim())) {
			if(employeeCodes.contains(",")) {
				String[] employeeCodesArray = employeeCodes.split(",");
				String[] employeeNamesArray = employeeNames.split(",");
				for(int i = 0;i<employeeCodesArray.length;i++) {
					EmployeeSchedulingRecord record = new EmployeeSchedulingRecord();
					BeanUtils.copyProperties(employeeSchedulingDto, record);
					record.setEmployeeCode(employeeCodesArray[i]);
					record.setEmployeeName(employeeNamesArray[i]);
					addEmployeeScheduling(record, employeeSchedulingDto.getFrom(), employeeSchedulingDto.getTo());
				}
			}else {
				EmployeeSchedulingRecord record = new EmployeeSchedulingRecord();
				BeanUtils.copyProperties(employeeSchedulingDto, record);
				record.setEmployeeCode(employeeCodes);
				record.setEmployeeName(employeeNames);
				addEmployeeScheduling(record, employeeSchedulingDto.getFrom(), employeeSchedulingDto.getTo());
			}
		}
	}
	/**
	 * 添加人员排班
	 * @param record
	 */
	private void addEmployeeScheduling(EmployeeSchedulingRecord record,Date from,Date to) {
		if(to.getTime()>=from.getTime()) {
			Calendar fromCal = Calendar.getInstance();
			fromCal.setTime(from);
			Calendar toCal = Calendar.getInstance();
			toCal.add(Calendar.DATE, 1);
			toCal.setTime(to);
			while(true) {
				if(!DateUtils.isSameDay(fromCal, toCal)) {
					EmployeeSchedulingRecord employeeSchedulingRecord = new EmployeeSchedulingRecord();
					BeanUtils.copyProperties(record, employeeSchedulingRecord);
					employeeSchedulingRecord.setSchedulingDate(fromCal.getTime());
					List<EmployeeSchedulingRecord> list = employeeSchedulingRecordDao.findByHQL("from EmployeeSchedulingRecord r where r.employeeCode=?0"
							+ " and r.schedulingDate=?1 and r.classCode=?2", new Object[] {employeeSchedulingRecord.getEmployeeCode(),
									employeeSchedulingRecord.getSchedulingDate(),employeeSchedulingRecord.getClassCode()});
					if(CollectionUtils.isEmpty(list)) {
						employeeSchedulingRecordDao.save(employeeSchedulingRecord);
					}
					fromCal.add(Calendar.DATE, 1);
				}else {
					break;
				}
			}
		}
	}

	@Override
	public List<EmployeeSchedulingRecord> queryEmployeeSchedulingRecordsByTodayAndCode(String code) {
		String hql ="from EmployeeSchedulingRecord e where e.employeeCode=?0 and DateDiff(dd,e.schedulingDate,getdate())=0 ";
		return employeeSchedulingRecordDao.findByHQL(hql, new Object[]{code});
	}
}
