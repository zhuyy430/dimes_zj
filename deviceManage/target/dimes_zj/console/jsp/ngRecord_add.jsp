<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<script>
		/**
		 * 根据工单查找工件
		 * @param row 工单对象
		 * */
		function queryWorkpick(row){
            $('#workpieceCode').textbox('setValue',row.workPieceCode);
            $('#workpieceName').textbox('setValue',row.workPieceName);
            $('#unitType').textbox('setValue',row.unitType);
            $('#graphNumber').textbox('setValue',row.graphNumber);
            $('#customerGraphNumber').textbox('setValue',row.customerGraphNumber);
            $('#batchNumber').textbox('setValue',row.batchNumber);
            $('#stoveNumber').textbox('setValue',row.stoveNumber);
            $('#version').textbox('setValue',row.version);
            $('#workSheetId').val(row.id);
            var url = 'workSheet/queryWorkSheetDetailByWorkSheetIdAndDeviceSiteId.do?workSheetId='+row.id +"&deviceSiteId="+$("#departmentTg").iTreegrid("getSelected").id;
            $('#processCode').iCombogrid("grid").iDatagrid({idField:'processCode',
                textField:'processCode',
                delay: 500,
                mode: 'remote',
                url:url,
                columns:[[
                    {field:'processId',title:'id',width:60,hidden:true},
                    {field:'processCode',title:'工序代码',width:100},
                    {field:'processName',title:'工序名称',width:100}
                ]],
                onClickRow : function(index, row) {
                    $('#processName').textbox('setValue',row.processName);
                    $('#processCode').textbox('setValue',row.processCode);
                    $('#processId').val(row.processId);
                },
                onLoadSuccess:function(data){
                    if(!!data && data.rows.length>0){
                        for(var i = 0;i<data.rows.length;i++){
                            var worksheet = data.rows[i];
                            if(worksheet.status==1){
                                $("#no").iCombogrid("setValue",worksheet.no);
                            }
                        }
                        $("#processCode").textbox("setValue",data.rows[0].processCode);
                        $("#processName").textbox("setValue",data.rows[0].processName);
                        $("#processId").val(data.rows[0].processId);
                    }
                }
            });
		}

	$(function(){
	    $("#occurDate").val(getDateTime(new Date()));
		//工单选择
		$('#no').iCombogrid({
		    idField:'no',
		    textField:'no',
		    delay: 500,
		    mode: 'remote',
		    url:'workSheet/queryWorkSheetByDeviceSiteId.do',
		    columns:[[
		        {field:'id',title:'id',width:60,hidden:true},
		        {field:'no',title:'工单号',width:100},
		        {field:'workPieceCode',title:'工件代码',width:100},
		        {field:'batchNumber',title:'批号',width:100},
		        {field:'stoveNumber',title:'材料编号',width:100},
		        {field:'productCount',title:'生产数量',width:100}
		    ]],
            onClickRow : function(index, row) {
		        queryWorkpick(row);
		    },
		    queryParams:{
		    	deviceSiteId:$("#departmentTg").iTreegrid("getSelected").id
		    }
		});


        //根据设备站点编码查找正在加工的工单
        $.get("workSheet/queryProcessingWorkSheetByDeviceSiteId.do",{deviceSiteId:$('#departmentTg').iTreegrid('getSelected').id},function(result){
            if(result){
                $("#no").iCombogrid("setValue",result.no);
                queryWorkpick(result);
            }
        });
		//查询所有工件
        $("#workpieceCode").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showInventoryDialog').dialog("open");
            }
        });
        $('#showInventoryDialog').dialog({
            title: '工件信息',
            width: 800,
            height: 600,
            closed: true,
            cache: false,
            href: 'console/jsp/showInventories.jsp',
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
		//工序选择
		$('#processCode').iCombogrid({
		    idField:'code',
		    textField:'code',
		    delay: 500,
		    mode: 'remote',
		    url:'',
		    columns:[[
		        {field:'id',title:'id',width:60,hidden:true},
		        {field:'code',title:'工序代码',width:100},
		        {field:'name',title:'工序名称',width:100}
		    ]]
		});
	});
	 //工件信息确定
	function confirmInventory(row){
		 $("#workpieceCode").iTextbox('setValue',row.code);
			$('#workpieceName').textbox('setValue',row.name);
			$('#unitType').textbox('setValue',row.unitType);
			$('#graphNumber').textbox('setValue',row.graphNumber);
			$('#version').textbox('setValue',row.version);
			$('#workSheetId').val('');
			$("#no").textbox("setValue","");
		//查找工序
		 var url = 'processes/queryProcessByWorkpieceIdAndDeviceSiteId.do?workpieceId='+row.code +"&deviceSiteId="+$("#departmentTg").iTreegrid("getSelected").id;
        $('#processCode').iCombogrid("grid").iDatagrid({idField:'code',
		    textField:'code',
		    delay: 500,
		    mode: 'remote',
		    url:url,
		    columns:[[
		        {field:'id',title:'id',width:60,hidden:true},
		        {field:'code',title:'工序代码',width:100},
		        {field:'name',title:'工序名称',width:100}
		    ]],
		    onClickRow : function(index, row) {
				$('#processName').textbox('setValue',row.name);
				$('#processId').val(row.id);
		    }});
          $('#showInventoryDialog').dialog("close");
	}
