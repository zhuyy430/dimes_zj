<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="reportForm/js/reportForm.js"></script>
<script type="text/javascript">
    $(function(){
        initYYYYMMDate();
		//显示生产单元按钮
        $("#productionUnitId").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showProductionUnitsDialog').dialog("open");
            }
        });
        showProductionUnitDialog();
    });
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
	<label>生产日期</label>
		<input name="date" id="qdate" type="text" style="width: 150px;float:left;" editable="false">
		<label>生产单元</label>
		<input id="productionUnitId"  data-toggle="topjui-textbox" style="width:200px;">
		<a href="javascript:void(0)"  class="layui-btn layui-btn-sm" onclick="showDailyOutputData()" style="text-decoration: none;margin-left: 10px;">确定</a>
		<a href="javascript:void(0)" class="layui-btn layui-btn-sm" onclick="exportExcel('dailyOutputReport','日产量')"  style="text-decoration: none;margin-left: 10px;">导出</a>
	</div>
	<div id="dailyOutputChart" style="width:100%;height:500px;">
	</div>
	<div style="width: 100%;">
		<table style="width: 95%;margin: auto auto;" id="dailyOutputReport"></table>
	</div>
</body>
</html>
<div id="showProductionUnitsDialog"></div>