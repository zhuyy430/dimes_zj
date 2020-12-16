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
 <link rel="stylesheet" type="text/css"
	href="paperless/css/bootstrap-select.min.css">
<link rel="stylesheet" href="paperless/css/bootstrap-datetimepicker.min.css"> 
<script type="text/javascript"
	src="paperless/js/bootstrap-select.min.js"></script> 
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
 
.title {
    height: 40px;
    width: 100%;
    color: #57EEFD;
    font-size: 25px;
    margin-left: 10px;
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

.mtabbox{}
.mtabbox ul{list-style:none;display:table;margin-top: 4px}
.mtabbox ul li{width:100px;height:77px;line-height:70px;padding-left:20px;margin-top:-20px;border:1px solid #00000E;cursor:pointer;background-color:#999999;}
.mtabbox ul li.active{background-color:#FFFFFF;font-weight:bold;border:0px solid #00000E;}
.mtabbox .content{}
.mtabbox .content>div{display:none;}
.mtabbox .content>div.active{display:block;margin-top: -30px}

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
 		$("#maintenancePhoto").css("border","1px solid  #00000E");
 		<%if (productionLine != null) {
 	   		 %>
        $.ajax({
                    type: "get",
                    dataType: "json",
                    async: false,  //同步
                    url: "paperlessMaintenancePlan/queryAllMaintenancePlanRecordsForPaperless.do",
                    data: {
                    deviceCode:  _deviceSiteCode,/* _deviceSiteCode, */
                    productionUnitId:"<%=productionLine.getId()%>"
                   },
                   success: function (data) {
                        var str = "";
                        //alert(JSON.stringify(data));
                        $.each(data, function (index, item) {  
                            //循环获取数据 
            
            var date = new Date(item.maintenanceDate);
 
            var value = date.getFullYear() + "-" + ((date.getMonth() + 1)<10?"0"+(date.getMonth() + 1):(date.getMonth() + 1)) + "-"
                    + (date.getDate()<10?"0"+date.getDate():date.getDate());
 
            var maintenancedDate="";
            if(item.maintenancedDate){
            var date1 = new Date(item.maintenancedDate);
 
            maintenancedDate = date1.getFullYear() + "-" + ((date1.getMonth() + 1)<10?"0"+(date1.getMonth() + 1):(date1.getMonth() + 1)) + "-"
                    + (date1.getDate()<10?"0"+date1.getDate():date1.getDate());
            }
            
            var clickStatus="";
            var lookStatus="查看";
            <%if (employee != null) {
       		 %>
	            if(item.status=='待接单'){
	                clickStatus="";
	            }else if(item.status=='待派单'){
	                clickStatus="";
	            }else if(item.status=='保养中'){
	                clickStatus="保养";
	            }else if(item.status=='已完成'){
	                clickStatus="";
	            }else if(item.status=='待确认'){
	                clickStatus="";
	            }else{
	            	clickStatus="";
	            }
	        <%} 
        	%>   
	         
            
            var employeeName = item.employeeName==null?"":item.employeeName;
            var confirmName =  item.confirmName==null?"":item.confirmName;
            if(item.status=='已完成'){
                            str += '<tr>' +   
                            	'<td>' + (index+1) + '</td>' +  
                                '<td style="display:none">' + item.id + '</td>' +  
                                '<td>' + item.maintenanceType + '</td>' +  
                                '<td>' + value + '</td>' +  
                                '<td>' + maintenancedDate + '</td>' +  
                                '<td>' + item.status + '</td>' +  
                                '<td>' + employeeName + '</td>' +  
                                '<td>' + confirmName + '</td>' +  
                                '<td><a href="javascript:void(0);" style="font-size:20px" onclick="parent.showM('+item.id+',1)">查看</a></td>' +  
                                '</tr>';
            }else{
            	if(item.employeeCode=="<%=employee.getCode()%>"){
            		str += '<tr>' +   
                	'<td>' + (index+1) + '</td>' +  
                    '<td style="display:none">' + item.id + '</td>' +  
                    '<td>' + item.maintenanceType + '</td>' +  
                    '<td>' + value + '</td>' +  
                    '<td>' + maintenancedDate + '</td>' +  
                    '<td>' + item.status + '</td>' +  
                    '<td>' + employeeName + '</td>' +  
                    '<td>' + confirmName + '</td>' +  
                    '<td style="width:100px;"><a href="javascript:void(0);" style="font-size:20px" onclick="parent.showM('+item.id+','+"'"+clickStatus+"'"+')">'+clickStatus+'</a> <a href="javascript:void(0);" style="font-size:20px" onclick="parent.showM('+item.id+','+"'"+lookStatus+"'"+')">'+lookStatus+'</a></td>' +  
                    '</tr>';
            	}else{
            		str += '<tr>' +   
                	'<td>' + (index+1) + '</td>' +  
                    '<td style="display:none">' + item.id + '</td>' +  
                    '<td>' + item.maintenanceType + '</td>' +  
                    '<td>' + value + '</td>' +  
                    '<td>' + maintenancedDate + '</td>' +  
                    '<td>' + item.status + '</td>' +  
                    '<td>' + employeeName + '</td>' +  
                    '<td>' + confirmName + '</td>' +  
                    '<td><a href="javascript:void(0);" style="font-size:20px" onclick="parent.showM('+item.id+',1)">查看</a></td>' +  
                    '</tr>';
            	}      
            	
            
            }
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
		<div style="color: white; font-size: 20px;">设备保养状况</div>
		<hr style="color: #666666; margin-top: 3px;" />
		<div style="width: 100%; text-align: center;">
			<table id="singles" class="table1"  style="width:98%;margin-left:auto;margin-right:auto;"> 
                        <thead> 
                            <tr>
                            	<th  style='width:60px;'></th>
                                <th>保养类别</th>
                                <th>计划日期</th>
                                <th>保养时间</th>
                                <th>状态</th>
                                <th>责任人</th>
                                <th>确认人</th>
                                <th style='width:100px;'></th>
                            </tr>
                        </thead>
                    </table>
		</div>
	</div>
</body>
</html>