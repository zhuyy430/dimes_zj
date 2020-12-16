<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
		$(function(){
			$('#employeeTable').iDatagrid({
			    url:'employee/queryAllEmployeesByProductionUnitId.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'员工代码',width:100},
			        {field:'name',title:'员工姓名',width:100},
			        {field:'note',title:'备注',width:100},
			        {field:'disabled',title:'是否停用',width:100,formatter:function(value,row,index){
			        	if(value){
			        		return 'Y';
			        	}else{
			        		return 'N';
			        	}
			        }}
			    ]],
			    queryParams:{
			    	productionUnitId:$("#productionUnitId").val()
			    }
			});
		});
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="employeeTable"></table>
	</div>
</div>