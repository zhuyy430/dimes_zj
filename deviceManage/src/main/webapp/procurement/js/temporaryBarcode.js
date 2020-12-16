//根据条件查询临时条码详情
function queryTemporaryBarcodeDetails(){
    $("#temporaryBarcodeDetailDg").iDatagrid("reload",{
        from:$("#from").val(),
        to:$("#to").val(),
        inventoryCode:$("#inventoryCode").val(),
        billType:$("#billType").iCombobox("getValue")
    })
}
//根据临时条码号查找临时条码详情
function queryTemporaryBarcodeDetailsByFormNo() {
    var detail = $("#temporaryBarcodeDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要查看的记录!");
        return false;
    }
    //$.get("temporaryBarcodeDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'临时条码查看/新增',href:'procurement/temporaryBarcodeDetail.jsp?requestNo='+ detail.temporaryBarcode.formNo,url:"temporaryBarcodeDetail/clearSession.do"});
    //});
}
//新增临时条码时，生成默认单号
function generateRequestNo() {
    $.get("temporaryBarcode/generateRequestNo.do",function (result) {
        $("#requestNo").iTextbox("setValue",result);
    });
}
//新增临时条码初始化组件
function init() {
    $("#auditBtn").iMenubutton("disable");
    $("#unauditBtn").iMenubutton("disable");
    $("#boxBar").iMenubutton("disable");
    $("#boxBarPreviewBtn").iMenubutton("disable");

    $("#billDate").iDatebox("setValue",getDate(new Date()));
    $("#inputDate").iTextbox("setValue",getDate(new Date()));
}
//根据临时条码编码查找临时条码信息
function queryTemporaryBarcodeByFormNo(formNo){
    //填充表头信息
    $.get("temporaryBarcode/queryByNo.do",{formNo:formNo},function(result){
        if(result){
            $("#requestNo").iTextbox("setValue",result.formNo);
            $("#billType").iCombobox("setValue",result.billType);
            $("#billDate").iDatebox("setValue",getDate(new Date(result.billDate)));
            if(result.inputDate){
                $("#inputDate").iTextbox("setValue",getDate(new Date(result.inputDate)));
            }else{
                $("#inputDate").iTextbox("setValue",getDate(new Date()));
            }
            $("#inputPersonName").iTextbox("setValue",result.inputPersonName);
            $("#auditor").iTextbox("setValue",result.auditorName);
            $("#_requestNo").val(result.formNo);
            if(result.auditStatus=='已审核'){
                $("#auditDate").iTextbox("setValue",getDate(new Date(result.auditDate)));
                $("#saveBtn").iMenubutton("disable");
                $("#auditBtn").iMenubutton("disable");
                $("#boxBar").iMenubutton("enable");
                $("#boxBarPreviewBtn").iMenubutton("enable");
                $("#deleteLineBtn").iMenubutton("disable");
                $("#unpackingBtn").iMenubutton("disable");
                $("#addLineBtn").iMenubutton("disable");
            }
            if(result.auditStatus=='未审核'){
                $("#auditDate").iTextbox("setValue","");
                $("#unauditBtn").iMenubutton("disable");
                $("#boxBar").iMenubutton("disable");
                $("#auditBtn").iMenubutton("enable");
                $("#saveBtn").iMenubutton("enable");
                $("#boxBarPreviewBtn").iMenubutton("disable");
                $("#deleteLineBtn").iMenubutton("enable");
                $("#unpackingBtn").iMenubutton("enable");
                $("#addLineBtn").iMenubutton("enable");
            }
            //显示可编辑列表
            showTemporaryBarcodes(formNo);
        }
    });
}
/**
 * 临时条码详情中显示临时条码 详情
 * @param requestNo
 */
