package com.digitzones.app.service.impl;

import com.digitzones.app.dao.IAppTaskPicDao;
import com.digitzones.app.model.TaskPic;
import com.digitzones.app.service.IAppTaskPicService;
import com.digitzones.model.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.List;
@Service
public class AppTaskPicServiceImpl implements IAppTaskPicService {

	@Autowired
	IAppTaskPicDao TaskPicDao;

	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateObj(TaskPic obj) {
		TaskPicDao.update(obj);
		
	}

	@Override
	public TaskPic queryByProperty(String name, String value) {
		return TaskPicDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(TaskPic obj) {
		return TaskPicDao.save(obj);
	}

	@Override
	public TaskPic queryObjById(Serializable id) {
		return TaskPicDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		TaskPicDao.deleteById(id);
	}

	/*@Override
	public void deleteObj(Long id) {
		TaskPicDao.deleteById(id);		
	}*/

	@Override
	public Serializable addTaskPic(TaskPic taskPic, File file) {
		return TaskPicDao.addTaskPic(taskPic,file);
	}

	@Override
	public List<TaskPic> queryListByProperty(Long taskId) {
		return TaskPicDao.findByTaskId(taskId);
	}

}
