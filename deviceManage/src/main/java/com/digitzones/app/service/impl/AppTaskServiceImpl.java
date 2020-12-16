package com.digitzones.app.service.impl;

import com.digitzones.app.dao.IAppTaskDao;
import com.digitzones.app.model.Task;
import com.digitzones.app.service.IAppTaskService;
import com.digitzones.model.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
@Service
public class AppTaskServiceImpl implements IAppTaskService {

	@Autowired
	IAppTaskDao TaskDao;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return TaskDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(Task obj) {
		TaskDao.update(obj);
		
	}

	@Override
	public Task queryByProperty(String name, String value) {
		return TaskDao.findSingleByProperty(name, value);
	}
	
	@Override
	public List<Task> queryListByProperty(String name, String value) {
		return TaskDao.findListByProperty(name, value);
	}

	@Override
	public Serializable addObj(Task obj) {
		return TaskDao.save(obj);
	}

	@Override
	public Task queryObjById(Serializable id) {
		return TaskDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		TaskDao.deleteById(id);
	}

	/*@Override
	public void deleteObj(Long id) {
		TaskDao.deleteById(id);
	}*/

	@Override
	public Serializable addTask(Task Task) {
		return TaskDao.addTask(Task);
	}

	@Override
	public List<Task> queryTaskByStatusAndUserId(String status, Long userId) {
		return TaskDao.queryTaskByStatusAndUserId(status, userId);
	}

	@Override
	public List<Task> queryTaskByCreateUserid(Long userId) {
		return TaskDao.queryTaskByCreateUserid(userId);
	}

	@Override
	public List<Task> queryTaskByTreatUserid(Long userId) {
		return TaskDao.queryTaskByTreatUserid(userId);
	}

	@Override
	public List<Task> queryTaskByCondition(String startDate, String endDate, String status, String content) {
		String hql="from Task t where 1=1 ";
		List<Object> list=new ArrayList<Object>();
		int i=0;
		try {
			if(startDate!=null&&!startDate.equals("")) {
				hql+=" and createtime>?"+i;
				list.add(sdf.parse(startDate));
				i++;
			}
			
			if(endDate!=null&&!endDate.equals("")) {
				hql+=" and createtime<?"+i;
				list.add(sdf.parse(endDate));
				i++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(status!=null&&!status.equals("")) {
			hql+=" and status=?"+i;
			list.add(status);
			i++;
		}
		if(content!=null&&!content.equals("")){
			hql+=" and (status like '%'+?"+i+"+'%' or description like '%'+?"+(i+1)+"+'%' or manageType like '%'+?"+(i+2)+"+'%' or userName like '%'+?"+(i+3)+"+'%' or treatDescription like '%'+?"+(i+4)+"+'%' or createUserName like '%'+?"+(i+5)+"+'%')";
			list.add(content);
			list.add(content);
			list.add(content);
			list.add(content);
			list.add(content);
			list.add(content);
		}
		return TaskDao.findByHQL(hql, list.toArray(new Object[0]));
	}

	
}
