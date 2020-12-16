<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script>
	$(function(){
		var now = new Date();
		var dateTime = now.toLocaleString('chinese', { hour12: false });;	
		$("#mappingDate").val(dateTime);
		$('#equipment').iCombogrid({
		    idField:'id',
		    textField:'code',
		    delay: 500,
		    mode: 'remote',
		    url:'equipment/queryAllEquipmentsByNotStopUse.do',
		    columns:[[
		        {field:'id',title:'id',width:60,hidden:true},
		        {field:'code',title:'序号',width:100},
		        {field:'name',title:'名称',width:100,formatter:function(value,row,index){
		            if(row.equipmentType){
		                return row.equipmentType.name;
					}else{
		                return '';
					}
					}},
		        {field:'unitType',title:'规格型号',width:120,formatter:function(value,row,index){
                        if(row.equipmentType){
                            return row.equipmentType.unitType;
                        }else{
                            return '';
                        }
                    }}
		    ]]
		});
		$('#helperName').iCombogrid({
		    idField:'code',
		    textField:'name',
		    delay: 500,
		    mode: 'remote',
		    url:'user/queryNotCurrentUsers.do',
		    columns:[[
		        {field:'code',title:'代码',width:100},
		        {field:'name',title:'名称',width:100},
		        {field:'cDepName',title:'所在部门',width:100}
		    ]],
            onClickRow : function(index, row) {
				$('#helperRealName').val(row.name);
			}
		});
	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="装备关联信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>

					<div class="topjui-row">
						<div class="topjui-col-sm12">
						<label class="topjui-form-label">关联时间</label>
						<div class="topjui-input-block">
							<input type="text" name="mappingDate" data-toggle="topjui-datetimebox"
								data-options="required:false" id="mappingDate">
						</div>
					</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">装备</label>
						 <div class="topjui-input-block">
								<input id="equipment" name="equipment" data-toggle="topjui-combogrid" data-options="required:true">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工单号</label>
							<div class="topjui-input-block">
								<input type="text" name="workSheetCode" data-toggle="topjui-textbox"
								data-options="required:false" id="workSheetCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">使用频次</label>
							<div class="topjui-input-block">
								<input type="text" name="usageRate" data-toggle="topjui-numberbox" value=1
								data-options="required:false" id="usageRate">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">辅助人</label>
							<div class="topjui-input-block">
								<input id="helperName" data-toggle="topjui-combogrid"
									name="helperName">
								<input type="hidden" name="helperRealName" id="helperRealName"/>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>