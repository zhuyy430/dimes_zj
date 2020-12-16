<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	$(function(){
		
		$.get('lostTimeRecord/queryLostTimeRecordOfHaltUp.do',function(data){

			var app = echarts.init(document.getElementById('main'));
			option = {
			    tooltip: {
			        trigger: 'axis',
			        axisPointer: {
			            type: 'cross',
			            crossStyle: {
			                color: '#999'
			            }
			        }
			    },
			    toolbox: {
			        feature: {
			            dataView: {show: true, readOnly: false},
			            magicType: {show: true, type: ['line', 'bar']},
			            restore: {show: true},
			            saveAsImage: {show: true}
			        }
			    },
			    legend: {
			        data:['故障时间','占比']
			    },
			    xAxis: [
			        {
			            type: 'category',
			            data: ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月'],
			            axisPointer: {
			                type: 'shadow'
			            }
			        }
			    ],
			    yAxis: [
			        {
			            type: 'value',
			            name: '故障小时数',
			            min: 0,
			            axisLabel: {
			                formatter: '{value} h'
			            }
			        },
			        {
			            type: 'value',
			            name: '占比',
			            min: 0,
			            interval: 5,
			            axisLabel: {
			                formatter: '{value} %'
			            }
			        }
			    ],
			    series: [
			        {
			            name:'故障时间',
			            type:'bar',
			            data:data.hours
			        },
			        {
			            name:'占比',
			            type:'line',
			            yAxisIndex: 1,
			            data:data.ratios
			        }
			    ]
			};

			app.setOption(option);
		});
	});
</script>
</head>
<body>
	<div id="main" style="width:800px;height:800px;"></div>
</body>
</html>