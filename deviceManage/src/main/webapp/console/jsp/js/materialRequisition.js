$(function () {
    $("#warehouseName").iTextbox({
        buttonIcon:'fa fa-search',
        onClickButton:function(){
            $('#showWarehousesDialog').dialog("open");
        }
    });
    //弹出选择仓库窗口
    $('#showWarehousesDialog').dialog({
        title: '仓库信息',
        width: 800,
        height: 600,
        closed: true,
        cache: false,
        href: 'procurement/showWarehouses.jsp',
        modal: true,
        buttons:[{
            text:'确定',
            handler:function(){
                var warehouse = $('#warehouseTable').iDatagrid('getSelected');
                confirmWarehouses(warehouse);
            }
        },{
            text:'关闭',
            handler:function(){
                $('#showWarehousesDialog').dialog("close");
            }
        }]
    });
});
//领料仓库确定
function confirmWarehouses(warehouse) {
	if(!warehouse){
        return false;
    }
        $("#warehouseName").iTextbox("setValue",warehouse.cWhCode);
        $("#warehouseName").iTextbox('setText',warehouse.cWhName);
        $("#warehouseCode").val(warehouse.cWhCode);
        $('#showWarehousesDialog').dialog("close");
}
//新增领料单详情时，增加一行
function addLine() {
    var no = $("#no").iTextbox("getValue");
    if(!no){
        alert("请输入工单单号!");
        return false;
    }

   /* var warehouseCode = $("#warehouseCode").val();
    if(!warehouseCode){
        alert("请选择领料仓库!");
        return false;
    }*/
    $.get("workSheet/queryWorkSheetByNo.do",{No:no},function (result) {
        if(!result || result=='null'){
            alert("工单单号不存在!");
            return false;
        }else{
            $('#materialRequisitionDetailDg').iEdatagrid('addRow');
        }
    });
}
//删除领料单详情
function deleteLine() {
    var detail = $('#materialRequisitionDetailDg').iEdatagrid('getSelected');
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }

    $.iMessager.confirm('确认','您确认想要删除该记录吗？',function(r){
        if (r){
            $.get("materialRequisitionDetail/deleteById.do",{id:detail.id},function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $('#materialRequisitionDetailDg').iEdatagrid('reload');
                }
            });
        }
    });
}
//根据条件查询入库申请单详情
function queryMaterialRequisitionDetails(){
    $("#materialRequisitionDg").iDatagrid("reload",{
        from:$("#from").iDatebox("getValue"),
        to:$("#to").iDatebox("getValue"),
        inventoryCode:$("#inventoryCodeSearch").iTextbox("getValue"),
        no:$("#noSearch").iTextbox("getValue"),
        barCode:$("#barCodeSearch").iTextbox("getValue"),
        furnaceNum:$("#furnaceNumSearch").iTextbox("getValue")
    });
}

//根据入库申请单号查找入库申请单详情
function queryMaterialRequisitionDetailsByFormNo() {
    var detail = $("#materialRequisitionDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要查看的记录!");
        return false;
    }
   // $.get("materialRequisitionDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'领料单查看/新增',href:'console/jsp/materialRequisitionDetail.jsp?materialRequisitionFormNo='+ detail.materialRequisition.formNo,url:"materialRequisitionDetail/clearSession.do"});
   // });
}

//新增入库申请单时，生成默认单号
function generateMaterialRequisitionFormNo() {
    $.get("materialRequisition/generateMaterialRequisitionFormNo.do",function (result) {
        $("#formNo").iTextbox("setValue",result);
    });
}

//根据领料单编码查找领料单信息
function queryMaterialRequisitionByFormNo(formNo){
    //填充表头信息
    $.get("materialRequisition/queryByFormNo.do",{formNo:formNo},function(result){
        if(result){
            $("#formNo").iTextbox("setValue",result.formNo);
            $("#pickingDate").iDatebox("setValue",getDate(new Date(result.pickingDate)));
            $("#no").iTextbox("setValue",result.workSheet.no);
            $("#pickerName").iTextbox("setValue",result.pickerName);
            $("#status").iTextbox("setValue",result.status);
            $("#warehouseCode").val(result.warehouseCode);
            $("#warehouseName").iTextbox("setValue",result.warehouseCode);
            $("#warehouseName").iTextbox("setText",result.warehouseName);
            showMaterialRequisitionDetail(formNo);
        }
    });
}
/**
 * 领用单详情中，显示领用单详情
 * @param formNo
 */
