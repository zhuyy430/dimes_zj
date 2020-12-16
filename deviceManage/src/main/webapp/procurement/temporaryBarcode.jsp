<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="procurement/js/temporaryBarcode.js"></script>
<script type="text/javascript">
    $(function () {
        var now = new Date();
        $("#from").iDatebox("setValue",getDate(new Date(now.getFullYear(),now.getMonth(),1)));
        $("#to").iDatebox("setValue",getDate(new Date()));
    });
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                        data-options="id:'temporaryBarcodeDetailDg',
                       url:'temporaryBarcodeDetail/queryTemporaryBarcodeDetail.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                    <thead>
                    <tr>
                        <th data-options="field:'formNo',title:'单据编号',width:'150px',align:'center',
                        formatter:function(value,row,index){
                            if(row.temporaryBarcode){
                                return row.temporaryBarcode.formNo;
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'billDate',title:'单据日期',width:'150px',align:'center',
                        formatter:function(value,row,index){
                             if(row.temporaryBarcode){
                                return getDate(new Date(row.temporaryBarcode.billDate));
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'inventoryCode',title:'物料代码',width:'150px',align:'center'"></th>
                        <th data-options="field:'inventoryName',title:'物料名称',width:'150px',align:'center'"></th>
                        <th data-options="field:'specificationType',title:'规格型号',width:'150px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'150px',align:'center'"></th>
                        <th data-options="field:'furnaceNumber',title:'材料编号',width:'150px',align:'center'"></th>
                        <th data-options="field:'amount',title:'数量',width:'150px',align:'center'"></th>
                        <th data-options="field:'amountOfBoxes',title:'箱数',width:'150px',align:'center'"></th>
                        <th data-options="field:'billType',title:'单据类型',width:'150px',align:'center',formatter:function(value,row,index){
                            if(value){
                                switch(value){
                                    case '0':return '新增';
                                    case '1':return '报工条码';
                                    case '2':return '材料条码';
                                }
                            }else{
                                return '';
                            }
                        }"></th>
                         <th data-options="field:'sourceBarcode',title:'来源条码',width:'150px',align:'center'"></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="temporaryBarcodeDetailDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'temporaryBarcodeDetailDg'
       }">
<sec:authorize access="hasAuthority('QUERY_TEMPORARYBARCODE')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="extend: '#temporaryBarcodeDetailDg-toolbar',
       iconCls: 'fa fa-search'" onclick="queryTemporaryBarcodeDetails()">查询</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXPORT_TEMPORARYBARCODE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls: 'fa fa-sign-out'"
    >导出</a>
</sec:authorize>
<sec:authorize access="hasAuthority('ADD_TEMPORARYBARCODE')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="extend: '#temporaryBarcodeDetailDg-toolbar',
       iconCls: 'fa fa-plus'" onclick="addTemporaryBarcode()">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_TEMPORARYBARCODE')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="iconCls:'fa fa-trash'" onclick="deleteTemporaryBarcode()">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SEE_TEMPORARYBARCODE')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="extend: '#temporaryBarcodeDetailDg-toolbar',
       iconCls: 'fa fa-eye'" onclick="queryTemporaryBarcodeDetailsByFormNo()">查看</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXAMINE_TEMPORARYBARCODE')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="iconCls:'fa fa-check-square-o'" id="audit" onclick="auditAtForm()">审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('REVERSEEXAMINE_TEMPORARYBARCODE')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="iconCls:'fa fa-history'" id="unaudit" onclick="unauditAtForm()">反审核</a>
</sec:authorize>
    <form id="searchForm" style="margin-top: 10px;margin-left: 10px;">
        单据日期:<input id="from" type="text" data-toggle="topjui-datebox" style="width: 12%;"> 至
        <input id="to" type="text" data-toggle="topjui-datebox" style="width: 12%;">
        物料代码:<input id="inventoryCode" data-toggle="topjui-textbox" style="width:12%">
        单据类型:<input id="billType" data-toggle="topjui-combobox" style="width:12%"
                    data-options="valueField:'value',textField:'text',data:[
                    {text:'新增',value:'0',selected:true},{text:'报工条码',value:'1'},{text:'材料条码',value:'2'}
                    ]">
    </form>
</div>
</body>
</html>
