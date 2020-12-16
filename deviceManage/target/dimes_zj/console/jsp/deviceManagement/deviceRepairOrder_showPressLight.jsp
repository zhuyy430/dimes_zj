<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
		$(function(){
			 //故障类型列表
			$('#pressLightType').iCombogrid(
					{
						idField : 'id',
						textField : 'name',
						delay : 500,
						mode : 'remote',
						url : 'projectType/queryProjectTypesByType.do?type=breakdownReasonType',
						columns : [ [ {
							field : 'id',
							title : 'id',
							width : 60,
							hidden : true
						}, {
							field : 'code',
							title : '代码',
							width : 100
						}, {
							field : 'name',
							title : '故障名称',
							width : 100
						}] ],
						onChange : function(newValue, oldValue) {
							$('#pressLightTable').iDatagrid("reload",{projectTypeId:$("#pressLightType").val(),type:"BREAKDOWNREASON"});
						}
					}); 
			$('#pressLightTable').iDatagrid({
				idField : 'id',
			    url:'deviceProject/queryDevicesProjectByProjectTypeId.do',
			    onDblClickRow :function(rowIndex,rowData){
			    	confirmPressLight(rowData);
	  				},
			    columns:[[
			        {field:'id',title:'id',hidden:true},
			        {field:'code',title:'故障原因代码',width:200},
			        {field:'name',title:'故障原因名称',width:200}
			    ]],
			    queryParams:{
			    	type:"BREAKDOWNREASON"
			    }
			});
		});
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
<div data-options="region:'north',title:'',border:false,height:40">
		<div style="height: 30px; margin-top: 10px;">
			<div
				style="float: left; line-height: 30px; width: 15%; font-size: 14px; margin-left: 5%;">故障类别:</div>
			<div style="float: left; width: 75%;">
				<input id="pressLightType" name="pressLightType"
					style="width: 196px; height: 25px; padding-left: 2px; border: 1px solid #D3D3D3;">
			</div>
		</div>
	</div>
	<div style="padding-bottom: 40px;"
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="pressLightTable"></table>
	</div>
</div>