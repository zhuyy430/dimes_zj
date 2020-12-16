<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#deviceTable').iDatagrid({
			    url:'classes/queryOtherDevices.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'代码',width:100},
			        {field:'name',title:'名称',width:100},
			        {field:'unitType',title:'规格型号',width:100},
			        {field:'status',title:'设备状态',width:100}
			    ]],
			    queryParams:{
			    	classesId:$('#departmentDg').iDatagrid("getSelected").id
			    }
			});
		}); 
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="deviceTable"></table>
	</div>
</div>