<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	function requestNGRecord(productionUnitId) {
		$.get("ngRecord/queryNGRecord4ProductionUnit.do", {
			productionUnitId : productionUnitId
		}, function(result) {
			ngRecord(result);
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
			var ppm = {
				name : className,
				type : 'line',
				data : data.ppmMap[className]
			};
			arr.push(ngCount);
			arr.push(ppm);
		}
		var goalNg = {
				name : '目标',
				type : 'line',
				yAxisIndex:1,
				data : data.ngGoalList
			};
		
		arr.push(goalNg);
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
				name : 'PPM',
				position:'right',
				axisLabel : {
					formatter : '{value}'
				}
			}, {
				type : 'value',
				name : 'NG数',
				position:'left',
				axisLabel : {
					formatter : '{value}'
				}
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
        	requestNGRecord(rec.id);
        }">
	</div>
	<div id="ngRecordGraphBody" style="width: 1800px; height: 800px;"></div>
</body>
</html>