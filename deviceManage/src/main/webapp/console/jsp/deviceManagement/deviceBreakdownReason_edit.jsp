<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
	label{text-align: center;}	
</style>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addDeviceForm" method="post">
			<div title="故障原因-新增" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
				<div style="height:20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">故障原因编码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">故障原因名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">说明</label>
							<div class="topjui-input-block">
								<input type="text" name="note"
									data-toggle="topjui-textbox" 
									id="note">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">处理方法</label>
							<div class="topjui-input-block">
								<input id="method" type="text" name="method" data-toggle="topjui-textbox" >
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">备注</label>
							<div class="topjui-input-block">
								<input type="text" name="remark" data-toggle="topjui-textbox"
									data-options="required:false,multiline:true,height:80" id="remark">
							</div>
						</div>
					</div> 
					
				</div>
			</div>
		</form>
	</div>
</div>
<div id="parameterDialog"></div>
<div id="deviceSitesDialog"></div>
<!-- 工具按钮 -->
<div id="workSheetDetail-toolbar" class="topjui-toolbar"
	data-options="grid:{
           type:'datagrid',
           id:'workSheetDetail'
       }">
	<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		data-options="method:'doAjax',
       extend: '#workSheetDetail-toolbar',
       iconCls:'fa fa-trash',
       url:'workSheet/deleteWorkSheetDetailInMemory.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'workSheetDetail',param:'id:id'}">删除</a>
</div>