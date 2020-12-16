<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
		$(function(){
			$('#deviceTable').iDatagrid({
				idField:'id',
				fitColumns:true,
			    url:'device/queryAllDevicesInDeviceMgr.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'设备代码',width:130},
			        {field:'name',title:'设备名称',width:130},
			        {field:'unitType',title:'规格型号',width:130},
			        {field:'manufacturer',title:'生产厂家',width:130}
			    ]]
			});
		});
		function reloadDevices(){
			$('#deviceTable').iDatagrid("load",{
				deviceCode:$("#deviceCode").val(),
				deviceName:$("#deviceNames").val(),
				manufacturer:$("#manufacturer").val()
			});
		}
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
<div data-options="region:'north',title:'',border:false,height:40">
		<div style="height: 30px;margin-top:2px;margin-left:10px;">
			<div style="width:100%;">
				设备编码:<input id="deviceCode" 
					name="deviceCode" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				设备名称:<input id="deviceNames" 
					name="deviceNames" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				生产厂家:<input id="manufacturer" 
					name="manufacturer" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" 
				onclick="reloadDevices()">搜索</a>
			</div>
		</div>
	</div>
	<div style="padding-bottom:40px;background:#eee;"
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="deviceTable"></table>
	</div>
</div>