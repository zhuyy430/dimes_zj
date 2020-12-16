<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	//点击产线，查询该产线的产量
	function requestOutput(productionUnitId) {
		$.get("output/queryOutput4ProductionUnit.do", {
			productionUnitId : productionUnitId
		}, function(result) {
			output(result);
		});
	}
	//产线级
	function output(data) {
		var arr = new Array();
		for(var i = 0;i<data.classNameList.length;i++){
			var className = data.classNameList[i];
			var ser = {
					name : className,
					type : 'bar',
					stack:data.classNameList[0],
					data : data.outputMap[className]
				};
			arr.push(ser);
		}
		
		var goalOutput = {
		        data: data.goalOutput,
		        type: 'line'
		    };
		
		arr.push(goalOutput);
		
		var myChart = echarts.init(document.getElementById("outputGraphBody"));
		option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend : {
				data : data.classNameList
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				containLabel : true
			},
			xAxis : [ {
				type : 'category',
				data : data.days
			} ],
			yAxis : [ {
				type : 'value'
			} ],
			series : arr
		};
		myChart.setOption(option);
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
	<div style="width: 50%; margin: auto auto;">
		<input id="cc1" data-toggle="topjui-combobox"
			data-options="
        valueField: 'id',
        textField: 'name',
        url: 'productionUnit/queryAllProductionUnits.do',
        onLoadSuccess: function () { //加载完成后,val[0]写死设置选中第一项
                var val = $(this).combobox('getData');
                for (var item in val[0]) {
                    if (item == 'id') {
                        $(this).combobox('select', val[0][item]);
                    }
                }
            },
        onSelect: function(rec){
        	requestOutput(rec.id);
        }">
	</div>
	<div id="outputGraphBody" style="width: 1800px; height: 800px;"></div>
</body>
</html>