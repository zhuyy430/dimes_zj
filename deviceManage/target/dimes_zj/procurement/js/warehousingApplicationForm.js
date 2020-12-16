$(function(){
    $("#receivingDate").iDatebox("setValue",getDate(new Date()));
    $("#warehouseName").iTextbox({
        buttonIcon:'fa fa-search',
        onClickButton:function(){
            $('#showWarehouses4DetailDialog').dialog("open");
        }
    });
    $("#vendor").iTextbox({
        buttonIcon:'fa fa-search',
        onClickButton:function(){
            $('#showVendorsDialog').dialog({title:'供应商信息'});
            $('#showVendorsDialog').dialog("open");
        }
    });
    //弹出选择供应商
    $('#showVendorsDialog').dialog({
        title: '供应商信息',
        width: 800,
        height: 600,
        closed: true,
        cache: false,
        href: 'procurement/showVendors.jsp',
        modal: true,
        buttons:[{
            text:'确定',
            handler:function(){
                var vendor = $('#vendorTable').iDatagrid('getSelected');
                confirm(vendor);
            }
        },{
            text:'关闭',
            handler:function(){
                $('#showVendorsDialog').dialog("close");
            }
        }]
    });
    //弹出选择仓库窗口
    $('#showWarehouses4DetailDialog').dialog({
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
                $('#showWarehouses4DetailDialog').dialog("close");
            }
        }]
    });
});
//供应商确认
function confirm(vendor){
	$("#vendorCode").val(vendor.cVenCode);
    $("#vendor").iTextbox('setValue',vendor.cVenName);
    $('#showVendorsDialog').dialog("close");
}
//仓库确认
function confirmWarehouses(warehouse){
	if(!warehouse){
        return false;
    }
    $.iMessager.confirm("警告","更改入库仓库将影响表体入库仓库，是否修改?",function (r) {
        if(r){
            $.get("warehousingApplicationFormDetail/modifyWarehouseInSession.do",{
                warehouseCode : warehouse.cWhCode,
                warehouseName : warehouse.cWhName
            },function (result) {
                $('#warehousingApplicationFormDetailDg').iEdatagrid("load");
            });
        }
        $("#warehouseCode").val(warehouse.cWhCode);
        $("#warehouseName").iTextbox('setValue',warehouse.cWhName);
        $('#showWarehouses4DetailDialog').dialog("close");
    });
}
//根据条件查询入库申请单详情
function queryWarehousingApplicationFormDetails(){
    $("#warehousingApplicationFormDetailDg").iDatagrid("reload",{
        from:$("#from").val(),
        to:$("#to").val(),
        vendor:$("#vendor").val(),
        requestNo:$("#requestNo").val(),
        purchasingOrderNo:$("#purchasingOrderNo").val(),
        warehouse:$("#warehouse").val(),
        furnaceNumber:$("#furnaceNumber").val()
    })
}
//根据入库申请单号查找入库申请单详情
function queryWarehousingApplicationFormDetailsByFormNo() {
    var detail = $("#warehousingApplicationFormDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要查看的记录!");
        return false;
    }
    //$.get("warehousingApplicationFormDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'入库申请单查看/新增',href:'procurement/warehousingApplicationFormDetail.jsp?requestNo='+ detail.warehousingApplicationForm.formNo,url:"warehousingApplicationFormDetail/clearSession.do"});
    //});
}

