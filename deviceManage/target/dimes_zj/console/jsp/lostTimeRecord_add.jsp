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
							<label class="topjui-form-label">开始时间</label>
							<div class="topjui-input-block">
								<input name="beginTime" data-toggle="topjui-datetimebox" data-options="required:true,showSeconds:true"
								id="beginTime">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">结束时间</label>
							<div class="topjui-input-block">
								<input name="endTime" data-toggle="topjui-datetimebox" data-options="required:true,showSeconds:true"
								id="endTime">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">损时类型</label>
							<div class="topjui-input-block">
								<input id="lostTimeTypeName" data-toggle="topjui-combobox"
									name="lostTimeTypeName"
									data-options="valueField:'name',textField:'name',
								url:'pressLightType/queryFirstLevelType.do?type=PRESSLIGHTTYPE', 
								onSelect: function(rec){
					            var url = 'pressLight/queryAllPressLightByTypeId.do?typeId='+rec.id;
					            $('#reason').iCombobox('reload', url);
				      		  }">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">损时原因</label>
							<div class="topjui-input-block">
								<input id="reason" data-toggle="topjui-combobox" name="reason"
									data-options="valueField:'reason',textField:'reason',onSelect:function(rec){
										$('#description').val(rec.description);
										if(rec.planHalt){
											$('#planHaltYes').attr('checked','checked');
										}else{
											$('#planHaltNo').attr('checked','checked');
										}
									}">
								<input type="hidden" name="deviceSiteId">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">计划停机</label>
							<div style="margin-top: 8px">
								<input name="planHalt" value="true"  id="planHaltYes"
									type="radio"> 是 <input name="planHalt" value="false" checked="checked"	type="radio" id="planHaltNo">否
							</div>
						</div>
					</div>
					<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">详细描述</label>
						<div class="topjui-input-block">
							<!-- <input type="textarea" name="recoverMethod"
								data-toggle="topjui-textarea" id="recoverMethod"> -->
								<textarea rows="5" cols="70" id="description" name="description"></textarea>
						</div>
					</div>
				</div>
				</div>
			</div>

		</form>
	</div>
</div>