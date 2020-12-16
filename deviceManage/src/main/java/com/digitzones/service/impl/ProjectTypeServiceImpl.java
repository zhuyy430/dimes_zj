package com.digitzones.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.dao.IProjectTypeDao;
import com.digitzones.model.Pager;
import com.digitzones.model.ProjectType;
import com.digitzones.service.IProjectTypeService;
@Service
public class ProjectTypeServiceImpl implements IProjectTypeService {
	private IProjectTypeDao projectTypeDao;
	@Autowired
	public void setProjectTypeDao(IProjectTypeDao projectTypeDao) {
		this.projectTypeDao = projectTypeDao;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return projectTypeDao.findByPage(hql, pageNo, pageSize, values);
	}
	@Override
	public void updateObj(ProjectType obj) {
		projectTypeDao.update(obj);
	}
	@Override
	public ProjectType queryByProperty(String name, String value) {
		return projectTypeDao.findSingleByProperty(name, value);
	}
	@Override
	public Serializable addObj(ProjectType obj) {
		return projectTypeDao.save(obj);
	}
	@Override
	public ProjectType queryObjById(Serializable id) {
		return projectTypeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		projectTypeDao.deleteById(id);
	}
	@Override
	public List<ProjectType> queryAllProjectType() {
		return projectTypeDao.findByHQL("from ProjectType dt ",new Object[] {});
	}
	@Override
	public List<ProjectType> queryTopProjectTypes(String rootType) {
		return projectTypeDao.findByHQL("from ProjectType dt where  dt.disabled=?0 and dt.code=?1 order by dt.code",new Object[] {false,rootType});
	}
	@Override
	public List<ProjectType> queryProjectTypesByType(String type) {
		return projectTypeDao.findByHQL("from ProjectType dt where  dt.disabled=?0 and dt.type=?1 order by dt.code",new Object[] {false,type});
	}
	@Override
	public List<ProjectType> queryProjectTypeByParentId(Long Id) {
		return projectTypeDao.findByHQL("from ProjectType dt where  dt.disabled=?0 and dt.parent.id=?1 order by dt.code",new Object[] {false,Id});
	}
	@Override
	public ProjectType queryProjectTypeByCodeAndType(String code, String type) {
		List<ProjectType> projectTypeList = projectTypeDao.findByHQL("from ProjectType dt where  dt.disabled=?0 and dt.code=?1 and dt.type=?2",new Object[] {false,code,type});
		if(!projectTypeList.isEmpty()&&projectTypeList!=null){
			return projectTypeList.get(0);
		}
		return null;
	}
}
