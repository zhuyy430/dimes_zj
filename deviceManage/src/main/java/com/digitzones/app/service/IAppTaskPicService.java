package com.digitzones.app.service;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import com.digitzones.app.model.TaskPic;
import com.digitzones.service.ICommonService;

public interface IAppTaskPicService extends ICommonService<TaskPic>{
	
	public Serializable addTaskPic(TaskPic taskPic, File file);
	
	public List<TaskPic> queryListByProperty(Long taskId);

}
