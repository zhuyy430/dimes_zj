<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post" >
		<div title="生产单元信息" data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div style="height:30px"></div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备件名称</label>
						<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
								data-options="required:false" id="name" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备件代码</label>
						<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
								data-options="required:false" id="code" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">规格型号</label>
						<div class="topjui-input-block">
							<input type="text" name="unitType" data-toggle="topjui-textbox"
								data-options="required:false" id="unitType" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">计量单位</label>
						<div class="topjui-input-block">
							<input type="text" name="measurementUnit" data-toggle="topjui-textbox"
								data-options="required:false" id="measurementUnit" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">数量</label>
						<div class="topjui-input-block">
							<input type="text" name="count" data-toggle="topjui-textbox"
								data-options="required:true" id="count">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">耗用日期</label>
						<div class="topjui-input-block">
							<input type="text" name="useDate" data-toggle="topjui-datetimebox"
								data-options="required:true" value="{notices.release_time}" id="useDate">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备注</label>
						<div class="topjui-input-block">
							<input type="text" name="note" data-toggle="topjui-textarea"
								data-options="required:false" id="note">
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>