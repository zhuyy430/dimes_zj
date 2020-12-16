<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="procurement/js/warehousingApplicationForm.js"></script>

</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                       data-options="id:'warehousingApplicationFormDetailDg',
                       url:'warehousingApplicationFormDetail/queryWarehousingApplicationFormDetail.do?isOut=true',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                    <thead>
                    <tr>
                        <th data-options="field:'formNo',title:'申请单号',width:'180px',align:'center',
                        formatter:function(value,row,index){
                            if(row.warehousingApplicationForm){
                                return row.warehousingApplicationForm.formNo;
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'receivingDate',title:'来料日期',width:'180px',align:'center',
                        formatter:function(value,row,index){
                             if(row.warehousingApplicationForm){
                                return getDate(new Date(row.warehousingApplicationForm.receivingDate));
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'vendorName',title:'供应商名称',width:'180px',align:'center',
                        formatter:function(value,row,index){
                            if(row.warehousingApplicationForm){
                                return row.warehousingApplicationForm.vendorName;
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'purchasingNo',title:'采购单号',width:'180px',align:'center'"></th>
                        <th data-options="field:'inventoryCode',title:'物料代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'inventoryName',title:'物料名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'specificationType',title:'规格型号',width:'180px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'180px',align:'center'"></th>
                        <th data-options="field:'furnaceNumber',title:'材料编号',width:'180px',align:'center'"></th>
                        <th data-options="field:'amount',title:'数量',width:'180px',align:'center',formatter:function(value,row,index){
                            if(row.amount){
                                return formatFloat(row.amount,3);
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'amountOfBoxes',title:'箱数',width:'180px',align:'center'"></th>
                        <th data-options="field:'warehouseName',title:'入库仓库',width:'180px',align:'center'"></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="warehousingApplicationFormDetailDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'warehousingApplicationFormDetailDg'
       }">
<sec:authorize access="hasAuthority('OS_QUERY_WAREHOUSINGAPPLICATIONFORM')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#warehousingApplicationFormDetailDg-toolbar',
       iconCls: 'fa fa-search'" onclick="queryWarehousingApplicationFormDetails()">查询</a>
</sec:authorize>
<sec:authorize access="hasAuthority('OS_EXPORT_WAREHOUSINGAPPLICATIONFORM')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls: 'fa fa-sign-out'"
    >导出</a>
</sec:authorize>
<sec:authorize access="hasAuthority('OS_ADD_WAREHOUSINGAPPLICATIONFORM')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#warehousingApplicationFormDetailDg-toolbar',
       iconCls: 'fa fa-plus'" onclick="addWarehousingApplicationFormIsOut()">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('OS_DEL_WAREHOUSINGAPPLICATIONFORM')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls:'fa fa-trash'" onclick="deleteWarehousingApplicationForm()">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('OS_SEE_WAREHOUSINGAPPLICATIONFORM')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="extend: '#warehousingApplicationFormDetailDg-toolbar',
       iconCls: 'fa fa-eye'" onclick="queryWarehousingApplicationFormDetailsByFormNoIsOut()">查看</a>
</sec:authorize>
<sec:authorize access="hasAuthority('OS_EXAMINE_WAREHOUSINGAPPLICATIONFORM')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls:'fa fa-check-square-o'" id="audit" onclick="auditAtForm()">审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('OS_REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORM')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls:'fa fa-history'" id="unaudit" onclick="unauditAtForm()">反审核</a>
</sec:authorize>
    <form id="searchForm" style="margin-top: 10px;margin-left: 10px;">
        来料日期:<input id="from" type="text" data-toggle="topjui-datebox" style="width: 10%;"> 至
        <input id="to" type="text" data-toggle="topjui-datebox" style="width: 10%;">
        <input type="hidden" name="vendorCode" id="vendorCode">
        材料编码:<input id="furnaceNumber" data-toggle="topjui-textbox" style="width:10%">
        申请单号:<input id="requestNo" data-toggle="topjui-textbox" style="width:10%">
        采购单号:<input id="purchasingOrderNo" data-toggle="topjui-textbox" style="width:10%">
        入库仓库:<input id="warehouse" data-toggle="topjui-textbox" style="width:10%">
    </form>
</div>
<div id="showVendorsDialog"></div>
<div id="showWarehousesDialog"></div>
</body>
</html>
