package com.digitzones.devmgr.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenanceStaffRecordDao;
import com.digitzones.devmgr.model.MaintenanceStaffRecord;
@Repository
public class MaintenanceStaffRecordDaoImpl extends CommonDaoImpl<MaintenanceStaffRecord> implements IMaintenanceStaffRecordDao {
	public MaintenanceStaffRecordDaoImpl() {
		super(MaintenanceStaffRecord.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<MaintenanceStaffRecord> findByOrderId(Long orderId) {
			String sql = "select * from MaintenanceStaffRecord m where m.DEVICEREPAIR_ID=?0" ;
			return getSession().createSQLQuery(sql).setParameter(0, orderId)
					.addEntity(MaintenanceStaffRecord.class).list();
	}
}
