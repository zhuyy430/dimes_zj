package com.digitzones.mc.service;

import java.util.List;
import java.util.Map;

import com.digitzones.mc.model.MCWorkSheet;
import com.digitzones.model.WorkSheet;
import com.digitzones.model.WorkSheetDetail;

public interface IMCWorkSheetService {
	/**
	 *根据设备站点代码查询 相对应的工单列表
	 */
	public List<WorkSheetDetail> findWorkSheetDetailList(String deviceSiteCode);
	/**
	 * 根据站点编码查找没有完工的工单详情
	 * @param deviceSiteCode
	 * @return
	 */
	public List<MCWorkSheet> queryNotCompleteWorkSheetByDeviceCode(String deviceSiteCode);
	/**
	 * 根据设备站点代码查询该站点上正在开工的工单信息
	 * @param deviceSiteCode 设备站点代码
	 * @return
	 */
	public List<MCWorkSheet> queryProcessingWorkSheetsByDeviceCode(String deviceSiteCode);
	/**
	 * 查询当前IP地址 上的所有站点上正在开工的工单信息
	 * @param clientIp
	 * @return
	 */
	public List<MCWorkSheet> queryProcessingWorkSheets(String clientIp);
	/**
	 * 报工：更新工单详情 
	 * @param params
	 */
	public void updateWorkSheetDetail(Map<Long,Integer> params);
	/**
	 * 通过工单no查询工单
	 */
	public WorkSheet queryWorkSheetIdByNo(String No);
}
