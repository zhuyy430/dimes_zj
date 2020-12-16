package com.digitzones.constants;

public class Constant {
	/**
	 * opc序号与生产序号映射常量类
	 * @author zdq
	 * 2018年12月20日
	 */
	public static class OpcMapping{
		/**复位时，设置生产序号的值常量*/
		public static final String UNSIGNED = "UNSIGNED";
	}
	/***
	 * 存放状态常量
	 * @author zdq
	 * 2018年12月18日
	 */
	public static  class Status{
		/**点检计划：未完成状态*/
		public static final String CHECKINGPLAN_UNCOMPLETE="未完成";
		/**点检计划：完成状态*/
		public static final String CHECKINGPLAN_COMPLETE="已完成";
		/**点检计划：计划状态*/
		public static final String CHECKINGPLAN_PLAN="计划";
		
		/**保养计划：未完成状态*/
		public static final String MAINTENANCEPLAN_UNCOMPLETE="未完成";
		/**保养计划：完成状态*/
		public static final String MAINTENANCEPLAN_COMPLETE="已完成";
		/**保养计划：待派单*/
		public static final String MAINTENANCEPLAN_PLAN="待派单";
		/**保养计划：等待接单确认*/
		public static final String MAINTENANCEPLAN_RECEIPT="待接单";
		/**保养计划：保养中*/
		public static final String MAINTENANCEPLAN_MAINTENANCING="保养中";
		/**保养计划：待确认*/
		public static final String MAINTENANCEPLAN_TOBECONFIRMED="待确认";
		
		
		/**推送标题*/
		public static final String MAINTAINTITLE_ASSIGN="接收任务!";
		public static final String MAINTAINTITLE_CONFIRM="确认保养单!";
		/**推送内容*/
		public static final String MAINTAINCONTENT_ASSIGN="您有一个分配任务请确认!";
		public static final String MAINTAINCONTENT_CONFIRM="您有一个保养单完成，请确认";
	}
	
	/***
	 * 计划周期类型
	 * @author zdq
	 * 2018年12月18日
	 */
	public static class CycleType{
		/**每班*/
		public static final String FOR_CLASS="forClass";
		/**每天*/
		public static final String FOR_DAY="forDay";
		/**每周*/
		public static final String FOR_WEEK="forWeek";
		/**每月*/
		public static final String FOR_MONTH="forMonth";
		/**每隔一段时间*/
		public static final String FOR_DURATION="forDuration";
	}
	/***
	 * 模具类型类型
	 * @author zdq
	 * 2018年12月18日
	 */
	public static class MouldType{
		/**常规模具*/
		public static final String MOULD_COMMON="common";
		/**试样模具*/
		public static final String MOULD_SAMPLE="sample";
		/*整模*/
		public static final String MOULD_COMPLETE="complete";
		/**组装模*/
		public static final String ASSEMBLY="forMonth";
	}
	/**
	 * 接单类型
	 * @author Administrator
	 */
	public static class ReceiveType{
		/**系统派单*/
		public static final String SYSTEMGASSIGN="SYSTEMGASSIGN";
		/**人工派单*/
		public static final String ARTIFICIALGASSIGN="ARTIFICIALGASSIGN";
		/**协助*/
		public static final String ASSIST="ASSIST";
		/**抢单*/
		public static final String ROBLIST="ROBLIST";
		/**返修*/
		public static final String REWORK="REWORK";
		/**转单*/
		public static final String TRANSFER="TRANSFER";
	}
	/**
	 * 设备报修单状态
	 * @author Administrator
	 */
	public static class DeviceRepairStatus{
		/**等待派单*/
		public static final String WAITINGASSIGN="WAITINGASSIGN";
		/**等待接单确认:待接单*/
		public static final String WAITINCOMFIRM="WAITINCOMFIRM";
		/**维修中*/
		public static final String MAINTAINING="MAINTAINING";
		/**车间确认*/
		public static final String WORKSHOPCOMFIRM="WORKSHOPCOMFIRM";
		/**待车间确认*/
		public static final String WAITWORKSHOPCOMFIRM="WAITWORKSHOPCOMFIRM";
		/**维修完成*/
		public static final String MAINTAINCOMPLETE="MAINTAINCOMPLETE";
		/**带病运行*/
		public static final String FAIL_SAFEOPERATION="FAIL_SAFEOPERATION";


