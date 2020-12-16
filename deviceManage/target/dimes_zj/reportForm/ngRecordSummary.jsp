<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="reportForm/js/reportForm.js"></script>
<script type="text/javascript">
    $(function(){
        var date = new Date();
        generateYYYYMM("from",new Date(date.getFullYear(),1,0));
        var nextMonth = new Date(date.getFullYear(),date.getMonth()+1,0);
        generateYYYYMM("to",nextMonth);
        //显示生产单元按钮
        $("#productionUnitId").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showMultiSelectProductionUnitsDialog').dialog("open");
            }
        });
        showMultiSelectProductionUnitDialog();
    });
	//显示多选生产单元对话框
    function showMultiSelectProductionUnitDialog(){
        //显示生产单元窗口
        $('#showMultiSelectProductionUnitsDialog').dialog({
            title: '生产单元',
            width: 800,
            height: 600,
            closed: true,
            cache: false,
            href: 'reportForm/productionSelect.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function(){
                    var productionUnits = $('#menus').iTreegrid('getCheckedNodes');
                    if(!productionUnits){
                        return false;
					}
					var productionUnitNames="";
                    var productionUnitIds="";
					for(var i = 0;i<productionUnits.length;i++){
					    var productionUnit = productionUnits[i];
					    productionUnitNames += productionUnit.name;
					    productionUnitIds += productionUnit.id;
					    if(i<productionUnits.length-1){
					        productionUnitNames += ",";
					        productionUnitIds += ",";
						}
					}
                    $("#productionUnitId").iTextbox('setValue',productionUnitIds);
                    $("#productionUnitId").iTextbox('setText',productionUnitNames);
                    $('#showMultiSelectProductionUnitsDialog').dialog("close");
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showMultiSelectProductionUnitsDialog').dialog("close");
                }
            }]
        });
	}
</script>
<style>
	.main{
		height:100%;
		width: 33%;
		float:left;
	}
	#dailyOutputCondition label{
		margin-left: 20px;
		font-size: 17px;
	}
</style>
</head>
<body>
<div id="dailyOutputCondition" style="height:50px;width: 100%;margin: 10px;padding-top: auto;padding-bottom: auto;">
	<label>生产年月</label>
	<input name="from" id="from" type="text" style="width: 150px;" editable="false">
	<label>至</label>
	<input name="to" id="to" type="text" style="width: 150px;" editable="false">
	<label>生产单元</label>
	<input id="productionUnitId"  data-toggle="topjui-textbox" style="width:200px;">
	<%--<label>统计周期</label>
	<input id="cycle"  data-toggle="topjui-combobox" style="width:200px;" data-options="valueField:'value',textField:'text',
	data:[{text:'月',value:'month',select:true},{text:'日',value:'day'}]">--%>
	<a href="javascript:void(0)"  class="layui-btn layui-btn-sm" onclick="showNgRecordSummaryData()" style="text-decoration: none;margin-left: 10px;">确定</a>
	<a href="javascript:void(0)" class="layui-btn layui-btn-sm" onclick="exportExcel('ngRecordSummaryReport','不合格品汇总统计')"  style="text-decoration: none;margin-left: 10px;">导出</a>
</div>
<div id="ngRecordSummaryChart" style="width:60%;height:500px;">
</div>
<div style="width: 100%;">
	<table style="width: 95%;margin: auto auto;" id="ngRecordSummaryReport"></table>
</div>
</body>
</html>
<div id="showMultiSelectProductionUnitsDialog"></div>