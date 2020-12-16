<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
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
			   url:'processes/queryProcessTree.do',
			   childGrid:{
			   	   param:'processId:id',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
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
		<div
			data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
			<div data-toggle="topjui-layout" data-options="fit:true">
				<div
					data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
					style="height: 60%;">
					<!-- datagrid表格 -->
					<table data-toggle="topjui-datagrid"
						data-options="id:'departmentDg',
                       url:'skill/querySkillsByProcessId.do',
                       singleSelect:true,
                       fitColumns:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'departmentTg'
                       },
			           childTab: [{id:'southTabs'}],
			           onSelect:function(index,row){
					           		switchButton('skillSwitchBtn',row.disabled);
					           },onLoadSuccess:function(){
					           		$('#position').iDatagrid('reload',{skillId:''});
					           }">
						<thead>
							<tr>
								<th
									data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
								<th
									data-options="field:'skillType',title:'技能类别',width:'180px',align:'center',formatter:function(value,row,index){
                            if (row.skillType) {
                                return row.skillType.name;
                            } else {
                                return '';
                            }
                        }"></th>
								<th
									data-options="field:'code',title:'技能代码',width:'180px',align:'center'"></th>
								<th data-options="field:'name',title:'技能名称',width:'180px',align:'center'"></th>
								<th data-options="field:'note',title:'备注',width:'180px',align:'center'"></th>
								<th
									data-options="field:'disabled',title:'停用',width:'180px',align:'center',
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
					style="height: 40%">
					<div data-toggle="topjui-tabs"
						data-options="id:'southTabs',
                     fit:true,
                     border:false,
                     parentGrid:{
                         type:'datagrid',
                         id:'departmentDg',
                         param:'skillId:id'
                     }">
						<div title="技能等级" data-options="id:'tab0',iconCls:'fa fa-th'">
							<!-- datagrid表格 -->
							<table data-toggle="topjui-datagrid"
								data-options="id:'position',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'processSkillLevel/queryProcessSkillLevelsBySkillId.do'">
								<thead>
									<tr>
										<th data-options="field:'id',title:'id',hidden:true"></th>
										<th data-options="field:'code',title:'等级代码',width:'180px',align:'center'"></th>
										<th
											data-options="field:'name',title:'等级名称',width:'180px',align:'center'"></th>
										<th
											data-options="field:'note',title:'备注',width:'180px',align:'center'"></th>
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
       <sec:authorize access="hasAuthority('ADD_EMPLOYEESKILL')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'treegrid',
       	id:'departmentTg'
       },
       dialog:{
           id:'skillAddDialog',
           width:600,
           height:600,
           href:'console/jsp/skill_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var processTreegrid = $('#departmentTg').iTreegrid('getSelected');
           			if(!processTreegrid){
           				$.iMessager.alert('提示','请选择工序');
           				$('#skillAddDialog').iDialog('close');
           				return false;
           			}
           			var processId = processTreegrid.id;
           			var code = $('#code').val();
           			if(code==null || ''===$.trim(code)){
           				$.iMessager.alert('提示','请输入人员技能代码');
           				return false;
           			}
           			
           			var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入人员技能名称');
           				return false;
           			}
           			$.get('skill/addSkill.do',{
           			code:code,
           			name:name,
           			'process.id':processId,
           			'skillType.name':$('#skillTypeName').iCombobox('getText'),
           			note:$('#note').val()
           			},function(data){
           				if(data.success){
	           				$('#skillAddDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#skillAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]}">新增</a>
            </sec:authorize>
           <sec:authorize access="hasAuthority('EDIT_EMPLOYEESKILL')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/skill_edit.jsp',
                url:'skill/querySkillById.do?id={id}',
                 buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           				var name = $('#name').val();
           			if(name==null || ''===$.trim(name)){
           				$.iMessager.alert('提示','请输入人员技能名称');
           				return false;
           			}
           			var skillTypeName = $('#skillTypeName').iCombobox('getText');
           			$.get('skill/updateSkill.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			'process.id':$('#departmentDg').iDatagrid('getSelected').process.id,
           			code:code,
           			name:name,
           			'skillType.name':skillTypeName,
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
            <sec:authorize access="hasAuthority('DEL_EMPLOYEESKILL')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'skill/deleteSkill.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentTg').iTreegrid('reload');}">删除</a>
        </sec:authorize>
       <sec:authorize access="hasAuthority('DISABLE_EMPLOYEESKILL')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-stop',
       url:'skill/disabledSkill.do',
       grid: {uncheckedMsg:'请选择操作的数据',id:'departmentDg',param:'id:id'}" id="skillSwitchBtn">停用</a>
        </sec:authorize>
	</div>
	<!-- 部门表格工具栏结束 -->
	<!-- 职位表格工具栏开始 -->
	<div id="position-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
       <sec:authorize access="hasAuthority('ADDSKILLLEVEL_EMPLOYEESKILL')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	id:'departmentDg',
       	type:'datagrid',
       	param:'skillId:id'
       },
       dialog:{
           id:'deviceAddDialog',
            width:620,
           height:500,
           href:'console/jsp/skill_skillLevel_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var code = $('#code').val();
           			var name = $('#name').val();
           			if(!code){
           				$.iMessager.alert('提示','请输入技能等级代码');
           				return false;
           			}
           			if(!name){
           				$.iMessager.alert('提示','请输入技能等级名称');
           				return false;
           			}
           			$.get('processSkillLevel/addProcessSkillLevel.do',{
           				'skill.id':$('#departmentDg').iDatagrid('getSelected').id,
           				code:code,
           				name:name,
           				note:$('#note').val()
           			},function(data){
           				if(data.success){
           					$('#deviceAddDialog').iDialog('close');
           					$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
        </sec:authorize>
       <sec:authorize access="hasAuthority('EDITSKILLLEVEL_EMPLOYEESKILL')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-edit',
       dialog:{
           id:'deviceEditDialog',
            width:620,
           height:500,
           href:'console/jsp/skill_skillLevel_edit.jsp',
           url:'processSkillLevel/queryProcessSkillLevelById.do?id={id}',
           buttons:[
           	{text:'编辑',handler:function(){
           			var name = $('#name').val();
           			if(!name){
           				$.iMessager.alert('提示','请输入技能等级名称');
           				return false;
           			}
           			$.get('processSkillLevel/updateProcessSkillLevel.do',{
           				id:$('#position').iDatagrid('getSelected').id,
           				name:name,
           				note:$('#note').val()
           			},function(data){
           				if(data.success){
           					$('#deviceEditDialog').iDialog('close');
           					$('#position').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">编辑</a>
        </sec:authorize>
       <sec:authorize access="hasAuthority('DELSKILLLEVEL_EMPLOYEESKILL')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'processSkillLevel/deleteProcessSkillLevel.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'id:id'}">删除</a>
        </sec:authorize>
	</div>
	<!-- 职位表格工具栏结束 -->
</body>
</html>