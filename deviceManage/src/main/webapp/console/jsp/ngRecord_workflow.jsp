<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="生产单元信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">处理意见:</label>
							<div class="topjui-input-block">
								<input name="suggestion" 
								style="height:150px;" data-toggle="topjui-textarea" data-options="required:false"
								id="suggestion">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>