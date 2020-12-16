<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		
		<form id="ff" method="post" >
		<div title="参数信息" data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div style="height:30px"></div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">参数代码</label>
						<div class="topjui-input-block">
							<input type="text" name="code" data-toggle="topjui-textbox"
								data-options="required:true" id="code" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">参数名称</label>
						<div class="topjui-input-block">
							<input type="text" name="name" data-toggle="topjui-textbox"
								data-options="required:true" id="name">
						</div>
					</div>
				</div>
				<!-- <div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">kfc</label>
							<div>
								<input type="radio" name="kfc" id="kfcY" value="true">是 <input
									type="radio" name="kfc" id="kfcN" value="false" checked="checked">否
							</div>
						</div>
					</div>  -->
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">kfc</label>
							<div class="topjui-input-block">
								<input data-toggle="topjui-combobox" name="kfc" id="kfc"
									data-options="
									valueField: 'value',
									textField: 'label',
									data: [{
									    label: '是',
									    value: 'true'
									},{
									    label: '否',
									    value: 'false'
									}]">
							</div>
						</div>
					</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备注</label>
						<div class="topjui-input-block">
							<input type="text" name="note"
								data-toggle="topjui-textarea" id="note">
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>