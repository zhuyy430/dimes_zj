<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#processesDeviceSiteTable').iDatagrid({
			    url:'inventory/queryOtherProcessParameters.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        /*{field:'processTypeName',title:'工序类别',width:100,
			        	formatter:function(value,row,index){
			        		if(row.processes.processType){
			        			return row.processes.processType.name;
			        		}else{
			        			return '';
			        		}
			        	}
			        } ,*/
			        {field:'processCode',title:'工序代码',width:120,
			        	formatter:function(value,row,index){
			        		if(row.processes){
			        			return row.processes.code;
			        		}else{
			        			return '';
			        		}
			        	}
			        },
			        {field:'processName',title:'工序名称',width:120,formatter:function(value,row,index){
			        	if(row.processes){
			        		return row.processes.name;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'parameterTypeName',title:'参数类别',width:120,formatter:function(value,row,index){
			        	if(row.parameters.parameterType){
			        		return row.parameters.parameterType.name;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'parameterCode',title:'参数代码',width:120,formatter:function(value,row,index){
			        	if(row.parameters){
			        		return row.parameters.code;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'parameterName',title:'参数名称',width:120,formatter:function(value,row,index){
			        	if(row.parameters){
			        		return row.parameters.name;
			        	}else{
			        		return '';
			        	}
			        }}
			    ]],
			    queryParams:{
			    	inventoryCode:$("#departmentDg").iDatagrid("getSelected").code
			    }
			});
		}); 
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="processesDeviceSiteTable"></table>
	</div>
</div>