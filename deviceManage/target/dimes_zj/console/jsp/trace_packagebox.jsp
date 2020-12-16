<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
</head>
<script>
	$(function() {
		initTable();
		//搜索按钮点击事件
		$("#searchBtn").click(function() {
							$('#pTab').iDatagrid({
								fitColumns:true,
								singleSelect:true,
								queryParams: {
									startTime: $('#startTime').val(),
									endTime: $('#endTime').val(),
									packageBoxCode:$('#packageBoxCode').val(),
									workpieceCode:$('#workpieceCode').val(),
							    },
								url:'trace/packingBox.do',
							    columns:[[
							        {field:'barCode',title:'包装箱条码',width:100,align:'center'},
							        {field:'packingDate',title:'扫描时间',width:100,align:'center',
							        	formatter:function(value,row,index){
				                        	if(value){
				                        		var date = new Date(value);
				                        		return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
				                        	}else{
				                        		return '';
				                        	}
				                        }
							        },
							        {field:'operator',title:'操作人员',width:100,align:'center'},
							        {field:'realCount',title:'工件数量',width:100,align:'center'},
							        {field:'productionUnitName',title:'生产单元',width:100,align:'center'}
							    ]]
							});
							$('#wTab').iDatagrid({
								fitColumns:true,
								singleSelect:true,
								queryParams: {
									startTime: $('#startTime').val(),
									endTime: $('#endTime').val(),
									packageBoxCode:$('#packageBoxCode').val(),
									workpieceCode:$('#workpieceCode').val(),
							    },
								url:'trace/workpiece.do',
							    columns:[[
							        {field:'packingDate',title:'采集时间',width:100,align:'center',formatter:function(value,row,index){
				                        	if(value){
				                        		var date = new Date(value);
				                        		return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
				                        	}else{
				                        		return '';
				                        	}
				                        }
							        },
							        {field:'barCode',title:'二维码',width:100,align:'center'},
							        {field:'boxCode',title:'包装箱条码',width:100,align:'center',formatter: function(value,row,index){
							                if (row.box){
							                    return row.box.barCode;
							                } else {
							                    return "";
							                }
							            }
							        }
							    ]]
							});
		});
		//清空按钮点击事件
		$("#cleanBtn").click(function() {
			// $("#departmentDg").iDatagrid("loadData",{ total: 0, rows: [] });
			$("#searchForm").form('clear');
		});
	});
	
	function initTable(){
		$('#pTab').iDatagrid({
			fitColumns:true,
			singleSelect:true,
			queryParams: {
				startTime: $('#startTime').val(),
				endTime: $('#endTime').val(),
				packageBoxCode:$('#packageBoxCode').val(),
				workpieceCode:$('#workpieceCode').val(),
		    },
			url:'trace/packingBox.do',
		    columns:[[
		        {field:'barCode',title:'包装箱条码',width:100,align:'center'},
		        {field:'packingDate',title:'扫描时间',width:100,align:'center',
		        	formatter:function(value,row,index){
                    	if(value){
                    		var date = new Date(value);
                    		return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                    	}else{
                    		return '';
                    	}
                    }
		        },
		        {field:'operator',title:'操作人员',width:100,align:'center'},
		        {field:'realCount',title:'工件数量',width:100,align:'center'},
		        {field:'productionUnitName',title:'生产单元',width:100,align:'center'}
		    ]]
		});
		$('#wTab').iDatagrid({
			fitColumns:true,
			singleSelect:true,
			queryParams: {
				startTime: $('#startTime').val(),
				endTime: $('#endTime').val(),
				packageBoxCode:$('#packageBoxCode').val(),
				workpieceCode:$('#workpieceCode').val(),
		    },
			url:'trace/workpiece.do',
		    columns:[[
		        {field:'packingDate',title:'采集时间',width:100,align:'center',formatter:function(value,row,index){
                    	if(value){
                    		var date = new Date(value);
                    		return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                    	}else{
                    		return '';
                    	}
                    }
		        },
		        {field:'barCode',title:'二维码',width:100,align:'center'},
		        {field:'boxCode',title:'包装箱条码',width:100,align:'center',formatter: function(value,row,index){
		                if (row.box){
		                    return row.box.barCode;
		                } else {
		                    return "";
		                }
		            }
		        }
		    ]]
		});
	}
