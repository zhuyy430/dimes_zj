package com.digitzones.devmgr.dao.impl;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.ICheckingPlanRecordItemFilesDao;
import com.digitzones.devmgr.model.CheckingPlanRecordItemFiles;
import org.springframework.stereotype.Repository;

@Repository
public class CheckingPlanRecordItemFilesDaoImpl extends CommonDaoImpl<CheckingPlanRecordItemFiles> implements ICheckingPlanRecordItemFilesDao {
    public CheckingPlanRecordItemFilesDaoImpl() {
        super(CheckingPlanRecordItemFiles.class);
    }
}
