<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>

    <script type="text/javascript">

        $(function(){
            initDailyOutputDate();
            //显示生产单元按钮
            $("#productionUnitId").iTextbox({
                buttonIcon:'fa fa-search',
                onClickButton:function(){
                    $('#showProductionUnitsDialog').dialog("open");
                }
            });
            showProductionUnitDialog();
        });
        //点击产线，查询该产线的损时记录
        function requestLostTime(){
            //生产日期
            var qdate = $("#qdate").iDatebox("getValue");
            //生产单元
            var productionUnitId = $("#productionUnitId").iTextbox("getValue");
            if(productionUnitId==null||productionUnitId==""){
                alert("请选择生产单元");
            }
            $.get("lostTimeRecord/queryLostTimeReasonForProductionUnitAndMonth.do",{productDate:qdate,productionUnitId:productionUnitId},function(result){
                showLostTimeReport(result.lostTimeList);
                showLostTimeReasonGraph(result);
                console.log(result.lostTimeList);
            });
        }
        //损时图形
        function showLostTimeReasonGraph(result){
            var series = new Array();
            for(var i = 0;i<result.classes.length;i++){
                var list=result.lostTimeList[(i+1)];
                list.splice(0,1);
                var jsonObj =  {
                    name:result.classes[i].name,
                    type:'bar',
                    stack:'班次',
                    data:list
                }

                series[i] = jsonObj;
            }

            series.push({
                name : '目标',
                type : 'line',
                itemStyle : {
                    normal : {
                        color:'#008000',
                    }
                },
                lineStyle:{
                    width:5
                },
                data : result.goalLostTimeList
            });

            option = {
                tooltip : {
                    trigger: 'axis',
                    axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                    }
                },
                legend: {
                    data:result.classes
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
                        data : result.days
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : series
            };

            // 基于准备好的dom，初始化echarts实例
            var myChart = echarts.init(document.getElementById('lostTimeReasonGraphBody'));
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        }


        //日产量报表
        function showLostTimeReport(reportList){
            var reportTable = $("#lostTimeReport");
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

        //初始化日产量页面的时间
        function initDailyOutputDate(){
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
                    	confirmProductionUnits();
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
    </script>
    <style>
        #lostTimeCondition label{
            margin-left: 20px;
            font-size: 17px;
        }
    </style>
</head>
<body>
<div id="lostTimeCondition" style="height:50px;width: 100%;margin: 10px;padding-top: auto;padding-bottom: auto;">
    <label>生产日期</label>
    <input name="date" id="qdate" type="text" style="width: 150px;float:left;" editable="false" data-options="required:true,width:200,showSeconds:true,
					         fitColumns:true,
	                       pagination:true,
					         parentGrid:{
	                       	type:'treegird',
	                       	id:'departmentTg'
	                       }">
    <label>生产单元</label>
    <input id="productionUnitId"  data-toggle="topjui-textbox" style="width:200px;">
    <a href="javascript:void(0)"  class="layui-btn layui-btn-sm" onclick="requestLostTime()" style="text-decoration: none;margin-left: 10px;">确定</a>
    <a href="javascript:void(0)" class="layui-btn layui-btn-sm" onclick="exportExcel('lostTimeReport','损时时间')"  style="text-decoration: none;margin-left: 10px;">导出</a>
</div>

<div id="lostTimeReasonGraphBody" style="width:100%;height:500px;"></div>
<div style="width: 100%;">
    <table style="width: 95%;margin: auto auto;" id="lostTimeReport"></table>
</div>
</body>
</html>
<div id="showProductionUnitsDialog"></div>