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
							<label class="topjui-form-label">按灯时间</label>
							<div class="topjui-input-block">
								<input name="pressLightTime" data-toggle="topjui-datetimebox"  value="{notices.release_time}" data-options="required:false,width:200,showSeconds:true"
								id="pressLightTime">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">故障类别</label>
							<div class="topjui-input-block">
								<input id="pressLightTypeId"
									data-toggle="topjui-combotree" name="pressLightTypeId"
									data-options="url:'pressLightType/queryFirstLevelType.do?type=PRESSLIGHTTYPE',
								onSelect: function(rec){
					            var url = 'pressLight/queryAllPressLightByTypeId.do?typeId='+rec.id;
					            $('#reason').iCombobox('reload', url);
				      		  }">
								<input type="hidden" name="deviceSiteId">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">故障原因</label>
							<div class="topjui-input-block">
								<input id="reason" data-toggle="topjui-combobox" name="reason"
									data-options="valueField:'code',textField:'reason',onSelect:function(rec){
										if(rec.halt){
											$('#halt').attr('checked','checked');
										}else{
											$('#notHalt').attr('checked','checked');
										}
									}">
								<input type="hidden" name="deviceSiteId">
							</div>
						</div>
					</div> 
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">是否停机</label>
							<div class="topjui-input-block">
								<input data-toggle="topjui-combobox" name="halt" id="halt" data-options="
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
						<label class="topjui-form-label">恢复方法</label>
						<div class="topjui-input-block">
							<!-- <input type="textarea" name="recoverMethod"
								data-toggle="topjui-textarea" id="recoverMethod"> -->
								<textarea rows="5" cols="60" id="recoverMethod" name="recoverMethod"></textarea>
						</div>
					</div>
				</div>
				</div>
			</div>

		</form>
	</div>
</div>