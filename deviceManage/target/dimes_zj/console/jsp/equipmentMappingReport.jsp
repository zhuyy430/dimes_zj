<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript">
 $(function(){
	 //搜索按钮点击事件
	$("#searchBtn").click(function(){
		  $("#departmentDg").iDatagrid("reload",'report/queryEquipmentMappingCountReport.do?'+$("#searchForm").serialize());
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
	  //装备搜索点击事件
	$("#equipmentCodes").iTextbox({
	    width:200,
	    prompt:'以逗号间隔站点代码',
	    buttonIcon:'fa fa-search',
	    onClickButton:function(){
	    	$('#showEquipmentsDialog').dialog("open");
	    }
	});
	//装备表格
	$('#equipmentTable').iDatagrid({
	    url:'equipment/queryAllEquipments.do',
	    columns:[[
	        {field:'id',title:'id',checkbox:true},
	        {field:'code',title:'装备代码',width:100},
	        {field:'name',title:'装备名称',width:100}
	    ]]/* ,
	    queryParams:{
	    	productionUnitId:$("#productionUnitId").val()
	    } */
	});
	//站点表格
	$('#deviceTable').iDatagrid({
	    url:'deviceSite/queryDeviceSitesByProductionUnitId.do',
	    columns:[[
	        {field:'id',title:'id',checkbox:true},
	        {field:'code',title:'站点代码',width:100},
	        {field:'name',title:'站点名称',width:100},
	        {field:'deviceCode',title:'设备代码',width:100,formatter:function(value,row,index){
	        	if(row.device){
	        		return row.device.code;
	        	}else{
	        		return '';
	        	}
	        }},
	        {field:'deviceName',title:'设备名称',width:100,formatter:function(value,row,index){
	        	if(row.device){
	        		return row.device.name;
	        	}else{
	        		return '';
	        	}
	        }},
	        {field:'unitType',title:'规格型号',width:100,formatter:function(value,row,index){
	        	if(row.device){
	        		return row.device.unitType;
	        	}else{
	        		return '';
	        	}
	        }}
	    ]]/* ,
	    queryParams:{
	    	productionUnitId:$("#productionUnitId").val()
	    } */
	});
	$('#showDeviceSitesDialog').dialog({
	    title: '设备站点',
	    width: 700,
	    height: 600,
	    closed: true,
	    cache: false,
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
	$('#showEquipmentsDialog').dialog({
	    title: '装备',
	    width: 700,
	    height: 600,
	    closed: true,
	    cache: false,
	    modal: true,
	    buttons:[{
	        text:'保存',
	        handler:function(){
	        	var equipments = $('#equipmentTable').iDatagrid('getSelections');
           		var codesArray = new Array();
           			for(var i = 0;i < equipments.length;i++){
           				codesArray.push(equipments[i].code);
           			}
           			var codesStr = JSON.stringify(codesArray);
           			codesStr = codesStr.replace('[','');
           			codesStr = codesStr.replace(']','');
           			codesStr = codesStr.replace(/"/g,'');
           		$('#equipmentCodes').iTextbox('setValue',codesStr);
	    		$('#showEquipmentsDialog').dialog("close");
	        }
	    },{
	        text:'关闭',
	        handler:function(){
	        	$('#showEquipmentsDialog').dialog("close");
	        }
	    }]
	});
}); 
</script>
</head>
<body>
	<div id="showDeviceSitesDialog">
		<div data-toggle="topjui-layout" data-options="fit:true">
			<div
				data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
				<table id="deviceTable"></table>
			</div>
		</div>
	</div>
	<div id="showEquipmentsDialog">
		<div data-toggle="topjui-layout" data-options="fit:true">
			<div
				data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
				<table id="equipmentTable"></table>
			</div>
		</div>
	</div>
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
                       url:'report/queryEquipmentMappingCountReport.do',
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
									data-options="field:'deviceSite.code',title:'设备站点代码',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[1];
                        		}"></th>
								<th
									data-options="field:'deviceSite.name',title:'设备站点名称',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[2];
                        		}"></th>
								<th
									data-options="field:'mappingDate',title:'关联时间',width:'180px',align:'center',formatter:function(value,row,index){
									if (row[3]) {
                                        var date = new Date(row[3]);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                        var hour = date.getHours();
                                        var hourStr = ((hour>=10)?hour:('0' + hour));
                                        
                                        var minute = date.getMinutes();
                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
                                        
                                        var second = date.getSeconds();
                                        var secondStr = ((second>=10)?second:('0' +second));
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr  + 
                                        				' ' + hourStr + ':' + minuteStr + ':' + secondStr;
                                        return dateStr;
                                    }else{
                                    	return '';
                                    }
                           
                        		}"></th>
								<th
									data-options="field:'unbindDate',title:'解除时间',width:'180px',align:'center',formatter:function(value,row,index){
                       
                            		if (row[4]) {
                                        var date = new Date(row[4]);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                        var hour = date.getHours();
                                        var hourStr = ((hour>=10)?hour:('0' + hour));
                                        
                                        var minute = date.getMinutes();
                                        var minuteStr = ((minute>=10)?minute:('0' +minute));
                                        
                                        var second = date.getSeconds();
                                        var secondStr = ((second>=10)?second:('0' +second));
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr  + 
                                        				' ' + hourStr + ':' + minuteStr + ':' + secondStr;
                                        return dateStr;
                                    }else{
                                    	return '';
                                    }
                        		}"></th>
								<th
									data-options="field:'equipment.equipmentType.name',title:'装备类别',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[13];
                        		}"></th>
								<th
									data-options="field:'equipment.code',title:'装备代码',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[5];
                        		}"></th>
								<th
									data-options="field:'equipment.name',title:'装备名称',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[6];
                        		}"></th>
								<th
									data-options="field:'equipment.unitType',title:'规格型号',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[7];
                        		}"></th>
                        		<th
									data-options="field:'no',title:'序列号',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[8];
                        		}"></th>
                        		<th
									data-options="field:'equipment.measurementObjective',title:'计量目标',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[9];
                        		}"></th>
                        		<th
									data-options="field:'equipment.cumulation',title:'计量累计',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[10];
                        		}"></th>
                        		<th
									data-options="field:'equipment.measurementDifference',title:'计量差异',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[11];
                        		}"></th>
                        		<th
									data-options="field:'usageRate',title:'使用频次',width:'180px',align:'center',formatter:function(value,row,index){
                            		return row[12];
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
			<span> <label for="productionUnitId">辅助人: </label> <input
				id="productionUnitId" name="productionUnitId"
				data-toggle="topjui-combobox"
				data-options="valueField:'id',textField:'name',url:'employee/queryAllEmployees.do'"
				type="text" style="width: 200px;">
			</span> <span> <label for="beginTime">关联时间: </label> <input
				id="beginTime" data-toggle="topjui-datebox" type="text"
				name="beginDate" style="width: 200px;"> 至 <input
				id="endTime" data-toggle="topjui-datebox" type="text" name="endDate"
				style="width: 200px;">
			</span> <span> <label for="deviceSiteCodes">站点代码:</label> <input
				type="text" name="deviceSiteCodes" id="deviceSiteCodes">
			</span>
			<span> <label for="equipmentCodes">装备代码:</label> <input
				type="text" name="equipmentCodes" id="equipmentCodes">
			</span>
			<span> <label for="no">序列号:</label> <input data-toggle="topjui-textbox" style="width: 200px;"
				type="text" name="no" id="no">
			</span>
			 <a id="searchBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">搜索</a>
		</form>
	</div>
	<!-- 相关文档表格工具栏结束 -->
</body>
</html>