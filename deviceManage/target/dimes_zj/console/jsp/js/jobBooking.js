$(function() {
    $("#jobBookingDate").iDatetimebox("setValue", getDateTime(new Date()));
    //添加行，弹出查找物料信息框
    $('#showWorkSheetsDialog').dialog({
        title: '工单信息',
        width: 1200,
        height: 600,
        closed: true,
        cache: false,
        href: 'console/jsp/showWorkSheets.jsp',
        modal: true,
        buttons:[{
            text:'确定',
            handler:function(){
                //获取选中的采购订单详情id
                var workSheet = $('#workSheetTable').iDatagrid('getSelected');
                confirmWorkSheets(workSheet);
            }
        },{
            text:'关闭',
            handler:function(){
                $('#showWorkSheetsDialog').dialog("close");
            }
        }]
    });
});
//工单单号确定
function confirmWorkSheets(workSheet){
	if(workSheet){
        fillWorkSheet(workSheet);
        $("#jobBookingFormDetailDg").iEdatagrid("reload");
    }
    $('#showWorkSheetsDialog').dialog("close");
}
//根据条件查询报工单详情
function queryJobBookingFormDetails(){
    $("#jobBookingFormDetailDg").iDatagrid("load",{
        from:$("#from").val(),
        to:$("#to").val(),
        productionUnitCode:$("#productionUnitCodeSearch").val(),
        no:$("#noSearch").val(),
        inventoryCode:$("#inventoryCodeSearch").val(),
        barCode:$("#barCodeSearch").val(),
        batchNo:$("#batchNoSearch").val()
    })
}
//根据报工单号查找报工单详情
function queryJobBookingFormDetailsByFormNo() {
    var detail = $("#jobBookingFormDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要查看的记录!");
        return false;
    }
    //$.get("jobBookingFormDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'报工单查看/新增',href:'console/jsp/jobBookingDetail.jsp?formNo='+ detail.jobBookingForm.formNo,url:"jobBookingFormDetail/clearSession.do"});
   // });
}
//新增报工单时，生成默认单号
function generateRequestNo() {
    $.get("jobBookingForm/generateRequestNo.do",function (result) {
        $("#formNo").iTextbox("setValue",result);
    });
}
//新增报工单初始化组件
function init() {
    $("#auditBtn").iMenubutton("disable");
    $("#unauditBtn").iMenubutton("disable");
    $("#editBtn").iMenubutton("disable");
    $("#boxBar").iMenubutton("disable");
    $("#boxBarPreviewBtn").iMenubutton("disable");
    $("#inWarehouseBtn").iMenubutton("disable");
}
//根据报工单编码查找报工单信息
function queryJobBookingFormByFormNo(formNo){
    $("#formNo").iTextbox("readonly");
    $("#workSheetNo").iTextbox("readonly");
    $("#processCode").iTextbox("readonly");
    $("#deviceSiteCode").iTextbox("readonly");
    $("#classCode").iTextbox("readonly");
    //填充表头信息
    $.get("jobBookingForm/queryByFormNo.do",{formNo:formNo},function(result){
        if(result){
            $("#formNo").iTextbox("setValue",result.formNo);
            $("#jobBookingDate").iDatetimebox("setValue",getDateTime(new Date(result.jobBookingDate)));
            $("#productionUnitName").iTextbox("setValue",result.productionUnitName);
            $("#productionUnitCode").val(result.productionUnitCode);
            $("#jobBookerName").iTextbox("setValue",result.jobBookerName);
            $("#jobBookerName").iTextbox("readonly");
            $("#auditor").iTextbox("setValue",result.auditorName);
            $("#classCode").iCombobox("setValue",result.classCode);
            $("#classCode").iCombobox("setText",result.className);
            $("#deviceSiteCode").iCombobox("setValue",result.deviceSiteCode);
            $("#deviceSiteCode").iCombobox("setText",result.deviceSiteName);
            $("#processCode").iCombobox("setValue",result.processesCode);
            setTimeout(function(){$("#processCode").iCombobox("setText",result.processesName);},100);
            $("#processCode").iCombobox("setText",result.processesName);

            $("#workSheetNo").iTextbox("setValue",result.workSheetNo);
            $("#inventoryCode").iTextbox("setValue",result.inventoryCode);
            $("#inventoryName").iTextbox("setValue",result.inventoryName);
            $("#batchNumber").iTextbox("setValue",result.batchNumber);
            $("#specificationType").iTextbox("setValue",result.unitType);
            $("#furnaceNumber").iTextbox("setValue",result.stoveNumber);

            if(result.auditStatus=='已审核'){
                $("#auditDate").iTextbox("setValue",getDate(new Date(result.auditDate)));
                $("#saveBtn").iMenubutton("disable");
                $("#auditBtn").iMenubutton("disable");
                $("#boxBar").iMenubutton("enable");
                $("#boxBarPreviewBtn").iMenubutton("enable");
                $("#inWarehouseBtn").iMenubutton("enable");
            }
            if(result.auditStatus=='未审核'){
                $("#auditDate").iTextbox("setValue","");
                $("#unauditBtn").iMenubutton("disable");
                $("#editBtn").iMenubutton("disable");
                $("#boxBar").iMenubutton("disable");
                $("#auditBtn").iMenubutton("enable");
                $("#saveBtn").iMenubutton("enable");
                $("#boxBarPreviewBtn").iMenubutton("disable");
                $("#inWarehouseBtn").iMenubutton("disable");
            }
            //显示可编辑列表
            showJobBookingForms(formNo);
            showRawMaterial();
        }
    });
}
//更新原材料
function updateRawMaterial(row,from){
    var amountOfUsed = 0;
    if(from == 'barCode'){
        amountOfUsed = row.amount;
    }

    if(from=='amountOfUsed'){
        amountOfUsed = row.amountOfUsed;
    }
    $.get('jobBookingFormDetail/updateByJobBookingDetailId.do',{
        id:row.id,
        inventoryCode : row.inventoryCode,
        inventoryName : row.inventoryName,
        specificationType : row.specificationType,
        amount : row.amount,
        batchNumber : row.batchNumber,
        furnaceNumber : row.furnaceNumber,
        amountOfUsed : amountOfUsed,
        jobBookingFormDetailId:$("#jobBookingFormDetailDg").iEdatagrid("getSelected").id,
        barCode:row.barCode
    },function(result){

    });
}

