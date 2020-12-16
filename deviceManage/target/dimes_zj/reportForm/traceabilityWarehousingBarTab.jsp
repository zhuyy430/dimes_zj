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
	    	$("#WarehousingBarPickingDate").iTextbox('setValue',value);
	    	$("#WarehousingBarPickerName").iTextbox('setValue',data.materialRequisition.pickerName);
	    	
	    })
	    
	    //根据箱条码信息获取箱信息
	    $.get("boxBar/queryBoxBarsByBarCode.do",{
	    	barCode:param
	    },function(data){
	    	$("#WarehousingBarSource").iTextbox('setValue',data.source);
	    	$("#WarehousingBarBoxNum").iTextbox('setValue',data.boxNum);
	    	if(data.tableName=="WarehousingApplicationFormDetail"){
	    		$.get("warehousingApplicationFormDetail/queryWarehousingApplicationFormDetailById.do",{
	    			id:data.fkey,
	    	    },function(result){
	    	    	var detail = result;
	    	    	var warehousing = detail.warehousingApplicationForm;
	    	    	$("#WarehousingBarCWhName").iTextbox('setValue',detail.warehouseName);
	    	    	var date=new Date(warehousing.receivingDate);
	    	    	var time = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
					+ (date.getDate()<10?"0"+date.getDate():date.getDate())
	    	    	$("#WarehousingBarReceivingDate").iTextbox('setValue',time);
	    	    	$("#WarehousingBarFormNo").iTextbox('setValue',warehousing.formNo);
	    	    	$("#WarehousingBarVendorName").iTextbox('setValue',warehousing.vendorName);
	    	    	$("#WarehousingBarPurchasingNo").iTextbox('setValue',detail.purchasingNo);
	    	    	$("#WarehousingBarInventoryCode").iTextbox('setValue',detail.inventoryCode);
	    	    	$("#WarehousingBarInventoryName").iTextbox('setValue',detail.inventoryName);
	    	    	$("#WarehousingBarSpecificationType").iTextbox('setValue',detail.specificationType);
	    	    	$("#WarehousingBarBatchNumber").iTextbox('setValue',detail.batchNumber);
	    	    	$("#WarehousingBarFurnaceNumber").iTextbox('setValue',detail.furnaceNumber);
	    	    	$("#WarehousingBarAmount").iTextbox('setValue',detail.amount);
	    	    	$("#WarehousingBarAmountOfBoxes").iTextbox('setValue',detail.amountOfBoxes);
	    	    	
	    	    });
	    	}
	    })
})
</script>
<head>
<title>Title</title>
</head>
<body>
						<div title="入库材料箱条码" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<div style="margin: 20px">
							<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">领料时间</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarPickingDate" data-toggle="topjui-textbox"
												data-options="required:false" id="WarehousingBarPickingDate" readonly="readonly"
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">领料仓库</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarCWhName" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="WarehousingBarCWhName"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">领料人员</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarPickerName" data-toggle="topjui-textbox"
												data-options="required:false," id="WarehousingBarPickerName" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">来源</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarSource" data-toggle="topjui-textbox"
												data-options="required:false," id="WarehousingBarSource" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">入库时间</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarReceivingDate" data-toggle="topjui-textbox"
												data-options="required:false" id="WarehousingBarReceivingDate" readonly="readonly"
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">入库单号</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarFormNo" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="WarehousingBarFormNo"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">供应商</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarVendorName" data-toggle="topjui-textbox"
												data-options="required:false," id="WarehousingBarVendorName" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">采购单号</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarPurchasingNo" data-toggle="topjui-textbox"
												data-options="required:false," id="WarehousingBarPurchasingNo" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">物料代码</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarInventoryCode"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WarehousingBarInventoryCode" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">物料名称</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarInventoryName"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WarehousingBarInventoryName" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">规格型号</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarSpecificationType"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WarehousingBarSpecificationType" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">批号</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarBatchNumber"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WarehousingBarBatchNumber" readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">材料编号</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarFurnaceNumber"
												data-toggle="topjui-textbox" data-options="required:false" style="text-align: right;"
												id="WarehousingBarFurnaceNumber" readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">数量</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarAmount"
												data-toggle="topjui-textbox" data-options="required:false" style="text-align: right;"
												id="WarehousingBarAmount" readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">箱号</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarBoxNum" data-toggle="topjui-textbox"
												data-options="required:false" id="WarehousingBarBoxNum" style="text-align: right;"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">总箱数</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingBarAmountOfBoxes"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WarehousingBarAmountOfBoxes" readonly="readonly" />
										</div>
									</div>
								</div>
							</div>
						</div>
</body>
</html>
