package com.digitzones.procurement.dao.impl;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IWarehousingApplicationFormDao;
import com.digitzones.procurement.model.WarehousingApplicationForm;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class WarehousingApplicationFormDaoImpl extends CommonDaoImpl<WarehousingApplicationForm> implements IWarehousingApplicationFormDao {
    public WarehousingApplicationFormDaoImpl() {
        super(WarehousingApplicationForm.class);
    }
    /**
     * 根据收料日期查找最大入库申请单单号
     * @param date 收料日期
     * @return 最大入库申请单单号
     */
    @Override
    public String queryMaxRequestNoByReceivingDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return (String) getSession().createNativeQuery("select MAX(formNo) from WarehousingApplicationForm where year(receivingDate)=?0" +
                " and month(receivingDate)=?1 and day(receivingDate)=?2")
                .setParameter(0,c.get(Calendar.YEAR)).setParameter(1,c.get(Calendar.MONTH)+1)
                .setParameter(2,c.get(Calendar.DATE)).uniqueResult();
    }

    /**
     * 根据箱号条码查找入库申请单
     *
     * @param barCode
     * @return
     */
    @Override
    public WarehousingApplicationForm queryByBarCode(String barCode) {
        List<WarehousingApplicationForm> list = getSession().createNativeQuery("select form.* from BoxBar bar inner join WarehousingApplicationFormDetail detail on bar.fkey=detail.id" +
                " inner join WarehousingApplicationForm form on form.formNo=detail.WAREHOUSINGAPPLICATIONFORM_CODE where bar.barCode=?0")
                .setParameter(0, barCode)
                .addEntity(WarehousingApplicationForm.class)
                .list();
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return null;
    }
}
