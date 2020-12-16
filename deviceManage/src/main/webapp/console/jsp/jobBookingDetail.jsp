<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="console/jsp/js/jobBooking.js"></script>
<script type="text/javascript">
    var requestNo='';
    $(function () {
        $("#jobBookerName").iTextbox("readonly");
        requestNo = '<%=request.getParameter("formNo")%>';
        if (requestNo == 'null') {
            requestNo = '<%=session.getAttribute("formNo")%>';
            if (requestNo == 'null') {
                requestNo = '';
            }
        }
        //添加报工单
        if (!requestNo) {
            $.get("user/queryLoginUser.do", function (result) {
                if (result.employee) {
                    $("#jobBookerName").iTextbox("setValue", result.employee.name);
                } else {
                    $("#jobBookerName").iTextbox("setValue", result.username);
                }
            });
            init();
            generateRequestNo();
            requestNo = '';
            //显示可编辑表格
            showJobBookingForms(requestNo);
            showRawMaterial();
        } else {//查看报工单
            $("#formNo").iTextbox("readonly");
            $("#workSheetNo").iTextbox("readonly");
            $("#processCode").iTextbox("readonly");
            $("#deviceSiteCode").iTextbox("readonly");
            $("#classCode").iTextbox("readonly");
            queryJobBookingFormByFormNo(requestNo);
        }

        $("#workSheetNo").iTextbox({
            buttonIcon: 'fa fa-search',
            onClickButton: function () {
                $('#showWorkSheetsDialog').dialog({title: '工单信息'});
                $('#showWorkSheetsDialog').dialog("open");
            }
        });
        //工单单号回车事件
        $("#workSheetNo").iTextbox("textbox").bind("keydown", function (event) {
            if (event.keyCode == 13) {
                $.get("workSheet/queryWorkSheetByNo.do", {No: $("#workSheetNo").iTextbox("getValue")}, function (result) {
                    fillWorkSheet(result);
                });
            }
        });
    });
    function openWindow(){
        var formNo = $("#formNo").iTextbox("getValue");
        if(formNo){
             window.open("console/jsp/barCode_print.jsp?formNo="+formNo);
        }else{
            alert("没有打印内容 !");
            return false;
        }
    }


    //修改报工数量
    function updateJobbookingNumber(){
        var selectRow=$('#jobBookingFormDetailDg').iDatagrid('getSelected');
        if(!selectRow){
            $.iMessager.alert('提示','请选择箱子');
            return false;
        }

        $.get("boxBar/queryByBarCode.do", {
            barCode: selectRow.barCode
        }, function (data) {
            if(data.haveRequisition){
                $.iMessager.alert('提示','该箱已领料');
                return false;
            }else{
                $('#editBarcode').iTextbox("setValue", selectRow.barCode);
                $('#editBarcodeNumber').iNumberbox('setValue', selectRow.amountOfJobBooking);

                $('#updateRawMaterialDg').iEdatagrid({
                    idField:'barCode',
                    rownumbers:true,
                    fitColumns:true,
                    pagination:false,
                    autoSave:true,
                    url: 'jobBookingFormDetail/queryByJobBookingDetailId.do',
                    queryParams: {
                        jobBookingDetailId:$("#jobBookingFormDetailDg").iEdatagrid("getSelected").id
                    },
                    onBeginEdit:function(index,row){
                        console.log(index);
                        var ed = $('#updateRawMaterialDg').iEdatagrid('getEditor', {index:0,field:'amountOfUsed'});
                        console.log(ed);
                        $(ed.target).textbox('textbox').bind('blur', function(e){
                            $('#updateRawMaterialDg').iEdatagrid('endEdit',0);
                        })
                    },
                    onEndEdit:function(index,row,changes){
                        if(row.amountOfUsed>row.amount){
                            alert("消耗数不能大于数量!");
                            row.amountOfUsed=row.amount
                            return false;
                        }

                    }
                });


                $('#showUpdateDialog').iDialog({
                    title: '修改报工数',
                    width: 1000,
                    height: 400,
                    closed: false,
                    cache: false,
                    onBeforeDestroy:function(){return false},
                    buttons:[{
                        text:'保存',
                        handler:function(){
                            var getRows=$("#updateRawMaterialDg").iEdatagrid("getRows");
                            var ids=[];
                            var materialNumbers=[];
                            for(var i=0;i<getRows.length;i++){
                                ids.push(getRows[0].id)
                                materialNumbers.push(getRows[0].amountOfUsed)
                            }
                            console.log(ids);
                            console.log(materialNumbers);
                            $("#updateRawMaterialDg").iEdatagrid("getData");

                            $.get("jobBookingFormDetail/updateJobBookingNumberAndBoxbarNumber.do", {
                                barcode: $('#editBarcode').iTextbox("getValue"),
                                boxbarNumber:$('#editBarcodeNumber').iNumberbox("getValue"),
                                materialIds:ids.toString(),
                                materialNumbers:materialNumbers.toString()
                            }, function (result) {
                            });
                        }
                    },{
                        text:'关闭',
                        handler:function(){
                            $('#showUpdateDialog').iDialog('close');
                        }
                    }]
                });
                $('#showUpdateDialog').iDialog('open');
            }
        });


    }
