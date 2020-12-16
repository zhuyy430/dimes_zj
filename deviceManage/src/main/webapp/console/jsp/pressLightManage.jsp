<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="../../common/jsp/head.jsp" %>
    <link type="text/css" href="console/js/topjui/css/topjui.timeaxis.css" rel="stylesheet">
     <script>
    	function timeAxis(id){
    		 $.get("workflow/queryWorkflow.do",{businessKey:id,type:'pressLight'},function(data){
    			var $div = $("<div  class='topjui-timeaxis-container' style='height:100%;width:100%;' data-toggle='topjui-timeaxis'>");
    			 var list = JSON.stringify(data.data);
    			//去除key中的单引号
    			list = list.replace(/"(\w+)":/g, "$1:");
    			var reg = new RegExp("\"","g");//g,表示全部替换。
    			var l = list.replace(reg,"'");
    			 $div.attr("data-options","title:'按灯流程',list:" + l);
    			 
    			 var $wrapper=$("<div class='wrapper'>");
    			 $div.append($wrapper);
    			 var $light = "<div class='light'><i></i></div>";
    			 $wrapper.append($light);
    			 
    			 var $topjui_timeaxis_main = $("<div class='topjui-timeaxis-main'>");
    			 $wrapper.append($topjui_timeaxis_main);
    			 
    			 var $title = $("<h1 class='title'>");
    			 $topjui_timeaxis_main.append($title);
    			 $title.text("按灯流程");
    			 for(var i = 0;i<data.data.length;i++){
    				 var $year = $("<div class='year year" + i +"'>");
    				 $topjui_timeaxis_main.append($year);
    				 
    				 var $yearValue = $("<h2>");
    				 $year.append($yearValue);
    				 
    				 $yearValue.append("<a>" + data.data[i].year + "<i></i></a>");
    				 
    				 var $list = $("<div class='list' style='height:116px;'>");
    				 $year.append($list);
    				 for(var j = 0;j<data.data[i].list.length;j++){
    					 var obj = data.data[i].list[j];
    					 var $ul = $("<ul class='ul"+j+"' style='position:absolute;'>");
    					 $list.append($ul);
    					 
    					 var $li = $("<li class='cls'>");
    					 $ul.append($li);
    					 
    					 var $date = $("<p class='date'>"+obj.date+"</p>");
    					 $li.append($date);
    					 var $intro = $("<p class='intro'>" + obj.intro + "</p>");
    					 $li.append($intro);
    					 var $version = $("<p class='version'>" + obj.version + "</p>");
    					 $li.append($version);
    					 var $more = $("<div class='more more00'>")
    					 $li.append($more);
    					 $more.append("<p>"+obj.more[0]?obj.more:'' + "</p>");
    				 }
    			 }
    			var tab1 = $("#tab1"); 
    			tab1.html($div);
    		}); 
    	}
    	/**动态改变按钮动作*/
    	function handleButtons(row){
    		var $confirmBtn = $("#confirmBtn");
    		var $lightoutBtn = $("#lightoutBtn");
    		var $recoverBtn = $("#recoverBtn");
    		
    		if(row.lightOutTime){
    			$lightoutBtn.iMenubutton("disable");
    		}else{
    			$lightoutBtn.iMenubutton("enable");
    			
    		}
    		
    		if(!row.recoverTime && row.lightOutTime){
    			$recoverBtn.iMenubutton("enable");
    		}else{
    			$recoverBtn.iMenubutton("disable");
    		}
    		
    		if(!row.confirmTime && row.recoverTime){
    			$confirmBtn.iMenubutton("enable");
    		}else{
    			$confirmBtn.iMenubutton("disable");
    		}
    	}
    </script>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
        <table
        id="departmentTg"
         data-toggle="topjui-treegrid"
               data-options="id:'departmentTg',
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   data:[{'id':0,'name':'操作分类','children':[{'id':1,'name':'待熄灯'},{'id':2,'name':'待恢复'},{'id':3,'name':'待确认'},{'id':4,'name':'已完成'}]}],
			   childGrid:{
			   	   param:'filterId:id',
                   grid:[
                       {type:'datagrid',id:'departmentDg'},
                   ]
			   }">
            <thead>
            <tr>
               <!--  <th data-options="field:'id',title:'id',checkbox:true"></th> -->
                <th data-options="field:'name',width:'100%',title:'分类'"></th>
            </tr>
            </thead>
        </table>
    </div>
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <!-- datagrid表格 -->
                <table  
                data-toggle="topjui-datagrid"
                       data-options="id:'departmentDg',
                       url:'pressLightRecord/queryPressLightRecordByFilterId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'departmentTg',
                       },
			           onSelect:function(index,row){
			           	 	timeAxis(row.id);
			           	 	handleButtons(row);
			           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'pressLightTime',title:'按灯时间',width:'180px',align:'center',formatter:function(value,row,index){
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
                        <th data-options="field:'pressLightTypeName',title:'故障类别',sortable:false"></th>
                        <th data-options="field:'deviceSiteCode',title:'站点代码',sortable:false,formatter:function(value,row,index){
                        	if(row.deviceSite){
                        		return row.deviceSite.code;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'deviceSiteName',title:'站点名称',sortable:false,formatter:function(value,row,index){
                        	if(row.deviceSite){
                        		return row.deviceSite.name;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <th data-options="field:'halt',title:'是否停机',sortable:false,formatter:function(value,row,index){
                        	if(value){
                        		return 'Y';
                        	}else{
                        		return 'N';
                        	}
                        }"></th>
                        <th data-options="field:'pressLightUserName',title:'按灯人员',sortable:false"></th>
                        <th data-options="field:'lightOutUserName',title:'熄灯人员',sortable:false"></th>
                        <th data-options="field:'lightOutTime',title:'熄灯时间',sortable:false,formatter:function(value,row,index){
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
                        <th data-options="field:'recovered',title:'是否恢复',sortable:false,formatter:function(value,row,index){
                        	if(value){
                        		return 'Y';
                        	}else{
                        		return 'N';
                        	}
                        }"></th>
                        <th data-options="field:'recoverUserName',title:'恢复人员',sortable:false"></th>
                        <th data-options="field:'recoverTime',title:'恢复时间',sortable:false,formatter:function(value,row,index){
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
                        <th data-options="field:'confirmUserName',title:'确认人员',sortable:false"></th>
                        <th data-options="field:'confirmTime',title:'确认时间',sortable:false,formatter:function(value,row,index){
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
                        <th data-options="field:'reason',title:'故障原因',sortable:false"></th>
                        <th data-options="field:'recoverMethod',title:'恢复方法',sortable:false"></th>
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
                     singleSelect:true,
                     parentGrid:{
                         type:'datagrid',
                         id:'departmentDg',
                         param:'id:id'
                     }">
                    <div title="流程时间轴" data-options="iconCls:'fa fa-th'" id="tab1">
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
       <sec:authorize access="hasAuthority('LIGHTOUT_PRESSLIGHTMGR')">
       <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-lightbulb-o',
            dialog: {
            	id:'lightoutDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/pressLightRecord_workflow.jsp',
                 buttons:[
           	{text:'熄灯',handler:function(){
           		$.get('pressLightRecord/lightOutPressLightRecord.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			suggestion:$('#suggestion').val()
           			},function(data){
           				if(data.success){
	           				$('#lightoutDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'取消',handler:function(){
           		$('#lightoutDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }" id="lightoutBtn">熄灯</a>
            </sec:authorize>
             <sec:authorize access="hasAuthority('RECOVER_PRESSLIGHTMGR')">
       <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-reply-all',
            dialog: {
            	id:'recoverDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/pressLightRecord_workflow.jsp',
                 buttons:[
           	{text:'恢复',handler:function(){
           		$.get('pressLightRecord/recoverPressLightRecord.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			suggestion:$('#suggestion').val()
           			},function(data){
           				if(data.success){
	           				$('#recoverDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'取消',handler:function(){
           		$('#recoverDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }" id="recoverBtn">恢复</a>
        </sec:authorize>
         <sec:authorize access="hasAuthority('CONFIRM_PRESSLIGHTMGR')">
       <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#departmentDg-toolbar',
            iconCls: 'fa fa-check',
            dialog: {
            	id:'confirmDialog',
                width: 600,
                height: 400,
                href: 'console/jsp/pressLightRecord_workflow.jsp',
                 buttons:[
           	{text:'熄灯',handler:function(){
           		$.get('pressLightRecord/confirmPressLightRecord.do',{
           			id:$('#departmentDg').iDatagrid('getSelected').id,
           			suggestion:$('#suggestion').val()
           			},function(data){
           				if(data.success){
	           				$('#confirmDialog').iDialog('close');
	           				$('#departmentDg').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'取消',handler:function(){
           		$('#confirmDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }" id="confirmBtn">确认</a>
            </sec:authorize>
    </div>
</body>
</html>