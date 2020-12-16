<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',iconCls:'icon-reload',fit:true,title:'',split:true,border:true,bodyCls:'border_top_none'">
		<table data-toggle="topjui-datagrid" data-options="id:'boxBarTable',
                       url:'boxBar/queryJobBookingDetailBoxBars.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                          singleSelect:true,
						selectOnCheck:false,
						checkOnSelect:false">
			<thead>
			<tr>
				<th data-options="field:'barCode',title:'条码',width:'180px',align:'center'"></th>
				<th data-options="field:'formNo',title:'单据编码',width:'180px',align:'center'"></th>
				<th data-options="field:'inventoryCode',title:'物料编码',width:'180px',align:'center'"></th>
				<th data-options="field:'inventoryName',title:'物料名称',width:'180px',align:'center'"></th>
				<th data-options="field:'boxNum',title:'箱号',width:'180px',align:'center'"></th>
				<th data-options="field:'amountOfBoxes',title:'总箱数',width:'180px',align:'center'"></th>
				<th data-options="field:'purchasingNo',title:'工单号',width:'180px',align:'center'"></th>
				<th data-options="field:'source',title:'来源',width:'180px',align:'center'"></th>
				<th data-options="field:'employeeName',title:'入库人员',width:'180px',align:'center'"></th>
				<th data-options="field:'warehousingDate',title:'入库时间',width:'180px',align:'center',formatter:function(value,row,index){
					if(value){
						return getDate(new Date(value));
					}else{
						return '';
					}
				}"></th>
				<th data-options="field:'warehouseNo',title:'入库单号',width:'180px',align:'center'"></th>
				<th data-options="field:'warehouseName',title:'入库仓库',width:'180px',align:'center'"></th>
				<th data-options="field:'amountOfPerBox',title:'入库数量',width:'180px',align:'center'"></th>
				<th data-options="field:'haveRequisition',title:'是否领料',width:'180px',align:'center',formatter:function(value,row,index){
                            if(row.haveRequisition){
                            	return '是';
                            }else{
                            	return '否';
                            }
                        }"></th>
				<th data-options="field:'requisitionDate',title:'领料日期',width:'180px',align:'center',
                        formatter:function(value,row,index){
                            if(value){
								return getDate(new Date(value));
							}else{
								return '';
							}
                        }"></th>
			</tr>
			</thead>
		</table>
	</div>
</div>
