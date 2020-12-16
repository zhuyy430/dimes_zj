<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post" >
		<div title="生产单元信息" data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div style="height:30px"></div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">控制线UL</label>
						<div class="topjui-input-block">
							<input type="text" name="upLine" data-toggle="topjui-textbox"
								data-options="required:false" id="upLine">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">控制线LL</label>
						<div class="topjui-input-block">
							<input type="text" name="lowLine" data-toggle="topjui-textbox"
								data-options="required:false" id="lowLine">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">标准值</label>
						<div class="topjui-input-block">
							<input type="text" name="standardValue" data-toggle="topjui-textbox"
								data-options="required:false" id="standardValue">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备注</label>
						<div class="topjui-input-block">
							<input type="text" name="note" data-toggle="topjui-textarea"
								data-options="required:false" id="note">
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>