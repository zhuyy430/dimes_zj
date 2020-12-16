package com.digitzones.service;

import com.digitzones.model.Pager;
import com.digitzones.model.ProcessDeviceSiteMapping;
import com.digitzones.model.WorkSheetDetail;

import java.util.Date;
import java.util.List;
/**
 * 工单详情service
 * @author zdq
 * 2018年6月20日
 */
public interface IWorkSheetDetailService extends ICommonService<WorkSheetDetail> {
	/**
	 * 根据工单id查找该工单详情
	 * @param workSheetId
	 * @return
	 */
	public List<WorkSheetDetail> queryWorkSheetDetailsByWorkSheetId(Long workSheetId);
	/**
	 * 根据工件id查询工序，设备站点等信息，并在内存中构建为WorkSheetDetail类型的列表 
	 * @param count  生产总数
	 * @return
	 */
	public void buildWorkSheetDetailListInMemoryByWorkpieceId(Integer count,String workpieceCode,String productionUnitCode,List<WorkSheetDetail> list);
	/**
	 * 根据工序id查询设备站点 
	 * @param processId
	 * @return
	 */
	public Pager<ProcessDeviceSiteMapping> queryOtherDeviceSitesByProcessId(Long processId,Long productionUnitId,int pageNo, int pageSize,List<WorkSheetDetail> workSheetDetails);
	/**
	 * 查询该工序下的工单详情数
	 * @param processId
	 * @return
	 */
	public Long queryCountByProcessId(Long processId,Long workSheetId);
	
	/**
	 * 根据工单id和设备站点id查找工单详情
	 * @param workSheetId
	 * @param deviceSiteId
	 * @return
	 */
	public List<WorkSheetDetail> queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId(Long workSheetId,Long deviceSiteId);
	/**
	 * 根据工单id，站点id和工序代码查找工单详情
	 * @param workSheetId
	 * @param deviceSiteId
	 * @param processCode
	 * @return
	 */
	public List<WorkSheetDetail> queryWorkSheetDetailByWorkSheetIdAndDeviceSiteIdAndProccessId(Long workSheetId,Long deviceSiteId,String processCode);
	/**
	 * 完工
	 * @param workSheet
	 */
	public List<WorkSheetDetail> workOver(Long deviceSiteId);
	/**
	 * 根据设备站点编码查找工单详情
	 * @param deviceSiteCode
	 * @return
	 */
	public WorkSheetDetail queryProcessingWorkSheetDetailByDeviceSiteCode(String deviceSiteCode);
	/**
	 * 完工
	 * @param workSheetDetailId
	 */
	public void complete(Long workSheetDetailId);
	/**
	 * 根据设备站点查找工单详情
	 * @param deviceSiteCode
	 * @return 
	 */
	public List<WorkSheetDetail> queryWorkSheetDetailByDeviceSiteCode(String deviceSiteCode);
	/**
	 * 根据工单id查找已完成工件数
	 * @param id
	 * @return
	 */
	public Long querySumCompleteByWorkSheetId(Long id);
	/**
	 * 根据id数组查找工单详情
	 * @param idsList
	 * @return
	 */
   	public  List<WorkSheetDetail> queryByIds(List<Long> idsList);

	/**
	 * 根据工单单号查找工序代码和名称
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


	/**
	 * 查询所有工单详情
	 */
	List<WorkSheetDetail> queryAllWorkSheetDetail();
}
