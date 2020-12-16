<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="工序信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工序代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工序名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
							</div>
						</div>
					</div>
					<!-- <div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工序类别</label>
							<div class="topjui-input-block">
								<input id="processTypeName" name="processTypeName"
									data-toggle="topjui-combobox"
									data-options="
							        valueField: 'name',
							        textField: 'name',
							        editable:true,
							        url: 'processes/queryAllProcessTypes.do'">
							</div>
						</div>
					</div> -->
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">备注</label>
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