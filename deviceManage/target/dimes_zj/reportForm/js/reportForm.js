//初始化yyyy-MM格式的时间
function initYYYYMMDate(){
    //月份查询条件组件初始化
    $('#qdate').datebox({
        //显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
        onShowPanel : function() {
            //触发click事件弹出月份层
            span.trigger('click');
            if (!tds)
            //延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                setTimeout(
                    function() {
                        tds = p.find('div.calendar-menu-month-inner td');
                        tds.click(function(e) {
                            //禁止冒泡执行easyui给月份绑定的事件
                            e.stopPropagation();
                            //得到年份
                            var year = /\d{4}/.exec(span.html())[0],
                                //月份
                                //之前是这样的month = parseInt($(this).attr('abbr'), 10) + 1;
                                month = parseInt($(this).attr('abbr'), 10);
                            //隐藏日期对象
                            $('#qdate').datebox('hidePanel')
                            //设置日期的值
                                .datebox( 'setValue',year+ '-'+ month);
                        });
                    }, 0);
        },
        //配置parser，返回选择的日期
        parser : function(s) {
            if (!s)
                return new Date();
            var arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(
                arr[1], 10) - 1, 1);
        },
        //配置formatter，只返回年月 之前是这样的d.getFullYear() + '-' +(d.getMonth());
        formatter : function(d) {
            var currentMonth = (d.getMonth() + 1);
            var currentMonthStr = currentMonth < 10 ? ('0' + currentMonth)
                : (currentMonth + '');
            return d.getFullYear() + '-' + currentMonthStr;
        }
    });
    //日期选择对象
    var p = $('#qdate').datebox('panel'),
        //日期选择对象中月份
        tds = false,
        //显示月份层的触发控件
        span = p.find('span.calendar-text');
    var curr_time = new Date();
    //设置前当月
    $("#qdate").datebox("setValue",curr_time.getFullYear() + "-" + (curr_time.getMonth() + 1));
}
//初始化生产单元窗口
function showProductionUnitDialog(){
    //显示生产单元窗口
    $('#showProductionUnitsDialog').dialog({
        title: '生产单元',
        width: 800,
        height: 600,
        closed: true,
        cache: false,
        href: 'reportForm/showProductionUnits.jsp',
        modal: true,
        buttons:[{
            text:'确定',
            handler:function(){
                var productionUnit = $('#productionUnitTable').iTreegrid('getSelected');
                confirmProductionUnits(productionUnit);
            }
        },{
            text:'关闭',
            handler:function(){
                $('#showProductionUnitsDialog').dialog("close");
            }
        }]
    });
}
//生产单元确认
function confirmProductionUnits(){
	var productionUnit = $('#productionUnitTable').iTreegrid('getSelected');
	$("#productionUnitId").iTextbox('setValue',productionUnit.id);
    $("#productionUnitId").iTextbox('setText',productionUnit.name);
    $('#showProductionUnitsDialog').dialog("close");
}
//显示日产量数据
function showDailyOutputData(){
    //生产日期
    var qdate = $("#qdate").iDatebox("getValue");
    //生产单元
    var productionUnitId = $("#productionUnitId").iTextbox("getValue");

    $.get("reportForm/queryDailyOutputData.do",{
        productDate:qdate,
        productionUnitId:productionUnitId
    },function(result){
        showDailyOutputChart(result);
        showDailyOutputReport(result.reportList);
    });
}
//显示oee记录数据
function showOeeRecordData(){
    //生产日期
    var qdate = $("#qdate").iDatebox("getValue");
    //生产单元
    var productionUnitId = $("#productionUnitId").iTextbox("getValue");

    $.get("reportForm/queryOeeRecordData.do",{
        productDate:qdate,
        productionUnitId:productionUnitId
    },function(result){
        showOeeRecordChart(result);
        showOeeRecordReport(result.reportList);
    });
}

//oee报表
function showOeeRecordReport(reportList){
    var reportTable = $("#oeeRecordReport");
    reportTable.empty();
    if(reportList!=null && reportList.length>0){
        for(var i = 0;i<reportList.length;i++){
            var tr = $("<tr style='height: 30px;'>");
            var dataList = reportList[i];
            if(dataList!=null && dataList.length>0){
                for(var j = 0;j<dataList.length;j++){
                    var data = dataList[j];
                    var td ;
                    if(j==0 || i==0){
                        td = $("<td style='border:1px solid gray;text-align: center;font-weight: bold;width:50px;background-color: lightgrey;'>");
                    }else{
                        td = $("<td style='border:1px solid gray;text-align: center;width:50px;'>");
                    }
                    td.append(data);
                    tr.append(td);
                }
            }
            reportTable.append(tr);
        }
    }
}

