<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
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
						<label class="topjui-form-label">保养类别</label>
						<div class="topjui-input-block">
								<input id="recordTypeName" data-toggle="topjui-textbox" name="recordTypeName" 
								data-options="required:true" readonly="readonly">
								<input type="hidden" name="pressLightId" id="pressLightId">
								<input type="hidden" name="id" id="id">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">保养项目</label>
						<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
								data-options="required:false" id="name" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">标准</label>
						<div class="topjui-input-block">
							<input name="standard" data-toggle="topjui-textarea" data-options="required:false"
								id="standard" >
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">方法</label>
						<div class="topjui-input-block">
							<input name="method" data-toggle="topjui-textarea" data-options="required:false"
								id="method" >
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">频次</label>
						<div class="topjui-input-block">
							<input name="frequency" data-toggle="topjui-textarea" data-options="required:false"
								id="frequency" >
						</div>
					</div>
				</div>
				<div class="topjui-row">
		            <div class="topjui-col-sm12">
		                <label class="topjui-form-label">结果</label>
		                <div class="topjui-input-block" style="padding-top: 4px;">
		                    <input name="result" type="radio" value="OK" checked="checked"/>OK
							<input name="result" type="radio" value="NG"/>NG
		                </div>
		            </div>
		        </div>

				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备注</label>
						<div class="topjui-input-block">
							<input name="Remarks" data-toggle="topjui-textarea" data-options="required:false"
								id="Remarks" >
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>