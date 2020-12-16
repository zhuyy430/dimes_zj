<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
$(function(){
	$('#from').iDatebox('setValue', new Date());
	$('#to').iDatebox('setValue', new Date());
	//显示生产单元按钮
    $("#ProductionSearch").iTextbox({
        buttonIcon:'fa fa-search',
        onClickButton:function(){
            $('#showInventoryDialog').dialog("open");
        }
    });
	
    $('#showInventoryDialog').dialog({
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
                var row = $('#menus').iTreegrid('getCheckedNodes');
                var selectName=[];
                var selectIds=[];
                for(var i=0;i<row.length;i++){
                	if(row[i].children.length>0)
                		continue;
                    selectName.push(row[i].name);
                    selectIds.push(row[i].id);
                }
                $('#ProductionSearch').iTextbox("setValue",selectName.join(","));
                $('#productionUnitIds').val(selectIds.join(","));
                $('#showInventoryDialog').dialog("close");
            }
        },{
            text:'关闭',
            handler:function(){
                $('#showInventoryDialog').dialog("close");
            }
        }]
    });
});


	function showOutputSumData() {
		//生产日期
		var from = $("#from").iDatebox("getValue");
		var to = $("#to").iDatebox("getValue");
		//生产单元
		var productionUnitIds = $("#productionUnitIds").val();
		if(!productionUnitIds){
			$.iMessager.alert('提示','请选择生产单元！');
			return
		}
		var cycle = $("#cycle").iTextbox("getValue");
		
		$.get("reportForm/queryOutputSumData.do", {
			from:from,
			to:to,
			productionUnitIds:productionUnitIds,
			cycle:cycle
		}, function(result) {
			ngRecord(result);
			showDailyOutputReport(result.reportList);
		});
	}
	//不合格记录:产线级
	function ngRecord(data) {
		console.log("ngRecord");
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
			arr.push(ngCount);
		}
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
			yAxis : [  {
				type : 'value',
				name : '',
				position:'left',
				axisLabel : {
					formatter : '{value}'
				}
			},{
				type : 'value',
				name : '',
				position:'right',
				axisLabel : {
					formatter : '{value}'
				}
			} ],
			series : arr
		};

		myChart.setOption(option);
	}
	//不合格报表
	function showDailyOutputReport(reportList){
		console.log("showDailyOutputReport");
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
		<input type="text" name="from" data-toggle="topjui-datebox"	id="from" style="width:150px;">
	<label>至</label>
		<input type="text" name="to" data-toggle="topjui-datebox"	id="to" style="width:150px;">
		&nbsp;&nbsp;&nbsp;
		<label>生产单元</label>
		<input id="ProductionSearch"  data-toggle="topjui-textbox" style="width:200px;">
		<input type="hidden" id="productionUnitIds">
		&nbsp;&nbsp;&nbsp;
		<span> <label for="searchChange">统计周期: </label> <input
				id="cycle" name="cycle" type="text" style="width: 140px;"
				data-toggle="topjui-combobox" 
				data-options="
				valueField:'id',
				textField:'name',
				data:[
					  {id:'month',name:'月',selected:true},
					  {id:'day',name:'日'}
				]"
				>
			</span> 
		
		<a href="javascript:void(0)"  class="layui-btn layui-btn-sm" onclick="showOutputSumData()" style="text-decoration: none;margin-left: 10px;">确定</a>
		<a href="javascript:void(0)" class="layui-btn layui-btn-sm" onclick="exportExcel('dailyOutputReport','产量汇总')"  style="text-decoration: none;margin-left: 10px;">导出</a>
	</div>
	<div id="ngRecordGraphBody" style="width: 1800px; height: 800px;"></div>
	<div style="width: 100%;">
		<table style="width: 95%;margin: auto auto;" id="dailyOutputReport"></table>
	</div>
</body>
</html>
<div id="showInventoryDialog"></div>