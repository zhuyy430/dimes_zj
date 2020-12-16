package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.ISparepartDao;
import com.digitzones.devmgr.dao.ISparepartRecordDao;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.DeviceSparepartMapping;
import com.digitzones.devmgr.model.Sparepart;
import com.digitzones.devmgr.model.SparepartRecord;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.service.IDeviceSparepartMappingService;
import com.digitzones.devmgr.service.ISparepartRecordService;
import com.digitzones.model.Pager;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class SparepartRecordServiceImpl implements ISparepartRecordService {
	@Autowired
	ISparepartRecordDao sparepartRecordDao;
	@Autowired
	ISparepartDao sparepartDao;
	@Autowired
	IDeviceSparepartMappingService deviceSparepartMappingService;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	IDeviceRepairOrderService deviceRepairOrderService;
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return sparepartRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(SparepartRecord obj) {
		sparepartRecordDao.update(obj);
	}

	@Override
	public SparepartRecord queryByProperty(String name, String value) {
		return sparepartRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(SparepartRecord obj) {
		return sparepartRecordDao.save(obj);
	}

	@Override
	public SparepartRecord queryObjById(Serializable id) {
		return sparepartRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		sparepartRecordDao.deleteById(id);
	}

	@Override
	public List<SparepartRecord> querySparepartRecordBydeviceRepairOrderId(Long deviceRepairOrderId) {
		String hql="from SparepartRecord m where m.deviceRepair.id=?0";
		return sparepartRecordDao.findByHQL(hql, new Object[] {deviceRepairOrderId});
	}

	@Override
	public void addSparepartRecords(Long deviceRepairId, String sparepartIds) {
		DeviceRepair deviceRepair = new DeviceRepair();
		deviceRepair.setId(deviceRepairId);
		String[] sparepartIdArray = sparepartIds.split(",");
		for(int i = 0;i<sparepartIdArray.length;i++) {
			Long sparepartId = Long.valueOf(sparepartIdArray[i]);
			Sparepart sparepart = sparepartDao.findById(sparepartId);
			 SparepartRecord sparepartRecord = new SparepartRecord();

			 sparepartRecord.setCreateDate(new Date());
			 sparepartRecord.setDeviceRepair(deviceRepair);
			 sparepartRecord.setSparepart(sparepart);
			 sparepartRecord.setQuantity(0l);
			 
			 sparepartRecordDao.save(sparepartRecord);
			DeviceRepair dorder = deviceRepairOrderService.queryObjById(deviceRepairId);
			DeviceSparepartMapping dsMapping = deviceSparepartMappingService.queryBySparePartIDAndDeviceId(sparepartId, dorder.getDevice().getId());
			if(dsMapping!=null){
				dsMapping.setLastUseDate(new Date());
				deviceSparepartMappingService.updateObj(dsMapping);
			}
		}
	}
}
