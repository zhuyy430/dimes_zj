<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
	label{text-align: center;}	
</style>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addDeviceForm" method="post">
			<div title="故障类型-新增" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
				<div style="height:20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">类别编号</label>
							<div class="topjui-input-block">
								<input type="text" name="typeCode" data-toggle="topjui-textbox"
									data-options="required:true" id="typeCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">类别名称</label>
							<div class="topjui-input-block">
								<input type="text" name="typeName" data-toggle="topjui-textbox"
									data-options="required:true" id="typeName">
								
								<!-- <input type="text" name="name" style="width: 100%;"
									data-toggle="topjui-datetimebox"
									data-options="required:true,showSeconds:true"
									id="manufactureDate"> -->
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">备注</label>
							<div class="topjui-input-block">
								<input type="text" name="typeNote"
									data-toggle="topjui-textbox" 
									id="typeNote">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">停用</label>
							<div class="topjui-input-block">
								<input id="typeDisabled" type="text" name="typeDisabled" data-toggle="topjui-combobox" value="0"
									data-options="
										valueField: 'value',
										textField: 'text',
										data: [{
										    value: '1',
										    text: '是'
										},{
										    value: '0',
										    text: '否'
										}]">
							</div>
						</div>
					</div>
					<!-- <div class="topjui-row">

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
						</div> -->
						<!-- <div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">备注</label>
							<div class="topjui-input-block">
								<input type="text" name="note" data-toggle="topjui-textbox"
									data-options="required:false,multiline:true,height:80" id="note">
							</div>
						</div>
						</div> -->
				</div>
			</div>
		</form>
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
       url:'workSheet/deleteWorkSheetDetailInMemory.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'workSheetDetail',param:'id:id'}">删除</a>
</div>