function showTemporaryBarcodes(requestNo) {
    $('#temporaryBarcodeDetailDg').iEdatagrid({
        pagination:false,
        autoSave:true,
        url: 'temporaryBarcodeDetail/queryByRequestNo.do',
        updateUrl:'temporaryBarcodeDetail/updateSession.do',
        queryParams:{
            requestNo:requestNo
        },onLoadSuccess:function(data){
            billTypeMode();
        },onEndEdit:function(index,row,changes){
            if(changes.amount || changes.amountOfPerBox){
                if(row.amount && row.amountOfPerBox){
                    if(row.amount<0){
                        row.amount = Math.abs(row.amount);
                    }
                    if(row.amountOfPerBox<0){
                        row.amountOfPerBox = Math.abs(row.amountOfPerBox);
                    }
                    if(parseFloat(row.amountOfPerBox)>parseFloat(row.amount)){
                        alert("每箱数大于总数量,请修改！");
                    }
                    row.amountOfBoxes = Math.ceil(row.amount/row.amountOfPerBox);
                }else{
                    if(row.amount){
                        row.amountOfPerBox=row.amount;
                        row.amountOfBoxes =Math.ceil(row.amount/row.amountOfPerBox);
                    }else{
                        row.amount=row.amountOfPerBox;
                        row.amountOfBoxes =Math.ceil(row.amount/row.amountOfPerBox);
                    }

                }
            }
        }
    });
}
//添加行
function addLines() {
    var billType=$("#billType").iCombobox("getValue");
    switch (billType) {
        case "0":{
            $('#showInventoryDialog').dialog("open");
            break;
        }
        case "2":
        case "1":{
            $('#showSearchDialog').dialog("open");
            break;
        }
        default:{
            alert("请选择单据类型!");
        }
    }
}
//删除临时条码详情
function deleteLine() {
    var detail = $('#temporaryBarcodeDetailDg').iEdatagrid('getSelected');
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }
    $.iMessager.confirm('确认','您确认想要删除记录吗？',function(r){
        if (r){
            $.get("temporaryBarcodeDetail/deleteById.do",{id:detail.id},function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $('#temporaryBarcodeDetailDg').iEdatagrid('reload');
                }
            });
        }
    });
}
//新增临时条码
function addTemporaryBarcode(){
    //$.get("temporaryBarcodeDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'临时条码查看/新增',href:'procurement/temporaryBarcodeDetail.jsp',url:"temporaryBarcodeDetail/clearSession.do"});
   // });
}
//保存临时条码(新增和查看)
function saveTemporaryBarcode(){
    $.iMessager.confirm("提示","确认执行保存操作吗？",function(r){
        if(r){
            save();
        }
    });
}

function save(){
    var requestNo = $("#requestNo").iTextbox("getValue");
    if(!requestNo){
        alert("请输入临时条码单号!");
        return false;
    }
    var billDate = $("#billDate").iDatebox("getValue");
    if(!billDate){
        alert("请输入单据日期!");
        return false;
    }

    var billType = $("#billType").iCombobox("getValue");
    if(!billType){
        alert("请选择单据类型!");
        return false;
    }
    $.get("temporaryBarcodeDetail/saveTemporaryBarcode.do",{
        formNo : requestNo,
        billDate : billDate,
        billType:billType
    },function(result){
        if(result.success){
            $.iMessager.alert("提示","保存成功!");
            $("#_requestNo").val(requestNo);
            $("#auditBtn").iMenubutton("enable");
            $("#auditBtn").iMenubutton("disable");
            queryTemporaryBarcodeByFormNo(requestNo);
        }else{
            $.iMessager.alert("提示",result.msg);
        }
    });
}
/**
 * 拆箱
 */
