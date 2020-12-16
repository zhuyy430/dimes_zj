<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
</head>
<script>
$(function() {
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		  $("#departmentDg").iDatagrid("reload",'equipmentMappingRecord/queryEquipmentMappingRecordByDeviceSiteIdandSearch.do?'+$("#searchForm").serialize());
	});
});
</script>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
			<table id="departmentTg" data-toggle="topjui-treegrid"
				data-options="id:'departmentTg',
			   idField:'code',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'productionUnit/queryDeviceSiteTree.do?module=dimes',
			   childGrid:{
			   	   param:'deviceSiteId:id',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   }">
				<thead>
					<tr>
						<!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
						<th data-options="field:'name',width:'100%',title:'生产单元'"></th>
					</tr>
				</thead>
			</table>
		</div>
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<!-- datagrid表格 -->
					<table data-toggle="topjui-datagrid"
						data-options="id:'departmentDg',
                       url:'equipmentMappingRecord/queryEquipmentMappingRecordByDeviceSiteId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'departmentTg',
                       },
			           childTab: [{id:'southTabs'}]">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
								<th
									data-options="field:'mappingDate',title:'关联时间',width:'180px',align:'center',formatter:function(value,row,index){
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
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr  + 
                                        				' ' + hourStr + ':' + minuteStr + ':' + secondStr;
                                        return dateStr;
                                    }else{
                                    	return '';
                                    }
                        }"></th>
								<th
									data-options="field:'equipmentCode',title:'装备序列号',width:'120px',align:'center',sortable:false,formatter:function(value,row,index){
                         if (row.equipment) {
                                        return row.equipment.code;
                                    }else{
                                    	return '';
                                    }
                        }"></th>
								<th
									data-options="field:'equipmentName',title:'装备名称',width:'120px',align:'center',sortable:false,formatter:function(value,row,index){
                        			 if (row.equipment) {
                        			 	if(row.equipment.equipmentType){
                                        return row.equipment.equipmentType.name;
                                      }else{
                                      	return '';
                                      }
                                    }else{
                                    	return '';
                                    }
                        }"></th>
								<th
									data-options="field:'pressLightUserName',title:'计量类型',width:'120px',align:'center',sortable:false,formatter:function(value,row,index){
                        			 if (row.equipment) {
                        			 if(row.equipment.equipmentType){
                                        return row.equipment.equipmentType.measurementType;
                                      }else{
                                      	return '';
                                      }
                                    }else{
                                    	return '';
                                    }
                        		}"></th>
								<th
									data-options="field:'lightOutUserName',title:'计量目标',width:'120px',align:'center',sortable:false,formatter:function(value,row,index){
                        			 if (row.equipment) {
                        			 if(row.equipment.equipmentType){
                                        return row.equipment.equipmentType.measurementObjective;
                                      }else{
                                      	return '';
                                      }
                                    }else{
                                    	return '';
                                    }
                        		}"></th>
								<th
									data-options="field:'lightOutTime',title:'计量累计',width:'120px',align:'center',sortable:false,formatter:function(value,row,index){
                        			 if (row.equipment) {
                                        return row.equipment.cumulation;
                                    }else{
                                    	return '';
                                    }
                        		}"></th>
								<th
									data-options="field:'recovered',title:'计量差异',width:'120px',align:'center',sortable:false,formatter:function(value,row,index){
                        			 if (row.equipment) {
                                        return row.equipment.measurementDifference;
                                    }else{
                                    	return '';
                                    }
                        		}"></th>
								<th
									data-options="field:'bindUsername',title:'关联人员',width:'120px',align:'center',sortable:false"></th>
								<th
									data-options="field:'helperName',title:'辅助人员',width:'120px',align:'center',sortable:false"></th>
								<th
									data-options="field:'workSheetCode',title:'工单号',width:'120px',align:'center',sortable:false"></th>
								<th
									data-options="field:'usageRate',title:'使用频次',width:'120px',align:'center',sortable:false"></th>
								<th
									data-options="field:'unbind',title:'关联状态',width:'120px',align:'center',sortable:false,formatter:function(value,row,index){
                        			 if (row.unbind==true) {
                                        return '已解除';
                                    }else{
                                    	return '已关联';
                                    }
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
<sec:authorize access="hasAuthority('ADD_EQUIPMENTMAPPINGRECORD')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'treegrid',
       	id:'departmentTg',
       	params:'deviceSiteId:id'
       },
       dialog:{
           id:'departmentAddDialog',
           width:600,
           height:500,
           href:'console/jsp/equipmentMappingRecord_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var equipmentId = $('#equipment').val();
           			if(!equipmentId){
           				$.iMessager.alert('提示','请选择设备');
           				return false;
           			}
           			$.get('equipmentMappingRecord/addEquipmentMappingRecord.do',{
           			mappingDate:$('#mappingDate').val(),
           			'equipment.id':$('#equipment').val(),
           			'deviceSite.id':$('#departmentTg').iTreegrid('getSelected').id,
           			workSheetCode:$('#workSheetCode').val(),
           			helperName:$('#helperRealName').val(),
           			helperId:$('#helperName').val(),
           			usageRate:$('#usageRate').val()
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
<sec:authorize access="hasAuthority('EDIT_EQUIPMENTMAPPINGRECORD')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'departmentEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/equipmentMappingRecord_edit.jsp',
                url:'equipmentMappingRecord/queryEquipmentMappingRecordById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           		var equipmentId = $('#equipmentId').val();
           			if(!equipmentId){
           				$.iMessager.alert('提示','请选择设备');
           				return false;
           			}
           			$.get('equipmentMappingRecord/updateEquipmentMappingRecord.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			mappingDate:$('#mappingDate').val(),
           			'equipment.id':$('#equipmentId').val(),
           			'deviceSite.id':$('#departmentTg').iTreegrid('getSelected').id,
           			workSheetCode:$('#workSheetCode').val(),
           			helperName:$('#helperName').combogrid('getText'),
           			helperId:$('#helperId').val(),
           			usageRate:$('#usageRate').val()
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
<sec:authorize access="hasAuthority('DEL_EQUIPMENTMAPPINGRECORD')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'equipmentMappingRecord/deleteEquipmentMappingRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('RELIEVE_EQUIPMENTMAPPINGRECORD')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-chain-broken',
       url:'equipmentMappingRecord/unbindEquipmentMappingRecord.do',
       grid: {uncheckedMsg:'请先勾选要操作的数据',id:'departmentDg',param:'id:id'}">解除关联</a>
</sec:authorize>
		<form id="searchForm" method="post"
			style="display: inline; float: right;">
			<span> <label for="beginTime">关联时间: </label> <input
				id="beginTime" data-toggle="topjui-datetimebox" type="text"
				name="beginDate" style="width: 190px;"> 至 <input
				id="endTime" data-toggle="topjui-datetimebox" type="text"
				name="endDate" style="width: 190px;">
			</span> <span> <label for="searchChange">过滤字段: </label> <input
				id="searchChange" name="searchChange" type="text"
				style="width: 140px;" data-toggle="topjui-combobox"
				data-options="
				valueField:'id',
				textField:'name',
				data:[{id:'equipment.code',name:'装备序列号'},
					  {id:'equipment.equipmentType.name',name:'装备名称'},
					  {id:'equipment.equipmentType.measurementType',name:'计量类型'},
					  {id:'equipment.equipmentType.measurementObjective',name:'计量目标'},
					  {id:'equipment.cumulation',name:'计量累计'},
					  {id:'equipment.measurementDifference',name:'计量差异'},
					  {id:'bindUsername',name:'关联人员'},
					  {id:'helperName',name:'辅助人员'},
					  {id:'workSheetCode',name:'工单号'}
				]">
			</span> <span> <label for="searchText">值:</label> <input
				data-toggle="topjui-textbox" style="width: 150px;" type="text"
				name="searchText" id="searchText">
			</span> <a id="searchBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">搜索</a>
		</form>
	</div>
	<!-- 部门表格工具栏结束 -->
	<!-- 职位表格工具栏开始 -->
	<div id="position-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'datagrid',
       	id:'departmentDg'
       },
       dialog:{
           id:'positionAddDialog',
            width:600,
           height:400,
           href:'console/jsp/position_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           		if($('#departmentDg').iDatagrid('getSelected')){
           			var positionCode = $('#positionCode').val();
           			if(positionCode==null || ''===$.trim(positionCode)){
           				return false;
           			}
           			
           			var positionName = $('#positionName').val();
           			if(positionName==null || ''===$.trim(positionName)){
           				return false;
           			}
           			$.get('position/addPosition.do',{
           			code:positionCode,
           			name:positionName,
           			'department.id':$('#departmentDg').iDatagrid('getSelected').id,
           			note:$('#positionNote').val()
           			},function(data){
           				if(data.success){
	           				$('#positionAddDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}else{
           			alert('请选择部门');
           			$('#positionAddDialog').iDialog('close');
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#positionAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#position-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'positionEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/position_edit.jsp',
                url:'position/queryPositionById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var positionCode = $('#positionCode_edit').val();
           			var positionName = $('#positionName_edit').val();
           			if(positionName==null || ''===$.trim(positionName)){
           				return false;
           			}
           			$.get('position/updatePosition.do',{
           			id:$('#position').iDatagrid('getSelected').id,
           			code:positionCode,
           			name:positionName,
           			'department.id':$('#departmentDg').iDatagrid('getSelected').id,
           			note:$('#positionNote_edit').val()
           			},function(data){
           				if(data.success){
	           				$('#positionEditDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#positionEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">编辑</a>
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'position/deletePosition.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'id:id'}">删除</a>
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'position/disabledPosition.do',
       grid: {uncheckedMsg:'请选择操作的职位',id:'position',param:'id:id'}">停用</a>
	</div>
</body>
</html>