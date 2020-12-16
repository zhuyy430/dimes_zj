<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="设备类别信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">项目编号</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">项目名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm8">
							<label class="topjui-form-label">设备类别</label>
							<div class="topjui-input-block">
								<input type="text" id="deviceTypeName"  name="deviceTypeName" data-toggle="topjui-combobox"
								data-options="required:false,
                       valueField:'id',
                       textField:'name',
                       url:'projectType/queryProjectTypesByType.do?type=deviceType',
                       onSelect: function(rec){
                       	$('#deviceTypeId').val(rec.id);
                       }
                       ">
                       
                       <input type="hidden" name="deviceTypeId" id="deviceTypeId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">标准</label>
							<div class="topjui-input-block">
								<input type="text" name="standard" data-toggle="topjui-textbox"
									   id="standard">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">方法</label>
							<div class="topjui-input-block">
								<input type="text" name="method" data-toggle="topjui-textbox"
									   id="method">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">频次</label>
							<div class="topjui-input-block">
								<input type="text" name="frequency" data-toggle="topjui-textbox"
									   id="frequency">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">说明</label>
							<div class="topjui-input-block">
								<input type="text" name="note" data-toggle="topjui-textarea"
									   id="note">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>