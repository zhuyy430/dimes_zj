<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
label {
	text-align: center;
}
</style>
<script>
$(function(){
	$('#showEmployeesDialog').dialog({
	    title: '员工信息',
	    width: 1000,
	    height: 700,
	    closed: true,
	    cache: false,
	    href: 'console/jsp/deviceManagement/maintenancePlan_showEmployees.jsp',
	    modal: true,
	    buttons:[{
	        text:'保存',
	        handler:function(){
	        	var employee = $('#employeeTable').iDatagrid('getSelected');
	        	if(!employee){
	        		$.iMessager.alert("警告","请选择责任人!");
	        		return false;
	        	}
           		$('#employeeName').iTextbox('setValue',employee.name);
           		$('#employeeCode').val(employee.code);
	    		$('#showEmployeesDialog').dialog("close");
	        }
	    },{
	        text:'关闭',
	        handler:function(){
	        	$('#showEmployeesDialog').dialog("close");
	        }
	    }]
	});
	
	//设备站点搜索点击事件
	$("#employeeName").iTextbox({
	    width:200,
	    prompt:'',
	    buttonIcon:'fa fa-search',
	    onClickButton:function(){
	    	$('#showEmployeesDialog').dialog("open");
	    }
	});
});

</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addDeviceForm" method="post">
			<div title="批量责任人" data-options="iconCls:'fa fa-th'">
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
							<label class="topjui-form-label" style="text-align: center;">保养类别</label>
							<div class="topjui-input-block">
								<input  name="maintenanceType" data-toggle="topjui-combobox"
									data-options="valueField:'name',textField:'name',
										url:'maintenanceType/queryAllMaintenanceType.do'" id="maintenanceType" >
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">责任人</label>
							<div class="topjui-input-block">
								<input type="text" name="employeeName" data-toggle="topjui-textbox"
									id="employeeName" />
								<input type="hidden" name="employeeCode" id="employeeCode"/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
<div id="showEmployeesDialog"></div>
