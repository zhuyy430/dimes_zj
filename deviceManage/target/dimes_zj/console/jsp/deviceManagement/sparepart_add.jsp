<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(function() {
		$('#deviceTable').iDatagrid({
			url : 'device/queryAllDevices.do?module=deviceManage',
			columns : [ [ {
				field : 'id',
				title : 'id',
				checkbox : true
			}, {
				field : 'code',
				title : '设备代码',
				width : 100
			}, {
				field : 'name',
				title : '设备名称',
				width : 100
			}, {
				field : 'unitType',
				title : '规格型号',
				width : 100
			} ] ]
		/* ,
				    onLoadSuccess:function(data){
				    	$("#deviceTable").iDatagrid("resize");
				    } */});
		//设备搜索点击事件
		$("#deviceCodes").iTextbox({
			prompt : '以逗号间隔设备代码',
			buttonIcon : 'fa fa-search',
			onClickButton : function() {
				$('#showDevicesDialog').iDialog("open");
			}
		});
		$('#showDevicesDialog')
				.iDialog(
						{
							title : '设备',
							width : 700,
							height : 600,
							closed : true,
							cache : false,
							modal : true,
							buttons : [
									{
										text : '保存',
										handler : function() {
											var deviceSites = $('#deviceTable')
													.iDatagrid('getSelections');
											var codesArray = new Array();
											for (var i = 0; i < deviceSites.length; i++) {
												codesArray
														.push(deviceSites[i].code);
											}
											var codesStr = JSON
													.stringify(codesArray);
											codesStr = codesStr
													.replace('[', '');
											codesStr = codesStr
													.replace(']', '');
											codesStr = codesStr.replace(/"/g,
													'');
											$('#deviceCodes').iTextbox(
													'setValue', codesStr);
											$('#showDevicesDialog').iDialog(
													"close");
										}
									},
									{
										text : '关闭',
										handler : function() {
											$('#showDevicesDialog').iDialog(
													"close");
										}
									} ]
						});
	});
</script>
<div id="showDevicesDialog">
	<table id="deviceTable"></table>
</div>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="备件信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">备件代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">备件名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="unitType" data-toggle="topjui-textbox"
									data-options="required:false" id="unitType">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">图号</label>
							<div class="topjui-input-block">
								<input type="text" name="graphNumber"
									data-toggle="topjui-textbox" data-options="required:false"
									id="graphNumber">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">厂商</label>
							<div class="topjui-input-block">
								<input type="text" name="manufacturer"
									data-toggle="topjui-textbox" data-options="required:false"
									id="manufacturer">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">计量单位</label>
							<div class="topjui-input-block">
								<input type="text" name="measurementUnit"
									data-toggle="topjui-textbox" data-options="required:false"
									id="measurementUnit">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">助记码</label>
							<div class="topjui-input-block">
								<input type="text" name="mnemonicCode"
									data-toggle="topjui-textbox" data-options="required:false"
									id="mnemonicCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">安全库存</label>
							<div class="topjui-input-block">
								<input type="text" name="inventory" data-toggle="topjui-textbox"
									data-options="required:false" id="inventory">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>