		/**推送标题*/
		public static final String DEVICEREPAIRTITLE_ASSIGN="接收任务!";
		public static final String DEVICEREPAIRTITLE_CONFIRM="确认报修单!";
		public static final String DEVICEREPAIRTITLE_NOSEND="报修单未派单超时!";
		/**推送内容*/
		public static final String DEVICEREPAIRCONTENT_ASSIGN="您有一个分配任务请确认!";
		public static final String DEVICEREPAIRCONTENT_CONFIRM="您有一个报修单完成，请确认";
		public static final String DEVICEREPAIRCONTENT_NOSEND="您有一个未派单的报修单已超时，请及时处理!";
	}
	/**
	 * 维修人员状态
	 * @author Administrator
	 */
	public static class MaintenanceStaffStatus{
		/**在岗*/
		public static final String ONDUTY="ONDUTY";
		/**休息*/
		public static final String REST="REST";
		/**夜班*/
		public static final String NIGHTSHIFT="NIGHTSHIFT";
		/**保养*/
		public static final String MAINTENANCE="MAINTENANCE";
		/**维修*/
		public static final String MAINTAIN="MAINTAIN";
		/**公出*/
		public static final String BEOUT="BEOUT";
	}
	/**
	 * 物品类型
	 * 如：类型可以为备品备件类型或其他类型
	 * 在库存与物品映射中使用(WarehouseGoodsMapping)
	 * @author Administrator
	 */
	public static class GoodType{
		/**备品备件类型*/
		public static final String SPAREPARTTYPE="sparepartType";
	}
	
	/**超级管理员*/
	public static final String ADMIN = "admin";
	/**
	 * 设备项目类型
	 * @author Administrator
	 */
	public static class DeviceProject{
		/**点检项目*/
		public static final String SPOTINSPECTION = "SPOTINSPECTION";
		/**润滑项目*/
		public static final String LUBRICATION = "LUBRICATION";
		/**保养项目*/
		public static final String MAINTAIN = "MAINTAIN";
		/**故障原因*/
		public static final String BREAKDOWNREASON = "BREAKDOWNREASON";
		/**维修项目*/
		public static final String MAINTENANCEITEM = "MAINTENANCEITEM";
	}
	/**
	 * 项目类型
	 * @author Administrator
	 */
	public static class ProjectType{
		/**设备类型*/
		public static final String DEVICE_TYPE="deviceType";
		/**备件类型*/
		public static final String SPAREPART_TYPE="sparePartType";
		/**故障原因类型*/
		public static final String BREAKDOWNREASON_TYPE="breakdownReasonType";
		/**维修项目类型*/
		public static final String MAINTENANCEITEM_TYPE="maintenanceItemType";
		
		
		/**设备类型根节点编码*/
		public static final String ROOTDEVICETYPE="ROOTDEVICETYPE";
		/**设备等级根节点编码*/
		public static final String ROOTDEVICELEVEL="ROOTDEVICELEVEL";
		/**备件类型根节点编码*/
		public static final String ROOTSPAREPARTTYPE="ROOTSPAREPARTTYPE";
		/**故障原因类型根节点编码*/
		public static final String ROOTBREAKDOWNREASONTYPE="ROOTBREAKDOWNREASONTYPE";
		/**维修项目类型根节点编码*/
		public static final String ROOTMAINTENANCEITEMTYPE="ROOTMAINTENANCEITEMTYPE";
	}
	
	/**系统允许最大用户数 */
	public static final int USER_COUNT = 10000;
	/**是否开启用户上限检测过滤器*/
	public static boolean isOpenUserCountFilter = false;
	
	/**系统用户数达到上限提示字符串*/
	public static final String USER_EXCEED_MSG = "用户数已达系统上限，如需开通更多用户，请联系开发商!";
	
