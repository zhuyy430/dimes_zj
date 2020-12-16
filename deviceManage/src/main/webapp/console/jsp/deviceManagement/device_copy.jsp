<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
	label{text-align: center;}	
</style>
<script>
	$(function(){
		var now = new Date();
		var dateTime = now.toLocaleString('chinese', { hour12: false });;	
		$("#mappingDate").val(dateTime);
		$('#projectTypeId').iCombogrid({
		    idField:'id',
		    textField:'name',
		    delay: 500,
		    mode: 'remote',
		    url:'projectType/queryProjectTypesByType.do?type=deviceType',
		    columns:[[
		        {field:'id',title:'id',width:60,hidden:true},
		        {field:'code',title:'代码',width:100},
		        {field:'name',title:'名称',width:100}
		    ]]
		});
		$("#code").val("");
	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addDeviceForm" method="post">
			<div title="设备管理-编辑" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
				<div style="height:20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">设备代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
						
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">设备名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
								
								<!-- <input type="text" name="name" style="width: 100%;"
									data-toggle="topjui-datetimebox"
									data-options="required:true,showSeconds:true"
									id="manufactureDate"> -->
							</div>
						</div>
						</div>
						<div class="topjui-row">
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="unitType"
									data-toggle="topjui-textbox" 
									id="unitType">
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">设备类别</label>
							<div class="topjui-input-block">
								<!-- <input id="deviceType" type="text" name="deviceType" data-toggle="topjui-combobox" value=""
									data-options="
										valueField: 'id',
										textField: 'name',
										url: 'projectType/queryAllProjectType.do?type=deviceType'
										"> -->
								 <input id="projectTypeId" name="projectTypeId" data-toggle="topjui-combogrid" data-options="required:true">
							</div>
						</div>
					</div>
					<div class="topjui-row">

						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">出厂编号</label>
							<div class="topjui-input-block">
								<input type="text" name="outFactoryCode" data-toggle="topjui-textbox"
									data-options="required:false" id="outFactoryCode">
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">生产厂家</label>
							<div class="topjui-input-block">
								<input type="text" name="manufacturer"
									data-toggle="topjui-textbox" data-options="required:false"
									id="manufacturer" >
							</div>
						</div>
						</div>
						<div class="topjui-row">
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">经销商</label>
							<div class="topjui-input-block">
								<input type="text" name="trader"
									data-toggle="topjui-textbox" data-options="required:false"
									id="trader" >
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">设备状态</label>
							<div class="topjui-input-block">
								<input id="status" type="text" name="status" data-toggle="topjui-combobox" value="0"
									data-options="
										valueField: 'value',
										textField: 'text',
										data: [{
										    value: '0',
										    text: '正常运行'
										},{
										    value: 'fault',
										    text: '故障维修'
										},{
										    value: '2',
										    text: '停机'
										},{
										    value: 'discontinuation',
										    text: '停用'
										},{
										    value: 'sellout',
										    text: '卖出'
										}]">
							</div>
						</div>

					</div>
					<div class="topjui-row">

						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">出厂日期</label>
							<div class="topjui-input-block">
								<input type="text" name="outFactoryDate"
									data-toggle="topjui-datebox" data-options="required:false"
									id="outFactoryDate">
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">到厂日期</label>
							<div class="topjui-input-block">
								<input type="text" name="inFactoryDate"
									data-toggle="topjui-datebox" data-options="required:false"
									id="inFactoryDate">
							</div>
						</div>
						</div>
						<div class="topjui-row">
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">安装日期</label>
							<div class="topjui-input-block">
								<input type="text" name="installDate"
									data-toggle="topjui-datebox" data-options="required:false"
									id="installDate">
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">验收日期</label>
							<div class="topjui-input-block">
								<input type="text" name="checkDate"
									data-toggle="topjui-datebox" data-options="required:false"
									id="checkDate"> 
							</div>
						</div>
					</div>
					<div class="topjui-row">

						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">资产编号</label>
							<div class="topjui-input-block">
								<input type="text" name="assetNumber"
									data-toggle="topjui-textbox" data-options="required:false"
									id="assetNumber">
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">设备重量</label>
							<div class="topjui-input-block">
								<input type="text" name="weight"
									data-toggle="topjui-textbox" data-options="required:false"
									id="weight">
							</div>
						</div>
						</div>
						<div class="topjui-row">
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">外形尺寸</label>
							<div class="topjui-input-block">
								<input type="text" name="shapeSize"
									data-toggle="topjui-textbox" data-options="required:false"
									id="shapeSize">
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">安装位置</label>
							<div class="topjui-input-block">
								<input type="text" name="installPosition"
									data-toggle="topjui-textbox" data-options="required:false"
									id="installPosition"> 
							</div>
						</div>
					</div>
					<div class="topjui-row">
					<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">单台功率</label>
							<div class="topjui-input-block">
								<input type="text" name="power" data-toggle="topjui-textbox"
									data-options="required:false" id="power">
							</div>
						</div>
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">实际功率</label>
							<div class="topjui-input-block">
								<input type="text" name="actualPower" data-toggle="topjui-textbox"
									data-options="required:false" id="actualPower">
							</div>
						</div>
						</div>
					<div class="topjui-row">
						<!-- <div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">生产单元</label>
							<div class="topjui-input-block">
								<input name="productionId" data-toggle="topjui-treegrid"
									data-options="required:true" id="productionId">
							</div>
						</div> -->
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">生产单元</label>
							<div class="topjui-input-block">
									<input data-toggle="topjui-combotreegrid"
									name="productionUnitName" id="productionUnitName"
									 data-options="width:'100%',required:true,
							        panelWidth:500,
							        url:'',
							        idField:'id',
							        treeField:'name',
							        url:'productionUnit/queryTopProductionUnits.do',
							        columns:[[
							            {field:'id',title:'id',hidden:true},
							            {field:'code',title:'编码',width:200},
							            {field:'name',title:'单元名称',width:200}
							        ]],
							        onSelect: function(rec){
           								 $('#productionUnitId').val(rec.id);
        							}"  
        							>
								<input type="hidden" name="productionUnitId"
									id="productionUnitId"> 
								<input type="hidden" name="productionUnitCode"
									id="productionUnitCode"> 
									<!-- <input type="text"
									name="productionUnitName" id="productionUnitName"
									data-toggle="topjui-textbox" readonly="readonly"> -->
							</div>
					</div>
						
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">备注</label>
							<div class="topjui-input-block">
								<input type="text" name="note" data-toggle="topjui-textbox"
									data-options="required:false,multiline:true,height:80" id="note">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>


<!-- 	<div data-options="region:'south',fit:false,split:true,border:false"
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
								data-options="field:'deviceSiteCode',title:'*站点代码',width:100,align:'center'"></th>
							<th
								data-options="field:'deviceSiteName',title:'*站点名称',width:100,align:'center'"></th>
							<th
								data-options="field:'productionCount',title:'计划数量',width:100,align:'center'" editor='numberbox'></th>
							<th
								data-options="field:'completeCount',title:'完工数量',width:100,align:'center'" ></th>
							<th
								data-options="field:'reportCount',title:'报工数',width:100,align:'center'" editor='numberbox'></th>
							<th
								data-options="field:'qualifiedCount',title:'合格数量',width:100,align:'center'" ></th>
							<th
								data-options="field:'unqualifiedCount',title:'不合格数量',width:100,align:'center'" ></th>
							<th
								data-options="field:'repairCount',title:'返修数量',width:100,align:'center'"></th>
							<th data-options="field:'scrapCount',title:'报废数量',width:100,align:'center'"></th>
							<th
								data-options="field:'parameterSource',title:'参数取值来源',width:100,align:'center'"></th>
							<th
								data-options="field:'firstReport',title:'*首件报告',width:100,align:'center'"></th>
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
								}" editor='text'></th>
							<th
								data-options="field:'note',title:'备注',width:100,align:'center'" editor='text'></th>
						</tr>
					</thead>
				</table>
			</div>
			<div title="条码信息" data-options="id:'tab1',iconCls:'fa fa-th'">
				<div title="条码信息" data-options="id:'tab1',iconCls:'fa fa-th'">
				<table id="barCode"></table>
			</div>
			</div>
		</div>
	</div>
 --></div>
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
       url:'workSheet/deleteWorkSheetDetailInMemory.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'workSheetDetail',param:'id:id'}">删除</a>
</div>