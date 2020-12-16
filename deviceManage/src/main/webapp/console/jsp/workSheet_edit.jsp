<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
	$(function() {
		//条码信息
		$('#barCode').iDatagrid({
	    url:'processRecord/queryProcessRecordByWorkSheetId.do',
	    columns:[[
	        {field:'id',title:'id',checkbox:false,hidden:true,width:200},
	        {field:'deviceSiteCode',title:'设备站点代码',width:200},
	        {field:'deviceSiteName',title:'设备站点名称',width:200},
	        {field:'collectionDate',title:'采集时间',width:200,formatter:function(value,row,index){
				if(value){
    				var date = new Date(value);
                    var month = date.getMonth()+1;
                    var monthStr = ((month>=10)?month:('0' + month));
                    
                    var day = date.getDate();
                    var dayStr = ((day>=10)?day:('0'+day));
                    var hour = date.getHours();
                    var hourStr = ((hour>=10)?hour:('0' + hour));
                    
                    var minute = date.getMinutes();
                    var minuteStr = ((minute>=10)?minute:('0' +minute));
                    
                    var second = date.getSeconds();
                    var secondStr = ((second>=10)?second:('0' +second));
                    
                    var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr  + 
                    				' ' + hourStr + ':' + minuteStr + ':' + secondStr; 
                      return dateStr;
                    	}else{
                    		return '';
                    	}
					}
	        },
	        {field:'serialNo',title:'二维码',width:200}
	    ]],
	    queryParams:{
	    	workSheetId:$("#productionUnitDg").iDatagrid("getSelected").id
	    }
	});
		//修改生产单元				
		$("#productionUnitName").iCombotreegrid({
			url : 'productionUnit/queryTopProductionUnits.do',
			idField : 'name',
			treeField : 'name',
			columns : [ [ {
				field : 'name',
				title : '名称',
				width : 300
			} ] ],
			onClickRow : function(row) {
				$("#productionUnitId").val(row.id);
				$("#productionUnitCode").val(row.productionUnitCode);
			}
		});

		// 可编辑工单 详情
		$('#workSheetDetail').iEdatagrid(
				{
					url : 'workSheet/queryWorkSheetDetailByWorkSheetId.do',
					queryParams : {
						workSheetId : $('#productionUnitDg').iDatagrid(
								'getSelected').id
					},
					pagination : false,
					onClickCell : function(rowIndex, field, value) {
						if (field == 'firstReport') {
							$('#parameterDialog').dialog("open");
						}
						//根据工序查询站点
						/* 	if(field=='deviceSiteCode' || field=='deviceSiteName'){
								 $('#deviceSitesDialog').dialog("open");
							} */
					}
				});

		//弹出查询参数窗体
		$('#parameterDialog').dialog({
			title : '添加参数信息',
			width : 800,
			height : 600,
			closed : true,
			cache : false,
			href : 'console/jsp/workSheet_editParameter.jsp',
			modal : true
		});

		//弹出查询参数窗体
		$('#deviceSitesDialog')
				.dialog(
						{
							title : '添加参数信息',
							width : 800,
							height : 600,
							closed : true,
							cache : false,
							href : 'console/jsp/workSheet_addDevice.jsp',
							modal : true,
							toolbar : [ {
								text : '保存',
								iconCls : 'fa fa-save',
								handler : function() {
									var ids = $('#processesDeviceSiteTable')
											.iDatagrid('getSelections');
									var idsArray = new Array();
									if (ids.length <= 0) {
										alert('请选择要添加的设备站点!');
									} else {
										for (var i = 0; i < ids.length; i++) {
											idsArray.push(ids[i].deviceSite.id);
										}
										$
												.get(
														'workSheet/addDeviceSite4Processes.do',
														{
															processesId : $(
																	'#workSheetDetail')
																	.iEdatagrid(
																			'getSelected').processId,
															deviceSiteIds : JSON
																	.stringify(idsArray)
														},
														function(data) {
															if (data.success) {
																$(
																		'#processesDeviceSiteTable')
																		.iDatagrid(
																				'reload');
																$(
																		'#workSheetDetail')
																		.iEdatagrid(
																				'reload',
																				{
																					workpieceId : $(
																							'#workPieceCode')
																							.iCombogrid(
																									"getValue")
																				});
															} else {
																alert(data.msg);
															}
														});
									}
								}
							} ]
						});
	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addWorkSheetForm" method="post">
			<div title="" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div
						style="height: 30px; width: 100%; text-align: center; font-size: 1.5em; font-weight: bold; margin: 20px auto;">
						生产任务单</div>
					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">单号</label>
							<div class="topjui-input-block">
								<input type="text" name="no" data-toggle="topjui-textbox"
									data-options="required:true" id="no" readonly="readonly">
								<input type="hidden" name="id" id="id">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">生产日期</label>
							<div class="topjui-input-block">
								<input type="text" name="manufactureDate" style="width: 100%;"
									data-toggle="topjui-datetimebox"
									data-options="required:true,showSeconds:true"
									id="manufactureDate">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">工件代码</label>
							<div class="topjui-input-block">
								<input type="text" name="workPieceCode"
									data-toggle="topjui-textbox" readonly="readonly"
									id="workPieceCode">
								<!-- 								<input type="text" name="workPieceCode"
									data-toggle="topjui-combogrid" data-options="required:true"
									id="workPieceCode"> -->
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">工件名称</label>
							<div class="topjui-input-block">
								<input type="text" name="workPieceName"
									data-toggle="topjui-textbox" data-options="required:false"
									id="workPieceName" readonly="readonly" />
							</div>
						</div>
					</div>
					<div class="topjui-row">

						<div class="topjui-col-sm3">
							<label class="topjui-form-label">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="unitType" data-toggle="topjui-textbox"
									data-options="required:false" id="unitType" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">图号</label>
							<div class="topjui-input-block">
								<input type="text" name="graphNumber"
									data-toggle="topjui-textbox" data-options="required:false"
									id="graphNumber" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">客户图号</label>
							<div class="topjui-input-block">
								<input type="text" name="customerGraphNumber"
									data-toggle="topjui-textbox" data-options="required:false"
									id="customerGraphNumber" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">版本号</label>
							<div class="topjui-input-block">
								<input type="text" name="version" data-toggle="topjui-textbox"
									data-options="required:false" id="version" readonly="readonly">
							</div>
						</div>

					</div>
					<div class="topjui-row">

						<div class="topjui-col-sm3">
							<label class="topjui-form-label">生产总量</label>
							<div class="topjui-input-block">
								<input type="text" name="productCount"
									data-toggle="topjui-numberbox" data-options="required:false"
									id="productCount">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">批号</label>
							<div class="topjui-input-block">
								<input type="text" name="batchNumber"
									data-toggle="topjui-textbox" data-options="required:false"
									id="batchNumber">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">材料编号</label>
							<div class="topjui-input-block">
								<input type="text" name="stoveNumber"
									data-toggle="topjui-textbox" data-options="required:false"
									id="stoveNumber">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">生产单元</label>
							<div class="topjui-input-block">
								<input data-toggle="topjui-combotreegrid"
									data-options="width:'100%'" name="productionUnitName"
									id="productionUnitName"> <input type="hidden"
									name="productionUnitId" id="productionUnitId"> <input
									type="hidden" name="productionUnitCode" id="productionUnitCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">工单类型</label>
							<div class="topjui-input-block">
								<input type="text" name="workSheetType" data-toggle="topjui-combobox"
									data-options="
										valueField: 'value',
										textField: 'text',
										data: [{
										    value: 'common',
										    text: '普通工单'
										},{
										    value: 'repair',
										    text: '返修工单'
										}]" id="workSheetType">
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label">备注</label>
							<div class="topjui-input-block">
								<input type="text" name="note" data-toggle="topjui-textbox"
									data-options="required:false" id="note">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>


	<div data-options="region:'south',fit:false,split:true,border:false"
		style="height: 65%">
		<div data-toggle="topjui-tabs"
			data-options="id:'southTabs',
                     fit:true,
                     border:false
                    ">
			<div title="工序流转" data-options="id:'tab0',iconCls:'fa fa-th'">
				<table title="" id="workSheetDetail">
					<thead>
						<tr>
							<th
								data-options="field:'id',title:'id',checkbox:false,hidden:true,width:200,align:'center'"></th>
							<th
								data-options="field:'processCode',title:'工序代码',checkbox:false,width:100,align:'center'"></th>
							<th
								data-options="field:'processName',title:'工序名称',align:'center'"></th>
							<th
								data-options="field:'deviceCode',title:'设备代码',width:100,align:'center'"></th>
							<th
								data-options="field:'deviceName',title:'设备名称',width:100,align:'center'"></th>
							<th
								data-options="field:'deviceSiteCode',title:'站点代码',width:100,align:'center'"></th>
							<th
								data-options="field:'deviceSiteName',title:'站点名称',width:100,align:'center'"></th>
							<th
								data-options="field:'productionCount',title:'计划数量',width:100,align:'center'"
								editor='numberbox'></th>
							<th
								data-options="field:'completeCount',title:'完工数量',width:100,align:'center'"
								></th>
							<th
								data-options="field:'reportCount',title:'报工数',width:100,align:'center'"
								editor='numberbox'></th>
							<th
								data-options="field:'qualifiedCount',title:'合格数量',width:100,align:'center'"
								></th>
							<th
								data-options="field:'unqualifiedCount',title:'不合格数量',width:100,align:'center'"
								></th>
							<th
								data-options="field:'repairCount',title:'返修数量',width:100,align:'center'"
								></th>
							<th
								data-options="field:'scrapCount',title:'报废数量',width:100,align:'center'"
								></th>
							<th
								data-options="field:'parameterSource',title:'参数取值来源',width:100,align:'center'"></th>
							<th
								data-options="field:'firstReport',title:'首件报告',width:100,align:'center'"></th>
							<th
								data-options="field:'status',title:'状态',width:100,align:'center',formatter:function(value,row,index){
									if(value){
										switch(value){
											case '0': return '计划';
											case '1': return '加工中';
											case '2': return '停机';
											case '3': return '完工';
										}
									}
								}"
								editor='text'></th>
							<th
								data-options="field:'note',title:'备注',width:100,align:'center'"
								editor='text'></th>
						</tr>
					</thead>
				</table>
			</div>
			<div title="条码信息" data-options="id:'tab1',iconCls:'fa fa-th'">
				<table id="barCode"></table>
			</div>
		</div>
	</div>
</div>

<div id="parameterDialog"></div>
<div id="deviceSitesDialog"></div>

<!-- 工具按钮 -->
<div id="workSheetDetail-toolbar" class="topjui-toolbar"
	data-options="grid:{
           type:'datagrid',
           id:'workSheetDetail'
       }">
	<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		data-options="method:'doAjax',
       extend: '#workSheetDetail-toolbar',
       iconCls:'fa fa-trash',
       url:'workSheet/deleteWorkSheetDetail.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'workSheetDetail',param:'id:id,processId:processId,workSheetId:workSheetId'}">删除</a>
</div>