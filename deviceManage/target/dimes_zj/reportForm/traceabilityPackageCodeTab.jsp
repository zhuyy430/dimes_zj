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
		$.get("packageCode/queryPackageCodeByCode.do",{
			code:'123qwe'//param
		    },function(result){
		    	console.log(result);
		    	$("#code").iTextbox('setValue',result.code);
		    	$("#inventoryCode").iTextbox('setValue',result.inventoryCode);
		    	$("#inventoryName").iTextbox('setValue',result.inventoryName);
		    	$("#batchNumber").iTextbox('setValue',result.batchNumber);
		    	$("#furnaceNumber").iTextbox('setValue',result.furnaceNumber);
		    	$("#specificationType").iTextbox('setValue',result.specificationType);
		    	$("#boxamount").iTextbox('setValue',result.boxamount);
		    	$("#boxnum").iTextbox('setValue',result.boxnum);
		    	$("#formNo").iTextbox('setValue',result.formNo);
		    	$("#saleNo").iTextbox('setValue',result.saleNo);
		    	$("#customer").iTextbox('setValue',result.customer);
		    	var date=new Date(result.createDate);
		    	var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
				+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
				+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
		    	$("#createDate").iTextbox('setValue',value);
		    	
		    })
})
</script>
<head>
<title>Title</title>
</head>
<body>
						<div title="采购订单" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<div style="margin: 20px">
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">包装条码</label>
										<div class="topjui-input-block">
											<input type="text" name="code" data-toggle="topjui-textbox"
												data-options="required:false" id="code" readonly="readonly"
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">物料代码</label>
										<div class="topjui-input-block">
											<input type="text" name="inventoryCode" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox" 
												data-options="required:false" id="inventoryCode"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">物料名称</label>
										<div class="topjui-input-block">
											<input type="text" name="inventoryName" data-toggle="topjui-textbox"
												data-options="required:false," id="inventoryName" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">批号</label>
										<div class="topjui-input-block">
											<input type="text" name="batchNumber" data-toggle="topjui-textbox"
												data-options="required:false," id="batchNumber" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">材料编号</label>
										<div class="topjui-input-block">
											<input type="text" name="furnaceNumber" data-toggle="topjui-textbox"
												data-options="required:false" id="furnaceNumber" readonly="readonly" 
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">规格型号</label>
										<div class="topjui-input-block">
											<input type="text" name="specificationType" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="specificationType" 
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">每箱数量</label>
										<div class="topjui-input-block">
											<input type="text" name="boxamount" data-toggle="topjui-textbox"
												data-options="required:false," id="boxamount" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">箱号</label>
										<div class="topjui-input-block">
											<input type="text" name="boxnum" data-toggle="topjui-textbox"
												data-options="required:false," id="boxnum" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">发货计划单号</label>
										<div class="topjui-input-block">
											<input type="text" name="formNo" data-toggle="topjui-textbox"
												data-options="required:false" id="formNo" readonly="readonly" 
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">销售订单编号</label>
										<div class="topjui-input-block">
											<input type="text" name="saleNo" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="saleNo" 
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">客户名称</label>
										<div class="topjui-input-block">
											<input type="text" name="customer" data-toggle="topjui-textbox"
												data-options="required:false," id="customer" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">生成时间</label>
										<div class="topjui-input-block">
											<input type="text" name="createDate" data-toggle="topjui-textbox"
												data-options="required:false," id="createDate" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
							</div>
						</div>
</body>
</html>
