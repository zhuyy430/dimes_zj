package com.digitzones.service.impl;

import com.digitzones.dao.IRawMaterialDao;
import com.digitzones.model.Pager;
import com.digitzones.model.RawMaterial;
import com.digitzones.service.IRawMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class RawMaterialServiceImpl implements IRawMaterialService {
    @Autowired
    private IRawMaterialDao rawMaterialDao;
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
        return rawMaterialDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(RawMaterial obj) {
        rawMaterialDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public RawMaterial queryByProperty(String name, String value) {
        return rawMaterialDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(RawMaterial obj) {
        return rawMaterialDao.save(obj);
    }
    /**
     * 根据id查询对象
     *
     * @param id
     * @return
     */
    @Override
    public RawMaterial queryObjById(Serializable id) {
        return rawMaterialDao.findById(id);
    }

    /**
     * 根据id删除对象
     *
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        rawMaterialDao.deleteById(id);
    }
    /**
     * 根据报工详情id查找原材料信息
     * @param jobBookingDetailId
     * @return
     */
    @Override
    public List<RawMaterial> queryByJobBookingDetailId(String jobBookingDetailId) {
        return rawMaterialDao.findByHQL("from RawMaterial rm where rm.jobBookingFormDetail.id=?0",new Object[]{jobBookingDetailId});
    }
}
