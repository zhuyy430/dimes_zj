<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	$(function(){
		$.get("fractionDefective/queryAllDayFractionDefectiveByMonth.do",function(data){
			fractionDefective(data);
		});
		
	});
	//一次性不合格率
	function fractionDefective(data){
		var myCharts = echarts.init(document.getElementById("main"));
		var colors = ['#5793f3', '#d14a61', '#675bba'];
		option = {
		    color: colors,
		    tooltip: {
		        trigger: 'none',
		        axisPointer: {
		            type: 'cross'
		        }
		    },
		    legend: {
		        data:['上个月同期', '当月PPM']
		    },
		    grid: {
		        top: 70,
		        bottom: 50
		    },
		    xAxis: [
		        {
		            type: 'category',
		            axisTick: {
		                alignWithLabel: true
		            },
		            axisLine: {
		                onZero: false,
		                lineStyle: {
		                    color: colors[1]
		                }
		            },
		            axisPointer: {
		                label: {
		                    formatter: function (params) {
		                        return 'PPM' + params.value
		                            + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
		                    }
		                }
		            },
		            data: data.thisMonth
		        },
		        {
		            type: 'category',
		            axisTick: {
		                alignWithLabel: true
		            },
		            axisLine: {
		                onZero: false,
		                lineStyle: {
		                    color: colors[0]
		                }
		            },
		            axisPointer: {
		                label: {
		                    formatter: function (params) {
		                        return 'PPM ' + params.value
		                            + (params.seriesData.length ? '：' + params.seriesData[0].data : '');
		                    }
		                }
		            },
		            data: data.preMonth
		        }
		    ],
		    yAxis: [
		        {
		            type: 'value'
		        }
		    ],
		    series: [
		        {
		            name:'上个月同期',
		            type:'line',
		            xAxisIndex: 1,
		            smooth: true,
		            data: data.prePPMList
		        },
		        {
		            name:'当月PPM',
		            type:'line',
		            smooth: true,
		            data: data.thisPPMList
		        }
		    ]
		};
		
		myCharts.setOption(option);
	}
</script>
<style>
.main{
	height:100%;
	width: 33%;
	float:left;
}
</style>
</head>
<body>
	<div id="main" style="width:1700px;height:800px;">
	</div>
</body>
</html>