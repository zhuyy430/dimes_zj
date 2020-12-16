<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
		$(function(){
			$('#sparepartTab').iDatagrid({
				idField:'id',
				fitColumns:true,
                pagination:true,
                pageSize:15,
    			pageList:[10,15,20],
    			pagePosition:'bottom',
			    url:'deviceSparepartMapping/queryOtherSpareparts.do',
			    columns:[[
			        {field:'id',title:'id',checkbox:true},
			        {field:'code',title:'备件代码',width:120,align:'center'} ,
			        {field:'name',title:'备件代码',width:120,align:'center'},
			        {field:'unitType',title:'规格型号',width:120,align:'center'},
			        {field:'measurementUnit',title:'计量单位',width:120,align:'center'}
			    ]],
			    queryParams:{
			    	deviceId:$("#deviceDg").iDatagrid("getSelected").id
			    }
			});
		});
		function reloadSparepart(){
			$('#sparepartTab').iDatagrid("load",{
				deviceId:$("#deviceDg").iDatagrid("getSelected").id,
				sparepartCode:$("#sparepartCode").val(),
				sparepartName:$("#sparepartName").val(),
				sparepartUnitType:$("#sparepartUnitType").val()
			})
		}
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
<div data-options="region:'north',title:'',border:false,height:40">
		<div style="height: 30px;margin-top:2px;margin-left:10px;">
			<div style="width:100%;">
				备件编码:<input id="sparepartCode" 
					name="sparepartCode" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				备件名称:<input id="sparepartName" 
					name="sparepartName" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				规格型号:<input id="sparepartUnitType" 
					name="sparepartUnitType" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
				<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" 
				onclick="reloadSparepart()">搜索</a>
			</div>
		</div>
	</div>
	<div style="padding-bottom:40px;background:#eee;"
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<table id="sparepartTab"></table>
	</div>
</div>
