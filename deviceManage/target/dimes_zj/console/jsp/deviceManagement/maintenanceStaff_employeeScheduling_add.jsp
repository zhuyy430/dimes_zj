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
		$("#employeeCodes").iTextbox({
		    width:200,
		    prompt:'以逗号间隔设备代码',
		    buttonIcon:'fa fa-search',
		    onClickButton:function(){
		    	$('#showEmployeesDialog').dialog("open");
		    }
		});
		$('#showEmployeesDialog').dialog({
		    title: '设备信息',
		    width: 700,
		    height: 600,
		    closed: true,
		    cache: false,
		    href: 'console/jsp/deviceManagement/maintenanceStaff_showEmployees.jsp',
		    modal: true,
		    buttons:[{
		        text:'保存',
		        handler:function(){
		        	var employees = $('#employeeTable').iDatagrid('getSelections');
	           		var codesArray = new Array();
	           		var namesArray = new Array();
	           			for(var i = 0;i < employees.length;i++){
	           				codesArray.push(employees[i].code);
	           				namesArray.push(employees[i].name);
	           			}
	           			var codesStr = JSON.stringify(codesArray);
	           			var namesStr = JSON.stringify(namesArray);
	           			codesStr = codesStr.replace('[','');
	           			codesStr = codesStr.replace(']','');
	           			codesStr = codesStr.replace(/"/g,'');
	           			namesStr = namesStr.replace('[','');
	           			namesStr = namesStr.replace(']','');
	           			namesStr = namesStr.replace(/"/g,'');
	           		$('#employeeCodes').iTextbox('setValue',codesStr);
	           		$('#employeeNames').iTextbox('setValue',namesStr);
		    		$('#showEmployeesDialog').dialog("close");
		        }
		    },{
		        text:'关闭',
		        handler:function(){
		        	$('#showEmployeesDialog').dialog("close");
		        }
		    }]
		});
	});
</script>
<div id="showEmployeesDialog">
		<div data-toggle="topjui-layout" data-options="fit:true">
			<div
				data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
				<table id="employeeTable"></table>
			</div>
		</div>
	</div>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addEmployeeForm" method="post">
			<div title="新增人员排班" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">日期区间</label>
							<div class="topjui-input-block">
								<input type="text" name="from" data-toggle="topjui-datebox"
									data-options="required:true" id="from" style="width: 46%;">
								TO <input type="text" name="to" data-toggle="topjui-datebox"
									data-options="required:true" id="to" style="width: 46%;">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">班次</label>
							<div class="topjui-input-block">
								<input type="text" name="classes"
									data-toggle="topjui-combobox" data-options="required:true,
									valueField:'code',textField:'name',url:'classes/queryAllClasses.do'"
									id="classes">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">员工代码</label>
							<div class="topjui-input-block">
								<input type="text" name="employeeCodes"
									data-toggle="topjui-textbox" data-options="required:true"
									id="employeeCodes">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">员工名称</label>
							<div class="topjui-input-block">
								<input type="text" name="employeeNames"
									data-toggle="topjui-textbox" readonly="readonly"
									id="employeeNames">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
