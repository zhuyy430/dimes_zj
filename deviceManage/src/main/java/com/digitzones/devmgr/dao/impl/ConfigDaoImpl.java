package com.digitzones.devmgr.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IConfigDao;
import com.digitzones.devmgr.model.Config;
@Repository
public class ConfigDaoImpl extends CommonDaoImpl<Config> implements IConfigDao {
	public ConfigDaoImpl() {
		super(Config.class);
	}
}