function unpacking() {
    var detailRecord = $('#temporaryBarcodeDetailDg').iEdatagrid("getSelected");
    if(!detailRecord){
        alert("请选择要拆箱的记录!");
        return false;
    }

    if(!detailRecord.amount){
        alert("请输入数量!");
        return false;
    }
    if(!detailRecord.amountOfPerBox){
        alert("请输入每箱数!");
        return false;
    }

    if(parseFloat(detailRecord.amount)<parseFloat(detailRecord.amountOfPerBox)){
        alert("数量小于每箱数!");
        return false;
    }
    if(parseFloat(detailRecord.amount)==parseFloat(detailRecord.amountOfPerBox)){
        alert("数量与每箱数相同，无需拆箱!");
        return false;
    }

    $.get("temporaryBarcodeDetail/unpacking.do",{id:detailRecord.id},function(result){
        $('#temporaryBarcodeDetailDg').iEdatagrid("reload");
    });
}
//临时条码页审核
function auditAtForm() {
    var detail = $("#temporaryBarcodeDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要审核的数据!");
        return false;
    }
    audit(detail.temporaryBarcode.formNo,'form');
}
//临时条码页反审核
function unauditAtForm() {
    $.iMessager.confirm("警告","反审核将删除已有的箱号条码信息，确认该操作吗?",function(r) {
        if (r) {
            var detail = $("#temporaryBarcodeDetailDg").iDatagrid("getSelected");
            if (!detail) {
                alert("请选择要反审核的数据!");
                return false;
            }
            unaudit(detail.temporaryBarcode.formNo, 'form');
        }
    });
}

//临时条码详情页审核
function auditAtFormDetail(){
    var formNo =  $("#requestNo").iTextbox("getValue");
    if(!formNo){
        alert("不存在该临时条码单信息,审核失败!");
        return false;
    }
    audit(formNo,'formDetail');
}
//临时条码详情页反审核
function unauditAtFormDetail(){
    $.iMessager.confirm("警告","反审核将删除已有的箱号条码信息，确认该操作吗?",function(r){
        if(r){
            var formNo =  $("#requestNo").iTextbox("getValue");
            if(!formNo){
                alert("不存在该临时条码单信息,审核失败!");
                return false;
            }
            unaudit(formNo,'formDetail');
        }
    });
}
//反审核
function unaudit(formNo,from) {
    $.get("temporaryBarcode/queryByNo.do",{
        formNo : formNo
    },function (result) {
        if(result==null){
            alert("不存在该临时条码信息,操作失败!");
        }else{
            if(result.auditStatus=='未审核'){
                alert('该临时条码单未被审核，操作无效!');
            }else{
                $.get("temporaryBarcode/unaudit.do",{formNo:formNo},function(result){
                    if(result.statusCode==200){
                        alert("操作完成!");
                        if(from=='formDetail'){
                            queryTemporaryBarcodeByFormNo(formNo);
                        }

                        if(from=='form'){
                            $("#temporaryBarcodeDetailDg").iDatagrid("reload");
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

//审核临时条码
function audit(formNo,from) {
    $.get("temporaryBarcode/queryByNo.do",{
        formNo : formNo
    },function (result) {
        if(result==null){
            alert("不存在该临时条码单信息,审核失败!");
        }else{
            if(result.auditStatus=='已审核'){
                alert('该临时条码单已被审核，无需再次审核!');
            }else{
                $.get("temporaryBarcode/audit.do",{formNo:formNo},function(result){
                    alert("审核完成!");
                    if(from=='formDetail'){
                        queryTemporaryBarcodeByFormNo(formNo);
                        $("#unauditBtn").iMenubutton("enable");
                    }

                    if(from=='form'){
                        $("#temporaryBarcodeDetailDg").iDatagrid("reload");
                        $("#unaudit").iMenubutton("enable");
                    }
                });
            }
        }
    });
}
//删除临时条码及详情
function deleteTemporaryBarcode(){
    var detail = $("#temporaryBarcodeDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }

    $.iMessager.confirm("提示","确认删除?",function(r){
        if(r){
            $.get("temporaryBarcode/deleteByNo.do",{
                formNo : detail.temporaryBarcode.formNo
            },function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    billTypeMode();
                    $("#temporaryBarcodeDetailDg").iDatagrid("reload");
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
    addParentTab({title:'箱号条码',href:'procurement/showTemporaryBarcodeBoxBarCodes.jsp'});
}