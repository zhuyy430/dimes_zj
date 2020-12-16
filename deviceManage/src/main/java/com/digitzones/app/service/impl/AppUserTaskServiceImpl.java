package com.digitzones.app.service.impl;

import com.digitzones.app.dao.IAppUserTaskDao;
import com.digitzones.app.model.UserTask;
import com.digitzones.app.service.IAppUserTaskService;
import com.digitzones.model.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
@Service
public class AppUserTaskServiceImpl implements IAppUserTaskService {

	@Autowired
	IAppUserTaskDao UserTaskDao;

	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateObj(UserTask obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public UserTask queryByProperty(String name, String value) {
		return UserTaskDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(UserTask obj) {
		return UserTaskDao.save(obj);
	}

	@Override
	public UserTask queryObjById(Serializable id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteObj(Serializable id) {

	}

	@Override
	public List<UserTask> queryListByTaskId(Long taskId) {
		//写sql语句
		return UserTaskDao.queryByTaskId(taskId);
	}

	@Override
	public UserTask queryByTaskIdAndUserId(Long taskId, Long userId) {
		List<UserTask> list = UserTaskDao.queryByTaskIdAndUserId(taskId, userId);
		if(!list.isEmpty()&&list.size()==1){
			return list.get(0);
		}
		return null;
		
	} 
}
