package com.digitzones.dao.impl;
import com.digitzones.dao.IMaterialRequisitionDao;
import com.digitzones.model.MaterialRequisition;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
@Repository
public class MaterialRequisitionDaoImpl extends CommonDaoImpl<MaterialRequisition> implements IMaterialRequisitionDao {
    public MaterialRequisitionDaoImpl() {
        super(MaterialRequisition.class);
    }
    /**
     * 根据领用日期查找最大领用单号
     * @param now
     * @return
     */
    @Override
    public String queryMaxFormNoByPickingDate(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        return (String) getSession().createNativeQuery("select MAX(formNo) from MaterialRequisition where year(pickingDate)=?0" +
                " and month(pickingDate)=?1 and day(pickingDate)=?2")
                .setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
                .setParameter(2,c.get(Calendar.DATE)).uniqueResult();
    }
}
