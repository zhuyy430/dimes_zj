<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="工件二维码规则" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工件代码</label>
							<div class="topjui-input-block">
								<input type="text" id="workpieceCode"  data-toggle="topjui-combobox"
								data-options="required:true,
			                       valueField:'code',
			                       textField:'code',
			                       url:'workpiece/queryWorkpieces.do',
			                       onSelect:function(rec){
			                       		$('#workpieceName').iTextbox('setValue',rec.name);
			                       		$('#customerGraphNumber').iTextbox('setValue',rec.customerGraphNumber);
			                       		$('#version').iTextbox('setValue',rec.version);
			                       		$('#workpieceId').val(rec.id);
			                       }" name="workpieceCode">
			                       <input type="hidden" id="workpieceId" name="workpieceIds">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工件名称</label>
							<div class="topjui-input-block">
								<input type="text" name="workpieceName" data-toggle="topjui-textbox"
									id="workpieceName" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">客户图号</label>
							<div class="topjui-input-block">
								<input type="text" name="customerGraphNumber" data-toggle="topjui-textbox" readonly="readonly"
									id="customerGraphNumber">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">版本号</label>
							<div class="topjui-input-block">
								<input type="text" name="version" data-toggle="topjui-textbox" readonly="readonly"
									id="version">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">厂商代码</label>
							<div class="topjui-input-block">
								<input type="text" name="manufacturerCode" data-toggle="topjui-textbox" data-options="required:true"
									id="manufacturerCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">日期</label>
							<div class="topjui-input-block">
								<input type="text" name="generateDate" data-toggle="topjui-textbox" data-options="required:true"
									id="generateDate">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">流水号</label>
							<div class="topjui-input-block">
								<input type="text" name="serNum" data-toggle="topjui-textbox" data-options="required:true"
									id="serNum">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">打印机IP</label>
							<div class="topjui-input-block">
								<input type="text" name="printerIp" data-toggle="topjui-textbox" data-options="required:true"
									id="printerIp" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">登录用户:</label>
							<div class="topjui-input-block">
								<input type="text" name="remoteUser" data-toggle="topjui-textbox"
									id="remoteUser">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">登录密码:</label>
							<div class="topjui-input-block">
								<input type="password" name="remotePass" data-toggle="topjui-textbox" 
									id="remotePass">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">共享目录名称:</label>
							<div class="topjui-input-block">
								<input type="text" name="sharedDir" data-toggle="topjui-textbox" data-options="required:true"
									id="sharedDir">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>