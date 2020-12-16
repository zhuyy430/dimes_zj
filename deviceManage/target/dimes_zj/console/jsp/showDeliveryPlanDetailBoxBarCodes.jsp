<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript">
	$(function () {
		var deliveryPlanDetailId = '<%=request.getParameter("deliveryPlanDetailId")%>';
		$("#deliveryPlanDetailId").val(deliveryPlanDetailId);
		//$("#boxBarTable").iDatagrid('reload',{deliveryPlanDetailId:deliveryPlanDetailId});
		$("#boxBarTable").iDatagrid({
            url:'boxBar/queryDeliveryPlanDetailBoxBars.do',
            singleSelect:true,
            fitColumns:true,
            pagination:true,
            singleSelect:true,
            selectOnCheck:false,
            checkOnSelect:false,
			queryParams:{deliveryPlanDetailId:deliveryPlanDetailId}
		});
    });
</script>
<input type="hidden" id="deliveryPlanDetailId">
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table data-toggle="topjui-datagrid" data-options="id:'boxBarTable'">
			<thead>
			<tr>
				<th data-options="field:'barCode',title:'条码',width:'180px',align:'center'"></th>
				<th data-options="field:'inventoryCode',title:'物料编码',width:'180px',align:'center'"></th>
				<th data-options="field:'inventoryName',title:'物料名称',width:'180px',align:'center'"></th>
				<th data-options="field:'specificationType',title:'规格型号',width:'180px',align:'center',formatter:function(value,row,index){
					if(row.deliveryPlanDetail){
						return row.deliveryPlanDetail.specificationType;
					}else{
						return '';
					}
				}"></th>
				<th data-options="field:'customerName',title:'客户',width:'180px',align:'center',formatter:function(value,row,index){
					if(row.deliveryPlanDetail){
						return row.deliveryPlanDetail.customerName;
					}else{
						return '';
					}
				}"></th>
				<th data-options="field:'amountOfPlan',title:'计划数量',width:'180px',align:'center',formatter:function(value,row,index){
					if(row.deliveryPlanDetail){
						return row.deliveryPlanDetail.amountOfPlan;
					}else{
						return '';
					}
				}"></th>
				<th data-options="field:'amountOfSended',title:'实发数量',width:'180px',align:'center',formatter:function(value,row,index){
					if(row.deliveryPlanDetail){
						return row.deliveryPlanDetail.amountOfSended;
					}else{
						return '';
					}
				}"></th>
				<th data-options="field:'boxNum',title:'箱号',width:'180px',align:'center'"></th>
				<th data-options="field:'amountOfBoxes',title:'总箱数',width:'180px',align:'center'"></th>
				<th data-options="field:'source',title:'来源',width:'180px',align:'center'"></th>
			</tr>
			</thead>
		</table>
	</div>
</div>
<%--
<div>
	<div id="vendorTable-toolbar" class="topjui-toolbar"
		 data-options="grid:{
           type:'datagrid',
           id:'vendorTable'
       }">
		供应商编码:<input id="vendorCode4Query"
					 name="vendorCode" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		供应商名称:<input id="vendorName4Query"
					 name="vendorName" style="width:100px;height:25px;padding-left:2px;border:1px solid #D3D3D3;">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'"
		   onclick="reloadVendors()">搜索</a>
	</div>
</div>--%>
