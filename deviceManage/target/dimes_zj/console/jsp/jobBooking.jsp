<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="console/jsp/js/jobBooking.js"></script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                        data-options="id:'jobBookingFormDetailDg',
                       url:'jobBookingFormDetail/queryJobBookingFormDetail.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                    <thead>
                    <tr>
                        <th data-options="field:'receivingDate',title:'报工日期',width:'180px',align:'center',
                        formatter:function(value,row,index){
                             if(row.jobBookingForm){
                                return getDate(new Date(row.jobBookingForm.jobBookingDate));
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'vendorName',title:'生产单元',width:'180px',align:'center',
                        formatter:function(value,row,index){
                            if(row.jobBookingForm){
                                return row.jobBookingForm.productionUnitName;
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'inventoryCode',title:'工件代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'inventoryName',title:'工件名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'180px',align:'center'"></th>
                        <th data-options="field:'amountOfJobBooking',title:'报工数量',width:'180px',align:'center'" data-options="formatter:function(value,row,index){
                            if(row.amountOfJobBooking){
                            return formatFloat(row.amountOfJobBooking,3);
                            }else{
                            return '';
                            }
                        }"></th>
                        <th data-options="field:'amountOfInWarehouse',title:'入库数量',width:'180px',align:'center'" data-options="formatter:function(value,row,index){
                            if(row.amountOfInWarehouse){
                            return formatFloat(row.amountOfInWarehouse,3);
                            }else{
                            return '';
                            }
                        }"></th>
                        <th data-options="field:'furnaceNumber',title:'材料编号',width:'180px',align:'center'"></th>


                        <th data-options="field:'specificationType',title:'规格型号',width:'180px',align:'center'"></th>
                        <th data-options="field:'formNo',title:'报工单号',width:'180px',align:'center',
                        formatter:function(value,row,index){
                            if(row.jobBookingForm){
                                return row.jobBookingForm.formNo;
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'no',title:'工单单号',width:'180px',align:'center'"></th>
                        <th data-options="field:'processCode',title:'工序代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'processName',title:'工序名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'haveRequisition',title:'是否领料',width:'180px',align:'center',formatter:function(value,row,index){
                            if(row.haveRequisition){
                            return '是';
                            }else{
                            return '否';
                            }
                        }"></th>
                        <th data-options="field:'requisitionDate',title:'领料日期',width:'180px',align:'center',
                        formatter:function(value,row,index){
                             if(row.requisitionDate){
                                return getDate(new Date(row.requisitionDate));
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'boxNum',title:'箱号',width:'100px',align:'center'"></th>
                        <%--<th data-options="field:'amountOfBoxes',title:'总箱数',width:'100px',align:'center'"></th>--%>
                        <th data-options="field:'jobBooker',title:'报工人',width:'100px',align:'center',
                        formatter:function(value,row,index){
                            if(row.jobBookingForm){
                                return row.jobBookingForm.jobBookerName;
                            }else{
                                return '';
                            }
                        }"></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="jobBookingFormDetailDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'jobBookingFormDetailDg'
       }">
<sec:authorize access="hasAuthority('QUERY_JOBOOKING')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="extend: '#jobBookingFormDetailDg-toolbar',
       iconCls: 'fa fa-search'" onclick="queryJobBookingFormDetails()">查询</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXPORT_JOBOOKING')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls: 'fa fa-sign-out'"
    >导出</a>
</sec:authorize>
<%--<sec:authorize access="hasAuthority('ADD_JOBOOKING')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="extend: '#jobBookingFormDetailDg-toolbar',
       iconCls: 'fa fa-plus'" onclick="addJobBookingForm()">新增</a>
</sec:authorize>--%>
<sec:authorize access="hasAuthority('DEL_JOBOOKING')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="iconCls:'fa fa-trash'" onclick="deleteJobBookingForm()">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SEE_JOBOOKING')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="extend: '#jobBookingFormDetailDg-toolbar',
       iconCls: 'fa fa-eye'" onclick="queryJobBookingFormDetailsByFormNo()">查看</a>
</sec:authorize>
        <%--<a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="iconCls:'fa fa-check-square-o'" id="audit" onclick="auditAtForm()">审核</a>
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="method:'doAjax',
       extend: '#jobBookingFormDetailDg-toolbar',
       iconCls:'fa fa-history',
       url:'classes/disabledClasses.do',
       grid: {uncheckedMsg:'请选择操作的班次',id:'jobBookingFormDetailDg',param:'id:id'}" id="unaudit" onclick="unauditAtForm()">反审核</a>--%>
    <form id="searchForm" style="margin-top: 10px;margin-left: 10px;">
        报工日期:<input id="from" type="text" data-toggle="topjui-datebox" style="width: 12%;"> 至
        <input id="to" type="text" data-toggle="topjui-datebox" style="width: 12%;">
        生产单元:<input type="text" id="productionUnitNameSearch" style="width: 200px;"
                    name="productionUnitName" data-toggle="topjui-combotree"
                    data-options="required:false,
                       valueField:'code',
                       textField:'name',
                       panelWidth:'250px',
                       url:'productionUnit/queryProductionUnitSiteTree.do',
                       onSelect: function(rec){
                       	$('#productionUnitCodeSearch').val(rec.code);
                       }
                       ">
        <input type="hidden" name="productionUnitCodeSearch" id="productionUnitCodeSearch">
        工件代码:<input id="inventoryCodeSearch" data-toggle="topjui-textbox" style="width:9%">
        工单单号:<input id="noSearch" data-toggle="topjui-textbox" style="width:9%">
        箱条码:<input id="barCodeSearch" data-toggle="topjui-textbox" style="width:12%">
        批号：<input id="batchNoSearch" data-toggle="topjui-textbox" style="width:9%">
    </form>
</div>
<div id="showVendorsDialog"></div>
<div id="showWarehousesDialog"></div>
</body>
</html>
