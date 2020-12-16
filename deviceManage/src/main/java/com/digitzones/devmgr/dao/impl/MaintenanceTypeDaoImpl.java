package com.digitzones.devmgr.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenanceTypeDao;
import com.digitzones.devmgr.model.MaintenanceType;
@Repository
public class MaintenanceTypeDaoImpl extends CommonDaoImpl<MaintenanceType> implements IMaintenanceTypeDao {
	public MaintenanceTypeDaoImpl() {
		super(MaintenanceType.class);
	}
}
