$(function(){
	init();
	queryData();
}); 

function showReport(){
	$.get("checkStatistics/separateStatisticsReport.do",{
		from:$('#from').val(),
		to:$('#to').val(),
		cycle:$('#cycle').val()
},function(result){
	showDelayCountDiv(result);
	showDelayAndUncompleteCountDiv(result);
});
}

//逾期次数
function showDelayCountDiv(result){
	var myChart = echarts.init(document.getElementById("delayCountDiv"));
	var dataArray = new Array();
	if(result.delayMap!=null ){
		for (var key in result.delayMap) {
			var jsonObj = {
					name:key,
					type:'bar',
					stack: '逾期',
					data:result.delayMap[key]
					
			}
			dataArray.push(jsonObj);
		}
	}
	/*if(result.delayAndUncompleteMap!=null ){
		for (var key in result.delayAndUncompleteMap) {
			var jsonObj = {
					name:key,
					type:'bar',
					stack: '逾期未完成',
					data:result.delayAndUncompleteMap[key]
			
			}
			dataArray.push(jsonObj);
		}
	}*/
	option = {
			title: {
		        text: '逾期次数'
		    },
	    tooltip : {
	        trigger: 'axis',
	        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
	            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
	        }
	    },
	    legend: {
	        data:result.categorySet //生产单元
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '3%',
	        containLabel: true
	    },
	    xAxis : [
	        {
	            type : 'category',
	            data : result.dateList  //日期
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series :dataArray
	};
	myChart.setOption(option);
}
//逾期次数
function showDelayAndUncompleteCountDiv(result){
	var myChart = echarts.init(document.getElementById("delayAndUncompleteCountDiv"));
	var dataArray = new Array();
	/*if(result.delayMap!=null ){
		for (var key in result.delayMap) {
			var jsonObj = {
					name:key,
					type:'bar',
					stack: '逾期',
					data:result.delayMap[key]
			
			}
			dataArray.push(jsonObj);
		}
	}*/
	if(result.delayAndUncompleteMap!=null ){
		for (var key in result.delayAndUncompleteMap) {
			var jsonObj = {
					name:key,
					type:'bar',
					stack: '逾期未完成',
					data:result.delayAndUncompleteMap[key]
			
			}
			dataArray.push(jsonObj);
		}
	}
	option = {
			title: {
				text: '逾期未完成'
			},
			tooltip : {
				trigger: 'axis',
				axisPointer : {            // 坐标轴指示器，坐标轴触发有效
					type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend: {
				data:result.categorySet //生产单元
			},
			grid: {
				left: '3%',
				right: '4%',
				bottom: '3%',
				containLabel: true
			},
			xAxis : [
				{
					type : 'category',
					data : result.dateList  //日期
				}
				],
				yAxis : [
					{
						type : 'value'
					}
					],
					series :dataArray
	};
	myChart.setOption(option);
}

//查询统计信息
function queryData(){
	showData();
	showReport();
}
//重置查询表单
function resetForm(){
	var nowdate = new Date();
	$("#to").val(getDate(nowdate));
	nowdate.setMonth(nowdate.getMonth()-1); 
	var y = nowdate.getFullYear(); 
	var m = nowdate.getMonth()+1; 
	var d = nowdate.getDate(); 
	var formatwdate = y+'-'+m+'-'+d;
	$("#from").val(formatwdate);
	$("#cycle").combobox("setValue",'周');
}

//初始化操作
function init(){
	layui.use('laydate', function(){
		  var laydate = layui.laydate;
		  laydate.render({
		    elem: '#from'
		  });
		});
	layui.use('laydate', function(){
		var laydate = layui.laydate;
		laydate.render({
			elem: '#to'
		});
	});
	var nowdate = new Date();
	$("#to").val(getDate(nowdate));
	nowdate.setMonth(nowdate.getMonth()-1); 
	var y = nowdate.getFullYear(); 
	var m = nowdate.getMonth()+1; 
	var d = nowdate.getDate(); 
	var formatwdate = y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d);
	$("#from").val(formatwdate);
}
//导出数据
function exportData(){
	window.location.href='checkStatistics/exportStatistics.do?from=' + $('#from').val() +"&to="+$('#to').val()+"&cycle="+$('#cycle').val();
}

//显示数据
function showData(){
	if(!$("#from").val() || !$("#to").val()){
		alert("请选择时间范围!");
		return ;
	}
	$('#pg').pivotgrid({
    	title:'',
    	toolbar:"#toolbar",
    	height:500,
    	url:'checkStatistics/statisticsReport.do',
    	method:'get',
    	toolbar:'#toolbar',
    	pivot:{
    		rows:['category'],
    		columns:['date'],
    		values:[
    			{field:'planCount',title:'计划次数',op:'sum',align:'center'},
    			{field:'completeCount',title:'完成次数',op:'sum',align:'center'},
    			{field:'delayCount',title:'逾期次数',align:'center',op:'sum'},
    			{field:'delayAndUncompleteCount',title:'逾期未完成',align:'center',op:'sum'},
    		], aggregate: {
                footer: {
                    frozenColumnTitle: 'Total'
                }
            }
    	},queryParams:{
    		from:$('#from').val(),
    		to:$('#to').val(),
    		cycle:$('#cycle').val()
    	}
    });
}