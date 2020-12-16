package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.devmgr.dao.IMaintainProjectDao;
import com.digitzones.devmgr.model.MaintainProject;
import com.digitzones.devmgr.service.IMaintainProjectService;
import com.digitzones.model.Pager;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class MaintainProjectServiceImpl implements IMaintainProjectService {

	@Autowired
	IMaintainProjectDao MaintainProjectDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return MaintainProjectDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(MaintainProject obj) {
		MaintainProjectDao.update(obj);
	}

	@Override
	public MaintainProject queryByProperty(String name, String value) {
		return MaintainProjectDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(MaintainProject obj) {
		return MaintainProjectDao.save(obj);
	}

	@Override
	public MaintainProject queryObjById(Serializable id) {
		return MaintainProjectDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		MaintainProjectDao.deleteById(id);
	}

	@Override
	public List<MaintainProject> queryMaintainProject(Long deviceRepairOrderId) {
		String hql="from MaintainProject m where m.deviceRepair.id=?0";
		return MaintainProjectDao.findByHQL(hql, new Object[] {deviceRepairOrderId});
	}


}
