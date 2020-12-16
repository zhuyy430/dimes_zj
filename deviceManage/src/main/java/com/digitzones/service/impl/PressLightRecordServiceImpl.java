package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IDeviceSiteDao;
import com.digitzones.dao.IPressLightRecordDao;
import com.digitzones.model.DeviceSite;
import com.digitzones.model.Pager;
import com.digitzones.model.PressLightRecord;
import com.digitzones.model.User;
import com.digitzones.service.IPressLightRecordService;
@Service("pressLightRecordService")
public class PressLightRecordServiceImpl implements IPressLightRecordService {
	private IPressLightRecordDao pressLightRecordDao;
	private IDeviceSiteDao deviceSiteDao;
	@Autowired
	public void setDeviceSiteDao(IDeviceSiteDao deviceSiteDao) {
		this.deviceSiteDao = deviceSiteDao;
	}

	@Autowired
	public void setPressLightRecordDao(IPressLightRecordDao pressLightRecordDao) {
		this.pressLightRecordDao = pressLightRecordDao;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return pressLightRecordDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(PressLightRecord obj) {
		//停机
		if(obj.getHalt()) {
			DeviceSite deviceSite = deviceSiteDao.findById(obj.getDeviceSite().getId());
			deviceSite.setStatus(Constant.DeviceSite.HALT);
			deviceSiteDao.update(deviceSite);
		}else {
			DeviceSite deviceSite = deviceSiteDao.findById(obj.getDeviceSite().getId());
			deviceSite.setStatus(Constant.DeviceSite.RUNNING);
			deviceSiteDao.update(deviceSite);
		}
		pressLightRecordDao.update(obj);
	}

	@Override
	public PressLightRecord queryByProperty(String name, String value) {
		return pressLightRecordDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(PressLightRecord obj) {
		//停机
		if(obj.getHalt()) {
			DeviceSite deviceSite = deviceSiteDao.findById(obj.getDeviceSite().getId());
			deviceSite.setStatus(Constant.DeviceSite.HALT);
			deviceSiteDao.update(deviceSite);
		}
		return pressLightRecordDao.save(obj);
	}

	@Override
	public PressLightRecord queryObjById(Serializable id) {
		return pressLightRecordDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		pressLightRecordDao.deleteById(id);
	}

	@Override
	public Long queryCountByPressLightTime(Date pressLightTime) {
		Long count = pressLightRecordDao.queryCountByPressLightTime(pressLightTime);
		return count==null?0:count;
	}

	@Override
	public List<PressLightRecord> queryPressLightRecordsByTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String hql = "from PressLightRecord plr where year(plr.pressLightTime)=?0 and month(plr.pressLightTime)=?1 and day(plr.pressLightTime)=?2";
		return  pressLightRecordDao.findByHQL(hql, new Object[] {c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DATE)});
	}

	@Override
	public Serializable addPressLightRecord(PressLightRecord pressLightRecord, User user, Map<String, Object> args) {
		if(user!=null) {
			if(pressLightRecord.getPressLightTime()!=null){
				pressLightRecord.setPressLightTime(pressLightRecord.getPressLightTime());
			}else{
				pressLightRecord.setPressLightTime(new Date());
			}
			pressLightRecord.setPressLightUserId(user.getId());
			pressLightRecord.setPressLightUserName(user.getUsername());
		}
		return pressLightRecordDao.save(pressLightRecord);
	}

	@Override
	public void recoverPressLightRecord(PressLightRecord pressLightRecord, User user, Map<String, Object> args) {
		pressLightRecord.setRecoverTime(new Date());
		if(args.get("suggestion").toString()!=null&&!args.get("suggestion").toString().equals("")){
			pressLightRecord.setRecoverMethod(args.get("suggestion").toString());
		}
		if(user!=null) {
			pressLightRecord.setRecoverUserId(user.getId());
			pressLightRecord.setRecoverUserName(user.getUsername());
		}
		pressLightRecordDao.update(pressLightRecord);
	}

	@Override
	public void lightoutPressLightRecord(PressLightRecord pressLightRecord, User user, Map<String, Object> args) {
		pressLightRecord.setLightOutTime(new Date());
		if(user!=null) {
			pressLightRecord.setLightOutUserId(user.getId());
			pressLightRecord.setLightOutUserName(user.getUsername());
		}
		pressLightRecordDao.update(pressLightRecord);
	}

	@Override
	public void confirmPressLightRecord(PressLightRecord pressLightRecord, User user, Map<String, Object> args) {
		pressLightRecord.setConfirmTime(new Date());
		if(user!=null) {
			pressLightRecord.setConfirmUserId(user.getId());
			pressLightRecord.setConfirmUserName(user.getUsername());
		}
		pressLightRecordDao.update(pressLightRecord);
	}

	@Override
	public void deletePressLightRecord(PressLightRecord pressLightRecord) {
		pressLightRecord.setDeleted(true);
		pressLightRecordDao.update(pressLightRecord);
	}
}
