<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	$(function(){
		
		$.get('lostTimeRecord/queryAllMonthLostTimeRecordFor1Year.do',function(result){
			 var myChart = echarts.init(document.getElementById('main'));
			 option = {
				        legend: {},
				        tooltip: {
				            trigger: 'axis',
				            showContent: false
				        },
				        dataset: {
				            source: result.data
				        },
				        xAxis: {type: 'category'},
				        yAxis: {gridIndex: 0},
				        grid: {top: '55%'},
				        series: [
				            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
				            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
				            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
				            {type: 'line', smooth: true, seriesLayoutBy: 'row'},
				            {
				                type: 'pie',
				                id: 'pie',
				                radius: '30%',
				                center: ['50%', '25%'],
				                label: {
				                    formatter: '{b}: {@2012} ({d}%)'
				                },
				                encode: {
				                    itemName: result.data[0][0],
				                    value: '2012',
				                    tooltip: '2012'
				                }
				            }
				        ]
				    };

				    myChart.on('updateAxisPointer', function (event) {
				        var xAxisInfo = event.axesInfo[0];
				        if (xAxisInfo) {
				            var dimension = xAxisInfo.value + 1;
				            myChart.setOption({
				                series: {
				                    id: 'pie',
				                    label: {
				                        formatter: '{b}: {@[' + dimension + ']} ({d}%)'
				                    },
				                    encode: {
				                        value: dimension,
				                        tooltip: dimension
				                    }
				                }
				            });
				        }
				    });
		        myChart.setOption(option);
		});
	});
</script>
</head>
<body>
	<div id="main" style="width:800px;height:800px;"></div>
</body>
</html>