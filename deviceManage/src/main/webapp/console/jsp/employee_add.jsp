<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script type="text/javascript">
		$(function(){
			$("#inDate").val(getDate(new Date()));
		});
	</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="员工信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">员工代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="employeeCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">员工名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="employeeName">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">员工IC号</label>
							<div class="topjui-input-block">
								<input type="text" name="ICNo" data-toggle="topjui-textbox"
									 id="ICNo">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">联系方式</label>
							<div class="topjui-input-block">
								<input type="text" name="tel" data-toggle="topjui-textbox" id="tel" data-options="required:true">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm8">
							<label class="topjui-form-label">岗位</label>
							<div class="topjui-input-block">
								<input type="text" id="employeePosition"  data-toggle="topjui-combobox"
								data-options="required:false,
                       valueField:'id',
                       textField:'name',
                       url:'position/queryPositions.do?deptid={departmentId}'">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm8">
							<label class="topjui-form-label">生产单元</label>
								<div class="topjui-input-block">
								<input type="text" id="productionUnitName"  name="productionUnitName" data-toggle="topjui-combotree"
								data-options="required:false,
                       valueField:'id',
                       textField:'name',
                       url:'productionUnit/queryProductionUnitSiteTree.do',
                       onSelect: function(rec){
                       	$('#productionUnitId').val(rec.id);
                       }
                       ">
                       <input type="hidden" name="productionUnitId" id="productionUnitId" />
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
								<input type="hidden" id="departmentId" name="departmentId"/>
								<input type="text" name="description"
									data-toggle="topjui-textarea" id="employeeNote">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">上传头像</label>
							<div class="topjui-input-block">
								<input name="employeePhoto" data-toggle="topjui-uploadbox"
									data-options="editable:false,
							   buttonText:'上传图片',
							   accept:'images',
							   uploadUrl:'employee/upload.do'"
									type="text" id="employeePhoto">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>

