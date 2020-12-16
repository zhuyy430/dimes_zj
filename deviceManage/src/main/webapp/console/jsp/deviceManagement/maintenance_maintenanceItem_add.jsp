<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
	$(function() {
		$('#projectTable').iDatagrid({
			idField:'id',
			pagination:true,
			pageSize:50,
			pageList:[10,20,30,40,50],
			pagePosition:'bottom',
			fitColumns:true,
			url : 'deviceProject/queryDeviceProjectByTypeAndMaintainRecordId.do',
			columns : [ [ {
				field : 'id',
				title : 'id',
				width : 60,
				checkbox : true
			}, {
				field : 'code',
				title : '代码',
				width : 100
			}, {
				field : 'name',
				title : '名称',
				width : 100
			}, {
				field : 'standard',
				title : '标准',
				width : 100
			}, {
				field : 'method',
				title : '方法',
				width : 100
			}, {
				field : 'frequency',
				title : '频次',
				width : 100
			}, {
				field : 'deviceTypeName',
				title : '设备类别',
				width : 100
			} ] ],
			queryParams : {
				recordId:$("#deviceDg").iDatagrid("getSelected").id,
		    	type:'MAINTAIN'
			}
		});
	});
	function reloadProjects(maintainTypeCode){
		if(!maintainTypeCode){
			maintainTypeCode=$("#mmaintainType").iCombobox('getValue');
		}
		$('#projectTable').iDatagrid('load',{
			recordId:$("#deviceDg").iDatagrid("getSelected").id,
	    	type:'MAINTAIN',
			condition:$("#mcondition").val(),
			maintainType:maintainTypeCode
		});
	}
</script>

<div data-toggle="topjui-layout" data-options="fit:true">
	<div data-options="region:'north',title:'',border:false,height:80">
		<div style="height: 30px;margin-top:10px;">
			<div style="float: left; line-height:30px;width:15%;font-size:14px;margin-left:5%;">过滤条件:</div>
			<div style="float: left;width:75%;">
				<input id="mcondition" placeholder="请输入项目名称或编码"
					name="condition" style="width:196px;height:30px;padding-left:2px;border:1px solid #D3D3D3;">
				<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" 
				onclick="reloadProjects()">搜索</a>
			</div>
		</div>
		<div style="height: 30px;margin-top:10px;">
			<div style="float: left; line-height:30px;width:15%;font-size:14px;margin-left:5%;">保养类别:</div>
			<div style="float: left;width:75%;">
				<input id="mmaintainType" data-toggle="topjui-combobox" style="margin-bottom:5px;"
					name="mmaintainType"
					data-options="width:200,valueField:'code',textField:'name',url:'maintenanceType/queryAllMaintenanceType.do',
					onSelect:function(record){
						reloadProjects(record.code);
					}">
			</div>
		</div>
	</div>
	<div style="padding-bottom:90px;background:#eee;" data-options="region:'center',title:'',fit:true,border:false">
		<table id="projectTable"></table>
	</div>
</div>