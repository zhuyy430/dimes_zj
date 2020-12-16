package com.digitzones.dao.impl;
import com.digitzones.dao.IJobBookingFormDetailDao;
import com.digitzones.model.Classes;
import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.procurement.dao.IWarehousingApplicationFormDetailDao;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class JobBookingFormDetailDaoImpl extends CommonDaoImpl<JobBookingFormDetail> implements IJobBookingFormDetailDao {
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	public JobBookingFormDetailDaoImpl() {
        super(JobBookingFormDetail.class);
    }
    @Override
    public void deleteByFormNo(String formNo) {
        String sql = "delete from JobBookingFormDetail where JOBBOOKING_CODE=?0";
        getSession().createNativeQuery(sql).setParameter(0,formNo).executeUpdate();
    }

    /**
     * 根据班次，日期，生产单元id查找产量
     *
     * @param classes
     * @param day
     * @param productionUnitId
     * @return
     */
    @Override
    public double queryBottleneckCountByClassesIdAndDay(Classes classes, Date day, Long productionUnitId) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    	String sql = "select isnull(sum(case  when detail.amountOfJobBooking is null then 0 else detail.amountOfJobBooking end) ,0) from JobBookingFormDetail detail inner join DEVICESITE site on detail.deviceSiteCode=site.code"
                + " inner join JobBookingForm form on detail.JOBBOOKING_CODE = form.formNo "
                + " inner join Device d on site.device_id = d.id "
                + " where detail.forJobBookingDate=?0 and d.productionunit_id=?1 and detail.classCode=?2 "
                + " and site.bottleneck=1 and d.isDimesUse=1";
        
       List list =  getSession().createNativeQuery(sql).setParameter(0, format.format(day))
        .setParameter(1,productionUnitId)
        .setParameter(2, classes.getCode()).list();
        if(list!=null&&list.size()>0) {
    		return (Double) list.get(0);
    	}
        return 0;
    }
/*    @Override
    public double queryBottleneckCountByClassesIdAndDay(Classes classes, Date day, Long productionUnitId) {
    	Calendar c = Calendar.getInstance() ;
    	c.setTime(day);
    	//班次开始时间设置
    	Calendar startCalendar = Calendar.getInstance();
    	startCalendar.setTime(classes.getStartTime());
    	startCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
    	startCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
    	startCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
    	//班次结束时间设置
    	Calendar endCalendar = Calendar.getInstance();
    	endCalendar.setTime(classes.getEndTime());
    	endCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
    	endCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
    	endCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
    	//班次跨天
    	if(classes.getStartTime().getTime()>=classes.getEndTime().getTime()) {
    		endCalendar.add(Calendar.DATE, 1);
    	}
    	
    	Date startDate = startCalendar.getTime();
    	Date endDate = endCalendar.getTime();
    	String sql = "select isnull(sum(case  when detail.amountOfJobBooking is null then 0 else detail.amountOfJobBooking end) ,0) from JobBookingFormDetail detail inner join DEVICESITE site on detail.deviceSiteCode=site.code"
    			+ " inner join JobBookingForm form on detail.JOBBOOKING_CODE = form.formNo "
    			+ " inner join Device d on site.device_id = d.id "
    			+ " where year(form.jobBookingDate)=?0 and month(form.jobBookingDate)=?1"
    			+ " and (form.jobBookingDate between ?2 and ?3) and d.productionunit_id=?4  "
    			+ " and site.bottleneck=1 and d.isDimesUse=1";
    	
    	int year = c.get(Calendar.YEAR);
    	int month = c.get(Calendar.MONTH)+1;
    	List list = getSession().createNativeQuery(sql)
    			.setParameter(0, year)
    			.setParameter(1, month)
    			.setParameter(2, startDate)
    			.setParameter(3,endDate)
    			.setParameter(4, productionUnitId)
    			.list();
    	if(list!=null&&list.size()>0) {
    		return (Double) list.get(0);
    	}
    	return 0;
    }
*/    /**
     * 查询生产数量和总标准节拍
     * @param classes
     * @param deviceSiteCode
     * @param date
     * @return
     */
    @Override
    public Object[] queryCountAndSumOfStandardBeat4CurrentClass(Classes classes, String deviceSiteCode, Date date) {
        String sql = "select isnull(sum(case when detail.amountOfJobBooking is null then 0 else detail.amountOfJobBooking end),0) _count," +
                " isnull(sum( case when ip.standardBeat is null then 0 else ip.standardBeat end),0) sumStandardBeat, " +
                " isnull(sum(case when detail.amountOfJobBooking is not null and ip.standardBeat is not null then detail.amountOfJobBooking*ip.standardBeat else 0 end),0) " +
                " from JobBookingFormDetail detail inner join JobBookingForm form on detail.JOBBOOKING_CODE=form.formNo " +
                " inner join INVENTORY_PROCESS ip on ip.INVENTORY_CODE = detail.inventoryCode and ip.process_id in " +
                "    (select p.id from PROCESSES p inner join JobBookingFormDetail d on p.code = d.processCode where d.processCode=detail.processCode) " +
                "  where detail.classCode=?1 and detail.deviceSiteCode=?0 and detail.forJobBookingDate=?2 " ;
        Object[] list = (Object[]) getSession().createNativeQuery(sql)
                .setParameter(0, deviceSiteCode)
                .setParameter(1,classes.getCode())
                .setParameter(2, format.format(date))
                .uniqueResult();
        return list;
    }
