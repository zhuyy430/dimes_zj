<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="状态故障代码信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">状态/故障代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">状态/故障名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">参数代码</label>
							<div class="topjui-input-block">
								<input type="text" name="parameterCode"
									data-toggle="topjui-textbox" data-options="required:true"
									id="parameterCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">参数名称</label>
							<div class="topjui-input-block">
								<input type="text" name="parameterName"
									data-toggle="topjui-textbox" data-options="required:true"
									id="parameterName">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">条件</label>
							<div class="topjui-input-block">
								<input type="text" id="conditions" name="conditions"
									data-toggle="topjui-combobox"
									data-options="required:false,
                       valueField:'text',
                       textField:'text',
                       data:[{text:'高于上限'},{text:'低于下限'}]">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">描述</label>
							<div class="topjui-input-block">
								<input
									type="text" name="note" data-toggle="topjui-textarea"
									id="note">
								<!-- ID -->
								<input type="hidden" name="id" id="id" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>