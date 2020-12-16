package com.digitzones.dao.impl;
import com.digitzones.dao.IInspectionRecordDao;
import com.digitzones.model.InspectionRecord;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;

@Repository
public class InspectionRecordDaoImpl extends CommonDaoImpl<InspectionRecord> implements IInspectionRecordDao {
    public InspectionRecordDaoImpl() {
        super(InspectionRecord.class);
    }
    /**
     * 根据检验日期查找最大检验单号
     * @param now
     * @return
     */
    @Override
    public String queryMaxFormNoByInspectionDate(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        return (String) getSession().createNativeQuery("select MAX(formNo) from InspectionRecord where year(inspectionDate)=?0" +
                " and month(inspectionDate)=?1 and day(inspectionDate)=?2")
                .setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
                .setParameter(2,c.get(Calendar.DATE)).uniqueResult();
    }
}
