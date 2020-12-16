<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="用户信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">用户名</label>
							<div class="topjui-input-block">
								<input type="text" name="username" data-toggle="topjui-textbox"
									data-options="required:true" id="username">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">关联员工</label>
							<div class="topjui-input-block">
								<input id="employee" name="employee" type="text" data-toggle="topjui-combogrid"
									data-options="width:250,
					           panelWidth:250,
					           idField:'code',
					           textField:'name',
					           url:'employee/queryAllEmployees.do',
					           columns:[[
					               {field:'id',title:'id',width:100,hidden:true},
					               {field:'code',title:'Code',width:100},
					               {field:'name',title:'Name',width:100}
					           ]]" />
							</div>
						</div>
					</div>
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