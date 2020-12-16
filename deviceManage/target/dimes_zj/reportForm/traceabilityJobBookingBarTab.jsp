<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="reportForm/js/traceabilityCenter.js"></script>
<style type="text/css">
.table1 td, .table1 th {
	border: 1px solid #D8BFD8;
	font-size: 14px;
	text-align: center;
	height: 50px;
}

</style>
<script type="text/javascript">
<!--

//-->
$(function(){
		var param="<%=request.getParameter("param")%>";
		//领料单信息
		$.get("materialRequisitionDetail/queryMaterialRequisitionDetailByBarCode.do",{
			barCode:param
	    },function(result){
	    	var data = result[0];
	    	console.log(data.materialRequisition.pickingDate);
	    	var date = new Date(data.materialRequisition.pickingDate);
			var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
					+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
					+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
	    	$("#JobBookingBarPickingDate").iTextbox('setValue',value);
	    	$("#JobBookingBarPickerName").iTextbox('setValue',data.materialRequisition.pickerName);
	    	
	    })
	    
	    //根据箱条码信息获取箱信息
	    $.get("boxBar/queryBoxBarsByBarCode.do",{
	    	barCode:param
	    },function(data){
	    	$("#JobBookingBarSource").iTextbox('setValue',data.source);
	    	$("#JobBookingBarBoxNum").iTextbox('setValue',data.boxNum);
	    	if(data.tableName=="JobBookingFormDetail"){
	    		$.get("jobBookingFormDetail/queryJobBookingFormDetailById.do",{
	    			id:data.fkey,
	    	    },function(result){
	    	    	var detail = result;
	    	    	var jobbooking = detail.jobBookingForm;
	    	    	var date = new Date(detail.jobBookingForm.jobBookingDate);
	    			var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
	    					+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
	    					+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
	    			$("#JobBookingBarDate").iTextbox('setValue',value);
	    			$("#JobBookingBarProductionUnitName").iTextbox('setValue',jobbooking.productionUnitName);
	    			$("#JobBookerBarName").iTextbox('setValue',jobbooking.jobBookerName);
	    			$("#JobBookingBarClassName").iTextbox('setValue',detail.className);
	    			$("#JobBookingBarWorkSheetNo").iTextbox('setValue',detail.no);
	    			$("#JobBookingBarInventoryCode").iTextbox('setValue',detail.inventoryCode);
	    			$("#JobBookingBarInventoryName").iTextbox('setValue',detail.inventoryName);
	    			$("#JobBookingBarUnitType").iTextbox('setValue',detail.specificationType);
	    			$("#JobBookingBarProcessesName").iTextbox('setValue',detail.processName);
	    			$("#JobBookingBarDeviceSiteCode").iTextbox('setValue',detail.deviceSiteCode);
	    			$("#JobBookingBarBatchNumber").iTextbox('setValue',detail.batchNumber);
	    			$("#JobBookingBarFurnaceNumber").iTextbox('setValue',detail.furnaceNumber);
	    			$("#JobBookingBarAmountOfJobBooking").iTextbox('setValue',detail.amountOfJobBooking);
	    			$("#JobBookingBarAmountOfBoxes").iTextbox('setValue',jobbooking.amountOfBoxes);
	    			$("#JobBookingBarCWhName").iTextbox('setValue',detail.warehouseName);
	    	    });
	    	}
	    })
})
</script>
<head>
<title>Title</title>
</head>
<body>
						<div title="报工材料箱条码" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<div style="margin: 20px">
							<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">领料时间</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarPickingDate" data-toggle="topjui-textbox"
												data-options="required:false" id="JobBookingBarPickingDate" readonly="readonly"
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">领料仓库</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarCWhName" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="JobBookingBarCWhName"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">领料人员</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarPickerName" data-toggle="topjui-textbox"
												data-options="required:false," id="JobBookingBarPickerName" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">来源</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarSource" data-toggle="topjui-textbox"
												data-options="required:false," id="JobBookingBarSource" style="text-align: right;"
												readonly="readonly" /> 
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">报工时间</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarDate" data-toggle="topjui-textbox"
												data-options="required:false" id="JobBookingBarDate" readonly="readonly"
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">生产单元</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarProductionUnitName" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="JobBookingBarProductionUnitName"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">报工人员</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookerBarName" data-toggle="topjui-textbox"
												data-options="required:false," id="JobBookerBarName" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">班次</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarClassName" data-toggle="topjui-textbox"
												data-options="required:false," id="JobBookingBarClassName" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">工单单号</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarWorkSheetNo"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingBarWorkSheetNo" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">物料代码</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarInventoryCode"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingBarInventoryCode" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">物料名称</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarInventoryName"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingBarInventoryName" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">规格型号</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarUnitType"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingBarUnitType" readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">工序</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarProcessesName"
												data-toggle="topjui-textbox" data-options="required:false" style="text-align: right;"
												id="JobBookingBarProcessesName" readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">站点代码</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarDeviceSiteCode" data-toggle="topjui-textbox"
												data-options="required:false" id="JobBookingBarDeviceSiteCode" style="text-align: right;"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">批号</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarBatchNumber"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingBarBatchNumber" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">材料编号</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarFurnaceNumber"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingBarFurnaceNumber" readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">报工数量</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarAmountOfJobBooking"
												data-toggle="topjui-textbox" data-options="required:false" style="text-align: right;"
												id="JobBookingBarAmountOfJobBooking" readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">箱号</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarBoxNum" data-toggle="topjui-textbox"
												data-options="required:false" id="JobBookingBarBoxNum" style="text-align: right;"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">总箱数</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingBarAmountOfBoxes"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingBarAmountOfBoxes" readonly="readonly" />
										</div>
									</div>
								</div>
							</div>
						</div>
</body>
</html>
