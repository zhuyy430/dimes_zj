package com.digitzones.dao.impl;
import com.digitzones.dao.IInspectionRecordDetailDao;
import com.digitzones.model.InspectionRecordDetail;
import org.springframework.stereotype.Repository;

@Repository
public class InspectionRecordDetailDaoImpl extends CommonDaoImpl<InspectionRecordDetail> implements IInspectionRecordDetailDao {
    public InspectionRecordDetailDaoImpl() {
        super(InspectionRecordDetail.class);
    }
    /**
     * 根据检验单号删除检验单详情
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        getSession().createNativeQuery("delete from InspectionRecordDetail where INSPECTION_RECORD_FORMNO=?0")
                .setParameter(0,formNo).executeUpdate();
    }
}
