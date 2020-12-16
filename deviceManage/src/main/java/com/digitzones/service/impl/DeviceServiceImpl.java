package com.digitzones.service.impl;

import com.digitzones.dao.IDeviceDao;
import com.digitzones.dao.IDeviceSiteDao;
import com.digitzones.dao.IDeviceSiteParameterMappingDao;
import com.digitzones.devmgr.dao.IDeviceProjectRecordDao;
import com.digitzones.devmgr.dao.IDeviceSparepartMappingDao;
import com.digitzones.devmgr.model.DeviceProjectRecord;
import com.digitzones.devmgr.model.DeviceSparepartMapping;
import com.digitzones.model.Device;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.DeviceSiteParameterMapping;
import com.digitzones.model.Pager;
import com.digitzones.procurement.service.IInventoryProcessMappingService;
import com.digitzones.procurement.service.IInventoryService;
import com.digitzones.service.*;
import com.digitzones.util.ExcelUtil;
import com.digitzones.util.WorkpieceProcessParameterExcelUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Service
public class DeviceServiceImpl implements IDeviceService {
	private IDeviceDao deviceDao;
	private IDeviceSiteDao deviceSiteDao;
	private IDeviceSiteParameterMappingDao deviceSiteParameterMappingDao;
	@Autowired
	private IDeviceSparepartMappingDao deviceSparepartMappingDao;
	@Autowired
	private IDeviceProjectRecordDao deviceProjectRecordDao;
	@Autowired
	private IParameterService parameterService;
	@Autowired
	private IParameterTypeService parameterTypeService;
	@Autowired
	private IInventoryService inventoryService;
	@Autowired
	private IProcessesService processesService;
	@Autowired
	private IInventoryProcessMappingService inventoryProcessMappingService;
	@Autowired
	private IWorkpieceProcessParameterMappingService workpieceProcessParameterMappingService;
	
	@Autowired
	public void setDeviceSiteDao(IDeviceSiteDao deviceSiteDao) {
		this.deviceSiteDao = deviceSiteDao;
	}

	@Autowired
	public void setDeviceDao(IDeviceDao deviceDao) {
		this.deviceDao = deviceDao;
	}
	
	@Autowired
	public void setDeviceSiteParameterMappingDao(IDeviceSiteParameterMappingDao deviceSiteParameterMappingDao) {
		this.deviceSiteParameterMappingDao = deviceSiteParameterMappingDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return deviceDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Device obj) {
		deviceDao.update(obj);
	}

