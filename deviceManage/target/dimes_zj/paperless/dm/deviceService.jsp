<%@page import="com.digitzones.model.Employee"%>
<%@page import="com.digitzones.model.ProductionUnit"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<%
    Employee employee = (Employee)session.getAttribute("employee");
	ProductionUnit productionLine=(ProductionUnit) session.getAttribute("productionLine");
%>
<link rel="stylesheet" href="mc/assets/css/bootstrap.min.css" media="screen" type="text/css" />
<script src="common/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="paperless/css/bootstrap-select.min.css">
<script type="text/javascript" src="paperless/js/bootstrap-select.min.js"></script>
<link rel="stylesheet" href="paperless/css/bootstrap-datetimepicker.min.css"> 

<script type="text/javascript"
    src="paperless/js/bootstrap-datetimepicker.min.js"></script>
    
<link rel="stylesheet"
    href="paperless/js/bootstrap-table/bootstrap-table.min.css">
<script type="text/javascript"
    src="paperless/js/bootstrap-table/bootstrap-table.js"></script>
<script type="text/javascript"
    src="paperless/js/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script
    src="paperless/js/bootstrap-table/extensions/editable/bootstrap-table-editable.js"></script>
<link rel="stylesheet"
    href="paperless/js/bootstrap-table/bootstrap-table.min.css">
    
<style>
body {
    margin: 0;
    background-color: #1C2437;
}
 
#layoutGraph div {
    height: 20px;
    width: 20px;
    position: relative;
    cursor: pointer;
    display: inline-block;
}
#outer {
    height: 750px;
    width: 100%;
    background-color: #1C2437;
}
 
#title {
    text-align: center;
    width: 100%;
    height: 50px;
    line-height: 50px;
    font-size: 50px;
    color: #FF0000;
    overflow: hidden;
}
 
#time {
    width: 100%;
    height: 90px;
    text-align: center;
    line-height: 98px;
    color: #57EEFD;
    font-size: 25px;
}
 
#productionUnit {
    height: 50px;
    font-size: 26px;
    color: #57EEFD;
    float: left;
    margin-left: 60px;
    margin-top: -95px;
    font-family: SimHei  ;
}
 
#department {
    height: 50px;
    font-size: 26px;
    color: #57EEFD;
    float: right;
    margin-right: 60px;
    margin-top: -95px;
    font-family: SimHei  ;
}
 
.font_style {
    color: white;
    font-size: 15px;
    margin-left: 5px;
}
 
#topPanel {
    height: 130px;
    width: 94%;
    margin: auto auto;
    margin-top: -50px;
    /* background-color: pink; */
    overflow: hidden;
}
 
.tp_left {
    height: 130px;
    width: 360px;
    /* margin-bottom: 20px; */
    float: left;
    background-color: #1C2437;
}
 
.tp_right {
    height: 90px;
    width: 1430px;
    /* background-color: red; */
    float: right;
    margin-top: 40px;
    overflow: hidden;
}
 
.tp_button {
    float: left;
    width: 220px;
    height: 100%;
    margin-right: 20px;
    color: white;
    line-height: 90px;
    font-size: 25px;
    text-align: center;
    border: 2px solid #666666;
}
 
 
#middle {
    height: 800px;
    width: 94%;
    margin: auto auto;
    margin-top: 15px;
}
 
.title {
    height: 40px;
    width: 100%;
    color: #57EEFD;
    font-size: 25px;
    margin-left: 10px;
}
 
.top {
    height: 205px;
    width: 100%;
    margin-bottom: 15px;
    background-color: #1C2437;
}
 
.top_content {
    height: 520px;
    width: 100%;
    border: solid 1px gray;
}
 
.bottom {
    height: 545px;
    width: 100%;
    background-color: #1C2437;
    overflow: hidden;
}
 
.bottom_content {
    height: 230px;
    width: 100%;
}
.left_title {
    height: 20px;
    width: 100%;
    color: #8AD5DA;
    margin-left: 20px;
    font-size: 18px;
}
 
.left_content {
    height: 150px;
    width: 90%;
    color: #1E8FF3;
    text-align: right;
    margin: 0 auto;
    border-bottom: solid 1px gray;
}
 
