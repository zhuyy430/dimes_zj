<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addDeviceForm" method="post" enctype="multipart/form-data">
			<div title="设备信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">设备代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">设备名称</label>
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
									id="unitType">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">状态</label>
							<div class="topjui-input-block">
								<input type="text" name="status" id="deviceStatus"
									data-toggle="topjui-combobox"
									data-options="valueField:'value',textField:'text',data: [{text:'运行', value: '0'},{text: '待机',value: '1'},{text: '停机',value: '2'}]">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">制造商</label>
							<div class="topjui-input-block">
								<input type="text" name="manufacturer"
									data-toggle="topjui-textbox" id="manufacturer">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">经销商</label>
							<div class="topjui-input-block">
								<input type="text" name="trader" data-toggle="topjui-textbox"
									id="trader">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">安装日期</label>
							<div class="topjui-input-block">
								<input type="text" name="installDate"
									data-toggle="topjui-datebox" id="installDate">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">出厂日期</label>
							<div class="topjui-input-block">
								<input type="text" name="outFactoryDate"
									data-toggle="topjui-datebox" id="outFactoryDate"
									editable="false">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">出厂编码</label>
							<div class="topjui-input-block">
								<input type="text" name="outFactoryCode"
									data-toggle="topjui-textbox" id="outFactoryCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">安装位置</label>
							<div class="topjui-input-block">
								<input type="text" name="installPosition"
									data-toggle="topjui-textbox" id="installPosition">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">参数取值</label>
							<div class="topjui-input-block">
								<!-- <input type="text" name="parameterValueType" data-toggle="topjui-textbox"
									id="parameterValueType"> -->


								<input name="parameterValueType" id="parameterValueType"
									data-toggle="topjui-combobox"
									data-options="valueField: 'text',
textField: 'text',
data: [{
    text: '固定值',
},{
    text: '变动值',
}]">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">上传图片</label>
							<div class="topjui-input-block">
								<input name="photoName" data-toggle="topjui-uploadbox"
									data-options="editable:false,
									   buttonText:'上传图片',
									   accept:'images',
									   uploadUrl:'device/upload.do'"
									type="text" id="photoName">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>