//显示原材料列表
function showRawMaterial(){
    $('#rawMaterialDg').iEdatagrid({
        idField:'barCode',
        rownumbers:true,
        fitColumns:true,
        pagination:false,
        autoSave:true,
        url: 'jobBookingFormDetail/queryByJobBookingDetailId.do',
        updateUrl:'jobBookingFormDetail/updateByJobBookingDetailId.do',
        onEndEdit:function(index,row,changes){
            if(row.amountOfUsed>row.amount){
                alert("消耗数不能大于数量!");
                row.amountOfUsed=row.amount
                return false;
            }
            if(changes.barCode){
                var data = $('#rawMaterialDg').iEdatagrid("getData");
                if(data.rows&&data.rows.length>0){
                    var count = 0;
                    for(var i = 0;i<data.rows.length;i++){
                        var rowData = data.rows[i];
                        if(rowData.barCode==row.barCode){
                            count++;
                            if(count>1){
                                alert("该条码已在行列表中！");
                                $('#rawMaterialDg').iEdatagrid("edit");
                                return false;
                            }
                        }
                    }
                }
                $.get("materialRequisitionDetail/queryByWorkSheetNoAndBarCode.do",{barCode:changes.barCode,workSheetNo: $("#workSheetNo").iTextbox("getValue")},
                    function(data){
                    if(data.length<=0){
                        alert("领料条码错误!");
                    }else{
                        var result = data[0];
                        row.id = result.id;
                        if(!row.id){
                            row.id=generateUUID();
                        }
                        row.inventoryCode = result.inventoryCode;
                        row.inventoryName = result.inventoryName;
                        row.specificationType = result.specificationType;
                        row.amount = result.amount;
                        row.batchNumber = result.batchNumber;
                        row.furnaceNumber = result.furnaceNumber;
                        row.amountOfUsed = result.amount;
                        row.unitName = result.unitName;
                        row.jobBookingFormDetailId=$("#jobBookingFormDetailDg").iEdatagrid("getSelected").id;
                        updateRawMaterial(row,'barCode');
                        $('#rawMaterialDg').iEdatagrid("refreshRow",index);
                    }
                });
            }else{
                updateRawMaterial(row,'amountOfUsed');
            }
        }, toolbar: [{
            iconCls: 'fa fa-plus',
            text:'新增行',
            handler: function(){
                var detail = $('#jobBookingFormDetailDg').iEdatagrid('getSelected');
                if(!detail){
                    alert("请选择箱条码信息!");
                    return false;
                }
                $('#rawMaterialDg').iEdatagrid("addRow");
            }
        },'-',{
            iconCls: 'fa fa-trash',
            text:'删除行',
            handler: function(){
                var detail = $('#rawMaterialDg').iEdatagrid('getSelected');
                if(!detail){
                    alert("请选择要删除的数据!");
                    return false;
                }

                $.iMessager.confirm('确认','您确认想要删除记录吗？',function(r){
                    if (r){
                        $.get("jobBookingFormDetail/deleteRawMaterialById.do",{id:detail.id},function (result) {
                            if(result.statusCode==200){
                                $.iMessager.show({
                                    title:'提示',
                                    msg:result.message,
                                    timeout:3000,
                                    showType:'slide'
                                });
                                $('#rawMaterialDg').iEdatagrid("reload",{
                                        jobBookingDetailId:$("#jobBookingFormDetailDg").iEdatagrid("getSelected").id
                                });
                            }
                        });
                    }
                });
            }
        }]
    });
}
/**
 * 报工单详情中显示报工单 详情
 * @param requestNo
 */
