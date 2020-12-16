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
		$.get("PO_Pomain/queryPO_PomainByPOID.do",{
			POID:param
		    },function(result){
		    	console.log(result);
		    	$("#PomainCPOID").iTextbox('setValue',result.cPOID);
		    	$("#PomainDPODate").iTextbox('setValue',result.dPODate);
		    	$("#PomainCBusType").iTextbox('setValue',result.cBusType);
		    	$("#PomainCState").iTextbox('setValue',result.cState);
		    	$("#PomainCVenName").iTextbox('setValue',result.cVenName);
		    	$("#PomainCVenPerson").iTextbox('setValue',result.cVenPerson);
		    	$("#PomainCVenPhone").iTextbox('setValue',result.cVenPhone);
		    	$("#PomainCMaker").iTextbox('setValue',result.cMaker);
		    	
		    	 $.get("PO_Podetails/queryPO_PodetailsByPOID.do",{
		    		 POID:param
		    	    },function(data){
		    	    	var str="";
		            	$.each(data, function (index, item) {  
		    				
		    				var cInvCName="";//物料类别
		    				if(item.cInvCName!=null)
		    					cInvCName=item.cInvCName;
		    				
		    				var cInvCode="";//物料代码
		    				if(item.cInvCode!=null)
		    					cInvCode=item.cInvCode;
		    				
		    				var cInvName="";//物料名称
		    				if(item.cInvName!=null)
		    					cInvName=item.cInvName;
		    				
		    				var cInvStd="";//规格型号
		    				if(item.cInvStd!=null)
		    					cInvStd=item.cInvStd;
		    				
		    				var iQuantity="";//	数量
		    				if(item.iQuantity!=null)
		    					iQuantity=item.iQuantity;
		    				
		    				var cComUnitName="";//单位
		    				if(item.cComUnitName!=null)
		    					cComUnitName=item.cComUnitName;
		    	
		    				var dArriveDate="";//计划到货日期
		    				if(item.dArriveDate!=null)
		    					dArriveDate=item.dArriveDate;
		    	
		    				var iflag="";//标志
		    				if(item.iflag!=null)
		    					iflag=item.iflag;
		    	
		    				var iarrqty="";//到货数量
		    				if(item.iarrqty!=null)
		    					iarrqty=item.iarrqty;
		    	
		    				var cPosition="";//货位编码
		    				if(item.cPosition!=null)
		    					cPosition=item.cPosition;
		    				
		    				str += '<tr>' +   
		    						'<td>' + (index+1) +'</td>' +  
		    	                    '<td>' + cInvCName +'</td>' +  
		    	                    '<td>' + cInvCode + '</td>' +  
		    	                    '<td>' + cInvName + '</td>' +  
		    	                    '<td>' + cInvStd + '</td>' +  
		    	                    '<td>' + iQuantity + '</td>' +  
		    	                    '<td>' + cComUnitName + '</td>' +  
		    	                    '<td>' + dArriveDate + '</td>' +  
		    	                    '<td>' + iflag + '</td>' +  
		    	                    '<td>' + iarrqty + '</td>' +  
		    	                    '<td>' + cPosition + '</td>' +  
		    	                    '</tr>'; 
		                }); 
		            	$("#PomainTbItem").empty();
		                $("#PomainTbItem").prepend(str);
		    	    })
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
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">采购订单号</label>
										<div class="topjui-input-block">
											<input type="text" name="PomainCPOID" data-toggle="topjui-textbox"
												data-options="required:false" id="PomainCPOID" readonly="readonly"
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">单据日期</label>
										<div class="topjui-input-block">
											<input type="text" name="PomainDPODate" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox" 
												data-options="required:false" id="PomainDPODate"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">订单类型</label>
										<div class="topjui-input-block">
											<input type="text" name="PomainCBusType" data-toggle="topjui-textbox"
												data-options="required:false," id="PomainCBusType" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">订单状态</label>
										<div class="topjui-input-block">
											<input type="text" name="PomainCState" data-toggle="topjui-textbox"
												data-options="required:false," id="PomainCState" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">供应商</label>
										<div class="topjui-input-block">
											<input type="text" name="PomainCVenName" data-toggle="topjui-textbox"
												data-options="required:false" id="PomainCVenName" readonly="readonly" 
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">联系人</label>
										<div class="topjui-input-block">
											<input type="text" name="PomainCVenPerson" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="PomainCVenPerson" 
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">联系电话</label>
										<div class="topjui-input-block">
											<input type="text" name="PomainCVenPhone" data-toggle="topjui-textbox"
												data-options="required:false," id="PomainCVenPhone" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">制单人</label>
										<div class="topjui-input-block">
											<input type="text" name="PomainCMaker" data-toggle="topjui-textbox"
												data-options="required:false," id="PomainCMaker" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm12">
									<table id="PomainTb" class="table1"
										style="width: 100%; margin-left: auto; margin-right: auto;">
										<thead>
											<tr>
												<th>序号</th>
												<th>物料类别</th>
												<th>物料代码</th>
												<th>物料名称</th>
												<th>规格型号</th>
												<th>数量</th>
												<th>单位</th>
												<th>计划到货日期</th>
												<th>标志</th>
												<th>到货数量</th>
												<th>货位编码</th>
											</tr>
										</thead>
										<tbody id="PomainTbItem"></tbody>
									</table>
									</div>
								</div>
							</div>
						</div>
</body>
</html>
