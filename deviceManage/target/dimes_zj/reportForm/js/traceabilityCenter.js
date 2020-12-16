//获取报工条码信息
function showJobBookingForm(param){
	$.get("jobBookingFormDetail/queryJobBookingFormDetailByBarCode.do",{
		barCode:param,//"BG-20190812004" 
    },function(result){
    	var detail = result;
    	var jobbooking = detail.jobBookingForm;
    	var date = new Date(detail.jobBookingForm.jobBookingDate);
		var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
				+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
				+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
		$("#JobBookingFormTime").iTextbox('setValue',value);
		$("#JobBookingFormProductionUnitName").iTextbox('setValue',jobbooking.productionUnitName);
		$("#jobBookerName").iTextbox('setValue',jobbooking.jobBookerName);
		$("#JobBookingFormClassName").iTextbox('setValue',detail.className);
		$("#JobBookingFormNo").iTextbox('setValue',detail.no);
		$("#JobBookingFormInventoryCode").iTextbox('setValue',detail.inventoryCode);
		$("#JobBookingFormInventoryName").iTextbox('setValue',detail.inventoryName);
		$("#JobBookingFormSpecificationType").iTextbox('setValue',detail.specificationType);
		$("#JobBookingFormProcessName").iTextbox('setValue',detail.processName);
		$("#JobBookingFormDeviceSiteCode").iTextbox('setValue',detail.deviceSiteCode);
		$("#JobBookingFormBatchNumber").iTextbox('setValue',detail.batchNumber);
		$("#JobBookingFormFurnaceNumber").iTextbox('setValue',detail.furnaceNumber);
		$("#JobBookingFormAmountOfJobBooking").iTextbox('setValue',detail.amountOfJobBooking);
		$("#JobBookingFormBoxNum").iTextbox('setValue',detail.boxNum);
		$("#JobBookingFormAmountOfBoxes").iTextbox('setValue',jobbooking.amountOfBoxes);
    });
	$('#southTabs').iTabs('select','报工条码信息');
}
//获取生产工单信息
function showWorkSheet(param){
	var src, title;
	src = "reportForm/traceabilityWorkSheetTab.jsp?param="+param;
	title = "生产工单";

	var iframe = '<iframe src="' + src + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	if($('#southTabs').iTabs('exists',title)){
        $('#southTabs').iTabs('close',title);
	}
	$('#southTabs').iTabs("add", {
		title: title,
		content: iframe,
		closable: true,
		iconCls: 'fa fa-th',
		border: true
	});
	//显示检验记录
	src = "reportForm/inspectionRecord.jsp?no="+param;
	title = "过程检验记录";

	iframe = '<iframe src="' + src + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	if($('#southTabs').iTabs('exists',title)){
        $('#southTabs').iTabs('close',title);
	}
	$('#southTabs').iTabs("add", {
		title: title,
		content: iframe,
		closable: true,
		iconCls: 'fa fa-th',
		border: true
	});

    $('#southTabs').iTabs("select","生产工单");
}
//获取报工材料箱条码信息
function showJobBookingBar(param){
	var src, title;
	src = "reportForm/traceabilityJobBookingBarTab.jsp?param="+param;
	title = "报工材料箱条码";

	var iframe = '<iframe src="' + src + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	if($('#southTabs').iTabs('exists',title)){
        $('#southTabs').iTabs('close',title);
	}
	$('#southTabs').iTabs("add", {
		title: title,
		content: iframe,
		closable: true,
		iconCls: 'fa fa-th',
		border: true
	});
}
//获取入库材料箱条码信息
function showWarehousingBar(param){
	var src, title;
	src = "reportForm/traceabilityWarehousingBarTab.jsp?param="+param;
	title = "入库材料箱条码";

	var iframe = '<iframe src="' + src + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	if($('#southTabs').iTabs('exists',title)){
        $('#southTabs').iTabs('close',title);
	}
	$('#southTabs').iTabs("add", {
		title: title,
		content: iframe,
		closable: true,
		iconCls: 'fa fa-th',
		border: true
	});
}
//获取入库申请单信息
function showWarehousing(param){
	var src, title;
	src = "reportForm/traceabilityWarehousingTab.jsp?param="+param;
	title = "入库申请单";

	var iframe = '<iframe src="' + src + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	if($('#southTabs').iTabs('exists',title)){
        $('#southTabs').iTabs('close',title);
	}
	$('#southTabs').iTabs("add", {
		title: title,
		content: iframe,
		closable: true,
		iconCls: 'fa fa-th',
		border: true
	});
}
//获取采购订单信息
function showPomain(param){
	var src, title;
	src = "reportForm/traceabilityPomainTab.jsp?param="+param;
	title = "采购订单";

	var iframe = '<iframe src="' + src + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	if($('#southTabs').iTabs('exists',title)){
        $('#southTabs').iTabs('close',title);
	}
	$('#southTabs').iTabs("add", {
		title: title,
		content: iframe,
		closable: true,
		iconCls: 'fa fa-th',
		border: true
	});
}
//获取采购包装条码
function showPackageCode(param){
	var src, title;
	src = "reportForm/traceabilityPackageCodeTab.jsp?param="+param;
	title = "包装条码";
	
	var iframe = '<iframe src="' + src + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
	if($('#southTabs').iTabs('exists',title)){
		$('#southTabs').iTabs('close',title);
	}
	$('#southTabs').iTabs("add", {
		title: title,
		content: iframe,
		closable: true,
		iconCls: 'fa fa-th',
		border: true
	});
}