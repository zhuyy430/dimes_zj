<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="console/jsp/js/deliveryPlan.js"></script>
<script type="text/javascript">
    $(function () {
        $("#deliverDate").iDatebox("setValue",getDate(new Date()));
        var  requestNo = '<%=request.getParameter("formNo")%>';
        if(requestNo=='null'){
            requestNo = '<%=session.getAttribute("delivery_plan_formNo")%>';
            if(requestNo=='null'){
                requestNo='';
            }
        }
        //添加发货单
        if(!requestNo){
            init();
            generateRequestNo();
            requestNo='';
            //显示可编辑表格
            showDeliveryPlans(requestNo);
        }else{//查看发货单
            $("#formNo").iTextbox("readonly");
            queryDeliveryPlanByFormNo(requestNo);
        }
    });
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table id='deliveryPlanDetailDg'>
                    <thead>
                    <tr>
                        <th field='formNo' width='100px',align='center'>销售订单</th>
                        <th field='customerName' width='100px' align='center'>客户名称</th>
                        <th field='inventoryCode' width='100px' align='center'>物料代码</th>
                        <th field='inventoryName' width='100px' align='center'>物料名称</th>
                        <th field='specificationType' width='100px' align='center'>规格型号</th>
                        <th field='weight' width='100px' align='center' editor="{type:'numberbox',options:{precision:2}}">单重/kg</th>
                        <th field='amountOfPlan' width='100px' align='center' editor="{type:'numberbox',options:{precision:2}}">计划数量</th>
                        <th field='deliverDateOfPlan' width='100px' align='center' editor="{type:'datebox',options:{editable:false}}">计划发货日期</th>
                        <th field='batchNumber' width='100px' align='center' editor="text">批号</th>
                        <th field='amountOfSended' width='100px' align='center'>实发数量</th>
                        <th field='warehouseCode' width='100px' align='center'>仓库代码</th>
                        <th field='warehouseName' width='100px' align='center'>出库仓库</th>
                        <th field='batchNumberOfSended' width='100px' align='center' editor="text">材料编号</th>
                        <th field='inspectionReport' width='100px' align='center' editor="text">客户批号</th>
                        <th field='note' width='100px' align='center' editor="text">备注</th>
                        <th field='status' width='100px' align='center'>状态</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div data-options="region:'south',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:10%;">
                <div class="topjui-fluid" style="margin-top: 20px;">
                    <div class="topjui-row">
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label"></label>
                            <div class="topjui-input-block">
                            </div>
                        </div>
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label">审核人</label>
                            <div class="topjui-input-block">
                                <input type="text" name="auditor" readonly data-toggle="topjui-textbox" id="auditor">
                            </div>
                        </div>
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label">审核时间</label>
                            <div class="topjui-input-block">
                                <input type="text" name="auditDate" readonly data-toggle="topjui-textbox" id="auditDate">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="deliveryPlanDetailDg-toolbar" class="topjui-toolbar">
<sec:authorize access="hasAuthority('ADDLINE_DELIVERYPLANDETAIL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton" data-options="iconCls: 'fa fa-plus'"
       onclick="addLines()">添加行</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DELLINE_DELIVERYPLANDETAIL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="iconCls: 'fa fa-trash'"
       onclick="deleteLine()" id="deleteLineBtn">删除行</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SPLITLINE_DELIVERYPLANDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-code-fork'"
       data-toggle="topjui-menubutton" onclick="splitLine()">拆分行</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SAVE_DELIVERYPLANDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-save'"
       data-toggle="topjui-menubutton" onclick="saveDeliveryPlan()" id="saveBtn">保存</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXAMINE_DELIVERYPLANDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-check-square-o'"
       data-toggle="topjui-menubutton" onclick="auditAtFormDetail()" id="auditBtn">审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('REVERSEEXAMINE_DELIVERYPLANDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-history'"
       data-toggle="topjui-menubutton" id="unauditBtn" onclick="unauditAtFormDetail()">反审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('BOXBAR_DELIVERYPLANDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-barcode'"
       data-toggle="topjui-menubutton" id="boxBar" onclick="showBoxBar()">箱号条码</a>
</sec:authorize>
<sec:authorize access="hasAuthority('OUTWAREHOUSE_DELIVERYPLANDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-sign-in'"
       data-toggle="topjui-menubutton" id="outWarehouseBtn">出库</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PRINT_DELIVERYPLANDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-barcode'"
       data-toggle="topjui-menubutton"  onclick="showPackagrCode()">打印包装条码</a>
</sec:authorize>
    <form id="ff" method="post">
        <div title="发货单" data-options="iconCls:'fa fa-th'">
            <div class="topjui-fluid">
                <div style="height: 30px;text-align: center;width: 100%;font-size: 24px;font-weight: bold;margin-bottom: 30px;margin-top: 30px;">
                    发货单
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">发货单号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="formNo" data-toggle="topjui-textbox"
                                   data-options="required:true" id="formNo">
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">计划日期</label>
                        <div class="topjui-input-block">
                            <input type="text" name="deliverDate" data-toggle="topjui-datebox"
                                   id="deliverDate">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div id="showSalesSlipsDialog"></div>
</body>
</html>
