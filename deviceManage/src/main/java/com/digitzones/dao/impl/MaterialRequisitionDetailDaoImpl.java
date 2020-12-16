package com.digitzones.dao.impl;
import com.digitzones.dao.IMaterialRequisitionDetailDao;
import com.digitzones.model.MaterialRequisitionDetail;
import org.springframework.stereotype.Repository;
@Repository
public class MaterialRequisitionDetailDaoImpl extends CommonDaoImpl<MaterialRequisitionDetail> implements IMaterialRequisitionDetailDao {
    public MaterialRequisitionDetailDaoImpl() {
        super(MaterialRequisitionDetail.class);
    }
    /**
     * 根据领料单号删除领料单详情
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        getSession().createNativeQuery("delete from MaterialRequisitionDetail where MATERIAL_REQUISITION_FORMNO=?0")
                .setParameter(0,formNo).executeUpdate();
    }
    /**
     * 更新领料数量到初始状态
     * @param id 报工详情id
     */
    @Override
    public void updateSurplusNum(String id) {
        String sql = "update MaterialRequisitionDetail  set SurplusNum=amount where barCode in (select barCode from boxbar where fkey=?0)";
        getSession().createNativeQuery(sql).setParameter(0,id).executeUpdate();
    }
}
