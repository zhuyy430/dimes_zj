<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#sparepartDeviceTable').iDatagrid({
				url:'deviceSparepartMapping/queryOtherDevices.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'设备代码',width:180,align:'center'} ,
			        {field:'name',title:'设备名称',width:180,align:'center'},
			        {field:'unitType',title:'规格型号',width:180,align:'center'} 
			    ]],
			    queryParams:{
			    	sparepartId:$("#departmentDg").iDatagrid("getSelected").id
			    }
			});
		}); 
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="sparepartDeviceTable"></table>
	</div>
</div>