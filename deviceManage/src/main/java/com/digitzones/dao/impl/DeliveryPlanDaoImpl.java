package com.digitzones.dao.impl;
import com.digitzones.dao.IDeliveryPlanDao;
import com.digitzones.dao.IJobBookingFormDao;
import com.digitzones.model.DeliveryPlan;
import com.digitzones.model.JobBookingForm;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
@Repository
public class DeliveryPlanDaoImpl extends CommonDaoImpl<DeliveryPlan> implements IDeliveryPlanDao {
    public DeliveryPlanDaoImpl() {
        super(DeliveryPlan.class);
    }
    /**
     * 根据发货日期查找最大发货单单号
     * @param date 发货日期
     * @return 最大发货单单号
     */
    @Override
    public String queryMaxFormNoByDeliverDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (String) getSession().createNativeQuery("select MAX(formNo) from DeliveryPlan where year(deliverDate)=?0" +
                " and month(deliverDate)=?1 and day(deliverDate)=?2")
                .setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
                .setParameter(2,c.get(Calendar.DATE)).uniqueResult();
    }
}
