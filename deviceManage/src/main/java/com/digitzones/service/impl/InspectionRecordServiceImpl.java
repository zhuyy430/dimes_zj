package com.digitzones.service.impl;

import com.digitzones.dao.IInspectionRecordDao;
import com.digitzones.dao.IInspectionRecordDetailDao;
import com.digitzones.model.InspectionRecord;
import com.digitzones.model.InspectionRecordDetail;
import com.digitzones.model.Pager;
import com.digitzones.service.IInspectionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Service
public class InspectionRecordServiceImpl implements IInspectionRecordService {
    @Autowired
    private IInspectionRecordDao inspectionRecordDao;
    @Autowired
    private IInspectionRecordDetailDao inspectionRecordDetailDao;
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
        return inspectionRecordDao.findByPage(hql, pageNo, pageSize, values);
    }

    /**
     * 更新对象
     * @param obj
     */
    @Override
    public void updateObj(InspectionRecord obj) {
        inspectionRecordDao.update(obj);
    }
    /**
     * 根据属性查询对象
     * @param name  属性名称
     * @param value 属性值
     * @return
     */
    @Override
    public InspectionRecord queryByProperty(String name, String value) {
        return inspectionRecordDao.findSingleByProperty(name,value);
    }
    /**
     * 添加对象
     * @param obj
     * @return
     */
    @Override
    public Serializable addObj(InspectionRecord obj) {
        return inspectionRecordDao.save(obj);
    }
    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public InspectionRecord queryObjById(Serializable id) {
        return inspectionRecordDao.findById(id);
    }
    /**
     * 根据id删除对象
     * @param id
     */
    @Override
    public void deleteObj(Serializable id) {
        inspectionRecordDao.deleteById(id);
    }

    /**
     * 根据检验日期查找最大检验单号
     *
     * @param now 检验日期
     * @return
     */
    @Override
    public String queryMaxFormNoByInspectionDate(Date now) {
        return inspectionRecordDao.queryMaxFormNoByInspectionDate(now);
    }

    /**
     * 新增检验工单
     * @param form    检验工单对象
     * @param details 检验工单详情
     */
    @Override
    public void addInspectionRecord(InspectionRecord form, List<InspectionRecordDetail> details) {
        inspectionRecordDao.save(form);
        boolean isNg = false;
        for(InspectionRecordDetail detail : details){
            detail.setInspectionRecord(form);
            if("NG".equals(detail.getInspectionResult())){
                isNg = true;
            }

            inspectionRecordDetailDao.save(detail);
        }
        if(isNg){
            form.setInspectionResult("NG");
        }else{
            form.setInspectionResult("OK");
        }
        inspectionRecordDao.update(form);
    }

    /**
     * 新增检验工单
     *
     * @param form       检验工单对象
     * @param details    检验工单详情
     */
    @Override
    public void updateInspectionRecord(InspectionRecord form, List<InspectionRecordDetail> details) {
        //更新领料单对象
       InspectionRecord waf = inspectionRecordDao.findById(form.getFormNo());
       /* copyProperties4InspectionRecord(form,waf);
        inspectionRecordDao.update(waf);*/
       boolean isNg = false;
        //更新领料单详情
        if(!CollectionUtils.isEmpty(details)){
            for (InspectionRecordDetail detail : details){
                InspectionRecordDetail wafd = inspectionRecordDetailDao.findById(detail.getId());
                if(wafd!=null) {
                    copyProperties4InspectionRecordDetail(detail, wafd);
                    if("NG".equals(wafd.getInspectionResult())){
                        isNg = true;
                    }
                    inspectionRecordDetailDao.update(wafd);
                }
            }
        }
        if(isNg){
            waf.setInspectionResult("NG");
        }else{
            waf.setInspectionResult("OK");
        }
        waf.setInspectionType(form.getInspectionType());
        inspectionRecordDao.update(waf);
    }
    /**
     * 根据领料单号删除领料单及详情
     * @param formNo
     */
    @Override
    public void deleteByFormNo(String formNo) {
        inspectionRecordDao.deleteById(formNo);
        inspectionRecordDetailDao.deleteByFormNo(formNo);
    }

    private void copyProperties4InspectionRecord(InspectionRecord form, InspectionRecord waf) {
        waf.setInspectionDate(form.getInspectionDate());
    }
    private void copyProperties4InspectionRecordDetail(InspectionRecordDetail formDetail, InspectionRecordDetail waf) {
        waf.setNote(formDetail.getNote());
        waf.setParameterValue(formDetail.getParameterValue());
        waf.setInspectionResult(formDetail.getInspectionResult());
    }

	@Override
	public List<InspectionRecord> queryInspectionRecordsByDeviceSite(String hql, List<Object> param) {
		return inspectionRecordDao.findByHQL(hql, param.toArray());
	}
}
