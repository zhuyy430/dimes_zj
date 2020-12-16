<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
	$(function(){
	    $("#repairDate").val(getDateTime(new Date()));

		//查询所有装备
        $("#equipmentTypeCode").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showInventoryDialog').dialog("open");
            }
        });
		//查询所有员工
        $("#repairEmployeeName").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showEmployeeDialog').dialog("open");
            }
        });
        $('#showInventoryDialog').dialog({
            title: '装备信息',
            width: 800,
            height: 600,
            closed: true,
            cache: false,
            href: 'console/jsp/showEquipment.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function(){
                	 var row = $('#inventoryTable').iDatagrid('getSelected');
                	 confirmInventory(row);
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showInventoryDialog').dialog("close");
                }
            }]
        });
        $('#showEmployeeDialog').dialog({
            title: '装备信息',
            width: 600,
            height: 600,
            closed: true,
            cache: false,
            href: 'console/jsp/showEmployee.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function(){
                	 var row = $('#employeeTab').iDatagrid('getSelected');
                	confirmEmployee(row);
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showEmployeeDialog').dialog("close");
                }
            }]
        });
	});

	//人员信息确定
    function confirmEmployee(row){
             $("#repairEmployeeName").textbox("setValue",row.name);
             $("#repairEmployeeCode").val(row.code);
             $('#showEmployeeDialog').dialog("close");
    }
	//装备信息确定
    function confirmInventory(row){
            $('#equipmentTypeCode').textbox('setValue',row.equipmentType.code);
            $('#equipmentTypeName').textbox('setValue',row.equipmentType.name);
            $('#unitType').textbox('setValue',row.equipmentType.unitType);
            //$('#unitType').textbox('setValue',row.version);
            $('#equipmentCode').textbox('setValue',row.code);
            $("#cumulation").textbox("setValue",row.cumulation);
            $("#equipmentId").val(row.id);
            $('#showInventoryDialog').dialog("close");
    }
</script>
<div id="showInventoryDialog"></div>
<div id="showEmployeeDialog"></div>
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
		data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="ff" method="post">
			<div title="不合格记录" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div style="height: 30px"></div>
					<div class="topjui-row">
					<div class="topjui-col-sm12">
						<label class="topjui-form-label">时间</label>
						<div class="topjui-input-block">
							<input type="text" name="repairDate" data-toggle="topjui-datetimebox"
								data-options="required:false" id="repairDate">
						</div>
					</div>
				</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">维修人员</label>
						 	<div class="topjui-input-block">
								<input id="repairEmployeeName" data-toggle="topjui-textbox" name="repairEmployeeName">
								<input id="repairEmployeeCode" name="repairEmployeeCode" type="hidden">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">装备代码</label>
							<div class="topjui-input-block">
								<input id="equipmentTypeCode" data-toggle="topjui-textbox" >
								<input type="hidden" name="equipmentId" id="equipmentId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">装备名称</label>
							<div class="topjui-input-block">
								<input type="text" name="equipmentTypeName" data-toggle="topjui-textbox"
								data-options="required:false" id="equipmentTypeName" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">规则型号</label>
							<div class="topjui-input-block">
								<input type="text" name="unitType" data-toggle="topjui-textbox"
								data-options="required:false" id="unitType" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">装备序号</label>
							<div class="topjui-input-block">
								<input type="text" name="equipmentCode" data-toggle="topjui-textbox"
								data-options="required:false" id="equipmentCode" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">计量累计</label>
							<div class="topjui-input-block">
								<input type="text" name="cumulation" data-toggle="topjui-textbox"
								data-options="required:false" id="cumulation" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">故障类别</label>
							<div class="topjui-input-block">
							<input id="pressLightTypeName" data-toggle="topjui-combobox" name="pressLightTypeName"
								data-options="valueField:'code',textField:'name',
								url:'pressLightType/queryFirstLevelType.do?type=EQUIPMENT',
								onSelect:function(rec){
                                    var url = 'pressLight/queryAllPressLightByTypeId.do?typeId='+rec.id;
                                    $('#reason').iCombobox('reload', url);
								}">
								<input type="hidden" id="ngReasonId" name="ngReasonId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">维修原因</label>
							<div class="topjui-input-block">
								<input id="reason" data-toggle="topjui-combobox" name="reason"
                                    data-options="valueField:'id',textField:'reason'">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">维修说明</label>
							<div class="topjui-input-block">
								<textarea rows="5" cols="86" id="note" name="note"></textarea>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>