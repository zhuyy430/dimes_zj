<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	$(function(){
		$.get('oee/queryOee4FactoryUp.do',function(data){
			var $main = $("#main");
			for (var i = 0;i<data.productionUnits.length;i++){
				var $div = $("<div style='float:left;height:400px;width:400px;' id='div_" + i + "' >");
				$main.append($div);
				oee("div_" + i,data.values[i]?data.values[i]:0,data.productionUnits[i].name);
			}
			secondary(data.preMonthOee,data.currentMonthOee,data.currentDayOee);
		});
	});
	//生产单元
	function oee(id,value,title){
		var app = echarts.init(document.getElementById(id));
		option = {
				title:{
					show:true,
					text:title,
					left:'center'
				},
			    tooltip : {
			        formatter: "{a} <br/>{b} : {c}%"
			    },
			    toolbox: {
			        feature: {
			            restore: {},
			            saveAsImage: {}
			        }
			    },
			    series: [
			        {
			            name: title,
			            type: 'gauge',
			            detail: {formatter:'{value}%'},
			            data: [{value: value, name: 'OEE'}]
			        }
			    ]
			};
		app.setOption(option);
	}
	//当前班
	function secondary(preMonthOee,currentMonthOee,currentDayOee){
		var app = echarts.init(document.getElementById('secondary'));
		option = {
			    tooltip : {
			        formatter: "{a} <br/>{c} {b}"
			    },
			    toolbox: {
			        show: true,
			        feature: {
			            restore: {show: true},
			            saveAsImage: {show: true}
			        }
			    },
			    series : [
			        {
			            name: '',
			            type: 'gauge',
			            z: 3,
			            min: 0,
			            max: 220,
			            splitNumber: 11,
			            radius: '50%',
			            axisLine: {            // 坐标轴线
			                lineStyle: {       // 属性lineStyle控制线条样式
			                    width: 10
			                }
			            },
			            axisTick: {            // 坐标轴小标记
			                length: 15,        // 属性length控制线长
			                lineStyle: {       // 属性lineStyle控制线条样式
			                    color: 'auto'
			                }
			            },
			            splitLine: {           // 分隔线
			                length: 20,         // 属性length控制线长
			                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
			                    color: 'auto'
			                }
			            },
			            axisLabel: {
			                backgroundColor: 'auto',
			                borderRadius: 2,
			                color: '#eee',
			                padding: 3,
			                textShadowBlur: 2,
			                textShadowOffsetX: 1,
			                textShadowOffsetY: 1,
			                textShadowColor: '#222'
			            },
			            title : {
			                // 其余属性默认使用全局文本样式，详见TEXTSTYLE
			                fontWeight: 'bolder',
			                fontSize: 20,
			                fontStyle: 'italic'
			            },
			            detail : {
			                // 其余属性默认使用全局文本样式，详见TEXTSTYLE
			                formatter: function (value) {
			                    value = (value + '').split('.');
			                    value.length < 2 && (value.push('00'));
			                    return ('00' + value[0]).slice(-2)
			                        + '.' + (value[1] + '00').slice(0, 2);
			                },
			                fontWeight: 'bolder',
			                borderRadius: 3,
			                backgroundColor: '#444',
			                borderColor: '#aaa',
			                shadowBlur: 5,
			                shadowColor: '#333',
			                shadowOffsetX: 0,
			                shadowOffsetY: 3,
			                borderWidth: 2,
			                textBorderColor: '#000',
			                textBorderWidth: 2,
			                textShadowBlur: 2,
			                textShadowColor: '#fff',
			                textShadowOffsetX: 0,
			                textShadowOffsetY: 0,
			                fontFamily: 'Arial',
			                width: 100,
			                color: '#eee',
			                rich: {}
			            },
			            data:[{value: currentMonthOee, name: '当前月'}]
			        },
			        {
			            name: '',
			            type: 'gauge',
			            center: ['20%', '55%'],    // 默认全局居中
			            radius: '35%',
			            min:0,
			            max:7,
			            endAngle:45,
			            splitNumber:7,
			            axisLine: {            // 坐标轴线
			                lineStyle: {       // 属性lineStyle控制线条样式
			                    width: 8
			                }
			            },
			            axisTick: {            // 坐标轴小标记
			                length:12,        // 属性length控制线长
			                lineStyle: {       // 属性lineStyle控制线条样式
			                    color: 'auto'
			                }
			            },
			            splitLine: {           // 分隔线
			                length:20,         // 属性length控制线长
			                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
			                    color: 'auto'
			                }
			            },
			            pointer: {
			                width:5
			            },
			            title: {
			                offsetCenter: [0, '-10%'],       // x, y，单位px
			            },
			            detail: {
			                // 其余属性默认使用全局文本样式，详见TEXTSTYLE
			                fontWeight: 'bolder'
			            },
			            data:[{value:preMonthOee, name: '上个月'}]
			        },
			        {
			            name: '',
			            type: 'gauge',
			            center: ['80%', '55%'],    // 默认全局居中
			            radius: '35%',
			            min:0,
			            max:7,
			            startAngle:135,
			            splitNumber:7,
			            axisLine: {            // 坐标轴线
			                lineStyle: {       // 属性lineStyle控制线条样式
			                    width: 8
			                }
			            },
			            axisTick: {            // 坐标轴小标记
			                length:12,        // 属性length控制线长
			                lineStyle: {       // 属性lineStyle控制线条样式
			                    color: 'auto'
			                }
			            },
			            splitLine: {           // 分隔线
			                length:20,         // 属性length控制线长
			                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
			                    color: 'auto'
			                }
			            },
			            pointer: {
			                width:5
			            },
			            title: {
			                offsetCenter: [0, '-10%'],       // x, y，单位px
			            },
			            detail: {
			                // 其余属性默认使用全局文本样式，详见TEXTSTYLE
			                fontWeight: 'bolder'
			            },
			            data:[{value:currentDayOee, name: '当天'}]
			        }
			    ]
			};

		app.setOption(option);
	}
</script>
</head>
<body>
	<div id="main" style="width:800px;height:400px;">
		
	</div>
	<div id="secondary" style="width:800px;height:600px;"></div>
</body>
</html>