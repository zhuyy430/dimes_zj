<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="损时原因" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">损时原因代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">损时原因</label>
							<div class="topjui-input-block">
								<input type="text" name="reason" data-toggle="topjui-textbox"
									data-options="required:true" id="reason">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">详细描述</label>
							<div class="topjui-input-block">
								<input type="text" name="description"
									data-toggle="topjui-textarea" id="description">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">是否停机</label>
							<div>
								<input name="halt" value="true" checked="checked"	type="radio">是 
								<input name="halt" value="false" type="radio">否
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
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">计划停机</label>
							<div>
								<input name="planHalt" value="true" 
									type="radio"> 是 <input name="planHalt" value="false" checked="checked"	type="radio">否
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>