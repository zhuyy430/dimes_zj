<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="生产单元信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">日期</label>
							<div class="topjui-input-block">
								<input name="date" data-toggle="topjui-datebox"  value="{notices.release_time}" data-options="required:true,width:200,showSeconds:true"
								id="date" >
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">班次</label>
							<div class="topjui-input-block">
								<input id="classesName"
									data-toggle="topjui-combobox" name="classesName"
									data-options="required:true,valueField:'id',textField:'name',
								url:'classes/queryAllClasses.do',
								onSelect: function(rec){
								$('classesId').val(rec.id);
								} 
								">
								<input type="hidden" name="classesId">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">运行时间</label>
							<div class="topjui-input-block">
								<input type="text"  data-toggle="topjui-numberbox" data-options="required:true,min:0,precision:1" name="runTime" id="runTime" >
							</div>
						</div>
					</div>
					<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">故障停机时间</label>
						<div class="topjui-input-block">
							<input type="text"  data-toggle="topjui-numberbox" data-options="required:true,min:0,precision:1" name="ngTime" id="ngTime" >
						</div>
					</div>
				</div>
				</div>
			</div>

		</form>
	</div>
</div>