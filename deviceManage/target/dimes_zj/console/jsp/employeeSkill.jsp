<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	$(function(){
		$.get("employeeSkill/queryEmployeeSkill.do",function(data){
			process(data);
		});
		$.get("employeeSkill/queryEmployeeSkillForemp.do",function(data){
			emp(data);
		});
	});
	
	//人员技能：人员
	function emp(data){
		var myChart = echarts.init(document.getElementById("emp"));

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
	
	//人员技能：工序
	function process(data){
		var myChart = echarts.init(document.getElementById("process"));

		option = {
		    tooltip: {
		        trigger: 'item',
		        formatter: "{a} <br/>{b}: {c} ({d}%)"
		    } ,
		    legend: {
		        orient: 'vertical',
		        x: 'left',
		        data:data.names
		    },
		    series: [
		        {
		            name:'工序/等级数',
		            type:'pie',
		            selectedMode: 'single',
		            radius: [0, '30%'],

		            label: {
		                normal: {
		                    position: 'inner'
		                }
		            },
		            labelLine: {
		                normal: {
		                    show: false
		                }
		            },
		            data:data.inner
		        },
		        {
		            name:'等级数/工序',
		            type:'pie',
		            radius: ['40%', '55%'],
		            label: {
		                normal: {
		                    formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
		                    backgroundColor: '#eee',
		                    borderColor: '#aaa',
		                    borderWidth: 1,
		                    borderRadius: 4,
		                    rich: {
		                        a: {
		                            color: '#999',
		                            lineHeight: 22,
		                            align: 'center'
		                        },
		                        hr: {
		                            borderColor: '#aaa',
		                            width: '100%',
		                            borderWidth: 0.5,
		                            height: 0
		                        },
		                        b: {
		                            fontSize: 16,
		                            lineHeight: 33
		                        },
		                        per: {
		                            color: '#eee',
		                            backgroundColor: '#334455',
		                            padding: [2, 4],
		                            borderRadius: 2
		                        }
		                    }
		                }
		            },
		            data:data.outer
		        }
		    ]
		};
			    myChart.setOption(option);
	}
</script>
</head>
<body>
	<div id="main" style="width: 900px; height: 800px;">
		<div id="process" style="height: 50%;"></div>
		<div id="emp" style="height: 50%;"></div>
	</div>
</body>
</html>