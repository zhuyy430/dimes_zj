<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>" />
<meta charset="utf-8" />
<!-- 浏览器标签图片 -->
<link rel="shortcut icon" href="console/js/topjui/images/favicon.ico" />
<!-- TopJUI框架样式 -->
<link type="text/css" href="console/js/topjui/css/topjui.core.min.css"
	rel="stylesheet">
<link type="text/css"
	href="console/js/topjui/themes/default/topjui.black.css"
	rel="stylesheet" id="dynamicTheme" />
<!-- FontAwesome字体图标 -->
<link type="text/css"
	href="console/js/static/plugins/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" />
<link type="text/css"
	href="console/js/static/plugins/layui/css/layui.css" rel="stylesheet" />
<link type="text/css" href="console/js/static/public/css/font.css"
	rel="stylesheet" />
<link type="text/css" href="console/js/static/public/css/main.css"
	rel="stylesheet" />
<!-- jQuery相关引用 -->
<script type="text/javascript"
	src="console/js/static/plugins/jquery/jquery.min.js"></script>

<!-- <script type="text/javascript" src="common/js/echarts.js"></script>
 -->
<script type="text/javascript"
	src="console/js/static/plugins/jquery/jquery.cookie.js"></script>
<!-- TopJUI框架配置 -->
<script type="text/javascript"
	src="console/js/static/public/js/topjui.config.js"></script>
<!-- TopJUI框架核心 -->
<script type="text/javascript"
	src="console/js/topjui/js/topjui.core.min.js"></script>
<!-- TopJUI中文支持 -->
<script type="text/javascript"
	src="console/js/topjui/js/locale/topjui.lang.zh_CN.js"></script>
<!-- layui框架js -->
<script type="text/javascript"
	src="console/js/static/plugins/layui/layui.js" charset="utf-8"></script>
<!-- 首页js -->
<script type="text/javascript"
	src="console/js/static/public/js/topjui.index.js" charset="utf-8"></script>
<script type="text/javascript"
	src="common/js/common.js" charset="utf-8"></script>
