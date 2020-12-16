<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
 <script>
 $(function() {
		//搜索按钮点击事件
		$("#searchBtn").click(function(){
			  $("#departmentDg").iDatagrid("reload",'ngRecord/queryNGRecordByDeviceSiteIdandSearch.do?'+$("#searchForm").serialize());
		});
	});
    </script>
</head>
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
						<th data-options="field:'name',width:'100%',title:'设备'"></th>
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
                       url:'ngRecord/queryNGRecordByDeviceSiteId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'departmentTg',
                       },
			           childTab: [{id:'southTabs'}],onLoadSuccess:function(){
					           		$('#position').iDatagrid('reload',{ngRecordId:''});
					           }">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>

								<th
									data-options="field:'occurDate',title:'产生时间',width:'120px',formatter:function(value,row,index){
										if(value){
											return getDateTime(new Date(value));
										}else{
											return '';
										}
									}"></th>
								<th
									data-options="field:'no',title:'工单号',width:'120px'"></th>
								<th
									data-options="field:'workpieceCode',title:'工件代码',width:'120px'"></th>
								<th	data-options="field:'workpieceName',title:'工件名称',width:'120px'"></th>
								<th
									data-options="field:'processCode',title:'工序代码',width:'120px'"></th>
								<th	data-options="field:'processName',title:'工序名称',width:'120px'"></th>
								<th
									data-options="field:'unitType',title:'规格型号',width:'120px'"></th>
								<th
									data-options="field:'graphNumber',title:'图号',width:'120px'"></th>
								<th data-options="field:'version',title:'版本号',width:'120px'"></th>
								<th data-options="field:'batchNumber',title:'批号',width:'120px'"></th>
								<th data-options="field:'stoveNumber',title:'材料编号',width:'120px'"></th>
								<th data-options="field:'ngCount',title:'数量',width:'120px'"></th>
								<th
									data-options="field:'category',title:'原因类别',width:'120px'"></th>
								<th
									data-options="field:'ngReason',title:'原因',width:'120px'"></th>
								<th
									data-options="field:'processingMethod',title:'处理方法',width:'120px',formatter:function(value,row,index){
		                         if (value) {
		                                       switch(value){
		                                       	case 'scrap':return '报废';
		                                       	case 'repair':return '返修';
		                                       	case 'compromise':return '让步接收';
		                                       }
		                                    }else{
		                                    	return '';
		                                    }
		                        }"></th>
								<th data-options="field:'inputUsername',title:'录入人',width:'120px'"></th>
																<th
									data-options="field:'inputDate',title:'录入时间',width:'120px',align:'center',formatter:function(value,row,index){
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
                            <th data-options="field:'inWarehouse',title:'是否入库',width:'60px',formatter:function(value,row,index){
                                if(value){
                                    return '是';
                                }else{
                                    return '否';
                                }
                            }"></th>
                            <th data-options="field:'warehouseDate',title:'入库时间',width:'120px',formatter:function(value,row,index){
                                if(value){
                                    return getDateTime(new Date(value));
                                }else{
                                    return '';
                                }
                            }"></th>
                            <th data-options="field:'inWarehouseUserName',title:'入库人员',width:'120px'"></th>
                            <th data-options="field:'inWarehouseName',title:'入库仓库',width:'120px'"></th>
							</tr>
						</thead>
					</table>
				</div>
				<div data-options="region:'south',fit:false,split:true,border:false"
					style="height: 40%">
					<div data-toggle="topjui-tabs"
						data-options="id:'southTabs',
                     fit:true,
                     border:false,
                     parentGrid:{
                         type:'datagrid',
                         id:'departmentDg',
                         param:'ngRecordId:id'
                     }">
						<div title="不合格详情" data-options="id:'tab0',iconCls:'fa fa-th'">
                        <table 
                         data-toggle="topjui-datagrid"
                               data-options="id:'position',
                               initCreate: false,
                               fitColumns:true,
                               singleSelect:true,
						       url:'ngProcessMethod/queryNGProcessMethodsByNGRecordId.do'">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',hidden:true"></th>
                                <th data-options="field:'processMethod',title:'处理方法',sortable:false,width:'200px',formatter:function(value,row,index){
		                         if (value) {
		                                       switch(value){
		                                       	case 'scrap':return '报废';
		                                       	case 'repair':return '返修';
		                                       	case 'compromise':return '让步接收';
		                                       }
		                                    }else{
		                                    	return '';
		                                    }
		                        }"></th>
                                <th data-options="field:'ngCount',title:'数量',sortable:false,width:'200px'"></th>
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
<sec:authorize access="hasAuthority('ADD_NGRECORD')">
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
           width:700,
           height:800,
           href:'console/jsp/ngRecord_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           	var occurdate = $('#occurDate').val();
           	if(!occurdate){
           		alert('时间不能为空!');
           		return false;
           	}
           	var ngCount = $('#ngCount').val();
           	if(!ngCount || ngCount<=0){
           		alert('数量值必须大于0!');
           		return false;
           	}
           			$.get('ngRecord/addNGRecord.do',{
           			occurDate:occurdate,
           			no:$('#no').val(),
           			'deviceSiteId':$('#departmentTg').iTreegrid('getSelected').id,
           			'deviceSiteName':$('#departmentTg').iTreegrid('getSelected').name,
           			workpieceCode:$('#workpieceCode').val(),
           			workpieceName:$('#workpieceName').val(),
           			processId:$('#processId').val(),
           			processCode:$('#processCode').val(),
           			processName:$('#processName').val(),
           			unitType:$('#unitType').val(),
           			customerGraphNumber:$('#customerGraphNumber').val(),
           			graphNumber:$('#graphNumber').val(),
           			version:$('#version').val(),
           			batchNumber:$('#batchNumber').val(),
           			stoveNumber:$('#stoveNumber').val(),
           			ngTypeId:$('#ngTypeId').val(),
           			ngTypeName:$('#ngTypeName').val(),
           			ngReason:$('#ngReason').val(),
           			ngReasonId:$('#ngReasonId').val(),
           			processingMethod:$('#processingMethod').val(),
           			ngCount:ngCount
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
<sec:authorize access="hasAuthority('EDIT_NGRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'departmentEditDialog',
                width:700,
                height: 800,
                href: 'console/jsp/ngRecord_edit.jsp',
                url:'ngRecord/queryNGRecordById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			$.get('ngRecord/updateNGRecord.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			occurDate:$('#occurDate').val(),
           			no:$('#no').val(),
           			'deviceSiteId':$('#departmentDg').iDatagrid('getSelected').deviceSiteId,
           			'deviceSiteCode':$('#departmentDg').iDatagrid('getSelected').deviceSiteCode,
           			'deviceSiteName':$('#departmentDg').iDatagrid('getSelected').deviceSiteName,
           			workpieceCode:$('#workpieceCode').val(),
           			workpieceName:$('#workpieceName').val(),
           			processId:$('#processId').val(),
           			processCode:$('#processCode').val(),
           			processName:$('#processName').val(),
           			unitType:$('#unitType').val(),
           			customerGraphNumber:$('#customerGraphNumber').val(),
           			graphNumber:$('#graphNumber').val(),
           			version:$('#version').val(),
           			batchNumber:$('#batchNumber').val(),
           			stoveNumber:$('#stoveNumber').val(),
           			ngTypeId:$('#ngTypeId').val(),
           			ngTypeName:$('#ngTypeName').val(),
           			ngReason:$('#ngReason').val(),
           			ngReasonId:$('#ngReasonId').val(),
           			processingMethod:$('#processingMethod').val(),
           			ngCount:$('#ngCount').val()
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
<sec:authorize access="hasAuthority('DEL_NGRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'ngRecord/deleteNGRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('INWAREHOUSE_NGRECORD')"><!-- INWAREHOUSE_NGRECORD -->
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		   data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-sign-out',
       dialog:{
           id:'equipmentMappingEditDialog',
            width:600,
           height:450,
           href:'console/jsp/ngRecord_inWarehouse.jsp',
           url:'{id}',
           buttons:[
           	{text:'保存',handler:function(){
           		var warehouse = $('#warehouseTable').iDatagrid('getSelected');
           		if(warehouse==null||warehouse==''){
           			$.iMessager.alert('提示','请选择入库仓库!');
           		}else{
           		var warehouseCode = warehouse.cWhCode;
					$.get('ngRecord/inWarehouseByNGRecord.do',{
           				id:$('#departmentDg').iDatagrid('getSelected').id,
           				warehouseCode:warehouseCode
           			},function(data){
           				if(data.success){
           					$('#equipmentMapping').iDatagrid('reload');
           					$('#equipmentMappingEditDialog').iDialog('close');
           				}else{
           					$('#equipmentMappingEditDialog').iDialog('close');
           					$.iMessager.alert('提示',data.msg);

           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#equipmentMappingEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">入库</a>
</sec:authorize>
             <form id="searchForm" method="post" style="display:inline;float: right;">
			<span> <label for="beginTime">产生时间: </label> <input
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
				data:[{id:'unitType',name:'规格型号'},
					  {id:'processCode',name:'工序代码'},
					  {id:'no',name:'工单号'},
					  {id:'workpieceCode',name:'工件代码'},
					  {id:'workpieceName',name:'工件名称'},
					  {id:'processName',name:'工序名称'},
					  {id:'customerGraphNumber',name:'客户 图号'},
					  {id:'graphNumber',name:'图号'},
					  {id:'version',name:'版本号'},
					  {id:'batchNumber',name:'批号'},
					  {id:'stoveNumber',name:'材料编号'},
					  {id:'ngCount',name:'数量'},
					  {id:'ngTypeName',name:'原因类别'},
					  {id:'ngReason',name:'原因'},
					  {id:'processingMethod',name:'处理方法'},
					  {id:'inputUsername',name:'录入人'},
					  {id:'auditorName',name:'审核人'},
					  {id:'reviewerName',name:'复核人'},
					  {id:'confirmUsername',name:'确认人'}
				]"
				>
			</span> 
			<span> <label for="searchText">输入:</label> <input data-toggle="topjui-textbox" style="width: 150px;"
				type="text" name="searchText" id="searchText">
			</span>
            <label><input name="inStatus" type="radio" value="true" />已入库 </label>
            <label><input name="inStatus" type="radio" value="false" />未入库 </label>
			 <a id="searchBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">搜索</a>
		</form>
	</div>
	<div id="position-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#position-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'positionEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/ngProcessMethod_edit.jsp',
                url:'ngProcessMethod/queryNGProcessMethodById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			var ngCount = $('#ngCount').val();
           			$.get('ngProcessMethod/updateNGProcessMethod.do',{
           			id:$('#position').iDatagrid('getSelected').id,
           			ngCount:ngCount
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
	</div>
</body>
</html>