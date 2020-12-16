package com.digitzones.devmgr.service.impl;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.digitzones.constants.Constant;
import com.digitzones.devmgr.dao.ITransferRecordDao;
import com.digitzones.devmgr.model.DeviceRepair;
import com.digitzones.devmgr.model.MaintenancePlanRecord;
import com.digitzones.devmgr.model.MaintenanceStaff;
import com.digitzones.devmgr.model.MaintenanceStaffRecord;
import com.digitzones.devmgr.model.TransferRecord;
import com.digitzones.devmgr.service.IDeviceRepairOrderService;
import com.digitzones.devmgr.service.IMaintenancePlanRecordService;
import com.digitzones.devmgr.service.IMaintenanceStaffRecordService;
import com.digitzones.devmgr.service.IMaintenanceStaffService;
import com.digitzones.devmgr.service.ITransferRecordService;
import com.digitzones.model.Pager;
@Service
public class TransferRecordServiceImpl implements ITransferRecordService {
	@Autowired
	private ITransferRecordDao transferRecordDao ;
	@Autowired
	private IMaintenanceStaffRecordService maintenanceStaffRecordService;
	@Autowired
	private IMaintenancePlanRecordService maintenancePlanRecordService;
	@Autowired
	private IMaintenanceStaffService maintenanceStaffService;
	@Autowired
	@Qualifier("deviceRepairOrderServiceProxy")
	private IDeviceRepairOrderService deviceRepairOrderService;
	@Override
	public Pager<TransferRecord> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return transferRecordDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(TransferRecord obj) {
		transferRecordDao.update(obj);
	}
	@Override
	public TransferRecord queryByProperty(String name, String value) {
		return transferRecordDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(TransferRecord obj) {
		return transferRecordDao.save(obj);
	}
	@Override
	public TransferRecord queryObjById(Serializable id) {
		return transferRecordDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		transferRecordDao.deleteById(id);
	}
	@Override
	public void updateTransferRecordRecord(MaintenanceStaff ms, TransferRecord trecord,String operator) {
		DeviceRepair deviceRepair = trecord.getDeviceRepair();
		deviceRepair.setMaintainCode(ms.getCode());
		deviceRepair.setMaintainName(ms.getName());
		deviceRepairOrderService.updateObj(deviceRepair);
		deviceRepairOrderService.confirmDeviceRepair(deviceRepair, operator, Constant.ReceiveType.TRANSFER);
		/**修改责任人信息*/
		List<MaintenanceStaffRecord> maintenanceStaffRecord = maintenanceStaffRecordService.queryMaintenanceStaffRecordPersonLiableByorderId(deviceRepair.getId());
		if(maintenanceStaffRecord!=null&&maintenanceStaffRecord.size()==1){
			MaintenanceStaffRecord m = maintenanceStaffRecord.get(0);
			m.setCode(ms.getCode());
			m.setName(ms.getName());
			maintenanceStaffRecordService.updateObj(m);
		}
		trecord.setStatus(true);
		trecord.setAcceptCode(ms.getCode());
		trecord.setAcceptName(ms.getName());
		this.updateObj(trecord);
		MaintenanceStaff maintenanceStaff = maintenanceStaffService.queryByProperty("code", trecord.getTransferCode());
		List<MaintenancePlanRecord> mlist = maintenancePlanRecordService.queryMaintenanceMPRWithUser(trecord.getTransferCode());
		List<DeviceRepair> dlist = deviceRepairOrderService.queryMaintenanceDeviceRepairWithUser(trecord.getTransferCode());
		if(!mlist.isEmpty()){
			maintenanceStaff.setWorkStatus(Constant.MaintenanceStaffStatus.MAINTENANCE);
		}else if(!dlist.isEmpty()){
			maintenanceStaff.setWorkStatus(Constant.MaintenanceStaffStatus.MAINTAIN);
		}else{
			maintenanceStaff.setWorkStatus(Constant.MaintenanceStaffStatus.ONDUTY);
		}
	}
	@Override
	   public long queryTransferRecordCountByDate(Date date) {
	      Calendar c = Calendar.getInstance();
	      c.setTime(date);
	      return transferRecordDao.findCount("from TransferRecord tr where year(tr.transferDate)=?0"
	            + " and month(tr.transferDate)=?1 and day(tr.transferDate)=?2", new Object[] {
	                  c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DATE)
	            });
	   }

	@Override
	public List<TransferRecord> queryAllTransferRecord(boolean status) {
		return transferRecordDao.findByHQL("from TransferRecord tr where status=?0 order by transferDate", new Object[] {status});
	}
}
