package com.digitzones.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IEquipmentDao;
import com.digitzones.model.Equipment;
@Repository
public class EquipmentDaoImpl extends CommonDaoImpl<Equipment> implements IEquipmentDao {

	public EquipmentDaoImpl() {
		super(Equipment.class);
	}

	@Override
	public Serializable addEquipment(Equipment equipment, File pic) {
		if(pic!=null && pic.exists()) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(pic);
				equipment.setPic(Hibernate.getLobCreator(getSession()).createBlob(input, input.available()));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		return this.save(equipment);
	}
	@Override
	public void updateEquipment(Equipment equipment, File pic) {
		if(pic!=null && pic.exists()) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(pic);
				equipment.setPic(Hibernate.getLobCreator(getSession()).createBlob(input, input.available()));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		this.update(equipment);
	}
}
