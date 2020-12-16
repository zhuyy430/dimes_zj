<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
label {
	text-align: center;
}

input[type='radio'] {
	margin-left: 5px;
	margin-top: 5px;
}

[for] {
	font-size: 14px;
	margin-left: 10px;
}

.cycleType {
	margin-top: 10px;
}
</style>
<script>
	$(function() {
		//设备站点搜索点击事件
		$("#deviceCodes").iTextbox({
			width : 200,
			prompt : '以逗号间隔设备代码',
			buttonIcon : 'fa fa-search',
			onClickButton : function() {
				$('#showDevicesDialog').dialog("open");
			}
		});
		$('#showDevicesDialog')
				.dialog(
						{
							title : '设备信息',
							width : 800,
							height : 800,
							closed : true,
							cache : false,
							href : 'console/jsp/deviceManagement/checkingPlan_showDevices.jsp',
							modal : true,
							buttons : [
									{
										text : '保存',
										handler : function() {
											var devices = $('#deviceTable')
													.iDatagrid('getSelections');
											var codesArray = new Array();
											for (var i = 0; i < devices.length; i++) {
												codesArray
														.push(devices[i].code);
											}
											var codesStr = JSON
													.stringify(codesArray);
											codesStr = codesStr
													.replace('[', '');
											codesStr = codesStr
													.replace(']', '');
											codesStr = codesStr.replace(/"/g,
													'');
											$('#deviceCodes').iTextbox(
													'setValue', codesStr);
											$('#showDevicesDialog').dialog(
													"close");
											if (codesStr.indexOf(",") == -1) {
												$
														.get(
																"device/queryDeviceByCode.do",
																{
																	name : 'code',
																	value : codesStr
																},
																function(result) {
																	$(
																			"#deviceName")
																			.iTextbox(
																					"setValue",
																					result.name);
																	$(
																			"#unitType")
																			.iTextbox(
																					"setValue",
																					result.unitType);
																});
											} else {
												$("#deviceName").iTextbox(
														"setValue", '');
												$("#unitType").iTextbox(
														"setValue", '');
											}
										}
									},
									{
										text : '关闭',
										handler : function() {
											$('#showDevicesDialog').dialog(
													"close");
										}
									} ]
						});
	});
	function reloadProjects(classesCode) {
		console.log(classesCode);
		if(!classesCode){
			classesCode=$("#classesType").iCombobox('getValue');
		}
	}
</script>
<div id="showDevicesDialog">
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
			<table id="deviceTable"></table>
		</div>
	</div>
</div>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addDeviceForm" method="post">
			<div title="新增点检计划" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">日期区间</label>
							<div class="topjui-input-block">
								<input type="text" name="from" data-toggle="topjui-datebox"
									data-options="required:true" id="from" style="width: 40%;">
								TO <input type="text" name="to" data-toggle="topjui-datebox"
									data-options="required:true" id="to" style="width: 40%;">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">点检周期</label>
							<div class="topjui-input-block"
								style="border-style: dotted; border-radius: 10px;">
								<div class="cycleType" >
								<div>
									<input type="radio" name="cycleType" value="forClass"
										id="forClass" /> <label for="forClass">每班</label>
								</div>
										
								<div style="height: 30px; margin-top: -25px; float: right;margin-right: 100px">
									<div
										style="float: left; line-height: 30px; width: 15%; font-size: 14px; margin-left: 5%;">班次:</div>
									<div style="float: left; width: 75%;">
										<input id="classesType" data-toggle="topjui-combobox"
											style="margin-bottom: 5px;" name="maintainType"
											data-options="width:200,valueField:'code',textField:'name',url:'classes/queryAllClasses.do',
					onSelect:function(record){
						reloadProjects(record.code);
					}">
									</div>
								</div>
								</div>
								<div class="cycleType">
									<input type="radio" name="cycleType" value="forDay" id="forDay"
										checked="checked" /> <label for="forDay">每天</label>
								</div>
								<%--<div class="cycleType">
									<input type="radio" name="cycleType" value="forWeek"
										id="forWeek" /> <label for="forWeek">每周</label>
									&nbsp;&nbsp;&nbsp;&nbsp; <input id="forWeekValue"
										data-toggle="topjui-combobox"
										style='width: 20%; height: 25px;'
										data-options="valueField:'text',textField:'text',
										data:[{text:'周一','selected':true},{text:'周二'},{text:'周三'},{text:'周四'},{text:'周五'},{text:'周六'},{text:'周日'}]">
								</div>
								<div class="cycleType">
									<input type="radio" name="cycleType" value="forMonth"
										id="forMonth" /> <label for="forMonth">每月</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input id="forMonthValue" data-toggle="topjui-combobox"
										style='width: 20%; height: 25px;'
										data-options="valueField:'text',textField:'text',
										data:[{text:'1','selected':true},{text:'2'},{text:'3'},{text:'4'},{text:'5'},{text:'6'},{text:'7'}
										,{text:'8'},{text:'9'},{text:'10'},{text:'11'},{text:'12'},{text:'13'}
										,{text:'14'},{text:'15'},{text:'16'},{text:'17'},{text:'18'},{text:'19'}
										,{text:'20'},{text:'21'},{text:'22'},{text:'23'},{text:'24'},{text:'25'}
										,{text:'26'},{text:'27'},{text:'28'}]"><label style="margin-left:5px;font-size:14px;">日</label>
								</div>
								<div class="cycleType" style="margin-bottom: 10px;">
									<input type="radio" name="cycleType" value="forDuration"
										id="forDuration" /> <label for="forMonth">每隔</label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<input type="text"  id="forDurationValue" data-toggle="topjui-numberbox" name="cycleType" 
										style='width: 20%; height: 25px;' /> <label style="margin-left:5px;font-size:14px;">天循环,
										首次日期
										</label>	<input type="text" name="startDate" data-toggle="topjui-datebox"
									data-options="required:false" id="startDate" style="width: 40%;">
								</div>--%>
							</div>
						</div>
					</div>
					<div class="topjui-row">

						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">设备代码</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceCodes"
									data-toggle="topjui-textbox" data-options="required:true"
									id="deviceCodes">
							</div>
						</div>
					</div>
					<div class="topjui-row">

						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">设备名称</label>
							<div class="topjui-input-block">
								<input type="text" name="deviceName"
									data-toggle="topjui-textbox" readonly="readonly"
									id="deviceName">
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="unitType" data-toggle="topjui-textbox"
									readonly="readonly" id="unitType">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