//根据入库申请单号查找入库申请单详情
function queryWarehousingApplicationFormDetailsByFormNoIsOut() {
    var detail = $("#warehousingApplicationFormDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要查看的记录!");
        return false;
    }
    //$.get("warehousingApplicationFormDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'入库申请单查看/新增',href:'procurement/outsourcingWarehousingApplicationFormDetail.jsp?requestNo='+ detail.warehousingApplicationForm.formNo,url:"warehousingApplicationFormDetail/clearSession.do"});
    //});
}
//新增入库申请单时，生成默认单号
function generateRequestNo() {
    $.get("warehousingApplicationForm/generateRequestNo.do",function (result) {
        $("#requestNo").iTextbox("setValue",result);
    });
}
//获取供应商
function getVendor() {
    $.get("vendor/queryVendor.do",function (result) {
        if(result){
            $("#vendor1").iTextbox("setValue",result.cVenName);
            $("#vendor").iTextbox("setValue",result.cVenName);
            $("#vendorCode").val(result.cVenCode);
            $('#vendor').iTextbox('readonly',true); // 启用只读模式
        }else{
            $('#vendor').iTextbox('readonly',false);
        }
    });
}
//新增入库申请单初始化组件
function init() {
    $("#auditBtn").iMenubutton("disable");
    $("#unauditBtn").iMenubutton("disable");
    $("#boxBar").iMenubutton("disable");
    $("#boxBarPreviewBtn").iMenubutton("disable");
    $("#inWarehouseBtn").iMenubutton("disable");
}
//根据入库申请单编码查找入库申请单信息
function queryWarehousingApplicationFormByFormNo(formNo){
    //填充表头信息
    $.get("warehousingApplicationForm/queryByFormNo.do",{formNo:formNo},function(result){
        if(result){
            $("#vendorCode").val(result.vendorCode);
            $("#_vendorCode").val(result.vendorCode);
            $("#vendor").iTextbox("setValue",result.vendorName);
            $("#requestNo").iTextbox("setValue",result.formNo);
            $("#auditStatus").iTextbox("setValue",result.auditStatus);
            $("#warehouseCode").val(result.warehouseCode);
            $("#warehouseName").iTextbox("setValue",result.warehouseName);
            $("#receivingDate").iDatebox("setValue",getDate(new Date(result.receivingDate)));
            $("#applierName").iTextbox("setValue",result.applierName);
            $("#auditor").iTextbox("setValue",result.auditorName);
            $("#cPersonCode").val(result.cPersonCode);
            $("#cDepCode").val(result.cDepCode);
            $("#_requestNo").val(result.formNo);
            if(result.warehousingStatus=='1'){
                $("#warehousingStatus").iTextbox("setValue","1");
                $("#warehousingStatus").iTextbox("setText","已入库");
            }else{
                $("#warehousingStatus").iTextbox("setValue","0");
                $("#warehousingStatus").iTextbox("setText","未入库");
            }
            if(result.auditStatus=='已审核'){
                $("#auditDate").iTextbox("setValue",getDate(new Date(result.auditDate)));
                $("#saveBtn").iMenubutton("disable");
                $("#auditBtn").iMenubutton("disable");
                $("#unauditBtn").iMenubutton("enable");
                $("#boxBar").iMenubutton("enable");
                $("#boxBarPreviewBtn").iMenubutton("enable");

                if(result.warehousingStatus=='1'){
                    $("#inWarehouseBtn").iMenubutton("disable");
                }else{
                    $("#inWarehouseBtn").iMenubutton("enable");
                }
                $("#addLineBtn").iMenubutton("disable");
                $("#deleteLineBtn").iMenubutton("disable");
                $("#unpackingBtn").iMenubutton("disable");
            }
            if(result.auditStatus=='未审核'){
                $("#auditDate").iTextbox("setValue","");
                $("#unauditBtn").iMenubutton("disable");
                $("#boxBar").iMenubutton("disable");
                $("#auditBtn").iMenubutton("enable");
                $("#saveBtn").iMenubutton("enable");
                $("#boxBarPreviewBtn").iMenubutton("disable");
                $("#inWarehouseBtn").iMenubutton("disable");
                $("#addLineBtn").iMenubutton("enable");
                $("#deleteLineBtn").iMenubutton("enable");
                $("#unpackingBtn").iMenubutton("enable");
            }
            //显示可编辑列表
            showWarehousingApplicationForms(formNo);
        }
    });
}
/**
 * 入库申请单详情中显示入库申请单 详情
 * @param requestNo
 */
