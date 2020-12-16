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
		$.get("warehousingApplicationForm/queryByFormNo.do",{
			 formNo:param
		    },function(result){
		    	$("#WarehousingVendorName").iTextbox('setValue',result.vendorName);
		    	$("#WarehousingFormNo").iTextbox('setValue',result.formNo);
		    	
		    	var date=new Date(result.receivingDate);
		    	var time = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
				+ (date.getDate()<10?"0"+date.getDate():date.getDate())
		    	$("#WarehousingReceivingDate").iTextbox('setValue',time);
		    	$("#WarehousingApplierName").iTextbox('setValue',result.applierName);
		    	
		    	 $.get("warehousingApplicationFormDetail/queryByRequestNo.do",{
		    		 requestNo:param
		    	    },function(data){
		    	    	var str="";
		            	$.each(data, function (index, item) {  
		    				var purchasingNo="";//采购单号
		    				if(item.purchasingNo!=null)
		    					purchasingNo=item.purchasingNo;
		    				
		    				var inventoryTypeName="";//物料类别
		    				if(item.inventoryTypeName!=null)
		    					inventoryTypeName=item.inventoryTypeName;
		    				
		    				var inventoryCode="";//物料代码
		    				if(item.inventoryCode!=null)
		    					inventoryCode=item.inventoryCode;
		    				
		    				var inventoryName="";//物料名称
		    				if(item.inventoryName!=null)
		    					inventoryName=item.inventoryName;
		    				
		    				var specificationType="";//规格型号
		    				if(item.specificationType!=null)
		    					specificationType=item.specificationType;
		    				
		    				var unit="";//单位
		    				if(item.unit!=null)
		    					unit=item.unit;
		    	
		    				var amount="";//	数量
		    				if(item.amount!=null)
		    					amount=item.amount;
		    	
		    				var batchNumber="";//批号
		    				if(item.batchNumber!=null)
		    					batchNumber=item.batchNumber;
		    	
		    				var furnaceNumber="";//材料编号
		    				if(item.furnaceNumber!=null)
		    					furnaceNumber=item.furnaceNumber;
		    	
		    				var amountOfPerBox="";//每箱数
		    				if(item.amountOfPerBox!=null)
		    					amountOfPerBox=item.amountOfPerBox;
		    	
		    				var warehouseCode="";//仓库代码
		    				if(item.warehouseCode!=null)
		    					warehouseCode=item.warehouseCode;
		    				
		    				var warehouseName="";//入库仓库
		    				if(item.warehouseName!=null)
		    					warehouseName=item.warehouseName;
		    				
		    				var amountOfInWarehouse="";//入库数量
		    				if(item.amountOfInWarehouse!=null)
		    					amountOfInWarehouse=item.amountOfInWarehouse;
		    				
		    				str += '<tr>' +   
		    						'<td>' + (index+1) +'</td>' +  
		    	                    '<td>' + purchasingNo +'</td>' +  
		    	                    '<td>' + inventoryTypeName + '</td>' +  
		    	                    '<td>' + inventoryCode + '</td>' +  
		    	                    '<td>' + inventoryName + '</td>' +  
		    	                    '<td>' + specificationType + '</td>' +  
		    	                    '<td>' + unit + '</td>' +  
		    	                    '<td>' + amount + '</td>' +  
		    	                    '<td>' + batchNumber + '</td>' +  
		    	                    '<td>' + furnaceNumber + '</td>' +  
		    	                    '<td>' + amountOfPerBox + '</td>' +  
		    	                    '<td>' + warehouseCode + '</td>' +  
		    	                    '<td>' + warehouseName + '</td>' +  
		    	                    '<td>' + amountOfInWarehouse + '</td>' +  
		    	                    '</tr>'; 
		                }); 
		            	$("#WarehousingTbItem").empty();
		                $("#WarehousingTbItem").prepend(str);
		    	    })
		    })
})
</script>
<head>
<title>Title</title>
</head>
<body>
						<div title="入库申请单" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<div style="margin: 20px">
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">供应商</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingVendorName" data-toggle="topjui-textbox"
												data-options="required:false" id="WarehousingVendorName" readonly="readonly"
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">申请单号</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingFormNo" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="WarehousingFormNo"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">收料日期</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingReceivingDate" data-toggle="topjui-textbox"
												data-options="required:false," id="WarehousingReceivingDate" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">申请人员</label>
										<div class="topjui-input-block">
											<input type="text" name="WarehousingApplierName" data-toggle="topjui-textbox"
												data-options="required:false," id="WarehousingApplierName" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm12">
									<table id="WarehousingTb" class="table1"
										style="width: 100%; margin-left: auto; margin-right: auto;">
										<thead>
											<tr>
												<th>序号</th>
												<th>采购单号</th>
												<th>物料类别</th>
												<th>物料代码</th>
												<th>物料名称</th>
												<th>规格型号</th>
												<th>单位</th>
												<th>数量</th>
												<th>批号</th>
												<th>材料编号</th>
												<th>每箱数</th>
												<th>仓库代码</th>
												<th>入库仓库</th>
												<th>入库数量</th>
											</tr>
										</thead>
										<tbody id="WarehousingTbItem"></tbody>
									</table>
									</div>
								</div>
							</div>
						</div>
</body>
</html>
