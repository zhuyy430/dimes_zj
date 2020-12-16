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
							<label class="topjui-form-label">故障类别</label>
							<div class="topjui-input-block">
								<input id="pressLightTypeId" data-toggle="topjui-combotree"
									name="pressLightTypeId"
									data-options="url:'pressLightType/queryAllPressLightType.do',
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
									data-options="valueField:'reason',textField:'reason',onSelect:function(rec){
										if(rec.halt){
											$('#halt').iCombobox('selecte','是');
										}else{
											$('#halt').iCombobox('selecte','否');
										}
										
										$('#pressLightCode').val(rec.code);
									}">
								<input type="hidden" name="pressLightCode" id="pressLightCode" />
								<input type="hidden" name="deviceSiteId">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">是否停机</label>
							<div class="topjui-input-block">
								<input data-toggle="topjui-combobox" name="halt" id="halt"
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
							<label class="topjui-form-label">恢复方法</label>
							<div class="topjui-input-block">
								<textarea rows="5" cols="60" id="recoverMethod"
									name="recoverMethod"></textarea>
							</div>
						</div>
					</div>
				</div>
			</div>

		</form>
	</div>
</div>