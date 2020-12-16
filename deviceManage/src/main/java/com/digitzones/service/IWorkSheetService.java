package com.digitzones.service;

import com.digitzones.bo.WorkSheetBo;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.model.WorkSheet;
import com.digitzones.model.WorkSheetDetail;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * 工单service
 * @author zdq
 * 2018年6月20日
 */
public interface IWorkSheetService extends ICommonService<WorkSheet> {
	/**
	 * 根据设备站点id查询当前站点下的工单
	 * @param deviceSiteId
	 * @return
	 */
	public List<WorkSheet> queryWorkSheetByDeviceSiteId(Long deviceSiteId);
	/**
	 * 根据站点id查找工单信息
	 * @param deviceSiteId
	 * @return
	 */
	public List<WorkSheet> queryWorkSheetsByDeviceSiteId(Long deviceSiteId);

	/**
	 * 根据设备站点id查找正在加工的工单
	 * @param deviceSiteId
	 * @return
	 */
	public WorkSheet queryRunningWorkSheetByDeviceSiteId(Long deviceSiteId);
	/**
	 * 根据设备站点id及 条件查询工单
	 * @param deviceSiteId
	 * @param q
	 * @return
	 */
	public List<WorkSheet> queryWorkSheetsByDeviceSiteIdAndConditions(Long deviceSiteId,String q);
	/**
	 * 添加工单
	 * @param workSheet
	 */
	public void addWorkSheet(WorkSheet workSheet,List<WorkSheetDetail> list,Boolean change);
	/**
	 * 更新工单及工单详情
	 * @param workSheet
	 * @param workSheetDetails
	 */
	public void updateWorkSheetAndWorkSheetDetails(WorkSheet workSheet,List<WorkSheetDetail> workSheetDetails);
	/**
	 * 开工
	 * @param workSheetId 工单id
	 * @throws CloneNotSupportedException 
	 */
	public void start(Long workSheetId);
	/**
	 * 停工
	 * @param id
	 */
	public void stop(Long id);
	/**
	 * 根据生产单元编码查找正在开工的工单信息
	 * @param productionUnitCode 生产单元编码
	 * @return List<WorkSheet>
	 */
	public List<WorkSheet> queryWorkingWorkSheetsByProductionUnitCode(String productionUnitCode);
	
	/**
	 * 完工
	 */
	public void updateOver(Long id, User user);
	/**
	 * 根据制单日期查找最大单号
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
	 * 删除工单下的详情和参数信息
	 * @param id
	 */
	public void deleteDetails(Serializable id);

	/**
	 * 查询生产过程监控
	 * @return
	 */
	public Pager queryByMonitoring(String sql, int pageNo, int pageSize, Object... values);

	public List<WorkSheetBo> queryByMonitoring(String sql, Object... values);
}
