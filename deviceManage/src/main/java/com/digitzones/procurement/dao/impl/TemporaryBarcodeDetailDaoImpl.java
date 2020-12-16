package com.digitzones.procurement.dao.impl;
import com.digitzones.dao.impl.CommonDaoImpl;
import com.digitzones.procurement.dao.ITemporaryBarcodeDetailDao;
import com.digitzones.procurement.model.TemporaryBarcodeDetail;
import org.springframework.stereotype.Repository;
@Repository
public class TemporaryBarcodeDetailDaoImpl extends CommonDaoImpl<TemporaryBarcodeDetail> implements ITemporaryBarcodeDetailDao {
    public TemporaryBarcodeDetailDaoImpl() {
        super(TemporaryBarcodeDetail.class);
    }
    @Override
    public void deleteByFormNo(String formNo) {
        String sql = "delete from TemporaryBarcodeDetail where TEMPORARYBARCODE_CODE=?0";
        getSession().createNativeQuery(sql).setParameter(0,formNo).executeUpdate();
    }
}
