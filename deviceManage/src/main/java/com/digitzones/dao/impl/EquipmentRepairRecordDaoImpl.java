package com.digitzones.dao.impl;

import com.digitzones.dao.IEquipmentRepairRecordDao;
import com.digitzones.model.EquipmentRepairRecord;
import org.springframework.stereotype.Repository;

@Repository
public class EquipmentRepairRecordDaoImpl extends CommonDaoImpl<EquipmentRepairRecord> implements IEquipmentRepairRecordDao {

	public EquipmentRepairRecordDaoImpl() {
		super(EquipmentRepairRecord.class);
	}

}