function showWarehousingApplicationForms(requestNo) {
    $('#warehousingApplicationFormDetailDg').iEdatagrid({
        pagination:false,
        autoSave:true,
        url: 'warehousingApplicationFormDetail/queryByRequestNo.do',
        updateUrl:'warehousingApplicationFormDetail/updateSession.do',
        queryParams:{
            requestNo:requestNo
        },onEdit:function(index,row){
            var ed = $('#warehousingApplicationFormDetailDg').iEdatagrid('getEditor', { index: index, field: 'warehouseCode' }); // get the editor
            ed.target.css("height","90%");

            // ed.target.parent().attr("title","搜索仓库");
            $(ed.target).iTextbox({
                buttonIcon:'fa fa-search',
                onClickButton:function(){
                    $('#showDetailDialog').dialog({
                        title: '仓库信息',
                        width: 800,
                        height: 600,
                        closed: false,
                        cache: false,
                        href: 'procurement/showWarehouses2.jsp',
                        modal: true,
                        buttons:[{
                            text:'确定',
                            handler:function(){
                                var warehouse = $('#warehouseTable2').iDatagrid('getSelected');
                                if(!warehouse){
                                    return false;
                                }
                                row.warehouseCode=warehouse.cWhCode;
                                row.warehouseName=warehouse.cWhName;
                                $('#warehousingApplicationFormDetailDg').iEdatagrid("refreshRow",index);
                                $.get("warehousingApplicationFormDetail/updateSession.do",row,function(data){
                                    $('#showDetailDialog').dialog("close");
                                    $('#warehousingApplicationFormDetailDg').iEdatagrid("editRow",index);
                                })
                            }
                        },{
                            text:'关闭',
                            handler:function(){
                                $('#showDetailDialog').dialog("close");
                            }
                        }]
                    });
                }
            });
            var locationCodeEditor = $('#warehousingApplicationFormDetailDg').iEdatagrid('getEditor', { index: index, field: 'locationCode' }); // get the editor
            locationCodeEditor.target.css("height","90%");
            $(locationCodeEditor.target).iTextbox({
                buttonIcon:'fa fa-search',
                onClickButton:function(){
                    var warehouseCode = row.warehouseCode;
                    if(!warehouseCode){
                        alert("请选择仓库信息!");
                        return false;
                    }
                    $('#showLocationDialog').dialog({
                        title: '货位信息',
                        width: 800,
                        height: 600,
                        closed: false,
                        cache: false,
                        href: 'procurement/showLocation.jsp?cWhCode=' + row.warehouseCode,
                        modal: true,
                        buttons:[{
                            text:'确定',
                            handler:function(){
                                var location = $('#locationTable').iDatagrid('getSelected');
                                if(!location){
                                    return false;
                                }
                                row.locationCode=location.cPosCode;
                                $('#warehousingApplicationFormDetailDg').iEdatagrid("refreshRow",index);
                                $.get("warehousingApplicationFormDetail/updateSession.do",row,function(data){
                                    $('#showLocationDialog').dialog("close");
                                    $('#warehousingApplicationFormDetailDg').iEdatagrid("editRow",index);
                                })
                            }
                        },{
                            text:'关闭',
                            handler:function(){
                                $('#showLocationDialog').dialog("close");
                            }
                        }]
                    });
                }
            });
        },onEndEdit:function(index,row,changes){
            if(changes.amount || changes.amountOfBoxes){
                if(row.amount && row.amountOfBoxes){
                    if(row.amount<0){
                        row.amount = Math.abs(row.amount);
                    }
                    if(row.amountOfBoxes<0){
                        row.amountOfBoxes = Math.abs(row.amountOfBoxes);
                    }
                    /*if(parseFloat(row.amountOfPerBox)>parseFloat(row.amount)){
                        alert("每箱数大于总数量,请修改！");
                    }*/
                    row.amountOfPerBox = Math.floor(row.amount/row.amountOfBoxes* 100)/ 100;
                }else{
                    row.amountOfPerBox = -1;
                }
            }
            //修改仓库编码
            if(changes.warehouseCode){
                //根据仓库编码查找仓库信息
                $.get("warehouseController/queryByWarehouseCode.do",{warehouseCode : changes.warehouseCode},function (result) {
                    row.warehouseName = result.cWhName;
                    $('#warehousingApplicationFormDetailDg').iEdatagrid("refreshRow",index);
                });
            }
        }
    });
}
//添加行
function addLines() {
    var vendor = $("#vendorCode").val();
    if(!vendor){
        alert("请选择供应商!");
        return false;
    }
    //添加行，弹出查找物料信息框
    $('#showPodetailDialog').dialog({
        title: '采购单信息',
        width: 1000,
        height: 600,
        closed: false,
        cache: false,
        href: 'procurement/showPodetails.jsp',
        modal: true,
        queryParams: { cVenCode: $("#vendorCode").val() },
        buttons:[{
            text:'确定',
            handler:function(){
                //获取选中的采购订单详情id
                var podetails = $("#podetailTable").iDatagrid("getSelections");
                //没有选择采购订单详情
                if(!podetails||podetails.length==0){
                    return false;
                }
                var wafd=$('#warehousingApplicationFormDetailDg').iDatagrid('getRows');
                var cpoId;
                if(wafd!=null&&wafd.length>0){
                    cpoId=wafd[0].purchasingNo
                }else{
                    cpoId=podetails[0].cpoId;
                }
                if(podetails.length>0){
                    var p = podetails[0];
                    $("#cPersonCode").val(p.po_pomain.cPersonCode);
                    $("#cDepCode").val(p.po_pomain.cDepCode);
                }
                var ids = new Array();
                for(var i = 0;i<podetails.length;i++){
                    if(podetails[i].cpoId==cpoId){
                        var podetail = podetails[i];
                        ids.push(podetail.id);
                    }else{

                        alert("订单号不同，请重新选择!")
                        return false;
                    }

                }
                $.get("PO_Podetails/addPodetails2Session.do",{
                    ids:JSON.stringify(ids),
                    warehouseCode:$("#warehouseCode").val(),
                    warehouseName:$("#warehouseName").val()
                },function (result) {
                    //刷新入库申请单详情数据
                    $('#warehousingApplicationFormDetailDg').iEdatagrid("reload");
                    $('#showPodetailDialog').dialog("close");
                });
            }
        },{
            text:'关闭',
            handler:function(){
                $('#showPodetailDialog').dialog("close");
            }
        }]
    });
}
//删除入库申请单详情
function deleteLine() {
    var detail = $('#warehousingApplicationFormDetailDg').iEdatagrid('getSelected');
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }

    $.iMessager.confirm('确认','您确认想要删除记录吗？',function(r){
        if (r){
            $.get("warehousingApplicationFormDetail/deleteById.do",{id:detail.id},function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $('#warehousingApplicationFormDetailDg').iEdatagrid('reload');
                }
            });
        }
    });
}
//新增入库申请单
function addWarehousingApplicationForm(){
   // $.get("warehousingApplicationFormDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'入库申请单查看/新增',href:'procurement/warehousingApplicationFormDetail.jsp',url:'warehousingApplicationFormDetail/clearSession.do'});
   // });
}
function addWarehousingApplicationFormIsOut(){
   // $.get("warehousingApplicationFormDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'入库申请单查看/新增',href:'procurement/outsourcingWarehousingApplicationFormDetail.jsp',url:'warehousingApplicationFormDetail/clearSession.do'});
   // });
}
//保存入库申请单(新增和查看)
function saveWarehousingApplicationForm(){
    $.iMessager.confirm("提示","确认执行保存操作吗？",function(r){
        if(r){
            save();
        }
    });
}

