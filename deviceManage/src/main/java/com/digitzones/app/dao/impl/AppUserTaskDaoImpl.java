package com.digitzones.app.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.app.dao.IAppUserTaskDao;
import com.digitzones.app.model.UserTask;
import com.digitzones.dao.impl.CommonDaoImpl;
@Repository
public class AppUserTaskDaoImpl extends CommonDaoImpl<UserTask> implements IAppUserTaskDao {

	public AppUserTaskDaoImpl() {
		super(UserTask.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<UserTask> queryByTaskId(Long taskId) {
		String sql = "select aut.id,aut.userid,aut.taskid from APP_USER_TASK aut where aut.taskid=?0";
		return getSession().createSQLQuery(sql).setParameter(0, taskId)
				.addEntity(UserTask.class).list();
	}
	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<UserTask> queryByTaskIdAndUserId(Long taskId,Long userId) {
		String sql = "select aut.id,aut.userid,aut.taskid from APP_USER_TASK aut where aut.taskid=?0 and aut.userid=?1";
		return getSession().createSQLQuery(sql).setParameter(0, taskId).setParameter(1, userId)
				.addEntity(UserTask.class).list();
	}

}
