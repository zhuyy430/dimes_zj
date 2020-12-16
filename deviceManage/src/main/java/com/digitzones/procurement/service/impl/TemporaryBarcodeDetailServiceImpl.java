package com.digitzones.procurement.service.impl;
import com.digitzones.model.Pager;
import com.digitzones.procurement.dao.ITemporaryBarcodeDetailDao;
import com.digitzones.procurement.model.TemporaryBarcodeDetail;
import com.digitzones.procurement.service.ITemporaryBarcodeDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.List;
@Service
public class TemporaryBarcodeDetailServiceImpl implements ITemporaryBarcodeDetailService {
    @Autowired
    private ITemporaryBarcodeDetailDao temporaryBarcodeDetailDao;
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
        return temporaryBarcodeDetailDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(TemporaryBarcodeDetail obj) {
        temporaryBarcodeDetailDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public TemporaryBarcodeDetail queryByProperty(String name, String value) {
        return temporaryBarcodeDetailDao.findSingleByProperty(name,value);
    }

    /**
     * 添加对象
     *
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(TemporaryBarcodeDetail obj) {
        return temporaryBarcodeDetailDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public TemporaryBarcodeDetail queryObjById(Serializable id) {
        return temporaryBarcodeDetailDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        temporaryBarcodeDetailDao.deleteById(id);
    }
    /**
     * 根据入库申请单编号查找入库申请单详情信息
     *
     * @param requestNo 入库申请单编号
     * @return
     */
    @Override
    public List<TemporaryBarcodeDetail> queryByTemporaryBarcodeNo(String requestNo) {
        return temporaryBarcodeDetailDao.findByHQL("from TemporaryBarcodeDetail detail where detail.temporaryBarcode.formNo=?0",new Object[]{requestNo});
    }
}