function showJobBookingForms(requestNo) {
    $('#jobBookingFormDetailDg').iEdatagrid({
        idField:'id',
        pagination:false,
        fitColumns:true,
        autoSave:true,
        url: 'jobBookingFormDetail/queryByFormNo.do',
        updateUrl:'jobBookingFormDetail/updateSession.do',
        queryParams:{
            formNo:requestNo,
            processCode:$("#processCode").iCombobox("getValue")
        },onEndEdit:function(index,row,changes){
            $.get('jobBookingFormDetail/updateSession.do',{id:row.id,amountOfJobBooking:row.amountOfJobBooking,boxNum:row.boxNum,boxBar:row.boxBar},
                function (result) {

                });
        },onSelect:function(index,row){
            $('#rawMaterialDg').iEdatagrid("reload",{
                    jobBookingDetailId:$("#jobBookingFormDetailDg").iEdatagrid("getSelected").id
            });
        },onAdd:function(index,row){
            //查找session中最大的箱号
            $.get("jobBookingFormDetail/queryMaxBoxNumInSession.do",{
                /* no:$("#no").iTextbox("getValue"),
                  processCode:$('#pCode').val()*/
                no:$("#workSheetNo").iTextbox('getValue'),
                processCode:$('#pCode').val()
            },function(result){
                row.boxNum = result.maxBoxNum;
                row.id = result.newId;
            });
        }, toolbar: [{
            iconCls: 'fa fa-plus',
            text:'新增行',
            handler: function(){
                var workSheetNo = $("#workSheetNo").iTextbox("getValue");
                if(!workSheetNo){
                    alert("请选择生产工单!");
                    return false;
                }
                $('#jobBookingFormDetailDg').iEdatagrid("addRow");
            }
        },'-',{
            iconCls: 'fa fa-trash',
            text:'删除行',
            handler: function(){
                deleteLine();
            }
        }]
    });
}
//删除报工单详情
function deleteLine() {
    var detail = $('#jobBookingFormDetailDg').iEdatagrid('getSelected');
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }

    $.iMessager.confirm('确认','您确认想要删除记录吗？',function(r){
        if (r){
            $.get("jobBookingFormDetail/deleteById.do",{id:detail.id},function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $('#jobBookingFormDetailDg').iEdatagrid('reload');
                }
            });
        }
    });
}
//新增报工单
function addJobBookingForm(){
    //$.get("jobBookingFormDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'报工单查看/新增',href:'console/jsp/jobBookingDetail.jsp',url:"jobBookingFormDetail/clearSession.do"});
    //});
}
//保存报工单(新增和查看)
function saveJobBookingForm(){
    $.iMessager.confirm("提示","确认执行保存操作吗？",function(r){
        if(r){
            save();
        }
    });
}

