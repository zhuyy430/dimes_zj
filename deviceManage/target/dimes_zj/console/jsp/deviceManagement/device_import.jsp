<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#deviceTab').iDatagrid({
			    url:'device/queryAllDevicesInDimes.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'设备代码',width:100,
			        	formatter:function(value,row,index){
			        		if(row){
			        			return row.code;
			        		}else{
			        			return '';
			        		}
			        	}
			        },
			        {field:'name',title:'设备名称',width:100,
			        	formatter:function(value,row,index){
			        		if(row){
			        			return row.name;
			        		}else{
			        			return '';
			        		}
			        	}
			        } ,
			        {field:'unitType',title:'规格型号',width:100,formatter:function(value,row,index){
			        	if(row){
			        		return row.unitType;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'status',title:'设备状态',width:100,formatter:function(value,row,index){
			        	if(row){
			        		return row.status;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'projectType',title:'设备类别',width:100,formatter:function(value,row,index){
			        	if(row){
			        		if(row.projectType){
				        		return row.projectType.name;
			        		}
			        		return '';
			        	}else{
			        		return '';
			        	}
			        }},
			    ]]
			});
		}); 
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="deviceTab"></table>
	</div>
</div>