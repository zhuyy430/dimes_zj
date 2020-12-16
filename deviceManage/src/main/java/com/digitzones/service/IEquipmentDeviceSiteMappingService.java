package com.digitzones.service;

import com.digitzones.model.EquipmentDeviceSiteMapping;
import com.digitzones.model.Pager;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
/**
 * 装备和设备站点关联service
 * @author zdq
 * 2018年6月11日
 */
public interface IEquipmentDeviceSiteMappingService extends ICommonService<EquipmentDeviceSiteMapping> {
	/**
	 * 根据序列号查询装备和设备的关联
	 * @param no
	 * @return
	 */
	public EquipmentDeviceSiteMapping queryByNo(String no);


	/**
     * 根据序列号查询装备和设备的关联
	 */
	public EquipmentDeviceSiteMapping queryByEquipmentCode(String code);

	/**
	 * 装备关联记录汇总表
	 */
	public Pager<List<Object[]>> queryEquipmentCountReport(Map<String,String> params,int rows,int page);


	public List<EquipmentDeviceSiteMapping> queryListByDeviceSiteCodeAndEquipmentTypeId(String deviceSiteCode,Long equipmentTypeId,String workSheetNo);


	/**
	 * mc端工单界面装备关联
	 * @param equipmentNos
	 * @param deviceSiteCode
	 * @param helperId
	 * @param usageRates
	 */
	public void addMCEquipmentsMappingRecordInWorkSheet(String equipmentNos, String deviceSiteCode, String helperId, String usageRates, HttpServletRequest request,String workSheetNo);

	/**
	 * 通过工单id查询工单的装备关联记录
	 */
	public List<EquipmentDeviceSiteMapping> queryEquipmentMappingRecordByWorkSheetNo(Long workSheetId);
}
