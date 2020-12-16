//根据条件查询发货单详情
function queryDeliveryPlanDetails(){
    $("#deliveryPlanDetailDg").iDatagrid("reload",{
        from:$("#from").val(),
        to:$("#to").val(),
        customerCode:$("#customerCodeSearch").val(),
        formNo:$("#formNoSearch").val(),
        inventoryCode:$("#inventoryCodeSearch").val(),
        batchNumber:$("#batchNumberSearch").val(),
        status:$("#statusSearch").iCombobox("getValue")
    });
}
//根据发货单号查找发货单详情
function queryDeliveryPlanDetailsByFormNo() {
    var detail = $("#deliveryPlanDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要查看的记录!");
        return false;
    }
    //$.get("deliveryPlanDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'发货单查看/新增',href:'console/jsp/deliveryPlanDetail.jsp?formNo='+ detail.deliveryPlan.formNo,url:"deliveryPlanDetail/clearSession.do"});
   // });
}
//新增发货单时，生成默认单号
function generateRequestNo() {
    $.get("deliveryPlan/generateRequestNo.do",function (result) {
        $("#formNo").iTextbox("setValue",result);
    });
}
//新增发货单初始化组件
function init() {
    $("#auditBtn").iMenubutton("disable");
    $("#unauditBtn").iMenubutton("disable");
    $("#boxBar").iMenubutton("disable");
    $("#outWarehouseBtn").iMenubutton("disable");
}
//根据发货单编码查找发货单信息
function queryDeliveryPlanByFormNo(formNo){
    //填充表头信息
    $.get("deliveryPlan/queryByFormNo.do",{formNo:formNo},function(result){
        if(result){
            $("#formNo").iTextbox("setValue",result.formNo);
            $("#deliveryPlanDate").iDatebox("setValue",getDate(new Date(result.deliveryPlanDate)));
            $("#productionUnit").iCombotree("setValue",result.productionUnitName);
            $("#productionUnitName").val(result.productionUnitName);
            $("#productionUnitCode").val(result.productionUnitCode);
            $("#jobBookerName").iTextbox("setValue",result.jobBookerName);
            $("#auditor").iTextbox("setValue",result.auditorName);
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
                $("#boxBar").iMenubutton("disable");
                $("#auditBtn").iMenubutton("enable");
                $("#saveBtn").iMenubutton("enable");
                $("#boxBarPreviewBtn").iMenubutton("disable");
                $("#inWarehouseBtn").iMenubutton("disable");
            }
            //显示可编辑列表
            showDeliveryPlans(formNo);
        }
    });
}
/**
 * 发货单详情中显示发货单 详情
 * @param requestNo
 */
function showDeliveryPlans(requestNo) {
    $('#deliveryPlanDetailDg').iEdatagrid({
        pagination:false,
        autoSave:true,
        url: 'deliveryPlanDetail/queryByFormNo.do',
        updateUrl:'deliveryPlanDetail/updateSession.do',
        queryParams:{
            formNo:requestNo
        }
    });
}
//添加行
function addLines() {
    //添加行，弹出查找销售订单
    $('#showSalesSlipsDialog').dialog({
        title: '销售订单信息',
        width: 1200,
        height: 600,
        closed: false,
        cache: false,
        href: 'console/jsp/showSalesSlips.jsp',
        modal: true,
        buttons:[{
            text:'确定',
            handler:function(){
                //获取选中的销售订单
                var salesSlips = $("#salesSlipTable").iDatagrid("getSelections");
                //没有选择销售订单
                if(!salesSlips){
                    return false;
                }
                var ids = new Array();
                for(var i = 0;i<salesSlips.length;i++){
                    var salesSlip = salesSlips[i];
                    ids.push(salesSlip.autoId);
                }
                $.get("deliveryPlanDetail/addDeliveryPlanDetails2Session.do",{
                    ids:JSON.stringify(ids)
                },function (result) {
                    //刷新发货单详情数据
                    $('#deliveryPlanDetailDg').iEdatagrid("reload");
                    $('#showSalesSlipsDialog').dialog("close");
                });
            }
        },{
            text:'关闭',
            handler:function(){
                $('#showSalesSlipsDialog').dialog("close");
            }
        }]
    });
}
//删除发货单详情
function deleteLine() {
    var detail = $('#deliveryPlanDetailDg').iEdatagrid('getSelected');
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }

    $.iMessager.confirm('确认','您确认想要删除记录吗？',function(r){
        if (r){
            $.get("deliveryPlanDetail/deleteById.do",{id:detail.id},function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $('#deliveryPlanDetailDg').iEdatagrid('reload');
                }
            });
        }
    });
}
//新增发货单
function addDeliveryPlan(){
    //$.get("deliveryPlanDetail/clearSession.do",function(result){
        addParentTabWithTip({title:'发货单查看/新增',href:'console/jsp/deliveryPlanDetail.jsp',url:"deliveryPlanDetail/clearSession.do"});
   // });
}
//保存发货单(新增和查看)
function saveDeliveryPlan(){
    $.iMessager.confirm("提示","确认执行保存操作吗？",function(r){
        if(r){
            save();
        }
    });
}

