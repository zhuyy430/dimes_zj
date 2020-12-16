<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
		$(function(){
			$('#workpieceTable').iDatagrid({
			    url:'workpiece/queryWorkpieces.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'工件代码',width:80},
			        {field:'name',title:'工件名称',width:80},
			        {field:'version',title:'版本号',width:80},
			        {field:'unitType',title:'规格型号',width:80},
			        {field:'measurementUnit',title:'计量单位',width:80},
			       /*  {field:'graphNumber',title:'图号',width:100},
			        {field:'customerGraphNumber',title:'客户图号',width:100}, */
			        {field:'workpieceType',title:'工件类别',width:80,formatter:function(value,row,index){
			        	if(value){
			        		return value.name;
			        	}else{
			        		return '';
			        	}
			        }},
			        {field:'disabled',title:'是否停用',width:80,formatter:function(value,row,index){
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
		<table id="workpieceTable"></table>
	</div>
</div>