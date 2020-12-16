package com.digitzones.dao.impl;
import com.digitzones.dao.IRawMaterialDao;
import com.digitzones.model.RawMaterial;
import org.springframework.stereotype.Repository;
@Repository
public class RawMaterialDaoImpl extends CommonDaoImpl<RawMaterial> implements IRawMaterialDao {
    public RawMaterialDaoImpl() {
        super(RawMaterial.class);
    }

    /**
     * 根据报工单详情id删除原材料信息
     *
     * @param detailId
     */
    @Override
    public void deleteByJobBookingFormDetailId(String detailId) {
        getSession().createNativeQuery("delete from RawMaterial where JOBBOOKINGFORMDETAIL_ID=?0")
                .setParameter(0,detailId).executeUpdate();
    }
}
