<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	$(function(){
		$.get("scraprate/queryScraprate.do",function(data){
			scraprate(data);
		});
	});
	
	function scraprate(data){
		var arr = new Array();
		for(var i = 0;i<data.ngTypeNames.length;i++){
			var position = 'middle';
			if(i==0){
				position = 'start';
			}
			if(i==data.ngTypeNames.length-1){
				position = "end";
			}
			var item = {
		            name:data.ngTypeNames[i],
		            type:'line',
		            step: position,
		            data:data.ppmMap[data.ngTypeNames[i]]
		        }
			
			arr.push(item);
		}
		var myChart = echarts.init(document.getElementById("main"));
		option = {
			    title: {
			        text: '报废率'
			    },
			    tooltip: {
			        trigger: 'axis'
			    },
			    legend: {
			        data:data.ngTypeNames
			    },
			    grid: {
			        left: '3%',
			        right: '4%',
			        bottom: '3%',
			        containLabel: true
			    },
			    toolbox: {
			        feature: {
			            saveAsImage: {}
			        }
			    },
			    xAxis: {
			        type: 'category',
			        data: data.monthList
			    },
			    yAxis: {
			        type: 'value'
			    },
			    series: arr
			};

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