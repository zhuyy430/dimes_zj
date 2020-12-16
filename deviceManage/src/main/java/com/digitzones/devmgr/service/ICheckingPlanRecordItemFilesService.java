package com.digitzones.devmgr.service;

import com.digitzones.devmgr.model.CheckingPlanRecordItemFiles;
import com.digitzones.service.ICommonService;

import java.util.List;

public interface ICheckingPlanRecordItemFilesService extends ICommonService<CheckingPlanRecordItemFiles> {
    /**
     * 根据点检项id查找该点检项对应的上传文件
     * @param itemId
     * @return
     */
    List<CheckingPlanRecordItemFiles> queryCheckingPlanRecordItemFilesByItemId(Long itemId);
}
