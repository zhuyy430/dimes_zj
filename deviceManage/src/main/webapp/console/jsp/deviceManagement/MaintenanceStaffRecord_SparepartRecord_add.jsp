<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
	$(function() {
		$('#sparepartTab')
				.iDatagrid(
						{
							fitColumns:true,
							url : 'sparepart/queryAllSparepartsByDeviceRepairIdAndOtherCondition.do',
							columns : [ [ {
								field : 'id',
								title : 'id',
								checkbox : true
							}, {
								field : 'name',
								title : '备件名称',
								width : 100,
								formatter : function(value, row, index) {
									if (row) {
										return row.name;
									} else {
										return '';
									}
								}
							}, {
								field : 'code',
								title : '备件代码',
								width : 100,
								formatter : function(value, row, index) {
									if (row) {
										return row.code;
									} else {
										return '';
									}
								}
							}, {
								field : 'unitType',
								title : '规格型号',
								width : 100,
								formatter : function(value, row, index) {
									if (row) {
										return row.unitType;
									} else {
										return '';
									}
								}
							}, {
								field : 'measurementUnit',
								title : '计量单位',
								width : 100,
								formatter : function(value, row, index) {
									if (row) {
										return row.measurementUnit;
									} else {
										return '';
									}
								}
							}, {
								field : 'note',
								title : '备注',
								width : 100,
								formatter : function(value, row, index) {
									if (row) {
										return row.note;
									} else {
										return '';
									}
								}
							} ] ],
							queryParams : {
								deviceRepairId : $("#deviceDg").iDatagrid(
										"getSelected").id
							}
						});
	});
	function reloadSparepart(){
		$('#sparepartTab').iDatagrid("load",{
			sparepartCode:$("#sparepartCode").val(),
			sparepartName:$("#sparepartName").val(),
			sparepartUnitType:$("#sparepartUnitType").val(),
			deviceRepairId : $("#deviceDg").iDatagrid(
			"getSelected").id
		})
	}
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div data-options="region:'north',title:'',border:false,height:40">
		<div style="height: 30px; margin-top: 2px;">
			<div style="width: 100%;text-align:center;">
				备件编码:<input id="sparepartCode" name="sparepartCode"
					style="width: 150px; height: 25px; padding-left: 5px; border: 1px solid #D3D3D3;">
				备件名称:<input id="sparepartName" name="sparepartName"
					style="width: 150px; height: 25px; padding-left: 2px; border: 1px solid #D3D3D3;">
				规格型号:<input id="sparepartUnitType" name="sparepartUnitType"
					style="width: 150px; height: 25px; padding-left: 2px; border: 1px solid #D3D3D3;">
				<a href="javascript:void(0)" data-toggle="topjui-menubutton"
					data-options="iconCls:'fa fa-search'" onclick="reloadSparepart()">搜索</a>
			</div>
		</div>
	</div>
	<div style="padding-bottom:50px;"
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="sparepartTab"></table>
	</div>
</div>
