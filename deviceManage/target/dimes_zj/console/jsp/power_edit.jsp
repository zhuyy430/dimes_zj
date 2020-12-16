<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script type="text/javascript">
$(function(){
$('#group').combobox({ url: 'power//queryAllGroup.do',
	editable:true,
	loadFilter: function (d) { return $(d).map(function () { return { group : this } }).get() }, valueField: 'group', textField: 'group' }); 
});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="角色信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">权限码</label>
							<div class="topjui-input-block">
								<input type="text" name="powerCode" data-toggle="topjui-textbox"
									data-options="required:true" id="powerCode" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">权限名</label>
							<div class="topjui-input-block">
								<input type="text" name="powerName" data-toggle="topjui-textbox"
									data-options="required:true" id="powerName">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">所属模块</label>
							<div class="topjui-input-block">
								<input id="group" name="group">
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