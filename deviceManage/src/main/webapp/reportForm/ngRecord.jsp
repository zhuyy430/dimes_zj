<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<script type="text/javascript" src="reportForm/js/reportForm.js"></script>
<script>
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

	function showngRecordData() {
		//生产日期
		var qdate = $("#qdate").iDatebox("getValue");
		//生产单元
		var productionUnitId = $("#productionUnitId").iTextbox("getValue");
		$.get("ngRecord/queryNGRecord4ProductionUnit.do", {
			productDate:qdate,
			productionUnitId:productionUnitId
		}, function(result) {
			ngRecord(result);
			showDailyOutputReport(result.reportList);
		});
	}
	//不合格记录:产线级
	function ngRecord(data) {
		var arr = new Array();
		for (var i = 0; i < data.classNameList.length; i++) {
			var className = data.classNameList[i];
			//不合格数
			var ngCount = {
				name : className,
				type : 'bar',
				stack : data.classNameList[0],
				data : data.ngCountMap[className]
			};
			//ppm
			/* var ppm = {
				name : className,
				type : 'line',
				data : data.ppmMap[className]
			}; */
			arr.push(ngCount);
			//arr.push(ppm);
		}
		/* var goalNg = {
				name : '目标',
				type : 'line',
				yAxisIndex:1,
				data : data.ngGoalList
			};
		
		arr.push(goalNg); */
		var myChart = echarts
				.init(document.getElementById("ngRecordGraphBody"));
		option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : {
					type : 'cross',
					crossStyle : {
						color : '#999'
					}
				}
			},
			legend : {
				data : data.classNameList
			},
			xAxis : [ {
				type : 'category',
				data : data.thisMonth,
				axisPointer : {
					type : 'shadow'
				}
			} ],
			yAxis : [ {
				type : 'value',
				name : 'NG数',
			}],
			series : arr
		};
		myChart.setOption(option);
	}
	//不合格报表
	function showDailyOutputReport(reportList){
	    var reportTable = $("#dailyOutputReport");
	    reportTable.empty();
	    if(reportList!=null && reportList.length>0){
	        for(var i = 0;i<reportList.length;i++){
	            var tr = $("<tr style='height: 30px;'>");
	          var dataList = reportList[i];
	          if(dataList!=null && dataList.length>0){
	              for(var j = 0;j<dataList.length;j++){
	                  var data = dataList[j];
	                  var td ;
	                  if(j==0 || i==0){
	                      td = $("<td style='border:1px solid gray;text-align: center;font-weight: bold;width:50px;background-color: lightgrey;'>");
	                  }else{
	                      td = $("<td style='border:1px solid gray;text-align: center;width:50px;'>");
	                  }
	                  td.append(data);
	                  tr.append(td);
	              }
	          }
	          reportTable.append(tr);
	        }
	    }
	}
</script>
<style>
.main {
	height: 100%;
	width: 33%;
	float: left;
}
</style>
</head>
<body>
	<div id="dailyOutputCondition" style="height:200px;width: 100%;margin: 10px;padding-top: auto;padding-bottom: auto;">
	<label>生产日期</label>
		<input name="date" id="qdate" type="text" style="width: 150px;float:left;" editable="false" data-options="required:true,width:200,showSeconds:true,
					         fitColumns:true,
	                       pagination:true,
					         parentGrid:{
	                       	type:'treegird',
	                       	id:'departmentTg'
	                       }">
		<label>生产单元</label>
		<input id="productionUnitId"  data-toggle="topjui-textbox" style="width:200px;">
		<input type="hidden" id="productionUnitCode">
		<a href="javascript:void(0)"  class="layui-btn layui-btn-sm" onclick="showngRecordData()" style="text-decoration: none;margin-left: 10px;">确定</a>
		<a href="javascript:void(0)" class="layui-btn layui-btn-sm" onclick="exportExcel('dailyOutputReport','不合格记录')"  style="text-decoration: none;margin-left: 10px;">导出</a>
	</div>
	<div id="ngRecordGraphBody" style="width: 100%; height: 500px;"></div>
	<div style="width: 100%;">
		<table style="width: 95%;margin: auto auto;" id="dailyOutputReport"></table>
	</div>
</body>
</html>
<div id="showProductionUnitsDialog"></div>