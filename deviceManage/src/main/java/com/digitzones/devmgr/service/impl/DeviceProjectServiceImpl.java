package com.digitzones.devmgr.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.digitzones.devmgr.dao.IDeviceProjectDao;
import com.digitzones.devmgr.model.DeviceProject;
import com.digitzones.devmgr.service.IDeviceProjectService;
import com.digitzones.model.Pager;

/**
 * 保养项目数据访问接口
 * @author Administrator
 *
 */
@Service
public class DeviceProjectServiceImpl implements IDeviceProjectService {
	
	@Autowired
	IDeviceProjectDao DeviceProjectDao;

	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return DeviceProjectDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(DeviceProject obj) {
		DeviceProjectDao.update(obj);
		
	}

	@Override
	public DeviceProject queryByProperty(String name, String value) {
		return DeviceProjectDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(DeviceProject obj) {
		// TODO Auto-generated method stub
		return DeviceProjectDao.save(obj);
	}

	@Override
	public DeviceProject queryObjById(Serializable id) {
		return DeviceProjectDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		DeviceProjectDao.deleteById(id);
		
	}

	@Override
	public List<DeviceProject> queryAllDeviceProjectByType(String type) {
		return	DeviceProjectDao.findListByProperty("type", type);
	}

	@Override
	public DeviceProject queryDeviceProjectByCodeAndType(String code, String type) {
		List<DeviceProject> deviceprojects = DeviceProjectDao.findByHQL("from DeviceProject d where d.code=?0 and d.type=?1 ",new Object[] {code,type});
		if(!deviceprojects.isEmpty()&&deviceprojects.size()==1){
			return deviceprojects.get(0);
		}
		return null;
	}

	@Override
	public boolean queryDeviceProjectByProjectTypeId(Long projectTypeId) {
		List<DeviceProject> deviceprojects = DeviceProjectDao.findByHQL("select dp from DeviceProject dp inner join dp.projectType p where p.id=?0 ",new Object[] {projectTypeId});
		if(!deviceprojects.isEmpty()&&deviceprojects.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public List<DeviceProject> queryAllDeviceProjectByProjectTypeId(Long projectTypeId) {
		return DeviceProjectDao.findByHQL("select dp from DeviceProject dp inner join dp.projectType p where p.id=?0 ",new Object[] {projectTypeId});
	}

	@Override
	public void imports(List<DeviceProject> dataList) {
		if(CollectionUtils.isEmpty(dataList)) {
			throw new RuntimeException("无导入数据!");
		}
		for(DeviceProject deviceProject : dataList) {
			List<DeviceProject> d = DeviceProjectDao.findByHQL("select dp from DeviceProject dp where dp.code=?0 and dp.type=?1",new Object[] {deviceProject.getCode(),deviceProject.getType()});
			if(!d.isEmpty()&&d!=null){
				throw new RuntimeException("导入失败，项目编号已经存在!");
			}
			DeviceProjectDao.save(deviceProject);
		}
	}

	@Override
	public List<DeviceProject> queryDevicesProjectByProjectTypeIdNotPage(String type, Long projectTypeId) {
		String hql = "select dp from DeviceProject dp inner join dp.projectType p where dp.type='" + type +"'";
		if(projectTypeId!=null && projectTypeId>0) {
			hql +=" and p.id=" + projectTypeId;
		}
		List<DeviceProject> d = DeviceProjectDao.findByHQL(hql,new Object[] {});
		return d;
	}

}
