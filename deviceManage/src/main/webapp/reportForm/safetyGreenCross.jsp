<%@page import="com.digitzones.model.Employee"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<%
	Employee employee = (Employee)session.getAttribute("employee");
%>
<script type="text/javascript">

    $(function(){
        //月份查询条件组件初始化
        $('#qdate')
            .datebox(
                {
                    onChange : function(date) {
                        initTable();
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

        initTable();
        //扫码窗口改变事件
        $("#qdate").change(function() {
            initTable();
        })


    })
    //获取表格数据
    function initTable(){
        var time = $("#qdate").val();
        $.get("secureEnvironmentRecord/querySecureEnvironment.do",{time:time+"-01"},function(d){
            qualityCalendar(d);
            resultData = d;
        });
    }

    //显示安环等级
    function setSecureEnvironmentGrade(data){
        //显示安环等级
        var $gradeDiv = $("#gradeDiv");
        $gradeDiv.empty();
        var grades = data.grades;
        if(grades&&grades.length>0){
            for(var i = 0;i<grades.length;i++){
                var grade = grades[i];
                var $colorDiv = $("<span style='width:30px;height:15px;display:inline-block;border-radius:3px;background-color:"+ grade.color +"'></span>");
                var $textDiv= $("<span style='height:15px;margin-left:5px;display:inline-block;backgournd-color:red;'>"+grade.name+"</span>");
                $gradeDiv.append($colorDiv).append($textDiv);
            }
        }
    }

    function qualityCalendar(data){
        setSecureEnvironmentGrade(data);
        var list = data.data;
        //获取table对象
        var myChart = $("#mainTable");
        myChart.empty();
        var date = new Date();
        var currentDay  = date.getDate();
        var month = date.getMonth()+1;
        var findDate = new Date($("#qdate").val()+"-01")
        var findMonth = findDate.getMonth()+1;
        var days = new Date(findDate.getFullYear(),(findDate.getMonth()+1),0).getDate();
        var flag = [
            [false,false,true,true,true,false,false],
            [false,false,true,true,true,false,false],
            [true,true,true,true,true,true,true],
            [true,true,true,true,true,true,true],
            [true,true,true,true,true,true,true],
            [false,false,true,true,true,false,false],
            [false,false,true,true,true,false,false]
        ];
        var data = [[],[],[],[],[],[],[]];
        var day = 1;
        for(var outer = 0;outer<flag.length;outer++){
            var $tr = $("<tr>");
            myChart.append($tr);
            var outerFlag = flag[outer];
            var dataOuter = data[outer];
            for(var inner = 0;inner<outerFlag.length;inner++){
                if(day>days){
                    if(outerFlag[inner]){
                        var $td = $("<td style='border:1px solid gray;height:85px;'>");
                    }else{
                        dataOuter[inner]='';
                        var $td = $("<td style='text-align:center;'>");
                    }
                }else{
                    if(outerFlag[inner]){
                        dataOuter[inner]=day>=10?day:('0'+day);
                        var secureEnvironmentRecord = list[day];
                        if(secureEnvironmentRecord){
                            if(secureEnvironmentRecord.grade){
                                var $td = $("<td style='text-align:center;border:1px solid gray;background-color:"+secureEnvironmentRecord.grade.color+"'>");
                            }
                        }else{
                            if(findMonth<month){
                                var $td = $("<td style='text-align:center;border:1px solid gray;background-color:#0f0;height:85px;'>");
                            }else if(findMonth==month){
                                if(day<=currentDay){
                                    var $td = $("<td style='text-align:center;border:1px solid gray;background-color:#0f0;height:85px;'>");
                                }else{
                                    var $td = $("<td style='text-align:center;border:1px solid gray;background-color:#0C77A6;height:85px;'>");
                                }
                            }else{
                                var $td = $("<td style='text-align:center;border:1px solid gray;background-color:#0C77A6;height:85px;'>");
                            }
                        }
                        day++;
                    }else{
                        dataOuter[inner]='';
                        var $td = $("<td style='text-align:center;'>");
                    }
                }
                $td.append(dataOuter[inner]);
                $tr.append($td);
            }
        }
    }
</script>
<script type="text/javascript"></script>

<style>
	body {
		padding: 0px;
		margin: 0px;
	}

	.chartDiv {
		height: 808px;
		width: 1400px;
		margin-bottom: 5px;
		/*border: 1px solid #0459A2;*/
	}

	.directLeft {
		float: left;
	}

	.directRight {
		float: right;
	}

	.chartDiv>:first-child {
		height: 50px;
		width: 100%;
	}
	/*高度可通过父容器高度计算获取*/
	.chartBodyDiv {
		height: 340px;
		width: 100%;
	}
</style>
</head>
<body>
<!-- 最外层容器 -->
<!-- 安全 -->
<div class="chartDiv" id="safetyCross" style="margin-left: 150px;">
	<div id="safetyCrossBody" class="chartBodyDiv"
		 style="text-align: center;">
		<div id="main" style="width: 100%; height: 90%;margin-top: 5%">
			<div style="float:right;margin-right: 2%;margin-bottom: 2%">
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
			<table id="mainTable"
				   style="width: 95%; height: 95%; margin: auto auto;margin-bottom:20px;"></table>
		<div id="gradeDiv"
			 style="margin: 0 auto; text-align: center; text-size: 20px; height: 15px; line-height: 20px;"></div>
		</div>
	</div>
</div>
</body>
</html>
