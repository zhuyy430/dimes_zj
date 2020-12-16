<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
var numberList=[];

$(function(){
	$('#craftsRouteTable').iDatagrid({
		idField:'id',
		singleSelect:true,
	    url:'craftsRoute/queryCraftsRoute.do',
	    onDblClickRow :function(rowIndex,rowData){
	    	confirmCraftsRoute();
				},
	    columns:[[
	        {field:'id',title:'id',checkbox:false,hidden:true},
	        {field:'code',title:'工艺路线代码',width:100},
	        {field:'name',title:'工艺路线名称',width:100},
	        {field:'version',title:'版本',width:100},
	        {field:'note',title:'备注',width:100},
	    ]],
	    queryParams:{
	    	InventoryCode:$('#departmentDg').iDatagrid("getSelected").cInvCode
	    }
	});
}); 
		
		
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="craftsRouteTable"></table>
	</div>
</div>