function save(){
    var no = $("#workSheetNo").iTextbox("getValue");
    if(!no){
        alert("请输入工单单号!");
        return false;
    }
    requestNo = $("#formNo").iTextbox("getValue");
    if(!requestNo){
        alert("请输入报工单号!");
        return false;
    }
    var jobBookingDate = $("#jobBookingDate").iDatetimebox("getValue");
    if(!jobBookingDate){
        alert("请输入报工日期!");
        return false;
    }

    var classCode = $("#classCode").iCombobox("getValue");
    if(!classCode){
        alert("请选择班次!");
        return false;
    }
    $.get("jobBookingFormDetail/saveJobBookingForm.do",{
        formNo : requestNo,
        workSheetNo:$("#workSheetNo").iTextbox("getValue"),
        jobBookingDate : jobBookingDate,
        productionUnitCode : $("#productionUnitCode").val(),
        productionUnitName : $("#productionUnitName").iTextbox("getValue"),
        classCode:classCode,
        className:$("#classCode").iCombobox("getText"),
        inventoryCode:$("#inventoryCode").iTextbox("getValue"),
        inventoryName:$("#inventoryName").iTextbox("getValue"),
        unitType:$("#specificationType").iTextbox("getValue"),
        batchNumber:$("#batchNumber").iTextbox("getValue"),
        stoveNumber:$("#furnaceNumber").iTextbox("getValue"),
        processesCode:$("#processCode").iCombobox("getValue"),
        processesName:$("#processCode").iCombobox("getText"),
        deviceSiteCode:$("#deviceSiteCode").iCombobox("getValue"),
        deviceSiteName:$("#deviceSiteCode").iCombobox("getText")
    },function(result){
        if(result.success){
            $.iMessager.alert("提示","保存成功!");
            $("#_requestNo").val(requestNo);
            queryJobBookingFormByFormNo(requestNo);
        }else{
            $.iMessager.alert("提示",result.msg);
        }
    });
}
/**
 * 拆箱
 */
function unpacking() {
    var detailRecord = $('#jobBookingFormDetailDg').iEdatagrid("getSelected");
    if(!detailRecord){
        alert("请选择要拆箱的记录!");
        return false;
    }
    if(!detailRecord.amountOfJobBooking){
        alert("请输入报工数量!");
        return false;
    }
    if(!detailRecord.amountOfPerBox){
        alert("请输入每箱数!");
        return false;
    }

    if(parseFloat(detailRecord.amountOfJobBooking)<parseFloat(detailRecord.amountOfPerBox)){
        alert("报工数量小于每箱数!");
        return false;
    }
    if(parseFloat(detailRecord.amount)==parseFloat(detailRecord.amountOfPerBox)){
        alert("报工数量与每箱数相同，无需拆箱!");
        return false;
    }

    $.get("jobBookingFormDetail/unpacking.do",{id:detailRecord.id},function(result){
        $('#jobBookingFormDetailDg').iEdatagrid("reload");
    });
}
//报工单页审核
function auditAtForm() {
    var detail = $("#jobBookingFormDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要审核的数据!");
        return false;
    }
    audit(detail.jobBookingForm.formNo,'form');
}
//报工单页反审核
function unauditAtForm() {
    /*$.iMessager.confirm("警告","反审核将删除已有的箱号条码信息，确认该操作吗?",function(r) {
        if (r) {
            var detail = $("#jobBookingFormDetailDg").iDatagrid("getSelected");
            if (!detail) {
                alert("请选择要反审核的数据!");
                return false;
            }
            unaudit(detail.jobBookingForm.formNo, 'form');
        }
    });*/
    var detail = $("#jobBookingFormDetailDg").iDatagrid("getSelected");
    if (!detail) {
        alert("请选择要反审核的数据!");
        return false;
    }
    unaudit(detail.jobBookingForm.formNo, 'form');
}

