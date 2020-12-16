package com.digitzones.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.IMeasuringToolDao;
import com.digitzones.model.Equipment;
@Repository
public class MeasuringToolDaoImpl extends CommonDaoImpl<Equipment> implements IMeasuringToolDao {

	public MeasuringToolDaoImpl() {
		super(Equipment.class);
	}

	@Override
	public Serializable addMeasuringTool(Equipment equipment, File pic) {
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
	public void updateMeasuringTool(Equipment equipment, File pic) {
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
