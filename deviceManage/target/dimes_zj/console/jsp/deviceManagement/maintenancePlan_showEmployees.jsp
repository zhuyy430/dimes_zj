<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
		$(function(){
			$('#employeeTable').iDatagrid({
				idField:'id',
			    url:'employee/queryEmployees.do',
			    singleSelect:true,
			    fitColumns:true,
			    columns:[[
			        {field:'id',title:'id',hidden:true},
			        {field:'code',title:'员工代码',width:130},
			        {field:'name',title:'员工名称',width:130},
			        {field:'positionName',title:'职务',width:130,formatter:function(value,row,index){
			        	if(row.position){
			        		return row.position.name;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'departmentCode',title:'部门编码',width:130,formatter:function(value,row,index){
			        	if(row.position){
			        		if(row.position.department){
			        			return row.position.department.code;
			        		}else{
			        			return '';
			        		}
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'departmentName',title:'部门',width:130,formatter:function(value,row,index){
			        	if(row.position){
			        		if(row.position.department){
			        			return row.position.department.name;
			        		}else{
			        			return '';
			        		}
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'productionUnitName',title:'生产单元',width:130,formatter:function(value,row,index){
			        	if(row.productionUnit){
			        		return row.productionUnit.name;
			        	}else{
			        		return '';
			        	}
			        }}
			    ]]
			});
		});
		
		function reloadEmployees(){
			$('#employeeTable').iDatagrid("load",{
				employeeCode:$("#employeeCodes").val(),
				employeeName:$("#employeeNames").val(),
				department:$("#department").val()
			});
		}
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
<div data-options="region:'north',title:'',border:false,height:40">
		<div style="height: 30px;margin-top:2px;margin-left:10px;">
			<div style="width:100%;">
				员工代码:<input id="employeeCodes" 
					name="employeeCodes" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				员工名称:<input id="employeeNames" 
					name="employeeNames" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				部门:<input id="department" placeholder="部门编码或名称"
					name="department" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" 
				onclick="reloadEmployees()">搜索</a>
			</div>
		</div>
	</div>
	<div style="padding-bottom:40px;background:#eee;"
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="employeeTable"></table>
	</div>
</div>