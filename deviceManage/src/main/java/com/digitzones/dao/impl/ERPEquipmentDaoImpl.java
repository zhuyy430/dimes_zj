package com.digitzones.dao.impl;

import com.digitzones.dao.IERPEquipmentDao;
import com.digitzones.model.ERPEquipment;
import org.springframework.stereotype.Repository;

/**
 * @author xuxf
 * @date 2020-5-6
 * @time 12:46
 */
@Repository
public class ERPEquipmentDaoImpl extends CommonDaoImpl<ERPEquipment> implements IERPEquipmentDao {
    public ERPEquipmentDaoImpl() {
        super(ERPEquipment.class);
    }
}
