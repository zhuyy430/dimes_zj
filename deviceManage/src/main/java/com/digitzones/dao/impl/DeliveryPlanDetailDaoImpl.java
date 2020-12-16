package com.digitzones.dao.impl;
import com.digitzones.dao.IDeliveryPlanDetailDao;
import com.digitzones.model.DeliveryPlanDetail;
import org.springframework.stereotype.Repository;

@Repository
public class DeliveryPlanDetailDaoImpl extends CommonDaoImpl<DeliveryPlanDetail> implements IDeliveryPlanDetailDao {
    public DeliveryPlanDetailDaoImpl() {
        super(DeliveryPlanDetail.class);
    }
    @Override
    public void deleteByFormNo(String formNo) {
        String sql = "delete from DeliveryPlanDetail where DELIVERYPLAN_CODE=?0";
        getSession().createNativeQuery(sql).setParameter(0,formNo).executeUpdate();
    }
}
