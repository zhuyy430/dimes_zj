package com.digitzones.service.impl;

import com.digitzones.dao.IJobBookingFormDetailDao;
import com.digitzones.model.Classes;
import com.digitzones.model.JobBookingFormDetail;
import com.digitzones.model.Pager;
import com.digitzones.service.IJobBookingFormDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Service
public class JobBookingFormDetailServiceImpl implements IJobBookingFormDetailService {
    @Autowired
    private IJobBookingFormDetailDao jobBookingFormDetailDao;
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
        return jobBookingFormDetailDao.findByPage(hql, pageNo, pageSize, values);
    }
    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(JobBookingFormDetail obj) {
        jobBookingFormDetailDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public JobBookingFormDetail queryByProperty(String name, String value) {
        return jobBookingFormDetailDao.findSingleByProperty(name,value);
    }

    /**
     * 添加对象
     *
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(JobBookingFormDetail obj) {
        return jobBookingFormDetailDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public JobBookingFormDetail queryObjById(Serializable id) {
        return jobBookingFormDetailDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        jobBookingFormDetailDao.deleteById(id);
    }
    /**
     * 根据报工单编号查找报工单详情信息
     * @param formNo 报工单编号
     * @return
     */
    @Override
    public List<JobBookingFormDetail> queryByJobBookingFormNo(String formNo) {
        return jobBookingFormDetailDao.findByHQL("from JobBookingFormDetail detail where detail.jobBookingForm.formNo=?0",new Object[]{formNo});
    }
    /**
     * 根据报工单号查找入库数大于0的记录数
     * @param formNo
     * @return
     */
    @Override
    public Long queryCountOfAmountOfInWarehouseBigThenZero(String formNo) {
        return jobBookingFormDetailDao.findCount("from JobBookingFormDetail detail where detail.jobBookingForm.formNo=?0" +
                " and detail.amountOfInWarehouse>0",new Object[]{formNo});
    }

    /**
     * 根据班次，日期，生产单元id查找产量
     *
     * @param c
     * @param d
     * @param productionUnitId
     * @return
     */
    @Override
    public double queryBottleneckCountByClassesIdAndDay(Classes c, Date d, Long productionUnitId) {
        return jobBookingFormDetailDao.queryBottleneckCountByClassesIdAndDay(c,d,productionUnitId);
    }
    /**
     * 查询生产数量和总标准节拍
     * @param currentClass
     * @param deviceSiteCode
     * @param date
     * @return
     */
    @Override
    public Object[] queryCountAndSumOfStandardBeat4CurrentClass(Classes currentClass, String deviceSiteCode, Date date) {
        return jobBookingFormDetailDao.queryCountAndSumOfStandardBeat4CurrentClass(currentClass,deviceSiteCode,date);
    }

    /**
     * 根据工单单号查找最大箱号
     *
     * @param no
     * @return
     */
    @Override
    public int queryMaxBoxNumByNoAndProcessCode(String no,String processCode) {
        return jobBookingFormDetailDao.queryMaxBoxNumByNoAndProcessCode(no,processCode);
    }

    @Override
    public List<JobBookingFormDetail> queryByWorksheetNo(String no) {
        String hql = "from JobBookingFormDetail detail where detail.jobBookingForm.workSheetNo=?0 order by detail.jobBookingForm.jobBookingDate desc";
        return jobBookingFormDetailDao.findByHQL(hql,new Object[]{no});
    }
}
