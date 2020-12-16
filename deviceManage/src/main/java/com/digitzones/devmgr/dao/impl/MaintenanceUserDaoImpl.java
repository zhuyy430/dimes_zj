package com.digitzones.devmgr.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenanceUserDao;
import com.digitzones.devmgr.model.MaintenanceUser;
@Repository
public class MaintenanceUserDaoImpl extends CommonDaoImpl<MaintenanceUser> implements IMaintenanceUserDao {
	public MaintenanceUserDaoImpl() {
		super(MaintenanceUser.class);
	}

	@Override
	public void deleteByMaintenancePlanRecordId(Long id) {
		getSession().createNativeQuery("delete from MaintenanceUser  where MAINTENANCEPLANRECORD_ID=?0")
		.setParameter(0, id).executeUpdate();
	}
}
