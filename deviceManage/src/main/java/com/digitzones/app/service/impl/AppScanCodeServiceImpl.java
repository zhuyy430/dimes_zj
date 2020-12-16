package com.digitzones.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitzones.app.dao.IAppScanCodeDao;
import com.digitzones.app.service.IAppScanCodeService;
@Service
public class AppScanCodeServiceImpl implements IAppScanCodeService {

	@Autowired
	IAppScanCodeDao appScanCodeDao;
	@Override
	public List<?> querySearch(String sql) {
		List<?> list = appScanCodeDao.findSearch(sql);
		return list;
	}

}