</script>
<style type="text/css">
    body{
        margin: 0;
        padding:0 auto;
    }
 /*   .datagrid-row-selected{
        background: #FFFFFF;!*自定义颜色*!
    }*/
</style>
</head>
<body>
<input type="hidden" id="_requestNo">
<div  style="width: 100%;">
<sec:authorize access="hasAuthority('SAVE_JOBOOKINGDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-save'"
       data-toggle="topjui-menubutton" onclick="saveJobBookingForm()" id="saveBtn">保存</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXAMINE_JOBOOKINGDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-check-square-o'"
       data-toggle="topjui-menubutton" onclick="auditAtFormDetail()" id="auditBtn">审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('REVERSEEXAMINE_JOBOOKINGDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-history'"
       data-toggle="topjui-menubutton" id="unauditBtn" onclick="unauditAtFormDetail()">反审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXAMINE_JOBOOKINGDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-check-square-o'"
       data-toggle="topjui-menubutton" onclick="updateJobbookingNumber()" id="editBtn">修改报工数</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PRINT_JOBOOKINGDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-eye'"
       data-toggle="topjui-menubutton" id="boxBarPreviewBtn" onclick="openWindow()">条码打印预览</a>
</sec:authorize>
<sec:authorize access="hasAuthority('INWAREHOUSESTATUS_JOBOOKINGDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-barcode'"
       data-toggle="topjui-menubutton" id="boxBar" onclick="showBoxBar()">入库状况</a>
</sec:authorize>
<sec:authorize access="hasAuthority('INWAREHOUSE_JOBOOKINGDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-sign-in'"
       data-toggle="topjui-menubutton" id="inWarehouseBtn">入库</a>
</sec:authorize>
</div>
<div style="width: 100%;">
    <form id="ff" method="post">
        <div title="报工单" data-options="iconCls:'fa fa-th'">
            <div class="topjui-fluid" style="width:98%;">
                <div style="height: 30px;text-align: center;width: 100%;font-size: 24px;font-weight: bold;margin-bottom: 30px;margin-top: 30px;">
                    生产报工单
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">报工单号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="formNo" data-toggle="topjui-textbox"
                                   data-options="required:true" id="formNo">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">报工日期</label>
                        <div class="topjui-input-block">
                            <input type="text" name="jobBookingDate" data-toggle="topjui-datetimebox"
                                   id="jobBookingDate">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">生产单元</label>
                        <div class="topjui-input-block">
                            <input type="text" id="productionUnitName"
                                   name="productionUnitName" data-toggle="topjui-textbox" readonly>
                            <input type="hidden" id="productionUnitCode" />
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">班次</label>
                        <div class="topjui-input-block">
                            <input type="text" name="classCode" data-toggle="topjui-combobox" data-options="
                            valueField: 'code',
                            textField: 'name',
                            url:'classes/queryAllClasses.do',onLoadSuccess:function(){
                                var data = $('#classCode').iCombobox('getData');
                                $('#classCode').iCombobox('select',data[0].code);
                            }"   id="classCode">
                        </div>
                    </div>
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">工单单号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="workSheetNo" data-toggle="topjui-textbox"
                                   id="workSheetNo">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">物料代码</label>
                        <div class="topjui-input-block">
                            <input type="text" name="inventoryCode" data-toggle="topjui-textbox"
                                   id="inventoryCode" readonly>
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">物料名称</label>
                        <div class="topjui-input-block">
                            <input type="text" name="inventoryName" data-toggle="topjui-textbox"
                                   id="inventoryName" readonly>
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">规格型号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="specificationType" data-toggle="topjui-textbox"
                                   id="specificationType" readonly>
                        </div>
                    </div>
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">工序</label>
                        <div class="topjui-input-block">
                            <input type="text" name="processCode" data-toggle="topjui-combobox"
                                   id="processCode" data-options="valueField: '0',
                            textField: '1',
                            url:'workSheet/queryProcessCodeAndNameByNo.do',
                            onLoadSuccess:function(){
                                 var data = $('#processCode').iCombobox('getData');
                                   if(data!=null &&data.length>0){
                                     $('#pCode').val(data[0][0]);
                                    $('#processCode').iCombobox('select',data[0][0]);
                                    $('#processCode').iCombobox('setText',data[0][1]);
                                    $('#deviceSiteCode').iCombobox('reload',{no:$('#workSheetNo').iTextbox('getValue'),
                                    processCode:data[0][0]});
                                }
                            },
                            onSelect:function(record){
                                $('#deviceSiteCode').iCombobox('reload',{no:$('#workSheetNo').iTextbox('getValue'),
                                processCode:record[0]});

                                 $('#pCode').val(record[0]);
                            }">

                            <input type="hidden" id="pCode">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">站点代码</label>
                        <div class="topjui-input-block">
                            <input type="text" name="deviceSiteCode" data-toggle="topjui-combobox"
                                   id="deviceSiteCode" data-options="valueField: '0',
                            textField: '1',
                            url:'workSheet/queryDeviceSitesByNoAndProcessCode.do',
                            onLoadSuccess:function(){
                                 var data = $('#deviceSiteCode').iCombobox('getData');
                                $('#deviceSiteCode').iCombobox('select',data[0][0]);
                            }">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">批号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="batchNumber" data-toggle="topjui-textbox"
                                   id="batchNumber">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">材料编号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="furnaceNumber" data-toggle="topjui-textbox"
                                   id="furnaceNumber" readonly>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
<div style="height:505px;width:96%;margin:0 auto;" title="箱材料信息">
    <div style="float: left;width: 30%;height:500px;">
        <table id="jobBookingFormDetailDg">
            <thead>
            <th field='id' width='100px' hidden="true" align='center'>id</th>
            <th field='barCode' width='100px' align='center'>箱条码</th>
            <th field='amountOfJobBooking' width='100px' align='center' editor="{type:'numberbox',options:{precision:2,}}" data-options="formatter:function(value,row,index){
                    if(row.amountOfJobBooking){
                    return formatFloat(row.amountOfJobBooking,3);
                    }else{
                    return '';
                    }
                }">数量</th>
            <th field='boxNum' width='100px' align='center'>箱号</th>
            </thead>
        </table>
    </div>
    <div style="float: right;width: 69%;height:500px;">
        <table id='rawMaterialDg'>
            <thead>
            <tr>
                <th field='id' width='100px' hidden="true" align='center' editor="text">id</th>
                <th field='jobBookingFormDetailId' width='100px' hidden="true" align='center'>jobBookingFormDetailId</th>
                <th field='barCode' width='100px' align='center' editor="text">材料条码</th>
                <th field='inventoryCode' width='100px' align='center'>物料代码</th>
                <th field='inventoryName' width='100px' align='center'>物料名称</th>
                <th field='specificationType' width='100px' align='center'>规格型号</th>
                <th field='batchNumber' width='100px' align='center'>批号</th>
                <th field='furnaceNumber' width='100px' align='center'>材料编号</th>
                <th field='unitName' width='80px' align='center'>单位</th>
                <th field='amount' width='80px' align='center' data-options="formatter:function(value,row,index){
                    if(row.amount){
                    return formatFloat(row.amount,3);
                    }else{
                    return '';
                    }
                }">数量</th>
                <th field='amountOfUsed' width='80px' align='center' editor="{type:'numberbox',options:{precision:2}}">消耗数量</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<div class="topjui-fluid" style="margin-top: 20px;">
    <div class="topjui-row">
        <div class="topjui-col-sm4">
            <label class="topjui-form-label">报工人</label>
            <div class="topjui-input-block">
                <input type="text" name="jobBookerName" readonly data-toggle="topjui-textbox" id="jobBookerName">
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
<div id="showWorkSheetsDialog"></div>




