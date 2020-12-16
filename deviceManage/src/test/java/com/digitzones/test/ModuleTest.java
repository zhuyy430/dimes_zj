package com.digitzones.test;

import com.digitzones.constants.Constant;
import com.digitzones.dao.IRoleModuleDao;
import com.digitzones.devmgr.model.Config;
import com.digitzones.devmgr.model.DeviceLevel;
import com.digitzones.devmgr.model.MaintenanceType;
import com.digitzones.devmgr.service.IConfigService;
import com.digitzones.devmgr.service.IDeviceLevelService;
import com.digitzones.devmgr.service.IMaintenanceTypeService;
import com.digitzones.mc.model.MCCommonSuggestion;
import com.digitzones.mc.model.MCCommonType;
import com.digitzones.mc.service.IMCCommonSuggestionService;
import com.digitzones.mc.service.IMCCommonTypeService;
import com.digitzones.model.*;
import com.digitzones.model.Module;
import com.digitzones.procurement.service.IInventoryClassService;
import com.digitzones.service.*;
import com.digitzones.util.PasswordEncoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
/**
 * 功能模块测试类
 * @author zdq
 * 2018年6月6日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath*:springContext-*.xml"})
public class ModuleTest {
	private IModuleService moduleService ;
	private IDepartmentService departmentService;
	private IProductionUnitService productionUnitService;
	private IParameterTypeService parameterTypeService;
	private IInventoryClassService workpieceTypeService;
	private IEquipmentTypeService equipmentTypeService;
	private ISkillLevelService skillLevelService;
	private IProcessTypeService processTypeService;
	private IRoleService roleService;
	private IUserService userService;
	@Autowired
	private IMCCommonSuggestionService mcCommonSuggestionService;
	@Autowired
	private IMCCommonTypeService mcCommonTypeService;
	@Autowired
	private IUserRoleService userRoleService;
	@Autowired
	private IRoleModuleDao roleModuleDao;
	@Autowired
	private IDeviceLevelService deviceLevelService;
	@Autowired
	private IRelatedDocumentTypeService relatedDocumentTypeService;
	@Autowired
	private IProjectTypeService projectTypeService;
	@Autowired
	private IMaintenanceTypeService maintenanceTypeService;
	@Autowired
	private IConfigService configService;
	@Autowired
	private IPressLightTypeService pressLightTypeService;
	@Autowired
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}
	@Autowired
	public void setProcessTypeService(IProcessTypeService processTypeService) {
		this.processTypeService = processTypeService;
	}
	@Autowired
	public void setSkillLevelService(ISkillLevelService skillLevelService) {
		this.skillLevelService = skillLevelService;
	}
	@Autowired
	public void setEquipmentTypeService(IEquipmentTypeService equipmentTypeService) {
		this.equipmentTypeService = equipmentTypeService;
	}
	@Autowired
	public void setWorkpieceTypeService(IInventoryClassService workpieceTypeService) {
		this.workpieceTypeService = workpieceTypeService;
	}
	@Autowired
	public void setParameterTypeService(IParameterTypeService parameterTypeService) {
		this.parameterTypeService = parameterTypeService;
	}
	@Autowired
	public void setProductionUnitService(IProductionUnitService productionUnitService) {
		this.productionUnitService = productionUnitService;
	}
	@Autowired
	public void setDepartmentService(IDepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	@Autowired
	public void setModuleService(IModuleService moduleService) {
		this.moduleService = moduleService;
	}
	@Test
	public void testAddModule() {
		Module module1 = new Module();
		module1.setName("采购管理");
		module1.setIcon("fa fa-cart-plus");
		module1.setPriority(1);
		moduleService.addModule(module1);



		Module secondLevel1 = new Module();
		secondLevel1.setName("采购管理");
		secondLevel1.setPriority(1);
		secondLevel1.setParent(module1);
		moduleService.addModule(secondLevel1);

		Module thirdLevel1 = new Module();
		thirdLevel1.setName("采购订单");
		thirdLevel1.setPriority(1);
		thirdLevel1.setLeaf(true);
		thirdLevel1.setDisabled(false);
		thirdLevel1.setUrl("procurement/purchasingOrder.jsp");
		thirdLevel1.setParent(secondLevel1);
		moduleService.addModule(thirdLevel1);

		Module thirdLevel2 = new Module();
		thirdLevel2.setName("入库申请单");
		thirdLevel2.setPriority(2);
		thirdLevel2.setLeaf(true);
		thirdLevel2.setDisabled(false);
		thirdLevel2.setUrl("procurement/warehousingApplicationForm.jsp");
		thirdLevel2.setParent(secondLevel1);
		moduleService.addModule(thirdLevel2);

		Module thirdLevel21 = new Module();
		thirdLevel21.setName("外协入库申请单");
		thirdLevel21.setPriority(3);
		thirdLevel21.setLeaf(true);
		thirdLevel21.setDisabled(false);
		thirdLevel21.setUrl("procurement/outsourcingWarehousingApplicationForm.jsp");
		thirdLevel21.setParent(secondLevel1);
		moduleService.addModule(thirdLevel21);

		Module thirdLevel3 = new Module();
		thirdLevel3.setName("入库验收审核");
		thirdLevel3.setPriority(4);
		thirdLevel3.setLeaf(true);
		thirdLevel3.setDisabled(false);
		thirdLevel3.setUrl("procurement/warehousingAudit.jsp");
		thirdLevel3.setParent(secondLevel1);
		moduleService.addModule(thirdLevel3);

		Module secondLevel2 = new Module();
		secondLevel2.setName("条码登记");
		secondLevel2.setPriority(2);
		secondLevel2.setParent(module1);
		moduleService.addModule(secondLevel2);

		Module thirdLevel4 = new Module();
		thirdLevel4.setName("临时条码登记");
		thirdLevel4.setPriority(3);
		thirdLevel4.setLeaf(true);
		thirdLevel4.setDisabled(false);
		thirdLevel4.setUrl("procurement/temporaryBarcode.jsp");
		thirdLevel4.setParent(secondLevel2);
		moduleService.addModule(thirdLevel4);

		Module module2 = new Module();
		module2.setName("生产管理");
		module2.setIcon("fa fa-industry");
		module2.setPriority(2);
		moduleService.addModule(module2);


		Module module21 = new Module();
		module21.setName("生产管理");
		module21.setPriority(1);
		module21.setParent(module2);
		moduleService.addModule(module21);

		Module module210 = new Module();
		module210.setName("ERP生产订单");
		module210.setPriority(1);
		module210.setParent(module21);
		module210.setLeaf(true);
		module210.setUrl("console/jsp/erpProductionRecode.jsp");
		moduleService.addModule(module210);
		
		Module module211 = new Module();
		module211.setName("生产工单");
		module211.setPriority(2);
		module211.setParent(module21);
		module211.setLeaf(true);
		module211.setUrl("console/jsp/workSheet.jsp");
		moduleService.addModule(module211);

		Module module212 = new Module();
		module212.setName("生产领料单");
		module212.setPriority(3);
		module212.setParent(module21);
		module212.setLeaf(true);
		module212.setUrl("console/jsp/materialRequisition.jsp");
		moduleService.addModule(module212);

		Module module213 = new Module();
		module213.setName("生产退料单");
		module213.setPriority(4);
		module213.setParent(module21);
		module213.setLeaf(true);
		module213.setUrl("console/jsp/returnSlip.jsp");
		moduleService.addModule(module213);

		Module module214 = new Module();
		module214.setName("生产报工单");
		module214.setPriority(5);
		module214.setParent(module21);
		module214.setLeaf(true);
		module214.setUrl("console/jsp/jobBooking.jsp");
		moduleService.addModule(module214);

		Module module215 = new Module();
		module215.setName("过程检验记录");
		module215.setPriority(6);
		module215.setParent(module21);
		module215.setLeaf(true);
		module215.setUrl("console/jsp/inspectionRecord.jsp");
		moduleService.addModule(module215);

		Module module216 = new Module();
		module216.setName("装备关联记录");
		module216.setPriority(7);
		module216.setParent(module21);
		module216.setLeaf(true);
		module216.setUrl("console/jsp/equipmentMappingRecord.jsp");
		moduleService.addModule(module216);

		Module module217 = new Module();
		module217.setName("量具关联记录");
		module217.setPriority(8);
		module217.setParent(module21);
		module217.setLeaf(true);
		module217.setUrl("console/jsp/measuringToolMappingRecord.jsp");
		moduleService.addModule(module217);

		Module module218 = new Module();
		module218.setName("损失时间记录");
		module218.setPriority(9);
		module218.setParent(module21);
		module218.setLeaf(true);
		module218.setUrl("console/jsp/lostTimeRecord.jsp");
		moduleService.addModule(module218);

		Module module219 = new Module();
		module219.setName("不合格品记录");
		module219.setPriority(10);
		module219.setParent(module21);
		module219.setLeaf(true);
		module219.setUrl("console/jsp/ngRecord.jsp");
		moduleService.addModule(module219);

		Module module2110 = new Module();
		module2110.setName("装备维修记录");
		module2110.setPriority(11);
		module2110.setParent(module21);
		module2110.setLeaf(true);
		module2110.setUrl("console/jsp/equipmentRapirRecord.jsp");
		moduleService.addModule(module2110);

		Module module2111 = new Module();
		module2111.setName("生产过程监控");
		module2111.setPriority(12);
		module2111.setParent(module21);
		module2111.setLeaf(true);
		module2111.setUrl("console/jsp/productionProcessMonitoring.jsp");
		moduleService.addModule(module2111);

		Module module22 = new Module();
		module22.setName("执行数据");
		module22.setPriority(2);
		module22.setParent(module2);
		moduleService.addModule(module22);

		Module module221 = new Module();
		module221.setName("设备执行");
		module221.setPriority(1);
		module221.setParent(module22);
		module221.setLeaf(true);
		module221.setUrl("console/jsp/processRecord.jsp");
		moduleService.addModule(module221);

		Module module222 = new Module();
		module222.setName("文档管控");
		module222.setPriority(2);
		module222.setParent(module22);
		module222.setLeaf(true);
		module222.setUrl("console/jsp/fileMgr.jsp");
		moduleService.addModule(module222);

		/*Module son7 = new Module();
		son7.setParent(module22);
		son7.setPriority(2);
		son7.setName("损时记录");
		son7.setLeaf(true);
		son7.setUrl("console/jsp/lostTimeRecord.jsp");
		moduleService.addModule(son7);*/

		Module module3 = new Module();
		module3.setName("销售管理");
		module3.setIcon("fa fa-buysellads");
		module3.setPriority(3);
		moduleService.addModule(module3);

		Module module31 = new Module();
		module31.setName("销售管理");
		module31.setPriority(1);
		module31.setParent(module3);
		moduleService.addModule(module31);

		Module module311 = new Module();
		module311.setName("发货计划");
		module311.setPriority(1);
		module311.setParent(module31);
		module311.setUrl("console/jsp/deliveryPlan.jsp");
		module311.setLeaf(true);
		moduleService.addModule(module311);
		
		/*Module module312 = new Module();
		module312.setName("包装条码打印");
		module312.setPriority(2);
		module312.setParent(module31);
		module312.setUrl("console/jsp/packageCodePrint.jsp");
		module312.setLeaf(true);
		moduleService.addModule(module312);*/
		
		Module module313 = new Module();
		module313.setName("包装记录");
		module313.setPriority(3);
		module313.setParent(module31);
		module313.setUrl("console/jsp/packageCodeRecord.jsp");
		module313.setLeaf(true);
		moduleService.addModule(module313);

		Module module4 = new Module();
		module4.setName("报表管理");
		module4.setIcon("fa fa-database");
		module4.setPriority(4);
		moduleService.addModule(module4);

		Module module41 = new Module();
		module41.setPriority(10);
		module41.setLeaf(false);
		module41.setParent(module4);
		module41.setDisabled(false);
		module41.setName("报表管理");
		moduleService.addModule(module41);


		Module son4 = new Module();
		son4.setParent(module4);
		son4.setName("系统配置");
		son4.setLeaf(false);
		son4.setPriority(20);
		moduleService.addModule(son4);


		Module son44 = new Module();
		son44.setParent(son4);
		son44.setLeaf(true);
		son44.setName("产线看板映射");
		son44.setUrl("console/jsp/productionUnitMapping.jsp");
		moduleService.addModule(son44);

		Module module411 = new Module();
		module411.setPriority(1);
		module411.setLeaf(true);
		module411.setParent(module41);
		module411.setDisabled(false);
		module411.setUrl("reportForm/traceabilityCenter.jsp");
		module411.setName("追溯中心");
		moduleService.addModule(module411);

		Module module412 = new Module();
		module412.setPriority(2);
		module412.setLeaf(true);
		module412.setParent(module41);
		module412.setDisabled(false);
		module412.setUrl("reportForm/dailyOutput.jsp");
		module412.setName("日产量记录表");
		moduleService.addModule(module412);

		Module module413 = new Module();
		module413.setPriority(3);
		module413.setLeaf(true);
		module413.setParent(module41);
		module413.setDisabled(false);
		module413.setUrl("reportForm/outputSummary.jsp");
		module413.setName("产量汇总表");
		moduleService.addModule(module413);

		Module module414 = new Module();
		module414.setPriority(4);
		module414.setLeaf(true);
		module414.setParent(module41);
		module414.setDisabled(false);
		module414.setUrl("reportForm/oeeRecord.jsp");
		module414.setName("OEE记录表");
		moduleService.addModule(module414);

		Module module415 = new Module();
		module415.setPriority(5);
		module415.setLeaf(true);
		module415.setParent(module41);
		module415.setDisabled(false);
		module415.setUrl("reportForm/ngRecord.jsp");
		module415.setName("不合格品记录表");
		moduleService.addModule(module415);

		Module module416 = new Module();
		module416.setPriority(6);
		module416.setLeaf(true);
		module416.setParent(module41);
		module416.setDisabled(false);
		module416.setUrl("reportForm/ngRecordSummary.jsp");
		module416.setName("不合格品汇总表");
		moduleService.addModule(module416);

		Module module417 = new Module();
		module417.setPriority(7);
		module417.setLeaf(true);
		module417.setParent(module41);
		module417.setDisabled(false);
		module417.setUrl("reportForm/safetyGreenCross.jsp");
		module417.setName("安全绿十字");
		moduleService.addModule(module417);

		Module module418 = new Module();
		module418.setPriority(8);
		module418.setLeaf(true);
		module418.setParent(module41);
		module418.setDisabled(false);
		module418.setUrl("reportForm/losttime.jsp");
		module418.setName("损失时间表");
		moduleService.addModule(module418);

		Module module419 = new Module();
		module419.setPriority(9);
		module419.setLeaf(true);
		module419.setParent(module41);
		module419.setDisabled(false);
		module419.setUrl("reportForm/losttimeAnalysis.jsp");
		module419.setName("损失时间分析表");
		moduleService.addModule(module419);

		Module p4 = new Module();
		p4.setName("设备管理");
		p4.setIcon("fa fa-wrench");
		p4.setPriority(5);
		moduleService.addModule(p4);

		Module p42 = new Module();
		p42.setName("设备点检");
		p42.setPriority(1);
		p42.setParent(p4);
		moduleService.addModule(p42);

		Module p421 = new Module();
		p421.setName("点检计划");
		p421.setPriority(1);
		p421.setParent(p42);
		p421.setLeaf(true);
		p421.setDisabled(false);
		p421.setUrl("console/jsp/deviceManagement/checkingPlan.jsp");
		moduleService.addModule(p421);

		Module p422 = new Module();
		p422.setName("设备点检");
		p422.setPriority(2);
		p422.setParent(p42);
		p422.setLeaf(true);
		p422.setDisabled(false);
		p422.setUrl("console/jsp/deviceManagement/checkingDevice.jsp");
		moduleService.addModule(p422);

		Module checkReport = new Module();
		checkReport.setName("点检状况分析表");
		checkReport.setPriority(3);
		checkReport.setParent(p42);
		checkReport.setLeaf(true);
		checkReport.setDisabled(false);
		checkReport.setUrl("console/jsp/deviceManagement/checkOverview.jsp");
		moduleService.addModule(checkReport);

		Module p423 = new Module();
		p423.setName("点检完成状况表");
		p423.setPriority(4);
		p423.setParent(p42);
		p423.setLeaf(true);
		p423.setDisabled(false);
		p423.setUrl("console/jsp/deviceManagement/checkStatistics.jsp");
		moduleService.addModule(p423);

		Module p43 = new Module();
		p43.setName("设备保养");
		p43.setPriority(3);
		p43.setParent(p4);
		moduleService.addModule(p43);

		Module p431 = new Module();
		p431.setName("保养计划");
		p431.setPriority(1);
		p431.setParent(p43);
		p431.setLeaf(true);
		p431.setDisabled(false);
		p431.setUrl("console/jsp/deviceManagement/maintenancePlan.jsp");
		moduleService.addModule(p431);

		Module p432 = new Module();
		p432.setName("设备保养中心");
		p432.setPriority(2);
		p432.setParent(p43);
		p432.setLeaf(true);
		p432.setDisabled(false);
		p432.setUrl("console/jsp/deviceManagement/maintenanceCenter.jsp");
		moduleService.addModule(p432);

		Module maintenanceReport = new Module();
		maintenanceReport.setName("保养状况分析表");
		maintenanceReport.setPriority(3);
		maintenanceReport.setParent(p43);
		maintenanceReport.setLeaf(true);
		maintenanceReport.setDisabled(false);
		maintenanceReport.setUrl("console/jsp/deviceManagement/maintenanceOverview.jsp");
		moduleService.addModule(maintenanceReport);

		Module maintenanceStatistices = new Module();
		maintenanceStatistices.setName("保养完成状况表");
		maintenanceStatistices.setPriority(4);
		maintenanceStatistices.setParent(p43);
		maintenanceStatistices.setLeaf(true);
		maintenanceStatistices.setDisabled(false);
		maintenanceStatistices.setUrl("console/jsp/deviceManagement/maintenanceStatistics.jsp");
		moduleService.addModule(maintenanceStatistices);

		Module p44 = new Module();
		p44.setName("设备维修");
		p44.setPriority(4);
		p44.setParent(p4);
		moduleService.addModule(p44);

		Module p441 = new Module();
		p441.setName("维修人员在岗状态");
		p441.setPriority(1);
		p441.setParent(p44);
		p441.setLeaf(true);
		p441.setUrl("console/jsp/deviceManagement/maintenanceStaff.jsp");
		moduleService.addModule(p441);

		Module p442 = new Module();
		p442.setName("设备运行记录");
		p442.setPriority(2);
		p442.setParent(p44);
		p442.setLeaf(true);
		p442.setUrl("console/jsp/deviceManagement/deviceOperationalRecord.jsp");
		moduleService.addModule(p442);

		Module p443 = new Module();
		p443.setName("设备报修单");
		p443.setPriority(3);
		p443.setParent(p44);
		p443.setLeaf(true);
		p443.setUrl("console/jsp/deviceManagement/deviceRepairOrder.jsp");
		moduleService.addModule(p443);

		Module p444 = new Module();
		p444.setName("设备维修记录");
		p444.setPriority(4);
		p444.setParent(p44);
		p444.setLeaf(true);
		p444.setUrl("console/jsp/deviceManagement/MaintenanceStaffRecord.jsp");
		moduleService.addModule(p444);

		Module p446 = new Module();
		p446.setName("维修转单中心");
		p446.setPriority(5);
		p446.setParent(p44);
		p446.setLeaf(true);
		p446.setUrl("console/jsp/deviceManagement/MaintenanceTransferCenter.jsp");
		moduleService.addModule(p446);

		Module p45 = new Module();
		p45.setName("基础资料设定");
		p45.setPriority(5);
		p45.setParent(p4);
		moduleService.addModule(p45);

		Module p451 = new Module();
		p451.setName("设备等级");
		p451.setPriority(1);
		p451.setParent(p45);
		p451.setLeaf(true);
		p451.setUrl("console/jsp/deviceManagement/deviceLevel.jsp");
		moduleService.addModule(p451);

		Module p4511 = new Module();
		p4511.setName("设备类别");
		p4511.setPriority(1);
		p4511.setParent(p45);
		p4511.setLeaf(true);
		p4511.setUrl("console/jsp/deviceManagement/deviceType.jsp");
		moduleService.addModule(p4511);

		Module p452 = new Module();
		p452.setName("设备卡片");
		p452.setPriority(2);
		p452.setParent(p45);
		p452.setUrl("console/jsp/deviceManagement/device.jsp");
		p452.setLeaf(true);
		moduleService.addModule(p452);

		Module p453 = new Module();
		p453.setName("备品类别");
		p453.setPriority(3);
		p453.setParent(p45);
		p453.setUrl("console/jsp/deviceManagement/sparePartType.jsp");
		p453.setLeaf(true);
		moduleService.addModule(p453);

		Module p450 = new Module();
		p450.setName("保养类别");
		p450.setPriority(5);
		p450.setParent(p45);
		p450.setUrl("console/jsp/deviceManagement/maintenanceType.jsp");
		p450.setLeaf(true);
		moduleService.addModule(p450);

		Module p454 = new Module();
		p454.setName("备品备件信息");
		p454.setPriority(4);
		p454.setParent(p45);
		p454.setUrl("console/jsp/deviceManagement/sparePart.jsp");
		p454.setLeaf(true);
		moduleService.addModule(p454);

		Module p455 = new Module();
		p455.setName("设备点检项目");
		p455.setPriority(6);
		p455.setParent(p45);
		p455.setUrl("console/jsp/deviceManagement/deviceInspectionProject.jsp");
		p455.setLeaf(true);
		moduleService.addModule(p455);

		Module p457 = new Module();
		p457.setName("设备保养项目");
		p457.setPriority(8);
		p457.setParent(p45);
		p457.setUrl("console/jsp/deviceManagement/deviceMaintenanceProject.jsp");
		p457.setLeaf(true);
		moduleService.addModule(p457);

		Module p458 = new Module();
		p458.setName("设备故障原因");
		p458.setPriority(9);
		p458.setParent(p45);
		p458.setUrl("console/jsp/deviceManagement/deviceBreakdownReason.jsp");
		p458.setLeaf(true);
		moduleService.addModule(p458);

		Module p459 = new Module();
		p459.setName("设备维修项目");
		p459.setPriority(10);
		p459.setParent(p45);
		p459.setUrl("console/jsp/deviceManagement/deviceServiceProject.jsp");
		p459.setLeaf(true);
		moduleService.addModule(p459);

		Module module6 = new Module();
		module6.setName("基础数据");
		module6.setIcon("fa fa-cubes");
		module6.setPriority(6);
		moduleService.addModule(module6);

		Module basicSon3 = new Module();
		basicSon3.setParent(module6);
		basicSon3.setPriority(3);
		basicSon3.setName("物料信息");
		moduleService.addModule(basicSon3); 
		
		Module parameterType = new Module();
		parameterType.setParent(basicSon3);
		parameterType.setName("参数信息");
		parameterType.setLeaf(true);
		parameterType.setPriority(1);
		parameterType.setUrl("console/jsp/parameterType.jsp");
		moduleService.addModule(parameterType);

		Module processes = new Module();
		processes.setParent(basicSon3);
		processes.setName("工序信息");
		processes.setLeaf(true);
		processes.setPriority(2);
		processes.setUrl("console/jsp/processes.jsp");
		moduleService.addModule(processes);

		Module craftsRoute = new Module();
		craftsRoute.setParent(basicSon3);
		craftsRoute.setName("工艺路线");
		craftsRoute.setLeaf(true);
		craftsRoute.setPriority(3);
		craftsRoute.setUrl("console/jsp/craftsRoute.jsp");
		moduleService.addModule(craftsRoute);

		Module inventoryClass = new Module();
		inventoryClass.setParent(basicSon3);
		inventoryClass.setName("物料类别");
		inventoryClass.setLeaf(true);
		inventoryClass.setPriority(4);
		inventoryClass.setUrl("console/jsp/workpieceType.jsp");
		moduleService.addModule(inventoryClass);

		Module inventory = new Module();
		inventory.setParent(basicSon3);
		inventory.setName("物料信息");
		inventory.setLeaf(true);
		inventory.setPriority(5);
		inventory.setUrl("console/jsp/Inventory.jsp");
		moduleService.addModule(inventory);

		Module basicSon34 = new Module();
		basicSon34.setParent(basicSon3);
		basicSon34.setName("NG原因");
		basicSon34.setLeaf(true);
		basicSon34.setPriority(6);
		basicSon34.setUrl("console/jsp/ngReason.jsp");
		moduleService.addModule(basicSon34);


		Module basicSon2 = new Module();
		basicSon2.setParent(module6);
		basicSon2.setName("装备信息");
		basicSon2.setPriority(2);
		moduleService.addModule(basicSon2);

		Module basicSon21 = new Module();
		basicSon21.setParent(basicSon2);
		basicSon21.setName("设备信息");
		basicSon21.setLeaf(true);
		basicSon21.setUrl("console/jsp/device.jsp");
		moduleService.addModule(basicSon21);

		Module basicSon22 = new Module();
		basicSon22.setParent(basicSon2);
		basicSon22.setName("装备信息");
		basicSon22.setUrl("console/jsp/equipmentType.jsp");
		basicSon22.setLeaf(true);
		moduleService.addModule(basicSon22);

		Module basicSon26 = new Module();
		basicSon26.setParent(basicSon2);
		basicSon26.setName("装备条码导入");
		basicSon26.setUrl("console/jsp/equipmentType.jsp");
		basicSon26.setLeaf(true);
		moduleService.addModule(basicSon26);

		Module basicSon23 = new Module();
		basicSon23.setParent(basicSon2);
		basicSon23.setName("量具信息");
		basicSon23.setLeaf(true);
		basicSon23.setUrl("console/jsp/measuringToolType.jsp");
		moduleService.addModule(basicSon23);

		Module basicSon24 = new Module();
		basicSon24.setParent(basicSon2);
		basicSon24.setName("故障原因");
		basicSon24.setLeaf(true);
		basicSon24.setUrl("console/jsp/pressLightType.jsp");
		moduleService.addModule(basicSon24);

		Module basicSon25 = new Module();
		basicSon25.setParent(basicSon2);
		basicSon25.setName("装备故障");
		basicSon25.setLeaf(true);
		basicSon25.setUrl("console/jsp/equipmentNGType.jsp");
		moduleService.addModule(basicSon25);

		Module basicSon4 = new Module();
		basicSon4.setParent(module6);
		basicSon4.setPriority(1);
		basicSon4.setName("公司结构");
		moduleService.addModule(basicSon4);
		
		Module productionUnit = new Module();
		productionUnit.setParent(basicSon4);
		productionUnit.setName("生产单元");
		productionUnit.setLeaf(true);
		productionUnit.setPriority(6);
		productionUnit.setUrl("console/jsp/productionUnit.jsp");
		moduleService.addModule(productionUnit);

		Module basicSon13 = new Module();
		basicSon13.setParent(basicSon4);
		basicSon13.setName("人员资料");
		basicSon13.setLeaf(true);
		basicSon13.setUrl("console/jsp/employee.jsp");
		moduleService.addModule(basicSon13);


		Module classes = new Module();
		classes.setParent(basicSon4);
		classes.setName("班次定义");
		classes.setLeaf(true);
		classes.setPriority(8);
		classes.setUrl("console/jsp/classes.jsp");
		moduleService.addModule(classes);

		Module module7 = new Module();
		module7.setName("系统设置");
		module7.setPriority(6);
		module7.setIcon("fa fa-cog");
		moduleService.addModule(module7);

		Module sysSetChild = new Module();
		sysSetChild.setName("系统设置");
		sysSetChild.setParent(module7);
		moduleService.addModule(sysSetChild);

		Module user = new Module();
		user.setName("用户管理");
		user.setParent(sysSetChild);
		user.setPriority(10);
		user.setUrl("console/jsp/user.jsp");
		user.setLeaf(true);
		user.setDisabled(false);
		moduleService.addModule(user);

		Module role = new Module();
		role.setName("角色管理");
		role.setPriority(20);
		role.setParent(sysSetChild);
		role.setUrl("console/jsp/role.jsp");
		role.setLeaf(true);
		role.setDisabled(false);
		moduleService.addModule(role);

		/*Module power = new Module();
		power.setName("权限管理");
		power.setPriority(3);
		power.setParent(sysSetChild);
		power.setUrl("console/jsp/power.jsp");
		power.setLeaf(true);
		power.setDisabled(false);
		moduleService.addModule(power);*/

		Module importSet = new Module();
		importSet.setParent(sysSetChild);
		importSet.setDisabled(false);
		importSet.setLeaf(true);
		importSet.setPriority(30);
		importSet.setName("基础资料导入");
		importSet.setUrl("console/jsp/importSet.jsp");
		moduleService.addModule(importSet);

		Module relatedDocumentType = new Module();
		relatedDocumentType.setParent(sysSetChild);
		relatedDocumentType.setDisabled(false);
		relatedDocumentType.setLeaf(true);
		relatedDocumentType.setPriority(40);
		relatedDocumentType.setName("文档类别");
		relatedDocumentType.setUrl("console/jsp/relatedDocumentType.jsp");
		moduleService.addModule(relatedDocumentType);

		Module orderLevel = new Module();
		orderLevel.setParent(sysSetChild);
		orderLevel.setDisabled(false);
		orderLevel.setLeaf(true);
		orderLevel.setPriority(50);
		orderLevel.setName("派单等级");
		orderLevel.setUrl("console/jsp/orderLevel.jsp");
		moduleService.addModule(orderLevel);

		Module secureEnvironmentGrade = new Module();
		secureEnvironmentGrade.setName("安环等级");
		secureEnvironmentGrade.setParent(sysSetChild);
		secureEnvironmentGrade.setUrl("console/jsp/secureEnvironmentGrade.jsp");
		secureEnvironmentGrade.setLeaf(true);
		secureEnvironmentGrade.setDisabled(false);
		secureEnvironmentGrade.setPriority(60);
		moduleService.addModule(secureEnvironmentGrade);
	}

	/**
	 * 初始化超级管理员和角色
	 */
	@Test
	public void initAdmin(){
		User admin = new User();
		admin.setUsername("admin");
		admin.setPassword(new PasswordEncoder("admin").encode("admin"));
		admin.setAllowDelete(false);
		userService.addObj(admin);

		Role adminRole = new Role();
		adminRole.setRoleName("admin");
		adminRole.setAllowDelete(false);
		adminRole.setDisable(false);
		roleService.addObj(adminRole);

		Role role = roleService.queryByProperty("roleName", "admin");
		List<Module> moduleList = moduleService.queryAllModule();
		for(Module module:moduleList){
			RoleModule rm = new RoleModule();
			rm.setModule(module);
			rm.setRole(role);
			roleModuleDao.save(rm);
		}
		UserRole userRole = new UserRole();
		userRole.setRole(role);
		userRole.setUser(admin);
		userRoleService.addObj(userRole);
	}

	@Test
	public void init() {
		ProductionUnit root = new ProductionUnit();
		root.setCode("ROOTPRODUCTIONUNIT");
		root.setName("中京");
		productionUnitService.addObj(root);

		ProductionUnit uncategorized = new ProductionUnit();
		uncategorized.setCode("UNCATEGORIZED");
		uncategorized.setName("未分类");
		uncategorized.setParent(root);
		productionUnitService.addObj(uncategorized);

		ProjectType projectType = new ProjectType();
		projectType.setCode("ROOTDEVICETYPE");
		projectType.setName("设备类别");
		projectTypeService.addObj(projectType);

		DeviceLevel projectLevel = new DeviceLevel();
		projectLevel.setCode("ROOTDEVICELEVEL");
		projectLevel.setName("设备等级");
		projectLevel.setWeight(0);
		deviceLevelService.addObj(projectLevel);

		ProjectType sparePartType = new ProjectType();
		sparePartType.setCode("ROOTSPAREPARTTYPE");
		sparePartType.setName("备件类别");
		projectTypeService.addObj(sparePartType);

		ProjectType breakdownReasonType = new ProjectType();
		breakdownReasonType.setCode("ROOTBREAKDOWNREASONTYPE");
		breakdownReasonType.setName("故障原因类别");
		projectTypeService.addObj(breakdownReasonType);
		
		ProjectType maintenanceItemType = new ProjectType();
		maintenanceItemType.setCode("ROOTMAINTENANCEITEMTYPE");
		maintenanceItemType.setName("维修项目类别");
		projectTypeService.addObj(maintenanceItemType);


		RelatedDocumentType type = new RelatedDocumentType();
		type.setModuleCode(Constant.RelatedDoc.DEVICE);
		type.setModuleName(Constant.RelatedDoc.DEVICE_TXT);
		type.setCode("SPOTINSPECTION");
		type.setName("点检项目");
		type.setAllowedDelete(false);
		type.setDisabled(false);
		type.setCreateDate(new Date());
		relatedDocumentTypeService.addObj(type);
		
		RelatedDocumentType type5 = new RelatedDocumentType();
		type5.setModuleCode(Constant.RelatedDoc.DEVICE);
		type5.setModuleName(Constant.RelatedDoc.DEVICE_TXT);
		type5.setCode("MAINTENANCEITEM");
		type5.setName("维修项目");
		type5.setAllowedDelete(false);
		type5.setDisabled(false);
		type5.setCreateDate(new Date());
		relatedDocumentTypeService.addObj(type5);
		
		RelatedDocumentType type0 = new RelatedDocumentType();
		type0.setModuleCode(Constant.RelatedDoc.EMPLOYEE);
		type0.setModuleName(Constant.RelatedDoc.EMPLOYEE_TXT);
		type0.setCode("EMPLOYEE");
		type0.setName("员工");
		type0.setAllowedDelete(false);
		type0.setDisabled(false);
		type0.setCreateDate(new Date());
		relatedDocumentTypeService.addObj(type0);
		
		RelatedDocumentType type1 = new RelatedDocumentType();
		type1.setModuleCode(Constant.RelatedDoc.EQUIPMENT);
		type1.setModuleName(Constant.RelatedDoc.EQUIPMENT_TXT);
		type1.setCode("EQUIPMENT");
		type1.setName("装备");
		type1.setAllowedDelete(false);
		type1.setDisabled(false);
		type1.setCreateDate(new Date());
		relatedDocumentTypeService.addObj(type1);
		
		RelatedDocumentType type2 = new RelatedDocumentType();
		type2.setModuleCode(Constant.RelatedDoc.MEASURINGTOOL);
		type2.setModuleName(Constant.RelatedDoc.MEASURINGTOOL_TXT);
		type2.setCode("MEASURINGTOOL");
		type2.setName("量具");
		type2.setAllowedDelete(false);
		type2.setDisabled(false);
		type2.setCreateDate(new Date());
		relatedDocumentTypeService.addObj(type2);
		
		RelatedDocumentType type3 = new RelatedDocumentType();
		type3.setModuleCode(Constant.RelatedDoc.MOULD);
		type3.setModuleName(Constant.RelatedDoc.MOULD_TXT);
		type3.setCode("MOULD");
		type3.setName("模具");
		type3.setAllowedDelete(false);
		type3.setDisabled(false);
		type3.setCreateDate(new Date());
		relatedDocumentTypeService.addObj(type3);
		
		RelatedDocumentType type4 = new RelatedDocumentType();
		type4.setModuleCode(Constant.RelatedDoc.PRODUCTIONLINE);
		type4.setModuleName(Constant.RelatedDoc.PRODUCTIONLINE_TXT);
		type4.setCode("PRODUCTIONLINE");
		type4.setName("产线");
		type4.setAllowedDelete(false);
		type4.setDisabled(false);
		type4.setCreateDate(new Date());
		relatedDocumentTypeService.addObj(type4);
		
		RelatedDocumentType type6 = new RelatedDocumentType();
		type6.setModuleCode(Constant.RelatedDoc.DEVICE);
		type6.setModuleName(Constant.RelatedDoc.DEVICE_TXT);
		type6.setCode("MAINTENANCEDOC");
		type6.setName("保养文档");
		type6.setAllowedDelete(false);
		type6.setDisabled(false);
		type6.setCreateDate(new Date());
		relatedDocumentTypeService.addObj(type5);
		
		MaintenanceType firstLevel = new MaintenanceType();
		firstLevel.setAllowedDelete(false);
		firstLevel.setCode("A");
		firstLevel.setName("一级保养");
		firstLevel.setNote("系统初始化保养类别，不允许删除！");
		maintenanceTypeService.addObj(firstLevel);
		
		MaintenanceType secondLevel = new MaintenanceType();
		secondLevel.setAllowedDelete(false);
		secondLevel.setCode("B");
		secondLevel.setName("二级保养");
		secondLevel.setNote("系统初始化保养类别，不允许删除！");
		maintenanceTypeService.addObj(secondLevel);

		Config cfg = new Config();
		cfg.setKey("autoDispatch");
		cfg.setValue("on");
		configService.addObj(cfg);

		//参数类型
		ParameterType parent1 = new ParameterType();
		parent1.setCode("ART");
		parent1.setName("工艺参数");

		parameterTypeService.addObj(parent1);
		ParameterType parent2 = new ParameterType();
		parent2.setCode("DEVICE");
		parent2.setName("设备参数");
		
		//故障或按灯类型
		PressLightType plt = new PressLightType();
		plt.setCode("TOPTYPE");
		plt.setName("损时类别");
		plt.setText("损时类别");
		plt.setTypeName(Constant.PressLightTypeName.PRESSLIGHT);

		PressLightType plt2 = new PressLightType();
		plt2.setCode("TOPTYPE");
		plt2.setName("故障类别");
		plt2.setText("故障类别");
		plt2.setTypeName(Constant.PressLightTypeName.EQUIPMENT);

		pressLightTypeService.addObj(plt);
		pressLightTypeService.addObj(plt2);

		PressLightType employee = new PressLightType();
		employee.setCode(Constant.PressLightType.EMPLOYEE);
		employee.setName("人员");
		employee.setText("人员");
		employee.setLevel(1);
		employee.setParent(plt);
		employee.setTypeName(Constant.PressLightTypeName.PRESSLIGHT);
		pressLightTypeService.addObj(employee);

		PressLightType device = new PressLightType();
		device.setCode(Constant.PressLightType.DEVICE);
		device.setName("设备");
		device.setText("设备");
		device.setLevel(1);
		device.setParent(plt);
		device.setTypeName(Constant.PressLightTypeName.PRESSLIGHT);
		pressLightTypeService.addObj(device);

		PressLightType materiel = new PressLightType();
		materiel.setCode(Constant.PressLightType.MATERIEL);
		materiel.setName("物料");
		materiel.setText("物料");
		materiel.setLevel(1);
		materiel.setParent(plt);
		materiel.setTypeName(Constant.PressLightTypeName.PRESSLIGHT);
		pressLightTypeService.addObj(materiel);

		PressLightType method = new PressLightType();
		method.setCode(Constant.PressLightType.METHOD);
		method.setName("方法");
		method.setText("方法");
		method.setLevel(1);
		method.setParent(plt);
		method.setTypeName(Constant.PressLightTypeName.PRESSLIGHT);
		pressLightTypeService.addObj(method);

		PressLightType secureEnvironment = new PressLightType();
		secureEnvironment.setCode(Constant.PressLightType.SECUREENVIRONMENT);
		secureEnvironment.setName("安环");
		secureEnvironment.setText("安环");
		secureEnvironment.setLevel(1);
		secureEnvironment.setParent(plt);
		secureEnvironment.setTypeName(Constant.PressLightTypeName.PRESSLIGHT);
		pressLightTypeService.addObj(secureEnvironment);

		PressLightType measure = new PressLightType();
		measure.setCode(Constant.PressLightType.MEASURE);
		measure.setName("测量");
		measure.setText("测量");
		measure.setLevel(1);
		measure.setParent(plt);
		measure.setTypeName(Constant.PressLightTypeName.PRESSLIGHT);
		pressLightTypeService.addObj(measure);

		parameterTypeService.addObj(parent2);

		ProcessType rootProcessType = new ProcessType();
		rootProcessType.setCode("root");
		rootProcessType.setName("工序");
		processTypeService.addObj(rootProcessType);

		EquipmentType equipmentType = new EquipmentType();
		equipmentType.setCode("EQUIPMENT");
		equipmentType.setName("装备");
		equipmentTypeService.addObj(equipmentType);

		EquipmentType measuringtool = new EquipmentType();
		measuringtool.setCode("MEASURINGTOOL");
		measuringtool.setName("量具");
		equipmentTypeService.addObj(measuringtool);
	}
	@Test
	public void init2(){
		MCCommonType commonType0 = new MCCommonType();
		commonType0.setCode("PressLight");
		commonType0.setName("按灯");
		mcCommonTypeService.addObj(commonType0);

		MCCommonType commonType1 = new MCCommonType();
		commonType1.setCode("NG");
		commonType1.setName("ng录入");
		mcCommonTypeService.addObj(commonType1);

		MCCommonType commonType2 = new MCCommonType();
		commonType2.setCode("LostTime");
		commonType2.setName("损时");
		mcCommonTypeService.addObj(commonType2);

		MCCommonType pl = mcCommonTypeService.queryByProperty("code", "PressLight");
		MCCommonType commonType3 = new MCCommonType();
		commonType3.setCode("lightoutPressLight");
		commonType3.setName("按灯熄灯");
		commonType3.setParentsId(pl.getId());
		mcCommonTypeService.addObj(commonType3);

		MCCommonType commonType4 = new MCCommonType();
		commonType4.setCode("recoverPressLight");
		commonType4.setName("按灯恢复");
		commonType4.setParentsId(pl.getId());
		mcCommonTypeService.addObj(commonType4);

		MCCommonType commonType5 = new MCCommonType();
		commonType5.setCode("confirmPressLight");
		commonType5.setName("按灯确认");
		commonType5.setParentsId(pl.getId());
		mcCommonTypeService.addObj(commonType5);

		MCCommonType outpl = mcCommonTypeService.queryByProperty("code", "lightoutPressLight");
		MCCommonSuggestion commonSuggestion1 = new MCCommonSuggestion();
		commonSuggestion1.setText("同意");
		commonSuggestion1.setCommonType(outpl);
		mcCommonSuggestionService.addObj(commonSuggestion1);

		MCCommonSuggestion commonSuggestion2 = new MCCommonSuggestion();
		commonSuggestion2.setText("不同意");
		commonSuggestion2.setCommonType(outpl);
		mcCommonSuggestionService.addObj(commonSuggestion2);

		MCCommonType recoverpl = mcCommonTypeService.queryByProperty("code", "recoverPressLight");
		MCCommonSuggestion recoverSuggestion1 = new MCCommonSuggestion();
		recoverSuggestion1.setText("同意");
		recoverSuggestion1.setCommonType(recoverpl);
		mcCommonSuggestionService.addObj(recoverSuggestion1);

		MCCommonSuggestion recoverSuggestion2 = new MCCommonSuggestion();
		recoverSuggestion2.setText("不同意");
		recoverSuggestion2.setCommonType(recoverpl);
		mcCommonSuggestionService.addObj(recoverSuggestion2);

		MCCommonType confirmpl = mcCommonTypeService.queryByProperty("code", "confirmPressLight");
		MCCommonSuggestion confirmSuggestion1 = new MCCommonSuggestion();
		confirmSuggestion1.setText("同意");
		confirmSuggestion1.setCommonType(confirmpl);
		mcCommonSuggestionService.addObj(confirmSuggestion1);

		MCCommonSuggestion confirmSuggestion2 = new MCCommonSuggestion();
		confirmSuggestion2.setText("不同意");
		confirmSuggestion2.setCommonType(confirmpl);
		mcCommonSuggestionService.addObj(confirmSuggestion2);

		MCCommonType NG = mcCommonTypeService.queryByProperty("code", "NG");
		MCCommonType NGType1 = new MCCommonType();
		NGType1.setCode("auditNG");
		NGType1.setName("NG审核");
		NGType1.setParentsId(NG.getId());
		mcCommonTypeService.addObj(NGType1);

		MCCommonType NGType2 = new MCCommonType();
		NGType2.setCode("reviewNG");
		NGType2.setName("NG复核");
		NGType2.setParentsId(NG.getId());
		mcCommonTypeService.addObj(NGType2);

		MCCommonType NGType3 = new MCCommonType();
		NGType3.setCode("confirmNG");
		NGType3.setName("NG确认");
		NGType3.setParentsId(NG.getId());
		mcCommonTypeService.addObj(NGType3);

		MCCommonType NGaudit = mcCommonTypeService.queryByProperty("code", "auditNG");
		MCCommonSuggestion NGauditSuggestion1 = new MCCommonSuggestion();
		NGauditSuggestion1.setText("同意");
		NGauditSuggestion1.setCommonType(NGaudit);
		mcCommonSuggestionService.addObj(NGauditSuggestion1);

		MCCommonSuggestion NGauditSuggestion2 = new MCCommonSuggestion();
		NGauditSuggestion2.setText("不同意");
		NGauditSuggestion2.setCommonType(NGaudit);
		mcCommonSuggestionService.addObj(NGauditSuggestion2);

		MCCommonType NGreview = mcCommonTypeService.queryByProperty("code", "reviewNG");
		MCCommonSuggestion NGreviewSuggestion1 = new MCCommonSuggestion();
		NGreviewSuggestion1.setText("同意");
		NGreviewSuggestion1.setCommonType(NGreview);
		mcCommonSuggestionService.addObj(NGreviewSuggestion1);

		MCCommonSuggestion NGreviewSuggestion2 = new MCCommonSuggestion();
		NGreviewSuggestion2.setText("不同意");
		NGreviewSuggestion2.setCommonType(NGreview);
		mcCommonSuggestionService.addObj(NGreviewSuggestion2);

		MCCommonType NGconfirm = mcCommonTypeService.queryByProperty("code", "confirmNG");
		MCCommonSuggestion NGconfirmSuggestion1 = new MCCommonSuggestion();
		NGconfirmSuggestion1.setText("同意");
		NGconfirmSuggestion1.setCommonType(NGconfirm);
		mcCommonSuggestionService.addObj(NGconfirmSuggestion1);

		MCCommonSuggestion NGconfirmSuggestion2 = new MCCommonSuggestion();
		NGconfirmSuggestion2.setText("不同意");
		NGconfirmSuggestion2.setCommonType(NGconfirm);
		mcCommonSuggestionService.addObj(NGconfirmSuggestion2);

		MCCommonType LostTime = mcCommonTypeService.queryByProperty("code", "LostTime");
		MCCommonType LostTime1 = new MCCommonType();
		LostTime1.setCode("LostTimeConfirm");
		LostTime1.setName("损时确认");
		LostTime1.setParentsId(LostTime.getId());
		mcCommonTypeService.addObj(LostTime1);

		MCCommonType LostTimeConfirm = mcCommonTypeService.queryByProperty("code", "LostTimeConfirm");
		MCCommonSuggestion LostTimeConfirmSuggestion1 = new MCCommonSuggestion();
		LostTimeConfirmSuggestion1.setText("同意");
		LostTimeConfirmSuggestion1.setCommonType(LostTimeConfirm);
		mcCommonSuggestionService.addObj(LostTimeConfirmSuggestion1);

		MCCommonSuggestion LostTimeConfirmSuggestion2 = new MCCommonSuggestion();
		LostTimeConfirmSuggestion2.setText("不同意");
		LostTimeConfirmSuggestion2.setCommonType(LostTimeConfirm);
		mcCommonSuggestionService.addObj(LostTimeConfirmSuggestion2);
	}
	@Test
	public void addModule(){
		Module module22 = moduleService.queryByProperty("name", "执行数据");
		/*Module son7 = new Module();
		son7.setParent(module22);
		son7.setPriority(2);
		son7.setName("损时记录");
		son7.setLeaf(true);
		son7.setUrl("console/jsp/lostTimeRecord.jsp");
		moduleService.addModule(son7);


		Module workflow = new Module();
		workflow.setParent(sysSetChild);
		workflow.setDisabled(false);
		workflow.setLeaf(true);
		workflow.setPriority(70);
		workflow.setName("流程管理");
		workflow.setUrl("console/jsp/workflow.jsp");
		moduleService.addModule(workflow);
		Module module2 = moduleService.queryByProperty("name", "生产管理");

		Module module22 = new Module();
		module22.setName("执行数据");
		module22.setPriority(2);
		module22.setParent(module2);
		moduleService.addModule(module22);

		Module module221 = new Module();
		module221.setName("设备执行");
		module221.setPriority(1);
		module221.setParent(module22);
		module221.setLeaf(true);
		module221.setUrl("console/jsp/processRecord.jsp");
		moduleService.addModule(module221);*/

		Module module222 = new Module();
		module222.setName("文档管控");
		module222.setPriority(2);
		module222.setParent(module22);
		module222.setLeaf(true);
		module222.setUrl("console/jsp/fileMgr.jsp");
		moduleService.addModule(module222);
	}
}