function save(){
    requestNo = $("#requestNo").iTextbox("getValue");
    if(!requestNo){
        alert("请输入申请单号!");
        return false;
    }
    var vendorCode = $("#vendorCode").val();
    var vendorName = $("#vendor").iTextbox("getValue");
    if(!vendorCode || !vendorName){
        alert("请选择供应商!");
        return false;
    }
    var receivingDate = $("#receivingDate").iDatebox("getValue");
    if(!receivingDate){
        alert("请输入收料日期!");
        return false;
    }
    var vendorName = $('#vendor').iTextbox("getValue");
    var warehouseCode = $("#warehouseCode").val();
    var warehouseName = $("#warehouseName").iTextbox("getValue");
    $.get("warehousingApplicationFormDetail/saveWarehousingApplicationForm.do",{
        formNo : requestNo,
        receivingDate : receivingDate,
        vendorCode : vendorCode,
        vendorName : vendorName,
        warehouseCode : warehouseCode,
        warehouseName : warehouseName,
        cPersonCode:$("#cPersonCode").val(),
        cDepCode:$("#cDepCode").val()
    },function(result){
        if(result.success){
            $.iMessager.alert("提示","保存成功!");
            $("#_requestNo").val(requestNo);
            $("#auditBtn").iMenubutton("enable");
            $("#auditBtn").iMenubutton("disable");
            queryWarehousingApplicationFormByFormNo(requestNo);
        }else{
            $.iMessager.alert("提示",result.msg);
        }
    });
}
/**
 * 拆箱
 */
