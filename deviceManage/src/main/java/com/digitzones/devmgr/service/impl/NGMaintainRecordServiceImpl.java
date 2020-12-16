package com.digitzones.devmgr.service.impl;

import com.digitzones.devmgr.dao.INGMaintainRecordDao;
import com.digitzones.devmgr.model.NGMaintainRecord;
import com.digitzones.devmgr.service.INGMaintainRecordService;
import com.digitzones.model.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class NGMaintainRecordServiceImpl implements INGMaintainRecordService {

	@Autowired
	INGMaintainRecordDao NGMaintainRecordDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return NGMaintainRecordDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(NGMaintainRecord obj) {
		NGMaintainRecordDao.update(obj);
	}
	@Override
	public NGMaintainRecord queryByProperty(String name, String value) {
		// TODO Auto-generated method stub
		return NGMaintainRecordDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(NGMaintainRecord obj) {
		return NGMaintainRecordDao.save(obj);
	}
	@Override
	public NGMaintainRecord queryObjById(Serializable id) {
		return NGMaintainRecordDao.findById(id);
	}
	@Override
	public void deleteObj(Serializable id) {
		NGMaintainRecordDao.deleteById(id);
	}
	@Override
	public List<NGMaintainRecord> queryNGMaintainRecordByDeviceRepairOrderId(Long deviceRepairOrderId) {
		String hql="from NGMaintainRecord n where n.deviceRepair.id=?0";
		return NGMaintainRecordDao.findByHQL(hql, new Object[]{deviceRepairOrderId});
	}
	@Override
	public NGMaintainRecord queryNGMaintainRecordByDeviceRepairOrderIdAnddeviceProjectId(Long deviceRepairOrderId,Long deviceProjectId) {
		String hql="from NGMaintainRecord n where n.deviceRepair.id=?0 and n.deviceProject.id=?1";
		List<NGMaintainRecord> list = NGMaintainRecordDao.findByHQL(hql, new Object[]{deviceRepairOrderId,deviceProjectId});
		if(!CollectionUtils.isEmpty(list)){
			return list.get(0);
		}
		return null;
	}
	@Override
	public List<NGMaintainRecord> queryNGMaintainRecordByDeviceProjectId(Long deviceProjectId) {
		String hql = "select dr from NGMaintainRecord dr where dr.deviceProject.id=?0 ";
		return NGMaintainRecordDao.findByHQL(hql,new Object[]{deviceProjectId});
	}
}
