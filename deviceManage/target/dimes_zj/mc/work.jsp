<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="head.jsp"%>
<script src="mc/assets/js/bootstrap-treeview.min.js"></script>
<style type="text/css">
	/**bootstrap table 选中行颜色*/
	.fixed-table-container tbody .selected td{background-color:#28BA69}
	.ant-btn {
		line-height: 1.499;
		position: relative;
		display: inline-block;
		font-weight: 400;
		white-space: nowrap;
		text-align: center;
		background-image: none;
		border: 1px solid transparent;
		-webkit-box-shadow: 0 2px 0 rgba(0,0,0,0.015);
		box-shadow: 0 2px 0 rgba(0,0,0,0.015);
		cursor: pointer;
		-webkit-transition: all .3s cubic-bezier(.645, .045, .355, 1);
		transition: all .3s cubic-bezier(.645, .045, .355, 1);
		-webkit-user-select: none;
		-moz-user-select: none;
		-ms-user-select: none;
		user-select: none;
		-ms-touch-action: manipulation;
		touch-action: manipulation;
		height: 50px;
		width:40%;
		padding: 0 15px;
		font-size: 14px;
		border-radius: 4px;
		color: rgba(0,0,0,0.65);
		background-color: #fff;
		border-color: #d9d9d9;
	}

	.table1 td, .table1 th {
		border: 1px solid #D8BFD8;
		font-size: 14px;
		text-align: center;
		height: 50px;
	}

</style>
<script type="text/javascript">
    var classesCode ="<%=request.getParameter("classesCode")%>";
    var _deviceCode="";
    var serialNub;
    var arrcode=[];
    var arrid=[];

    var equipmentList=[];
	var usageRateList=[];

    $.ajaxSetup({
        cache : false
    });

    $(function() {
        //生产单元tree
        var treedata =[];
        $.ajax({
            type : "post",
            url : contextPath + "productionUnit/queryTopProductionUnits.do",
            data : {},
            cache:false,
            dataType : "json",
            success : function(data) {
                //console.log(JSON.stringify(data));
                $.each(data,function(index, Type) {
                    getData(index,Type,treedata);
                })
                $('#tree').treeview({
                    data: treedata,//节点数据
                    color:"#000000",
                    backColor:"#FFFFFF",
                    borderColor:"#FFFFFF"
                });
            }
        })
        //初始化工单表列
        initWorkSheetTable();
        //初始化tab
        initTab();
        //初始化报工列表
        initCompeteTable();
        $("td,th").addClass("text-center");

        //材料条码窗口改变事件
        $("#barCode").keydown(function(event) {
            if (event.keyCode == 13) {
                getBoxbar();
            }
        })
        //
        $("#barListTable").bootstrapTable({
            idField : 'id',
            url : "materialRequisitionDetail/queryByWorkSheetNo.do",
            cache: false,
            striped : false, //隔行换色
            clickToSelect : true,
            checkboxHeader : true,
            height:430,
            columns : [/*  {
                checkbox : true,
                field : '',
                title : ''
            }, */ {
                field : 'barCode',
                title : '材料条码'
            }, {
                field : 'inventoryCode',
                title : '物料代码'
            }, {
                field : 'allClassCodes',
                title : '班次代码',
                width : 80,
               	formatter : function(value, row, index) {
                   if (!value) {
                       return "";
                   }
                   return value;
               }
            }, {
                field : 'className',
                title : '班次名称',
                width : 80,
                	formatter : function(value, row, index) {
                        if (!value) {
                            return "";
                        }
                        return value;
                    }
            }, {
                field : 'surplusNum',
                title : '余量',
                formatter : function(value, row, index) {
                    if (!value) {
                        return 0;
                    }
                    return value.toFixed(2);
                }
            },{
                field : 'amount',
                title : '条码数量',
                formatter : function(value, row, index) {
                    if (!value) {
                        return 0;
                    }
                    return value;
                }
            }],
            toolbar : '#userListToolbar'
        });

        $("#specialBarListTable").bootstrapTable({
            idField : 'id',
            url : "materialRequisitionDetail/queryByWorkSheetNo.do",
            cache: false,
            striped : false, //隔行换色
            clickToSelect : true,
            checkboxHeader : true,
            height:430,
            columns : [ {
                checkbox : true,
                field : '',
                title : ''
            }, {
                field : 'barCode',
                title : '材料条码'
            }, {
                field : 'inventoryCode',
                title : '物料代码'
            }, {
                field : 'surplusNum',
                title : '余量',
                formatter : function(value, row, index) {
                    if (!value) {
                        return 0;
                    }
                    return value.toFixed(2);
                }
            },{
                field : 'amount',
                title : '条码数量',
                formatter : function(value, row, index) {
                    if (!value) {
                        return 0;
                    }
                    return value;
                }
            }],
            toolbar : '#specialUserListToolbar'
        });
    });
    //弹出添加用户的窗口
    function showAddbarDialog(){
        $('#showUsersListTable').bootstrapTable('refresh');
        $("#addbarDlg").modal();
    }

    //弹出添加用户的窗口
    function showSpecialAddbarDialog(){
        $('#showSpecialUsersListTable').bootstrapTable('refresh');
        $("#specialAddbarDlg").modal();
    }
    //开工
    var workSheet;
    function start() {
        selections = $("#showWorkSheetsListTable").bootstrapTable("getSelections");

		workSheet = selections[0];
		$.get(contextPath + "mcWorkSheet/queryIshaveNotMapping.do",{id:workSheet.id},function(result){
			if(allowMultiWorkSheetRunning ) {
				if (!selections || selections.length <= 0) {
					$("#alertText").text("请选择要开工的工单!");
					$("#alertDialog").modal();
					return false;
				} else {
					//判断当前工单是否在加工中
					if (workSheet.status == "1") {
						$("#alertText").text("该工单已在加工中!");
						$("#alertDialog").modal();
						return false;
					} else {
						if(result.success){
							$("#workNameStart").html('您确认将批次"' + workSheet.batchNumber + '" 的工单状态切换为 "开工" 吗？');
							$("#StartConfirmDlg").modal('show');
						}else{
							$("#workNameStart").html(result.msg);
							$("#StartConfirmDlg").modal('show');
						}

					}
				}
			}else{
				if (existence) {
					$("#alertText").text("已有工单在加工中!");
					$("#alertDialog").modal();
					return false;
				}
				if (selections.length <= 0) {
					$("#alertText").text("请选择开工的工单!");
					$("#alertDialog").modal();
					return false;
				}
				if (workSheet.status == '1') {
					$("#alertText").text("该工单已在加工中!");
					$("#alertDialog").modal();
					return false;
				}
				if(result.success){
					$("#workNameStart").html('您确认将批次"' + workSheet.batchNumber + '" 的工单状态切换为 "开工" 吗？');
					$("#StartConfirmDlg").modal('show');
				}else{
					$("#workNameStart").html(result.msg);
					$("#StartConfirmDlg").modal('show');
				}

			}
		});

    }
    //复位
    function reset() {
        $.get(contextPath + "opcMapping/reset.do", function(data) {
            $("#alertTitle").text("提示");
            $("#alertText").text("已复位");
            $("#alertDialog").modal();
        });
    }

    //停工
    function stop() {
        if(allowMultiWorkSheetRunning ){
            selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
            if(!selections || selections.length<=0){
                $("#alertText").text("请选择要停工的工单!");
                $("#alertDialog").modal();
                return false;
            }else{
                //判断当前工单是否在加工中
                workSheet = selections[0];
                if(workSheet.status!="1"){
                    $("#alertText").text("该工单并未开工，不能执行停工操作!");
                    $("#alertDialog").modal();
                    return false;
                }else{
                    $("#workNameStop").html('您确认将批次"'+workSheet.batchNumber+'" 的工单状态切换为 "停工" 吗？');
                    $("#StopConfirmDlg").modal('show');
                }
            }
        }else if (!existence) {
            $("#alertText").text("该生产单元暂无工单在加工，请确认后再试!");
            $("#alertDialog").modal();
            return false;
        }else{
            selections = $("#showWorkSheetsListTable").bootstrapTable("getData");
            workSheet = selections[0];
            $("#workNameStop").html('您确认将批次"'+workSheet.batchNumber+'" 的工单状态切换为 "停工" 吗？');
            $("#StopConfirmDlg").modal('show');
        }
    }
    //停工确认
    function stopwork() {
        if(allowMultiWorkSheetRunning) {
            $.get(contextPath + "mcWorkSheet/stop.do",{no:workSheet.no},function(data){
                refreshWorkSheetsTable(workSheet.deviceSiteCode);
            });
        }else {
            $.get(contextPath + "mcWorkSheet/stopWork.do", {
                deviceSiteCode: $("#currentDeviceSiteCode").val()
            }, function (data) {
                refreshWorkSheetsTable(workSheet.deviceSiteCode);
            });
        }
    }
    //完工
    function completed() {
        if(allowMultiWorkSheetRunning ){
            selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
            if(!selections || selections.length<=0){
                $("#alertText").text("请选择要完工的工单!");
                $("#alertDialog").modal();
                return false;
            }else{
                //判断当前工单是否在加工中
                workSheet = selections[0];
                if(workSheet.status!="1"){
                    $("#alertText").text("该工单并未开工，不能执行完工操作!");
                    $("#alertDialog").modal();
                    return false;
                }else{
                    if(workSheet.productionCount>workSheet.reportCount){
                        $("#workNameCompleted").html('该工单的完工数小于计划数,'+'您确认将批次"'+workSheet.batchNumber+'" 的工单状态切换为 "完工" 吗？');
                    }else{
                        $("#workNameCompleted").html('您确认将批次"'+workSheet.batchNumber+'" 的工单状态切换为 "完工" 吗？');
                    }
                    $("#CompletedConfirmDlg").modal('show');
                }
            }
        } else if (!existence) {
            $("#alertText").text("该生产单元暂无工单在加工，请确认后再试!");
            $("#alertDialog").modal();
            return false;
        }else{
            selections = $("#showWorkSheetsListTable").bootstrapTable("getData");
            workSheet = selections[0];
            if(workSheet.productionCount>workSheet.reportCount){
                $("#workNameCompleted").html('该工单的完工数小于计划数,'+'您确认将批次"'+workSheet.batchNumber+'" 的工单状态切换为 "完工" 吗？');
            }else{
                $("#workNameCompleted").html('您确认将批次"'+workSheet.batchNumber+'" 的工单状态切换为 "完工" 吗？');
            }
            $("#CompletedConfirmDlg").modal('show');
        }
    }
    //完工确认
    function completedwork() {
        if(allowMultiWorkSheetRunning) {
            $.get(contextPath + "mcWorkSheet/over.do",{no:workSheet.no,deviceSiteCode:workSheet.deviceSiteCode},function(data){
                refreshWorkSheetsTable(workSheet.deviceSiteCode);
            });
        }else{
            $.get(contextPath + "mcWorkSheet/completedWork.do",{
                deviceSiteCode : $("#currentDeviceSiteCode").val()
            }, function(data) {
                refreshWorkSheetsTable(workSheet.deviceSiteCode);
            });
        }
    }

    //开工确认
    function isAllMapping(){
		$.get(contextPath + "mcWorkSheet/queryIshaveNotMapping.do",{id:workSheet.id},function(data){
			if (data.statusCode==200) {
				refreshWorkSheetsTable(workSheet.deviceSiteCode);
			} else {
				$("#alertText").text(data.message);
				$("#alertDialog").modal();
			}
		});
	}


    //开工确认
    function startWork() {
        if(allowMultiWorkSheetRunning) {
            $.get(contextPath + "mcWorkSheet/start.do",{no:workSheet.no},function(data){
                if (data.statusCode==200) {
                    refreshWorkSheetsTable(workSheet.deviceSiteCode);
                } else {
                    $("#alertText").text(data.message);
                    $("#alertDialog").modal();
                }
            });
        }else {
            $.get(contextPath + "mcWorkSheet/startWork.do", {
                id: workSheet.id
            }, function (data) {
                if (data.success) {
                    refreshWorkSheetsTable(workSheet.deviceSiteCode);
                } else {
                    $("#alertText").text(data.msg);
                    $("#alertDialog").modal();
                }
            });
        }
    }
    //报工  showCompleteDialog
    var workSheetRecordNo = ""; //记录工单号
    var workSheetRecordBatchNumber = ""; //记录修改过后的批号
    function report() {
    	$("#sumCInvDefine14").val('');
        if(allowMultiWorkSheetRunning){
            selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
            if(!selections || selections.length<=0){
                $("#alertText").text("请选择要报工的工单!");
                $("#alertDialog").modal();
                return false;
			}else{
                if(selections[0].status!="1"){
                    $("#alertText").text("该工单未开工，不能报工!");
                    $("#alertDialog").modal();
				}else{
                    showCompleteDialog();
                }
			}
		}else {
            if (!existence) {
                $("#alertText").text("该生产单元暂无工单在加工，请确认后再试!");
                $("#alertDialog").modal();
                return false;
            }
            showCompleteDialog();
        }
    }

    //特殊报工  showCompleteDialog
    function specialReport() {
    	$("#sumCInvDefine14").val('');
        if(allowMultiWorkSheetRunning){
            selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
            if(!selections || selections.length<=0){
                $("#alertText").text("请选择要报工的工单!");
                $("#alertDialog").modal();
                return false;
			}else{
                if(selections[0].status!="1"){
                    $("#alertText").text("该工单未开工，不能报工!");
                    $("#alertDialog").modal();
				}else{
                    showSpecialCompleteDialog();
                }
			}
		}else {
            if (!existence) {
                $("#alertText").text("该生产单元暂无工单在加工，请确认后再试!");
                $("#alertDialog").modal();
                return false;
            }
            showSpecialCompleteDialog();
        }
    }
    //换线
    function change() {
        //获取选中的数据
        var selections = $("#showWorkSheetsListTable").bootstrapTable(
            "getSelections");
        workSheet = selections[0];
        if (workSheet.status != '0') {
            $("#alertText").text("当前工单已开工，不能更换生产单元!");
            $("#alertDialog").modal();
            return false;
        }
        showupdateProduction();
    }
    //查询所有设备站点，初始化tab页
    function initTab() {
        $
            .get(
                contextPath + "mcdeviceSite/getAllMCDeviceSite.do",
                function(data) {
                    var $ul = $("#myTab");
                    if (data.length > 0) {
                        for (var i = 0; i < data.length; i++) {
                            var deviceSite = data[i];
                            if (i == 0) {
                                $("#currentDeviceSiteCode").val(
                                    deviceSite.deviceSiteCode);
                                $("#showWorkSheetsListTable")
                                    .bootstrapTable(
                                        "refresh",
                                        {
                                            url : contextPath
                                                + "mcWorkSheet/queryWorkSheetByDeviceSiteCode.do",

                                            query : {
                                                deviceSiteCode : deviceSite.deviceSiteCode
                                            }
                                        });
                                selectTrueWork();
                            }
                            var $li = $('<li role="presentation"><a onclick="refreshWorkSheetsTable(\''
                                + deviceSite.deviceSiteCode
                                + '\')" href="#showWorkSheetsListPanel" aria-controls="showWorkSheetsListPanel" role="tab" data-toggle="tab">'
                                + deviceSite.deviceSiteName + '<br />' + deviceSite.deviceSiteCode
                                + '</a></li>');
                            $ul.append($li);
                        }
                        $('#myTab a:first').tab('show');
                    }
                });
    }
    //tab切换事件
    var existence;
    //是否允许多个工单同时开工
    var allowMultiWorkSheetRunning;
    function refreshWorkSheetsTable(deviceSiteCode) {
        $("#currentDeviceSiteCode").val(deviceSiteCode);
        $("#showWorkSheetsListTable").bootstrapTable(
            "refresh",
            {
                url : contextPath
                    + "mcWorkSheet/queryWorkSheetByDeviceSiteCode.do",

                query : {
                    deviceSiteCode : deviceSiteCode
                }
            });
        selectTrueWork()
    }
    //查询站点是否有加工的工单
    function selectTrueWork() {
        $.get(contextPath
            + "mcWorkSheet/judgequeryWorkSheetByDeviceSiteCode.do", {
            deviceSiteCode : $("#currentDeviceSiteCode").val()
        }, function(data) {
            existence = data.existence;
            allowMultiWorkSheetRunning=data.allowMultiWorkSheetRunning;
        });
    }
    //初始化工单列表table
    function initWorkSheetTable() {
        $("#showWorkSheetsListTable").bootstrapTable({
            height : longTableHeight,
            idField : 'id',
            singleSelect : true,
            clickToSelect : true,
            cache : false,
            striped : true, //隔行换色
            rowStyle : function(row, index) {
                if (row.status == '1') {
                    return {
                        css : {
                            "background-color" : "#AED0DD"
                        }
                    };
                } else {
                    return {};
                }
            },
            columns : [
                {
                    radio : true,
                    field : '',
                    title : ''
                },
                {
                    field : 'manufactureDate',
                    title : '生产日期',
                    formatter : function(value, row, index) {
                        if (value) {
                            var date = new Date(value);
                            return date.getFullYear() + '-'
                                + (date.getMonth() + 1) + '-'
                                + date.getDate();
                        }
                    }
                }, {
                    field : 'no',
                    title : '工单号',
                }, {
                    field : 'workPieceCode',
                    title : '工件代码'
                }, {
                    field : 'workPieceName',
                    title : '工件名称',
                }, {
                    field : 'customerGraphNumber',
                    title : '客户图号'
                }, {
                    field : 'graphNumber',
                    title : '图号'
                }, {
                    field : 'batchNumber',
                    title : '批号'
                }, {
                    field : 'stoveNumber',
                    title : '材料编号'
                }, {
                    field : 'version',
                    title : '版本号'
                }, {
                    field : 'productionCount',
                    title : '计划数量'
                }, {
                    field : 'status',
                    title : '状态',
                    formatter : function(value, row, index) {
                        if (value) {
                            switch (value) {
                                case '0':
                                    return '计划';
                                case '1':
                                    return '加工中...';
                                case '2':
                                    return '停工';
                                case '3':
                                    return '完工';
                            }
                        }
                    }
                } ]
        });

    }
    //初始化报工列表
    function initCompeteTable() {
        $("#completeTable").bootstrapTable(
            {
                height : 250,
                idField : 'id',
                clickToSelect : true,
                striped : true, //隔行换色
                cache : false,
                /* onEditableSave : function(field, row, oldValue, $el) {
                    alert(row.reportCount);
                }, */
                columns : [
                    {
                        field : 'manufactureDate',
                        title : '生产日期',
                        formatter : function(value, row, index) {
                            if (value) {
                                var date = new Date(value);
                                return date.getFullYear() + '-'
                                    + (date.getMonth() + 1) + '-'
                                    + date.getDate();
                            }
                        }
                    }, {
                        field : 'no',
                        title : '工单号'
                    }, {
                        field : 'workPieceCode',
                        title : '工件代码'
                    }, {
                        field : 'workPieceName',
                        title : '工件名称'
                    }, {
                        field : 'deviceSiteCode',
                        title : '站点代码'
                    }, {
                        field : 'productionCount',
                        title : '计划数量'
                    }, {
                        field : 'completeCount',
                        title : '加工数量'
                    }, {
                        field : 'reportCount',
                        title : '报工数量',
                        formatter : function(value, row, index) {
                            if (value == 0) {
                                return row.completeCount;
                            } else {
                                return value;
                            }
                        },
                        editable : {
                            mode : "inline",
                            type : 'number',
                            title : '报工数量'
                        }
                    }, {
                        field : 'customerGraphNumber',
                        title : '客户图号'
                    }, {
                        field : 'graphNumber',
                        title : '图号'
                    }, {
                        field : 'batchNumber',
                        title : '批号'
                    }, {
                        field : 'stoveNumber',
                        title : '材料编号'
                    }, {
                        field : 'version',
                        title : '版本号'
                    } ]
                /* , onEditableHidden: function(field, row, $el, reason) { // 当编辑状态被隐藏时触发
                                                if(reason === 'save') {
                                                    var $td = $el.closest('tr').children();
                                                    $td.eq(-1).html((row.price*row.number).toFixed(2));
                                                    $el.closest('tr').next().find('.editable').editable('show'); //编辑状态向下一行移动
                                                   } else if(reason === 'nochange') {
                                                    $el.closest('tr').next().find('.editable').editable('show');
                                                   }
                                                  } */

            });
        $('#completeTable').on('click', 'td:has(.editable)', function(e) {
            //e.preventDefault();
            e.stopPropagation(); // 阻止事件的冒泡行为
            $(this).find('.editable').editable('show'); // 打开被点击单元格的编辑状态
        });

    }
    //计算总料耗
    function computeSumCinvDefine14(){
        var num = $("#num").val();
        if(num){
            var sum = num*$("#cInvDefine14").val();
            $("#sumCInvDefine14").val(sum.toFixed(2));
        }
    }

    //计算总料耗
    function specialComputeSumCinvDefine14(){
        var num = $("#specialNum").val();
        if(num){
            var sum = num*$("#specialCInvDefine14").val();
            $("#specialSumCInvDefine14").val(sum.toFixed(2));
        }
    }

    //弹出完工对话框
    function showCompleteDialog(action) {
        serialNub=1;
        $("#boxBarTbItem").empty();
        $("#num").val("");
        $("#barCode").val("");
        arrcode=[];
        arrid=[];
        var selections = $("#showWorkSheetsListTable").bootstrapTable("getData");
        if(allowMultiWorkSheetRunning){
            selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
		}
        workSheet = selections[0];
        $.get(contextPath + "mcuser/queryLoginMCUserByClientIp.do", function(
            data) {
            if (data.mcUser) {
                $("#username").val(data.mcUser.employeeName);
            } else {
                $("#alertText").text("请先登录系统！");
                $("#alertDialog").modal();
            }
        });
        $("#reporWork").removeAttr("disabled"); //使按钮能够被点击
        $.get(contextPath + "mcjobBookingForm/queryBoxNo.do",{
            workNo:workSheet.no,
            processCode:workSheet.processCode
        }, function(boxnum) {
            $("#boxnum").val(boxnum+1);
        });

        $("#workPieceCode").val(workSheet.workPieceCode);
        $("#workPieceName").val(workSheet.workPieceName);
        $("#unitType").val(workSheet.unitType);
        if(workSheetRecordNo == workSheet.no){
            $("#batchNumber").val(workSheetRecordBatchNumber);
        }else{
            $("#batchNumber").val(workSheet.batchNumber);
        }
        $("#deviceCode").val($("#currentDeviceSiteCode").val());
        $("#processName").val(workSheet.processName);
        $("#workPieceName").val(workSheet.workPieceName);
        $("#graphNumber").val(workSheet.graphNumber);
        $("#stoveNumber").val(workSheet.stoveNumber);

        $.get("inventory/queryByInventoryCode.do",{inventoryCode:workSheet.workPieceCode},function(result){
            if(result){
                $("#cInvDefine14").val(result.cInvDefine14);
            }
        });
        //工单报工
        $.get("mcjobBookingForm/queryNumBerByWorkSheetNo.do",{workSheetNo:workSheet.no},function(result){
            if(result){
                $("#workSheetNum").val(result);
            }else{
                $("#workSheetNum").val(0);
            }
        });
		//获取损耗率阀值
        $.get("mcWorkSheet/queryProductionUnitByNo.do",{no : workSheet.no},function(result){
            if(result){
                $("#threshold").val(result.threshold);
            }
        });
        /*if(classesCode!="null"){*/
            $.get("classes/queryAllClassesByproductionUnitCode.do",{
                code:workSheet.productionUnitCode
            },function(data){
                /*$("#classesName").val(result.name);
                $("#classesCode").val(result.code);*/
				var htmlselect = "<option></option>";
				$.each(data,function(index, Type) {
					if(Type.withinTime){
                        htmlselect += "<option value='"+Type.code+"' selected = 'selected'>"+Type.name+"</option>";
                    }else{
                        htmlselect += "<option value='"+Type.code+"'>"+Type.name+"</option>";
                    }
				})
				$("#classesName").html(htmlselect);
				$("#classesName").selectpicker({
					noneSelectedText: '请选择',
					width:'180px'
				});
				$("#classesName").selectpicker('refresh');
				classesChange();
            })
        /*}else
            $("#classesCode").val("");*/



        $("#workPieceid").val(workSheet.id);
        $("#num").val("");

        $("#completeDialog").modal();
        $("#barListTable").empty();
        $("#barListTable")
            .bootstrapTable(
                "refresh",
                {
                    url : contextPath
                        + "materialRequisitionDetail/queryByWorkSheetNo.do",

                    query : {
                        workSheetNo : workSheet.no//workSheet.no"BG-20190725000",
                        //tablename:"JobBookingFormDetail"
                    }
                });
    }

    //弹出特殊报工框
    function showSpecialCompleteDialog() {
        serialNub=1;
        $("#specialBoxBarTbItem").empty();
        $("#specialNum").val("");
        $("#specialBarCode").val("");
        arrcode=[];
        arrid=[];
        var selections = $("#showWorkSheetsListTable").bootstrapTable("getData");
        if(allowMultiWorkSheetRunning){
            selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
		}
        workSheet = selections[0];
        $.get(contextPath + "mcuser/queryLoginMCUserByClientIp.do", function(
            data) {
            if (data.mcUser) {
                $("#specialUsername").val(data.mcUser.employeeName);
            } else {
                $("#alertText").text("请先登录系统！");
                $("#alertDialog").modal();
            }
        });
        $("#specialReporWork").removeAttr("disabled"); //使按钮能够被点击
        $.get(contextPath + "mcjobBookingForm/queryBoxNo.do",{
            workNo:workSheet.no,
            processCode:workSheet.processCode
        }, function(boxnum) {
            $("#specialBoxnum").val(boxnum+1);
        });

        $("#specialWorkPieceCode").val(workSheet.workPieceCode);
        $("#specialWorkPieceName").val(workSheet.workPieceName);
        $("#specialUnitType").val(workSheet.unitType);
        $("#specialBatchNumber").val(workSheet.batchNumber);
        $("#specialDeviceCode").val($("#currentDeviceSiteCode").val());
        $("#specialProcessName").val(workSheet.processName);
        $("#specialWorkPieceName").val(workSheet.workPieceName);
        $("#specialGraphNumber").val(workSheet.graphNumber);
        $("#specialStoveNumber").val(workSheet.stoveNumber);

        $.get("inventory/queryByInventoryCode.do",{inventoryCode:workSheet.workPieceCode},function(result){
            if(result){
                $("#specialCInvDefine14").val(result.cInvDefine14);
            }
        });

        //工单报工
        $.get("mcjobBookingForm/queryNumBerByWorkSheetNo.do",{workSheetNo:workSheet.no},function(result){
            if(result){
                $("#specialworkSheetNum").val(result);
            }else{
                $("#specialworkSheetNum").val(0);
            }
        });
      //获取损耗率阀值
        $.get("mcWorkSheet/queryProductionUnitByNo.do",{no : workSheet.no},function(result){
            if(result){
                $("#specialThreshold").val(result.threshold);
            }
        });
        /* if(classesCode!="null"){
            $.get("classes/queryClassesByCode.do",{
                code:classesCode
            },function(data){
                $("#specialClassesName").val(data.name);
                $("#specialClassesCode").val(data.code);
            })
        } */
        $.get("classes/queryAllClassesByproductionUnitCode.do",{
            code:workSheet.productionUnitCode
        },function(data){
            /*$("#classesName").val(result.name);
            $("#classesCode").val(result.code);*/
			var htmlselect = "<option></option>";
			$.each(data,function(index, Type) {
			    if(Type.withinTime){
				    htmlselect += "<option value='"+Type.code+"' selected = 'selected'>"+Type.name+"</option>";
				}else{
				    htmlselect += "<option value='"+Type.code+"'>"+Type.name+"</option>";
				}
			})

			$("#specialClassesName").html(htmlselect);
			$("#specialClassesName").selectpicker({
				noneSelectedText: '请选择',
				width:'180px'
			});
			$("#specialClassesName").selectpicker('refresh');
			specialClassesChange();
        })


        $("#specialWorkPieceid").val(workSheet.id);
        $("#specialNum").val("");

        $("#specialCompleteDialog").modal();
        $("#specialBarListTable").empty();
        $("#specialBarListTable")
            .bootstrapTable(
                "refresh",
                {
                    url : contextPath
                        + "materialRequisitionDetail/queryByWorkSheetNo.do",

                    query : {
                        workSheetNo : workSheet.no//workSheet.no"BG-20190725000",
                        //tablename:"JobBookingFormDetail"
                    }
                });
    }
    
    //弹出完工对话框
    function reporWork() {
        $("#reporWork").attr("disabled", "disabled"); //使按钮不能被点击
        num=$("#num").val();
        if (!num>0) {
            $("#alertText").text("数量值不合法!");
            $("#alertDialog").modal();
            $("#reporWork").removeAttr("disabled"); //使按钮能够被点击
            return false;
        }
        if($("#classesName").val()==""||$("#classesName").val()==null){
			$("#alertText").text("请选择班次!");
			$("#alertDialog").modal();
			$("#reporWork").removeAttr("disabled"); //使按钮能够被点击
			return false;
		}
        selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
        workSheet = selections[0];
        //消耗数量
        var checkValueObj=$(".checkValue");
        var checkValue = "";
        var checkValueSum = 0;  //消耗总数量
        if(checkValueObj && checkValueObj.length>0){
            for(var i = 0;i<checkValueObj.length;i++){
                var valstr="#boxBarTbItem tr:eq("+i+") td:eq(2)";
                checkValue+=$(checkValueObj[i]).val()+" ,";
                checkValueSum+=parseFloat($(checkValueObj[i]).val());
                if(parseFloat($(checkValueObj[i]).val())>parseFloat($(valstr).html())){
                    $("#alertText").text("消耗数量不能大于余量，请重新输入!");
                    $("#alertDialog").modal();
                    $("#reporWork").removeAttr("disabled"); //使按钮能够被点击
                    return false;
                }
            }
            checkValue =checkValue.substring(0,checkValue.length-1);
        }

        //材料条码
        var codeValue = "";
        if(arrcode && arrcode.length>0){
            for(var i = 0;i<arrcode.length;i++){
                codeValue+=arrcode[i]+" ,";
            }
            codeValue =codeValue.substring(0,codeValue.length-1);
        }
        if(!codeValue){
            $("#alertText").text("请先添加材料信息再点击确认！");
            $("#alertDialog").modal();
            $("#reporWork").removeAttr("disabled"); //使按钮能够被点击
            return false;
        }
        //判断消耗数是否合理
        var max = $("#sumCInvDefine14").val()*(1+$("#threshold").val()/100);
        var min = $("#sumCInvDefine14").val()*(1-$("#threshold").val()/100);
        if(checkValueSum>max){
        	$("#alertText").text("消耗材料数量超出上限，保存失败!");
            $("#alertDialog").modal();
            $("#reporWork").removeAttr("disabled"); //使按钮能够被点击
            return false;
        }
        if(checkValueSum<min){
        	/* if(confirm("消耗材料数量不足，是否继续保存？")){
            }else {
            	$("#reporWork").removeAttr("disabled"); //使按钮能够被点击
                return false;
            } */
        	$("#alertText").text("消耗材料数量低于最低下限，保存失败!");
            $("#alertDialog").modal();
            $("#reporWork").removeAttr("disabled"); //使按钮能够被点击
            return false;
        }
        //材料条码id
        var idValue = "";
        if(arrid && arrid.length>0){
            for(var i = 0;i<arrid.length;i++){
                idValue+=arrid[i]+" ,";
            }
            idValue =idValue.substring(0,idValue.length-1);
        }

        workSheetRecordNo = workSheet.no
        workSheetRecordBatchNumber = $("#batchNumber").val();
        $.get("mcjobBookingFormDetail/saveJobBookingForm.do",{
            jobBookingDate : getNowFormatDate(),
            workSheetId: $("#workPieceid").val(),
            batchNumber: $("#batchNumber").val(),
            deviceSiteCode:$("#deviceCode").val(),
            num:$("#num").val(),
            boxnum:$("#boxnum").val(),
            codeValues:codeValue,
            idValues:idValue,
            forJobBookingDate:$("#forJobBookingDate").val(),
            classesCode:$("#classesName").val(),
            checkValues:checkValue,
            processesCode:workSheet.processCode,
            processesName:workSheet.processName
        },function(result){
            if(result.success){
                $.get("mcjobBookingForm/audit.do",{
                    formNo : result.formNo
                },function(result){

                })
                $("#completeDialog").modal('hide');
                //alert("保存成功!");
                window.open("console/jsp/barCode_print.jsp?formNo="+result.formNo);
            }else{
                $("#reporWork").removeAttr("disabled"); //使按钮能够被点击
                alert(result.msg);
            }
        });

    }

    //弹出完工对话框
    function specialReporWork() {
    	$("#specialReporWork").attr("disabled", "disabled"); //使按钮不能被点击
        num=$("#specialNum").val();
        if (!num>0) {
            $("#alertText").text("数量值不合法!");
            $("#alertDialog").modal();
            $("#specialReporWork").removeAttr("disabled"); //使按钮能够被点击
            return false;
        }

        if($("#specialClassesName").val()==""||$("#specialClassesName").val()==null){
			$("#alertText").text("请选择班次!");
			$("#alertDialog").modal();
			$("#specialReporWork").removeAttr("disabled"); //使按钮能够被点击
			return false;
		}
        var selections = $("#showWorkSheetsListTable").bootstrapTable(
            "getData");
        workSheet = selections[0];
        //消耗数量
        var checkValueObj=$(".specialCheckValue");
        var checkValue = "";
        var checkValueSum = 0;  //消耗总数量
        if(checkValueObj && checkValueObj.length>0){
            for(var i = 0;i<checkValueObj.length;i++){
                var valstr="#specialBoxBarTbItem tr:eq("+i+") td:eq(2)";
                checkValue+=$(checkValueObj[i]).val()+" ,";
                checkValueSum+=parseFloat($(checkValueObj[i]).val());
                if(parseFloat($(checkValueObj[i]).val())>parseFloat($(valstr).html())){
                    $("#alertText").text("消耗数量不能大于余量，请重新输入!");
                    $("#alertDialog").modal();
                    $("#specialReporWork").removeAttr("disabled"); //使按钮能够被点击
                    return false;
                }
            }
            checkValue =checkValue.substring(0,checkValue.length-1);
        }
        //判断消耗数是否合理
        var max = $("#specialSumCInvDefine14").val()*(1+$("#specialThreshold").val()/100);
        var min = $("#specialSumCInvDefine14").val()*(1-$("#specialThreshold").val()/100);
        if(checkValueSum>max){
        	$("#alertText").text("消耗材料数量超出上限，保存失败!");
            $("#alertDialog").modal();
            $("#specialReporWork").removeAttr("disabled"); //使按钮能够被点击
            return false;
        }
        if(checkValueSum<min){
        	if(confirm("消耗材料数量不足，是否继续保存？")){
            }else {
            	$("#specialReporWork").removeAttr("disabled"); //使按钮能够被点击
                return false;
            }
        }
        //材料条码
        var codeValue = "";
        if(arrcode && arrcode.length>0){
            for(var i = 0;i<arrcode.length;i++){
                codeValue+=arrcode[i]+" ,";
            }
            codeValue =codeValue.substring(0,codeValue.length-1);
        }
        //材料条码id
        var idValue = "";
        if(arrid && arrid.length>0){
            for(var i = 0;i<arrid.length;i++){
                idValue+=arrid[i]+" ,";
            }
            idValue =idValue.substring(0,idValue.length-1);
        }

        $.get("mcjobBookingFormDetail/saveJobBookingForm.do",{
            jobBookingDate : getNowFormatDate(),
            workSheetId: $("#specialWorkPieceid").val(),
            batchNumber: $("#specialBatchNumber").val(),
            deviceSiteCode:$("#specialDeviceCode").val(),
            num:$("#specialNum").val(),
            boxnum:$("#specialBoxnum").val(),
            codeValues:codeValue,
            idValues:idValue,
            forJobBookingDate:$("#specialforJobBookingDate").val(),
            classesCode:$("#specialClassesName").val(),
            checkValues:checkValue,
            processesCode:workSheet.processCode,
            processesName:workSheet.processName
        },function(result){
            if(result.success){
                $.get("mcjobBookingForm/audit.do",{
                    formNo : result.formNo
                },function(result){

                })
                $("#specialCompleteDialog").modal('hide');
                //alert("保存成功!");
                window.open("console/jsp/barCode_print.jsp?formNo="+result.formNo);
            }else{
                $("#specialReporWork").removeAttr("disabled"); //使按钮能够被点击
                alert(result.msg);
            }
        });

    }

    function getNowFormatDate() {//获取当前时间
        var date = new Date();
        var seperator1 = "-";
        var seperator2 = ":";
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
        return currentdate;
    }
    //完工或报工
    function reportWorkSheet() {
        $
            .get(
                contextPath + "mcWorkSheet/reportWork.do",
                {
                    objs : JSON.stringify(objsArray)
                },
                function(data) {
                    if (data.success) {
                        existence = false;
                        $("#showWorkSheetsListTable")
                            .bootstrapTable(
                                "refresh",
                                {
                                    url : contextPath
                                        + "mcWorkSheet/queryWorkSheetByDeviceSiteCode.do",

                                    query : {
                                        deviceSiteCode : $(
                                            "#currentDeviceSiteCode")
                                            .val()
                                    }
                                });

                        $("#completeDialog").modal('hide');
                    } else {
                        $("#alertText").text(data.msg);
                        $("#alertDialog").modal();
                    }
                });
    }
    //根据材料条码添加信息
    function getBoxbar() {
        var selections = $("#showWorkSheetsListTable").bootstrapTable("getData");
        if(allowMultiWorkSheetRunning){
            selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
		}
        workSheet = selections[0];
        $
            .get(
                contextPath + "materialRequisitionDetail/queryByWorkSheetNoAndBarCode.do",
                {
                    barCode : $("#barCode").val(),
                    workSheetNo : workSheet.no
                },
                function(result) {
                    data = result[0];
                    if (data) {
                        var boxBarTb=$("#boxBarTbItem");

                         var codes=[];
                         for(var i=0;i<arrcode.length;i++){
							 codes.push(arrcode[i]);
						 }
						codes.push(data.barCode);
						$.get("boxBar/queryIsTheSameClass.do",{barCodes:JSON.stringify(codes)},function(result){
							if(result.success){
								if(arrcode.indexOf(data.barCode+"")!=-1){
									 $("#alertText").text("该原材料已存在，请核实:" + data.barCode);
									 $("#alertDialog").modal();
									return false;
								}else{
									arrcode.push(data.barCode+"");
									arrid.push(data.id+"");
									var str = '<tr>' +
											/*  '<td>' + (serialNub++) +'</td>' + */
											'<td>' + data.barCode + '</td>' +
											'<td>' + data.inventoryCode + '</td>' +
											'<td>' + data.surplusNum.toFixed(2) + '</td>' +
											'<td >'  +"<input type='number' class='checkValue' value='"+data.surplusNum.toFixed(2) +"' style='border-style:none;width:100%;height: 100%'/> "+ '</td>' +
											'<td >'  +"<input type='button'  value='"+"删除" +"' style='border-style:none;width:100%;height: 100%' onclick='deleterow(this)'/> "+ '</td>' +
											'</tr>';
									boxBarTb.append(str);
								}
							}else{
								$("#alertText").text(result.msg);
								$("#alertDialog").modal();
								return false;
							}
						})


                    } else {
                        $("#alertText").text("该原材料不存在，请核实！");
                        $("#alertDialog").modal();
                    }
                });
    }

    //根据材料条码添加信息(特殊报工)
    function specialGetBoxbar() {
        var selections = $("#showWorkSheetsListTable").bootstrapTable("getData");
        if(allowMultiWorkSheetRunning){
            selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
		}
        workSheet = selections[0];
        $
            .get(
                contextPath + "materialRequisitionDetail/queryByWorkSheetNoAndBarCode.do",
                {
                    barCode : $("#specialBarCode").val(),
                    workSheetNo : workSheet.no
                },
                function(result) {
                    data = result[0];
                    if (data) {
                        var boxBarTb=$("#specialBoxBarTbItem");

                         var codes=[];
                         for(var i=0;i<arrcode.length;i++){
							 codes.push(arrcode[i]);
						 }
						codes.push(data.barCode);
						if(arrcode.indexOf(data.barCode+"")!=-1){
							 $("#alertText").text("该原材料已存在，请核实:" + data.barCode);
							 $("#alertDialog").modal();
							return false;
						}else{
							arrcode.push(data.barCode+"");
							arrid.push(data.id+"");
							var str = '<tr>' +
									/*  '<td>' + (serialNub++) +'</td>' + */
									'<td>' + data.barCode + '</td>' +
									'<td>' + data.inventoryCode + '</td>' +
									'<td>' + data.surplusNum.toFixed(2) + '</td>' +
									'<td >'  +"<input type='number' class='specialCheckValue' value='"+data.surplusNum.toFixed(2) +"' style='border-style:none;width:100%;height: 100%'/> "+ '</td>' +
									'<td >'  +"<input type='button'  value='"+"删除" +"' style='border-style:none;width:100%;height: 100%' onclick='specialDeleterow(this)'/> "+ '</td>' +
									'</tr>';
							boxBarTb.append(str);
						}
                    } else {
                        $("#alertText").text("该原材料不存在，请核实！");
                        $("#alertDialog").modal();
                    }
                });
    }

    //根据源材料添加信息
    function addbarsBtn() {
        var selections = $("#barListTable").bootstrapTable("getSelections");
        if(selections){
            var boxBarTb=$("#boxBarTbItem");
            var str = '';
			var codes=[];
			for(var i=0;i<arrcode.length;i++){
				codes.push(arrcode[i]);
			}
			for(var i=0;i<selections.length;i++){
				var data=selections[i];
				if(codes.length>0){
					if(codes.indexOf(data.barCode)==-1){
						codes.push(data.barCode);
					}
				}else{
					codes.push(data.barCode);
				}



			}
			$.get("boxBar/queryIsTheSameClass.do",{barCodes:JSON.stringify(codes)},function(result){
				if(result.success){
					for(var i=0;i<codes.length;i++){
						if(arrcode.indexOf(codes[i])!=-1){
							/* $("#alertText").text("该原材料已存在，请核实:" + data.barCode);
                             $("#alertDialog").modal();
                             return;*/
							continue;
						}
						arrcode.push(codes[i]);
						for(var y=0;y<selections.length;y++){
							var data=selections[y];
							if(data.barCode==codes[i]){
								arrid.push(data.id);
								str += '<tr>' +
										/*  '<td>' + (serialNub++) +'</td>' +   */
										'<td>' + data.barCode + '</td>' +
										'<td>' + data.inventoryCode + '</td>' +
										'<td>' + data.surplusNum.toFixed(2) + '</td>' +
										'<td >'  +"<input type='number' class='checkValue' value='"+data.surplusNum.toFixed(2) +"' style='border-style:none;width:100%;height: 100%'/> "+ '</td>' +
										'<td >'  +"<input type='button'  value='"+"删除" +"' style='border-style:none;width:100%;height: 100%' onclick='deleterow(this)'/> "+ '</td>' +
										'</tr>';
							}
						}
					}
					boxBarTb.append(str);
				}else{
					$("#alertText").text(result.msg);
					$("#alertDialog").modal();
					return false;
				}
			});



        }
    }

    //根据特殊报工源材料添加信息
    function specialAddbarsBtn() {
        var selections = $("#specialBarListTable").bootstrapTable("getSelections");
        if(selections){
            var boxBarTb=$("#specialBoxBarTbItem");
            var str = '';
			var codes=[];
			for(var i=0;i<arrcode.length;i++){
				codes.push(arrcode[i]);
			}
			for(var i=0;i<selections.length;i++){
				var data=selections[i];
				if(codes.length>0){
					if(codes.indexOf(data.barCode)==-1){
						codes.push(data.barCode);
					}
				}else{
					codes.push(data.barCode);
				}



			}
			for(var i=0;i<codes.length;i++){
				if(arrcode.indexOf(codes[i])!=-1){
					continue;
				}
				arrcode.push(codes[i]);
				for(var y=0;y<selections.length;y++){
					var data=selections[y];
					if(data.barCode==codes[i]){
						arrid.push(data.id);
						str += '<tr>' +
								/*  '<td>' + (serialNub++) +'</td>' +   */
								'<td>' + data.barCode + '</td>' +
								'<td>' + data.inventoryCode + '</td>' +
								'<td>' + data.surplusNum.toFixed(2) + '</td>' +
								'<td >'  +"<input type='number' class='specialCheckValue' value='"+data.surplusNum.toFixed(2) +"' style='border-style:none;width:100%;height: 100%'/> "+ '</td>' +
								'<td >'  +"<input type='button'  value='"+"删除" +"' style='border-style:none;width:100%;height: 100%' onclick='specialDeleterow(this)'/> "+ '</td>' +
								'</tr>';
					}
				}
			}
			boxBarTb.append(str);

        }
    }
    //删除源材料信息
    function deleterow(obj) {
        var index = $(obj).parents("tr").index(); //这个可获取当前tr的下标
        arrcode.splice(index,1);
        arrid.splice(index,1);
        $(obj).parents("tr").remove();
    }
  	//特殊报工删除源材料信息
    function specialDeleterow(obj) {
        var index = $(obj).parents("tr").index(); //这个可获取当前tr的下标
        arrcode.splice(index,1);
        arrid.splice(index,1);
        $(obj).parents("tr").remove();
    }

    function getData(index,data,tree) {
        if(data==undefined){return}
        if(index<1){
            if(tree.length == 0){   //起始设置
                var d={
                    text: data.name, //节点显示的文本值  string
                    code:data.code,
                    Id:data.id,
                    selectedIcon: "glyphicon glyphicon-ok", //节点被选中时显示的图标       string
                    color: "#000000", //节点的前景色      string
                    backColor: "#FFFFFF", //节点的背景色      string
                    href: "", //节点上的超链接
                    selectable: true, //标记节点是否可以选择。false表示节点应该作为扩展标题，不会触发选择事件。  string
                    state: { //描述节点的初始状态    Object
                        checked: true, //是否选中节点
                        /*disabled: true,*/ //是否禁用节点
                        expanded: false, //是否展开节点
                        selected: false //是否选中节点
                    },
                    nodes: []
                }
                tree.push(d);
                if(data.children){
                    $.each(data.children,function(ind, Typ) {
                        getData(ind,Typ,d);
                    })
                }
            }else{ //下一级对象
                var c = {
                    text: data.name,
                    code:data.code,
                    Id:data.id,
                    state: { //描述节点的初始状态    Object
                        checked: true, //是否选中节点
                        expanded: true, //是否展开节点
                    },
                    nodes: []
                }
                tree.nodes.push(c);
                if(data.children){
                    $.each(data.children,function(ind, Typ) {
                        getData(ind,Typ,c);
                    })
                }
            }
        }else{
            if(tree instanceof Array){
                var c = {
                    text: data.name,
                    code:data.code,
                    Id:data.id,
                    state: { //描述节点的初始状态    Object
                        checked: true, //是否选中节点
                        expanded: true, //是否展开节点
                    },
                    nodes: []
                }
                tree.push(c)
                if(data.children){
                    $.each(data.children,function(ind, Typ) {
                        getData(ind,Typ,c);
                    })
                }
            }else{
                var c = {
                    text: data.name,
                    code:data.code,
                    Id:data.id,
                    state: { //描述节点的初始状态    Object
                        checked: true, //是否选中节点
                        expanded: true, //是否展开节点
                    },
                    nodes: []
                }
                tree.nodes.push(c);
                if(data.children){
                    $.each(data.children,function(ind, Typ) {
                        getData(ind,Typ,c);
                    })
                }
            }
        }
    }

    //显示
    function showupdateProduction() {
        $("#updateProduction").modal('show');
    }
    //更新生产单元
    function update() {
        var selections = $("#showWorkSheetsListTable").bootstrapTable(
            "getSelections");
        var ws = selections[0];
        var data = $("#tree").treeview('getSelected');
        if(ws.productionUnitCode==data[0].code){
            $("#alertText").text("该工单已经在当前生产单元！");
            $("#alertDialog").modal();
            return false;
        }
        $.get("mcWorkSheet/chengeProductionUnitWithWorkSheet.do", {
            productionUnitId: data[0].Id,
            workSheetDetailId:ws.id
        }, function (result) {
            if (result.success) {
                updateProductionHide();
                refreshWorkSheetsTable(workSheet.deviceSiteCode);
            }else{
                $("#alertText").text(result.msg);
                $("#alertDialog").modal();
            }
        })
    }
    //隐藏
    function updateProductionHide() {
        $("#updateProduction").modal('hide');
    }



    //弹出(关联/替换)框
    function showmodel(e) {

        if(e=='add'){//equipmentcode，equipmentname，unitType，no，equipmenthelp，usageRate
            selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
            //console.log(selections[0].workPieceCode);
            if(!selections || selections.length<=0){
                $("#alertText").text("请选择要关联装备的工单!");
                $("#alertDialog").modal();
                return false;
            }

            $('#equipmentNo').val("");
			$('#equipmentcode').val("");
            $('#equipmentname').val("");
            $('#equipmentUnitType').val("");
            $('#equipmenthelp').val("");
            $('#useFrequency').val("");

			equipmentList=[];
			usageRateList=[];

            /*$('#glModal').modal('show');
            //弹出框显示后触发事件
            $('#glModal').on('shown.bs.modal', function(e) {
                $('#equipmentcode').focus();
            });*/
			queryInventoryMapping();
        }
    }

	$(function(){
		$("#equipmentNo").change(function() {
			var	codetext=$("#equipmentNo").val();
			if(codetext==null||codetext==""){
				codetext="+++_+_+_+__++";
			}
			equipmentSearch(codetext,"");
		})
		//装备代码文本框回车事件
		$("#equipmentNo").keydown(function(event) {
			if (event.keyCode == 13) {
				var	codetext=$("#equipmentNo").val();
				if(codetext==null||codetext==""){
					codetext="+++_+_+_+__++";
				}
				equipmentSearch(codetext,"");
			}
		});

		$("#equipmenthelp").change(function() {
			$("#UserCode").val("");
			helperselect("kadd");
		})

		$("#equipmenthelp").keydown(function(event) {
			if (event.keyCode == 13) {
				$("#UserCode").val("");
				helperselect("kadd");
			}
		});

	})


    //通过装备code查找装备名称
    function equipmentSearch(e,refer) {
        if(!e){
            e=$("#equipmentNo").val();
        }
        $.ajax({
            url : "mcequipment/queryEquipmentIsMappingByCode.do",
            data : {equipmentNo:e},
            cache:false,
            dataType : "json",
            success : function(data) {
                //var equipment= data.get(0);
                if(data.success){
					$("#equipmentcode").val(data.equipment.equipmentType.code);
                    $("#equipmentname").val(data.equipment.equipmentType.name);
                    $("#equipmentUnitType").val(data.equipment.equipmentType.unitType);
                    $("#equipmentcode").attr("equipID",data.equipment.id);

                    $("#"+data.equipment.equipmentType.code+"No").empty();
					$("#"+data.equipment.equipmentType.code+"No").text(data.equipment.code);
					$("#"+data.equipment.equipmentType.code+"Cumulation").text(data.equipment.cumulation.toFixed(2));
					$.get("inventoryEquipmentTypeMapping/queryMappingByInventoryCodeAndEquipmentTypeId.do",{
						inventoryCode:selections[0].workPieceCode,
						equipmentTypeId:data.equipment.equipmentType.id
					},function(result){
						if(result!=null) {
							$("#useFrequency").val(result.useFrequency);
							usageRateList.push(result.useFrequency);
						}
					})


					equipmentList.push(data.equipment.code);
                }else{
                    $("#equipmentcode").val("");
                    $("#equipmentname").val("");
                    $("#equipmentUnitType").val("");
                    $("#equipmentcode").attr("equipID","");
                    $("#equipmentNo").val("");
                    $("#alertText").text(data.msg);
                    $("#alertDialog").modal();
                }
            }
        });
    }


    //显示关联确认框
	function showMappingConfirmDlg(){
		$("#mappingConfirmDlg").modal('show');
	}


    //设置员工信息:回车或扫码
    function helperselect(e) {
        var userCode;
        //var userName;
		if(e=='kadd'){
            userCode = $("#equipmenthelp").val();
        }

        if (!userCode) {
            $("#alertText").text("请输入员工代码！");
            $("#alertDialog").modal();
        } else {
            $
                .ajax({
                    url : contextPath
                    + "employee/queryEmployeeByCode.do",
                    type : 'post',
                    data : {
                        "code" : userCode
                    },
                    cache:false,
                    success : function(data) {
                        // alert(JSON.stringify(data));
                        if(!data){
                            $("#alertText").text("该员工信息不存在，请重新输入！");
                            $("#alertDialog").modal();
                        }

						if(e=='kadd'){
                            $("#equipmenthelp").val(data.name);
                            $("#equipmenthelp").attr("code",data.code);
                            //$('#glModal').modal('show');
                        }

                    }
                })
        }
    }


    //关联提交
    function mappingcommit() {

    	if(equipmentList.length<=0){
			$("#alertText").text("请录入装备");
			$("#alertDialog").modal();
			return false;
		}

        var equipmentid = $("#equipmentcode").attr("equipID");
        var equipmenthelpname = $("#equipmenthelp").val();
        var equipmenthelpid = $('#equipmenthelp').attr("code");
		var deviceSiteCode1=$("#currentDeviceSiteCode").val();
        //var usageRate = $('#useFrequency').val()
        selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
        workSheet = selections[0];
        $.get("mcequipment/mcaddEquipmentsMappingRecordInWorkSheet.do", {
            //mappingDate:$('#mappingDate').val(),
            equipmentNos : equipmentList.toString(),
            deviceSiteCode : deviceSiteCode1,
            helperId : equipmenthelpid,
            usageRates : usageRateList.toString(),
            workSheetNo :workSheet.no,
        }, function(data) {
            if (data.success) {
				$('#glModal').modal('hide');
                $("#alertText").text("成功关联");
                $("#alertDialog").modal();

                //$('#departmentAddDialog').iDialog('close');
                //$('#departmentDg').iDatagrid('reload');
            } else {
                $("#alertText").text(data.msg);
                $("#alertDialog").modal();
            }
        });

        /* var helperName=$("#equipmenthelp").find("option:selected").text();
        var	helperId=$('#equipmenthelp').val();
        alert(helperName+"==="+helperId);  */
    }


    function queryInventoryMapping(){
        //var selections1 = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
        if(selections){
            var boxBarTb=$("#equipmentTbItem");
            var str = '';
			var deviceSiteCode1=$("#currentDeviceSiteCode").val();

            $.get("inventoryEquipmentTypeMapping/queryListByInventoryCodeAndDeviceSiteCode.do",{InventoryCode:selections[0].workPieceCode,deviceSiteCode:deviceSiteCode1,workSheetNo:selections[0].no},function(result){
                if(result.success){
					boxBarTb.empty();
					for(var y=0;y<result.data.length;y++){
						if(result.data[y].equipment.code!=null){
                            str += '<tr>' +
								'<td>' + (y+1) +'</td>' +
                                '<td>' + result.data[y].equipment.equipmentType.code + '</td>' +
                                '<td>' + result.data[y].equipment.equipmentType.name + '</td>' +
                                '<td>' + result.data[y].equipment.equipmentType.unitType + '</td>' +
                                '<td>' + result.data[y].equipment.code + '</td>' +
                                '<td>' + result.data[y].equipment.equipmentType.measurementObjective.toFixed(2) + '</td>' +
                                '<td>' + result.data[y].equipment.cumulation.toFixed(2) + '</td>' +
                                '</tr>';
						}else{
							str += '<tr>' +
									'<td>' + (y+1) +'</td>' +
									'<td>' + result.data[y].equipment.equipmentType.code + '</td>' +
									'<td>' + result.data[y].equipment.equipmentType.name + '</td>' +
									'<td>' + result.data[y].equipment.equipmentType.unitType + '</td>' +
									'<td id=\''+result.data[y].equipment.equipmentType.code+'No\'></td>' +
									'<td>' + result.data[y].equipment.equipmentType.measurementObjective.toFixed(2) + '</td>' +
									'<td id=\''+result.data[y].equipment.equipmentType.code+'Cumulation\'></td>' +
									'</tr>';
						}
					}

                    boxBarTb.append(str);
					$('#glModal').modal('show');
					//弹出框显示后触发事件
					$('#glModal').on('shown.bs.modal', function(e) {
						$('#equipmentNo').focus();
					});
                }else{
                    $("#alertText").text(result.msg);
                    $("#alertDialog").modal();
                    return false;
                }
            });

        }
	}
	//模态框点击返回事件(hide)
	function modelHide() {
		$('#glModal').modal('hide');
	}

	//班次改变事件
	function classesChange(){//queryDateByClassCode
	     $.get("classes/queryDateByClassCode.do",{code:$("#classesName").val()},function(result){
	        if(result){
	            $("#forJobBookingDate").val(result)
	        }
	     })
	     var selections = $("#showWorkSheetsListTable").bootstrapTable("getData");
         if(allowMultiWorkSheetRunning){
             selections = $("#showWorkSheetsListTable").bootstrapTable( "getSelections");
         }
         workSheet = selections[0];
         //当班报工
	     $.get("mcjobBookingForm/queryNumBerByWorkSheetNoAndClassCode.do",{workSheetNo:workSheet.no,classesCode:$("#classesName").val()},function(result){
             if(result){
                 $("#classesNum").val(result)
             }else{
                  $("#classesNum").val(0)
             }
         });
	}
	//班次改变事件
	function specialClassesChange(){//queryDateByClassCode
	     $.get("classes/queryDateByClassCode.do",{code:$("#specialClassesName").val()},function(result){
	        if(result){
	            $("#specialforJobBookingDate").val(result)
	        }
	     })
	     //当班报工
         $.get("mcjobBookingForm/queryNumBerByWorkSheetNoAndClassCode.do",{workSheetNo:workSheet.no,classesCode:$("#classesName").val()},function(result){
              if(result){
                  $("#specialclassesNum").val(result)
              }else{
                   $("#specialclassesNum").val(0)
              }
          });
	}
</script>

<style type="text/css">

</style>


<!-- 存放当前选中tab的设备站点代码 -->
<input type="hidden" id="currentDeviceSiteCode" />
<div class="tyPanelSize">
	<div class="tytitle" style="height: 7%; text-align: center">
		<div style="overflow: hidden; display: inline-block">
			<!-- <img style="float: left; margin-top: 7px;"
				src="mc/assets/css/images/tb.png"> -->
			<span class="fa fa-user-circle fa-2x" aria-hidden="true"
				  style="float: left; margin-top: 10px; margin-right: 10px"></span> <span
				style="float: left; margin-top: 17px;">工单管理</span>
		</div>
	</div>
	<div class="mc_workmanage_center" style="height: 68%;">
		<div style="text-align: center">

			<ul id="myTab" class="nav nav-tabs" role="tablist"
				style="overflow: hidden; display: inline-block;">
			</ul>

			<!-- Tab panes -->
			<div id="myTabpanes" class="tab-content"
				 style="border-top: 1px solid darkgray; margin-top: -6px;">

				<div role="tabpanel" class="tab-pane active"
					 id="showWorkSheetsListPanel"
					 style="width: 96%; margin-left: 2%;">
					<!-- <div style='width: 100%; float: right; margin-top: 0px;'> -->
					<div style="overflow: hidden;text-align: center;height:40px;">
						<!-- <span onclick='toolsearch()' class='fa fa-search' aria-hidden='true'
							style='float: left; margin-top: 7px; font-size: 30px;'></span>
						<input id='search' type='text'
							style='float: left; margin-left: 1%; margin: 10px;' /> -->
					</div>
					<!-- 工单列表 -->
					<table id="showWorkSheetsListTable"
						   style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
					</table>
					<!-- </div> -->
				</div>
			</div>
		</div>
	</div>
	<div class="mc_workmanage_bottom" style="height: 25%; overflow: hidden;margin-left: 2%; margin-right: 2%">
		<div style="margin-top: 25px;">

			<div data-toggle="modal" onclick="report()" style="float: right;margin-right: 0;"
				 data-target="#gdModal"
				 class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-download" aria-hidden="true"
					  style=" margin: 0 auto"></span>
				<h4 style="text-align: center">报工</h4>
			</div>
			<div data-toggle="modal" onclick="specialReport()" style="float: right;margin-right: 20px;background-color: red"
				 data-target="#gdModal"
				 class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-download" aria-hidden="true"
					  style=" margin: 0 auto"></span>
				<h4 style="text-align: center">特殊报工</h4>
			</div>

			<div onclick="completed()" style="float: left;"
				 class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-check-square-o" aria-hidden="true"
					  style=" margin: 0 auto"></span>
				<h4 style="text-align: center">完工</h4>
			</div>

			<div onclick="stop()" style="float: left;"
				 class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-exclamation-circle" aria-hidden="true"
					  style=" margin: 0 auto"></span>
				<h4 style="text-align: center">停工</h4>
			</div>
			<div onclick="start()" style="float: left;"
				 class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-play-circle-o" aria-hidden="true"
					  style=" margin: 0 auto"></span>
				<h4 style="text-align: center">开工</h4>
			</div>

			<div onclick="change()" style="float: left;"
				 class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-exchange" aria-hidden="true"
					  style=" margin: 0 auto"></span>
				<h4 style="text-align: center">换线</h4>
			</div>

			<div onclick="showmodel('add')" style="float: left;margin-right: 0;"
				 class="container-fluid functionButton">
				<h6></h6>
				<span class="fa fa-chain fa-4x" aria-hidden="true"
					  style=" margin: 0 auto"></span>
				<h4 style="text-align: center">关联</h4>
			</div>
		</div>
	</div>

</div>
<%@ include file="end.jsp"%>


<!--报工-->
<div class="modal fade" id="completeDialog" tabindex="-1" role="dialog"
	 aria-labelledby="myModalLabel" aria-hidden="true"
	 data-backdrop="static">
	<div class="modal-dialog" style="width: 780px;">
		<div class="modal-content">
			<div class="modal-body" style="background-color: #D3F3F3">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">生产报工</h4>
				<div
						style="float: left; width: 300px; border: 0px solid darkgray; margin-top: 20px; padding: 5px; padding-left: 20px;float: left;margin-left: 30px">
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">工件代码 </span><input
							id="workPieceCode" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly /><input
							id="workPieceid" type="hidden"/>
							<input	id="threshold" type="hidden"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">规格型号 </span><input
							id="unitType" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">批号 </span><input
							id="batchNumber" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">设备代码 </span><input
							id="deviceCode" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">工序 </span><input
							id="processName" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">工单报工 </span><input
							id="workSheetNum" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  onchange="computeSumCinvDefine14()" readonly/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">数量 </span><input
							id="num" type="text" onkeyup="value=value.replace(/[^\d]/g,'')"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  onchange="computeSumCinvDefine14()"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">总料耗 </span><input
							id="sumCInvDefine14" type="text" onkeyup="value=value.replace(/[^\d]/g,'')"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  readonly/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">材料条码 </span><input
							id="barCode" type="text"  autofocus="autofocus"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  />
						<span class="fa fa-bars fa-2x"
							  style="float: right;padding-top: 2px;border:2px; border-color: #000000" onclick="showAddbarDialog()"></span>
					</div>
				</div>
				<div style="float: left;width: 80px;margin-top: 17%;">
				</div>
				<div
						style="float: right; width: 300px; border: 0px solid darkgray; margin-top: 20px;  padding: 5px; padding-left: 10px;float: left;margin-right: 30px">
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">工件名称 </span><input
							id="workPieceName" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">图号 </span><input
							id="graphNumber" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly="readonly" />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">材料编号 </span><input
							id="stoveNumber" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">人员 </span><input
							id="username" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">当班报工 </span><input
							id="classesNum" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">班次 </span><%--<input
							id="classesName" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							value="1" class="form-control"/>--%>
						<div style="margin-left: -4px;display: inline-block;">
							<select id="classesName"  equipID=""
									class="selectpicker" data-live-search="true" onchange="classesChange()">
							</select>
						</div>
						<input type="hidden" id="classesCode" />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">归属日期 </span><input
							id="forJobBookingDate" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							value="1" class="form-control" />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">箱号 </span><input
							id="boxnum" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							value="1" class="form-control" readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">料耗 </span><input
							id="cInvDefine14" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							value="1" class="form-control" readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<input type="button" class="ant-btn" value="添加材料" style="margin-right: 3%"  onclick="getBoxbar()"><!-- data-dismiss="modal" -->
						<input type="button" class="ant-btn" value="确定" onclick="reporWork()" id="reporWork" style="background-color: #3F3FFA;color: white;">
					</div>
				</div>
			</div>
			<div style="width: 87%;padding-left: 7%;">
				<table id="boxBarTb" class="table1"
					   style="width: 100%; margin-left: auto; margin-right: auto;">
					<thead>
					<tr>
						<!-- <th>序号</th> -->
						<th>材料条码</th>
						<th>物料代码</th>
						<th>余量</th>
						<th>消耗数量</th>
						<th>操作</th>
					</tr>
					</thead>
					<tbody id="boxBarTbItem"></tbody>
				</table>
			</div>
			<div class="modal-footer" style="margin-top: 15px;border: 0px solid darkgray;">
				<div style="float:right;width: 100%;">
				</div>
			</div>
			<div style="width: 100%;height: 30px;background-color:#D3F3F3;">
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal -->
</div>
<!-- 特殊报工 -->
<div class="modal fade" id="specialCompleteDialog" tabindex="-1" role="dialog"
	 aria-labelledby="myModalLabel" aria-hidden="true"
	 data-backdrop="static">
	<div class="modal-dialog" style="width: 780px;">
		<div class="modal-content">
			<div class="modal-body" style="background-color: #D3F3F3">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">特殊报工</h4>
				<div
						style="float: left; width: 300px; border: 0px solid darkgray; margin-top: 20px; padding: 5px; padding-left: 20px;float: left;margin-left: 30px">
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">工件代码 </span><input
							id="specialWorkPieceCode" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly /><input
							id="specialWorkPieceid" type="hidden"/>
							<input	id="specialThreshold" type="hidden"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">规格型号 </span><input
							id="specialUnitType" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">批号 </span><input
							id="specialBatchNumber" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">设备代码 </span><input
							id="specialDeviceCode" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">工序 </span><input
							id="specialProcessName" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly />
					</div>
					<div style="margin-top: 20px;">
                        <span style="width: 60px; display: inline-block;">工单报工 </span><input
                            id="specialworkSheetNum" type="text"
                            style="width: 180px; display: inline-block; text-align: left;"
                            class="form-control"  onchange="computeSumCinvDefine14()" readonly/>
                    </div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">数量 </span><input
							id="specialNum" type="text" onkeyup="value=value.replace(/[^\d]/g,'')"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  onchange="specialComputeSumCinvDefine14()"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">总料耗 </span><input
							id="specialSumCInvDefine14" type="text" onkeyup="value=value.replace(/[^\d]/g,'')"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  readonly/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">材料条码 </span><input
							id="specialBarCode" type="text"  autofocus="autofocus"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  />
						<span class="fa fa-bars fa-2x"
							  style="float: right;padding-top: 2px;border:2px; border-color: #000000" onclick="showSpecialAddbarDialog()"></span>
					</div>
				</div>
				<div style="float: left;width: 80px;margin-top: 17%;">
				</div>
				<div
						style="float: right; width: 300px; border: 0px solid darkgray; margin-top: 20px;  padding: 5px; padding-left: 10px;float: left;margin-right: 30px">
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">工件名称 </span><input
							id="specialWorkPieceName" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">图号 </span><input
							id="specialGraphNumber" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly="readonly" />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">材料编号 </span><input
							id="specialStoveNumber" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">人员 </span><input
							id="specialUsername" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
                        <span style="width: 60px; display: inline-block;">当班报工 </span><input
                            id="specialclassesNum" type="text"
                            style="width: 180px; display: inline-block; text-align: left;"
                            class="form-control" readonly="readonly"/>
                    </div>
					<!-- <div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">班次 </span><input
							id="specialClassesName" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							value="1" class="form-control" readonly="readonly"/>
						<input type="hidden" id="specialClassesCode" />
					</div> -->
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">班次 </span>
						<div style="margin-left: -4px;display: inline-block;">
							<select id="specialClassesName"  equipID=""
									class="selectpicker" data-live-search="true" onchange="specialClassesChange()">
							</select>
						</div>
						<input type="hidden" id="specialClassesCode" />
					</div>
					<div style="margin-top: 20px;">
                        <span style="width: 60px; display: inline-block;">归属日期 </span><input
                            id="specialforJobBookingDate" type="text"
                            style="width: 180px; display: inline-block; text-align: left;"
                            value="1" class="form-control" />
                    </div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">箱号 </span><input
							id="specialBoxnum" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							value="1" class="form-control" readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">料耗 </span><input
							id="specialCInvDefine14" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							value="1" class="form-control" readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<input type="button" class="ant-btn" value="添加材料" style="margin-right: 3%"  onclick="specialGetBoxbar()"><!-- data-dismiss="modal" -->
						<input type="button" class="ant-btn" value="确定" onclick="specialReporWork()" id="specialReporWork" style="background-color: #3F3FFA;color: white;">
					</div>
				</div>
			</div>
			<div style="width: 87%;padding-left: 7%;">
				<table id="specialBoxBarTb" class="table1"
					   style="width: 100%; margin-left: auto; margin-right: auto;">
					<thead>
					<tr>
						<!-- <th>序号</th> -->
						<th>材料条码</th>
						<th>物料代码</th>
						<th>余量</th>
						<th>消耗数量</th>
						<th>操作</th>
					</tr>
					</thead>
					<tbody id="specialBoxBarTbItem"></tbody>
				</table>
			</div>
			<div class="modal-footer" style="margin-top: 15px;border: 0px solid darkgray;">
				<div style="float:right;width: 100%;">
				</div>
			</div>
			<div style="width: 100%;height: 30px;background-color:#D3F3F3;">
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal -->
</div>
<!--model-->
</div>
</div>



<!-- 报工确认 -->
<div class="modal fade" id="ConfirmDlg">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header" style="background-color: #D3F3F3">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">提示信息</h4>
			</div>
			<div class="modal-body">
				<p>您确认要报工吗？</p>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="reportWorkSheet()" class="btn btn-success"
				   data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
<!-- 开工确认框 -->
<div class="modal fade" id="StartConfirmDlg" style="margin-top: 400px">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header" style="background-color: #D3F3F3">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">工单开工</h4>
			</div>
			<div class="modal-body" style="height: 150px">
				<div style="margin: 30px">
					<span id="workNameStart">
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="startWork()" class="btn btn-success"
				   data-dismiss="modal">确定</a>
			</div>
			<div style="background-color: #D3F3F3;height: 30px">
				<p>注意：请确认当前生产单元没有工单在加工状态。否则将开工失败</p>
			</div>
		</div>
	</div>
</div>
<!-- 停工确认框 -->
<div class="modal fade" id="StopConfirmDlg" style="margin-top: 400px">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header" style="background-color: #D3F3F3">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">工单停工</h4>
			</div>
			<div class="modal-body" style="height: 150px">
				<div style="margin: 30px">
					<span id="workNameStop">
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="stopwork()" class="btn btn-success"
				   data-dismiss="modal">确定</a>
			</div>
			<div style="background-color: #D3F3F3;height: 30px">
				<p>注意：切换为停工状态后的工单，还可以在此界面上再次进行开工</p>
			</div>
		</div>
	</div>
</div>
<!-- 完工确认框 -->
<div class="modal fade" id="CompletedConfirmDlg" style="margin-top: 400px">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header" style="background-color: #D3F3F3">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">工单完工</h4>
			</div>
			<div class="modal-body" style="height: 150px">
				<div style="margin: 30px">
					<span id="workNameCompleted">
				</div>
			</div>
			<div class="modal-footer">
				<input type="hidden" id="type" />
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="completedwork()" class="btn btn-success"
				   data-dismiss="modal">确定</a>
			</div>
			<div style="background-color: #D3F3F3;height: 30px">
				<p >注意：切换为完工状态的工单，将不再在此界面上显示</p>
			</div>
		</div>
	</div>
</div>
<!-- 材料信息弹出框 -->
<div class="modal fade" tabindex="-1" role="dialog"
	 id="addbarDlg">
	<div class="modal-dialog" role="document" style="width: 600px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">原材料</h4>
			</div>
			<div class="modal-body">
				<table id="barListTable"></table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<!-- <button type="button" class="btn btn-default" data-dismiss="modal" id="addbarsBtn" onclick="addbarsBtn()">确定</button> -->
			</div>
		</div>
	</div>
</div>

<!-- 特殊报工材料信息弹出框 -->
<div class="modal fade" tabindex="-1" role="dialog"
	 id="specialAddbarDlg">
	<div class="modal-dialog" role="document" style="width: 600px;">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title">原材料</h4>
			</div>
			<div class="modal-body">
				<table id="specialBarListTable"></table>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<button type="button" class="btn btn-default" data-dismiss="modal" id="specialAddbarsBtn" onclick="specialAddbarsBtn()">确定</button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="updateProduction" role="dialog"
	 aria-labelledby="myModalLabel" aria-hidden="true"
	 data-backdrop="static">
	<div class="modal-dialog" style="width: 850px;color: white;">
		<div class="modal-content" style="background-color:#FFFFFF;width:100%;height:100%;">
			<div class="modal-header" style="background-color: #D3F3F3">
				<div
						style="height:30px; width: 600px; overflow: hidden;">
					<span style="font-size: 20px;color: black;">生产单元</span>
				</div>
			</div>
			<div class="modal-body">
				<div id="tree" style="width: 100%; height: 600px; background-color: #FFFFFF; padding: 20px;overflow-y:auto;"></div>
			</div>
			<div class="modal-footer" style="margin-top: 20px;background-color: #D3F3F3">
				<div style="float: right; margin-right: 50px;width: 400px;">
						 <span class='btn btn-default' style="margin-right: 20px;"
							   onclick="update()">确认</span>
					<span  class='btn btn-default'
						   onclick="updateProductionHide()">取消</span>
				</div>
			</div>
		</div>
	</div>
</div>



<!--关联-->
<%--<div class="modal fade" id="glModal" tabindex="-1" role="dialog"
	 aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
	<div class="modal-dialog" style="width: 780px;margin-top: 150px">
		<div class="modal-content">
			<div class="modal-body" style="height: 70px;background-color: #D3F3F3">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h2 class="modal-title"
					style="border-bottom: 0px solid darkgray; font-size: 25px; padding: 5px;">装备关联</h2>
			</div>
			<div style="float: left; width: 300px; border: 0px solid darkgray; margin-top: 20px; padding: 5px; padding-left: 20px;float: left;margin-left: 30px">

				<div style="margin-top: 20px;">
					<span style="width: 60px; display: inline-block;font-weight:bold">装备代码 </span><input
						id="equipmentcode" type="text" equipID=""
						style="width: 280px; display: inline-block;"
						class="form-control" />
				</div>
				<div style="margin-top: 20px;">
					<span style="width: 60px; display: inline-block;font-weight:bold;text-align:right;padding-right:3px;">名称 </span><input
						id="equipmentname" type="text"
						style="width: 280px; display: inline-block;"
						class="form-control" readOnly="true" />
				</div>
				<div style="margin-top: 20px;">
					<span style="width: 60px; display: inline-block;font-weight:bold">规格型号 </span><input
						id="unitType" type="text"
						style="width: 280px; display: inline-block; "
						class="form-control" value=""  readonly="readonly"/>
				</div>
				<div style="margin-top: 20px;">
					<span style="width: 60px; display: inline-block;font-weight:bold;text-align:right;padding-right:3px;">辅助人 </span>
					<input type="text" id="equipmenthelp" code=""
						   style="width: 280px; display: inline-block; "
						   class="form-control" />
					<input type="hidden" id="equipmenthelpid" code=""
						   class="form-control" />
				</div>
				<div style="margin-top: 20px;">
					<span style="width: 60px; display: inline-block;font-weight:bold">使用频次 </span><input
						id="usageRate" type="text"
						style="width: 280px; display: inline-block; "
						class="form-control" value="1" />
				</div>
				<div style="margin-top: 40px;">
					<input	onclick="modelHide()" value="取消" type="button" class="ant-btn" style="margin-left: 8%;float: left;font-weight:bold">
					<input data-toggle="modal"  type="button" onclick="equipmentSearch('','submit')"
						   style="margin-left: 8%;background-color: #85E0A3;font-weight:bold" value="确定" class="ant-btn">
				</div>
			</div>
			<div style="width: 100%;height: 30px;background-color:#D3F3F3;">
			</div>
		</div>
	</div>
	<!-- /.modal-content -->
</div>--%>


<div class="modal fade" id="glModal" tabindex="-1" role="dialog"
	 aria-labelledby="myModalLabel" aria-hidden="true"
	 data-backdrop="static">
	<div class="modal-dialog" style="width: 780px;">
		<div class="modal-content">
			<div class="modal-body" style="background-color: #D3F3F3">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">装备关联</h4>
				<div
						style="float: left; width: 300px; border: 0px solid darkgray; margin-top: 20px; padding: 5px; padding-left: 20px;float: left;margin-left: 30px">
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">装备序号 </span><input
							id="equipmentNo" type="text" equipID=""
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">装备名称 </span><input
							id="equipmentname" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">辅助人 </span><input
							id="equipmenthelp" type="text" code=""
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" />
						<input type="hidden" id="equipmenthelpid" code=""
							   class="form-control" />
					</div>

				</div>
				<div style="float: left;width: 80px;margin-top: 17%;">
				</div>
				<div
						style="float: right; width: 300px; border: 0px solid darkgray; margin-top: 20px;  padding: 5px; padding-left: 10px;float: left;margin-right: 30px">
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">装备代码 </span><input
							id="equipmentcode" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control"  readonly="readonly"/>
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">规格型号 </span><input
							id="equipmentUnitType" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly="readonly" />
					</div>
					<div style="margin-top: 20px;">
						<span style="width: 60px; display: inline-block;">使用频次 </span><input
							id="useFrequency" type="text"
							style="width: 180px; display: inline-block; text-align: left;"
							class="form-control" readonly="readonly"/>
					</div>
				</div>
			</div>
			<div style="width: 87%;padding-left: 7%;">
				<table id="equipmentTb" class="table1"
					   style="width: 100%; margin-left: auto; margin-right: auto;">
					<thead>
					<tr>
						<th>序号</th>
						<th>装备代码</th>
						<th>装备名称</th>
						<th>规格型号</th>
						<th>装备序号</th>
						<th>计量目标</th>
						<th>计量累计</th>
					</tr>
					</thead>
					<tbody id="equipmentTbItem"></tbody>
				</table>
			</div>
			<div style="margin-top: 40px;padding-left: 40%">
				<input	onclick="modelHide()" value="取消" type="button" class="ant-btn" style="margin-left: 8%;float: left;font-weight:bold">
				<input data-toggle="modal"  type="button" onclick="showMappingConfirmDlg()"
					   style="margin-left: 8%;background-color: #85E0A3;font-weight:bold" value="确定" class="ant-btn">
			</div>
			<div class="modal-footer" style="margin-top: 15px;border: 0px solid darkgray;">
				<div style="float:right;width: 100%;">
				</div>
			</div>
			<div style="width: 100%;height: 30px;background-color:#D3F3F3;">
			</div>
		</div>
		<!-- /.modal-content -->
	</div>
	<!-- /.modal -->
</div>



<!--关联确认框-->
<div class="modal fade" id="mappingConfirmDlg">
	<div class="modal-dialog">
		<div class="modal-content message_align">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">×</span>
				</button>
				<h4 class="modal-title">提示信息</h4>
			</div>
			<div class="modal-body">
				<p>您确认要关联吗？</p>
			</div>
			<div class="modal-footer">

				<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				<a onclick="mappingcommit()" class="btn btn-success"
				   data-dismiss="modal">确定</a>
			</div>
		</div>
	</div>
</div>
