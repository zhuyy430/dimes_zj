<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="明细信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">序号</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">状态</label>
							<div class="topjui-input-block">
								<input type="text" name="status" data-toggle="topjui-combobox"
									   data-options="
											valueField: 'text',
											textField: 'text',
											data: [{
												text: '在库',selected:'true'
											},{
												text: '已关联'
											},{
												text: '报废'
											}]"
									   id="status">
							</div>
						</div>
					</div>

					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">计量累计</label>
							<div class="topjui-input-block">
								<input type="text" name="cumulation"
									data-toggle="topjui-numberbox" data-options="required:false"
									id="cumulation">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">计量差异</label>
							<div class="topjui-input-block">
								<input type="text" name="measurementDifference"
									data-toggle="topjui-numberbox" data-options="required:false"
									id="measurementDifference">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">质保期</label>
							<div class="topjui-input-block">
								<input type="text" name="warrantyPeriod"
									data-toggle="topjui-numberbox" data-options="required:false"
									id="warrantyPeriod">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">上传图片</label>
							<div class="topjui-input-block">
								<input name="picName" data-toggle="topjui-uploadbox"
									data-options="editable:false,
   buttonText:'上传图片',
   accept:'images',
   uploadUrl:'equipment/upload.do'"
									type="text" id="picName">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">备注</label>
							<div class="topjui-input-block">
								<input type="text" name="note"
									   data-toggle="topjui-textarea" id="note">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>