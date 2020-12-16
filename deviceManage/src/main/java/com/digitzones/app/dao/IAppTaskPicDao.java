package com.digitzones.app.dao;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.digitzones.app.model.TaskPic;
import com.digitzones.dao.ICommonDao;

public interface IAppTaskPicDao extends ICommonDao<TaskPic>{

	public Serializable addTaskPic(TaskPic taskPic, File file);
	public List<TaskPic> findByTaskId(Long taskId);
}
