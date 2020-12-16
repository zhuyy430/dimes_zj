package com.digitzones.dao.impl;
import com.digitzones.dao.IJobBookingFormDao;
import com.digitzones.model.JobBookingForm;
import com.digitzones.procurement.dao.IWarehousingApplicationFormDao;
import com.digitzones.procurement.model.WarehousingApplicationForm;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;

@Repository
public class JobBookingFormDaoImpl extends CommonDaoImpl<JobBookingForm> implements IJobBookingFormDao {
    public JobBookingFormDaoImpl() {
        super(JobBookingForm.class);
    }
    /**
     * 根据报工日期查找最大报工单单号
     *
     * @param date 报工日期
     * @return 最大报工单单号
     */
    @Override
    public String queryMaxFormNoByJobBookingDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (String) getSession().createNativeQuery("select MAX(formNo) from JobBookingForm where year(jobBookingDate)=?0" +
                " and month(jobBookingDate)=?1 and day(jobBookingDate)=?2")
                .setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
                .setParameter(2,c.get(Calendar.DATE)).uniqueResult();
    }

    /**
     * 查找最大报工单单号
     *
     * @return 最大报工单单号
     */
    @Override
    public String queryMaxFormNo() {
    	return (String) getSession().createNativeQuery("select MAX(formNo) from JobBookingForm")
    			.uniqueResult();
    }

    @Override
    public Double queryNumberByJobBookingForm(String no) {
        return (Double) getSession().createNativeQuery("select sum(d.amountOfJobBooking) from jobBookingForm d where d.workSheetNo=?0 ")
                .setParameter(0,no).uniqueResult();
    }

    @Override
    public Double queryNumberByJobBookingFormAndClassesCode(String no, String classesCode) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return (Double) getSession().createNativeQuery("select sum(amountOfJobBooking) from JobBookingForm where year(jobBookingDate)=?0" +
                " and month(jobBookingDate)=?1 and day(jobBookingDate)=?2 and workSheetNo=?3 and classCode=?4")
                .setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
                .setParameter(2,c.get(Calendar.DATE)).setParameter(3,no).setParameter(4,classesCode).uniqueResult();
    }
}
