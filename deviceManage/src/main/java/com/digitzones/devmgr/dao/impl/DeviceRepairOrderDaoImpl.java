package com.digitzones.devmgr.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import com.digitzones.constants.Constant;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceRepairOrderDao;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
@Repository
public class DeviceRepairOrderDaoImpl extends CommonDaoImpl<DeviceRepair> implements IDeviceRepairOrderDao {
	public DeviceRepairOrderDaoImpl() {
		super(DeviceRepair.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<DeviceRepair> findAlarmsCount(String alarmedIds) {
		// TODO Auto-generated method stub
		String[] ids = alarmedIds.split(",");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		String sql = "select * from DeviceRepair d where d.status=?0 and year(d.createDate)=?1 and month(d.createDate)=?2"
				+ " and day(d.createDate)=?3 and d.id not in(:ids)";
		return getSession().createSQLQuery(sql).setParameter(0, Constant.DeviceRepairStatus.WAITINGASSIGN)
												.setParameter(1, calendar.get(Calendar.YEAR))
												.setParameter(2, calendar.get(Calendar.MONTH)+1)
												.setParameter(3, calendar.get(Calendar.DATE))
												.setParameterList("ids", ids)
				.addEntity(DeviceRepair.class).list();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<DeviceRepair> findFirstDeviceRepair() {
		String sql = "select * from DeviceRepair d where d.status=?0 order by d.createDate";
		return getSession().createSQLQuery(sql).setParameter(0, Constant.DeviceRepairStatus.WAITINGASSIGN)
				.addEntity(DeviceRepair.class).list();
	}

	@Override
	public List<DeviceRepair> queryDeviceRepairWithUserandStatus(String status, String username) {
		String sql="select * from DeviceRepair d where 1=1 "; 
		if(!username.equals("")&&username!=null) {
			sql+=" and exists(select * from MaintenanceStaffRecord s where d.id=s.DEVICEREPAIR_ID and s.name=:username) ";
		}
		if(!status.equals("")&&status!=null) {
			sql+=" and d.status=:status";
		}
	      NativeQuery dataQueryObj = getSession().createSQLQuery(sql);
	      if(!username.equals("")&&username!=null) {
		        dataQueryObj.setParameter("username", username);
		      
		   }

				

	      if(!status.equals("")&&status!=null) {
	        
	            dataQueryObj.setParameter("status", status);
	         
	      }
		     
		
		return dataQueryObj.addEntity(DeviceRepair.class).list();
	}

	@Override
	public List<DeviceRepair> queryReceiptDeviceRepairWithUser(String code) {
		String sql = "select * from DeviceRepair d where exists(select * from MaintenanceStaffRecord s where d.id=s.DEVICEREPAIR_ID and s.code=?0 and s.receiveTime is null and s.completeTime is null) and d.status!='WAITWORKSHOPCOMFIRM' order by d.createDate desc";
		return getSession().createSQLQuery(sql).setParameter(0, code)
				.addEntity(DeviceRepair.class).list();
	}

	@Override
	public List<DeviceRepair> queryMaintenanceDeviceRepairWithUser(String code) {
		String sql = "select * from DeviceRepair d where exists(select * from MaintenanceStaffRecord s where d.id=s.DEVICEREPAIR_ID and s.code=?0 and s.receiveTime is not null and s.completeTime is null) and d.status!='WAITWORKSHOPCOMFIRM' order by d.createDate desc";
		return getSession().createSQLQuery(sql).setParameter(0, code)
				.addEntity(DeviceRepair.class).list();
	}
	@Override
	public List<DeviceRepair> queryMaintenanceDeviceRepairWithStatus(String status) {
		String sql = "select * from DeviceRepair d where  d.status=?0";
		return getSession().createSQLQuery(sql).setParameter(0, status)
				.addEntity(DeviceRepair.class).list();
	}

	@Override
	public int queryBadgeWithDeviceRepair(String hql) {
		Integer count = new Integer(((Long) getSession().createQuery(hql).uniqueResult()).toString());
		return count.intValue();
	}
	
	
	
	
}