<script>
$(function(){
	index();
});
//设备管理首页
function index(){
	$.get("deviceManageHome/index.do",function(result){
		//维修人员在岗人数
		$("#employeeOnLine").text(result.countOnPosition);
		//今日保养设备
		$("#maintenaceCount").text(result.maintenancePlanRecordCount);
		//今日报修
		$("#repairCount").text(result.deviceRepairOrderCount);
		//维修转单
		$("#changeOrderCount").text(result.transferRecordCount);
		//自动派单
		var autoDispatch = result.autoDispatch;
		switch(autoDispatch){
		case "on":{
			$("#autoDispatch").text("on");
			$("#autoDispatchIcon").removeClass("fa-toggle-off");
			$("#autoDispatchDiv").css("background-color","#5FB878");
			if(!$("#autoDispatchIcon").hasClass("fa-toggle-on")){
				$("#autoDispatchIcon").addClass("fa-toggle-on");
			}
			break;
		}
		case "off":{
			$("#autoDispatch").text("off");
			$("#autoDispatchIcon").removeClass("fa-toggle-on");
			$("#autoDispatchDiv").css("background-color","#2F4056");
			if(!$("#autoDispatchIcon").hasClass("fa-toggle-off")){
				$("#autoDispatchIcon").addClass("fa-toggle-off");
			}
			break;
		}
		default:"";
		}
		//设备维修单-待接单
		repair_receipting(result.deviceRepairReceipting);
		//设备维修单-待确认
		repair_confirming(result.deviceRepairConfirming);
		//设备保养-待接单
		maintenance_receipting(result.maintenancePlanRecordReceipting);
		//设备保养-待确认
		maintenance_confirming(result.maintenancePlanRecordConfirming);
	});
}
//设备保养-待确认
function maintenance_confirming(receiptingList){
	var $tbody = $("#maintenance_confirming");
	$tbody.empty();
	if(receiptingList!=null &&receiptingList.length>0){
		for(var i = 0;i<receiptingList.length;i++){
			var maintenanceUser = receiptingList[i];
			var $tr = $("<tr>");
			var maintenanceDate = $("<td>");
			maintenanceDate.append(getDateTime(new Date(maintenanceUser.maintenancePlanRecord.maintenanceDate)));
			$tr.append(maintenanceDate);
			
			var maintenancedDate = $("<td>");
			maintenancedDate.append(getDateTime(new Date(maintenanceUser.maintenancePlanRecord.maintenancedDate)));
			$tr.append(maintenancedDate);
			
			var deviceCode = $("<td>");
			deviceCode.append(maintenanceUser.maintenancePlanRecord.device.code);
			$tr.append(deviceCode); 
			
			var deviceName = $("<td>");
			deviceName.append(maintenanceUser.maintenancePlanRecord.device.name);
			$tr.append(deviceName); 
			
			var unitType = $("<td>");
			unitType.append(maintenanceUser.maintenancePlanRecord.device.unitType);
			$tr.append(unitType); 
			
			var maintenanceType = $("<td>");
			maintenanceType.append(maintenanceUser.maintenancePlanRecord.maintenanceType==null?"":maintenanceUser.maintenancePlanRecord.maintenanceType.name);
			$tr.append(maintenanceType); 
			
			var productionUnit = $("<td>");
			productionUnit.append(maintenanceUser.maintenancePlanRecord.device.productionUnit==null?"":maintenanceUser.maintenancePlanRecord.device.productionUnit.name);
			$tr.append(productionUnit); 
			
			var maintenanceUserName = $("<td>");
			maintenanceUserName.append(maintenanceUser.name);
			$tr.append(maintenanceUserName);
			
			var status = $("<td>");
			status.append(maintenanceUser.maintenancePlanRecord.status);
			$tr.append(status);
			
			var completeDate = $("<td>");
			completeDate.append(maintenanceUser.completeDate==null?"":getDateTime(new Date(maintenanceUser.completeDate)));
			$tr.append(completeDate);
			
			var occupyTime = $("<td>");
			occupyTime.append(maintenanceUser.occupyTime);
			$tr.append(occupyTime);
			
			var oper = $("<td>");
			var $a=$("<a href='javascript:void(0)' onclick='confirmMaintenance("+maintenanceUser.maintenancePlanRecord.id+")'>");
			$a.append("确认");
			oper.append($a);
			$tr.append(oper);
			$tbody.append($tr);
		}
	}
}
//设备保养确认操作
function confirmMaintenance(maintenancePlanRecordId){
	$.get("maintenancePlanRecord/confirm.do",{id:maintenancePlanRecordId},function(result){
		if(result.statusCode==200){
			index();
		}else{
			$.iMessager.alert("警告",result.message);
		}
	});
}
//设备保养-待接单
function maintenance_receipting(receiptingList){
	var $tbody = $("#maintenance_receipting");
	$tbody.empty();
	if(receiptingList!=null &&receiptingList.length>0){
		for(var i = 0;i<receiptingList.length;i++){
			var maintenanceUser = receiptingList[i];
			var $tr = $("<tr>");
			var maintenanceDate = $("<td>");
			maintenanceDate.append(getDateTime(new Date(maintenanceUser.maintenancePlanRecord.maintenanceDate)));
			$tr.append(maintenanceDate);
			
			var deviceCode = $("<td>");
			deviceCode.append(maintenanceUser.maintenancePlanRecord.device.code);
			$tr.append(deviceCode); 
			
			var deviceName = $("<td>");
			deviceName.append(maintenanceUser.maintenancePlanRecord.device.name);
			$tr.append(deviceName); 
			
			var unitType = $("<td>");
			unitType.append(maintenanceUser.maintenancePlanRecord.device.unitType);
			$tr.append(unitType); 
			
			var maintenanceType = $("<td>");
			maintenanceType.append(maintenanceUser.maintenancePlanRecord.maintenanceType==null?"":maintenanceUser.maintenancePlanRecord.maintenanceType.name);
			$tr.append(maintenanceType); 
			
			var productionUnit = $("<td>");
			productionUnit.append(maintenanceUser.maintenancePlanRecord.device.productionUnit==null?"":maintenanceUser.maintenancePlanRecord.device.productionUnit.name);
			$tr.append(productionUnit); 
			
			var maintenanceUserName = $("<td>");
			maintenanceUserName.append(maintenanceUser.name);
			$tr.append(maintenanceUserName);
			
			var oper = $("<td>");
			var $a=$("<a href='javascript:void(0)' onclick='receiptMaintenance("+maintenanceUser.maintenancePlanRecord.id+")'>");
			$a.append("接单");
			oper.append($a);
			$tr.append(oper);
			$tbody.append($tr);
		}
	}
}
//设备保养，接单
function receiptMaintenance(maintenancePlanRecordId){
	$.get("maintenancePlanRecord/receipt.do",{maintenancePlanRecordId:maintenancePlanRecordId},function(result){
		if(result.success){
			index();
		}else{
			alert(result.msg);
		}
	});
}

