package com.digitzones.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.IEquipmentTypeDao;
import com.digitzones.model.EquipmentType;
@Repository
public class EquipmentTypeDaoImpl extends CommonDaoImpl<EquipmentType> implements IEquipmentTypeDao {
	public EquipmentTypeDaoImpl() {
		super(EquipmentType.class);
	}
}
