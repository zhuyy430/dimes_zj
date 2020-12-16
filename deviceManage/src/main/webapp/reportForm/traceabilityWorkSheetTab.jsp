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
//时间戳转换为时间
function timestampToTime(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y+M+D+h+m+s;
}




$(function(){
		var param="<%=request.getParameter("param")%>";
		$.get("workSheet/queryWorkSheetByNo.do",{
			No:param,
	    },function(result){
	    	var date = new Date(result.manufactureDate);
			var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
					+ (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
					+ (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
	    	
	    	$("#WorkSheetNo").iTextbox('setValue',result.no);
	    	$("#WorkSheetManufactureDate").iTextbox('setValue',value);
	    	var type="";
	    	if(result.workSheetType=="common")
	    		type="普通工单";
	    	else if(result.workSheetType=="repair")
	    		type="返修工单";
	    	$("#WorkSheetType").iTextbox('setValue',type);
	    	var status="";
	    	if(result.status=="0")
	    		status="计划";
	    	else if(result.status=="1")
	    		status="加工中";
	    	else if(result.status=="2")
	    		status="停工";
	    	else if(result.status=="3")
	    		status="完工";
	    	$("#WorkSheetStatus").iTextbox('setValue',status);
	    	$("#WorkSheetWorkPieceCode").iTextbox('setValue',result.workPieceCode);
	    	$("#WorkSheetWorkPieceName").iTextbox('setValue',result.workPieceName);
	    	$("#WorkSheetUnitType").iTextbox('setValue',result.unitType);
	    	$("#WorkSheetGraphNumber").iTextbox('setValue',result.graphNumber);
	    	$("#WorkSheetProductCount").iTextbox('setValue',result.productCount);
	    	$("#WorkSheetProductionUnitName").iTextbox('setValue',result.productionUnitName);
	    	$("#WorkSheetBatchNumber").iTextbox('setValue',result.batchNumber);
	    	$("#WorkSheetStoveNumber").iTextbox('setValue',result.stoveNumber);
	    	$("#WorkSheetStoveNumber").iTextbox('setValue',result.stoveNumber);
	    	//获取工单详情
	    	/*$.get("workSheet/queryWorkSheetDetailByWorkSheetId4Print.do",{
	    		workSheetId:result.id,
	        },function(data){
	        	var str="";
	        	$.each(data, function (index, item) {  
					var processRoute="";//加工顺序
					if(item.processRoute!=null)
						processRoute=item.processRoute;
					
					var processCode="";//工序代码
					if(item.processCode!=null)
						processCode=item.processCode;
					
					var processName="";//工序名称
					if(item.processName!=null)
						processName=item.processName;
					
					var deviceCode="";//设备代码
					if(item.deviceCode!=null)
						deviceCode=item.deviceCode;
					
					var deviceName="";//设备名称
					if(item.deviceName!=null)
						deviceName=item.deviceName;
					
					var productionCount="";//计划数量
					if(item.productionCount!=null)
						productionCount=item.productionCount;
					
					var reportCount="";//报工数量
					if(item.reportCount!=null)
						reportCount=item.reportCount;
					
					var unqualifiedCount="";//不合格数
					if(item.unqualifiedCount!=null)
						unqualifiedCount=item.unqualifiedCount;
					
					var repairCount="";//返修数
					if(item.repairCount!=null)
						repairCount=item.repairCount;
					
					var scrapCount="";//报废数
					if(item.scrapCount!=null)
						scrapCount=item.scrapCount;
		
					var compromiseCount="";//让步接收数
					if(item.compromiseCount!=null)
						compromiseCount=item.compromiseCount;
					
					var detailStatus="";//详情状态 
					if(result.status=="0")
						detailStatus="计划";
			    	else if(result.status=="1")
			    		detailStatus="加工中";
			    	else if(result.status=="2")
			    		detailStatus="停工";
			    	else if(result.status=="3")
			    		detailStatus="完工";
					
					str += '<tr>' +   
		                    '<td>' + processRoute +'</td>' +  
		                    '<td>' + processCode + '</td>' +  
		                    '<td>' + processName + '</td>' +  
		                    '<td>' + deviceCode + '</td>' +  
		                    '<td>' + deviceName + '</td>' +  
		                    '<td>' + productionCount + '</td>' +  
		                    '<td>' + reportCount + '</td>' +  
		                    '<td>' + unqualifiedCount + '</td>' +  
		                    '<td>' + repairCount + '</td>' +  
		                    '<td>' + scrapCount + '</td>' +  
		                    '<td>' + compromiseCount + '</td>' +  
		                    '<td>' + detailStatus + '</td>' +  
		                    '</tr>'; 
	            }); 
	        	$("#WorkSheetTbItem").empty();
	            $("#WorkSheetTbItem").prepend(str);
	           
	            $('#southTabs').iTabs('select',{
	            	which: '报工条码信息',
	            	});
	        })*/
	    	//获取装备关联记录
			$.get("equipmentMappingRecord/queryEquipmentMappingRecordByWorkSheetNo.do",{
				workSheetId:result.id,
			},function(data){
				var str="";
				$.each(data, function (index, item) {

					var deviceSiteCodeM="";//站点代码
					var deviceSiteNameM="";//站点名称

					if(item.deviceSite!=null){
						if(item.deviceSite.code!=null){
							deviceSiteCodeM=item.deviceSite.code;
						}
						if(item.deviceSite.name!=null){
							deviceSiteNameM=item.deviceSite.name;
						}
					}

					var equipmentNoM="";//装备序号
					var equipmentCodeM="";//装备代码
					var equipmentNameM="";//装备名称
					var equipmentUnitTypeM="";//规格型号
					var measurementType=""; //计量类型
					var cumulation="";//计量累积
					var measurementObjective="";//计量目标
					var measurementDifference="";//计量差异
					var mappingDate="";//关联时间
					var unbindDate="";//解除时间

					if(item.equipment!=null){
						if(item.equipment.code!=null){
							equipmentNoM=item.equipment.code;
						}
						if(item.equipment.cumulation!=null){
							cumulation=item.equipment.cumulation;
						}
						if(item.equipment.measurementDifference!=null){
							measurementDifference=item.equipment.measurementDifference;
						}



						if(item.equipment.equipmentType!=null){
							if(item.equipment.equipmentType.code!=null){
								equipmentCodeM=item.equipment.equipmentType.code;
							}
							if(item.equipment.equipmentType.name!=null){
								equipmentNameM=item.equipment.equipmentType.name;
							}
							if(item.equipment.equipmentType.unitType!=null){
								equipmentUnitTypeM=item.equipment.equipmentType.unitType;
							}
							if(item.equipment.equipmentType.measurementObjective!=null){
								measurementObjective=item.equipment.equipmentType.measurementObjective;
							}
							if(item.equipment.equipmentType.measurementType!=null){
								measurementType=item.equipment.equipmentType.measurementType;
							}
						}
					}

					if(item.unbindDate!=null){
						unbindDate=timestampToTime(item.unbindDate);
					}
					if(item.mappingDate!=null){
                        mappingDate=timestampToTime(item.mappingDate);
					}



					str += '<tr>' +
							'<td>' + (index+1) +'</td>' +
                            '<td>' + deviceSiteCodeM +'</td>' +
                            '<td>' + deviceSiteNameM + '</td>' +
							'<td>' + equipmentNoM +'</td>' +
							'<td>' + equipmentCodeM + '</td>' +
							'<td>' + equipmentNameM + '</td>' +
							'<td>' + equipmentUnitTypeM + '</td>' +
							'<td>' + measurementType + '</td>' +
							'<td>' + cumulation + '</td>' +
							'<td>' + measurementObjective + '</td>' +
							'<td>' + measurementDifference + '</td>' +
							'<td>' + mappingDate + '</td>' +
							'<td>' + unbindDate + '</td>' +
							'</tr>';
				});
				$("#WorkSheetTbItem").empty();
				$("#WorkSheetTbItem").prepend(str);

				$('#southTabs').iTabs('select',{
					which: '报工条码信息',
				});
			})
	    })
})
</script>
<head>
<title>Title</title>
</head>
<body>
						<div title="生产工单" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<div style="margin: 20px">
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">工单单号</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetNo" data-toggle="topjui-textbox"
												data-options="required:false" id="WorkSheetNo" readonly="readonly"
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">生产日期</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetManufactureDate" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="WorkSheetManufactureDate"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">工单类型</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetType" data-toggle="topjui-textbox"
												data-options="required:false," id="WorkSheetType" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">工单状态</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetStatus" data-toggle="topjui-textbox"
												data-options="required:false," id="WorkSheetStatus" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">工件代码</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetWorkPieceCode"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WorkSheetWorkPieceCode" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">工件名称</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetWorkPieceName"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WorkSheetWorkPieceName" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">规格型号</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetUnitType"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WorkSheetUnitType" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">图号</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetGraphNumber"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WorkSheetGraphNumber" readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">生产数量</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetProductCount"
												data-toggle="topjui-textbox" data-options="required:false" style="text-align: right;"
												id="WorkSheetProductCount" readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">生产单元</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetProductionUnitName" data-toggle="topjui-textbox"
												data-options="required:false" id="WorkSheetProductionUnitName" style="text-align: right;"
												onChange="ofsoNoChange()" readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">批号</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetBatchNumber"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WorkSheetBatchNumber" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">材料编号</label>
										<div class="topjui-input-block">
											<input type="text" name="WorkSheetStoveNumber"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="WorkSheetStoveNumber" readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm12">
									<table id="WorkSheet" class="table1"
										style="width: 100%; margin-left: auto; margin-right: auto;">
										<thead>
											<tr>
												<!-- <th>序号</th> -->
												<%--<th>加工顺序</th>
												<th>工序代码</th>
												<th>工序名称</th>
												<th>设备代码</th>
												<th>设备名称</th>
												<th>计划数量</th>
												<th>报工数量</th>
												<th>不合格数量</th>
												<th>返修数量</th>
												<th>报废数量</th>
												<th>让步接收数量</th>
												<th>状态</th>--%>
												<th>序号</th>
												<th>站点编码</th>
												<th>站点名称</th>
												<th>装备序号</th>
												<th>装备代码</th>
												<th>装备名称</th>
												<th>规格型号</th>
												<th>计量类型</th>
												<th>计量累积</th>
												<th>计量目标</th>
												<th>计量差异</th>
												<th>关联时间</th>
												<th>解除时间</th>
											</tr>
										</thead>
										<tbody id="WorkSheetTbItem"></tbody>
									</table>
									</div>
								</div>
									
							</div>
						</div>
</body>
</html>
