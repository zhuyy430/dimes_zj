<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript">
 $(function(){
	 //搜索按钮点击事件
	$("#searchBtn").click(function(){
		  $("#departmentDg").iDatagrid("reload",'report/queryLostTimeCountReport.do?'+$("#searchForm").serialize());
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
	    href: 'console/jsp/lostTimeCountReport_showDeviceSites.jsp',
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
}); 
</script>
</head>
<body>
	<div id="showDeviceSitesDialog"></div>
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<!-- datagrid表格 -->
					<table data-toggle="topjui-datagrid"
						data-options="id:'departmentDg',
                       url:'report/queryLostTimeCountReport.do',
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
									data-options="field:'productionUnitCode',title:'生产单元代码',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[1];
                        		}"></th>
								<th
									data-options="field:'productionUnitName',title:'生产单元名称',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[2];
                        		}"></th>
								<th
									data-options="field:'deviceSiteCode',title:'设备站点代码',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[3];
                        		}"></th>
								<th
									data-options="field:'lostTimeBasicTypeName',title:'损时类别',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[5]==null?row[6]:row[5];
                        		}"></th>
								<th
									data-options="field:'lostTimeTypeName',title:'损时小类',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[6];
                        		}"></th>
								<th
									data-options="field:'lostTimeReason',title:'损时原因',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[7];
                        		}"></th>
								<th
									data-options="field:'lostTimeCount',title:'损时次数',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[0];
                        		}"></th>
								<th
									data-options="field:'lostTimeTime',title:'损时时间',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[8];
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
			<span> <label for="productionUnitId">生产单元: </label> <input
				id="productionUnitId" name="productionUnitId"
				data-toggle="topjui-combobox"
				data-options="valueField:'id',textField:'name',url:'productionUnit/queryAllProductionUnits.do'"
				type="text" style="width: 200px;">
			</span> <span> <label for="beginTime">损时时间: </label> <input
				id="beginTime" data-toggle="topjui-datebox" type="text"
				name="beginDate" style="width: 200px;"> 至 <input
				id="endTime" data-toggle="topjui-datebox" type="text" name="endDate"
				style="width: 200px;">
			</span> <span> <label for="deviceSiteCodes">站点代码:</label> <input
				type="text" name="deviceSiteCodes" id="deviceSiteCodes">
			</span> <a id="searchBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">搜索</a>
		</form>
	</div>
	<!-- 相关文档表格工具栏结束 -->
</body>
</html>