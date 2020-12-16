<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">

		<form id="ff" method="post">
			<div title="文档类型信息" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">等级编码</label>
							<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
									data-options="required:true" id="code">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">等级名称</label>
							<div class="topjui-input-block">
								<input type="text" name="name" data-toggle="topjui-textbox"
									data-options="required:true" id="name">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">类别</label>
							<div class="topjui-input-block">
								<input type="text" name="orderType" data-toggle="topjui-combobox"
									data-options="valueField:'moduleCode',textField:'moduleName',
										data:[{moduleCode:'维修',moduleName:'维修',selected:true},
										{moduleCode:'保养',moduleName:'保养'}]" id="orderType">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm6">
							<label class="topjui-form-label">颜色</label>
							<div class="topjui-input-block">
								<input type="color" name="color" data-toggle="topjui-textbox"
									id="color">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">超时时间</label>
							<div class="topjui-input-block">
								<input type="text" name="timing" data-toggle="topjui-numberspinner"
									id="timing">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm6">
							<label class="topjui-form-label">人员</label>
							<div class="topjui-input-block">
								<input id="employee" name="employee" type="text" data-toggle="topjui-combogrid"
									data-options="width:435,
					           panelWidth:435,
					           idField:'id',
					           textField:'name',
					           url:'employee/queryAllEmployees.do',
					           columns:[[
					               {field:'id',title:'id',width:100,hidden:true},
					               {field:'code',title:'代码',width:200},
					               {field:'name',title:'名称',width:200}
					           ]]" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>