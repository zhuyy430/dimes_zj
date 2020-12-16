package com.digitzones.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.IPositionDao;
import com.digitzones.model.Position;
@Repository
public class PositionDaoImpl extends CommonDaoImpl<Position> implements IPositionDao {
	public PositionDaoImpl() {
		super(Position.class);
	}

	@Override
	public int queryEmployeeCountById(Long id) {
		String sql = "select count(id) from Employee e where e.position_id=?0";
		return (int) getSession().createNativeQuery(sql).setParameter(0, id).uniqueResult();
	}
}
