package com.digitzones.devmgr.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.devmgr.dao.IDeviceRepairOrderPicDao;
import com.digitzones.devmgr.model.DeviceRepairPic;
@Repository
public class DeviceRepairOrderPicDaoImpl extends CommonDaoImpl<DeviceRepairPic> implements IDeviceRepairOrderPicDao {
	public DeviceRepairOrderPicDaoImpl() {
		super(DeviceRepairPic.class);
	}

	@Override
	public Serializable addOrderPic(DeviceRepairPic deviceRepairPic, File pic) {
		if(pic!=null && pic.exists()) {
			FileInputStream input = null;
			try {
				input = new FileInputStream(pic);
				deviceRepairPic.setPic(Hibernate.getLobCreator(getSession()).createBlob(input, input.available()));
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		return this.save(deviceRepairPic);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public List<DeviceRepairPic> findByTaskId(Long deviceRepairId) {
		String sql = "select atp.id,atp.pic,atp.picName,atp.orderid from DEVICEREPAIRPIC atp where atp.orderid=?0";
		return getSession().createSQLQuery(sql).setParameter(0, deviceRepairId)
				.addEntity(DeviceRepairPic.class).list();
	}
	
	@Override
	public Serializable addDeviceRepairOrderPic(Long orderId,File file) {
		DeviceRepairPic d = new DeviceRepairPic();
		if(file!=null && file.exists()) {
			FileInputStream stream = null;
			try {
				stream = new FileInputStream(file);
				d.setOrderId(orderId);
				d.setPic(Hibernate.getLobCreator(getSession()).createBlob(stream, stream.available()));
				d.setPicName("console/deviceImgs/" + file.getName());
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		return  this.save(d);
	}

}
