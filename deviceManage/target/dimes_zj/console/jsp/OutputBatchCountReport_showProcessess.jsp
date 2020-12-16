<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
		$(function(){
			$('#processessTable').iDatagrid({
			    url:'processes/queryProcessess.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'工序代码',width:100},
			        {field:'name',title:'工序名称',width:100},
			        {field:'processType',title:'工序类别',width:100,formatter:function(value,row,index){
			        	if(value){
			        		return value.name;
			        	}else{
			        		return '';
			        	}
			        }},
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
		<table id="processessTable"></table>
	</div>
</div>