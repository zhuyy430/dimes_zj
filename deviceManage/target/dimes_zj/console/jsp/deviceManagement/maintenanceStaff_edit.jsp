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
							<label class="topjui-form-label">员工代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">员工姓名</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">派单顺序</label>
							<div class="topjui-input-block">
								<input type="number" name="queue" data-toggle="topjui-textbox"
									id="queue">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工作状态</label>
							<div class="topjui-input-block">
								<input type="text" name="workStatus" data-toggle="topjui-combobox" 
									id="workStatus" data-options="valueField:'code',textField:'text',
									data:[{code:'ONDUTY',text:'在岗',selected:true},{code:'MAINTENANCE',text:'保养'},
									{code:'MAINTAIN',text:'维修'},{code:'BEOUT',text:'公出'},{code:'REST',text:'休息'}]">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>