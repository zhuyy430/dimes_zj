<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/jtopo-min.js"></script>
<script type="text/javascript" src="reportForm/js/traceabilityCenter.js"></script>
<script type="text/javascript" src="reportForm/js/reverseTrace.js"></script>
<style type="text/css">
.table1 td, .table1 th {
	border: 1px solid #D8BFD8;
	font-size: 14px;
	text-align: center;
	height: 50px;
}
</style>
<head>
<title>Title</title>
</head>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 30%;">
                    <div style="height: 50px;width: 90%;padding-top: 10px;padding-left: 10px;">
                        报工条码 <input data-toggle="topjui-textbox" data-options="width:200" id="barCode">
                        <button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="reverseTrace()" style="margin-right:20px;">追溯</button>
                        包装条码 <input data-toggle="topjui-textbox" data-options="width:200" id="packBarCode">
                        <button type="button" class="layui-btn layui-btn-sm layui-btn-primary" onclick="packReverseTrace()">追溯</button>
                    </div>
                    <canvas height="300" width="1500" id="myCanvas"></canvas>
				</div>
				<div data-options="region:'south',fit:false,split:true,border:false"
					style="height: 40%">
					<div data-toggle="topjui-tabs"
						data-options="id:'southTabs',
	                     fit:true,
	                     border:false
	                     ">
						<div title="报工条码信息" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<div style="margin: 20px">
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">报工时间</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormTime" data-toggle="topjui-textbox"
												data-options="required:false" id="JobBookingFormTime" readonly="readonly"
												style="text-align: right;">
										</div>
									</div>

									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">生产单元</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormProductionUnitName" style="width: 100%;text-align: right;"
												data-toggle="topjui-textbox"
												data-options="required:false,showSeconds:true" id="JobBookingFormProductionUnitName"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">报工人员</label>
										<div class="topjui-input-block">
											<input type="text" name="jobBookerName" data-toggle="topjui-textbox"
												data-options="required:false," id="jobBookerName" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">班次</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormClassName" data-toggle="topjui-textbox"
												data-options="required:false," id="JobBookingFormClassName" style="text-align: right;"
												readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">工单单号</label>
										<div class="topjui-input-block">
											<input type="text" name="cVenName"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingFormNo" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">物料代码</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormInventoryCode"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingFormInventoryCode" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">物料名称</label>
										<div class="topjui-input-block">
											<input type="text" name="cVenPhone"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingFormInventoryName" readonly="readonly" /> 
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">规格型号</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormSpecificationType"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingFormSpecificationType" readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">工序</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormProcessName"
												data-toggle="topjui-textbox" data-options="required:false" style="text-align: right;"
												id="JobBookingFormProcessName" readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">站点代码</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormDeviceSiteCode" data-toggle="topjui-textbox"
												data-options="required:false" id="JobBookingFormDeviceSiteCode" style="text-align: right;"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">批号</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormBatchNumber"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingFormBatchNumber" readonly="readonly" />
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">材料编号</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormFurnaceNumber"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingFormFurnaceNumber" readonly="readonly" />
										</div>
									</div>
								</div>
								<div class="topjui-row">
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">报工数量</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormAmountOfJobBooking"
												data-toggle="topjui-textbox" data-options="required:false" style="text-align: right;"
												id="JobBookingFormAmountOfJobBooking" readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">箱号</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormBoxNum" data-toggle="topjui-textbox"
												data-options="required:false" id="JobBookingFormBoxNum" style="text-align: right;"
												readonly="readonly">
										</div>
									</div>
									<div class="topjui-col-sm3">
										<label class="topjui-form-label" style="font-weight: 700;font-size: 18px">总箱数</label>
										<div class="topjui-input-block">
											<input type="text" name="JobBookingFormAmountOfBoxes"
												data-toggle="topjui-textbox" data-options="required:false," style="text-align: right;"
												id="JobBookingFormAmountOfBoxes" readonly="readonly" />
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