</script>
<body>
	<!-- <div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'">
                    <form id="searchForm" method="post" style="display: inline; float:;">
						<span> <label for="beginTime">开始时间: </label> <input
							id="startTime" data-toggle="topjui-datebox" type="text"
							name=startTime style="width: 200px;"> 结束时间: <input
							id="endTime" data-toggle="topjui-datebox" type="text" name="endTime"
							style="width: 200px;">
						</span> <span> <label for="searchText">包装条码:</label> <input
							data-toggle="topjui-textbox" style="width: 150px;" type="text"
							name="packageBoxCode" id="packageBoxCode">
						</span> <span> <label for="searchText">工件条码:</label> <input
							data-toggle="topjui-textbox" style="width: 150px;" type="text"
							name="workpieceCode" id="workpieceCode">
						</span> <a id="searchBtn" href="javascript:void(0)"
							data-toggle="topjui-menubutton"
							data-options="iconCls:'',plain:false"
							style="text-align: center; padding: 3px auto; width: 100px;">搜索</a>
						<a id="cleanBtn" href="javascript:void(0)"
							data-toggle="topjui-menubutton"
							data-options="iconCls:'',plain:false"
							style="text-align: center; padding: 3px auto; width: 100px;">清空</a>
					</form>
                    <div data-toggle="topjui-tabs"
                        data-options="id:'southTabs',
                     fit:true,
                     border:false
                    ">
                        <div title="包装条码" data-options="id:'tab0',iconCls:'fa fa-th'">
							datagrid表格
							<table id="pTab" data-options="fitColumns:true,singleSelect:true"></table>
						</div>
						<div title="工件条码" data-options="id:'tab2',iconCls:'fa fa-th'">
							datagrid表格
							<table id="wTab" data-options="fitColumns:true,singleSelect:true"></table>
						</div>
				</div>
			</div>
		</div>
	</div>
</div> -->
<div data-toggle="topjui-layout" data-options="fit:true">
<div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 22%;">
<form id="searchForm" method="post" style="display: inline; float:;">
						<div style="margin: 20px 0 0 40px"> <label for="beginTime">开始时间: </label> <input
							id="startTime" data-toggle="topjui-datebox" type="text"
							name=startTime style="width: 250px;"> &nbsp; &nbsp; &nbsp; &nbsp;
							 结束时间: <input
							id="endTime" data-toggle="topjui-datebox" type="text" name="endTime"
							style="width: 250px;">
						</div> 
						<div style="margin: 10px 0 0 40px"> <label for="searchText">包装条码:</label> <input
							data-toggle="topjui-textbox" style="width: 350px;" type="text"
							name="packageBoxCode" id="packageBoxCode">
						</div> 
						<div style="margin: 10px 0 0 40px"> <label for="searchText">工件条码:</label> <input
							data-toggle="topjui-textbox" style="width: 350px;" type="text"
							name="workpieceCode" id="workpieceCode">
						</div>
						<div style="margin: 10px 0 0 200px">
						 <a id="searchBtn" href="javascript:void(0)"
							data-toggle="topjui-menubutton"
							data-options="iconCls:'fa fa-search',plain:false"
							style="text-align: center; padding: 3px auto; width: 100px;">追溯</a>
						<a id="cleanBtn" href="javascript:void(0)"
							data-toggle="topjui-menubutton"
							data-options="iconCls:'fa fa-times',plain:false"
							style="text-align: center; padding: 3px auto; width: 100px;">清空</a>
						</div>
					</form>
</div>
<div data-options="region:'south',fit:false,split:true,border:false"
					style="height: 78%">
		<div data-toggle="topjui-tabs" 
                        data-options="id:'southTabs',
                     fit:true,
                     border:false
                    ">
                        <div title="包装条码" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table id="pTab" data-options="fitColumns:true,singleSelect:true"></table>
						</div>
						<div title="工件条码" data-options="id:'tab2',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table id="wTab" data-options="fitColumns:true,singleSelect:true"></table>
						</div>
		</div>
 </div> 
 </div>
</body>
</html>