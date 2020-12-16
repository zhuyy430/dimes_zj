//新增检验单页面，"新增"按钮点击事件
//关闭当前tab页，打开一个新的tab页
function addNew() {
    //清空session
    $.get("inspectionRecordDetail/clearSession.do",function(result){
        location.replace("console/jsp/inspectionRecordDetail.jsp");
    })
}
//根据条件查询入库申请单详情
function queryInspectionRecordDetails(){
    $("#inspectionRecordDg").iDatagrid("reload",{
        from:$("#from").iDatebox("getValue"),
        to:$("#to").iDatebox("getValue"),
        inventoryCode:$("#inventoryCode").val(),
        no:$("#noSearch").iTextbox("getValue"),
        productionUnitCode:$("#productionUnitCodeSearch").val(),
        inspectionResult:$("#inspectionResultSearch").iCombobox("getValue"),
        classCode:$("#classCode").iCombobox("getValue"),
        processName:$("#processNameSearch").iTextbox("getValue"),
        batchNumber:$("#batchNumberSearch").iTextbox("getValue")
    });
}
//根据入库申请单号查找入库申请单详情
function queryInspectionRecordDetailsByFormNo() {
    var inspectionRecord = $("#inspectionRecordDg").iDatagrid("getSelected");
    if(!inspectionRecord){
        alert("请选择要查看的记录!");
        return false;
    }
   // $.get("inspectionRecordDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'检验单查看/新增',href:'console/jsp/inspectionRecordDetail.jsp?inspectionRecordFormNo='+ inspectionRecord.formNo,url:"inspectionRecordDetail/clearSession.do"});
   // });
}
//新增入库申请单时，生成默认单号
function generateInspectionRecordFormNo() {
    $.get("inspectionRecord/generateInspectionRecordFormNo.do",function (result) {
        $("#formNo").iTextbox("setValue",result);
    });
}
//根据检验单编码查找检验单信息
function queryInspectionRecordByFormNo(formNo){
    //填充表头信息
    $.get("inspectionRecord/queryByFormNo.do",{formNo:formNo},function(result){
        if(result){
            $("#formNo").iTextbox("setValue",result.formNo);
            $("#inspectionDate").iDatebox("setValue",getDate(new Date(result.inspectionDate)));
            $("#no").iTextbox("setValue",result.no);
            $("#inspectorName").iTextbox("setValue",result.inspectorName);
            $("#productionUnitCode").val(result.productionUnitCode);
            $("#productionUnitName").iTextbox("setValue",result.productionUnitName);
            $("#inventoryCode").iTextbox("setValue",result.inventoryCode);
            $("#inventoryName").iTextbox("setValue",result.inventoryName);
            $("#batchNumber").iTextbox("setValue",result.batchNumber);
            $("#specificationType").iTextbox("setValue",result.specificationType);
            $("#furnaceNumber").iTextbox("setValue",result.furnaceNumber);
            $("#processCode").iCombobox("setValue",result.processCode);
            $("#processCode").iCombobox("setText",result.processName);
            $("#processName").val(result.processName);
            $("#classCode").iCombobox("setValue",result.classCode);
            $("#className").val(result.className);
            $("#inspectionType").iCombobox("setValue",result.inspectionType);
            $("#inspectionType").iCombobox("setText",result.inspectionType);
            showInspectionRecordDetail(formNo);
        }
    });
}
/**
 * 检验单详情中，显示检验单详情
 * @param formNo
 */
function showInspectionRecordDetail(formNo) {
    $('#inspectionRecordDetailDg').iEdatagrid({
        pagination:false,
        autoSave:true,
        url: 'inspectionRecordDetail/queryByFormNo.do',
        updateUrl:'inspectionRecordDetail/updateSession.do',
        queryParams:{
            formNo:formNo
        },onEndEdit:function(index,row,changes){
            if(row.parameterValue){
                var pv = parseFloat(row.parameterValue);
                if(pv>=row.lowLine && pv<=row.upLine){
                    row.inspectionResult = "OK";
                }else{
                    row.inspectionResult = "NG";
                }
            }
        }
    });
}
//检验单
function addInspectionRecord(){
   // $.get("inspectionRecordDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'检验单查看/新增',href:'console/jsp/inspectionRecordDetail.jsp',url:"inspectionRecordDetail/clearSession.do"});
    //});
}
//删除检验单及详情
function deleteInspectionRecord(){
    var inspectionRecord = $("#inspectionRecordDg").iDatagrid("getSelected");
    if(!inspectionRecord){
        alert("请选择要删除的数据!");
        return false;
    }
    $.iMessager.confirm("提示","确认删除?",function(r){
        if(r){
            $.get("inspectionRecord/deleteByFormNo.do",{
                formNo : inspectionRecord.formNo
            },function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $("#inspectionRecordDg").iDatagrid("reload");
                }

                if(result.statusCode==300){
                    $.iMessager.alert("警告",result.message,"messager-error");
                }
            });
        }
    })
}
//保存检验单(新增和查看)
function saveInspectionRecord(){
    $.iMessager.confirm("提示","确认执行保存操作吗？",function(r){
        if(r){
            save();
        }
    });
}
function save(){
    var requestNo = $("#formNo").iTextbox("getValue");
    if(!requestNo){
        alert("请输入检验单号!");
        return false;
    }
    var inspectionDate = $("#inspectionDate").iDatebox("getValue");
    if(!inspectionDate){
        alert("请输入检验日期!");
        return false;
    }
    var no = $("#no").iTextbox("getValue");
    if(!no){
        alert("请输入工单单号!");
        return false;
    }
    $.get("inspectionRecord/saveInspectionRecord.do",$("#ff").serialize()/*{
        formNo : requestNo,
        inspectionDate : inspectionDate,
        "workSheet.no" : no
    }*/,function(result){
        if(result.success){
            $.iMessager.alert("提示","保存成功!");
            queryInspectionRecordByFormNo(requestNo);
        }else{
            $.iMessager.alert("提示",result.msg);
        }
    });
}
// 填充新增过程检验记录头部信息
function fillWorkSheet(workSheet){
    $("#no").iTextbox("setValue",workSheet.no);
    $("#productionUnitCode").val(workSheet.productionUnitCode);
    $("#productionUnitName").iTextbox("setValue",workSheet.productionUnitName);
    $("#inventoryCode").iTextbox("setValue",workSheet.workPieceCode);
    $("#inventoryName").iTextbox("setValue",workSheet.workPieceName);
    $("#batchNumber").iTextbox("setValue",workSheet.batchNumber);
    $("#specificationType").iTextbox("setValue",workSheet.unitType);
    $("#furnaceNumber").iTextbox("setValue",workSheet.stoveNumber);
}

