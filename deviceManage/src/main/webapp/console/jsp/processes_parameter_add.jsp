<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#parameterTable').iDatagrid({
			    url:'parameter/queryOtherParameters.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'参数代码',width:100},
			        {field:'name',title:'参数名称',width:100},
			        {field:'rules',title:'参数取值规则',width:100},
			        {field:'kfc',title:'KPC',width:100,formatter:function(value,row,index){
			        	if(value){
			        		return 'Y';
			        	}else{
			        		return 'N';
			        	}
			        }},
			        {field:'disabled',title:'停用',width:100,formatter:function(value,row,index){
			        	if(value){
			        		return 'Y';
			        	}else{
			        		return 'N';
			        	}
			        }},
			        {field:'note',title:'备注',width:100}
			    ]],
			    queryParams:{
			    	processId:$('#departmentDg').iDatagrid("getSelected").id
			    }
			});
		}); 
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="parameterTable"></table>
	</div>
</div>