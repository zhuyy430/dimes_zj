<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../common/jsp/head.jsp" %>
</head>
<script>
$(function() {
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		  $("#departmentDg").iDatagrid("reload",'trace/parameter.do?'+$("#searchForm").serialize());
		  $.get('trace/parameter.do?'+$("#searchForm").serialize(), function(result){
				$("#count").text(result.num);
			  });
	});
	//清空按钮点击事件
	$("#cleanBtn").click(function(){
		 $("#searchForm").form('clear');
		 $("#count").text(0);
	});
	//导出按钮点击事件
	$("#exportBtn").click(function(){
		window.location.href="trace/ExportParameter.do?"+$("#searchForm").serialize();
	});
});
</script>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <!-- datagrid表格 -->
                <table  
                data-toggle="topjui-datagrid"
                       data-options="id:'departmentDg',
                       url:'trace/parameter.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}]">
                    <thead>
                    <tr>
                    	<th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'opcNo',title:'二维码',width:'150px',align:'center'"></th>
                        <th data-options="field:'workPieceCode',title:'工件代码',width:'150px',align:'center'"></th>
                        <th data-options="field:'workPieceName',title:'工件名称',width:'150px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'150px',align:'center'"></th>
                        <th data-options="field:'stoveNumber',title:'材料编号',width:'150px',align:'center'"></th>
                        <th data-options="field:'processCode',title:'工序代码',width:'150px',align:'center'"></th>
                        <th data-options="field:'processName',title:'工序名称',width:'150px',align:'center'"></th>
                        <th data-options="field:'parameterCode',title:'参数代码',width:'150px',align:'center'"></th>
                        <th data-options="field:'parameterName',title:'参数名称',width:'150px',align:'center'"></th>
                        <th data-options="field:'upLine',title:'控制线UL',width:'150px',align:'center',formatter:function(value,row,index){
										if(value){
											return Math.floor(value * 100) / 100 ;
										}else{
											return '';
										}
									}"></th>
                        <th data-options="field:'lowLine',title:'控制线LL',width:'150px',align:'center',formatter:function(value,row,index){
										if(value){
											return Math.floor(value * 100) / 100 ;
										}else{
											return '';
										}
									}"></th>
                        <th data-options="field:'standardValue',title:'标准值',width:'150px',align:'center',formatter:function(value,row,index){
										if(value){
											return Math.floor(value * 100) / 100 ;
										}else{
											return '';
										}
									}"></th>
                        <th data-options="field:'parameterValue',title:'参数值',width:'150px',align:'center',formatter:function(value,row,index){
										if(value){
											return Math.floor(value * 100) / 100 ;
										}else{
											return '';
										}
									}"></th>
                        <th data-options="field:'status',title:'状态',width:'150px',align:'center'"></th>
                        <th data-options="field:'statusCode',title:'状态/故障代码',width:'150px',align:'center'"></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>

<!-- 参数追溯表格工具栏 -->
<div id="departmentDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
       <form id="searchForm" method="post" style="display: inline; float:;">
       <span> <label for="searchText">参数代码:</label> <input data-toggle="topjui-textbox" style="width: 150px;"
				type="text" name="parameterCode" id="parameterCode">
			</span>
    <span> <label for="searchText">工单单号:</label> <input data-toggle="topjui-textbox" style="width: 150px;"
				type="text" name="worksheetNo" id="worksheetNo">
			</span>
    <span> <label for="searchText">批号:</label> <input data-toggle="topjui-textbox" style="width: 150px;"
				type="text" name="batchNum" id="batchNum">
			</span>
    <span> <label for="searchText">追溯值:</label> <input data-toggle="topjui-numberbox" data-options="min:0,precision:2" style="width: 150px;"
				type="text" name="minValue" id="minValue">
			</span>
    <span> <label for="searchText">到</label> <input data-toggle="topjui-numberbox" data-options="min:0,precision:2" style="width: 150px;"
				type="text" name="maxValue" id="maxValue">
			</span>
			 <a id="searchBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">追溯</a>
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