<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../../common/jsp/head.jsp"%>
<link
	href="console/js/static/plugins/bootstrap/css/bootstrap.min.css"
	rel="stylesheet">
<link type="text/css"
	href="console/js/static/plugins/font-awesome/css/font-awesome.min.css"
	rel="stylesheet" />
<script type="text/javascript"
	src="console/js/static/public/js/topjui.index.js" charset="utf-8"></script>
<style type="text/css">

.table1 td, th {
	border: 1px solid #D8BFD8;
	font-size: 14px;
	text-align: center;
	height: 50px;
}

</style>
<script>
	var poid = "<%=request.getParameter("id")%>";
	$("#id").val(poid);
$(function() {
	//内容填充
	$.post("PO_Pomain/queryPO_PomainByPOID.do",{POID:poid},function(result){
		//$("#productionUnitName").textbox("setValue",data.deviceRepair.device.productionUnit.name);
		$("#cPOID").textbox("setValue", result.cPOID);
		$("#cMaker").textbox("setValue",result.cMaker);
		$("#cVerifier").textbox("setValue",result.cVerifier);
		$("#cBusType").textbox("setValue",result.cBusType);
		$("#cState").textbox("setValue",result.cState);
		$("#cMemo").textbox("setValue",result.cMemo);
		$("#cVenPerson").textbox("setValue",result.cVenPerson);
		$("#cVenPhone").textbox("setValue",result.cVenPhone);
		$("#cVenName").textbox("setValue",result.cVenName);
		
		var date = new Date(result.dPODate);
		var year = date.getFullYear();
		var month = date.getMonth()+1;
		var day = date.getDate();
		var dateStr = year + "-" + (month>9?month:("0"+month)) + "-" + (day>9?day:("0"+day));
		$("#dPODate").textbox("setValue",dateStr);
	});
	
	 $.ajax({
         type: "get",
         dataType: "json",
         async: false,  //同步
         url: "PO_Podetails/queryPO_PodetailsByPOID.do",
         data: {
        	 POID: poid,
        },
        success: function (data) {
             var str = "";
             $.each(data, function (index, item) {  
                 //循环获取数据
                 var i =index+1;
                 var date = new Date(item.dArriveDate);
			var year = date.getFullYear();
			var month = date.getMonth()+1;
			var day = date.getDate();
			var dateStr = year + "-" + (month>9?month:("0"+month)) + "-" + (day>9?day:("0"+day));
                 str += '<tr>' +   
                     '<td style="display:none">' + item.id + '</td>' +  
                     '<td>' + i +'</td>' +  
                     '<td>' + (item.cInvCName==null?"":item.cInvCName) + '</td>' +  
                     '<td>' + (item.cInvCode==null?"":item.cInvCode) + '</td>' +  
                     '<td>' + (item.cInvName==null?"":item.cInvName) + '</td>' +  
                     '<td>' + (item.cInvStd==null?"":item.cInvStd) + '</td>' +  
                     '<td>' + (item.iQuantity==null?"":item.iQuantity) + '</td>' +  
                     '<td>' + (item.cComUnitName==null?"":item.cComUnitName) + '</td>' +  
                     '<td>' + (dateStr==null?"":dateStr) + '</td>' +  
                     '<td>' + (item.iflag==null?"":item.iflag) + '</td>' +  
                     '<td>' + (item.iarrqty==null?"":item.iarrqty) + '</td>' +  
                     '<td>' + (item.cPosition==null?"":item.cPosition) + '</td>' +  
                     '</tr>'; 
             }); 
             $("#singles").prepend(str);
         },
     });
	 
})
</script>

