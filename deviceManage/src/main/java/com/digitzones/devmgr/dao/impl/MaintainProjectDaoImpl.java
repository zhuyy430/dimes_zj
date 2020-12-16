package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IMaintainProjectDao;
import com.digitzones.devmgr.model.MaintainProject;
@Repository
public class MaintainProjectDaoImpl extends CommonDaoImpl<MaintainProject> implements IMaintainProjectDao {
	public MaintainProjectDaoImpl() {
		super(MaintainProject.class);
	}
}
