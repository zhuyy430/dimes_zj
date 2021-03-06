<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
	$(function() {
	//故障类型列表
	$("#typeCode").iCombogrid({
		idField:'code',
		textField:'code',
		url:'projectType/queryProjectTypesByType.do?type=maintenanceItemType',
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
			title : '名称',
			width : 100
		}]],
		onClickRow : function(index, row) {
			$('#typeName').textbox('setValue',row.name);
			$('#typeId').val(row.id);
			$('#typeCode').textbox('setValue',row.code);
			$('#projectCode').iCombogrid("reload",{
				type:'MAINTENANCEITEM',
				projectTypeId:row.id
			});
		}
	});
	
	$('#projectCode').iCombogrid(
			{
				idField : 'code',
				textField : 'name',
				delay : 500,
				mode : 'remote',
				url : 'deviceProject/queryDevicesProjectByProjectTypeId.do?type=MAINTENANCEITEM',
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
					title : '项目名称',
					width : 100
				}, {
					field : 'typeCode',
					title : '类别代码',
					width : 100,
					formatter: function (value, row, index) {  
						if(row.projectType==null||row.projectType==''){
							 return "";
						}else{
							return row.projectType.code;
						}
				    }
				}, {
					field : 'TypeName',
					title : '类别名称',
					width : 100,
					formatter: function (value, row, index) {  
						if(row.projectType==null||row.projectType==''){
							 return "";
						}else{
							return row.projectType.name;
						}
				    }
				}] ],
				onClickRow : function(index, row) {
					$('#projectName').textbox('setValue',row.name);
				}
			});
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
						<label class="topjui-form-label">类别代码</label>
						<div class="topjui-input-block">
								<input type="text" name="typeCode" data-toggle="topjui-textbox"
								data-options="required:false" id="typeCode">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">类别名称</label>
						<div class="topjui-input-block">
								<input type="text" name="typeName" data-toggle="topjui-textbox"
								data-options="required:false" id="typeName" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">项目代码</label>
						<div class="topjui-input-block">
								<input type="text" name="projectCode" data-toggle="topjui-textbox"
								data-options="required:true" id="projectCode" >
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">项目名称</label>
						<div class="topjui-input-block">
								<input id="projectName" data-toggle="topjui-textbox" name="projectName" 
								data-options="required:true">
								<input type="hidden" name="typeId" id="typeId">
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