package com.digitzones.devmgr.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenanceSparepartDao;
import com.digitzones.devmgr.model.MaintenanceSparepart;
@Repository
public class MaintenanceSparepartDaoImpl extends CommonDaoImpl<MaintenanceSparepart> implements IMaintenanceSparepartDao {
	public MaintenanceSparepartDaoImpl() {
		super(MaintenanceSparepart.class);
	}
}
