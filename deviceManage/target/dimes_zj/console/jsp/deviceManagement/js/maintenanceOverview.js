$(function(){
	init();
	queryData();
}); 

function showReport(){
	$.get("maintenanceOverview/separateOverviewReport.do",{
		from:$('#from').val(),
		to:$('#to').val()
},function(result){
	showOverviewReport(result)
});
}
//显示状态报表
function showOverviewReport(result){
	var myChart = echarts.init(document.getElementById("statusCountDiv"));
	option = {
		    tooltip : {
		        trigger: 'axis',
		        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
		            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
		        }
		    },
		    legend: {
		        data:['计划次数','完成次数','逾期次数','逾期未完成']
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
		            data :result.productionUnitNameList
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'完成次数',
		            type:'bar',
		            stack:'数量',
		            data:result.sumComplete
		        },
		        {
		            name:'逾期次数',
		            type:'bar',
		            stack: '数量',
		            data:result.sumDelay
		        },
		        {
		            name:'计划次数',
		            type:'line',
		            data:result.sumPlan
		        },
		        {
		            name:'逾期未完成',
		            type:'line',
		            data:result.sumDelayAndUncomplete
		        }
		    ]
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
	window.location.href='maintenanceOverview/exportOverview.do?from=' + $('#from').val() +"&to="+$('#to').val();
}

//显示数据
function showData(){
	if(!$("#from").val() || !$("#to").val()){
		alert("请选择时间范围!");
		return ;
	}
	$('#pg').pivotgrid({
    	title:'',
    	height:500,
    	url:'maintenanceOverview/overviewReport.do',
    	method:'get',
    	pivot:{
    		rows:['category'],
    		columns:['txt'],
    		values:[
    			{field:'planCount',title:'计划次数',align:'center'},
    			{field:'completeCount',title:'完成次数',align:'center'},
    			{field:'delayCount',title:'逾期次数',align:'center'},
    			{field:'delayAndUncompleteCount',title:'逾期未完成',align:'center'}
    		], aggregate: {
                footer: {
                    frozenColumnTitle: 'Total'
                }
            }
    	},queryParams:{
    		from:$('#from').val(),
    		to:$('#to').val()
    	}
    });
}