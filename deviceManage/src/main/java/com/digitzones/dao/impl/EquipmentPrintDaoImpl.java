package com.digitzones.dao.impl;

import com.digitzones.dao.IEquipmentPrintDao;
import com.digitzones.model.EquipmentPrint;
import org.springframework.stereotype.Repository;

/**
 * @author xuxf
 * @date 2020-5-6
 * @time 13:25
 */
@Repository
public class EquipmentPrintDaoImpl extends CommonDaoImpl<EquipmentPrint> implements IEquipmentPrintDao {
    public EquipmentPrintDaoImpl() {
        super(EquipmentPrint.class);
    }
}
