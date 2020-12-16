package com.digitzones.dao.impl;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.IAppClientMapDao;
import com.digitzones.model.AppClientMap;
@Repository
public class AppClientMapDaoImpl extends CommonDaoImpl<AppClientMap> implements IAppClientMapDao {
	public AppClientMapDaoImpl() {
		super(AppClientMap.class);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> queryCids(List<String> usernames) {
		String hql = "select acm.cid from AppClientMap acm where acm.username in :usernames";
		List<String> cids = this.getSession().createQuery(hql).setParameterList("usernames", usernames).list();
		return cids;
	}
}
