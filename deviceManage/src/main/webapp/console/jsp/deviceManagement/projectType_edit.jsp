<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		
		<form id="ff" method="post" >
		<div title="编辑项目维修类别" data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div style="height:30px"></div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">类别代码</label>
						<div class="topjui-input-block">
							<input type="text" name="code" readonly="readonly" data-toggle="topjui-textbox"
								data-options="required:true" id="typeCode_edit">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">类别名称</label>
						<div class="topjui-input-block">
							<input type="text" name="name" data-toggle="topjui-textbox"
								data-options="required:true" id="typeName_edit">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备注</label>
						<div class="topjui-input-block">
						<input type="text" name="note" 
								data-toggle="topjui-textbox" id="typeNote_edit">
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>