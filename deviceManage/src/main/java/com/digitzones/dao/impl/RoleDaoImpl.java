package com.digitzones.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.IRoleDao;
import com.digitzones.model.Role;
@Repository
public class RoleDaoImpl extends CommonDaoImpl<Role> implements IRoleDao {
	public RoleDaoImpl() {
		super(Role.class);
	}

}
