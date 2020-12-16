package com.digitzones.procurement.dao.impl;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.IWarehousingApplicationFormDetailDao;
import com.digitzones.procurement.model.WarehousingApplicationFormDetail;
import org.springframework.stereotype.Repository;
@Repository
public class WarehousingApplicationFormDetailDaoImpl extends CommonDaoImpl<WarehousingApplicationFormDetail> implements IWarehousingApplicationFormDetailDao {
    public WarehousingApplicationFormDetailDaoImpl() {
        super(WarehousingApplicationFormDetail.class);
    }

    @Override
    public void deleteByFormNo(String formNo) {
        String sql = "delete from WarehousingApplicationFormDetail where WAREHOUSINGAPPLICATIONFORM_CODE=?0";
        getSession().createNativeQuery(sql).setParameter(0,formNo).executeUpdate();
    }
}
