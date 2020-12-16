package com.digitzones.devmgr.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintenanceItemDao;
import com.digitzones.devmgr.model.MaintenanceItem;
@Repository
public class MaintenanceItemDaoImpl extends CommonDaoImpl<MaintenanceItem> implements IMaintenanceItemDao {
	public MaintenanceItemDaoImpl() {
		super(MaintenanceItem.class);
	}
}