function unpacking() {
    var detailRecord = $('#warehousingApplicationFormDetailDg').iEdatagrid("getSelected");
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

    $.get("warehousingApplicationFormDetail/unpacking.do",{id:detailRecord.id},function(result){
        $('#warehousingApplicationFormDetailDg').iEdatagrid("reload");
    });
}
//入库申请单页审核
function auditAtForm() {
    var detail = $("#warehousingApplicationFormDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要审核的数据!");
        return false;
    }
    audit(detail.warehousingApplicationForm.formNo,'form');
}
//入库申请单页反审核
function unauditAtForm() {
    $.iMessager.confirm("警告","反审核将删除已有的箱号条码信息，确认该操作吗?",function(r) {
        if (r) {
            var detail = $("#warehousingApplicationFormDetailDg").iDatagrid("getSelected");
            if (!detail) {
                alert("请选择要反审核的数据!");
                return false;
            }
            unaudit(detail.warehousingApplicationForm.formNo, 'form');
        }
    });
}

//入库申请单详情页审核
function auditAtFormDetail(){
    var formNo =  $("#requestNo").iTextbox("getValue");
    if(!formNo){
        alert("不存在该申请单信息,审核失败!");
        return false;
    }
    audit(formNo,'formDetail');
}
//入库申请单详情页反审核
function unauditAtFormDetail(){
    $.iMessager.confirm("警告","反审核将删除已有的箱号条码信息，确认该操作吗?",function(r){
        if(r){
            var formNo =  $("#requestNo").iTextbox("getValue");
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
    $.get("warehousingApplicationForm/queryByFormNo.do",{
        formNo : formNo
    },function (result) {
        if(result==null){
            alert("不存在该申请单信息,操作失败!");
        }else{
            if(result.auditStatus=='未审核'){
                alert('该申请单未被审核，操作无效!');
            }else{
                $.get("warehousingApplicationForm/unaudit.do",{formNo:formNo},function(result){
                    if(result.statusCode==200){
                        alert("操作完成!");
                        if(from=='formDetail'){
                            queryWarehousingApplicationFormByFormNo(formNo);
                        }

                        if(from=='form'){
                            $("#warehousingApplicationFormDetailDg").iDatagrid("reload");
                        }
                    }else{
                        alert(result.message);
                    }
                });
            }
        }
    });
}

//审核入库申请单
function audit(formNo,from) {
    $.get("warehousingApplicationForm/queryByFormNo.do",{
        formNo : formNo
    },function (result) {
        if(result==null){
            alert("不存在该申请单信息,审核失败!");
        }else{
            if(result.auditStatus=='已审核'){
                alert('该申请单已被审核，无需再次审核!');
            }else{
                $.get("warehousingApplicationForm/audit.do",{formNo:formNo},function(result){
                    alert("审核完成!");
                    if(from=='formDetail'){
                        queryWarehousingApplicationFormByFormNo(formNo);
                    }

                    if(from=='form'){
                        $("#warehousingApplicationFormDetailDg").iDatagrid("reload");
                    }
                });
            }
        }
    });
}
//删除入库申请单及详情
function deleteWarehousingApplicationForm(){
    var detail = $("#warehousingApplicationFormDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }

    $.iMessager.confirm("提示","确认删除?",function(r){
        if(r){
            $.get("warehousingApplicationForm/deleteByFormNo.do",{
                formNo : detail.warehousingApplicationForm.formNo
            },function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $("#warehousingApplicationFormDetailDg").iDatagrid("reload");
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
    addParentTab({title:'箱号条码',href:'procurement/showBoxBarCodes.jsp'});
}