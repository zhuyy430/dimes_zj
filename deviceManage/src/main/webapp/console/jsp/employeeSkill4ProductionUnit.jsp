<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	function requestEmployeeSkill(productionUnitId) {
		$.get("employeeSkill/queryEmployeeSkill4empByProductionUnitId.do", {
			productionUnitId : productionUnitId
		}, function(result) {
			employeeSkill(result);
		});
	}
	//产线级
	function employeeSkill(data) {
		var myChart = echarts.init(document.getElementById("employeeSkillGraphBody"));

		var d = data.data;

		d = d.map(function (item) {
		    return [item[0], item[1], item[2] || '-'];
		});

		option = {
		    tooltip: {
		        position: 'top'
		    },
		    animation: false,
		    grid: {
		        height: '50%',
		        y: '10%'
		    },
		    xAxis: {
		        type: 'category',
		        data: data.emps,
		        splitArea: {
		            show: true
		        }
		    },
		    yAxis: {
		        type: 'category',
		        data: data.skillLevels,
		        splitArea: {
		            show: true
		        }
		    },
		    visualMap: {
		        min: 0,
		        max: 10,
		        calculable: true,
		        orient: 'horizontal',
		        left: 'center',
		        bottom: '15%'
		    },
		    series: [{
		        name: 'Punch Card',
		        type: 'heatmap',
		        data: d,
		        label: {
		            normal: {
		                show: true
		            }
		        },
		        itemStyle: {
		            emphasis: {
		                shadowBlur: 10,
		                shadowColor: 'rgba(0, 0, 0, 0.5)'
		            }
		        }
		    }]
		};
		 myChart.setOption(option);
	}
</script>
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
        	requestEmployeeSkill(rec.id);
        }">
	</div>
	<div id="employeeSkillGraphBody" style="width: 1800px; height: 800px;"></div>
</body>
</html>