//显示oee记录图表
function showOeeRecordChart(result){
    var series = new Array();
    var colorArray = new Array();
    for(var i = 0;i<result.classes.length;i++){
        var jsonObj = {
            name:result.classes[i].name,
            type:'bar',
            itemStyle: {
                normal: {
                    color:colorArray[i],
                    label: {
                        show: false,
                        position: 'top',
                        formatter: '{c}'
                    }
                }
            },
            data:result.oeeList[i]
        }

        series[i] = jsonObj;
    }
    series.push({
        name:'平均值',
        type:'line',
        itemStyle : {
            normal : {
                color:'#888'
            }
        },
        data:result.avgs
    });

    series.push({
        name : '目标',
        type : 'line',
        itemStyle : {
            normal : {
                color:'#008000',
            }
        },
        data : result.goalOeeList
    });
    var jsonStr = JSON.stringify(series);
    var  option = {
        title:{
            show : true,
            text : 'oee'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data:result.classes
        },
        xAxis: [{
            type: 'category',
            data: result.days
        }],
        yAxis: [{
            type: 'value',
            name: 'oee值/百分比',
            min: 0,
            max: 100,
            interval: 10,
            axisLabel: {
                formatter: '{value}% '
            }
        }],
        series: series
    };
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('oeeRecordChart'));
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
}
//显示不合格品记录汇总数据
function showNgRecordSummaryData(){
    //生产日期
    var from = $("#from").iDatebox("getValue");
    var to = $("#to").iDatebox("getValue");
    //生产单元
    var productionUnitIds = $("#productionUnitId").iTextbox("getValue");

    $.get("reportForm/queryNgRecordSummaryData.do",{
        from:from,
        to:to,
        productionUnitIds:productionUnitIds
    },function(result){
        showNgRecordSummaryChart(result);
        showNgRecordSummaryReport(result.reportList);
    });
}
//显示不合格品汇总图表
function showNgRecordSummaryChart(result){
    var data = [];
    var categoryNames = [];
    if(result.ngRecordList!=null && result.ngRecordList.length>0){
        for(var i = 0;i<result.ngRecordList.length;i++){
            var objArray = result.ngRecordList[i];
            var obj = {value:objArray[1],name:objArray[0]};
            categoryNames.push(objArray[0]);
            data.push(obj);
        }
    }
    var myChart = echarts.init(document.getElementById("ngRecordSummaryChart"));
    option = {
        title : {
            text: '不合格品统计',
            x:'center'
        },
        tooltip : {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: categoryNames
        },
        series : [
            {
                name: '',
                type: 'pie',
                radius : '55%',
                center: ['50%', '60%'],
                data:data,
                itemStyle: {
                    normal:{
                        label:{
                            show: true,
                            position: 'inner',
                            formatter: '{d}%'
                        },
                        labelLine :{show:true} /*,
                        color:function(params) {
                            //自定义颜色
                            var colorList = result.emergencyColorList;
                            return colorList[params.dataIndex]
                        }*/
                    } ,
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    myChart.setOption(option);
}

//不合格品记录汇总报表
function showNgRecordSummaryReport(reportList){
    var reportTable = $("#ngRecordSummaryReport");
    reportTable.empty();
    if(reportList!=null && reportList.length>0){
        for(var i = 0;i<reportList.length;i++){
            var tr = $("<tr style='height: 30px;'>");
            var dataList = reportList[i];
            if(dataList!=null && dataList.length>0){
                for(var j = 0;j<dataList.length;j++){
                    var data = dataList[j];
                    var td ;
                    if(j==0 || i==0){
                        td = $("<td style='border:1px solid gray;text-align: center;font-weight: bold;width:50px;background-color: lightgrey;'>");
                    }else{
                        td = $("<td style='border:1px solid gray;text-align: center;width:50px;'>");
                    }
                    td.append(data);
                    tr.append(td);
                }
            }
            reportTable.append(tr);
        }
    }
}
//日产量报表
function showDailyOutputReport(reportList){
    var reportTable = $("#dailyOutputReport");
    reportTable.empty();
    if(reportList!=null && reportList.length>0){
        for(var i = 0;i<reportList.length;i++){
            var tr = $("<tr style='height: 30px;'>");
            var dataList = reportList[i];
            if(dataList!=null && dataList.length>0){
                for(var j = 0;j<dataList.length;j++){
                    var data = dataList[j];
                    var td ;
                    if(j==0 || i==0){
                        td = $("<td style='border:1px solid gray;text-align: center;font-weight: bold;width:50px;background-color: lightgrey;'>");
                    }else{
                        td = $("<td style='border:1px solid gray;text-align: center;width:50px;'>");
                    }
                    if(!isNaN(data)){
                        data = Math.floor(data * 100) / 100;
                    }
                    td.append(data);
                    tr.append(td);
                }
            }
            reportTable.append(tr);
        }
    }
}
//日产量图表
function showDailyOutputChart(data) {
    var arr = new Array();
    for(var i = 0;i<data.classNameList.length;i++){
        var className = data.classNameList[i];
        var ser = {
            name : className,
            type : 'bar',
            stack:data.classNameList[0],
            data : data.outputMap[className]
        };
        arr.push(ser);
    }

    var goalOutput = {
        data: data.goalOutput,
        type: 'line'
    };

    arr.push(goalOutput);

    var myChart = echarts.init(document.getElementById("dailyOutputChart"));
    option = {
        tooltip : {
            trigger : 'axis',
            axisPointer : { // 坐标轴指示器，坐标轴触发有效
                type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        legend : {
            data : data.classNameList
        },
        grid : {
            left : '3%',
            right : '4%',
            bottom : '3%',
            containLabel : true
        },
        xAxis : [ {
            type : 'category',
            data : data.days
        } ],
        yAxis : [ {
            type : 'value'
        } ],
        series : arr
    };
    myChart.setOption(option);
}
