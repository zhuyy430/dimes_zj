package com.digitzones.procurement.dao.impl;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.ITemporaryBarcodeDao;
import com.digitzones.procurement.model.TemporaryBarcode;
import org.springframework.stereotype.Repository;
import java.util.Calendar;
import java.util.Date;
@Repository
public class TemporaryBarcodeDaoImpl extends CommonDaoImpl<TemporaryBarcode> implements ITemporaryBarcodeDao {
    public TemporaryBarcodeDaoImpl() {
        super(TemporaryBarcode.class);
    }
    /**
     * 根据收料日期查找最大入库申请单单号
     * @param date 收料日期
     * @return 最大入库申请单单号
     */
    @Override
    public String queryMaxRequestNoByBillDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (String) getSession().createNativeQuery("select MAX(formNo) from TemporaryBarcode where year(billDate)=?0" +
                " and month(billDate)=?1 and day(billDate)=?2")
                .setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
                .setParameter(2,c.get(Calendar.DATE)).uniqueResult();
    }
}