/*    @Override
    public Object[] queryCountAndSumOfStandardBeat4CurrentClass(Classes classes, String deviceSiteCode, Date date) {
    	Calendar c = Calendar.getInstance() ;
    	c.setTime(date);
    	//班次开始时间设置
    	Calendar startCalendar = Calendar.getInstance();
    	startCalendar.setTime(classes.getStartTime());
    	startCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
    	startCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
    	startCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
    	//班次结束时间设置
    	Calendar endCalendar = Calendar.getInstance();
    	endCalendar.setTime(classes.getEndTime());
    	endCalendar.set(Calendar.YEAR, c.get(Calendar.YEAR));
    	endCalendar.set(Calendar.MONTH,c.get(Calendar.MONTH));
    	endCalendar.set(Calendar.DATE, c.get(Calendar.DATE));
    	//班次跨天
    	if(classes.getStartTime().getTime()>=classes.getEndTime().getTime()) {
    		endCalendar.add(Calendar.DATE, 1);
    	}
    	Date startDate = startCalendar.getTime();
    	Date endDate = endCalendar.getTime();
    	String sql = "select isnull(sum(case when detail.amountOfJobBooking is null then 0 else detail.amountOfJobBooking end),0) _count," +
    			" isnull(sum( case when ip.standardBeat is null then 0 else ip.standardBeat end),0) sumStandardBeat, " +
    			" isnull(sum(case when detail.amountOfJobBooking is not null and ip.standardBeat is not null then detail.amountOfJobBooking*ip.standardBeat else 0 end),0) " +
    			" from JobBookingFormDetail detail inner join JobBookingForm form on detail.JOBBOOKING_CODE=form.formNo " +
    			" inner join INVENTORY_PROCESS ip on ip.INVENTORY_CODE = detail.inventoryCode and ip.process_id in " +
    			"    (select p.id from PROCESSES p inner join JobBookingFormDetail d on p.code = d.processCode where d.processCode=detail.processCode) " +
    			"  where detail.classCode=?1 and detail.deviceSiteCode=?0 and form.jobBookingDate between ?2 and ?3 " ;
    	Object[] list = (Object[]) getSession().createNativeQuery(sql)
    			.setParameter(0, deviceSiteCode)
    			.setParameter(1,classes.getCode())
    			.setParameter(2, startDate)
    			.setParameter(3,endDate)
    			.uniqueResult();
    	return list;
    }
*/    /**
     * 根据工单单号查找最大箱号
     * @param no
     * @return
     */
    @Override
    public int queryMaxBoxNumByNoAndProcessCode(String no,String processCode) {
        return (int) getSession().createNativeQuery("select isnull(max(case when boxNum is null then 0 else boxNum end),0)" +
                "  from JobBookingFormDetail where no=?0 and processCode=?1")
                .setParameter(0,no)
                .setParameter(1,processCode)
                .uniqueResult();
    }
}
