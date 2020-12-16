<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript">
 $(function(){
	 //搜索按钮点击事件
	$("#searchBtn").click(function(){
		  $("#departmentDg").iDatagrid("reload",'report/queryProductionUnitOutputCountReport.do?'+$("#searchForm").serialize());
	});
	 
	  //设备站点搜索点击事件
	$("#deviceSiteCodes").iTextbox({
	    width:200,
	    prompt:'以逗号间隔站点代码',
	    buttonIcon:'fa fa-search',
	    onClickButton:function(){
	    	$('#showDeviceSitesDialog').dialog("open");
	    }
	});
	  
	$('#showDeviceSitesDialog').dialog({
	    title: '设备站点',
	    width: 700,
	    height: 600,
	    closed: true,
	    cache: false,
	    href: 'console/jsp/OutputBatchCountReport_showDeviceSites.jsp',
	    modal: true,
	    buttons:[{
	        text:'保存',
	        handler:function(){
	        	var deviceSites = $('#deviceTable').iDatagrid('getSelections');
           		var codesArray = new Array();
           			for(var i = 0;i < deviceSites.length;i++){
           				codesArray.push(deviceSites[i].code);
           			}
           			var codesStr = JSON.stringify(codesArray);
           			codesStr = codesStr.replace('[','');
           			codesStr = codesStr.replace(']','');
           			codesStr = codesStr.replace(/"/g,'');
           		$('#deviceSiteCodes').iTextbox('setValue',codesStr);
	    		$('#showDeviceSitesDialog').dialog("close");
	        }
	    },{
	        text:'关闭',
	        handler:function(){
	        	$('#showDeviceSitesDialog').dialog("close");
	        }
	    }]
	});

	//人员搜索点击事件
	$("#employee").iTextbox({
	    width:200,
	    prompt:'以逗号间隔人员代码',
	    buttonIcon:'fa fa-search',
	    onClickButton:function(){
	    	$('#showEmployeeDialog').dialog("open");
	    }
	});
	  
	$('#showEmployeeDialog').dialog({
	    title: '人员代码',
	    width: 700,
	    height: 600,
	    closed: true,
	    cache: false,
	    href: 'console/jsp/OutputBatchCountReport_showEmployee.jsp',
	    queryParams: { "productionUnitId": $('#productionUnitId').val() },
	    modal: true,
	    buttons:[{
	        text:'保存',
	        handler:function(){
	        	var deviceSites = $('#employeeTable').iDatagrid('getSelections');
           		var codesArray = new Array();
           			for(var i = 0;i < deviceSites.length;i++){
           				codesArray.push(deviceSites[i].code);
           			}
           			var codesStr = JSON.stringify(codesArray);
           			codesStr = codesStr.replace('[','');
           			codesStr = codesStr.replace(']','');
           			codesStr = codesStr.replace(/"/g,'');
           		$('#employee').iTextbox('setValue',codesStr);
	    		$('#showEmployeeDialog').dialog("close");
	        }
	    },{
	        text:'关闭',
	        handler:function(){
	        	$('#showEmployeeDialog').dialog("close");
	        }
	    }]
	});
	  //工序搜索点击事件
	$("#processess").iTextbox({
	    width:200,
	    prompt:'以逗号间隔工序代码',
	    buttonIcon:'fa fa-search',
	    onClickButton:function(){
	    	$('#showProcessessDialog').dialog("open");
	    }
	});
	  
	$('#showProcessessDialog').dialog({
	    title: '工序代码',
	    width: 700,
	    height: 600,
	    closed: true,
	    cache: false,
	    href: 'console/jsp/OutputBatchCountReport_showProcessess.jsp',
	    modal: true,
	    buttons:[{
	        text:'保存',
	        handler:function(){
	        	var deviceSites = $('#processessTable').iDatagrid('getSelections');
           		var codesArray = new Array();
           			for(var i = 0;i < deviceSites.length;i++){
           				codesArray.push(deviceSites[i].code);
           			}
           			var codesStr = JSON.stringify(codesArray);
           			codesStr = codesStr.replace('[','');
           			codesStr = codesStr.replace(']','');
           			codesStr = codesStr.replace(/"/g,'');
           		$('#processess').iTextbox('setValue',codesStr);
	    		$('#showProcessessDialog').dialog("close");
	        }
	    },{
	        text:'关闭',
	        handler:function(){
	        	$('#showProcessessDialog').dialog("close");
	        }
	    }]
	});
	  //工件搜索点击事件
	$("#workpiece").iTextbox({
	    width:200,
	    prompt:'以逗号间隔工件代码',
	    buttonIcon:'fa fa-search',
	    onClickButton:function(){
	    	$('#showWorkpieceDialog').dialog("open");
	    }
	});
	  
	$('#showWorkpieceDialog').dialog({
	    title: '工件代码',
	    width: 700,
	    height: 600,
	    closed: true,
	    cache: false,
	    href: 'console/jsp/OutputBatchCountReport_showWorkpiece.jsp',
	    modal: true,
	    buttons:[{
	        text:'保存',
	        handler:function(){
	        	var deviceSites = $('#workpieceTable').iDatagrid('getSelections');
           		var codesArray = new Array();
           			for(var i = 0;i < deviceSites.length;i++){
           				codesArray.push(deviceSites[i].code);
           			}
           			var codesStr = JSON.stringify(codesArray);
           			codesStr = codesStr.replace('[','');
           			codesStr = codesStr.replace(']','');
           			codesStr = codesStr.replace(/"/g,'');
           		$('#workpiece').iTextbox('setValue',codesStr);
	    		$('#showWorkpieceDialog').dialog("close");
	        }
	    },{
	        text:'关闭',
	        handler:function(){
	        	$('#showWorkpieceDialog').dialog("close");
	        }
	    }]
	});
}); 
 //显示更多筛选条件
 function searchMore() {
		$("#search").removeAttr("hidden");
		$("#Lhelp").removeAttr("hidden");
		$("#Mhelp").attr("hidden","hidden");
	}
 //隐藏筛选条件
	 function searchLess() {
		$("#Mhelp").removeAttr("hidden");
		$("#search").attr("hidden","hidden");
		$("#Lhelp").attr("hidden","hidden");
	}
</script>
</head>
<body>
	<div id="showDeviceSitesDialog"></div>
	<div id="showClassesDialog"></div>
	<div id="showEmployeeDialog"></div>
	<div id="showProcessessDialog"></div>
	<div id="showWorkpieceDialog"></div>
	<div data-toggle="topjui-layout" data-options="fit:true" style="">
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<!-- datagrid表格 -->
					<table data-toggle="topjui-datagrid"
						data-options="id:'departmentDg',
                       url:'report/queryProductionUnitOutputCountReport.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           		switchButton('classSwitchBtn',row.disabled);
					           }">
						<thead>
							<tr>
								<th
									data-options="field:'productionUnitDate',title:'日期',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[1];
                        		}"></th>
								<th
									data-options="field:'productionUnitCode',title:'生产单元代码',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[2];
                        		}"></th>
								<th
									data-options="field:'productionUnitName',title:'生产单元名称',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[3];
                        		}"></th>
								<th
									data-options="field:'deviceSiteCode',title:'设备站点代码',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[4];
                        		}"></th>
								<th
									data-options="field:'workPieceCode',title:'工件代码',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[5]
                        		}"></th>
								<th
									data-options="field:'workPieceName',title:'工件名称',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[6];
                        		}"></th>
								<th
									data-options="field:'unitType',title:'规格型号',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[7];
                        		}"></th>
								<th
									data-options="field:'processCode',title:'工序代码',width:'100px',align:'center',formatter:function(value,row,index){
                            		return row[8];
                        		}"></th>
								<th
									data-options="field:'processName',title:'工序名称',width:'100px',align:'center',formatter:function(value,row,index){
                            		return row[9];
                        		}"></th>
								<th
									data-options="field:'sum',title:'数量',width:'100px',align:'center',formatter:function(value,row,index){
                            		return row[0];
                        		}"></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 部门表格工具栏开始 -->
	<div id="departmentDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
		<form id="searchForm" method="post">
			<div>
				<span> <label for="productionUnitId">生产单元: </label> <input
					id="productionUnitId" name="productionUnitId"
					data-toggle="topjui-combobox"
					data-options="valueField:'id',textField:'name',url:'productionUnit/queryAllProductionUnits.do'"
					type="text" style="width: 200px;">
				</span> <span> <label for="beginTime">时&nbsp;间: </label> <input
					id="beginTime" data-toggle="topjui-datebox" type="text"
					name="beginDate" style="width: 200px;"> 至 <input
					id="endTime" data-toggle="topjui-datebox" type="text" name="endDate"
					style="width: 200px;">
				</span> <span> <label for="deviceSiteCodes">站点代码:</label> <input
					type="text" name="deviceSiteCodes" id="deviceSiteCodes">
				</span> 
				<a id="searchBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;float: ;">搜索</a>
				<a style="text-decoration: underline;float: ;margin: 5px" id="Mhelp" onclick="searchMore()" >更多条件</a>
				<a style="text-decoration: underline;float: ;margin: 5px" id="Lhelp" onclick="searchLess()" hidden="hidden">返回</a>
			</div> 
			<div id="search" hidden="hidden" style="margin-left: 2px;margin-top: 8px" >
				<!-- <span> <label for="classes">班&nbsp;&nbsp;次: </label> <input
					id="classes" name="classes"
					data-toggle="topjui-combobox"
					data-options="valueField:'code',textField:'name',url:'classes/queryAllClasses.do'"
					type="text" style="width: 200px;">
				</span> <span> <label for="employee">人&nbsp;&nbsp;员:</label> <input
					type="text" name="employee" id="employee">
				</span> --> <span> <label for="processess">工&nbsp;&nbsp;序:</label> <input
					type="text" name="processess" id="processess">
				</span> <span> <label for="workpiece">工&nbsp;&nbsp;件:</label> <input
					type="text" name="workpiece" id="workpiece">
				</span>
			</div> 
		</form>
	</div>
	<!-- 相关文档表格工具栏结束 -->
</body>
</html>