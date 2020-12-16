<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../common/jsp/head.jsp" %>
</head>
<script>
$(function() {
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		  $("#departmentDg").iDatagrid("reload",'qualityCalendarRecord/queryQualityCalendarRecordsBySearch.do?'+$("#searchForm").serialize());
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
                       url:'qualityCalendarRecord/queryQualityCalendarRecords.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}]">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'currentDate',title:'日期',width:'180px',align:'center'"></th>
                        <th data-options="field:'typeName',title:'类别',width:'180px',align:'center'"></th>
                        <th data-options="field:'gradeName',title:'等级',width:'180px',align:'center'"></th>
                        <th data-options="field:'customer',title:'投诉客户',width:'180px',align:'center'"></th>
                        <th data-options="field:'content',title:'投诉内容',width:'180px',align:'center'"></th>
                        <th data-options="field:'contacts',title:'联系人',width:'180px',align:'center'"></th>
                        <th data-options="field:'tel',title:'联系方式',width:'180px',align:'center'"></th>
                        <th data-options="field:'note',title:'备注',width:'180px',align:'center'"></th>
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
       <sec:authorize access="hasAuthority('ADD_QUALITYCALENDARRECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'qualityCalendarRecordAddDialog',
           width:600,
           height:600,
           href:'console/jsp/qualityCalendarRecord_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var currentDate = $('#currentDate').val();
           			if(currentDate==null || ''===$.trim(currentDate)){
           				$.iMessager.alert('提示','请选择日期');
           				return false;
           			}
           			$.get('qualityCalendarRecord/addQualityCalendarRecord.do',{
           			currentDate:currentDate,
           			typeId:$('#typeId').val(),
           			typeName:$('#typeName').val(),
           			gradeId:$('#gradeId').val(),
           			gradeName:$('#gradeName').val(),
           			customer:$('#customer').val(),
           			content:$('#content').val(),
           			contacts:$('#contacts').val(),
           			tel:$('#tel').val(),
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#qualityCalendarRecordAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#qualityCalendarRecordAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
            </sec:authorize>
           <sec:authorize access="hasAuthority('EDIT_QUALITYCALENDARRECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 600,
                href: 'console/jsp/qualityCalendarRecord_edit.jsp',
                url:'qualityCalendarRecord/queryQualityCalendarRecordById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
          		 	var currentDate = $('#currentDate').val();
           			if(currentDate==null || ''===$.trim(currentDate)){
           				$.iMessager.alert('提示','请选择日期');
           				return false;
           			}
           			$.get('qualityCalendarRecord/updateQualityCalendarRecord.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			currentDate:currentDate,
           			typeId:$('#typeId').val(),
           			typeName:$('#typeName').val(),
           			gradeId:$('#gradeId').val(),
           			gradeName:$('#gradeName').val(),
           			customer:$('#customer').val(),
           			content:$('#content').val(),
           			contacts:$('#contacts').val(),
           			tel:$('#tel').val(),
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#classEditDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#classEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
             </sec:authorize>
            <sec:authorize access="hasAuthority('DEL_QUALITYCALENDARRECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'qualityCalendarRecord/deleteQualityCalendarRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
        </sec:authorize>
        <form id="searchForm" method="post" style="display:inline;float: right;">
			<span> <label for="beginTime">时间: </label> <input
				id="beginTime" data-toggle="topjui-datetimebox" type="text"
				name="beginDate" style="width: 190px;"> 至 <input
				id="endTime" data-toggle="topjui-datetimebox" type="text" name="endDate"
				style="width: 190px;">
			</span> 
			<span> <label for="searchChange">过滤字段: </label> <input
				id="searchChange" name="searchChange" type="text" style="width: 140px;"
				data-toggle="topjui-combobox" 
				data-options="
				valueField:'id',
				textField:'name',
				data:[{id:'typeName',name:'类别'},
					  {id:'gradeName',name:'等级'},
					  {id:'customer',name:'投诉客户'},
					  {id:'content',name:'投诉内容'},
					  {id:'contacts',name:'联系人'},
					  {id:'tel',name:'联系方式'},
					  {id:'note',name:'备注'}
				]"
				>
			</span> 
			<span> <label for="searchText">值:</label> <input data-toggle="topjui-textbox" style="width: 150px;"
				type="text" name="searchText" id="searchText">
			</span>
			 <a id="searchBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">搜索</a>
		</form>
    </div>
</body>
</html>