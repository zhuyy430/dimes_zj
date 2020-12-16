<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
		$(function(){
			$('#employeeTable').iDatagrid({
			    url:'maintenanceStaff/queryMaintenanceStaffAll.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'员工代码',width:130},
			        {field:'name',title:'员工名称',width:130}
			    ]]
			});
		});
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="employeeTable"></table>
	</div>
</div>