function showMaterialRequisitionDetail(formNo) {
    $('#materialRequisitionDetailDg').iEdatagrid({
        pagination:false,
        autoSave:true,
        url: 'materialRequisitionDetail/queryByFormNo.do',
        updateUrl:'materialRequisitionDetail/updateSession.do',
        queryParams:{
            formNo:formNo
        },onEndEdit:function(index,row,changes){
            if(changes.barCode){
                $.get("materialRequisitionDetail/queryByBarCodeAndNo.do",{barCode:changes.barCode,no:$("#no").iTextbox("getValue")},
                    function(ret){
                    if(!ret.success){
                        alert(ret.msg);
                        //$('#materialRequisitionDetailDg').iEdatagrid("editRow",index);
                    }else{
                        var result = ret.detail;
                        row.inventoryCode = result.inventoryCode;
                        row.inventoryName = result.inventoryName;
                        row.specificationType = result.specificationType;
                        row.amount = result.amount;
                        row.batchNumber = result.batchNumber;
                        row.furnaceNumber = result.furnaceNumber;
                        row.boxNum = result.boxNum;
                        row.amountOfBoxes = result.amountOfBoxes;
                        row.id = result.id;
                        row.positionCode=result.positionCode;
                        $('#materialRequisitionDetailDg').iEdatagrid("refreshRow",index);
                    }
                });
            }
        }
    });
}

//领料单
function addMateriaRequisition(){
   // $.get("materialRequisitionDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'领料单查看/新增',href:'console/jsp/materialRequisitionDetail.jsp',url:"materialRequisitionDetail/clearSession.do"});
    //});
}

//删除领料单及详情
function deleteMateriaRequisition(){
    var detail = $("#materialRequisitionDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }

    $.iMessager.confirm("提示","确认删除?",function(r){
        if(r){
            $.get("materialRequisition/deleteByFormNo.do",{
                formNo : detail.materialRequisition.formNo
            },function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $("#materialRequisitionDg").iDatagrid("reload");
                }

                if(result.statusCode==300){
                    $.iMessager.alert("警告",result.message,"messager-error");
                }
            });
        }
    })
}
//保存领料单(新增和查看)
function saveMaterialRequisition(){
    $.iMessager.confirm("提示","确认执行保存操作吗？",function(r){
        if(r){
            save();
        }
    });
}

function save(){
    var requestNo = $("#formNo").iTextbox("getValue");
    if(!requestNo){
        alert("请输入领料单号!");
        return false;
    }
    var pickingDate = $("#pickingDate").iDatebox("getValue");
    if(!pickingDate){
        alert("请输入领料日期!");
        return false;
    }
    var no = $("#no").iTextbox("getValue");
    if(!no){
        alert("请输入工单单号!");
        return false;
    }
    var warehouseCode = $("#warehouseCode").val();
    if(!warehouseCode){
        alert("请选择仓库!");
        return false;
    }
    $.get("materialRequisition/saveMaterialRequisition.do",{
        formNo : requestNo,
        pickingDate : pickingDate,
        "workSheet.no" : no,
        warehouseCode:warehouseCode,
        warehouseName:$("#warehouseName").iTextbox("getText")
    },function(result){
        if(result.success){
            $.iMessager.alert("提示","保存成功!");
            $("#_requestNo").val(requestNo);
            $("#erpMaterialRequisition").iMenubutton("enable");
            queryMaterialRequisitionByFormNo(requestNo);
        }else{
            $.iMessager.alert("提示",result.msg);
        }
    });
}

/***
 * 生成ERP领料单
 */
function generateERPMaterialRequisition() {
    var formNo = $("#formNo").iTextbox("getValue");
    $.get("materialRequisition/queryByFormNo.do",{formNo:formNo},function (result) {
        if(!result){
            alert("请先保存该领料单!");
        }else{
            if(result.status=='已生成'){
                alert("已生成过ERP领料单，无需重复操作!");
            }else{
                $.get("materialRequisition/generateERPMaterialRequisition.do",{
                    no:$("#no").iTextbox("getValue"),
                    formNo:formNo
                },function (result) {
                    alert(result.msg);
                });
            }
        }
    });
}