	/**
	 * 工单详情静态内存存储列表
	 */
	public static String WORKSHEETDETAIL = "workSheetDetail";
	/**
	 * 存储工件id，只在添加工单时使用
	 */
	public static String WORKPIECEID = "workpieceId";//"UNSIGNED";
	/**
	 * 相关文档相关的常量
	 */
	public static class RelatedDoc{
		/**跟备品备件相关的文档*/
		public static final String SPAREPART="sparepart";
		public static final String SPAREPART_TXT="备品备件";
		/**跟设备相关的文档*/
		public static final String DEVICE="device";
		public static final String DEVICE_TXT="设备";
		/**跟工件相关的文档*/
		public static final String WORKPIECE="workpiece";
		public static final String WORKPIECE_TXT="工件";
		/**跟量具相关的文档*/
		public static final String MEASURINGTOOL="measuringtool";
		public static final String MEASURINGTOOL_TXT="量具";
		/**跟装备相关的文档*/
		public static final String EQUIPMENT="equipment";
		public static final String EQUIPMENT_TXT="装备";
		/**跟员工相关的文档*/
		public static final String EMPLOYEE="employee";
		public static final String EMPLOYEE_TXT="员工";
		/**跟产线相关的文档*/
		public static final String PRODUCTIONLINE="production";
		public static final String PRODUCTIONLINE_TXT="产线";
		/**跟模具相关的文档*/
		public static final String MOULD="mould";
		public static final String MOULD_TXT="模具";
		/**跟保养相关的文档*/
		public static final String MAINTENANCE="MAINTENANCEDOC";
		public static final String MAINTENANCE_TXT="保养";
	}
	/**
	 * 设备站点常量
	 * @author zdq
	 * 2018年6月23日
	 */
	public static class DeviceSite{
		/**站点运行状态：运行*/
		public static final String RUNNING = "0";
		/**站点运行状态：待机*/
		public static final String STANDBY = "1";
		/**站点运行状态：停机*/
		public static final String HALT = "2";
	}
	/**
	 * 用户常量
	 * @author zdq
	 * 2018年6月23日
	 */
	public static class User{
		/**存放在session中登录用户的key*/
		public static final String LOGIN_USER = "loginUser";
		/**MC端登录用户key*/
		public static final String MC_LOGIN_USER = "MCLoginUser";
		/**超级管理员用户名*/
		public static final String ADMIN="admin";
	}
	/**
	 * 加工信息常量
	 * @author zdq
	 * 2018年6月25日
	 */
	public static class ProcessRecord{
		/**不良品*/
		public static final String NG = "ng";
		public static final String OK = "ok";
		/**返修*/
		public static final String REPAIR = "repair";
		/**报废*/
		public static final String SCRAP = "scrap";
		/**让步接收*/
		public static final String COMPROMISE = "compromise";
		
	}
	/**
	 * 参数类型
	 * @author zdq
	 * 2018年6月27日
	 */
	public static class ParameterType{
		/**工艺参数*/
		public static final String ART="ART";
		/**设备参数*/
		public static final String DEVICE="DEVICE";
	}
	/**
	 * 装备类型
	 * @author zdq
	 * 2018年6月27日
	 */
	public static class EquipmentType{
		/**装备*/
		public static final String EQUIPMENT="EQUIPMENT";
		/**模具*/
		public static final String MEASURINGTOOL="MEASURINGTOOL";
	}
	/**
	 * 装备
	 * @author zdq
	 * 2018年6月28日
	 */
	public static class Equipment{
		/**计时型*/
		public static final String TIMING = "TIMING";
		/**计数型*/
		public static final String COUNTING = "COUNTING";
	}
	/**
	 *  工单常量类
	 * @author zdq
	 * 2018年7月10日
	 */
	public static class WorkSheet{
		/**普通工单*/
		public static final String COMMON = "common";
		/**返修工单*/
		public static final String REPAIR = "repair";
		/**
		 * 工单状态
		 * @author Administrator
		 */
		public static class Status{
			/**计划*/
			public static final String PLAN="0";
			/**加工中*/
			public static final String PROCESSING="1";
			/**停工*/
			public static final String STOP="2";
			/**完工*/
			public static final String COMPLETE="3";
		}
	}
	/**
	 * @author zdq
	 * 2018年7月23日
	 */
	public static  class PressLightType{
		/**人员*/
		public static final String EMPLOYEE="employee";
		/**设备*/
		public static final String DEVICE="device";
		/**物料*/
		public static final String MATERIEL="materiel";
		/**方法*/
		public static final String METHOD="method";
		/**安环*/
		public static final String SECUREENVIRONMENT="secureEnvironment";
		/**测量*/
		public static final String MEASURE="measure";
	}
	/**
	 * 故障类别
	 */
	public static  class PressLightTypeName{
		/**按灯故障*/
		public static final String PRESSLIGHT="pressLightType";
		/**装备故障*/
		public static final String EQUIPMENT="equipment";
	}
	/**
	 * 工作流静态类
	 * @author zdq
	 * 2018年8月2日
	 */
	public static class Workflow{
		/**删除流程内记录提示信息*/
		public static final String DELETE_TIP = "该记录已在流程内，不允许删除!";
		/**
		 * 工作流类型
		 * @author Administrator
		 */
		public static class Type{
			/**不合格品记录*/
			public static final String NG = "ng";
			/**损时记录*/
			public static final String LOSTTIME = "lostTime";
			/**按灯记录*/
			public static final String PRESSLIGHT = "pressLight";
			/**报修单 记录*/
			public static final String DEVICEREPAIR = "deviceRepair";
		}
		/**意见*/
		public static final String SUGGESTION = "suggestion";
		/**启动流程的用户ID*/
		public static final String APPLY_USER_ID = "applyUserId";
		/**损时确认角色*/
		public static final String LOSTTIME_CONFIRM_ROLES = "lostTimeConfirmRoles";
		/**损时候选确认人*/
		public static final String LOSTTIME_CONFIRM_PERSONS = "lostTimeConfirmPersons";
		/**损时确认人*/
		public static final String LOSTTIME_CONFIRM_PERSON = "lostTimeConfirmPerson";
		/**熄灯人*/
		public static final String LIGHTOUT_PERSON = "lightoutPerson";
		/**熄灯候选人*/
		public static final String LIGHTOUT_PERSONS = "lightoutPersons";
		/**熄灯角色*/
		public static final String LIGHTOUT_ROLES = "lightoutRoles";
		/**按灯恢复人*/
		public static final String LIGHTOUT_RECOVER_PERSON = "lightoutRecoverPerson";
		/**按灯恢复候选人*/
		public static final String LIGHTOUT_RECOVER_PERSONS = "lightoutRecoverPersons";
		/**按灯恢复角色*/
		public static final String LIGHTOUT_RECOVER_ROLES = "lightoutRecoverRoles";
		/**按灯确认人*/
		public static final String LIGHTOUT_CONFIRM_PERSON = "lightoutConfirmPerson";
		/**按灯确认候选人*/
		public static final String LIGHTOUT_CONFIRM_PERSONS = "lightoutConfirmPersons";
		/**按灯确认角色*/
		public static final String LIGHTOUT_CONFIRM_ROLES = "lightoutConfirmRoles";
		/**不合格品审核人*/
		public static final String NG_AUDIT_PERSON = "ngAuditPerson";
		/**不合格品候选审核人*/
		public static final String NG_AUDIT_PERSONS = "ngAuditPersons";
		/**不合格品候选审核角色*/
		public static final String NG_AUDIT_ROLES = "ngAuditRoles";
		/**不合格品复核人*/
		public static final String NG_REVIEW_PERSON = "ngReviewPerson";
		/**不合格品候选复核人*/
		public static final String NG_REVIEW_PERSONS = "ngReviewPersons";
		/**不合格品候选复核角色*/
		public static final String NG_REVIEW_ROLES = "ngReviewRoles";
		/**不合格品确认人*/
		public static final String NG_CONFIRM_PERSON = "ngConfirmPerson";
		/**不合格品候选确认人*/
		public static final String NG_CONFIRM_PERSONS = "ngConfirmPersons";
		/**不合格品候选确认角色*/
		public static final String NG_CONFIRM_ROLES = "ngConfirmRoles";
	}
	/**
	 * 角色常量
	 * @author Administrator
	 */
	public static class Role{
		public static final String NG_AUDIT_ROLE = "NG审核人";
		public static final String NG_REVIEW_ROLE = "NG复核人";
		public static final String NG_CONFIRM_ROLE = "NG确认人";
		public static final String LIGHTOUT_RECOVER_ROLE = "按灯恢复人";
		public static final String LIGHTOUT_ROLE = "熄灯人";
		public static final String LIGHTOUT_CONFIRM_ROLE = "按灯确认人";
		public static final String LOSTTIME_CONFIRM_ROLE = "损时确认人";
	}
	/**
	 * Oee类型
	 * @author Administrator
	 */
	public static class OeeType{
		public static final String OEETYPE_CURRENTDAY = "CURRENTDAY";  //当天
		public static final String OEETYPE_CURRENTMONTH = "CURRENTMONTH";  //当月
		public static final String OEETYPE_PREMONTH = "PREMONTH";  //上月
		public static final String OEETYPE_UNITDATA= "UNITDATA";  //生产单元
	}
	public static class AppTaskStatus{
		public static final String TASKSTATUS_CREATE = "CREATE";  //创建的
		public static final String TASKSTATUS_RECEIVE = "RECEIVE";  //收到的
		public static final String TASKSTATUS_COMPLETE = "COMPLETE";  //已完成的
		public static final String TASKTITLE_COMPLETE = "完成任务";  //已完成推送标题
		public static final String TASKCONTENT_COMPLETE = "您的一个任务已完成，请确认!";  //已完成推送内容
		public static final String TASKTITLE_ASSIGN = "接收任务";  //分配推送标题
		public static final String TASKCONTENT_ASSIGN = "您收到一个任务未完成，请确认!";  //分配推送内容
	}
}
