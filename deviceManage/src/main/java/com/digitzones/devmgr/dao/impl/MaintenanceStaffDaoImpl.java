package com.digitzones.devmgr.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenanceStaffDao;
import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.Sparepart;
@Repository
public class MaintenanceStaffDaoImpl extends CommonDaoImpl<MaintenanceStaff> implements IMaintenanceStaffDao {
	public MaintenanceStaffDaoImpl() {
		super(MaintenanceStaff.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MaintenanceStaff> findListByProductionUnitIdAndStatus(Long productionUnitId ,String status) {
		String sql = "select mm.* from MAINTENANCESTAFF mm inner join EmployeeProductionUnitRecord ep on mm.code=ep.employeeCode where mm.id not in( "
				+" select m.id from MAINTENANCESTAFF m inner join MAINTENANCESTAFFRECORD mr on m.code=mr.code "
				+" inner join DEVICEREPAIR d on mr.DEVICEREPAIR_ID=d.id "
				+" where d.status!='MAINTAINCOMPLETE' "
				+" group by m.id) and workStatus=?0 and queue>0 and ep.productionUnit_ID=?1 group by mm.changeDate,mm.code,mm.departmentName,mm.id,mm.name "
				+" ,mm.onDutyStatus,mm.positionName,mm.queue,mm.tel,mm.workStatus order by mm.queue asc" ;
		return getSession().createNativeQuery(sql).setParameter(0, status).setParameter(1, productionUnitId)
				.addEntity(MaintenanceStaff.class).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MaintenanceStaff> findListByStatus(String status) {
		String sql = "select mm.* from MAINTENANCESTAFF mm where mm.id not in( "
				+" select m.id from MAINTENANCESTAFF m inner join MAINTENANCESTAFFRECORD mr on m.code=mr.code "
				+" inner join DEVICEREPAIR d on mr.DEVICEREPAIR_ID=d.id "
				+" where d.status!='MAINTAINCOMPLETE' "
				+" group by m.id) and workStatus=?0 and queue>0 group by mm.changeDate,mm.code,mm.departmentName,mm.id,mm.name "
				+" ,mm.onDutyStatus,mm.positionName,mm.queue,mm.tel,mm.workStatus order by mm.queue asc" ;
		return getSession().createNativeQuery(sql).setParameter(0, status)
				.addEntity(MaintenanceStaff.class).list();
	}
}
