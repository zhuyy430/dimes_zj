<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../common/jsp/head.jsp" %>
   <script type="text/javascript">
 	function openWindow(){
		var checkedArray = $("#position").iDatagrid("getChecked");
		if(checkedArray.length>0){
			var ids = "";
			for(var i = 0;i<checkedArray.length;i++){
				var device = checkedArray[i];
				ids += device.id +",";
			}
			
			ids = ids.substring(0,ids.length-1);
			$("#ids").val(ids);
 			var newWin = window.open("console/jsp/ngReason_print.jsp"); 
		}else{
			alert("请选择要打印二维码的记录!");
			return false;
		}
	} 
</script>
</head>
<body>
<input type="hidden" id="ids" />
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
        <!-- treegrid表格 -->
        <table
         data-toggle="topjui-treegrid"
               data-options="id:'parameterTypeTg',
			   idField:'code',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'processes/queryProcessTree.do',
			   childGrid:{
			   	   param:'processId:id',
                   grid:[
                       {type:'datagrid',id:'position'}
                   ]
			   }">
            <thead>
            <tr>
               <!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
                <th data-options="field:'name',width:'100%',title:'工序'"></th>
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                 <table  
                data-toggle="topjui-datagrid"
                       data-options="id:'position',
                       url:'ngReason/queryNGReasonsByProcessId.do',
                       singleSelect:true,
						selectOnCheck:false,
						checkOnSelect:false,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'parameterTypeTg',
                       },
			           childTab: [{id:'southTabs'}]">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',checkbox:true"></th>
                                <th data-options="field:'ngCode',title:'不良原因代码',width:'150px',align:'center'"></th>
                                <th data-options="field:'ngReason',title:'不良原因',width:'150px',align:'center'"></th>
                                <th data-options="field:'category',title:'不良原因大类',width:'150px',align:'center'"></th>
                                <th data-options="field:'note',title:'备注',width:'150px',align:'center'"></th>
                                <th data-options="field:'processingMethod',title:'处理方法',width:'150px',align:'center'"></th>
                            </tr>
                            </thead>
                        </table>
            </div>
        </div>
    </div>
</div>
<!-- 部门表格工具栏开始 -->
<div id="position-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
    <sec:authorize access="hasAuthority('ADD_NGREASON')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'treegrid',
               id:'parameterTypeTg'
            },
       dialog:{
           id:'parameterAddDialog',
            width:600,
           height:400,
           href:'console/jsp/ngReason_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#ngCode').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入不良原因代码');
           				return false;
           			}
           			
           			var ngReason = $('#ngReason').val();
           			if(ngReason==null || ''===$.trim(ngReason)){
           				$.iMessager.alert('提示','请不良原因');
           				return false;
           			}
           			$.get('ngReason/addNGReason.do',{
           			ngCode:code,
           			ngReason:ngReason,
           			category:$('#category').val(),
           			processingMethod:$('#processingMethod').val(),
           			'process.id':$('#parameterTypeTg').iTreegrid('getSelected').id,
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#parameterAddDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
    </sec:authorize>
    <sec:authorize access="hasAuthority('EDIT_NGREASON')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#position-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'parameterEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/ngReason_edit.jsp',
                url:'ngReason/queryNGReasonById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#ngCode').val();
           			var ngReason = $('#ngReason').val();
           			if(ngReason==null || ''===$.trim(ngReason)){
           				$.iMessager.alert('提示','请输入不良原因');
           				return false;
           			}
           			$.get('ngReason/updateNGReason.do',{
           			id:$('#position').iDatagrid('getSelected').id,
           			ngCode:code,
           			ngReason:ngReason,
           			category:$('#category').val(),
           			processingMethod:$('#processingMethod').val(),
           			'process.id':$('#position').iDatagrid('getSelected').process.id,
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#parameterEditDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#parameterEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
    </sec:authorize>
    <sec:authorize access="hasAuthority('DEL_NGREASON')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'ngReason/deleteNGReason.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'id:id'},
       onSuccess:function(){$('#parameterTypeTg').iTreegrid('reload');}">删除</a>
    </sec:authorize>
    <sec:authorize access="hasAuthority('PRINTQR_NGREASON')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="iconCls: 'fa fa-print',
            modal:true,
            parentGrid:{
		       	type:'treegrid',
		       	id:'parameterTypeTg'
		      }" onClick="openWindow()">打印二维码</a>
    </sec:authorize>
    </div>
</body>
</html>