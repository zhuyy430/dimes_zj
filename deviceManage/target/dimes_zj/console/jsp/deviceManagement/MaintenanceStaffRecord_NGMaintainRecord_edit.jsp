<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
	$(function() {
	//故障类型列表
	/* $('#reason').iCombogrid(
			{
				idField : 'code',
				textField : 'name',
				delay : 500,
				mode : 'remote',
				url : 'projectType/queryProjectTypesByType.do?type=breakdownReasonType',
				columns : [ [ {
					field : 'id',
					title : 'id',
					width : 60,
					hidden : true
				}, {
					field : 'code',
					title : '代码',
					width : 100
				}, {
					field : 'name',
					title : '故障类型',
					width : 100
				}] ],
				onClickRow : function(index, row) {
					$("#pressLightId").val(row.id);
					$('#code').textbox('setValue',row.code);
				}
			}); */
	});
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post" >
		<div title="新增维修人员" data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div style="height:30px"></div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">故障类型</label>
						<div class="topjui-input-block">
								<input id="reason" data-toggle="topjui-textbox" name="reason" 
								data-options="required:true" readonly="readonly">
								<input type="hidden" name="pressLightId" id="pressLightId">
								<input type="hidden" name="id" id="id">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">故障代码</label>
						<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
								data-options="required:false" id="code" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">说明</label>
						<div class="topjui-input-block">
							<input name="note" data-toggle="topjui-textarea" data-options="required:false"
								id="note" >
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">处理方法</label>
						<div class="topjui-input-block">
							<input name="processingMethod" data-toggle="topjui-textarea" data-options="required:false"
								id="processingMethod" >
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备注</label>
						<div class="topjui-input-block">
							<input name="remark" data-toggle="topjui-textarea" data-options="required:false"
								id="remark" >
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>