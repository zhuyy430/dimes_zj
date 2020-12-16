<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="质量记录" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">投诉时间</label>
							<div class="topjui-input-block">
								<input name="currentDate" id="currentDate"
									data-toggle="topjui-datetimebox"
									data-options="required:true,showSeconds:true">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">投诉类别</label>
							<div class="topjui-input-block">
								<input id="typeName" data-toggle="topjui-combobox"
									data-options="
								        valueField: 'name',
								        textField: 'name',
								        url: 'qualityCalendarType/queryAllQualityCalendarTypes.do',
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
								        url: 'qualityGrade/queryAllQualityGrades.do',
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
							<label class="topjui-form-label">客户名称</label>
							<div class="topjui-input-block">
								<input type="text" name="customer" data-toggle="topjui-textbox"
									id="customer">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">投诉内容</label>
							<div class="topjui-input-block">
								<input type="text" name="content" data-toggle="topjui-textarea"
									id="content">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">联系人</label>
							<div class="topjui-input-block">
								<input type="text" name="contacts" data-toggle="topjui-textbox"
									data-options="required:false" id="contacts">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">联系方式</label>
							<div class="topjui-input-block">
								<input type="text" name="tel" data-toggle="topjui-textbox"
									data-options="required:false" id="tel">
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