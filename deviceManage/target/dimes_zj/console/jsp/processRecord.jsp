<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
</head>
<script>
$(function() {
	//搜索按钮点击事件
	$("#searchBtn").click(function(){
		  $("#departmentDg").iDatagrid("reload",'processRecord/queryProcessRecordByDeviceSiteIdandSearch.do?'+$("#searchForm").serialize());
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
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <!-- datagrid表格 -->
                <table   id="departmentDg"
                data-toggle="topjui-datagrid"
                       data-options="id:'departmentDg',
                       url:'processRecord/queryProcessRecordByDeviceSiteId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'departmentTg',
                       },
			           ">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
								<th
									data-options="field:'collectionDate',title:'生产时间',width:'220px',align:'center',formatter:function(value,row,index){
                        	if(value){
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
                                        	//var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr;			
                                        return dateStr;
                        	}else{
                        		return '';
                        	}
                        }"></th>
								<th data-options="field:'productNum',title:'生产序号',width:'260px',align:'center',sortable:false"></th>
								<th data-options="field:'opcNo',title:'二维码',width:'260px',align:'center',sortable:false"></th>
								<th data-options="field:'no',title:'工单单号',width:'130px',align:'center',sortable:true"></th>
								<th data-options="field:'workPieceCode',width:'120px',align:'center',title:'工件代码'"></th>
								<th data-options="field:'workPieceName',width:'100px',align:'center',title:'工件名称'"></th>
								<th data-options="field:'unitType',width:'100px',align:'center',title:'规格型号'"></th>
								<th data-options="field:'graphNumber',width:'100px',align:'center',title:'图号'"></th>
								<th data-options="field:'version',width:'100px',align:'center',title:'版本号'"></th>
								<th data-options="field:'realBeat',width:'100px',align:'center',title:'即时节拍'"></th>
								<th data-options="field:'shortHalt',width:'100px',align:'center',title:'短停机'"></th>
								<th data-options="field:'processCode',width:'100px',align:'center',title:'工序代码'"></th>
								<th data-options="field:'processName',width:'100px',align:'center',title:'工序名称'"></th>
								<th data-options="field:'batchNumber',width:'100px',align:'center',title:'批号'"></th>
								<th data-options="field:'stoveNumber',width:'100px',align:'center',title:'材料编号'"></th>
								<th data-options="field:'status',width:'100px',align:'center',title:'状态'"></th>
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
	<sec:authorize access="hasAuthority('ADD_PROCESSRECORD')">
			<a href="javascript:void(0)" data-toggle="topjui-menubutton"
				data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'treegrid',
       	id:'departmentTg',
       	param:'deviceSiteId:id'
       },
       dialog:{
           id:'departmentAddDialog',
           width:700,
           height:750,
           href:'console/jsp/processRecord_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var collectionDate = $('#collectionDate').val();
           			if(!collectionDate){
           				$.iMessager.alert('提示','请选择生产时间!');
           				$('#collectionDate').focus();
           				return false;
           			}
           	
           			var serialNo = $('#serialNo').val();
           			if(serialNo==null || ''===$.trim(serialNo)){
           				$.iMessager.alert('提示','请输入生产序号!');
           				$('#serialNo').focus();
           				return false;
           			}
           			
           			
           			var workPieceCode = $('#workPieceCode').val();
           			if(!workPieceCode){
           				$.iMessager.alert('提示','请输入工件代码!');
           				$('#workPieceCode').focus();
           				return false;
           			}
           			
           			var processCode = $('#processCode').val();
           			if(!processCode){
           				$.iMessager.alert('提示','请输入工序代码!');
           				$('#processCode').focus();
           				return false;
           			}
           			$.get('processRecord/addProcessRecord.do',{
           			collectionDate:$('#collectionDate').val(),
           			productNum:$('#serialNo').val(),
           			no:$('#no').iCombogrid('getValue'),
           			workSheetId:$('#workSheetId').val(),
           			workPieceCode:$('#workPieceCode').val(),
           			workPieceName:$('#workPieceName').val(),
           			unitType:$('#unitType').val(),
           			opcNo:$('#opcNo').val(),
           			graphNumber:$('#graphNumber').val(),
           			version:$('#version').val(),
           			processId:$('#processId').val(),
           			processCode:$('#processCode').val(),
           			processName:$('#processName').val(),
           			batchNumber:$('#batchNumber').val(),
           			'deviceSiteId':$('#departmentTg').iTreegrid('getSelected').id,
           			deviceSiteCode:$('#departmentTg').iTreegrid('getSelected').code,
           			deviceSiteName:$('#departmentTg').iTreegrid('getSelected').name,
           			stoveNumber:$('#stoveNumber').val(),
           			status:$('#status').val(),
           			realBeat:$('#realBeat').val()
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
        <sec:authorize access="hasAuthority('EDIT_PROCESSRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'departmentEditDialog',
                width: 700,
                height: 750,
                href: 'console/jsp/processRecord_edit.jsp',
                url:'processRecord/queryProcessRecordById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           		var collectionDate = $('#collectionDate').val();
           			if(!collectionDate){
           				$.iMessager.alert('提示','请选择生产时间!');
           				$('#collectionDate').focus();
           				return false;
           			}
           	
           			var serialNo = $('#productNum').val();
           			if(serialNo==null || ''===$.trim(serialNo)){
           				$.iMessager.alert('提示','请输入生产序号!');
           				$('#serialNo').focus();
           				return false;
           			}
           			
           			
           			var workPieceCode = $('#workPieceCode').val();
           			if(!workPieceCode){
           				$.iMessager.alert('提示','请输入工件代码!');
           				$('#workPieceCode').focus();
           				return false;
           			}
           			
           			var processCode = $('#processCode').val();
           			if(!processCode){
           				$.iMessager.alert('提示','请输入工序代码!');
           				$('#processCode').focus();
           				return false;
           			}
           			$.get('processRecord/updateProcessRecord.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			collectionDate:$('#collectionDate').val(),
           			productNum:$('#productNum').val(),
           			no:$('#no').iCombogrid('getValue'),
           			workSheetId:$('#workSheetId').val(),
           			workPieceCode:$('#workPieceCode').val(),
           			workPieceName:$('#workPieceName').val(),
           			opcNo:$('#opcNo').val(),
           			unitType:$('#unitType').val(),
           			graphNumber:$('#graphNumber').val(),
           			version:$('#version').val(),
           			processId:$('#processId').val(),
           			processCode:$('#processCode').val(),
           			processName:$('#processName').val(),
           			batchNumber:$('#batchNumber').val(),
           			'deviceSiteId':$('#departmentDg').iDatagrid('getSelected').deviceSiteId,
           			deviceSiteCode:$('#departmentDg').iDatagrid('getSelected').deviceSiteCode,
           			deviceSiteName:$('#departmentDg').iDatagrid('getSelected').deviceSiteName,
           			stoveNumber:$('#stoveNumber').val(),
           			status:$('#status').val(),
           			realBeat:$('#realBeat').val()
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
            <sec:authorize access="hasAuthority('DEL_PROCESSRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'processRecord/deleteProcessRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}
       ">删除</a>
         </sec:authorize>
        <form id="searchForm" method="post" style="display:inline;float: right;">
			<span> <label for="beginTime">生产时间: </label> <input
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
				data:[{id:'realBeat',name:'即时节拍'},
					  {id:'no',name:'工单单号'},
					  {id:'serialNo',name:'生产序号'},
					  {id:'workPieceCode',name:'工件代码'},
					  {id:'workPieceName',name:'工件名称'},
					  {id:'unitType',name:'规格型号'},
					  {id:'graphNumber',name:'图号'},
					  {id:'version',name:'版本号'},
					  {id:'shortHalt',name:'短停机'},
					  {id:'processCode',name:'工序代码'},
					  {id:'processName',name:'工序名称'},
					  {id:'batchNumber',name:'批号'},
					  {id:'stoveNumber',name:'材料编号'}
				]"
				>
			</span> 
			<span> <label for="searchText">输入:</label> <input data-toggle="topjui-textbox" style="width: 150px;"
				type="text" name="searchText" id="searchText">
			</span>
			 <a id="searchBtn" href="javascript:void(0)"
				data-toggle="topjui-menubutton"
				data-options="iconCls:'fa fa-search',plain:false"
				style="text-align: center; padding: 3px auto; width: 100px;">搜索</a>
		</form>
    </div>
<!-- 部门表格工具栏结束 -->
<!-- 职位表格工具栏开始 -->
<div id="position-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
 <sec:authorize access="hasAuthority('ADDPARAMETER_PROCESSRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
        parentGrid:{
       	type:'datagrid',
       	id:'departmentDg',
       	param:'workPieceCode:workPieceCode,processCode:processCode'
       },
       dialog:{
           id:'positionAddDialog',
            width:600,
           height:600,
           href:'console/jsp/processParameterRecord_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			/*var parameterCode = $('#parameterCode').val();
           			if(parameterCode==null || ''===$.trim(parameterCode)){
           				return false;
           			}*/
           			$.get('processParameterRecord/addProcessParameterRecord.do',{
           			'processRecord.id':$('#departmentDg').iDatagrid('getSelected').id,
           			parameterCode:$('#parameterCode').val(),
           			parameterName:$('#parameterName').val(),
           			upLine:$('#upLine').val(),
           			lowLine:$('#lowLine').val(),
           			standardValue:$('#standardValue').val(),
           			currentDate:$('#currentDate').val(),
           			parameterValue:$('#parameterValue').iTextbox('getText'),
           			/*status:$('#status').val(),*/
           			statusCode:$('#statusCode').val()
           			},function(data){
           				if(data.success){
	           				$('#positionAddDialog').iDialog('close');
	           				$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#positionAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
       </sec:authorize>
        <sec:authorize access="hasAuthority('EDITPARAMETER_PROCESSRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#position-toolbar',
            iconCls: 'fa fa-pencil',
		    grid:{
		       	type:'datagrid',
		       	id:'position'
		       },
            dialog: {
            	id:'positionEditDialog',
                width: 600,
                height: 700,
                href: 'console/jsp/processParameterRecord_edit.jsp',
                url:'processParameterRecord/queryProcessParameterRecordById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			$.get('processParameterRecord/updateProcessParameterRecord.do',{
           			id:$('#position').iDatagrid('getSelected').id,
           			'processRecord.id':$('#departmentDg').iDatagrid('getSelected').id,
           			parameterCode:$('#parameterCode').val(),
           			parameterName:$('#parameterName').val(),
           			upLine:$('#upLine').val(),
           			lowLine:$('#lowLine').val(),
           			standardValue:$('#standardValue').val(),
           			currentDate:$('#currentDate').val(),
           			parameterValue:$('#parameterValue').val(),
           			statusCode:$('#statusCode').val()
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
            </sec:authorize>
             <sec:authorize access="hasAuthority('DELPARAMETER_PROCESSRECORD')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'processParameterRecord/deleteProcessParameterRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'id:id'}">删除</a>
       </sec:authorize>
    </div>
<!-- 职位表格工具栏结束 -->
<!-- 相关文档表格工具栏开始 -->
<div id="relateDoc-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'relateDoc'
       }">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
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
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
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
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
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
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'search',
       extend: '#relateDoc-toolbar'">停用</a>

	</div>

</body>
</html>