<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addWorkSheetForm" method="post" style="margin-right: 5%">
			<div title="" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<!-- <div
						style="height: 30px; width: 100%; font-size: 1.5em; font-weight: bold; margin-top: 20px;">
							<button type="button" class="btn btn-default" id="update">保存</button>
							<button type="button" class="btn btn-default" id="delay">延期</button>
							<button type="button" class="btn btn-default" id="transfer">转移</button>
							<div class="btn-group">
								<button style="width: 160px" type="button"
									class="btn btn-default dropdown-toggle" data-toggle="dropdown"
									aria-haspopup="true" aria-expanded="false">
									状态转移 <span class="caret"></span>
								</button>
								<ul class="dropdown-menu" id="problemStatus">
								</ul>
							</div>
					</div> -->
					<div
						style="height: 30px; width: 100%; text-align: center; font-size: 1.5em; font-weight: bold; margin: 20px auto;">
						采购订单</div>
					<div class="topjui-row">
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="font-weight: 700;">采购订单号</label>
							<div class="topjui-input-block">
								<input type="text" name="cPOID" data-toggle="topjui-textbox"
									data-options="required:false" id="cPOID" readonly="readonly">
								<input type="text" hidden="hidden" name="org" id="org">
								<input type="text" hidden="hidden" name="id"
									id="id">
							</div>
						</div>

						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="font-weight: 700;">单据日期</label>
							<div class="topjui-input-block">
								<input type="text" name="dPODate" style="width: 100%;"
									data-toggle="topjui-textbox"
									data-options="required:false,showSeconds:true" id="dPODate"
									readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="font-weight: 700;">订单状态</label>
							<div class="topjui-input-block">
								<input type="text" name="cState"
									data-toggle="topjui-textbox" data-options="required:false,"
									id="cState" readonly="readonly"/> 
									<input type="text" hidden="hidden"
									name="sourceOfProblemId" id="sourceOfProblemId">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="font-weight: 700;">供应商</label>
							<div class="topjui-input-block">
								<input type="text" name="cVenName"
									data-toggle="topjui-textbox" data-options="required:false,"
									id="cVenName" readonly="readonly"/> 
									<input type="text" hidden="hidden"
									name="functionModuleId" id="functionModuleId">
							</div>
						</div>
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="font-weight: 700;">联系人</label>
							<div class="topjui-input-block">
								<input type="text" name="cVenPerson"
									data-toggle="topjui-textbox" data-options="required:false,"
									id="cVenPerson" readonly="readonly"/> 
									<input type="text" hidden="hidden"
									name="functionCategoryId" id="functionCategoryId">
							</div>
						</div>
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="font-weight: 700;">联系电话</label>
							<div class="topjui-input-block">
								<input type="text" name="cVenPhone" data-toggle="topjui-textbox"
									data-options="required:false," id="cVenPhone" readonly="readonly"/> 
									<input
									type="hidden" name="emergencyCode" id="emergencyCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="font-weight: 700;">订单类型</label>
							<div class="topjui-input-block">
								<input type="text" name="cBusType" data-toggle="topjui-textbox"
									data-options="required:false" id="cBusType" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="font-weight: 700;">制单人</label>
							<div class="topjui-input-block">
								<input type="text" name="cMaker" data-toggle="topjui-textbox"
									data-options="required:false" id="cMaker"
									onChange="ofsoNoChange()" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm4">
							<label class="topjui-form-label" style="font-weight: 700;">审核人</label>
							<div class="topjui-input-block">
								<input type="text" name="cVerifier" data-toggle="topjui-textbox"
									data-options="required:false," id="cVerifier" readonly="readonly" />
								<input type="text" hidden="hidden" name="auditorCode"
									id="auditorCode">
							</div>
						</div>

					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<div class="topjui-input-block">
								<div style="width: 100%;">
									<table id="singles" class="table1"
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
									</table>
								</div>
							</div>
						</div>
					</div>

					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="font-weight: 700;">备注</label>
							<div class="topjui-input-block">
								<input type="text" name="cMemo"
									data-toggle="topjui-textarea" style="height: 180px;"
									data-options="required:false" id="cMemo" readonly="readonly">
							</div>
						</div>
					</div>
					
				</div>
			</div>
		</form>
	</div>
</div>
<div id="parameterDialog"></div>
<div id="deviceSitesDialog"></div>