<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#employees').iDatagrid({
			    url:'employee/queryEmployees4UserImport.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'员工代码',width:100},
			        {field:'name',title:'员工名称',width:100},
			        {field:'productionUnitName',title:'所在生产单元',width:100,formatter:function(value,row,index){
			        	if(row.productionUnit!=null){
			        		return row.productionUnit.name;
			        	}else{
			        		return '';
			        	}
			        }}
			    ]]
			});
		});
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="employees"></table>
	</div>
</div>