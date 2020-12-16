<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#employeProductionUnitTab').iDatagrid({
			    url:'productionUnit/queryAllProductionUnitsByMaintenanceStaffId.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'name',title:'生产单元名称',width:100,
			        	formatter:function(value,row,index){
			        		if(row){
			        			return row.name;
			        		}else{
			        			return '';
			        		}
			        	}
			        } ,
			        {field:'code',title:'生产单元代码',width:100,
			        	formatter:function(value,row,index){
			        		if(row){
			        			return row.code;
			        		}else{
			        			return '';
			        		}
			        	}
			        },
			        {field:'note',title:'备注',width:100,formatter:function(value,row,index){
			        	if(row){
			        		return row.note;
			        	}else{
			        		return '';
			        	}
			        }}
			    ]],
			    queryParams : {
					id : $("#departmentDg").iDatagrid(
							"getSelected").id
				}
			});
		}); 
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="employeProductionUnitTab"></table>
	</div>
</div>