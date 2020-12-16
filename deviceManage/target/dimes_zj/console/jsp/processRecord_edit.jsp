<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
	$(function(){
		$('#no').iCombogrid({
		    idField:'no',
		    textField:'no',
		    delay: 500,
		    mode: 'remote',
		    url:'processRecord/queryWorkSheetByDeviceSiteId.do',
		    columns:[[
		        {field:'id',title:'id',width:60,hidden:true},
		        {field:'no',title:'工单号',width:100},
		        {field:'workPieceCode',title:'工件代码',width:100},
		        {field:'customerGraphNumber',title:'客户图号',width:100},
		        {field:'batchNumber',title:'批号',width:100},
		        {field:'stoveNumber',title:'材料编号',width:100},
		        {field:'productCount',title:'生产数量',width:100}
		    ]],
		    onClickRow : function(index, row) {
		    	$('#workPieceCode').textbox('setValue',row.workPieceCode);
				$('#workPieceName').textbox('setValue',row.workPieceName);
				$('#unitType').textbox('setValue',row.unitType);
				$('#graphNumber').textbox('setValue',row.graphNumber);
				$('#version').textbox('setValue',row.version);
				$('#workSheetId').val(row.id);
				$('#batchNumber').textbox('setValue',row.batchNumber);
				$('#stoveNumber').textbox('setValue',row.stoveNumber);
	            var url = 'processRecord/queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId.do?workSheetId='+row.id+"&deviceSiteId=" + $("#departmentTg").iTreegrid("getSelected").id;
	            $('#processCode').iCombobox('reload', url);
		    },
		    queryParams:{
		    	deviceSiteId:$("#departmentTg").iTreegrid("getSelected").id
		    }
		});

	});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post" >
		<div title="设备执行记录" data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div style="height:30px"></div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">生产时间</label>
						<div class="topjui-input-block">
							<input type="text" name="collectionDate" data-toggle="topjui-datetimebox"
								data-options="required:false" id="collectionDate">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">生产序号</label>
						<div class="topjui-input-block">
							<input type="text" name="productNum" data-toggle="topjui-textbox"
								data-options="required:true" id="productNum">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">二维码</label>
						<div class="topjui-input-block">
							<input type="text" name="opcNo" data-toggle="topjui-textbox"
								data-options="required:false" id="opcNo">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">工单单号</label>
						<div class="topjui-input-block">
								<input id="no" data-toggle="topjui-combogrid" name="no">
								<input type="hidden" name="deviceSiteId">
								<input type="hidden" name="workSheetId" id="workSheetId">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">工件代码</label>
						<div class="topjui-input-block">
							<input type="text" name="workPieceCode" data-toggle="topjui-textbox"
								data-options="required:true" id="workPieceCode">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">工件名称</label>
						<div class="topjui-input-block">
							<input type="text" name="workPieceName" data-toggle="topjui-textbox"
								data-options="required:false" id="workPieceName">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">规格型号</label>
						<div class="topjui-input-block">
							<input type="text" name="unitType" data-toggle="topjui-textbox"
								data-options="required:false" id="unitType">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">图号</label>
						<div class="topjui-input-block">
							<input type="text" name="graphNumber" data-toggle="topjui-textbox"
								data-options="required:false" id="graphNumber">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">版本号</label>
						<div class="topjui-input-block">
							<input type="text" name="version" data-toggle="topjui-textbox"
								data-options="required:false" id="version">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">工序代码</label>
						<div class="topjui-input-block">
								<input id="processCode" data-toggle="topjui-combobox" name="processCode" 
								data-options="required:true,valueField:'processCode',textField:'processCode',
								onSelect:function(rec){
									$('#processName').textbox('setValue',rec.processName);
									$('#processId').val(rec.processId);
								}">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">工序名称</label>
						<div class="topjui-input-block">
							<input type="text" name="processName" data-toggle="topjui-textbox"
								data-options="required:false" id="processName">
							<input type="hidden" name="processId"  id="processId">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">即时节拍</label>
						<div class="topjui-input-block">
							<input type="text" name="realBeat" data-toggle="topjui-numberbox"
								data-options="required:false" id="realBeat">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">批号</label>
						<div class="topjui-input-block">
							<input type="text" name="batchNumber" data-toggle="topjui-textbox"
								data-options="required:false" id="batchNumber">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">材料编号</label>
						<div class="topjui-input-block">
							<input type="text" name="stoveNumber" data-toggle="topjui-textbox"
								data-options="required:false" id="stoveNumber">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">状态</label>
						<div class="topjui-input-block">
							<input type="text" name="status" data-toggle="topjui-combobox"
								data-options="valueField: 'text',
									textField: 'text',
									data: [{
									    text: 'OK'
									},{
									    text: 'NG'
									}]" id="status">
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>