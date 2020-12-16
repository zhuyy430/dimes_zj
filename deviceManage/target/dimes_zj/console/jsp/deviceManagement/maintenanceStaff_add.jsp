<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#employeeTab').iDatagrid({
			    url:'employee/queryEmployeeNotInMaintenanceStaff.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'name',title:'员工姓名',width:100,
			        	formatter:function(value,row,index){
			        		if(row){
			        			return row.name;
			        		}else{
			        			return '';
			        		}
			        	}
			        } ,
			        {field:'code',title:'员工代码',width:100,
			        	formatter:function(value,row,index){
			        		if(row){
			        			return row.code;
			        		}else{
			        			return '';
			        		}
			        	}
			        },
			        {field:'departmentName',title:'部门名称',width:100,formatter:function(value,row,index){
			        	if(row){
			        		return row.departmentName;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'positionName',title:'岗位名称',width:100,formatter:function(value,row,index){
			        	if(row){
			        		return row.positionName;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'tel',title:'联系方式',width:100}
			    ]]
			});
		}); 
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="employeeTab"></table>
	</div>
</div>