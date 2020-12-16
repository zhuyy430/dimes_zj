package com.digitzones.service.impl;

import com.digitzones.dao.IInspectionRecordDetailDao;
import com.digitzones.model.InspectionRecordDetail;
import com.digitzones.model.Pager;
import com.digitzones.service.IInspectionRecordDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class InspectionRecordDetailServiceImpl implements IInspectionRecordDetailService {
    @Autowired
    private IInspectionRecordDetailDao materialRequisitionDetailDao;
    /**
     * 分页查询对象
     * @param hql
     * @param pageNo
     * @param pageSize
     * @param values
     * @return
     */
    @Override
    public Pager queryObjs(String hql, int pageNo, int pageSize, Object... values) {
        return materialRequisitionDetailDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(InspectionRecordDetail obj) {
        materialRequisitionDetailDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public InspectionRecordDetail queryByProperty(String name, String value) {
        return materialRequisitionDetailDao.findSingleByProperty(name,value);
    }

    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(InspectionRecordDetail obj) {
        return materialRequisitionDetailDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public InspectionRecordDetail queryObjById(Serializable id) {
        return materialRequisitionDetailDao.findById(id);
    }

    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        materialRequisitionDetailDao.deleteById(id);
    }

    /**
     * 根据领料单号查找领料单详情
     * @param formNo 领料单号
     * @return
     */
    @Override
    public List<InspectionRecordDetail> queryByFormNo(String formNo) {
        return materialRequisitionDetailDao.findByHQL("from InspectionRecordDetail detail where detail.inspectionRecord.formNo=?0 ",
                new Object[]{formNo});
    }
}
