package com.digitzones.dao;

import com.digitzones.bo.WorkSheetBo;
import com.digitzones.model.Pager;
import com.digitzones.model.WorkSheet;
import org.apache.poi.hssf.record.formula.functions.T;

import java.util.Date;
import java.util.List;

/**
 * 工单dao
 * @author zdq
 * 2018年6月20日
 */
public interface IWorkSheetDao extends ICommonDao<WorkSheet> {
    /**
     * 根据制单日期 查找最大单号
     * @param now
     * @return
     */
   public String queryMaxNoByMakeDocumentDate(Date now);
    /**
     * 根据报工条码查找工单
     * @param barCode
     * @return
     */
    public WorkSheet queryByBarCode(String barCode);


    /**
     * 查询生产过程监控
     * @return
     */
    public Pager<T> queryByMonitoring(String sql, int pageNo, int pageSize, Object... values);

    public List<WorkSheetBo> queryByMonitoring(String sql, Object... values);
}
