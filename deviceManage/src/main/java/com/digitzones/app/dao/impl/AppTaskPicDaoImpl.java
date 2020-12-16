package com.digitzones.app.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.digitzones.app.dao.IAppTaskPicDao;
import com.digitzones.app.model.TaskPic;
import com.digitzones.dao.impl.CommonDaoImpl;
@Repository
public class AppTaskPicDaoImpl extends CommonDaoImpl<TaskPic> implements IAppTaskPicDao {

	public AppTaskPicDaoImpl() {
		super(TaskPic.class);
	}

	@Override
	public Serializable addTaskPic(TaskPic taskPic, File pic) {
		if(pic!=null && pic.exists()) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(pic);
				taskPic.setPic(Hibernate.getLobCreator(getSession()).createBlob(input, input.available()));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		return this.save(taskPic);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<TaskPic> findByTaskId(Long taskId) {
		String sql = "select atp.id,atp.pic,atp.picName,atp.taskid from APP_TASK_PIC atp where atp.taskid=?0";
		return getSession().createSQLQuery(sql).setParameter(0, taskId)
				.addEntity(TaskPic.class).list();
	}

}
