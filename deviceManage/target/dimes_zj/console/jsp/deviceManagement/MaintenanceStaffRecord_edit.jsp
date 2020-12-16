<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript">
$('#parameterId').iCombogrid({
    idField:'id',
    textField:'code',
    url:'parameter/queryOtherParametersByDeviceSiteId.do',
    columns:[[
        {field:'code',title:'参数代码',width:100},
        {field:'name',title:'参数名称',width:100}
    ]]
});
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post" >
		<div title="编辑维修人员信息" data-options="iconCls:'fa fa-th'">
			<div class="topjui-fluid">
				<div style="height:30px"></div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">员工名称</label>
						<div class="topjui-input-block">
								<input id="name" data-toggle="topjui-combobox" name="name" 
								data-options="valueField:'name',textField:'name',
								url:'employee/queryAllEmployees.do',
						            onSelect:function(data){
						            	$('#code').textbox('setValue',data.code);
						            	$('#unitType').textbox('setValue',data.unitType);
						            	$('#measurementUnit').textbox('setValue',data.measurementUnit);
						            }
						            ,required:true">
								<input type="hidden" name="id" id="id">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">员工代码</label>
						<div class="topjui-input-block">
								<input type="text" name="code" data-toggle="topjui-textbox"
								data-options="required:false" id="code" readonly="readonly">
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">派单时间</label>
						<div class="topjui-input-block">
							<input name="date" data-toggle="topjui-datetimebox"  value="{notices.release_time}" data-options="required:true,width:200,showSeconds:true"
								id="date" >
						</div>
					</div>
				</div>
				<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">占用工时(分)</label>
						<div class="topjui-input-block">
								<input type="text" name="occupyTime" data-toggle="topjui-textbox"
								data-options="required:false" id="occupyTime">
						</div>
					</div>
				</div>
			</div>
		</div>
		</form>
	</div>
</div>