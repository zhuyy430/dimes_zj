<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="设备等级信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">设备等级代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">设备等级名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">设备等级权重</label>
							<div class="topjui-input-block">
								<input type="text" name="weight" data-toggle="topjui-numberbox" 
									data-options="required:true,min:0,precision:0" id="weight" value="10">
									<span style="color:red;">权重值为正整数，值越小，优先级越高!</span>
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