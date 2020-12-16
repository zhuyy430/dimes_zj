<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
	$(function() {
	//故障类型列表
	$('#sparepartName').iCombogrid(
			{
				idField : 'code',
				textField : 'name',
				delay : 500,
				mode : 'remote',
				url : 'sparepart/querySparepartsByDeviceRepairId.do?DeviceRepairId='+$('#deviceDg').iDatagrid('getSelected').id,
				columns : [ [ {
					field : 'id',
					title : 'id',
					width : 60,
					hidden : true
				}, {
					field : 'code',
					title : '备件代码',
					width : 100
				}, {
					field : 'name',
					title : '备件名称',
					width : 100
				},{
					field : 'unitType',
					title : '规格型号',
					width : 100
				},{
					field : 'measurementUnit',
					title : '计量单位',
					width : 100
				},{
					field : 'note',
					title : '备注',
					width : 100
				}] ],
				onClickRow : function(index, row) {
					$('#sparepartCode').textbox('setValue',row.code);
	            	$('#sparepartId').val(row.id);
				}
			});
	});
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
						<label class="topjui-form-label">耗用时间</label>
						<div class="topjui-input-block">
								<input name="date" data-toggle="topjui-datetimebox"  value="{notices.release_time}" data-options="required:true,width:200,showSeconds:true"
								id="date" >
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备件名称</label>
						<div class="topjui-input-block">
								<input id="sparepartName" data-toggle="topjui-textbox" name="sparepartName" 
								data-options="required:true">
								<input type="hidden" name="sparepartId" id="sparepartId">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备件代码</label>
						<div class="topjui-input-block">
								<input type="text" name="sparepartCode" data-toggle="topjui-textbox"
								data-options="required:false" id="sparepartCode" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">数量</label>
						<div class="topjui-input-block">
								<input type="text" value="0" name="quantity" data-toggle="topjui-numberspinner"
								data-options="required:false" id="quantity">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">备注</label>
						<div class="topjui-input-block">
							<input name="note" data-toggle="topjui-textarea" data-options="required:false"
								id="note" >
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>