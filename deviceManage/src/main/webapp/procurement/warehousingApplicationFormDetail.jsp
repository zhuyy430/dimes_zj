<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../common/jsp/head.jsp"%>
<link href="procurement/css/warehousingApplicationForm.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="procurement/js/warehousingApplicationForm.js"></script>
<script type="text/javascript">

    function ajaxLoading(){
        $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
        $("<div class=\"datagrid-mask-msg\"></div>").html("加载中，请稍候。。。").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
    }
    function ajaxLoadEnd(){
        $(".datagrid-mask").remove();
        $(".datagrid-mask-msg").remove();
    }

    var _vendorCode;
    var  requestNo='';
    $(function () {
        requestNo = '<%=request.getParameter("requestNo")%>';
        if(requestNo=='null'){
            requestNo = '<%=session.getAttribute("formNo")%>';
            if(requestNo=='null'){
                requestNo='';
            }
        }
        //添加入库申请单
        if(!requestNo){
            init();
            generateRequestNo();
            requestNo='';
            //显示可编辑表格
            showWarehousingApplicationForms(requestNo);
        }else{//查看入库申请单
            queryWarehousingApplicationFormByFormNo(requestNo);
            $("#requestNo").iTextbox("readonly");
        }
        $("#vendor").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showVendors4DetailDialog').dialog("open");
            }
        });
        //弹出选择供应商
        $('#showVendors4DetailDialog').dialog({
            title: '供应商信息',
            width: 800,
            height: 600,
            closed: true,
            cache: false,
            href: 'procurement/showVendors.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function() {
                    var vendor = $('#vendorTable').iDatagrid('getSelected');
                    confirm(vendor);
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showVendors4DetailDialog').dialog("close");
                }
            }]
        });
    });
    //供应商确定
    function confirm(vendor){
        if(vendor){
            $("#vendorCode").val(vendor.cVenCode);
            $("#vendor").iTextbox('setValue', vendor.cVenName);
            var _vendorCode = $("#_vendorCode").val();
            if (_vendorCode && _vendorCode != vendor.cVenCode ) {
                $("#_vendorCode").val(vendor.cVenCode);
                $.get("warehousingApplicationFormDetail/clearWarehousingApplicationDetailList.do", function (result) {
                    $('#warehousingApplicationFormDetailDg').iEdatagrid("reload");
                });
            }
        }
        $('#showVendors4DetailDialog').dialog("close");
    }
    function openWindow(){
        var checkedArray = $("#warehousingApplicationFormDetailDg").iDatagrid("getRows");
        if(checkedArray.length>0){
            var newWin = window.open("procurement/barCode_print.jsp?formNo="+requestNo);
        }else{
            alert("没有打印内容 !");
            return false;
        }
    }
    //入库
    function  inWarehouse() {
        var formNo = $("#requestNo").iTextbox("getValue");
        ajaxLoading();
        $.get("warehousingApplicationFormDetail/generateArrivalSlip.do",{formNo:formNo},function (result) {
            ajaxLoadEnd();
            alert(result.message);
            queryWarehousingApplicationFormByFormNo($("#requestNo").iTextbox("getValue"));
        });
    }
