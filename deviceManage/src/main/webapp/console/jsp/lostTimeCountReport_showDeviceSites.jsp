<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
		$(function(){
			$('#deviceTable').iDatagrid({
			    url:'deviceSite/queryDeviceSitesByProductionUnitId.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'站点代码',width:100},
			        {field:'name',title:'站点名称',width:100},
			        {field:'deviceCode',title:'设备代码',width:100,formatter:function(value,row,index){
			        	if(row.device){
			        		return row.device.code;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'deviceName',title:'设备名称',width:100,formatter:function(value,row,index){
			        	if(row.device){
			        		return row.device.name;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'unitType',title:'规格型号',width:100,formatter:function(value,row,index){
			        	if(row.device){
			        		return row.device.unitType;
			        	}else{
			        		return '';
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
		<table id="deviceTable"></table>
	</div>
</div>