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
<script>
	//存储设备编码
	var deviceCode = '';
	$(function(){
		//获取设备编码
		deviceCode = parent.$("#deviceCode").val();
		//查询点检记录
		loadCheckPlanRecord();
	});
	//加载项目点检记录
	function loadCheckPlanRecord(){
		 <%if (productionLine != null) {
    		 %>
			$.get("paperlessCheckPlan/queryCheckingPlanRecordByDeviceCode.do",{
				deviceCode:deviceCode,
				productionUnitId:"<%=productionLine.getId()%>"
			},function(result){
				if(result!=null && result.length>0){
					var $tbody = $("#checkPlanRecordContent");
					$tbody.empty();
					for(var i = 0;i<result.length;i++){
						var checkPlanRecord = result[i];
						var $tr = $("<tr>");
						
						var $num=$("<td>");
						$num.append(i+1);
						$tr.append($num);
						
						var $checkDate = $("<td>");
						$checkDate.append(getDate(new Date(checkPlanRecord.checkingDate)));
						$tr.append($checkDate);
						
						var $className = $("<td>");
						$className.append(checkPlanRecord.className);
						$tr.append($className);
						
						var checkedDate = checkPlanRecord.checkedDate;
						var $checkedDate = $("<td>");
						if(checkedDate!=null && checkedDate!=''){
							$checkedDate.append(getDate(new Date(checkedDate)));
						}
						$tr.append($checkedDate);
						
						var $status = $("<td>");
						$status.append(checkPlanRecord.status);
						$tr.append($status);
						
						var $employeeName = $("<td>");
						$employeeName.append(checkPlanRecord.employeeName);
						$tr.append($employeeName);
						
						
						var $opr = $("<td>");
						<%if (employee != null) {
			        		 %>
						if(checkPlanRecord.status=='已完成'){
							$opr.append("<a href='javascript:void(0)' style='font-size:20px' onClick='parent.showCheckingDetail("+checkPlanRecord.id+",0)'>查看</a>");
						}else{
							$opr.append("<a href='javascript:void(0)' style='font-size:20px' onClick='parent.showCheckingDetail("+checkPlanRecord.id+",1)'>点检</a>");
						}
						<%} 
			        	%>
						$tr.append($opr);
						$tbody.append($tr);
					}
				}
			});
			<%} 
     	%>
	}
	
	
</script>
<style>
body {
	margin: 0;
	background-color: #1C2437;
}

td, th {
	border: white 1px solid;
	color: white;
	font-size: 14px;
	text-align: center;
	height: 50px;
}
</style>
</head>
<body>
	<div id="outer" style="height: 100%; width: 100%;">
		<div style="color: white; font-size: 20px;">近期点检状况</div>
		<hr style="color: #666666; margin-top: 3px;" />
		<div style="width: 100%; text-align: center;">
			<table style="width: 98%; margin-left: auto; margin-right: auto;">
				<thead>
					<tr>
						<th  style='width:60px;'></th>
						<th  style='width:250px;'>点检计划日期</th>
						<th>班次</th>
						<th style='width:250px;'>点检时间</th>
						<th>状态</th>
						<th style='width:250px;'>点检人</th>
						<th  style='width:100px;'></th>
					</tr>
				</thead>
				<tbody id="checkPlanRecordContent">
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>