//设备维修单-待接单
function repair_receipting(receiptingList){
	var $tbody = $("#repair_receipting");
	$tbody.empty();
	if(receiptingList!=null &&receiptingList.length>0){
		for(var i = 0;i<receiptingList.length;i++){
			var deviceRepair = receiptingList[i];
			var $tr = $("<tr>");
			var serialNumber = $("<td>");
			serialNumber.append(deviceRepair.deviceRepair.serialNumber);
			$tr.append(serialNumber);
			
			var createDate = $("<td>");
			createDate.append(getDateTime(new Date(deviceRepair.deviceRepair.createDate)));
			$tr.append(createDate);
			
			var Informant = $("<td>");
			Informant.append(deviceRepair.deviceRepair.informant);
			$tr.append(Informant); 
			
			var deviceCode = $("<td>");
			deviceCode.append(deviceRepair.deviceRepair.device.code);
			$tr.append(deviceCode); 
			
			var deviceName = $("<td>");
			deviceName.append(deviceRepair.deviceRepair.device.name);
			$tr.append(deviceName); 
			
			var unitType = $("<td>");
			unitType.append(deviceRepair.deviceRepair.device.unitType);
			$tr.append(unitType); 
			
			var ngreason = $("<td>");
			ngreason.append(deviceRepair.deviceRepair.ngreason==null?"":deviceRepair.deviceRepair.ngreason.name);
			$tr.append(ngreason); 
			
			var ngDescription = $("<td>");
			ngDescription.append(deviceRepair.deviceRepair.ngDescription);
			$tr.append(ngDescription); 
			
			var productionUnit = $("<td>");
			productionUnit.append(deviceRepair.deviceRepair.productionUnit==null?"":deviceRepair.deviceRepair.productionUnit.name);
			$tr.append(productionUnit); 
			
			var assignTime = $("<td>");
			assignTime.append(getDateTime(new Date(deviceRepair.assignTime)));
			$tr.append(assignTime);
			
			var oper = $("<td>");
			var $a=$("<a href='javascript:void(0)' onclick='receipt("+deviceRepair.id+")'>");
			$a.append("接单");
			oper.append($a);
			$tr.append(oper);
			$tbody.append($tr);
		}
	}
}
//设备维修--接单
function receipt(maintenanceStaffRecordId){
	$.get("deviceRepairOrder/updateDeviceRepairOrderStatusById.do",{status:"MAINTAINING",id:maintenanceStaffRecordId},function(result){
		if(result.statusCode==200){
			index();
		}else{
			alert(result.message);
		}
	});
}
//设备维修单-待确认
function repair_confirming(receiptingList){
	var $tbody = $("#repair_confirming");
	$tbody.empty();
	if(receiptingList!=null &&receiptingList.length>0){
		for(var i = 0;i<receiptingList.length;i++){
			var deviceRepair = receiptingList[i];
			var $tr = $("<tr>");
			var serialNumber = $("<td>");
			serialNumber.append(deviceRepair.deviceRepair.serialNumber);
			$tr.append(serialNumber);
			
			var createDate = $("<td>");
			createDate.append(getDateTime(new Date(deviceRepair.deviceRepair.createDate)));
			$tr.append(createDate);
			
			var Informant = $("<td>");
			Informant.append(deviceRepair.deviceRepair.informant);
			$tr.append(Informant); 
			
			var deviceCode = $("<td>");
			deviceCode.append(deviceRepair.deviceRepair.device.code);
			$tr.append(deviceCode); 
			
			var deviceName = $("<td>");
			deviceName.append(deviceRepair.deviceRepair.device.name);
			$tr.append(deviceName); 
			
			var unitType = $("<td>");
			unitType.append(deviceRepair.deviceRepair.device.unitType);
			$tr.append(unitType); 
			
			var ngreason = $("<td>");
			ngreason.append(deviceRepair.deviceRepair.ngreason==null?"":deviceRepair.deviceRepair.ngreason.name);
			$tr.append(ngreason); 
			
			var ngDescription = $("<td>");
			ngDescription.append(deviceRepair.deviceRepair.ngDescription);
			$tr.append(ngDescription); 
			
			var productionUnit = $("<td>");
			productionUnit.append(deviceRepair.deviceRepair.productionUnit==null?"":deviceRepair.deviceRepair.productionUnit.name);
			$tr.append(productionUnit); 
			
			var assignTime = $("<td>");
			assignTime.append(getDateTime(new Date(deviceRepair.assignTime)));
			$tr.append(assignTime);
			
			var maintainName = $("<td>");
			maintainName.append(deviceRepair.deviceRepair.maintainName);
			$tr.append(maintainName); 
			
			var completeTime = $("<td>");
			completeTime.append(deviceRepair.completeTime==null?"":getDateTime(new Date(deviceRepair.completeTime)));
			$tr.append(completeTime);
			
			var occupyTime = $("<td>");
			occupyTime.append(deviceRepair.occupyTime==null?0:deviceRepair.occupyTime);
			$tr.append(occupyTime);
			
			var oper = $("<td style='width:50px;'>");
			var $a=$("<a href='javascript:void(0)' onclick='confirm("+deviceRepair.id+")'>");
			$a.append("确认");
			oper.append($a);
			$tr.append(oper);
			$tbody.append($tr);
		}
	}
}
//设备维修--确认
function confirm(maintenanceStaffRecordId){
	$.get("deviceRepairOrder/updateDeviceRepairOrderStatusById.do",{status:"MAINTAINING",id:maintenanceStaffRecordId},function(result){
		if(result.statusCode==200){
			index();
		}else{
			alert(result.message);
		}
	});
}
//自动派单切换
function switchAutoDispatch(){
	$.get("deviceManageHome/switchAutoDispatch.do",function(result){
		switch(result){
		case "on":{
			$("#autoDispatch").text("on");
			$("#autoDispatchIcon").removeClass("fa-toggle-off");
			$("#autoDispatchDiv").css("background-color","#5FB878");
			if(!$("#autoDispatchIcon").hasClass("fa-toggle-on")){
				$("#autoDispatchIcon").addClass("fa-toggle-on");
			}
			break;
		}
		case "off":{
			$("#autoDispatch").text("off");
			$("#autoDispatchIcon").removeClass("fa-toggle-on");
			$("#autoDispatchDiv").css("background-color","#2F4056");
			if(!$("#autoDispatchIcon").hasClass("fa-toggle-off")){
				$("#autoDispatchIcon").addClass("fa-toggle-off");
			}
			break;
		}
		}
	});
}
//添加tab标签
function addDeviceTab(title,url){
	var tab = parent.$('#index_tabs').iTabs("getTab",title);
	if(!tab){
		var params = {title:title,href:url}; 
		addParentTab(params);
	}else{
		parent.$('#index_tabs').iTabs("select",title);
	}
}
</script>
<style>
.layui-badge {
	height: initial;
	line-height: 30px;
	text-align: left;
	font-size: 14px;
}
</style>
</head>
<body>
	<div class="layui-container-fluid">
		<div class="panel_box row">
			<div class="panel col">
				<a href="javascript:;" onclick="addDeviceTab('维修人员在岗状态','console/jsp/deviceManagement/maintenanceStaff.jsp')">
					<div class="panel_icon" style="background-color: #54ADE8;">
						<i class="layui-icon" data-icon=""></i>
					</div>
					<div class="panel_word">
						<span id="employeeOnLine">0</span> <cite>在岗人员</cite>
					</div>
				</a>
			</div>
			<div class="panel col">
				<a href="javascript:;" data-url="">
					<div class="panel_icon" style="background-color: #FF5722;">
						<!-- <i class="fa fa-bell" data-icon=""></i> -->
						<i class="fa fa-warning" data-icon=""></i>
					</div>
					<div class="panel_word">
						<span>0</span> <cite>低于安全库存</cite>
					</div>
				</a>
			</div>
			<div class="panel col">
				<a href="javascript:void(0);" onClick="addDeviceTab('设备保养中心','console/jsp/deviceManagement/maintenanceCenter.jsp')">
					<div class="panel_icon" style="background-color: #009688;">
						<i class="fa fa-braille" data-icon=""></i>
					</div>
					<div class="panel_word">
						<span id="maintenaceCount">0</span> <cite>今日保养设备</cite>
					</div>
				</a>
			</div>
			<div class="panel col">
				<a href="javascript:void(0);" target="_self"
					onclick="addDeviceTab('设备维修记录','console/jsp/deviceManagement/MaintenanceStaffRecord.jsp')">
					<div class="panel_icon" style="background-color: #ED1C24;">
						<i class="fa fa-gear" data-icon=""></i>
					</div>
					<div class="panel_word">
						<span id="repairCount">0</span> <cite>今日报修</cite>
					</div>
				</a>
			</div>
			<div class="panel col max_panel">
				<a href="javascript:void(0);" onClick="addDeviceTab('维修转单中心','console/jsp/deviceManagement/MaintenanceTransferCenter.jsp')">
					<div class="panel_icon" style="background-color: #F7B824;">
						<i class="fa fa-exchange" data-icon=""></i>
					</div>
					<div class="panel_word allNews">
						<span id="changeOrderCount">0</span> <em>维修转单</em>

					</div>
				</a>
			</div>
			<div class="panel col max_panel">
				<a href="javascript:void(0);" onClick="switchAutoDispatch()">
					<div class="panel_icon" id="autoDispatchDiv" style="background-color:#2F4056;">
						<i class="fa fa-toggle-on" data-icon="" id="autoDispatchIcon"></i>
					</div>
					<div class="panel_word allNews">
						<span id="autoDispatch"></span> <em>自动派单</em>
					</div>
				</a>
			</div>
		</div>
		<div class="layui-row layui-col-space5" style="overflow: auto;">
			<div class="layui-col-md12">
				<blockquote class="layui-elem-quote title">
					设备报修单-待接单
					<!-- <i class="iconfont icon-new1"></i> -->
				</blockquote>
				<table class="layui-table" lay-skin="line" style="margin-top: 10px;">
					<tr>
						<th>报修单号</th>
						<th>报修时间</th>
						<th>报修人</th>
						<th>设备代码</th>
						<th>设备名称</th>
						<th>规格型号</th>
						<th>故障类型</th>
						<th>故障描述</th>
						<th>生产单元</th>
						<th>派单时间</th>
						<th>操作</th>
					</tr>
					<tbody class="hot_news" id="repair_receipting">
					</tbody>
				</table>
			</div>
		</div>
		<div class="layui-row layui-col-space10">
			<div class="layui-col-md16">
				<blockquote class="layui-elem-quote title">
					设备维修单-待确认
					<!-- <i class="iconfont icon-new1"></i> -->
				</blockquote>
				<table class="layui-table" lay-skin="line" style="margin-top: 10px;">
					<tr>
						<th>报修单号</th>
						<th>报修时间</th>
						<th>报修人</th>
						<th>设备代码</th>
						<th>设备名称</th>
						<th>规格型号</th>
						<th>故障类型</th>
						<th>故障描述</th>
						<th>生产单元</th>
						<th>派单时间</th>
						<th>维修人员</th>
						<th>完成时间</th>
						<th>占用工时(分钟)</th>
						<th>操作</th>
					</tr>
					<tbody class="hot_news" id="repair_confirming">
					</tbody>
				</table>
			</div>
		</div>
		<div class="layui-row layui-col-space10">
			<div class="layui-col-md12">
				<blockquote class="layui-elem-quote title">
					设备保养记录-待接单
					<!-- <i class="iconfont icon-new1"></i> -->
				</blockquote>
				<table class="layui-table" lay-skin="line" style="margin-top: 10px;">
					<tr>
						<th>计划日期</th>
						<th>设备代码</th>
						<th>设备名称</th>
						<th>规格型号</th>
						<th>保养类别</th>
						<th>生产单元</th>
						<th>责任人</th>
						<th>操作</th>
					</tr>
					<tbody class="hot_news" id="maintenance_receipting">
					</tbody>
				</table>
			</div>
		</div>
		<div class="layui-row layui-col-space10">
			<div class="layui-col-md12">
				<blockquote class="layui-elem-quote title">
					设备保养记录-待确认
					<!-- <i class="iconfont icon-new1"></i> -->
				</blockquote>
				<table class="layui-table" lay-skin="line" style="margin-top: 10px;">
					<tr>
						<th>计划日期</th>
						<th>保养时间</th>
						<th>设备代码</th>
						<th>设备名称</th>
						<th>规格型号</th>
						<th>保养类别</th>
						<th>生产单元</th>
						<th>责任人</th>
						<th>保养状态</th>
						<th>完成时间</th>
						<th>占用工时</th>
						<th>操作</th>
					</tr>
					<tbody class="hot_news" id="maintenance_confirming">
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>