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
 			var newWin = window.open("console/jsp/measuringTool_print.jsp"); 
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
               data-options="id:'measuringToolTypeTg',
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'measuringToolType/queryTopEquipmentTypes.do',
			   childGrid:{
			   	   param:'pid:id',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   }">
            <thead>
            <tr>
               <!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
                <th data-options="field:'name',width:'100%',title:'量具信息'"></th>
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
                       data-options="id:'departmentDg',
                       url:'measuringToolType/queryEquipmentTypesByParentId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'measuringToolTypeTg',
                       },
			           childTab: [{id:'southTabs'}],
			            onSelect:function(index,row){
					           		switchButton('measuringToolTypeSwitchBtn',row.disabled);
					           },onLoadSuccess:function(){
					           		$('#position').iDatagrid('reload',{measuringToolTypeId:''});
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'code',title:'装备代码',width:'180px',align:'center'"></th>
                        <th data-options="field:'name',title:'装备名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'unitType',title:'规格型号',width:'100px',align:'center'"></th>
                        <th data-options="field:'manufacturer',title:'生产厂家',width:'100px',align:'center'"></th>
                        <th data-options="field:'baseCode',title:'baseCode',width:'100px',align:'center',hidden:true"></th>
                        <th data-options="field:'trader',title:'经销商',width:'100px',align:'center'"></th>
                        <th data-options="field:'outFactoryDate',title:'生产日期',width:'100px',align:'center',
                                 formatter:function(value,row,index){
                                    if (value) {
                                        var date = new Date(value);
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

                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr ;
                                        return dateStr;
                                    }else{
                                    	return '';
                                    }
                                }"></th>
                        <th data-options="field:'measurementType',title:'计量类型',width:'100px',align:'center'"></th>
                        <th data-options="field:'measurementObjective',title:'计量目标',width:'180px',align:'center'"></th>
                        <th data-options="field:'note',title:'备注',width:'180px',align:'center'"></th>
                        <th data-options="field:'disabled',title:'停用',width:'180px',align:'center',
                        formatter:function(value,row,index){
                        	if(value){
                        		return 'Y';
                        	}else{
                        		return 'N';
                        	}
                        }"></th>
                    </tr>
                    </thead>
                </table>
            </div>
            <div data-options="region:'south',fit:false,split:true,border:false"
                 style="height:40%">
                <div data-toggle="topjui-tabs"
                     data-options="id:'southTabs',
                     fit:true,
                     border:false,
                     parentGrid:{
                         type:'datagrid',
                         id:'departmentDg',
                         param:'measuringToolTypeId:id'
                     }">
                    <div title="明细信息" data-options="id:'tab0',iconCls:'fa fa-th'">
                        <!-- datagrid表格 -->
                        <table 
                         data-toggle="topjui-datagrid"
                               data-options="id:'position',
                               initCreate: false,
                              singleSelect:true,
								selectOnCheck:false,
								checkOnSelect:false,
                               fitColumns:true,
						       url:'measuringTool/queryEquipmentsByEquipmentTypeId.do'">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',checkbox:true"></th>
                                <th data-options="field:'code',title:'序号',width:'100px',align:'center'"></th>
                                <th data-options="field:'cumulation',title:'计量累计',width:'100px',align:'center'"></th>
                                <th data-options="field:'measurementDifference',title:'计量差异',width:'100px',align:'center'"></th>
                                <th data-options="field:'warrantyPeriod',title:'质保期',width:'100px',align:'center'"></th>
                                <th data-options="field:'status',title:'状态',width:'100px',align:'center'"></th>
                                <th data-options="field:'note',title:'备注',width:'100px',align:'center'"></th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
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
<sec:authorize access="hasAuthority('ADDTYPE_MEASURINGTOOL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'treegrid',
               id:'measuringToolTypeTg'
            },
       dialog:{
           id:'measuringToolTypeAddDialog',
           width:600,
           height:600,
           href:'console/jsp/measuringToolType_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入量具代码');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入量具名称');
           				return false;
           			}
           			
           			var baseCode = $('#measuringToolTypeTg').iTreegrid('getSelected').baseCode;
           			if(!baseCode){
           				baseCode = $('#measuringToolTypeTg').iTreegrid('getSelected').code;
           			}
           			
           			$.get('measuringToolType/addEquipmentType.do',{
           			code:code,
           			name:name,
           			baseCode:baseCode,
           			'parent.id':$('#measuringToolTypeTg').iTreegrid('getSelected').id,
           			unitType:$('#unitType').val(),
           			manufacturer:$('#manufacturer').val(),
           			trader:$('#trader').val(),
           			outFactoryDate:$('#outFactoryDate').val(),
           			measurementType:$('#measurementType').val(),
           			measurementObjective:$('#measurementObjective').val(),
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#measuringToolTypeAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
	           				$('#measuringToolTypeTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#measuringToolTypeAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'}
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDITTYPE_MEASURINGTOOL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'measuringToolTypeEditDialog',
                width: 600,
                height: 600,
                href: 'console/jsp/measuringToolType_edit.jsp',
                url:'measuringToolType/queryEquipmentTypeById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入类型名称');
           				return false;
           			}
           			$.get('measuringToolType/updateEquipmentType.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			code:code,
           			name:name,
           			baseCode:$('#departmentDg').iDatagrid('getSelected').baseCode,
           			'parent.id':$('#departmentDg').iDatagrid('getSelected').parent.id,
           			unitType:$('#unitType').val(),
           			manufacturer:$('#manufacturer').val(),
           			trader:$('#trader').val(),
           			outFactoryDate:$('#outFactoryDate').val(),
           			measurementType:$('#measurementType').val(),
           			measurementObjective:$('#measurementObjective').val(),
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#measuringToolTypeEditDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
	           				$('#departmentTg').iTreegrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#measuringToolTypeEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'}
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DELTYPE_MEASURINGTOOL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'measuringToolType/deleteEquipmentType.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#measuringToolTypeTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DISABLETYPE_MEASURINGTOOL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'measuringToolType/disabledEquipmentType.do',
       grid: {uncheckedMsg:'请选择操作的参数类型',id:'departmentDg',param:'id:id'}" id="measuringToolTypeSwitchBtn">停用</a>
</sec:authorize>
    </div>
<!-- 部门表格工具栏结束 -->
<!-- 职位表格工具栏开始 -->
<div id="position-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
<sec:authorize access="hasAuthority('ADD_MEASURINGTOOL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
               type:'datagrid',
               id:'departmentDg'
            },
       dialog:{
           id:'measuringToolAddDialog',
            width:600,
           height:500,
           href:'console/jsp/measuringTool_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入模具代码!');
           				return false;
           			}
           			
           			$.get('measuringTool/addEquipment.do',{
           			code:code,
           			baseCode:$('#departmentDg').iDatagrid('getSelected').baseCode,
           			'equipmentType.id':$('#departmentDg').iDatagrid('getSelected').id,
           			status:$('#status').iCombobox('getValue'),
           			cumulation:$('#cumulation').val(),
           			note:$('#note').val(),
           			measurementDifference:$('#measurementDifference').val(),
           			warrantyPeriod:$('#warrantyPeriod').val(),
           			picName:$('#picName').val()
           			},function(data){
           				if(data.success){
	           				$('#measuringToolAddDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#measuringToolAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'}
           ]
       }">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_MEASURINGTOOL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#position-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'measuringToolEditDialog',
                width: 600,
                height: 500,
                href: 'console/jsp/measuringTool_edit.jsp',
                url:'measuringTool/queryEquipmentById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入模具代码!');
           				return false;
           			}
           			$.get('measuringTool/updateEquipment.do',{
           			id:$('#position').iDatagrid('getSelected').id,
           			code:code,
           			baseCode:$('#departmentDg').iDatagrid('getSelected').baseCode,
           			'equipmentType.id':$('#departmentDg').iDatagrid('getSelected').id,
           			status:$('#status').iCombobox('getValue'),
           			cumulation:$('#cumulation').val(),
           			note:$('#note').val(),
           			measurementDifference:$('#measurementDifference').val(),
           			warrantyPeriod:$('#warrantyPeriod').val(),
           			pic:$('#pic').val()
           			},function(data){
           				if(data.success){
	           				$('#measuringToolEditDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#measuringToolEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'}
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MEASURINGTOOL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'measuringTool/deleteEquipment.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DISABLE_MEASURINGTOOL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
      data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-stop',
       url:'measuringTool/disabledEquipment.do',
       grid: {uncheckedMsg:'请选择操作的参数',id:'position',param:'id:id'}" id="measuringToolSwitchBtn">停用</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PRINTQR_MEASURINGTOOL')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="iconCls: 'fa fa-print',
            modal:true,
            parentGrid:{
		       	type:'datagrid',
		       	id:'departmentDg'
		      }" onClick="openWindow()">打印二维码</a>
</sec:authorize>
    </div>
<!-- 职位表格工具栏结束 -->
<!-- 相关文档表格工具栏开始 -->
<div id="relateDoc-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'relateDoc'
       }">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#relateDoc-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'userAddDialog',
           href:_ctx + '/html/complex/dialog_add.html',
           buttonsGroup:[
               {text:'保存',url:_ctx + '/json/response/success.json',iconCls:'fa fa-plus',handler:'ajaxForm',btnCls:'topjui-btn-brown'}
           ]
       }">新增</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#relateDoc-toolbar',
            iconCls: 'fa fa-pencil',
            grid: {
                type: 'datagrid',
                id: 'userDg'
            },
            dialog: {
                width: 950,
                height: 500,
                href: _ctx + '/html/complex/user_edit.html?uuid={uuid}',
                url: _ctx + '/json/product/detail.json?uuid={uuid}',
                buttonsGroup: [
                    {
                        text: '更新',
                        url: _ctx + '/json/response/success.json',
                        iconCls: 'fa fa-save',
                        handler: 'ajaxForm',
                        btnCls: 'topjui-btn-green'
                    }
                ]
            }">编辑</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#relateDoc-toolbar',
       iconCls:'fa fa-trash',
       url:_ctx + '/json/response/success.json',
       grid: {uncheckedMsg:'请先勾选要删除的数据',param:'uuid:uuid,code:code'}">删除</a>
<!--     <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'filter',
       extend: '#userDg-toolbar'
       ">过滤</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'search',
       extend: '#userDg-toolbar'">查询</a> -->
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'search',
       extend: '#relateDoc-toolbar'">停用</a>
    </div>
<!-- 相关文档表格工具栏结束 -->
</body>
</html>