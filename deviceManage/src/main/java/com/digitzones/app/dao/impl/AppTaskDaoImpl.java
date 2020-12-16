package com.digitzones.app.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.digitzones.app.dao.IAppTaskDao;
import com.digitzones.app.model.Task;
import com.digitzones.dao.impl.CommonDaoImpl;
@Repository
public class AppTaskDaoImpl extends CommonDaoImpl<Task> implements IAppTaskDao {

	public AppTaskDaoImpl() {
		super(Task.class);
	}

	@Override
	public Serializable addTask(Task task) {
		return this.save(task);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<Task> queryTaskByStatusAndUserId(String status, Long userId) {
		/*String sql = "select t.id,t.Description,t.createUserid,t.createtime,t.manageType,t.pic,t.picName,t.userId,t.status,t.treatDescription,"
					+" t.treatUserid from APP_TASK t where t.status =?0 and t.userId=?1 order by t.createtime";*/
		String sql ="select at.id,at.status,at.description,at.manageType,at.userId,at.code,at.treatDescription,at.treatUserid,at.treatUserName,at.userName,"
				+ "at.createUserid,at.createUserName,at.createtime,at.createUserCode "
					+" from APP_TASK at left join APP_USER_TASK aut on at.id = aut.taskid where at.status=?0 and aut.userid =?1 ";
		return getSession().createSQLQuery(sql).setParameter(0, status).setParameter(1, userId)
				.addEntity(Task.class).list();
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<Task> queryTaskByCreateUserid(Long userId) {
		String sql = "select t.id,t.Description,t.createUserid,t.createUserName,t.createtime,t.manageType,t.userId,t.status,t.treatDescription,t.code,t.createUserCode,"
				+" t.treatUserid,t.treatUserName,t.userName from APP_TASK t where t.CreateUserid =?0 order by t.createtime";
		return getSession().createSQLQuery(sql).setParameter(0, userId)
				.addEntity(Task.class).list();
	}
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<Task> queryTaskByTreatUserid(Long userId) {
		String sql = "select t.id,t.Description,t.createUserid,t.createUserName,t.createtime,t.manageType,t.userId,t.status,t.treatDescription,"
				+" t.treatUserid,t.treatUserName,t.userName,t.code,t.createUserCode from APP_TASK t where t.treatUserid =?0 order by t.createtime";
		return getSession().createSQLQuery(sql).setParameter(0, userId)
				.addEntity(Task.class).list();
	}
	/*@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<Task> queryTaskByUserid(String status,Long userId) {
		String sql = "select t.id,t.Description,t.createUserid,t.createtime,t.manageType,t.pic,t.picName,t.userId,t.status,t.treatDescription,"
				+" t.treatUserid from APP_TASK t where t.userId=?0 and t.status =?1 order by t.createtime";
		return getSession().createSQLQuery(sql).setParameter(0, userId).setParameter(0, status)
				.addEntity(Task.class).list();
	}*/

}