</script>
</head>
<input type="hidden" id="_vendorCode">
<input type="hidden" id="_requestNo">
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table id='warehousingApplicationFormDetailDg'>
                    <thead>
                    <tr>
                        <th field='purchasingNo' width='180px',align='center'>采购单号</th>
                        <th field='inventoryTypeName' width='120px' align='center'>物料类别</th>
                        <th field='inventoryCode' width='120px' align='center'>物料代码</th>
                        <th field='inventoryName' width='120px' align='center'>物料名称</th>
                        <th field='specificationType' width='120px' align='center'>规格型号</th>
                        <th field='unit' width='80px' align='center'>单位</th>
                        <th field='amount' width='80px' align='center' editor="{type:'numberbox',options:{precision:2,}}"
                            data-options="formatter:function(value,row,index){
                            if(row.amount){
                                return formatFloat(row.amount,2);
                            }else{
                                return '';
                            }
                        }">数量</th>
                        <th field='batchNumber' width='150px' align='center' editor="text">批号</th>
                        <th field='furnaceNumber' width='150px' align='center' editor="text">材料编号</th>
                        <th field='stoveNumber' width='180px' align='center' editor="text">炉号</th>
                        <th field='manufacturer' width='180px' align='center'
                            editor="{type:'combobox',options:{valueField:'cValue',textField:'cValue',url:'steelMill/queryAll.do'}}">钢厂</th>
                        <th field='amountOfPerBox' width='80px' align='center' data-options="formatter:function(value,row,index){
                            if(row.amountOfPerBox){
                                return formatFloat(row.amountOfPerBox,3);
                            }else{
                                return '';
                            }
                        }">每箱数</th>
                        <th field='amountOfBoxes' width='80px' align='center'  editor="{type:'numberbox'}">箱数</th>
                        <th field='locationCode' width='120px' align='center'  editor="text">货位</th>
                        <th field='warehouseCode' width='180px' align='center' editor="text">仓库代码</th>
                        <th field='warehouseName' width='180px' align='center'>入库仓库</th>
                        <th field='amountOfInWarehouse' width='80px' align='center'>入库数量</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div data-options="region:'south',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:10%;">
                <div class="topjui-fluid" style="margin-top: 20px;">
                    <div class="topjui-row">
                        <div class="topjui-col-sm4">
                            <label class="topjui-form-label">入库状态</label>
                            <div class="topjui-input-block">
                                <input type="text" name="warehousingStatus" readonly data-toggle="topjui-textbox" id="warehousingStatus">
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
<div id="warehousingApplicationFormDetailDg-toolbar" class="topjui-toolbar">
<sec:authorize access="hasAuthority('ADDLINE_WAREHOUSINGAPPLICATIONFORMDETAIL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton" data-options="iconCls: 'fa fa-plus'"
       onclick="addLines()" id="addLineBtn">添加行</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DELLINE_WAREHOUSINGAPPLICATIONFORMDETAIL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="iconCls: 'fa fa-trash'"
       onclick="deleteLine()" id="deleteLineBtn">删除行</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SPLITBOX_WAREHOUSINGAPPLICATIONFORMDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-code-fork'"
       data-toggle="topjui-menubutton" onclick="unpacking()" id="unpackingBtn">拆箱</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SAVE_WAREHOUSINGAPPLICATIONFORMDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-save'"
       data-toggle="topjui-menubutton" onclick="saveWarehousingApplicationForm()" id="saveBtn">保存</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-check-square-o'"
       data-toggle="topjui-menubutton" onclick="auditAtFormDetail()" id="auditBtn">审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('REVERSEEXAMINE_WAREHOUSINGAPPLICATIONFORMDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-history'"
       data-toggle="topjui-menubutton" id="unauditBtn" onclick="unauditAtFormDetail()">反审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('BOXBAR_WAREHOUSINGAPPLICATIONFORMDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-barcode'"
       data-toggle="topjui-menubutton" id="boxBar" onclick="showBoxBar()">箱号条码</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PRINT_WAREHOUSINGAPPLICATIONFORMDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-eye'"
       data-toggle="topjui-menubutton" id="boxBarPreviewBtn" onclick="openWindow()">条码打印预览</a>
</sec:authorize>
<sec:authorize access="hasAuthority('INWAREHOUSE_WAREHOUSINGAPPLICATIONFORMDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-sign-in'"
       data-toggle="topjui-menubutton" id="inWarehouseBtn" onclick="inWarehouse()">入库</a>
</sec:authorize>
    <form id="ff" method="post">
        <div title="入库申请单" data-options="iconCls:'fa fa-th'">
            <div class="topjui-fluid">
                <div style="height: 30px;text-align: center;width: 100%;font-size: 24px;font-weight: bold;margin-bottom: 30px;margin-top: 30px;">
                    入库申请单
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">供应商</label>
                        <div class="topjui-input-block">
                            <input type="text" name="vendor" data-toggle="topjui-textbox" id="vendor" >
                            <input type="hidden" name="vendorCode"  id="vendorCode">
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">申请单号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="code" data-toggle="topjui-textbox"
                                   data-options="required:true" id="requestNo">

                            <input type="hidden" id="cDepCode" />
                            <input type="hidden" id="cPersonCode" />
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">审核状态</label>
                        <div class="topjui-input-block">
                            <input type="text" name="auditStatus" data-toggle="topjui-textbox"
                                   readonly id="auditStatus">
                        </div>
                    </div>
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">入库仓库</label>
                        <div class="topjui-input-block">
                            <input type="text" name="warehouseName" data-toggle="topjui-textbox" id="warehouseName">
                            <input type="hidden" name="warehouseCode"  id="warehouseCode">
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">收料日期</label>
                        <div class="topjui-input-block">
                            <input type="text" name="code" data-toggle="topjui-datebox"
                                   id="receivingDate">
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">申请人员</label>
                        <div class="topjui-input-block">
                            <input type="text" name="code" data-toggle="topjui-textbox"
                                   readonly id="applierName">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div id="showVendors4DetailDialog"></div>
<div id="showWarehouses4DetailDialog"></div>
<div id="showPodetailDialog"></div>
<div id="showDetailDialog"></div>
<div id="showLocationDialog"></div>
</body>
</html>
