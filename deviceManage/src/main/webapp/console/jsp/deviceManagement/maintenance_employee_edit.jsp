<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="showDevicesDialog">
		<div data-toggle="topjui-layout" data-options="fit:true">
			<div
				data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
				<table id="deviceTable"></table>
			</div>
		</div>
	</div>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addDeviceForm" method="post">
			<div title="新增保养人员" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">派单类型</label>
							<div class="topjui-input-block">
								<input type="text" name="orderType" data-toggle="topjui-combobox"
									data-options="required:true,valueField:'text',textField:'text',
									data:[{text:'人工派单',selected:true},{text:'协助'}]" id="orderType">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm6">
							<label class="topjui-form-label" style="text-align: center;">人员</label>
							<div class="topjui-input-block">
								<input id="code" name="code" type="text" data-toggle="topjui-combogrid"
									data-options="width:435,
					           panelWidth:435,
					           idField:'code',
					           textField:'name',
					           url:'employee/queryAllEmployees.do',
					           columns:[[
					               {field:'id',title:'id',width:100,hidden:true},
					               {field:'code',title:'代码',width:200},
					               {field:'name',title:'名称',width:200}
					           ]]" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">占用工时(分)</label>
						<div class="topjui-input-block">
								<input type="text" name="occupyTime" data-toggle="topjui-textbox"
								data-options="required:false" id="occupyTime">
						</div>
					</div>
				</div>
				</div>
			</div>
		</form>
	</div>
</div>
