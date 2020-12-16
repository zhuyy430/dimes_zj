<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	$(function(){
		$.get("scrapStatus/queryScrapStatusUp.do",function(data){
			scrapStatus(data);
		});
	});
	//报废状态
	function scrapStatus(data){
		var myChart = echarts.init(document.getElementById("main"));
		//报废数量
		var scrapCount = data.dataList;
		option = {
		    tooltip: {},
		    visualMap: {
		        max: 20,
		        inRange: {
		            color: ['#313695', '#4575b4', '#74add1', '#abd9e9', '#e0f3f8', '#ffffbf', '#fee090', '#fdae61', '#f46d43', '#d73027', '#a50026']
		        }
		    },
		    xAxis3D: {
		        type: 'category',
		        data: data.processes
		    },
		    yAxis3D: {
		        type: 'category',
		        data: data.days
		    },
		    zAxis3D: {
		        type: 'value'
		    },
		    grid3D: {
		        boxWidth: 200,
		        boxDepth: 80,
		        viewControl: {
		            // projection: 'orthographic'
		        },
		        light: {
		            main: {
		                intensity: 1.2,
		                shadow: true
		            },
		            ambient: {
		                intensity: 0.3
		            }
		        }
		    },
		    series: [{
		        type: 'bar3D',
		        data: scrapCount.map(function (item) {
		            return {
		                value: [item[0], item[1], item[2]],
		            }
		        }),
		        shading: 'lambert',

		        label: {
		            textStyle: {
		                fontSize: 16,
		                borderWidth: 1
		            }
		        },

		        emphasis: {
		            label: {
		                textStyle: {
		                    fontSize: 20,
		                    color: '#900'
		                }
		            },
		            itemStyle: {
		                color: '#900'
		            }
		        }
		    }]
		}
			    myChart.setOption(option);
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