package com.digitzones.service.impl;

import com.digitzones.dao.IMaterialRequisitionDetailDao;
import com.digitzones.model.MaterialRequisitionDetail;
import com.digitzones.model.Pager;
import com.digitzones.service.IMaterialRequisitionDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class MaterialRequisitionDetailServiceImpl implements IMaterialRequisitionDetailService {
    @Autowired
    private IMaterialRequisitionDetailDao materialRequisitionDetailDao;
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
    public void updateObj(MaterialRequisitionDetail obj) {
        materialRequisitionDetailDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public MaterialRequisitionDetail queryByProperty(String name, String value) {
        return materialRequisitionDetailDao.findSingleByProperty(name,value);
    }

    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(MaterialRequisitionDetail obj) {
        return materialRequisitionDetailDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public MaterialRequisitionDetail queryObjById(Serializable id) {
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
    public List<MaterialRequisitionDetail> queryByFormNo(String formNo) {
        return materialRequisitionDetailDao.findByHQL("from MaterialRequisitionDetail detail where detail.materialRequisition.formNo=?0 ",
                new Object[]{formNo});
    }
    /**
     * 根据工单单号查找领料单详情
     * @param workSheetNo 生产工单单号
     * @return
     */
    @Override
    public List<MaterialRequisitionDetail> queryByWorkSheetNo(String workSheetNo) {
    	return materialRequisitionDetailDao.findByHQL("from MaterialRequisitionDetail detail where detail.materialRequisition.workSheet.no=?0 and detail.SurplusNum>0",
    			new Object[]{workSheetNo});
    }
    /**
     * 根据工单单号和箱条码查找领料单详情
     * @param workSheetNo 工单号
     * @param barCode 箱条码
     * @return
     */
    @Override
    public List<MaterialRequisitionDetail> queryByWorkSheetNoAndBarCode(String workSheetNo,String barCode) {
    	return materialRequisitionDetailDao.findByHQL("from MaterialRequisitionDetail detail where detail.materialRequisition.workSheet.no=?0"
    			+ " and detail.barCode=?1 ",
    			new Object[]{workSheetNo,barCode});
    }
    /**
     * 根据箱条码查找领料单详情
     * @param barCode 箱条码
     * @return
     */
    @Override
    public List<MaterialRequisitionDetail> queryByBarCode(String barCode) {
    	return materialRequisitionDetailDao.findByHQL("from MaterialRequisitionDetail detail where detail.barCode=?0 ",
    			new Object[]{barCode});
    }
}
