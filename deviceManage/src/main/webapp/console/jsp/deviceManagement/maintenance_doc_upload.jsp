<%@ page language="java" 
	pageEncoding="UTF-8"%>
<script>
	$(function() {
		$.ajax({
          	type : "post",
	            url : contextPath + "maintenancePlanRecord/queryDocTypeByMaintenancePlanRecordId.do",
	            data : {"maintenancePlanRecordId":$("#deviceDg").iDatagrid("getSelected").id,},
				cache:false,
	            dataType : "json",
	            success : function(data) {
					if(data){
						$('#docType').textbox('setValue',data.name);
						$('#docTypeId').val(data.id);
					}
	            }
	        })
	});
</script>
<div id="showDevicesDialog">
		<div data-toggle="topjui-layout" data-options="fit:true">
			<div
				data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
				<table id="deviceTable"></table>
			</div>
		</div>
	</div>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="uploadDocForm" method="post" enctype="multipart/form-data">
			<div title="文档上传" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 20px;"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">文档类别</label>
							<div class="topjui-input-block">
								<input type="text" data-toggle="topjui-textbox" name="docType" id="docType" readonly="readonly">
								<input type="hidden"  name="docTypeId" id="docTypeId">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">上传文件</label>
							<div class="topjui-input-block">
								<input type="file" name="file"  id="file">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">文档名称</label>
							<div class="topjui-input-block">
								<input type="text" data-toggle="topjui-textbox" name="name" id="name">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label" style="text-align: center;">文档说明</label>
							<div class="topjui-input-block">
								<input type="text" data-toggle="topjui-textarea" name="note" id="note">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>
