package com.digitzones.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.INGProcessMethodDao;
import com.digitzones.model.NGProcessMethod;
@Repository
public class NGProcessMethodDaoImpl extends CommonDaoImpl<NGProcessMethod> implements INGProcessMethodDao {
	public NGProcessMethodDaoImpl() {
		super(NGProcessMethod.class);
	}
}