</script>
<div id="showInventoryDialog"></div>
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
							<input type="text" name="occurDate" data-toggle="topjui-datetimebox"
								data-options="required:false" id="occurDate">
						</div>
					</div>
				</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工单号</label>
						 	<div class="topjui-input-block">
								<input id="no" data-toggle="topjui-combogrid" name="no">
								<input id="workSheetId" name="workSheetId" type="hidden">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工件代码</label>
							<div class="topjui-input-block">
								<input id="workpieceCode" data-toggle="topjui-textbox">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工件名称</label>
							<div class="topjui-input-block">
								<input type="text" name="workpieceName" data-toggle="topjui-textbox"
								data-options="required:false" id="workpieceName" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工序代码</label>
							<div class="topjui-input-block">
								<input type="text" name="processCode" data-toggle="topjui-combogrid"
								data-options="required:false" id="processCode">
								
								<input type="hidden" name="processId" id="processId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">工序名称</label>
							<div class="topjui-input-block">
								<input type="text" name="processName" data-toggle="topjui-textbox"
								data-options="required:false" id="processName" >
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="unitType" data-toggle="topjui-textbox"
								data-options="required:false" id="unitType" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">图号</label>
							<div class="topjui-input-block">
								<input type="text" name="graphNumber" data-toggle="topjui-textbox"
								data-options="required:false" id="graphNumber" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">版本号</label>
							<div class="topjui-input-block">
								<input type="text" name="version" data-toggle="topjui-textbox"
								data-options="required:false" id="version" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">批号</label>
							<div class="topjui-input-block">
								<input type="text" name="batchNumber" data-toggle="topjui-textbox"
								data-options="required:false" id="batchNumber" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">材料编号</label>
							<div class="topjui-input-block">
								<input type="text" name="stoveNumber" data-toggle="topjui-textbox"
								data-options="required:false" id="stoveNumber" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">原因</label>
							<div class="topjui-input-block">
							<input id="ngReason" data-toggle="topjui-combobox" name="ngReason"
								data-options="valueField:'id',textField:'ngReason',
								url:'ngReason/queryAllNGReasons.do',
								onSelect:function(rec){
									$('#ngReasonId').val(rec.id);
									switch(rec.processingMethod){
										case '报废':$('#processingMethod').iCombobox('setValue','scrap');break;
										case '返修':$('#processingMethod').iCombobox('setValue','repair');break;
										case '让步接收':$('#processingMethod').iCombobox('setValue','compromise');break;
										default:$('#processingMethod').iCombobox('setValue',rec.processingMethod);
									}
								}">
								<input type="hidden" id="ngReasonId" name="ngReasonId" />
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">处理方法</label>
							<div class="topjui-input-block">
								<input id="processingMethod" name="processingMethod"
									data-toggle="topjui-combobox"
									data-options="valueField: 'value',
										textField: 'text',
										data: [{
											value:'scrap',
										    text: '报废',
										},{
											value:'repair',
										    text: '返修'
										},{
											value:'compromise',
										    text: '让步接收'
										}]">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm12">
							<label class="topjui-form-label">数量</label>
							<div class="topjui-input-block">
								<input type="text" name="ngCount" data-toggle="topjui-numberbox"
								data-options="required:false" id="ngCount" value="0">
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
</div>