	@Override
	public Device queryByProperty(String name, String value) {
		return deviceDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(Device obj) {
		return deviceDao.save(obj);
	}

	@Override
	public Device queryObjById(Serializable id) {
		return deviceDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		deviceDao.deleteById(id);
	}

	@Override
	public Device queryDeviceByDeviceSiteId(Long deviceSiteId) {
		
		return this.deviceSiteDao.findById(deviceSiteId).getDevice();
	}

	@Override
	public List<Device> queryDevicesByProductionUnitId(Long productionUnitId,String module) {
		String hql = "";
		if(module.equals("dimes")){
			hql = "from Device d where d.productionUnit.id=?0 and d.isDimesUse=?1 order by d.code";
			return this.deviceDao.findByHQL(hql, 
					new Object[] {productionUnitId,true});
		}else if(module.equals("deviceManage")){
			hql = "from Device d where d.productionUnit.id=?0 and d.isDeviceManageUse=?1 order by d.code";
			return this.deviceDao.findByHQL(hql, 
					new Object[] {productionUnitId,true});
		}
		return null;
	}

	@Override
	public Serializable addDevice(Device device, File file) {
		return this.deviceDao.addDevice(device, file);
	}

	@Override
	public Integer queryDeviceSiteCountByDeviceId(Long deviceId) {
		return deviceDao.queryDeviceSiteCountByDeviceId(deviceId);
	}

	@Override
	public Integer queryClassesCountByDeviceId(Long deviceId) {
		return deviceDao.queryClassesCountByDeviceId(deviceId);
	}

	@Override
	public void updateDevice(Device device, File photo) {
		deviceDao.updateDevice(device, photo);
	}

	@Override
	public boolean deleteDevices(String[] ids) {
		for(int i = 0;i<ids.length;i++) {
			Long id = Long .valueOf(ids[i]);
			Integer count = this.queryDeviceSiteCountByDeviceId(id);
			if(count>0) {
				throw new RuntimeException("设备下存在站点，无法删除!");
			}
			deleteObj(id);
		}
		return true; 
	}

	@Override
	public void disableDevices(String[] ids) {
		for(int i = 0;i<ids.length;i++) {
			Long id = Long .valueOf(ids[i]);
			//查询该设备是否已排班，排班的设备不允许停用
			Integer classesCount = queryClassesCountByDeviceId(Long.valueOf(id));
			if(classesCount>0) {
				throw new RuntimeException("存在排班设备，该操作终止!");
			}
			Device d = queryObjById(Long.valueOf(id));
			if(d.getDisabled()!=null) {
				d.setDisabled(!d.getDisabled());
			}

			updateObj(d);
		}
	}

	@Override
	public List<Device> queryAllDevices(String module) {
		if(module.equals("dimes")){
			return deviceDao.findByHQL("from Device d where d.disabled!=?0 and d.isDimesUse=?1", new Object[] {true,true});
		}else if(module.equals("deviceManage")){
			return deviceDao.findByHQL("from Device d where d.disabled!=?0 and d.isDeviceManageUse=?1", new Object[] {true,true});
		}
		return null;
	}

	@Override
	public void imports(List<Device> list) throws RuntimeException{
		if(CollectionUtils.isEmpty(list)) {
			throw new RuntimeException("无导入数据!");
		}
		for(Device device : list) {
			Device d = deviceDao.findSingleByProperty("code", device.getCode());
			if(d!=null) {
				throw new RuntimeException("导入失败,设备编码已存在 :" + d.getCode());
			}
			deviceDao.addDevice(device, null);
			DeviceSite deviceSite =new DeviceSite();
			deviceSite.setCode(device.getCode()+"_1");
			deviceSite.setName(device.getName());
			deviceSite.setDevice(device);
			deviceSiteDao.save(deviceSite);
		}
	}

	@Override
	public void addCopyDevice(Device device, Long copyId) {
		String deviceCode=device.getCode();
		deviceDao.save(device);
		Device device1=deviceDao.findSingleByProperty("code", deviceCode);
		//添加设备站点
		DeviceSite deviceSite =new DeviceSite();
		deviceSite.setCode("DS_"+device1.getCode());
		deviceSite.setName(device.getName());
		deviceSite.setDevice(device1);
		deviceSiteDao.save(deviceSite);
		DeviceSite deviceSite1=deviceSiteDao.findSingleByProperty("code", "DS_"+device1.getCode());
		DeviceSite copyDeviceSite=deviceSiteDao.findByHQL("from DeviceSite d where d.device.id=?0", new Object[] {copyId}).get(0);
		//添加设备参数
		List<DeviceSiteParameterMapping> dspmlist=deviceSiteParameterMappingDao.findByHQL("from DeviceSiteParameterMapping dspm where dspm.deviceSite.id=?0", new Object[] {copyDeviceSite.getId()});
		for(DeviceSiteParameterMapping dspm:dspmlist) {
			DeviceSiteParameterMapping dspml=new DeviceSiteParameterMapping();
			BeanUtils.copyProperties(dspm,dspml);
			dspml.setId(null);
			dspml.setDeviceSite(deviceSite1);
			dspml.setUpdateDate(new Date());
			deviceSiteParameterMappingDao.save(dspml);
		}
		
		//添加备品备件
		List<DeviceSparepartMapping> dsmlist=deviceSparepartMappingDao.findByHQL("from DeviceSparepartMapping dsm where dsm.device.id=?0", new Object[] {copyId});
		for(DeviceSparepartMapping dsm:dsmlist) {
			DeviceSparepartMapping dsml=new DeviceSparepartMapping();
			BeanUtils.copyProperties(dsm,dsml);
			dsml.setId(null);
			dsml.setDevice(device1);
			deviceSparepartMappingDao.save(dsml);
		}
		
		//添加点检项目
		List<DeviceProjectRecord> checklist=deviceProjectRecordDao.findByHQL("from DeviceProjectRecord dpr where dpr.device.id=?0 and dpr.type=?1", new Object[] {copyId,"SPOTINSPECTION"});
		for(DeviceProjectRecord check:checklist) {
			DeviceProjectRecord checkl=new DeviceProjectRecord();
			BeanUtils.copyProperties(check,checkl);
			checkl.setId(null);
			checkl.setCreateTime(new Date());
			checkl.setDevice(device1);
			deviceProjectRecordDao.save(checkl);
		}
		
		//添加保养项目
		List<DeviceProjectRecord> maintainlist=deviceProjectRecordDao.findByHQL("from DeviceProjectRecord dpr where dpr.device.id=?0 and dpr.type=?1", new Object[] {copyId,"MAINTAIN"});
		for(DeviceProjectRecord maintain:maintainlist) {
			DeviceProjectRecord maintainl=new DeviceProjectRecord();
			BeanUtils.copyProperties(maintain,maintainl);
			maintainl.setId(null);
			maintainl.setCreateTime(new Date());
			maintainl.setDevice(device1);
			deviceProjectRecordDao.save(maintainl);
		}
			
		//添加润滑项目
		List<DeviceProjectRecord> lubricationlist=deviceProjectRecordDao.findByHQL("from DeviceProjectRecord dpr where dpr.device.id=?0 and dpr.type=?1", new Object[] {copyId,"LUBRICATION"});
		for(DeviceProjectRecord lub:lubricationlist) {
			DeviceProjectRecord luble=new DeviceProjectRecord();
			BeanUtils.copyProperties(lub,luble);
			luble.setId(null);
			luble.setCreateTime(new Date());
			luble.setDevice(device1);
			deviceProjectRecordDao.save(luble);
		}
	}

	@Override
	public List<Device> queryDevicesByProductionUnitIdAndModule(Long id, String module) {
		switch(module) {
		case "dimes":
			return this.deviceDao.findByHQL("from Device d where d.productionUnit.id=?0 and d.isDimesUse=?1", 
					new Object[] {id,true});
		case "deviceManage":
			return this.deviceDao.findByHQL("from Device d where d.productionUnit.id=?0 and d.isDeviceManageUse=?1", 
					new Object[] {id,true}); 
		}
		return null;
	}
	@Override
	public void deleteMgrDevices(String[] mgrDeviceIds) {
		for(int i = 0;i<mgrDeviceIds.length;i++) {
			Long deviceId = Long .valueOf(mgrDeviceIds[i]);
			deviceSiteDao.deleteByDeviceId(deviceId);
			deleteObj(deviceId);
		}
	}
	/**
	 * 导入工件工序 参数信息
	 * @param file
	 */
	@Override
	public void importWorkpieceProcessParameters(MultipartFile file) {
		ExcelUtil excelUtil = new WorkpieceProcessParameterExcelUtil(parameterTypeService,processesService,inventoryService,parameterService,inventoryProcessMappingService,workpieceProcessParameterMappingService);
		excelUtil.getData(file);
	}
}
