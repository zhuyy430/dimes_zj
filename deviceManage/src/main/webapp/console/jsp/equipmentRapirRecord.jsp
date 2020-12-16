<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
 <script>
 $(function() {
		//搜索按钮点击事件
		$("#searchBtn").click(function(){
			  $("#departmentDg").iDatagrid("reload",'equipmentRepairRecord/queryEquipmentRepairRecordBySearch.do?'+$("#searchForm").serialize());
		});
	});
    </script>
</head>
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
                           url:'equipmentRepairRecord/queryEquipmentRepairRecord.do',
                           singleSelect:true,
                           fitColumns:true,
                           pagination:true">
                        <thead>
                        <tr>
                            <th data-options="field:'repairDate',title:'维修时间',width:'180px',align:'center',
                            formatter:function(value,row,index){
                                    return getDateTime(new Date(row.repairDate));
                            }"></th>
                            <th data-options="field:'code',title:'装备代码',width:'150px',align:'center',
                            formatter:function(value,row,index){
                                if(row.equipment){
                                    if(row.equipment.equipmentType){
                                        return row.equipment.equipmentType.code;
                                    }
                                    return '';
                                }else{
                                    return '';
                                }
                            }"></th>
                            <th data-options="field:'equipmentTypeName',title:'装备名称',width:'150px',align:'center',
                            formatter:function(value,row,index){
                                if(row.equipment){
                                    if(row.equipment.equipmentType){
                                        return row.equipment.equipmentType.name;
                                    }
                                    return '';
                                }else{
                                    return '';
                                }
                            }"></th>
                            <th data-options="field:'unitType',title:'规格型号',width:'150px',align:'center',
                            formatter:function(value,row,index){
                                if(row.equipment){
                                    if(row.equipment.equipmentType){
                                        return row.equipment.equipmentType.unitType;
                                    }
                                    return '';
                                }else{
                                    return '';
                                }
                            }"></th>
                            <th data-options="field:'equipmentTypeCode',title:'装备序号',width:'150px',align:'center',
                            formatter:function(value,row,index){
                                if(row.equipment){
                                    return row.equipment.code;
                                }else{
                                    return '';
                                }
                            }"></th>
                            <th data-options="field:'cumulation',title:'计量累计',width:'150px',align:'center',
                            formatter:function(value,row,index){
                                if(row.equipment){
                                    return row.equipment.cumulation;
                                }else{
                                    return '';
                                }
                            }"></th>
                            <th data-options="field:'pressLightTypeName',title:'故障类别',width:'150px',align:'center',
                            formatter:function(value,row,index){
                                if(row.pressLight){
                                    return row.pressLight.pressLightType.name;
                                }else{
                                    return '';
                                }
                            }"></th>
                            <th data-options="field:'reason',title:'维修原因',width:'150px',align:'center',
                            formatter:function(value,row,index){
                                if(row.pressLight){
                                        return row.pressLight.reason;
                                }else{
                                    return '';
                                }
                            }"></th>
                            <th data-options="field:'repairEmployeeName',title:'维修人',width:'150px',align:'center'"></th>
                            <th data-options="field:'note',title:'维修说明',width:'200px',align:'center'"></th>
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
<sec:authorize access="hasAuthority('ADD_EQUIPMENTRAPIRRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       dialog:{
           id:'departmentAddDialog',
           width:700,
           height:650,
           href:'console/jsp/equipmentRapirRecord_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           	var repairDate = $('#repairDate').val();
           	if(!repairDate){
           		alert('时间不能为空!');
           		return false;
           	}
           	var repairEmployeeName = $('#repairEmployeeName').val();
           	var repairEmployeeCode = $('#repairEmployeeCode').val();
           	var reason = $('#reason').val();
           	var note = $('#note').val();
           	var equipmentId = $('#equipmentId').val();

           	if(!equipmentId){
           		alert('请选择装备信息');
           		return false;
           	}
           			$.get('equipmentRepairRecord/addEquipmentRepairRecord.do',{
           			repairDate:repairDate,
           			repairEmployeeName:repairEmployeeName,
           			repairEmployeeCode:repairEmployeeCode,
           			'Equipment.id':equipmentId,
           			'pressLight.id':reason,
           			note:note
           			},function(data){
           				if(data.success){
	           				$('#departmentAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#departmentAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EDIT_EQUIPMENTRAPIRRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'departmentEditDialog',
                width:700,
                height: 650,
                href: 'console/jsp/equipmentRapirRecord_edit.jsp',
                url:'equipmentRepairRecord/queryEquipmentRepairRecordById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           	var repairDate = $('#repairDate').val();
                       	if(!repairDate){
                       		alert('时间不能为空!');
                       		return false;
                       	}
                       	var repairEmployeeName = $('#repairEmployeeName').val();
                        var repairEmployeeCode = $('#repairEmployeeCode').val();
                        var pressLightId = $('#pressLightId').val();
                        var note = $('#note').val();
                        var equipmentId = $('#equipmentId').val();

                       	if(!equipmentId){
                       		alert('请选择装备信息');
                       		return false;
                       	}
           			$.get('equipmentRepairRecord/updateEquipmentRepairRecord.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			repairDate:repairDate,
                    repairEmployeeName:repairEmployeeName,
                    repairEmployeeCode:repairEmployeeCode,
                    'Equipment.id':equipmentId,
                    'pressLight.id':pressLightId,
                    note:note
           			},function(data){
           				if(data.success){
	           				$('#departmentEditDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#departmentEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_EQUIPMENTRAPIRRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'equipmentRepairRecord/deleteEquipmentRepairRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
             <form id="searchForm" method="post" style="display:inline;float: right;">
			<span> <label for="beginTime">维修时间: </label> <input
				id="beginTime" data-toggle="topjui-datetimebox" type="text"
				name="beginDate" style="width: 190px;"> 至 <input
				id="endTime" data-toggle="topjui-datetimebox" type="text" name="endDate"
				style="width: 190px;">
			</span>
			<span> <label for="searchChange">选择: </label> <input
				id="searchChange" name="searchChange" type="text" style="width: 140px;"
				data-toggle="topjui-combobox"
				data-options="
				valueField:'id',
				textField:'name',
				data:[{id:'equipmentTypeCode',name:'装备代码'},
					  {id:'equipmentTypeName',name:'装备名称'},
					  {id:'equipmentCode',name:'装备序号'},
					  {id:'pressLightTypeName',name:'故障类别'},
					  {id:'pressLightReason',name:'维修原因'},
					  {id:'repairEmployeeName',name:'维修人'}
				]"
				>
			</span>
			<span> <label for="searchText">输入:</label> <input data-toggle="topjui-textbox" style="width: 150px;"
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