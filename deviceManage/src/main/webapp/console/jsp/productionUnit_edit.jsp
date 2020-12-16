<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post" >
			<div title="部门信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">生产单元代码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" readonly="readonly" data-toggle="topjui-textbox"
									   data-options="required:true" id="productionUnitCode_edit">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">生产单元名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									   data-options="required:true" id="productionUnitName_edit">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">产量目标</label>
							<div class="topjui-input-block">
								<input type="text" name="goalOutput"
									   data-toggle="topjui-numberbox" id="goalOutput">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">OEE目标</label>
							<div class="topjui-input-block">
								<input type="text" name="goalOee"
									   data-toggle="topjui-numberbox" id="goalOee">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">损时目标(小时)</label>
							<div class="topjui-input-block">
								<input type="text" name="goalLostTime"
									   data-toggle="topjui-numberbox" id="goalLostTime">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">不合格品目标</label>
							<div class="topjui-input-block">
								<input type="text" name="goalNg"
									   data-toggle="topjui-numberbox" id="goalNg">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">损耗率阀值</label>
							<div class="topjui-input-block">
								<input type="text" name="threshold"
									   data-toggle="topjui-numberbox" id="threshold">
							</div>
						</div>
					</div>
					<div class="topjui-row">
                        <div class="topjui-col-sm12">
                            <label class="topjui-form-label">默认节拍</label>
                            <div class="topjui-input-block">
                            <input type="text" name="beat"
                                    data-toggle="topjui-numberbox" id="beat">
                            </div>
                        </div>
                    </div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">班次类别</label>
							<div class="topjui-input-block">
								<input id="classTypeName" name="classTypeName"
									   data-toggle="topjui-combobox"
									   data-options="required:true,
							        valueField: 'name',
							        textField: 'name',
							        editable:false,
							        url: 'classes/queryAllClassTypes.do'">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">多工单同时开工</label>
							<div class="topjui-input-block">
								<input type="text" name="allowMultiWorkSheetRunning" id="allowMultiWorkSheetRunning"
									   data-toggle="topjui-combobox"
									   data-options="valueField:'value',textField:'text',data: [{text:'不允许', value: 'false',selected:true},{text: '允许',value:'true'}],
										onChange:function(newValue,oldValue){
											if(newValue){
												$('#allowMultiWorkSheetRunning').iCombobox('setValue','true');
											}else{
												$('#allowMultiWorkSheetRunning').iCombobox('setValue','false');
											}
										}">
							</div>
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">描述</label>
						<div class="topjui-input-block">
							<!-- <input type="hidden" id="parentId" name="parent.id" /> -->
							<input type="text" name="note"
								   data-toggle="topjui-textarea" id="productionUnitNote_edit">
						</div>
					</div>
				</div>
			</div>
	</div>

	</form>
</div>
</div>