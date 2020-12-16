package com.digitzones.dao;

import java.util.Date;
import java.util.List;

import com.digitzones.model.Pager;
import com.digitzones.model.ProcessDeviceSiteMapping;
import com.digitzones.model.WorkSheetDetail;
/**
 * 工单详情dao
 * @author zdq
 * 2018年6月20日
 */
public interface IWorkSheetDetailDao extends ICommonDao<WorkSheetDetail> {
	/**
	 * 查询不在[deviceSiteIdList]的设备站点
	 * @param deviceSiteIdList 设备站点List
	 * @param processId 工序id
	 * @return
	 */
	public Pager<ProcessDeviceSiteMapping> queryDeviceSiteOutOfListByProcessId(List<Long> deviceSiteIdList,Long productionUnitId,Long processId,int pageNo, int pageSize);
	/**
	 * 根据工序id和工单id查找工单详情数
	 * @param processId
	 * @param workSheetID
	 * @return
	 */
	public Long queryCountByProcessIdAndWorkSheetId(Long processId,Long workSheetID);
	/**
	 * 根据工单单号 查找工序编码和名称
	 * @param no
	 * @return
	 */
    public List<Object[]> queryProcessCodeAndNameByNo(String no);
    /**
     * 根据时间和单元Id获取完成总数
     * @param from
     * @param to
     * @param productionUnitId
     * @return
     */
    public Long querySumByProductionUnitId(Date from ,Date to,String productionUnitId);
	/**
	 * 根据工单单号和工序编码查找设备站点信息
	 * @param no 工单单号
	 * @param processCode 工序编码
	 * @return
	 */
    List<Object[]> queryDeviceSiteByNoAndProcessCode(String no, String processCode);
}
