package com.digitzones.devmgr.service.impl;

import com.digitzones.devmgr.dao.ICheckingPlanRecordItemFilesDao;
import com.digitzones.devmgr.model.CheckingPlanRecordItemFiles;
import com.digitzones.devmgr.service.ICheckingPlanRecordItemFilesService;
import com.digitzones.model.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class CheckingPlanRecordItemFilesServiceImpl implements ICheckingPlanRecordItemFilesService {
    @Autowired
    private ICheckingPlanRecordItemFilesDao checkingPlanRecordItemFilesDao;
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
        return checkingPlanRecordItemFilesDao.findByPage(hql, pageNo, pageSize, values);
    }

    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(CheckingPlanRecordItemFiles obj) {
        checkingPlanRecordItemFilesDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public CheckingPlanRecordItemFiles queryByProperty(String name, String value) {
        return checkingPlanRecordItemFilesDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(CheckingPlanRecordItemFiles obj) {
        return checkingPlanRecordItemFilesDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public CheckingPlanRecordItemFiles queryObjById(Serializable id) {
        return checkingPlanRecordItemFilesDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        checkingPlanRecordItemFilesDao.deleteById(id);
    }
    /**
     * 根据点检项id查找该点检项对应的上传文件
     * @param itemId
     * @return
     */
    @Override
    public List<CheckingPlanRecordItemFiles> queryCheckingPlanRecordItemFilesByItemId(Long itemId) {
        return checkingPlanRecordItemFilesDao.findByHQL("from CheckingPlanRecordItemFiles itemFile where itemFile.checkingPlanRecordItem.id=?0",
                new Object[]{itemId});
    }
}
