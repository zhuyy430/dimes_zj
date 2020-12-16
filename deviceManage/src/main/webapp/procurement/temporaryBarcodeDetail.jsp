<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="procurement/js/temporaryBarcode.js"></script>
<script type="text/javascript">
    var _vendorCode;
    $(function () {
        $.get("user/queryLoginUser.do",function(user){
            if(user.employee){
                $("#inputPersonName").iTextbox("setValue",user.employee.name);
            }else{
                $("#inputPersonName").iTextbox("setValue",user.username);
            }
        });

        var  requestNo = '<%=request.getParameter("requestNo")%>';
        if(requestNo=='null'){
            requestNo = '<%=session.getAttribute("formNo")%>';
            if(requestNo=='null'){
                requestNo='';
            }
        }
        //添加临时条码单
        if(!requestNo){
            init();
            generateRequestNo();
            requestNo='';
            //显示可编辑表格
            showTemporaryBarcodes(requestNo);
        }else{//查看入库申请单
            queryTemporaryBarcodeByFormNo(requestNo);
            $("#requestNo").iTextbox("readonly");
        }
        //物料信息弹框
        $('#showInventoryDialog').dialog({
            title: '工件信息',
            width: 800,
            height: 600,
            closed: true,
            cache: false,
            href: 'procurement/showInventories.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function(){
                    var inventory = $('#inventoryTable').iDatagrid('getSelections');
                    if(!inventory){
                        return false;
                    }
                    var codes = new Array();
                    for(var i = 0;i<inventory.length;i++){
                        var inv = inventory[i];
                        codes.push(inv.code);
                    }
                    $.get("temporaryBarcodeDetail/addTemporaryBarcodes2Session.do",{
                        codes:JSON.stringify(codes)
                    },function (result) {
                        //刷新入库申请单详情数据
                        $('#temporaryBarcodeDetailDg').iEdatagrid("reload");
                    });
                    $('#showInventoryDialog').dialog("close");
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showInventoryDialog').dialog("close");
                }
            }]
        });
        //输入物料或报工条码查找窗口
        $('#showSearchDialog').dialog({
            title: '条码录入',
            width: 800,
            height: 600,
            closed: true,
            cache: false,
            href: 'procurement/showSearch.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function() {
                    var barCode = $("#barCode").val();
                    if(!barCode){
                        alert("请输入条码!");
                        return false;
                    }

                    if(!/^[0-9]*$/.test(barCode)){
                        alert("条码信息错误!");
                        return false;
                    }
                    $.post('temporaryBarcodeDetail/addTemporaryBarcodes2Session.do',{barCode:barCode,billType:$("#billType").iCombobox("getValue")},function(result){
                        //刷新入库申请单详情数据
                        $('#temporaryBarcodeDetailDg').iEdatagrid("reload");
                    });
                    $('#showSearchDialog').dialog("close");
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showSearchDialog').dialog("close");
                }
            }]
        });
    });
    function openWindow(){
        var checkedArray = $("#temporaryBarcodeDetailDg").iDatagrid("getRows");
        if(checkedArray.length>0){
            var newWin = window.open("procurement/printTemporaryBarcode.jsp");
        }else{
            alert("没有打印内容 !");
           return false;
        }
    }
    //设置单据类型只读或非只读
    function billTypeMode(){
        $.get("temporaryBarcodeDetail/queryTemporaryBarcodeCountInSession.do",function (result) {
            if(result!=null && result>0){
                $("#billType").iCombobox("readonly");
            }else{
                $("#billType").iCombobox("readonly",false);
            }
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
                <table id='temporaryBarcodeDetailDg'>
                    <thead>
                    <tr>
                        <th field='sourceBarcode' width='180px',align='center'>来源条码</th>
                        <th field='inventoryCode' width='180px' align='center'>物料代码</th>
                        <th field='inventoryName' width='180px' align='center'>物料名称</th>
                        <th field='specificationType' width='180px' align='center'>规格型号</th>
                        <th field='unit' width='80px' align='center'>单位</th>
                        <th field='amount' width='80px' align='center' editor="{type:'numberbox',options:{precision:2,}}">数量</th>
                        <th field='batchNumber' width='180px' align='center' editor="text">批号</th>
                        <th field='furnaceNumber' width='180px' align='center' editor="text">材料编号</th>
                        <th field='stoveNumber' width='180px' align='center' editor="text">炉号</th>
                        <th field='positonCode' width='180px' align='center' editor="text">货位</th>
                        <th field='manufacturer' width='180px' align='center' editor="text">钢厂</th>
                        <th field='amountOfPerBox' width='80px' align='center' editor="{type:'numberbox',options:{precision:2}}">每箱数</th>
                        <th field='amountOfBoxes' width='80px' align='center'>箱数</th>
                        <th field='note' width='80px' align='center' editor="text">备注</th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div data-options="region:'south',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:10%;">
                <div class="topjui-fluid" style="margin-top: 20px;">
                    <div class="topjui-row">
                        <div class="topjui-col-sm3">
                            <label class="topjui-form-label">制单人</label>
                            <div class="topjui-input-block">
                                <input type="text" name="inputPersonName" readonly data-toggle="topjui-textbox" id="inputPersonName">
                            </div>
                        </div>
                        <div class="topjui-col-sm3">
                            <label class="topjui-form-label">制单时间</label>
                            <div class="topjui-input-block">
                                <input type="text" name="inputDate" readonly data-toggle="topjui-textbox" id="inputDate">
                            </div>
                        </div>
                        <div class="topjui-col-sm3">
                            <label class="topjui-form-label">审核人</label>
                            <div class="topjui-input-block">
                                <input type="text" name="auditor" readonly data-toggle="topjui-textbox" id="auditor">
                            </div>
                        </div>
                        <div class="topjui-col-sm3">
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
<div id="temporaryBarcodeDetailDg-toolbar" class="topjui-toolbar">
<sec:authorize access="hasAuthority('ADDLINE_TEMPORARYBARCODEDETAIL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton" data-options="iconCls: 'fa fa-plus'"
       onclick="addLines()" id="addLineBtn">添加行</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DELLINE_TEMPORARYBARCODEDETAIL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="iconCls: 'fa fa-trash'"
       onclick="deleteLine()" id="deleteLineBtn">删除行</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SPLITBOX_TEMPORARYBARCODEDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-code-fork'"
       data-toggle="topjui-menubutton" onclick="unpacking()" id="unpackingBtn">拆箱</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SAVE_TEMPORARYBARCODEDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-save'"
       data-toggle="topjui-menubutton" onclick="saveTemporaryBarcode()" id="saveBtn">保存</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXAMINE_TEMPORARYBARCODEDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-check-square-o'"
       data-toggle="topjui-menubutton" onclick="auditAtFormDetail()" id="auditBtn">审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('REVERSEEXAMINE_TEMPORARYBARCODEDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-history'"
       data-toggle="topjui-menubutton" id="unauditBtn" onclick="unauditAtFormDetail()">反审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('BOXBAR_TEMPORARYBARCODEDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-barcode'"
       data-toggle="topjui-menubutton" id="boxBar" onclick="showBoxBar()">箱号条码</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PRINT_TEMPORARYBARCODEDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-eye'"
       data-toggle="topjui-menubutton" id="boxBarPreviewBtn" onclick="openWindow()">条码打印预览</a>
</sec:authorize>
    <form id="ff" method="post">
        <div title="条码登记单" data-options="iconCls:'fa fa-th'">
            <div class="topjui-fluid">
                <div style="height: 30px;text-align: center;width: 100%;font-size: 24px;font-weight: bold;margin-bottom: 30px;margin-top: 30px;">
                    条码登记单
                </div>
                <div class="topjui-row">

                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">单据编号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="code" data-toggle="topjui-textbox"
                                   data-options="required:true" id="requestNo">
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">单据日期</label>
                        <div class="topjui-input-block">
                            <input type="text" name="billDate" data-toggle="topjui-datebox" id="billDate" >
                        </div>
                    </div>
                    <div class="topjui-col-sm4">
                        <label class="topjui-form-label">单据类型</label>
                        <div class="topjui-input-block">
                            <input id="billType" data-toggle="topjui-combobox"
                                   data-options="valueField:'value',textField:'text',data:[
                                {text:'新增',value:'0',selected:true},{text:'报工条码',value:'1'},{text:'材料条码',value:'2'}]">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div id="showInventoryDialog"></div>
<div id="showSearchDialog"></div>
</body>
</html>
