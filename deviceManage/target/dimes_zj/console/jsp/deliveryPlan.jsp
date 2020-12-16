<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="console/jsp/js/deliveryPlan.js"></script>
<script type="text/javascript">
    $(function(){
        $("#customerCodeSearch").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showCustomersDialog').dialog("open");
            }
        });

        //弹出选择供应商
        $('#showCustomersDialog').dialog({
            title: '客户信息',
            width: 800,
            height: 600,
            closed: true,
            cache: false,
            href: 'console/jsp/showCustomers.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function(){
                    var customer = $('#customerTable').iDatagrid('getSelected');
                    confirmCustomers(customer);
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showCustomersDialog').dialog("close");
                }
            }]
        });
    });
    function confirmCustomers(customer){
    	$("#customerCode").val(customer.ccusCode);
        $("#customerCodeSearch").iTextbox('setValue',customer.ccusName);
        $('#showCustomersDialog').dialog("close");
    }
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                        data-options="id:'deliveryPlanDetailDg',
                       url:'deliveryPlanDetail/queryDeliveryPlanDetail.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                    <thead>
                    <tr>
                        <th data-options="field:'formNo',title:'计划单号',width:'180px',align:'center',
                        formatter:function(value,row,index){
                            if(row.deliveryPlan){
                                return row.deliveryPlan.formNo;
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'receivingDate',title:'计划日期',width:'180px',align:'center',
                        formatter:function(value,row,index){
                             if(row.deliveryPlan){
                                return getDate(new Date(row.deliveryPlan.deliverDate));
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'customerName',title:'客户名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'formNo',title:'销售订单',width:'180px',align:'center'"></th>
                        <th data-options="field:'inventoryCode',title:'物料代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'inventoryName',title:'物料名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'specificationType',title:'规格型号',width:'180px',align:'center'"></th>
                        <th data-options="field:'deliverDateOfPlan',title:'计划发货日期',width:'180px',align:'center',
                        formatter:function(value,row,index){
                             if(value){
                                return getDate(new Date(value));
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'amountOfPlan',title:'计划数量',width:'180px',align:'center'"></th>
                        <th data-options="field:'amountOfSended',title:'实发数量',width:'100px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'100px',align:'center'"></th>
                        <th data-options="field:'status',title:'状态',width:'100px',align:'center'"></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="deliveryPlanDetailDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'deliveryPlanDetailDg'
       }">
<sec:authorize access="hasAuthority('QUERY_DELIVERYPLAN')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="extend: '#deliveryPlanDetailDg-toolbar',
       iconCls: 'fa fa-search'" onclick="queryDeliveryPlanDetails()">查询</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXPORT_DELIVERYPLAN')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls: 'fa fa-sign-out'"
    >导出</a>
</sec:authorize>
<sec:authorize access="hasAuthority('ADD_DELIVERYPLAN')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="extend: '#deliveryPlanDetailDg-toolbar',
       iconCls: 'fa fa-plus'" onclick="addDeliveryPlan()">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_DELIVERYPLAN')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="iconCls:'fa fa-trash'" onclick="deleteDeliveryPlan()">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SEE_DELIVERYPLAN')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="extend: '#deliveryPlanDetailDg-toolbar',
       iconCls: 'fa fa-eye'" onclick="queryDeliveryPlanDetailsByFormNo()">查看</a>
</sec:authorize>
       <%-- <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="iconCls:'fa fa-check-square-o'" id="audit" onclick="auditAtForm()">审核</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls:'fa fa-history'" id="unaudit" onclick="unauditAtForm()">反审核</a>--%>
    <form id="searchForm" method="post">
        <div title="" data-options="iconCls:'fa fa-th'">
            <div class="topjui-fluid">
                <div style="height: 10px"></div>
                <div class="topjui-row">
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">计划发货日期</label>
                        <div class="topjui-input-block">
                            <input id="from" type="text" data-toggle="topjui-datebox" style="width:47%;"> 至
                            <input id="to" type="text" data-toggle="topjui-datebox" style="width:47%;">
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">客户</label>
                        <div class="topjui-input-block">
                            <input id="customerCodeSearch" name="customerCodeSearch" data-toggle="topjui-textbox">
                            <input type="hidden" id="customerCode">
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">销售订单</label>
                        <div class="topjui-input-block">
                            <input type="text" id="formNoSearch"
                                   name="formNoSearch" data-toggle="topjui-textbox">
                        </div>
                    </div>
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">物料</label>
                        <div class="topjui-input-block">
                            <input id="inventoryCodeSearch" data-toggle="topjui-textbox">
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label"> 批号</label>
                        <div class="topjui-input-block">
                            <input id="batchNumberSearch" data-toggle="topjui-textbox">
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">状态</label>
                        <div class="topjui-input-block">
                            <input id="statusSearch" data-toggle="topjui-combobox" data-options="
                            valueField: 'text',
                            textField: 'text',
                            data:[{text:'计划'},{text:'部分发货'},{text:'完成'},{text:'终止'}]">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div id="showCustomersDialog"></div>
</body>
</html>
