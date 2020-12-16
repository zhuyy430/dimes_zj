package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.IQualityCalendarRecordDao;
import com.digitzones.model.QualityCalendarRecord;
@Repository
public class QualityCalendarRecordDaoImpl extends CommonDaoImpl<QualityCalendarRecord> implements IQualityCalendarRecordDao {
	public QualityCalendarRecordDaoImpl() {
		super(QualityCalendarRecord.class);
	}

	@Override
	public Integer queryCountByDayAndTypeId(int year, int month, int day, Long typeId) {
		String sql = "select count(id) from QUALITYCALENDARRECORD q where year(currentDate)=?0 and month(currentDate)=?1"
				+ " and day(currentDate)=?2 and typeId=?3";
		Integer count = (Integer) this.getSession().createNativeQuery(sql)
						 .setParameter(0, year)
						 .setParameter(1, month)
						 .setParameter(2, day)
						 .setParameter(3,typeId)
						 .uniqueResult();
		if(count!=null) {
			return count;
		}
		return 0;
	}
}