.left_content span {
    text-align: right;
    font-size: 5em;
}
 
.left_content span+span {
    font-size: 3em;
}
 
table {
    width: 95%;
    margin: auto auto;
}
 
table thead tr {
    border-bottom: solid 1px gray;
    color: #5D5F6A;
}
 
tr {
    height: 30px;
}
 
#singles tr td, #topSort tr td {
    text-align: center;
    color: #FFF;
}
 
#showStaffTable tr td, #showNGRecordTable tr td,#showMaintainProjectTable tr td, #showSparepartRecordTable tr td {
    text-align: center;
    color: #000;
}
 
#loginInfo {
    text-align: center;
    width: 100%;
    height: 40px;
    text-align: right;
    color: #57EEFD;
    margin-right: 50px;
    margin-bottom: 0px;
    position: absolute;
    left: -20px;
    top: 20px;
    font-size: 20px;
    z-index: 10000;
}
 
#warnning tr td:nth-child(odd) {
    background: #313348;
}
 
#warnning tr td:nth-child(even) {
    background: #2C2E45;
}
 
#topSort tr:nth-child(odd) {
    background: #313348;
}
 
#topSort tr:nth-child(even) {
    background: #2C2E45;
}
#right{
    height:100%;
    width:100%;
} 
.table2  th,td,tr{
    border:black 1px solid;
    color:black;
    font-size:14px;
    text-align:center;
    height:50px;
} 
 
 
.table1 td,th{
    border:white 1px solid;
    color:white;
    font-size:14px;
    text-align:center;
    height:50px;
}
*{margin:0px;padding:0px;}
.tabbox{}
.tabbox ul{list-style:none;display:table;margin-top: 4px;float:left;f}
.tabbox ul li{width:100px;height:77px;line-height:70px;padding-left:20px;margin-top:-20px;border:1px solid #00000E;cursor:pointer;background-color:#999999;}
#ngselete  ul  {margin-top:10px;}
#ngselete  ul  li{width:240px;height:40px;margin-top:-0px;margin-left:-20px;border:0px solid #00000E;cursor:pointer;background-color:#FFFFFF;}
.tabbox ul li.active{background-color:#FFFFFF;font-weight:bold;border:0px solid #00000E;}
.tabbox .content{}
.tabbox .content>div{display:none;}
.tabbox .content>div.active{display:block;margin-top: -30px}
 
.functionButton{
    width:110px;
    height:110px;
    padding-top:5px;
    border:2px solid darkgray;
    border-radius:16px;
    margin-right:20px;
    display:inline-block;
    cursor:pointer;
    text-align:center;
}
.updateButton{
    width:80px;
    height:80px;
    padding-top:5px;
    border:2px solid darkgray;
    border-radius:16px;
    margin-left:20px;
    float: right;
    margin-top:8px;
    display:inline-block;
    cursor:pointer;
    text-align:center;
}
</style>
 
<script type="text/javascript">
var _deviceSiteCode = ""; //选择的设备编码
//获取设备编码
_deviceSiteCode = parent.$("#deviceCode").val();
window.onload=function(){
    //定时器每秒调用一次fnDate()
    setInterval(function(){
    fnDate();
    },1000);
    }
    
    function fnDate(){
        //时间
        var date = new Date();
 
        var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
                + (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
                + (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
        $("#time").text(value);
    }
    
    $(function() {
        $("#photo").css("border","1px dotted #00000E");
        $("#updatePhoto").css("border","1px solid  #00000E");
        <%if (productionLine != null) {
   		 %>
        $.ajax({
                    type: "get",
                    dataType: "json",
                    async: false,  //同步
                    url: "paperlessDeviceRepair/queryPaperLessDeviceRepairOrderByDeviceCode.do",
                    data: {
                    deviceCode: _deviceSiteCode,
                    productionUnitId:"<%=productionLine.getId()%>"
                   },
                   success: function (data) {
                        var str = "";
                        //alert(JSON.stringify(data));
                        $.each(data, function (index, item) {  
                            //循环获取数据 
            var status = ""   ;
            var clickStatus="";
            var lookStatus="查看";
            var usercode="";
            <%if (employee != null) {
        		 %>
        		 
        		 if(item.status=='WAITINCOMFIRM'){
                     status='等待接单确认';
                     clickStatus="";
                 }else if(item.status=='WAITINGASSIGN'){
                     status='等待派单';
                     clickStatus="";
                 }else if(item.status=='MAINTAINING'){
                     status='维修中';
                     if(item.maintainCode=="<%=employee.getCode()%>"){
                     	clickStatus="维修";
                     }else{
	                     clickStatus="";
                     }
                 }else if(item.status=='MAINTAINCOMPLETE'){
                     status='维修完成';
                     clickStatus="";
                 }else if(item.status=='WAITWORKSHOPCOMFIRM'){
                     status='待车间确认';
                     clickStatus="";
                 }
        		 
        	<%} 
        	%>
            	
            
            
            
            var date = new Date(item.createDate);
 
            var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
                    + (date.getDate()<10?"0"+date.getDate():date.getDate()) + " " + (date.getHours()<10?"0"+date.getHours():date.getHours()) + ":"
                    + (date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes()) + ":" + (date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
 
            var completeDate="";
            if(item.completeDate){
            var date1 = new Date(item.completeDate);
 
            completeDate = date1.getFullYear() + "-" + ((date1.getMonth() + 1)<10?"0"+(date1.getMonth() + 1):(date1.getMonth() + 1)) + "-"
                    + (date1.getDate()<10?"0"+date1.getDate():date1.getDate()) + " " + (date1.getHours()<10?"0"+date1.getHours():date1.getHours()) + ":"
                    + (date1.getMinutes()<10?"0"+date1.getMinutes():date1.getMinutes()) + ":" + (date1.getSeconds()<10?"0"+date1.getSeconds():date1.getSeconds());
            }
            var ngname="";
            if(item.ngreason){
            	ngname=item.ngreason.name
            }
             var maintainName=item.maintainName==null?"":item.maintainName;   
                            str += '<tr>' +   
                            	'<td>' + (index+1) + '</td>' +  
                                '<td style="display:none">' + item.id + '</td>' +  
                                '<td>' + value + '</td>' +  
                                '<td>' + completeDate + '</td>' +  
                                '<td>' + status + '</td>' +  
                                '<td>' + ngname + '</td>' +  
                                '<td>' + maintainName + '</td>' +  
                                '<td style="width:100px;"><a href="javascript:void(0);" style="font-size:20px" onclick="parent.showS('+item.id+','+"'"+clickStatus+"'"+')">'+clickStatus+'</a> <a href="javascript:void(0);" style="font-size:20px" onclick="parent.showS('+item.id+','+"'"+lookStatus+"'"+')">'+lookStatus+'</a></td>' +  
                                '</tr>';  
                        }); 
                        $("#singles").prepend(str);
                    },
                });
        <%} 
     	%>
              });
        
       
    //退出
    function logout() {
        $.iMessager.confirm('确认对话框', '您想要退出该系统吗？', function(r) {
            if (r) {
                $.get("paperlessUser/logout.do", function(result) {
                    if (result.success) {
                        window.location.href = "paperless/homePage.jsp";
                    } else {
                        $.iMessager.alert("警告", "退出失败，系统内部错误!");
                    }
                });
            }
        });
    }
</script>
</head>
<body>
    <div id="outer" style="height: 100%; width: 100%;">
		<div style="color: white; font-size: 20px;">设备维修状况</div>
		<hr style="color: #666666; margin-top: 3px;" />
		<div style="width: 100%; text-align: center;">
			<table id="singles" class="table1"  style="width:98%;margin-left:auto;margin-right:auto;"> 
                        <thead> 
                            <tr>
                            	<th  style='width:60px;'></th>
                                <th>报修时间</th>
                                <th>维修完成时间</th>
                                <th>状态</th>
                                <th>故障类型</th>
                                <th>责任人</th>
                                <th style='width:150px;'></th>
                            </tr>
                        </thead>
                    </table>
		</div>
	</div>
</body>
</html>