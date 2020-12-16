package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.dao.ISecureEnvironmentRecordDao;
import com.digitzones.model.Pager;
import com.digitzones.model.SecureEnvironmentRecord;
import com.digitzones.service.ISecureEnvironmentRecordService;
@Service
public class SecureEnvironmentRecordServiceImpl implements ISecureEnvironmentRecordService {
	private ISecureEnvironmentRecordDao secureEnvironmentRecordDao;
	@Autowired
	public void setSecureEnvironmentRecordDao(ISecureEnvironmentRecordDao secureEnvironmentRecordDao) {
		this.secureEnvironmentRecordDao = secureEnvironmentRecordDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return secureEnvironmentRecordDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(SecureEnvironmentRecord obj) {
		secureEnvironmentRecordDao.update(obj);
	}

	@Override
	public SecureEnvironmentRecord queryByProperty(String name, String value) {
		return secureEnvironmentRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(SecureEnvironmentRecord obj) {
		return secureEnvironmentRecordDao.save(obj);
	}
	@Override
	public SecureEnvironmentRecord queryObjById(Serializable id) {
		return secureEnvironmentRecordDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		secureEnvironmentRecordDao.deleteById(id);
	}
	@Override
	public SecureEnvironmentRecord queryMostSeriousSecureEnvironmentRecordByDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DATE);
		String hql = "from SecureEnvironmentRecord record inner join fetch record.grade g "
				+ "	where year(record.currentDate)=?0 and month(record.currentDate)=?1 and day(record.currentDate)=?2"
				+ " and  g.weight=(select max(r.grade.weight) from SecureEnvironmentRecord r  "
				+ " where year(r.currentDate)=?0 and month(r.currentDate)=?1 and day(r.currentDate)=?2)";
		List<SecureEnvironmentRecord> list = secureEnvironmentRecordDao.findByHQL(hql, new Object[] {year,month,day});
		if(!CollectionUtils.isEmpty(list)) {
			return list.get(0); 
		}
		return null;
	}
}
