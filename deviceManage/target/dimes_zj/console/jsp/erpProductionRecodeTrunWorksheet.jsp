<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="设备类别信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label">预计开工日期</label>
							<div class="topjui-input-block">
								<!-- <input type="text" name="startDate" data-toggle="topjui-datetimebox"
									data-options="required:true" id="startDate">  -->
									<input type="hidden" name="moDId"
									id="moDId">
									<input type="text" name="startDate"
									data-toggle="topjui-datetimebox" id="startDate" editable="false">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<!-- <label class="topjui-form-label">生产单元</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
							</div> -->
							<label class="topjui-form-label">生产单元</label>
							<div class="topjui-input-block">
									<input data-toggle="topjui-combotreegrid" data-options="width:'100%',required:true,
							        panelWidth:500,
							        url:'',
							        idField:'id',
							        treeField:'name',
							        url:'productionUnit/queryTopProductionUnits.do',
							        columns:[[
							            {field:'id',title:'id',hidden:true},
							            {field:'code',title:'编码',width:200},
							            {field:'name',title:'单元名称',width:200}
							        ]],
							        onSelect: function(rec){
            $('#productionUnitId').val(rec.id)
            $('#productionUnitCode').val(rec.code)
        }"  name="productionUnitName" id="productionUnitName">
								<input type="hidden" name="productionUnitId"
									id="productionUnitId"> 
								<input type="hidden" name="productionUnitCode"
									id="productionUnitCode">
								<input type="hidden" name="unitType"
									id="unitType">
								<input type="hidden" name="define24"
									id="define24">
								<input type="hidden" name="Define33"
									id="Define33">
								<input type="hidden" name="mdeptCode"
									id="mdeptCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label">工件代码</label>
							<div class="topjui-input-block">
								<input type="text" id="detailInvCode"  name="detailInvCode" data-toggle="topjui-textbox" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label">工件名称</label>
							<div class="topjui-input-block">
								<input type="text" name="cInvName" data-toggle="topjui-textbox"
									   id="cInvName" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="cInvStd" data-toggle="topjui-textbox"
									   id="cInvStd" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label">生产批号</label>
							<div class="topjui-input-block">
								<input type="text" name="moLotCode" data-toggle="topjui-textbox"
									   id="moLotCode">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm10">
							<label class="topjui-form-label">生产数量</label>
							<div class="topjui-input-block">
								<input type="text" name="detailQty" data-toggle="topjui-textbox"
									   id="detailQty" readonly="readonly">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>