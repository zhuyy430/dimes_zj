<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../common/jsp/head.jsp" %>
    <link type="text/css" href="console/js/topjui/css/topjui.timeaxis.css" rel="stylesheet">
    <script>
    $(function() {
    	//搜索按钮点击事件
    	$("#searchBtn").click(function(){
    		  $("#departmentDg").iDatagrid("reload",'lostTimeRecord/queryLostTimeRecordByDeviceSiteIdandSearch.do?'+$("#searchForm").serialize());
    	});
    });
    </script>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
        <table
        id="departmentTg"
         data-toggle="topjui-treegrid"
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
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:55%;">
                <!-- datagrid表格 -->
                <table  
                data-toggle="topjui-datagrid"
                       data-options="id:'departmentDg',
                       url:'lostTimeRecord/queryLostTimeRecordByDeviceSiteId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'departmentTg'
                       }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'beginTime',title:'开始时间',width:'180px',align:'center',formatter:function(value,row,index){
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
                                        return dateStr;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'endTime',title:'结束时间',width:'180px',align:'center',formatter:function(value,row,index){
                        		var date;
                        	if(value){
                        		 date = new Date(value);
                        	}else{
                        		date = new Date();
                        	}
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
                        }"></th>
                        <th data-options="field:'sumOfLostTime',title:'损时合计(分钟)',width:'150px',align:'center',formatter:function(value,row,index){
                        	if(value){
                        		value = value*60;
                        		return value.toFixed(2);
                        	}else{
                        		return '';
                        	}
                        	}
                        "></th>
                        <th data-options="field:'recordUserName',title:'记录人',width:'150px',align:'center'"></th>
                        <th data-options="field:'lostTimeTypeName',title:'损时类型',width:'150px',align:'center'"></th>
                        <th data-options="field:'reason',title:'损时原因',width:'150px',align:'center'"></th>
                        <th data-options="field:'description',title:'详细描述',width:'150px',align:'center'"></th>
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
<sec:authorize access="hasAuthority('ADD_LOSTTIMERECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#departmentDg-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	type:'treegrid',
       	id:'departmentTg'
       },
       dialog:{
           id:'departmentAddDialog',
           width:600,
           height:500,
           href:'console/jsp/lostTimeRecord_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			beginTime=$('#beginTime').val();
           			endTime=$('#endTime').val();
					if(endTime<beginTime){
						$.iMessager.alert('提示','开始时间大于结束时间，请重新选择!');
						return false;
					}
           			$.get('lostTimeRecord/addLostTimeRecord.do',{
           			beginTime:$('#beginTime').val(),
           			endTime:$('#endTime').val(),
           			reason:$('#reason').val(),
           			planHalt:$('input[name=planHalt]:checked').val(),
           			lostTimeTypeName:$('#lostTimeTypeName').val(),
           			'deviceSite.id':$('#departmentTg').iTreegrid('getSelected').id,
           			description:$('#description').val()
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
<sec:authorize access="hasAuthority('EDIT_LOSTTIMERECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-pencil',
            dialog: {
            	id:'departmentEditDialog',
                width: 600,
                height: 500,
                href: 'console/jsp/lostTimeRecord_edit.jsp',
                url:'lostTimeRecord/queryLostTimeRecordById.do?id={id}',
                 buttons:[
           	{text:'编辑',handler:function(){
           			beginTime=$('#beginTime').val();
           			endTime=$('#endTime').val();
					if(endTime<beginTime){
						$.iMessager.alert('提示','开始时间大于结束时间，请重新选择!');
						return false;
					}
           		$.get('lostTimeRecord/updateLostTimeRecord.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			beginTime:$('#beginTime').val(),
           			endTime:$('#endTime').val(),
           			reason:$('#reason').val(),
           			planHalt:$('input[name=planHalt]:checked').val(),
           			lostTimeTypeName:$('#lostTimeTypeName').val(),
           			'deviceSite.id':$('#departmentTg').iTreegrid('getSelected').id,
           			description:$('#description').val()
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
<sec:authorize access="hasAuthority('DEL_LOSTTIMERECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'lostTimeRecord/deleteLostTimeRecord.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'},
       onSuccess:function(){$('#departmentTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
             <form id="searchForm" method="post" style="display:inline;float: right;">
			<span> <label for="beginDate">损时开始: </label> <input
				id="beginDate" data-toggle="topjui-datetimebox" type="text"
				name="beginDate" style="width: 190px;"> 至 <input
				id="endDate" data-toggle="topjui-datetimebox" type="text" name="endDate"
				style="width: 190px;">
			</span> 
			<span> <label for="searchChange">选择: </label> <input
				id="searchChange" name="searchChange" type="text" style="width: 140px;"
				data-toggle="topjui-combobox" 
				data-options="
				valueField:'id',
				textField:'name',
				data:[{id:'recordUserName',name:'记录人'},
				{id:'reason',name:'损时原因'},
					  {id:'lostTimeTypeName',name:'损时类型'},
					   {id:'description',name:'详细描述'}
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
<!-- 部门表格工具栏结束 -->
</body>
</html>