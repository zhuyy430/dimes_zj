<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		
		<form id="ff" method="post" >
		<div title="不合格品处理信息 " data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">处理方法</label>
						<div class="topjui-input-block">
							<input type="text" name="processMethod" readonly="readonly" data-toggle="topjui-textbox"
								data-options="required:true" id="processMethod">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">数量</label>
						<div class="topjui-input-block">
							<input type="text" name="ngCount" data-toggle="topjui-numberbox"
								data-options="required:true" id="ngCount">
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>