function save(){
    var formNo = $("#formNo").iTextbox("getValue");
    if(!formNo){
        alert("请输入发货单号!");
        return false;
    }
    var deliverDate = $("#deliverDate").iDatebox("getValue");
    if(!deliverDate){
        alert("请输入发货日期!");
        return false;
    }
    $.get("deliveryPlanDetail/saveDeliveryPlan.do",{
        formNo : formNo,
        deliverDate : deliverDate
    },function(result){
        if(result.success){
            $.iMessager.alert("提示","保存成功!");
            $("#_requestNo").val(formNo);
            $("#auditBtn").iMenubutton("enable");
            $("#auditBtn").iMenubutton("disable");
            queryDeliveryPlanByFormNo(formNo);
        }else{
            $.iMessager.alert("提示",result.msg);
        }
    });
}
/**
 * 拆箱
 */
function splitLine() {
    var detailRecord = $('#deliveryPlanDetailDg').iEdatagrid("getSelected");
    if(!detailRecord){
        alert("请选择要拆分的行!");
        return false;
    }
    if(!detailRecord.amountOfPlan){
        alert("请输入发货数量!");
        return false;
    }
    $.get("deliveryPlanDetail/splitLine.do",{id:detailRecord.id},function(result){
        $('#deliveryPlanDetailDg').iEdatagrid("reload");
    });
}
//发货单页审核
function auditAtForm() {
    var detail = $("#deliveryPlanDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要审核的数据!");
        return false;
    }
    audit(detail.deliveryPlan.formNo,'form');
}
//发货单页反审核
function unauditAtForm() {
    $.iMessager.confirm("警告","确认该操作吗?",function(r) {
        if (r) {
            var detail = $("#deliveryPlanDetailDg").iDatagrid("getSelected");
            if (!detail) {
                alert("请选择要反审核的数据!");
                return false;
            }
            unaudit(detail.deliveryPlan.formNo, 'form');
        }
    });
}

//发货单详情页审核
function auditAtFormDetail(){
    var formNo =  $("#formNo").iTextbox("getValue");
    if(!formNo){
        alert("不存在该申请单信息,审核失败!");
        return false;
    }
    audit(formNo,'formDetail');
}
//发货单详情页反审核
function unauditAtFormDetail(){
    $.iMessager.confirm("警告","确认该操作吗?",function(r){
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
    $.get("deliveryPlan/queryByFormNo.do",{
        formNo : formNo
    },function (result) {
        if(result==null){
            alert("不存在该申请单信息,操作失败!");
        }else{
            if(result.auditStatus=='未审核'){
                alert('该申请单未被审核，操作无效!');
            }else{
                $.get("deliveryPlan/unaudit.do",{formNo:formNo},function(result){
                    if(result.statusCode==200){
                        alert("操作完成!");
                        if(from=='formDetail'){
                            queryDeliveryPlanByFormNo(formNo);
                        }

                        if(from=='form'){
                            $("#deliveryPlanDetailDg").iDatagrid("reload");
                            /*$("#audit").iMenubutton("enable");
                            $("#unaudit").iMenubutton("disable");*/
                        }
                    }else{
                        alert(result.message);
                    }
                });
            }
        }
    });
}

//审核发货单
function audit(formNo,from) {
    $.get("deliveryPlan/queryByFormNo.do",{
        formNo : formNo
    },function (result) {
        if(result==null){
            alert("不存在该申请单信息,审核失败!");
        }else{
            if(result.auditStatus=='已审核'){
                alert('该申请单已被审核，无需再次审核!');
            }else{
                $.get("deliveryPlan/audit.do",{formNo:formNo},function(result){
                    alert("审核完成!");
                    if(from=='formDetail'){
                        queryDeliveryPlanByFormNo(formNo);
                        $("#unauditBtn").iMenubutton("enable");
                    }

                    if(from=='form'){
                        $("#deliveryPlanDetailDg").iDatagrid("reload");
                        $("#unaudit").iMenubutton("enable");
                    }
                });
            }
        }
    });
}
//删除发货单及详情
function deleteDeliveryPlan(){
    var detail = $("#deliveryPlanDetailDg").iDatagrid("getSelected");
    if(!detail){
        alert("请选择要删除的数据!");
        return false;
    }

    $.iMessager.confirm("提示","确认删除?",function(r){
        if(r){
            $.get("deliveryPlan/deleteByFormNo.do",{
                formNo : detail.deliveryPlan.formNo
            },function (result) {
                if(result.statusCode==200){
                    $.iMessager.show({
                        title:'提示',
                        msg:result.message,
                        timeout:3000,
                        showType:'slide'
                    });
                    $("#deliveryPlanDetailDg").iDatagrid("reload");
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
    var deliveryPlanDetail = $("#deliveryPlanDetailDg").iDatagrid("getSelected");
    if(!deliveryPlanDetail){
        alert("请选择发货单行记录！");
        return false;
    }
    addParentTab({title:'箱号条码',href:'console/jsp/showDeliveryPlanDetailBoxBarCodes.jsp?deliveryPlanDetailId=' +deliveryPlanDetail.id });
}
//箱号条码按钮点击事件
function showPackagrCode() {
	var deliveryPlanDetail = $("#deliveryPlanDetailDg").iDatagrid("getSelected");
	if(!deliveryPlanDetail){
		alert("请选择发货单行记录！");
		return false;
	}
	addParentTab({title:'包装条码打印',href:'console/jsp/packageCodePrint.jsp?deliveryPlanDetailId=' +deliveryPlanDetail.id });
	$.get("packageCode/deleteSession.do", {
	}, function(data) {
		
	})
}