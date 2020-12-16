<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<style>
	label{text-align: center;}	
</style>
<script>
	$(function(){
		var now = new Date();
		var dateTime = now.toLocaleString('chinese', { hour12: false });;	
		$("#mappingDate").val(dateTime);
	/* 	$('#name').iCombogrid({
		    idField:'id',
		    textField:'name',
		    delay: 500,
		    mode: 'remote',
		    url:'deviceProject/queryDeviceProjectByType.do',
		    columns:[[
		        {field:'id',title:'id',width:60,hidden:true},
		        {field:'code',title:'代码',width:100},
		        {field:'name',title:'名称',width:100}
		    ]],
		    queryParams : {
				type : 'MAINTAIN',
				deviceId :$('#deviceDg').iDatagrid('getSelected').id
			}
		}); */
	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addDeviceForm" method="post">
			<div title="保养项目-编辑" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
				<div style="height:20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">保养项目</label>
							<div class="topjui-input-block">
							    <input id="name" name="name" data-toggle="topjui-textbox" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">保养类别</label>
							<div class="topjui-input-block">
							    <input id="recordTypeName" data-toggle="topjui-textbox" readonly="readonly"
									name="recordTypeName">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">标准</label>
							<div class="topjui-input-block">
								<input type="text" name="standard" data-toggle="topjui-textbox"
									 id="standard">
							</div>
						</div>
					</div>
						
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">方法</label>
							<div class="topjui-input-block">
								<input type="text" name="method"
									data-toggle="topjui-textbox" 
									id="method">
							</div>
						</div>
					</div>
					
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label" style="text-align: center;">频次</label>
							<div class="topjui-input-block">
								<input type="text" name="frequency" data-toggle="topjui-textbox"
									 id="frequency">
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