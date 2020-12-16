package com.digitzones.app.dao.impl;

import com.digitzones.app.dao.IAppMytaskViewDao;
import com.digitzones.app.model.MytaskView;
import com.digitzones.dao.impl.CommonDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AppMytaskViewDaoImpl extends CommonDaoImpl<MytaskView> implements IAppMytaskViewDao{

	public AppMytaskViewDaoImpl() {
		super(MytaskView.class);
	}

	@Override
	public List<MytaskView> queryMySendOutTaskByUsercode(String code) {
		String sql="select * from MYTASKVIEW where assignCode=?0 and status not in('已完成' ,'MAINTAINCOMPLETE' ,'MAINTAINCOMPLETE') order by cdate desc";
		List a = getSession().createSQLQuery(sql).setParameter(0, code)
				.addEntity(MytaskView.class).list();
		return a;
	}

	@Override
	public List<MytaskView> queryMyReceiveTaskByUsercode(String code) {
		String sql="select * from MYTASKVIEW where code=?0 and status not in('已完成' ,'MAINTAINCOMPLETE' ,'MAINTAINCOMPLETE') order by cdate desc";
		List a = getSession().createSQLQuery(sql).setParameter(0, code)
				.addEntity(MytaskView.class).list();
		return a;
	}

	@Override
	public List<MytaskView> queryMyCompleteTaskByUsercode(String code) {
		String sql="select * from MYTASKVIEW where (assignCode=?0 or code=?1) and status in('已完成' ,'MAINTAINCOMPLETE' ,'MAINTAINCOMPLETE') order by cdate desc";
		List a = getSession().createSQLQuery(sql).setParameter(0, code).setParameter(1, code)
				.addEntity(MytaskView.class).list();
		return a;
	}

}
