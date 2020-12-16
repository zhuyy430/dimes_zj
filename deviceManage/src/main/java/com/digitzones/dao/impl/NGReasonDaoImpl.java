package com.digitzones.dao.impl;

import org.springframework.stereotype.Repository;

import com.digitzones.dao.INGReasonDao;
import com.digitzones.model.NGReason;

import java.util.List;

@Repository
public class NGReasonDaoImpl extends CommonDaoImpl<NGReason> implements INGReasonDao {
	public NGReasonDaoImpl() {
		super(NGReason.class);
	}
	/**
	 * 查找所有不良原因大类
	 * @return
	 */
	@Override
	public List<String> queryAllCategories() {
		return getSession().createNativeQuery("select distinct category from NGReason").list();
	}
}
