package com.digitzones.app.service;

import java.util.List;

import com.digitzones.app.model.UserTask;
import com.digitzones.service.ICommonService;

public interface IAppUserTaskService extends ICommonService<UserTask>{
	
	public List<UserTask> queryListByTaskId(Long taskId);
	
	public UserTask queryByTaskIdAndUserId(Long taskId, Long userId);

}
