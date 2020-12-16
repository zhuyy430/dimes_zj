package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.ISparepartDao;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.service.ISparepartService;
import com.digitzones.model.Pager;
@Service
public class SparepartServiceImpl implements ISparepartService {
	@Autowired
	private ISparepartDao sparepartDao;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return sparepartDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(Sparepart obj) {
		sparepartDao.update(obj);
	}
	@Override
	public Sparepart queryByProperty(String name, String value) {
		return sparepartDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(Sparepart obj) {
		return sparepartDao.save(obj);
	}
	@Override
	public Sparepart queryObjById(Serializable id) {
		return sparepartDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		sparepartDao.deleteById(id);
	}
	@Override
	public List<Sparepart> querySparepartsByDeviceCode(String code) {
		return sparepartDao.querySparepartsByDeviceCode(code);
	}
	@Override
	public List<Sparepart> querySparepartsByDeviceCode(String code,Long devicerepairId) {
		return sparepartDao.querySparepartsByDeviceCode(code,devicerepairId);
	}
	@Override
	public List<Sparepart> queryMaintenanceSparepartsByDeviceCode(String deviceCode,Long maintenancePlanRecordId) {
		String hql = "from Sparepart part where (part.deviceCodes like ?0 or part.deviceCodes=',,') and"
				+ "  part.code not in (select ms.code from MaintenanceSparepart ms where ms.maintenancePlanRecord.id=?1)";
		return sparepartDao.findByHQL(hql,new Object[] {"%,"+deviceCode+",%",maintenancePlanRecordId});
	}
	@Override
	public List<Sparepart> queryAllSpareparts() {
		return sparepartDao.findAll();
	}
	@Override
	public List<Sparepart> queryAllSparepartsBymaintenancePlanRecordId(Long maintenancePlanRecordId) {
		String hql = "from Sparepart part where"
				+ "  part.code not in (select ms.code from MaintenanceSparepart ms where ms.maintenancePlanRecord.id=?0)";
		return sparepartDao.findByHQL(hql,new Object[] {maintenancePlanRecordId});
	}
	@Override
	public List<Sparepart> queryAllSparepartsByDeviceRepairId(Long deviceRepairId) {
		String hql = "from Sparepart part where"
				+ "  part.code not in (select sr.sparepart.code from SparepartRecord sr where sr.deviceRepair.id=?0)";
		
		return sparepartDao.findByHQL(hql,new Object[] {deviceRepairId});
	}
	@Override
	public List<Sparepart> queryOtherSpareparts(String hql, List<Object> args) {
		return sparepartDao.findByHQL(hql, args.toArray());
	}
}
