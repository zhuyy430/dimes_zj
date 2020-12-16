package com.digitzones.devmgr.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.ISparepartDao;
import com.digitzones.devmgr.model.Sparepart;
@Repository
public class SparepartDaoImpl extends CommonDaoImpl<Sparepart> implements ISparepartDao {
	public SparepartDaoImpl() {
		super(Sparepart.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sparepart> querySparepartsByDeviceCode(String code) {
		String sql = "select * from SPAREPART s where (s.deviceCodes like '%,"+code+",%' or s.deviceCodes=',,') and s.id not in "
				+ "(select sparepartId from DEVICESPAREPARTRECORD ds where ds.deviceCode=?0 and ds.unbindDate is null)" ;
		return getSession().createNativeQuery(sql).setParameter(0, code)
				.addEntity(Sparepart.class).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Sparepart> querySparepartsByDeviceCode(String code,Long devicerepairId) {
		String sql = "select * from SPAREPART s where (s.deviceCodes like '%,"+code+",%' or s.deviceCodes=',,') and s.id not in "
				+ "(select s.SPAREPART_ID from SPAREPARTRECORD s where s.DEVICEREPAIR_ID=?0)" ;
		return getSession().createNativeQuery(sql).setParameter(0, devicerepairId)
				.addEntity(Sparepart.class).list();
	}
}
