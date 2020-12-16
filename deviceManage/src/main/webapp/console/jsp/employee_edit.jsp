<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(function() {
		$("#positionName").iCombobox({
			url : "position/queryPositions.do",
			valueField : 'id',
			textField : 'name',
			queryParams : {
				deptid : $("#departmentTg").iTreegrid("getSelected").id
			},
			onSelect : function(rec) {
				$("#positionId").val(rec.id);
			}
		});
		$("#departmentName").iCombobox({
			url : "department/queryAllDepartments.do",
			valueField : 'id',
			textField : 'name',
			onSelect : function(rec) {
				$("#departmentId").val(rec.id);
			}
		});
	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="员工信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">员工代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" readonly="readonly"
									data-toggle="topjui-textbox" data-options="required:true"
									id="employeeCode_edit">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">员工名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="employeeName_edit">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">员工IC号</label>
							<div class="topjui-input-block">
								<input type="text" name="icno" data-toggle="topjui-textbox" id="ICNo_edit">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">联系方式</label>
							<div class="topjui-input-block">
								<input type="text" name="tel" data-toggle="topjui-textbox" id="tel">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm8">
							<label class="topjui-form-label">岗位</label>
							<div class="topjui-input-block">
								<input type="text" id="positionName" name="positionName">

								<input type="hidden" name="positionId" id="positionId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm8">
							<label class="topjui-form-label">部门</label>
							<div class="topjui-input-block">
								<input type="text" id="departmentName" name="departmentName">

								<input type="hidden" name="departmentId" id="departmentId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm8">
							<label class="topjui-form-label">生产单元</label>
							<div class="topjui-input-block">
								<input type="text" id="productionUnitName"
									name="productionUnitName" data-toggle="topjui-combotree"
									data-options="required:false,
                       valueField:'id',
                       textField:'name',
                       url:'productionUnit/queryProductionUnitSiteTree.do',
                       onSelect: function(rec){
                       	$('#productionUnitId').val(rec.id);
                       }
                       ">
								<input type="hidden" name="productionUnitId"
									id="productionUnitId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">入职日期</label>
							<div class="topjui-input-block">
								<input type="text" name="inDate"
									data-toggle="topjui-datebox" id="inDate">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">描述</label>
							<div class="topjui-input-block">
								<input type="hidden" id="departmentId" name="departmentId" /> <input
									type="text" name="note" data-toggle="topjui-textarea"
									id="employeeNote_edit">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">上传头像</label>
							<div class="topjui-input-block">
								<input name="photo" data-toggle="topjui-uploadbox"
									data-options="editable:false,
									   buttonText:'上传图片',
									   accept:'images',
									   uploadUrl:'employee/upload.do'"
									type="text" id="photo">
							</div>
						</div>
					</div>
				</div>
			</div>

		</form>
	</div>
</div>