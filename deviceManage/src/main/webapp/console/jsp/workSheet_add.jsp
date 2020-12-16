<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="common/js/jQuery.print.js"></script>
<script type="text/javascript" src="console/jsp/js/worksheet.js"></script>
<link rel="stylesheet" href="console/jsp/css/workSheet.css">
<script>
    var  requestNo = '<%=request.getParameter("WorkSheetNo")%>';
    $(function(){
        if(requestNo=='null'){
            requestNo = '<%=session.getAttribute("WorkSheetNo")%>';
            if(requestNo=='null'){
                requestNo='';
            }else{
                $("#workSheetNo").val(requestNo);
            }
        }else{
            $("#workSheetNo").val(requestNo);
        }
    });
    $(function() {
    	var erp ; 
        //添加工单
        if(!requestNo){
            //获取生产单元编码
            var _productionUnitId = '<%=request.getParameter("productionUnitId")%>';
            var _moDId = '<%=request.getParameter("moDId")%>';
            
            var _moLotCode = '<%=request.getParameter("moLotCode")%>';
            var _startDate = '<%=request.getParameter("startDate")%>';
            //保存生产单元编码
            $("#productionUnitId").val(_productionUnitId);
            $.get("productionUnit/queryProductionUnitById.do",{id:_productionUnitId},function(result){
                $("#productionUnitCode").val(result.code);
                $("#productionUnitName").iTextbox("setValue",result.name);
            });
            if(_moDId){
            	$.get("Mom_orderdetail/queryMom_recorddetailById.do",{id:_moDId},function(result){
	            	erp = result;
	            	 $("#inventoryCodeSearch").iTextbox("setValue",erp.detailInvCode);
                    $("#workPieceCode").val(erp.detailInvCode);
	            	 $("#workPieceName").iTextbox("setValue",erp.cInvName);
                    $("#workPieceId").val(erp.detailInvCode);
	            	 $("#unitType").iTextbox("setValue",erp.cInvStd);
	            	 $("#productCount").iTextbox("setValue",erp.detailQty);
	            	 $("#batchNumber").iTextbox("setValue",erp.moLotCode);
	            	 $("#stoveNumber").iTextbox("setValue",erp.define24);
	            	 $("#Define33").iTextbox("setValue",erp.define33);
	            	 $("#invcode").iTextbox("setValue",erp.moallocateInvcode);
	            	 $("#Qty").iTextbox("setValue",erp.moallocateQty);
	            	 $("#LotNo").iTextbox("setValue",erp.lotNo);
	            	 
	            	// 可编辑工单 详情(新增)
                     $('#workSheetDetail').iDatagrid({
                         url:'inventory/queryWorkpieceProcessDeviceSiteMapping.do',
                         fitColumns:true,
                         pagination:false,
                         singleSelect:true,
                         columns:[[
                             {field:'id',title:'id',hidden:true},
                             {field:'processCode',title:'加工顺序',width:120,align:'center'},
                             {field:'processCode',title:'工序代码',width:120,align:'center'},
                             {field:'processName',title:'工序名称',width:120,align:'center'},
                             {field:'deviceCode',title:'设备代码',width:120,align:'center'},
                             {field:'deviceName',title:'设备名称',width:120,align:'center'},
                             {field:'productionCount',title:'计划数量',width:100,align:'center'},
                             {field:'reportCount',title:'报工数',width:100,align:'center'},
                             {field:'unqualifiedCount',title:'不合格数量',width:100,align:'center'},
                             {field:'repairCount',title:'返修数量',width:100,align:'center'},
                             {field:'scrapCount',title:'报废数量',width:100,align:'center'},
                             {field:'compromiseCount',title:'让步接收数量',width:100,align:'center'},
                             {field:'status',title:'状态',width:100,align:'center',formatter:function(value,row,index){
                                     if(value){
                                         switch(value){
                                             case '0': return '计划';
                                             case '1': return '加工中';
                                             case '2': return '停机';
                                             case '3': return '完工';
                                         }
                                     }
                                 }}
                         ]],
                         queryParams:{
                             inventoryCode:erp.detailInvCode,
                             count:$("#productCount").val(),
                             productionUnitCode:$("#productionUnitCode").val()

                         }

                     });
            	})
            }
            //初始化生产时间
            $("#manufactureDate").iDatetimebox('setValue',getDateTime(new Date()));
            //初始化生产任务单号
            $.get("workSheet/generateNo.do",function (result) {
                $("#no").iTextbox("setValue",result);
				if(erp){
		            $("#no").iTextbox("setValue",erp.moId+"-"+erp.sortSeq);
				}
            });
            
            $("#saveSheet").iMenubutton("enable");
            $("#printSheet").iMenubutton("disable");
            $("#startSheet").iMenubutton("disable");
            $("#stopSheet").iMenubutton("disable");
            $("#overSheet").iMenubutton("disable");
            //查询所有工件
            $("#inventoryCodeSearch").iTextbox({
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
               /*  onDblClickRow :function(rowIndex,rowData){
                	confirmInventory(rowData);
     				}, */
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

            //查询所有货位
            $("#locationCodes").iTextbox({
                buttonIcon:'fa fa-search',
                onClickButton:function(){
                    $('#showLocationDialog').dialog("open");
                }
            });
            $('#showLocationDialog').dialog({
                title: '货位信息',
                width: 800,
                height: 600,
                closed: true,
                cache: false,
                href: 'procurement/showLocation2selects.jsp',
                modal: true,
               /*  onDblClickRow :function(rowIndex,rowData){
                	confirmInventory(rowData);
     				}, */
                buttons:[{
                    text:'确定',
                    handler:function(){
                        var row = $('#locationTable').iDatagrid('getChecked');
                        //confirmInventory(row);
                        var codes = $("#locationCodes").val();
                        var codesList=codes.split(",");
                        $.each(row, function(i,v){
                        	if(codesList.indexOf(v.cPosCode)>-1){
                        		return true;
                        	}
                        	if(codes==""){
                        		codes = v.cPosCode;
                        	}else{
                        		codes += ","+v.cPosCode;
                        	}
                         }); 
                        $("#locationCodes").iTextbox("setValue",codes);
                        $('#showLocationDialog').dialog("close");
                    }
                },{
                    text:'关闭',
                    handler:function(){
                        $('#showLocationDialog').dialog("close");
                    }
                }]
            });
        }else{//查看工单
            $.get("workSheet/queryWorkSheetByNo.do",{No:requestNo},function(result){
                if(result){
                    _result = result;
                    $("#id").val(result.id);
                    $("#no").iTextbox("setValue",result.no);
                    $("#no").iTextbox("readonly");
                    $("#manufactureDate").iDatetimebox("setValue",result.manufactureDate);
                    $("#workSheetType").iCombobox("setValue",result.workSheetType);
                    if(result.status==0){
                        $("#status").iTextbox("setValue","计划");
                    }else if (result.status==1){
                        $("#status").iTextbox("setValue","加工中");
                    }else if (result.status==2){
                        $("#status").iTextbox("setValue","停工");
                    }else if (result.status==3){
                        $("#status").iTextbox("setValue","完工");
                    }
                    if(result.status!=0){
                        //$("#productCount").iTextbox("readonly");
                        //$("#batchNumber").iTextbox("readonly");
                        //$("#stoveNumber").iTextbox("readonly");
                        $("#manufactureDate").iDatetimebox("readonly");
                        $("#workSheetType").iCombobox("readonly");
                    }
                    $("#inventoryCodeSearch").iTextbox("setValue",result.workPieceCode);
                    $("#workPieceCode").val(result.workPieceCode);
                    $("#workPieceName").iTextbox("setValue",result.workPieceName);
                    $("#productionUnitCode").val(result.productionUnitCode);
                    $("#productionUnitName").iTextbox("setValue",result.productionUnitName);
                    $("#unitType").iTextbox("setValue",result.unitType);
                    $("#graphNumber").iTextbox("setValue", result.graphNumber);
                    $("#productCount").iTextbox("setValue",result.productCount);
                    $("#productionUnitId").val(result.productionUnitId);
                    $("#batchNumber").iTextbox("setValue",result.batchNumber);
                    $("#stoveNumber").iTextbox("setValue",result.stoveNumber);
                    $("#inventoryCodeSearch").iTextbox("readonly");
                    $("#Define33").iTextbox("setValue",result.define33);
                    $("#invcode").iTextbox("setValue",result.moallocateInvcode);
                    $("#Qty").iTextbox("setValue",result.moallocateQty);
                    $("#LotNo").iTextbox("setValue",result.lotNo);
                    $("#locationCodes").iTextbox("setValue",result.locationCodes);
					$("#sumOfAllJobBooking").iTextbox("setValue",result.sumOfAllJobBooking);
					$("#sumOfAllInWarehouse").iTextbox("setValue",result.sumOfAllInWarehouse);
                    //$("#locationCodes").iTextbox("readonly");
                    $('#workSheetDetail').iDatagrid({
                        url:'workSheet/queryWorkSheetDetailByWorkSheetId.do',
                        fitColumns:true,
                        singleSelect:true,
                        columns:[[
                            {field:'id',title:'id',hidden:true},
                            {field:'processRoute',title:'加工顺序',width:120,align:'center'},
                            {field:'processCode',title:'工序代码',width:120,align:'center'},
                            {field:'processName',title:'工序名称',width:120,align:'center'},
                            {field:'deviceCode',title:'设备代码',width:120,align:'center'},
                            {field:'deviceName',title:'设备名称',width:120,align:'center'},
                            {field:'productionCount',title:'计划数量',width:100,align:'center'},
                            {field:'reportCount',title:'报工数',width:100,align:'center'},
                            {field:'unqualifiedCount',title:'不合格数量',width:100,align:'center'},
                            {field:'repairCount',title:'返修数量',width:100,align:'center'},
                            {field:'scrapCount',title:'报废数量',width:100,align:'center'},
                            {field:'compromiseCount',title:'让步接收数量',width:100,align:'center'},
                            {field:'status',title:'状态',width:100,align:'center',formatter:function(value,row,index){
                                    if(value){
                                        switch(value){
                                            case '0': return '计划';
                                            case '1': return '加工中';
                                            case '2': return '停机';
                                            case '3': return '完工';
                                        }
                                    }
                                }}
                        ]],
                        queryParams:{
                            workSheetId:$("#id").val()
                        }
                    });
                }
            });
        }

        $('#MaterialRequisitionDetail').iDatagrid({
            url:'materialRequisitionDetail/queryMaterialRequisitionDetailByWorkSheetNo.do',
            fitColumns:true,
            singleSelect:true,
            columns:[[
                {field:'id',title:'id',hidden:true},
                {field:'pickingDate',title:'领料日期',width:120,align:'center'},
                {field:'formNo',title:'领料单号',width:120,align:'center'},
                {field:'barCode',title:'条码信息',width:120,align:'center'},
                {field:'inventoryCode',title:'物料代码',width:120,align:'center'},
                {field:'inventoryName',title:'物料名称',width:120,align:'center'},
                {field:'specificationType',title:'规格型号',width:100,align:'center'},
                {field:'amount',title:'已领数量',width:100,align:'center'},
                {field:'batchNumber',title:'批号',width:100,align:'center'},
                {field:'furnaceNumber',title:'材料编码',width:100,align:'center'},
                {field:'boxNum',title:'箱号',width:100,align:'center'},
                {field:'amountOfBoxes',title:'总箱数',width:100,align:'center'}
            ]],
            queryParams:{
                workSheetNo:requestNo
            }
        });

        $('#JobBookingFormDetail').iDatagrid({
            url:'jobBookingFormDetail/queryJobBookingFormDetailPageByWorkSheetNo.do',
            fitColumns:true,
            singleSelect:true,
            columns:[[
                {field:'id',title:'id',hidden:true},
                {field:'jobBookingDate',title:'报工日期',width:120,align:'center'},
                {field:'jobBookingTime',title:'报工时间',width:120,align:'center'},
                {field:'jobBookerName',title:'报工人员',width:120,align:'center'},
                {field:'barCode',title:'条码信息',width:120,align:'center'},
                {field:'amountOfJobBooking',title:'报工数量',width:120,align:'center'},
				{field:'amountOfInWarehouse',title:'入库数量',width:120,align:'center'},
                {field:'boxNum',title:'箱号',width:100,align:'center'},
                {field:'amountOfBoxes',title:'总箱数',width:100,align:'center'}
            ]],
            queryParams:{
                workSheetNo:requestNo
            }
        });



    });
    //工件代码确定
    function confirmInventory(row) {
    	$("#workPieceCode").val(row.code);
        $("#inventoryCodeSearch").iTextbox('setValue',row.code);
        $("#workPieceName").textbox("setValue", row.name);
        $("#unitType").textbox("setValue",row.unitType);
        $("#customerGraphNumber").val(row.customerGraphNumber);
        $("#graphNumber").textbox("setValue", row.graphNumber);
        $("#version").val(row.version);
        $("#workpieceId").val(row.code);
        $("#unitCode").val(row.cComUnitCode);
        $("#unitName").val(row.measurementUnit);
        // 可编辑工单 详情(新增)
        $('#workSheetDetail').iDatagrid({
            url:'inventory/queryWorkpieceProcessDeviceSiteMapping.do',
            fitColumns:true,
            pagination:false,
            singleSelect:true,
            columns:[[
                {field:'id',title:'id',hidden:true},
                {field:'processCode',title:'加工顺序',width:120,align:'center'},
                {field:'processCode',title:'工序代码',width:120,align:'center'},
                {field:'processName',title:'工序名称',width:120,align:'center'},
                {field:'deviceCode',title:'设备代码',width:120,align:'center'},
                {field:'deviceName',title:'设备名称',width:120,align:'center'},
                {field:'productionCount',title:'计划数量',width:100,align:'center'},
                {field:'reportCount',title:'报工数',width:100,align:'center'},
                {field:'unqualifiedCount',title:'不合格数量',width:100,align:'center'},
                {field:'repairCount',title:'返修数量',width:100,align:'center'},
                {field:'scrapCount',title:'报废数量',width:100,align:'center'},
                {field:'compromiseCount',title:'让步接收数量',width:100,align:'center'},
                {field:'status',title:'状态',width:100,align:'center',formatter:function(value,row,index){
                        if(value){
                            switch(value){
                                case '0': return '计划';
                                case '1': return '加工中';
                                case '2': return '停机';
                                case '3': return '完工';
                            }
                        }
                    }}
            ]],
            queryParams:{
                inventoryCode:row.code,
                count:$("#productCount").val(),
                productionUnitCode:$("#productionUnitCode").val()

            }

        });

        $('#showInventoryDialog').dialog("close");
    }
    function add() {
        $.get("workSheet/clearSession.do",function(result){
            location.reload();
        })
    }
    //保存入库申请单(新增和查看)
    function saveworkSheet(){
        $.iMessager.confirm("提示","确认执行保存操作吗？",function(r){
            if(r){
                if(!requestNo){
                    save();
                }else{
                    updata();
                }
            }
        });
    }
    function save(){
        var productionUnitId = $("#productionUnitId").val();
        if(!productionUnitId){
            alert("请选择生产单元!");
            return false;
        }

        var workPieceCode = $("#workPieceCode").val();
        if(!workPieceCode){
            alert("请选择工件代码!");
            return false;
        }

        var manufactureDate = $("#manufactureDate").val();
        if(!manufactureDate){
            alert("请输入生产日期!");
            return false;
        }

        var productCount = $("#productCount").val();
        if(!productCount){
            alert("请输入生产总量!");
            return false;
        }

        if(productCount<=0){
            alert("生产总量必须大于0!");
            return false;
        }

        $.get("workSheet/addWorkSheet.do",{
            no: $("#no").val(),
            manufactureDate:manufactureDate,
            workPieceName:$('#workPieceName').val(),
            unitType:$('#unitType').val(),
            graphNumber:$('#graphNumber').val(),
            customerGraphNumber:$('#customerGraphNumber').val(),
            version:$('#version').val(),
            productCount:$('#productCount').val(),
            batchNumber:$('#batchNumber').iTextbox("getValue"),
            stoveNumber:$('#stoveNumber').iTextbox("getValue"),
			Define33:$('#Define33').iTextbox("getValue"),
			moallocateInvcode:$('#invcode').iTextbox("getValue"),
			moallocateQty:$('#Qty').iTextbox("getValue"),
			LotNo:$('#LotNo').iTextbox("getValue"),

            productionUnitId:$('#productionUnitId').val(),
            productionUnitName:$('#productionUnitName').val(),
            productionUnitCode:$('#productionUnitCode').val(),
            workSheetType:$('#workSheetType').val(),
            note:"",
            workPieceCode:workPieceCode,
            unitCode:$("#unitCode").val(),
            unitName:$("#unitName").val(),
            locationCodes:$("#locationCodes").val()
        },function(result){
            if(result.success){
                if(result.changeNoMsg){
                    $.iMessager.alert("提示",result.changeNoMsg,'messager-info',function () {
                        location.reload();
                    });

                }else{
                    $.iMessager.alert("提示","保存成功!",'messager-info',function () {
                        location.reload();
                    });
                }
            }else{
                $.iMessager.alert("提示",result.msg);
            }

        });
    }

    function updata(){
        var productionUnitId = $("#productionUnitId").val();
        if(!productionUnitId){
            alert("请选择生产单元!");
            return false;
        }

        var workPieceCode = $("#workPieceCode").val();
        if(!workPieceCode){
            alert("请选择工件代码!");
            return false;
        }

        var manufactureDate = $("#manufactureDate").val();
        if(!manufactureDate){
            alert("请输入生产日期!");
            return false;
        }

        var productCount = $("#productCount").val();
        if(!productCount){
            alert("请输入生产总量!");
            return false;
        }

        if(productCount<=0){
            alert("生产总量必须大于0!");
            return false;
        }

        $.get("workSheet/updataWorkSheet.do",{
            id:$("#id").val(),
            no: $("#no").val(),
            manufactureDate:manufactureDate,
            workPieceName:$('#workPieceName').val(),
            unitType:$('#unitType').val(),
            graphNumber:$('#graphNumber').val(),
            customerGraphNumber:$('#customerGraphNumber').val(),
            version:$('#version').val(),
            productCount:$('#productCount').val(),
            batchNumber:$('#batchNumber').val(),
            stoveNumber:$('#stoveNumber').val(),
            productionUnitId:$('#productionUnitId').val(),
            productionUnitName:$('#productionUnitName').val(),
            productionUnitCode:$('#productionUnitCode').val(),
            workSheetType:$('#workSheetType').val(),
            note:"",
            workPieceCode:workPieceCode,
			locationCodes:$("#locationCodes").val(),

			Define33:$('#Define33').iTextbox("getValue"),
			moallocateInvcode:$('#invcode').iTextbox("getValue"),
			moallocateQty:$('#Qty').iTextbox("getValue"),
			LotNo:$('#LotNo').iTextbox("getValue"),
        },function(result){
            if(result.success){
                $.iMessager.alert("提示","保存成功!",'messager-info',function () {
                    location.reload();
                });
            }else{
                $.iMessager.alert("提示",result.msg);
            }

        });
    }


    function deleteDetail(){
        if($("#workSheetDetail").iDatagrid("getSelected")!=null){
            if(!requestNo){
                $.get("workSheet/deleteWorkSheetDetailInMemory.do",{
                    id:$("#workSheetDetail").iDatagrid("getSelected").id
                },function(result){
                    if(result.success){
                        $('#workSheetDetail').iDatagrid('load','workSheet/querySessionWorkSheetDetail.do');
                    }
                });
            }else{
                //查看删除
                $.get("workSheet/deleteWorkSheetDetail.do",{
                    id:$("#workSheetDetail").iDatagrid("getSelected").id
                },function(result){
                    if(result.success){
                        $('#workSheetDetail').iDatagrid('load');
                    }
                });
            }

        }else{
            $.iMessager.alert('提示','请先勾选要删除的数据');
        }
    }


    //开工
    function start(){
        $.get("workSheet/start.do",{
            id:$("#id").val()
        },function(result){
            if(result.statusCode==200){
                $.iMessager.alert(result.title,result.message);
                location.reload();
            }else{
                $.iMessager.alert(result.title,result.message);
            }
        });
    }

    //停工
    function stop(){
        $.get("workSheet/stop.do",{
            id:$("#id").val()
        },function(result){
            if(result.statusCode==200){
                $.iMessager.alert(result.title,result.message);
                location.reload();
            }else{
                $.iMessager.alert(result.title,result.message);
            }
        });
    }
    //完工
    function over(){
        $.get("workSheet/over.do",{
            id:$("#id").val()
        },function(result){
            if(result.statusCode==200){
                $.iMessager.alert(result.title,result.message);
                location.reload();
            }else{
                $.iMessager.alert(result.title,result.message);
            }
        });
    }
</script>
<div id="showInventoryDialog"></div>
<div id="showLocationDialog"></div>
<input type="hidden" name="id"
	   id="id">
<input type="hidden" id="workSheetNo">
<div data-toggle="topjui-layout" data-options="fit:true">
	<div
			data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
		<form id="addWorkSheetForm" method="post">
			<div style="height:40px;width:100%;margin-top: 5px;margin-left: 10px;">
<sec:authorize access="hasAuthority('ADD_WORKSHEETDETAIL')">
				<a href="javascript:void(0)"
				   data-toggle="topjui-menubutton"
				   data-options="iconCls:'fa fa-plus'" onclick="add()">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SAVE_WORKSHEETDETAIL')">
				<a href="javascript:void(0)"
				   data-toggle="topjui-menubutton"
				   data-options="iconCls:'fa fa-save'" id="saveSheet" onclick="saveworkSheet()">保存</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PRINT_WORKSHEETDETAIL')">
				<a href="javascript:void(0)"
				   data-toggle="topjui-menubutton"
				   data-options="iconCls:'fa fa-print'" id="printSheet" onclick="printSheet()">打印</a>
</sec:authorize>
<sec:authorize access="hasAuthority('START_WORKSHEETDETAIL')">
				<a href="javascript:void(0)"
				   data-toggle="topjui-menubutton"
				   data-options="iconCls:'fa fa-play'" id="startSheet" onclick="start()">开工</a>
</sec:authorize>
<sec:authorize access="hasAuthority('STOP_WORKSHEETDETAIL')">
				<a href="javascript:void(0)"
				   data-toggle="topjui-menubutton"
				   data-options="iconCls:'fa fa-stop'" id="stopSheet" onclick="stop()">停工</a>
</sec:authorize>
<sec:authorize access="hasAuthority('FINISH_WORKSHEETDETAIL')">
				<a href="javascript:void(0)"
				   data-toggle="topjui-menubutton"
				   data-options="iconCls:'fa fa-check'" id="overSheet" onclick="over()">完工</a>
</sec:authorize>
			</div>
			<div title="" data-options="iconCls:'fa fa-th'">
				<div class="topjui-fluid">
					<div
							style="height: 30px; width: 100%; text-align: center; font-size: 1.5em; font-weight: bold; margin: 20px auto;margin-top: 0px;">
						生产任务单</div>
					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">单号</label>
							<div class="topjui-input-block">
								<input type="text" name="no" data-toggle="topjui-textbox"
									   data-options="required:true" id="no">
							</div>
						</div>

						<div class="topjui-col-sm3">
							<label class="topjui-form-label">生产日期</label>
							<div class="topjui-input-block">
								<input type="text" name="manufactureDate" style="width: 100%;"
									   data-toggle="topjui-datetimebox"
									   data-options="required:true,showSeconds:true"
									   id="manufactureDate">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">工单类型</label>
							<div class="topjui-input-block">
								<input type="text" name="workSheetType" data-toggle="topjui-combobox" value="common"
									   data-options="
										valueField: 'value',
										textField: 'text',
										data: [{
										    value: 'common',
										    text: '普通工单'
										},{
										    value: 'repair',
										    text: '返修工单'
										}]" id="workSheetType">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">工单状态</label>
							<div class="topjui-input-block">
								<input type="text" name="status" data-toggle="topjui-textbox"
									   data-options="required:false" id="status" value="计划" readonly="readonly">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">工件代码</label>
							<div class="topjui-input-block">
								<!-- <input type="text" name="workPieceCode" data-options="required:true" data-toggle="topjui-textbox"
									   id="workPieceCode" > -->
								<input id="inventoryCodeSearch" data-toggle="topjui-textbox">
								<input type="hidden" id="workPieceCode">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">工件名称</label>
							<div class="topjui-input-block">
								<input type="hidden" name="workpieceId" id="workpieceId"/>

								<input type="text" name="workPieceName"
									   data-toggle="topjui-textbox" data-options="required:false"
									   id="workPieceName" readonly="readonly" />
							</div>
						</div>
						<input type="hidden" name="unitCode" id="unitCode"/>
						<input type="hidden" name="unitName" id="unitName"/>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">规格型号</label>
							<div class="topjui-input-block">
								<input type="text" name="unitType" data-toggle="topjui-textbox"
									   data-options="required:false" id="unitType" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">图号</label>
							<div class="topjui-input-block">
								<input type="text" name="graphNumber"
									   data-toggle="topjui-textbox" data-options="required:false"
									   id="graphNumber" readonly="readonly">
							</div>
						</div>
						<input type="hidden" name="customerGraphNumber" id="customerGraphNumber" >
						<input type="hidden" name="version" id="version" >

					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">生产总量</label>
							<div class="topjui-input-block">
								<input type="text" name="productCount"
									   data-toggle="topjui-textbox" data-options="required:true"
									   id="productCount" style="width:100%;" value="0" onchange="countChange()">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">生产单元</label>
							<div class="topjui-input-block">
								<input type="hidden" name="productionUnitId"
									   id="productionUnitId">
								<input type="hidden" name="productionUnitCode"
									   id="productionUnitCode">
								<input type="text"  data-toggle="topjui-textbox" data-options="required:false"
									   name="productionUnitName" id="productionUnitName" readonly="readonly">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">批号</label>
							<div class="topjui-input-block">
								<input type="text" name="batchNumber"
									   data-toggle="topjui-textbox" data-options="required:false"
									   id="batchNumber">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">材料编号</label>
							<div class="topjui-input-block">
								<input type="text" name="stoveNumber"
									   data-toggle="topjui-textbox" data-options="required:false"
									   id="stoveNumber">
							</div>
						</div>
					</div>

					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">材料数量</label>
							<div class="topjui-input-block">
								<input type="text" name="Define33"
									   data-toggle="topjui-textbox" data-options="required:false"
									   id="Define33" style="width:100%;" >
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">子件代码</label>
							<div class="topjui-input-block">
								<input type="text"  data-toggle="topjui-textbox" data-options="required:false"
									   name="invcode" id="invcode">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">子件数量</label>
							<div class="topjui-input-block">
								<input type="text" name="Qty"
									   data-toggle="topjui-textbox" data-options="required:false"
									   id="Qty">
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">子件批号</label>
							<div class="topjui-input-block">
								<input type="text" name="LotNo"
									   data-toggle="topjui-textbox" data-options="required:false"
									   id="LotNo">
							</div>
						</div>
					</div>
					<div class="topjui-row">
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">货位</label>
							<div class="topjui-input-block">
								<!-- <input type="text" name="workPieceCode" data-options="required:true" data-toggle="topjui-textbox"
									   id="workPieceCode" > -->
								<input id="locationCodes" data-toggle="topjui-textbox">
								<!-- <input type="hidden" id="locationCodes"> -->
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">报工总数</label>
							<div class="topjui-input-block">
								<!-- <input type="text" name="workPieceCode" data-options="required:true" data-toggle="topjui-textbox"
									   id="workPieceCode" > -->
								<input id="sumOfAllJobBooking" data-toggle="topjui-textbox">
								<!-- <input type="hidden" id="locationCodes"> -->
							</div>
						</div>
						<div class="topjui-col-sm3">
							<label class="topjui-form-label">入库总数</label>
							<div class="topjui-input-block">
								<!-- <input type="text" name="workPieceCode" data-options="required:true" data-toggle="topjui-textbox"
									   id="workPieceCode" > -->
								<input id="sumOfAllInWarehouse" data-toggle="topjui-textbox">
								<!-- <input type="hidden" id="locationCodes"> -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</div>
	<div data-options="region:'south',fit:false,split:true,border:false"
		 style="height: 65%">
		<div data-toggle="topjui-tabs"
			 data-options="id:'southTabs',
                     fit:true,
                     border:false
                    ">
			<div title="工序流转" data-options="id:'tab0',iconCls:'fa fa-th'">
				<table id="workSheetDetail"></table>
			</div>
			<div title="领料状况" data-options="id:'tab1',iconCls:'fa fa-th'">
				<table id="MaterialRequisitionDetail"></table>
			</div>
			<div title="报工状况" data-options="id:'tab1',iconCls:'fa fa-th'">
				<table id="JobBookingFormDetail"></table>
			</div>
		</div>
	</div>
</div>
<!-- 工具按钮 -->
<div id="workSheetDetail-toolbar" class="topjui-toolbar"
	 data-options="grid:{
           type:'datagrid',
           id:'workSheetDetail'
       }">
	<a href="javascript:void(0)" data-toggle="topjui-menubutton"
	   data-options="
       extend: '#workSheetDetail-toolbar',
       iconCls:'fa fa-trash'" onclick="deleteDetail()">删除</a>
</div>

<div style="display:none;width:210mm;padding:10px;" id="printDiv">
	<div style="width:100%;font-weight: bold;text-align: center;font-size: 21px;">工序流转卡</div>
	<div style="width:100%;height:50mm;">
		<div style="float:left;height:100%;width:25%;margin-left:5mm;text-align: center">
			<img src="" style="margin:auto;height:150px;width:150px;" id="qrCode"/>
			<div id="no4Print" style="width:100%;text-align: center;margin-top:20px;font-size:14px;"></div>
		</div>
		<div style="float:left;height:100%;width:70%;margin-left:5mm;">
			<table style="width:100%;margin-top:10mm;height:150px;" id="printTable">
				<tr>
					<th>代码:</th>
					<td id="workPieceCode4Print">
					</td>
					<th>生产日期:</th>
					<td id="manufactureDate4Print"></td>
					
				</tr>
				<tr>
					<th>名称:</th>
					<td id="workPieceName4Print"></td>

					<th>生产单元:</th>
					<td id="productionUnitName4Print"></td>
				</tr>
				<tr>
					<th>规格:</th>
					<td id="unitType4Print">
					</td>
					<th>材料编号:</th>
					<td id="stoveNumber4Print"></td>
				</tr>
				<tr>
					<th>数量:</th>
					<td id="productCount4Print">
					</td>
					<th>子件数量:</th>
					<td id="moallocateQty"></td>
				</tr>
				<tr>
					<th>批号:</th>
					<td id="batchNumber4Print"></td>
					<th>子件代码:</th>
					<td id="moallocateInvcode"></td>
				</tr>
			</table>
		</div>
	</div>
	<table id="workSheetDetail4Print" style="width:100%;margin-top:20px;">
		<thead>
		<tr>
			<th style="width:10mm;">序号</th>
			<th style="width:30mm;">工序代码</th>
			<th style="width:30mm;">工序名称</th>
			<th style="width:30mm;">设备代码</th>
			<th style="width:30mm;">设备名称</th>
			<th style="width:20mm;">数量</th>
			<th style="width:30mm;"></th>
			<th style="width:30mm;"></th>
		</tr>
		</thead>
		<tbody id="contentBody"></tbody>
	</table>
</div>


<div id="printOrderDiv" style="width:241mm;height: 140mm;padding:10px;display:none ;position: absolute;">
	<img src="" style="height:22mm;width:22mm;top: 20mm;right: 22mm;position: absolute;" id="qrCode1"/>
	<div style="text-align: center;font-size: 16pt;margin-top: 15mm;width: 100%;">江苏南洋中京科技有限公司</div>
	<div style="text-align: center;font-size: 20pt;font-weight: bold;margin-top: 7mm;width: 100%;">锯料生产指令单</div>
	<table style="width: 200mm;margin-left: 20mm;margin-top: 5mm;" border="1" cellspacing="0">
		<thead style="background-color: #6a6a6a;">
			<tr>
				<th>生产令</th>
				<th>物料编码</th>
				<th>物料名称</th>
				<th>规格/型号</th>
				<th>下达数量</th>
				<th>要求完工日期</th>
				<th>材料编码</th>
				<th>料耗(KG)</th>
				<th>重量</th>
				<th>备注 库位</th>
			</tr>
		</thead>
		<tbody id="tbodyPrint">

		</tbody>
	</table>
	<div style="margin-left: 20mm;width: 200mm;margin-top: 10mm;">
		<div class="printLine">编制:<span id="makeDocumenter"></span></div>
		<div class="printLine">审核:</div>
		<div class="printLine">发料:</div>
		<div class="printLine">生产签收:</div>
	</div>
	<div style="margin-left: 20mm;width: 200mm;margin-top: 7mm;">
		<div class="printLine">日期:<span id="makeDocumentDate"></span></div>
		<div class="printLine">日期:</div>
		<div class="printLine">日期:</div>
		<div class="printLine">日期:</div>
	</div>
	<div style="font-size: 10pt;margin-left: 20mm;width: 200mm;margin-top: 12mm;">
		备注:白联下料车间存根，红联保管员存根，黄联计划存根
	</div>
</div>


<div id="printDD" data-toggle="topjui-dialog" title="提示" style="width:400px;height:200px;" data-options="resizable:true,modal:true,title:'提示',onBeforeDestroy:function(){return false}">
	<div style="text-align: center; font-size: 18px;margin-top: 30px;">请选择打印内容</div>
	<div style="margin-top: 30px;margin-left: 80px;">
		<div href="#" class="l-btn topjui-btn-green l-btn-small" style="height: 30px;display: inline-block;" onclick="printWorksheet()">
		<span class="l-btn-left l-btn-icon-left" style="margin-top: 3px;">
			<span class="l-btn-text" style="margin:0 14px 0 14px;">工序流转卡</span>
		</span>
		</div>
		<div href="#" class="l-btn topjui-btn-green l-btn-small" style="height: 30px;margin-left: 10px;display: inline-block;" onclick="printOrder()">
		<span class="l-btn-left l-btn-icon-left" style="margin-top: 3px;">
			<span class="l-btn-text" style="margin:0 14px 0 14px;">锯料生产指令单</span>
		</span>
		</div>
	</div>

</div>


<style>
	@media print {
		table thead tr th {
			font-size: 13pt;font-weight: bold;
		}
		table tbody tr th {
			font-size: 13pt;
		}
		.printLine{
			width: 45mm;display: inline-block;font-size: 13pt;
		}
	}
</style>