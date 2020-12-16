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
	    height: 600,
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
			<div title="编辑保养记录" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">日期</label>
							<div class="topjui-input-block">
								<input type="text" name="maintenanceDate"
									data-toggle="topjui-datebox" 
									id="maintenanceDate"> <input
									type="hidden" name="id" id="id" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">班次</label>
							<div class="topjui-input-block">
								<input type="text" name="className" data-toggle="topjui-textbox"
									 id="className" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">保养类别</label>
							<div class="topjui-input-block">
								<input type="text" name="maintenanceType" data-toggle="topjui-textbox"
									id="maintenanceType" readonly="readonly" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">计划状态</label>
							<div class="topjui-input-block">
								<input type="text" name="status" data-toggle="topjui-textbox"
									id="status" readonly="readonly" />
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
