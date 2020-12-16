<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#processesDeviceSiteTable').iDatagrid({
			    url:'workpiece/queryOtherProcessDeviceSites.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'processTypeName',title:'工序类别',width:100,
			        	formatter:function(value,row,index){
			        		if(row.processes){
			        			if(row.processes.processType){
			        				return row.processes.processType.name;
			        			}else{
			        				return '';
			        			}
			        		}else{
			        			return '';
			        		}
			        	}
			        } ,
			        {field:'processCode',title:'工序代码',width:100,
			        	formatter:function(value,row,index){
			        		if(row.processes){
			        			return row.processes.code;
			        		}else{
			        			return '';
			        		}
			        	}
			        },
			        {field:'processName',title:'工序名称',width:100,formatter:function(value,row,index){
			        	if(row.processes){
			        		return row.processes.name;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'deviceCode',title:'设备代码',width:100,formatter:function(value,row,index){
			        	if(row.deviceSite){
			        		return row.deviceSite.device.code;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'deviceName',title:'设备名称',width:100,formatter:function(value,row,index){
			        	if(row.deviceSite){
			        		return row.deviceSite.device.name;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'deviceSiteCode',title:'站点代码',width:100,formatter:function(value,row,index){
			        	if(row.deviceSite){
			        		return row.deviceSite.code;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'deviceSiteName',title:'站点名称',width:100,formatter:function(value,row,index){
			        	if(row.deviceSite){
			        		return row.deviceSite.name;
			        	}else{
			        		return '';
			        	}
			        }} 
			    ]],
			    queryParams:{
			    	workpieceId:$("#departmentDg").iDatagrid("getSelected").id
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