</body>
</html>
<div id="showUpdateDialog" data-toggle="topjui-dialog">
    <div style="margin-top: 10px;margin-bottom: 20px;">
        <label style="margin-left: 280px;">箱号</label>
        <input type="text" name="editBarcode" data-toggle="topjui-textbox" style="width: 200px;margin-left: 20px;" data-options="required:true" id="editBarcode" readonly>
        <label style="margin-left: 30px">数量</label>
        <input type="text" name="editBarcodeNumber" data-toggle="topjui-numberbox" style="width: 200px;" data-options="required:true" id="editBarcodeNumber">

    </div>
    <table id='updateRawMaterialDg'>
        <thead>
        <tr>
            <th field='id' width='100px' hidden="true" align='center' editor="text">id</th>
            <th field='jobBookingFormDetailId' width='100px' hidden="true" align='center'>jobBookingFormDetailId</th>
            <th field='barCode' width='100px' align='center'>材料条码</th>
            <th field='inventoryCode' width='100px' align='center'>物料代码</th>
            <th field='inventoryName' width='100px' align='center'>物料名称</th>
            <th field='specificationType' width='100px' align='center'>规格型号</th>
            <th field='batchNumber' width='100px' align='center'>批号</th>
            <th field='furnaceNumber' width='100px' align='center'>材料编号</th>
            <th field='unitName' width='80px' align='center'>单位</th>
            <th field='amount' width='80px' align='center' data-options="formatter:function(value,row,index){
                    if(row.amount){
                    return formatFloat(row.amount,3);
                    }else{
                    return '';
                    }
                }">数量</th>
            <th field='amountOfUsed' width='80px' align='center' editor="{type:'numberbox',options:{precision:2}}">消耗数量</th>
        </tr>
        </thead>
    </table>
</div>