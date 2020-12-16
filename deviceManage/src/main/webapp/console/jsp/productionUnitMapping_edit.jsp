<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="生产单元-客户端IP映射信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">ip地址</label>
							<div class="topjui-input-block">
								<input type="text" name="clientIp" data-toggle="topjui-textbox"
									data-options="required:true" id="clientIp" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">生产单元</label>
							<div class="topjui-input-block">
								<input id="productionUnitName" name="productionUnitName"
									data-toggle="topjui-combobox"
									data-options="required:true,
							        valueField: 'id',
							        textField: 'name',
							        url: 'productionUnit/queryAllLeafProductionUnits.do',
							        onSelect: function(rec){
							            $('#productionUnitId').val(rec.id);
							        }">
							        
							        <input type="hidden" name="productionUnitId" id="productionUnitId">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>