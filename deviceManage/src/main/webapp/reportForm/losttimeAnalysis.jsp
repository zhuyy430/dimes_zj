<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<script>
    var productionUnitIds;
    $(function() {

        var date = new Date();
        var month = date.getMonth() + 1 + '月';
        //故障时间



        var time = new Date();
        $("#time").text(
            time.getFullYear() + "-" + (time.getMonth() + 1) + "-"
            + time.getDate());






        $("#ProductionSearch").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showInventoryDialog').dialog("open");
            }
        });
        $('#showInventoryDialog').dialog({
            title: '生产单元',
            width: 800,
            height: 600,
            closed: true,
            cache: false,
            href: 'reportForm/productionSelect.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function(){
                    var row = $('#menus').iTreegrid('getCheckedNodes');
                    console.log(row);
                    var selectName=[];
                    var selectIds=[];
                    for(var i=0;i<row.length;i++){
                        selectName.push(row[i].name);
                        selectIds.push(row[i].id);
                    }
                    $('#ProductionSearch').iTextbox("setValue",selectName.join(","));
                    productionUnitIds=selectIds.join(",");
                    initTable(productionUnitIds,$("#qdate").val());
                    $('#showInventoryDialog').dialog("close");
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showInventoryDialog').dialog("close");
                }
            }]
        });
        //月份查询条件组件初始化
        $('#qdate').datebox(
                {
                    onChange : function(date) {
                        initTable(productionUnitIds,$("#qdate").val());
                    },
                    //显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
                    onShowPanel : function() {
                        //触发click事件弹出月份层
                        span.trigger('click');
                        if (!tds)
                        //延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                            setTimeout(
                                function() {
                                    tds = p
                                        .find('div.calendar-menu-month-inner td');
                                    tds
                                        .click(function(e) {
                                            //禁止冒泡执行easyui给月份绑定的事件
                                            e.stopPropagation();
                                            //得到年份
                                            var year = /\d{4}/
                                                    .exec(span
                                                        .html())[0],
                                                //月份
                                                //之前是这样的month = parseInt($(this).attr('abbr'), 10) + 1;
                                                month = parseInt($(
                                                    this).attr(
                                                    'abbr'), 10);

                                            //隐藏日期对象
                                            $('#qdate')
                                                .datebox(
                                                    'hidePanel')
                                                //设置日期的值
                                                .datebox(
                                                    'setValue',
                                                    year
                                                    + '-'
                                                    + month);
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
        $("#qdate").datebox("setValue",
            curr_time.getFullYear() + "-" + (curr_time.getMonth() + 1));


        //扫码窗口改变事件
        $("#qdate").change(function() {
            initTable(productionUnitIds,$("#qdate").val());
        })


        function initTable(productionUnitIds,productDate){
            $.get('lostTimeRecord/queryLostTimeRecordByYearAndMonthAndProduction.do',{
                productionUnitIds:productionUnitIds,
                productDate:productDate
            },function(result) {
                var myChart = echarts.init(document.getElementById('deviceHaltTime'));
                option = {
                    legend : {
                        textStyle : {
                            color : '#8FA4BF'
                        }
                    },
                    tooltip : {
                        trigger : 'axis',
                        showContent : false
                    },
                    dataset : {
                        source : result.data
                    },
                    xAxis : {
                        type : 'category',
                        axisLabel : {
                            color : '#8FA4BF'
                        }
                    },
                    yAxis : {
                        name : '故障时间',
                        gridIndex : 0,
                        axisLabel : {
                            formatter : '{value} h',
                            color : '#8FA4BF'
                        },
                        nameTextStyle : {
                            color : '#8FA4BF'
                        }
                    },
                    grid : {
                        top : '55%'
                    },
                    series : [ {
                        type : 'line',
                        smooth : true,
                        seriesLayoutBy : 'row'
                    }, {
                        type : 'line',
                        smooth : true,
                        seriesLayoutBy : 'row'
                    }, {
                        type : 'line',
                        smooth : true,
                        seriesLayoutBy : 'row'
                    }, {
                        type : 'line',
                        smooth : true,
                        seriesLayoutBy : 'row'
                    }, {
                        type : 'line',
                        smooth : true,
                        seriesLayoutBy : 'row'
                    }, {
                        type : 'line',
                        smooth : true,
                        seriesLayoutBy : 'row'
                    }, {
                        type : 'line',
                        smooth : true,
                        seriesLayoutBy : 'row'
                    }, {
                        type : 'line',
                        smooth : true,
                        seriesLayoutBy : 'row'
                    }, {
                        type : 'pie',
                        id : 'pie',
                        radius : '30%',
                        center : [ '50%', '25%' ],
                        tooltip:{
                            trigger:"item",
                            formatter:function(data){
                                var month;
                                var returnHtml="空";
                                var option = myChart.getOption();
                                var series = option.series;
                                for(var i = 0;i<series.length;i++){
                                    var encode = series[i].encode;
                                    if(encode){
                                        if(!isNaN(encode.value)){
                                            month = encode.value;
                                        }else{
                                            month=parseInt(encode.value.replace(/[^0-9]/ig,""));
                                        }
                                    }
                                }
                                $.ajaxSetup({
                                    async : false
                                });
                                $.get("lostTimeRecord/queryLostTimeDetail.do",{
                                    typeName:data.name,
                                    month:month
                                },function(result){
                                    //0:类别名称  1：详细类别名称  2：损时数
                                    if(result && result.length>0){
                                        for(var i = 0;i<result.length;i++){
                                            var detail = result[i];
                                            returnHtml=detail[0] + "\t" + detail[1] + "\t" + detail[2] + "<br />"
                                        }
                                    }
                                });
                                $.ajaxSetup({
                                    async : true
                                });
                                return returnHtml;
                            }
                        },
                        label : {
                            formatter : '{b}:  ({d}%)'
                        },
                        encode : {
                            itemName : result.data[0][0],
                            value : month,
                            tooltip : month
                        }
                    } ]
                };

                myChart.on('updateAxisPointer', function(event) {
                    var xAxisInfo = event.axesInfo[0];
                    if (xAxisInfo) {
                        var dimension = xAxisInfo.value + 1;
                        myChart.setOption({
                            series : {
                                id : 'pie',
                                label : {
                                    formatter : '{b}: {@[' + dimension
                                    + ']} ({d}%)'
                                },
                                encode : {
                                    value : dimension,
                                    tooltip : dimension
                                }
                            }
                        });
                    }
                });
                myChart.setOption(option);
            });
        }
    });



</script>
<style>
    body {
        margin: 0;
    }



    #middle {
        height: 700px;
        width: 90%;
        margin: auto auto;
    }



    .title {
        height: 40px;
        width: 100%;
        color: #2c3b41;
        font-size: 25px;
        margin-left: 10px;
        margin-top: 5px;
    }

    #left {
        height: 100%;
        width: 100%;
        float: left;

    }


    #deviceHaltTime {
        height: 100%;
        width: 100%;
    }


</style>
</head>
<body>

<div id="showInventoryDialog"></div>
    <div id="middle">
        <div class="title">损时时间分析表</div>
        <div class="topjui-row">
            <div class="topjui-col-sm3">
                <label class="topjui-form-label">损时月份</label>
                <div class="topjui-input-block">
                    <input name="date" id="qdate" type="text" style="width: 150px;float:left;" editable="false" data-options="required:true,width:200,showSeconds:true,
                                  fitColumns:true,
                               pagination:true,
                                 parentGrid:{
                                type:'treegird',
                                id:'departmentTg'
                               },
                               onSelect:function(index,row){
                                       initTable();
                                }
                                ">
                </div>
            </div>
            <div class="topjui-col-sm5">
                <label class="topjui-form-label">生产单元</label>
                <div class="topjui-input-block">
                    <input id="ProductionSearch" data-toggle="topjui-textbox">
                </div>
            </div>
        </div>
        <div id="left">
            <div id="deviceHaltTime"></div>
        </div>
    </div>


</body>
</html>