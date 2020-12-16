<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
</head>
<script>
	$(function() {
		//搜索按钮点击事件
		$("#searchBtn").click(function() {
							$("#departmentDg").iDatagrid(
									"reload",
									'trace/reverse.do?'
											+ $("#searchForm").serialize());
							$.get('trace/reverse.do?'+$("#searchForm").serialize(), function(result){
								$("#count").text(result.num);
							  });
						});
		//清空按钮点击事件
		$("#cleanBtn").click(function() {
			$("#searchForm").form('clear');
			$("#count").text(0);
		});
		//导出按钮点击事件
		$("#exportBtn").click(function(){
			window.location.href="trace/ExportReverse.do?"+$("#searchForm").serialize();
		});
	});
</script>
<body>
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
                       url:'trace/reverse.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}]">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
								<th
									data-options="field:'deviceSiteCode',title:'设备站点代码',width:'150px',align:'center'"></th>
								<th
									data-options="field:'deviceSiteName',title:'设备站点名称',width:'150px',align:'center'"></th>
								<th
									data-options="field:'processCode',title:'工序代码',width:'150px',align:'center'"></th>
								<th
									data-options="field:'processName',title:'工序名称',width:'150px',align:'center'"></th>
								<th
									data-options="field:'parameterCode',title:'参数代码',width:'150px',align:'center'"></th>
								<th
									data-options="field:'parameterName',title:'参数名称',width:'150px',align:'center'"></th>
								<th
									data-options="field:'upLine',title:'控制线UL',width:'150px',align:'center',formatter:function(value,row,index){
										if(value){
											return Math.floor(value * 100) / 100 ;
										}else{
											return '';
										}
									}"></th>
								<th
									data-options="field:'lowLine',title:'控制线LL',width:'150px',align:'center',formatter:function(value,row,index){
										if(value){
											return Math.floor(value * 100) / 100 ;
										}else{
											return '';
										}
									}"></th>
								<th
									data-options="field:'standardValue',title:'标准值',width:'150px',align:'center',formatter:function(value,row,index){
										if(value){
											return Math.floor(value * 100) / 100 ;
										}else{
											return '';
										}
									}"></th>
								<th
									data-options="field:'parameterValue',title:'参数值',width:'150px',align:'center',formatter:function(value,row,index){
										if(value){
											return Math.floor(value * 100) / 100 ;
										}else{
											return '';
										}
									}"></th>
								<th
									data-options="field:'status',title:'状态',width:'150px',align:'center'"></th>
								<th
									data-options="field:'statusCode',title:'状态/故障代码',width:'150px',align:'center'"></th>
								<th
									data-options="field:'batchNumber',title:'批号',width:'150px',align:'center'"></th>
								<th
									data-options="field:'stoveNumber',title:'材料编号',width:'150px',sortable:false"></th>
								<th
									data-options="field:'collectionDate',title:'产生日期',width:'220px',sortable:false"></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>

	<!-- 反向追溯表格工具栏 -->
	<div id="departmentDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
		<form id="searchForm" method="post" style="display: inline; float:;">
			<span> <label for="searchText">二维码信息:</label> <input
				data-toggle="topjui-textbox" style="width: 150px;" type="text"
				name="serialNo" id="serialNo">
			</span> <span> <label for="searchText">批次信息:</label> <input
				data-toggle="topjui-textbox" style="width: 150px;" type="text"
				name="batchNumber" id="batchNumber">
			</span> <span> <label for="searchText">设备站点:</label> <input
				data-toggle="topjui-textbox" style="width: 150px;" type="text"
				name="deviceSiteCode" id="deviceSiteCode">
			</span> <a id="searchBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">搜索</a>
			<a id="cleanBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">清空</a>
			<a id="exportBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">导出</a>
		</form>
		<div style="float: right;margin-right: 10px">
			<span style="float: left; margin-top: 17px;" >共有</span>
			<span style="float: left; margin-top: 17px;" id="count">0</span>
			<span style="float: left; margin-top: 17px;" >个二维码</span>
		</div>
	</div>
</body>
</html>