//报工单详情页审核
function auditAtFormDetail(){
    var formNo =  $("#formNo").iTextbox("getValue");
    if(!formNo){
        alert("不存在该申请单信息,审核失败!");
        return false;
    }
    audit(formNo,'formDetail');
}
//报工单详情页反审核
function unauditAtFormDetail(){
    $.iMessager.confirm("警告","反审核将删除已有的箱号条码信息，确认该操作吗?",function(r){
        if(r){
            var formNo =  $("#formNo").iTextbox("getValue");
            if(!formNo){
                alert("不存在该申请单信息,审核失败!");
                return false;
            }
            unaudit(formNo,'formDetail');
        }
    });
}
//反审核
function unaudit(formNo,from) {
    $.get("jobBookingForm/queryByFormNo.do",{
        formNo : formNo
    },function (result) {
        if(result==null){
            alert("不存在该申请单信息,操作失败!");
        }else{
            if(result.auditStatus=='未审核'){
                alert('该申请单未被审核，操作无效!');
            }else{
                $.get("jobBookingForm/unaudit.do",{formNo:formNo},function(result){
                    if(result.statusCode==200){
                        alert("操作完成!");
                        if(from=='formDetail'){
                            queryJobBookingFormByFormNo(formNo);
                        }

                        if(from=='form'){
                            $("#jobBookingFormDetailDg").iDatagrid("reload");
                            $("#audit").iMenubutton("enable");
                            $("#unaudit").iMenubutton("disable");
                        }
                    }else{
                        alert(result.message);
                    }
                });
            }
        }
    });
}

//审核报工单
function audit(formNo,from) {
    $.get("jobBookingForm/queryByFormNo.do",{
        formNo : formNo
    },function (result) {
        if(result==null){
            alert("不存在该申请单信息,审核失败!");
        }else{
            if(result.auditStatus=='已审核'){
                alert('该申请单已被审核，无需再次审核!');
            }else{
                $.get("jobBookingForm/audit.do",{formNo:formNo},function(result){
                    alert("审核完成!");
                    if(from=='formDetail'){
                        queryJobBookingFormByFormNo(formNo);
                        $("#unauditBtn").iMenubutton("enable");
                        $("#editBtn").iMenubutton("enable");
                    }

                    if(from=='form'){
                        $("#jobBookingFormDetailDg").iDatagrid("reload");
                        $("#unaudit").iMenubutton("enable");
                    }
                });
            }
        }
    });
}
//删除报工单及详情
function deleteJobBookingForm(){
    var detail = $("#jobBookingFormDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }

    $.iMessager.confirm("提示","确认删除?",function(r){
        if(r){
            $.get("jobBookingForm/deleteByFormNo.do",{
                formNo : detail.jobBookingForm.formNo
            },function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $("#jobBookingFormDetailDg").iDatagrid("reload");
                }

                if(result.statusCode==300){
                    $.iMessager.alert("警告",result.message,"messager-error");
                }
            });
        }
    })
}
//箱号条码按钮点击事件
function showBoxBar() {
    addParentTab({title:'箱号条码',href:'console/jsp/showBoxBarCodes.jsp'});
}

// 填充新增报工记录头部信息
function fillWorkSheet(workSheet){
    $.get("jobBookingFormDetail/removeJobBookingListFromSession.do",function(result){
        $("#workSheetNo").iTextbox("setValue",workSheet.no);
        $("#productionUnitCode").val(workSheet.productionUnitCode);
        $("#productionUnitName").iTextbox("setValue",workSheet.productionUnitName);
        $("#inventoryCode").iTextbox("setValue",workSheet.workPieceCode);
        $("#inventoryName").iTextbox("setValue",workSheet.workPieceName);
        $("#batchNumber").iTextbox("setValue",workSheet.batchNumber);
        $("#specificationType").iTextbox("setValue",workSheet.unitType);
        $("#furnaceNumber").iTextbox("setValue",workSheet.stoveNumber);

        $("#processCode").iCombobox("clear");
        $("#processCode").iCombobox("reload","workSheet/queryProcessCodeAndNameByNo.do?no="+$('#workSheetNo').iTextbox('getValue'));
        $("#jobBookingFormDetailDg").iEdatagrid("reload");
        $("#rawMaterialDg").iEdatagrid("reload");
    });

}