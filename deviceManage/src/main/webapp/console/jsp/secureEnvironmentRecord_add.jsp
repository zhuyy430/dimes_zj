<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="安环记录" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">时间</label>
							<div class="topjui-input-block">
								<input name="currentDate" id="currentDate"
									data-toggle="topjui-datetimebox"
									data-options="required:true,showSeconds:true,editable:false">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">类别</label>
							<div class="topjui-input-block">
								<input id="typeName" data-toggle="topjui-combobox"
									data-options="
								        valueField: 'name',
								        textField: 'name',
								        url: 'secureEnvironmentType/queryAllSecureEnvironmentTypes.do',
								        onSelect: function(rec){
								        	$('#typeId').val(rec.id);
								        }"
									name="typeName"> <input type="hidden" name="typeId"
									id="typeId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">等级</label>
							<div class="topjui-input-block">
								<input id="gradeName" data-toggle="topjui-combobox"
									data-options="
								        valueField: 'name',
								        textField: 'name',
								        url: 'secureEnvironmentGrade/queryAllSecureEnvironmentGrades.do',
								        onSelect: function(rec){
								        	$('#gradeId').val(rec.id);
								        }"
									name="gradeName"> <input type="hidden" name="gradeId"
									id="gradeId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">说明</label>
							<div class="topjui-input-block">
								<input type="text" name="description" data-toggle="topjui-textarea"
									id="description">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>