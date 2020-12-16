<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript">
	//新增工单
	function addWorkSheet() {
	    var productionUnit = $("#productionUnitTg").iTreegrid("getSelected");
	    if(!productionUnit){
	        alert("请选择生产单元!");
	        return false;
		}
        $.get("workSheet/clearSession.do",function(result){
        	addParentTab({title:'新增/查看工单',href:'console/jsp/workSheet_add.jsp?productionUnitId=' + productionUnit.id});
        })
    }


	//根据条件查询报工单详情
	function queryWorksheets(){

		$("#productionUnitDg").iDatagrid("load",{
			from:$("#from").val(),
			to:$("#to").val(),
			batchNo:$("#batchNoSearch").val(),
			inventoryCode:$("#inventoryCodeSearch").val(),
			no:$("#noSearch").val(),
			status:$("#statusSearch").val()
		})
	}



    function SeeWorkSheet(){
        var detail = $("#productionUnitDg").iDatagrid("getSelected");
        if(!detail){
            alert("请选择要查看的记录!");
            return false;
        }
        $.get("workSheet/clearSession.do",function(result){
       		addParentTab({title:'新增/查看工单',href:'console/jsp/workSheet_add.jsp?WorkSheetNo='+ detail.no});
        });
	}
	//换线
    function changeProductionUnit(){
        var detail = $("#productionUnitDg").iDatagrid("getSelected");
        if(!detail){
            alert("请选择要换线的生产工单!");
            return false;
        }
        if (detail.status != '0') {
            alert("当前工单已开工，不能更换生产单元!");
            return false;
        }
        $('#showProductionUnitsDialog').dialog({
            title: '生产单元信息',
            width: 800,
            height: 600,
            closed: false,
            cache: false,
            href: 'console/jsp/showProductionUnits.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function(){
                    var productionUnit = $('#showProductionUnitTg').iTreegrid('getSelected');
                    if(!productionUnit){
                        return false;
                    }
                    if(detail.productionUnitCode==productionUnit.code){
                        alert("该工单已经在当前生产单元！");
                        return false;
					}
                    $.get("mcWorkSheet/chengeProductionUnitForPC.do", {
                        productionUnitId: productionUnit.id,
                        workSheetId:detail.id
                    }, function (result) {
                        if (result.success) {
                            $("#productionUnitDg").iDatagrid("reload");
                        }else{
                           alert(result.msg);
                        }
                        $('#showProductionUnitsDialog').dialog("close");
                    });
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showProductionUnitsDialog').dialog("close");
                }
            }]
        });
	}
</script>
</head>
<body>
	<div data-toggle="topjui-layout" data-options="fit:true">
		<div
			data-options="region:'west',title:'',split:true,border:false,width:'15%',iconCls:'fa fa-sitemap',headerCls:'border_right',bodyCls:'border_right'">
			<!-- treegrid表格 -->
			<table id="productionUnitTg" data-toggle="topjui-treegrid"
				data-options="id:'productionUnitTg',
			   idField:'id',
			   treeField:'name',
			   fitColumns:true,
			   fit:true,
			   singleSelect:true,
			   url:'productionUnit/queryTopProductionUnits.do',
			   childGrid:{
			   	   param:'productionUnitId:id',
                   grid:[
                       {type:'datagrid',id:'productionUnitDg'},
                   ]
			   },onSelect:function(index, row){
					var production = $('#productionUnitTg').iTreegrid('getSelected');
					var productionId = '';
					if(production){
					productionId = production.id;
					}
					$('#productionUnitDg').iDatagrid('load',{
					productionUnitId:productionId
					});
				}">
				<thead>
					<tr>
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
						data-options="id:'productionUnitDg',
                       url:'workSheet/queryWorkSheetsByProductionUnitId.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                       parentGrid:{
                           type:'treegrid',
                           id:'productionUnitTg',
                       },
			           childTab: [{id:'southTabs'}]">
						<thead>
							<tr>
								<th data-options="field:'id',title:'id',checkbox:false,hidden:true"></th>
								<th data-options="field:'no',title:'单号',width:'150px',align:'center',sortable:false"></th>
								<th	data-options="field:'workSheetType',title:'工单类型',width:'80px',align:'center',sortable:false,formatter:function(value,row,index){
									if(value){
										if(value=='common'){
											return '普通工单';
										}
										if(value=='repair'){
											return '返修工单';
										}
									}else{
										return '';
									}
								}"></th>
								<th
									data-options="field:'manufactureDate',title:'生产日期',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
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
								<th data-options="field:'workPieceCode',width:'120px',align:'center',title:'工件代码',sortable:false"></th>
								<th data-options="field:'workPieceName',width:'120px',align:'center',title:'工件名称',sortable:false"></th>
								
								<th data-options="field:'unitType',title:'规格型号',width:'120px',align:'center',sortable:false"></th>
								<th data-options="field:'graphNumber',title:'图号',width:'120px',align:'center',sortable:false"></th>
								<th
									data-options="field:'productCount',title:'生产数量',width:'80px',align:'center',sortable:false"></th>
								<th data-options="field:'batchNumber',title:'批号',width:'120px',align:'center',sortable:false"></th>
								<th data-options="field:'stoveNumber',title:'材料编号',width:'120px',align:'center',sortable:false"></th>
								<th
									data-options="field:'productionUnitName',width:'120px',align:'center',title:'生产单元',sortable:false"></th>
								<th data-options="field:'status',title:'状态',width:'80px',align:'center',sortable:false,formatter:function(value,row,index){
									if(value){
										switch(value){
											case '0': return '计划';
											case '1': return '加工中';
											case '2': return '停工';
											case '3': return '完工';
										}
									}
								}"></th>
								<th data-options="field:'note',title:'备注',width:'80px',align:'center',sortable:false"></th>
								<th
									data-options="field:'documentMaker',title:'制单人',width:'80px',align:'center',sortable:false"></th>
								<th
									data-options="field:'makeDocumentDate',title:'制单日期',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
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
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 生产单元表格工具栏开始 -->
	<div id="productionUnitDg-toolbar" class="topjui-toolbar"
		data-options="grid:{
           type:'datagrid',
           id:'productionUnitDg'
       }">
		<a href="javascript:void(0)"
		   data-toggle="topjui-menubutton"
		   data-options="extend: '#productionUnitDg-toolbar',
       iconCls: 'fa fa-search'" onclick="queryWorksheets()">查询</a>
<sec:authorize access="hasAuthority('ADD_WORKSHEET')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="extend: '#productionUnitDg-toolbar',
       iconCls: 'fa fa-plus', parentGrid:{
               type:'treegrid',
               id:'productionUnitTg'
            }" onclick="addWorkSheet()">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SEE_WORKSHEET')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="extend: '#productionUnitDg-toolbar',
       iconCls: 'fa fa-plus', parentGrid:{
               type:'treegrid',
               id:'productionUnitTg'
            }" onclick="SeeWorkSheet()">查看</a>
</sec:authorize>
<sec:authorize access="hasAuthority('GENERATEWORKSHEET_ERPPRODUCTION')">
     <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#productionUnitDg-toolbar',
            iconCls: 'fa fa-sign-out',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 500,
                href: 'console/jsp/erpProductionRecodeTrunWorksheet.jsp',
                url:'Mom_orderdetail/queryMom_recorddetailByCodeAndSeq.do?code={no}',
                 buttons:[
           	{text:'确定',handler:function(){

           			var moDId=$('#moDId').val();
           			var moLotCode=$('#moLotCode').val();
           			var startDate=$('#startDate').val();
           			var productionUnitId=$('#productionUnitId').val();
           			var cInvName=$('#cInvName').val();
           			var detailInvCode=$('#detailInvCode').val();
           			var unitType=$('#unitType').val();
           			var define24=$('#define24').val();
           			var Define33=$('#Define33').val();
           			if(!Define33){
           			    Define33 = 0;
           			}
           			var mdeptCode=$('#mdeptCode').val();
           			var obj=$('#productionUnitDg').datagrid('getSelected');
           			var no = obj.no;

           			if(!productionUnitId){
           				$.iMessager.alert('提示','生产单元不能为空,请选择生产单元!');
           				return false;
           			}

           		$.get('Mom_orderdetail/queryMom_recorddetailById.do',{
           			id:obj.moDId
           			 },function(result){
							$.get('workSheet/addWorkSheet.do',{
							    id:obj.id,
                                no: no,
                                manufactureDate:startDate,
                                workPieceName:cInvName,
                                unitType:unitType,
                                productCount:obj.productCount,
                                batchNumber:moLotCode,
                                stoveNumber:define24,
                                productionUnitId:productionUnitId,
                                productionUnitName:$('#productionUnitName').iCombobox('getText'),
                                productionUnitCode:$('#productionUnitCode').val(),
                                workSheetType:'common',
                                note:'',
                                workPieceCode:detailInvCode,
                                fromErp:true,
                                Define33:Define33,
                                moallocateInvcode:obj.moallocateInvcode,
                                moallocateQty:obj.moallocateQty,
                                LotNo:obj.lotNo,
                                moDId:obj.moDId,
                                departmentCode:mdeptCode,
                                moId:obj.moId,
                                mocode:obj.mocode
                            },function(result){
                                if(result.success){
                                    if(result.changeNoMsg){
                                        $.iMessager.alert('提示',result.changeNoMsg,'messager-info',function () {
                                            location.reload();
                                        });

                                    }else{
                                        $.iMessager.alert('提示',result.msg,'messager-info',function () {
                                            location.reload();
                                        });
                                        $('#classEditDialog').iDialog('close');
                                    }
                                }else{
                                    $.iMessager.alert('提示',result.msg);
                                }
                            });
           			 })

           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'取消',handler:function(){
           		$('#classEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">ERP变更</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_WORKSHEET')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#productionUnitDg-toolbar',
       iconCls:'fa fa-trash',
       url:'workSheet/deleteWorkSheet.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'productionUnitDg',param:'id:id'},
       onSuccess:function(){$('#productionUnitTg').iTreegrid('reload');}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('START_WORKSHEET')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#productionUnitDg-toolbar',
       iconCls:'fa fa-play',
       url:'workSheet/start.do',
       confirmMsg:'确认开工?',
       grid: {uncheckedMsg:'请先勾选开工的工单',id:'productionUnitDg',param:'id:id'}">开工</a>
</sec:authorize>
<sec:authorize access="hasAuthority('STOP_WORKSHEET')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#productionUnitDg-toolbar',
       iconCls:'fa fa-stop',
       url:'workSheet/stop.do',
       confirmMsg:'确认停工?',
       grid: {uncheckedMsg:'请先勾选停工的工单',id:'productionUnitDg',param:'id:id'}">停工</a>
</sec:authorize>
<sec:authorize access="hasAuthority('FINISH_WORKSHEET')">
       <a href="javascript:void(0)" data-toggle="topjui-menubutton"
			data-options="method:'doAjax',
       extend: '#productionUnitDg-toolbar',
       iconCls:'fa fa-check',
       url:'workSheet/over.do',
       confirmMsg:'确认完工?',
       grid: {uncheckedMsg:'请先勾选完工的工单',id:'productionUnitDg',param:'id:id'}">完工</a>
</sec:authorize>
<sec:authorize access="hasAuthority('LINECHANGE_WORKSHEET')">
		<a href="javascript:void(0)" data-toggle="topjui-menubutton"
		   data-options="extend: '#productionUnitDg-toolbar',
       			iconCls: 'fa fa-exchange', parentGrid:{
               type:'treegrid',
               id:'productionUnitTg'
            }" onclick="changeProductionUnit()">换线</a>
</sec:authorize>
		<form id="searchForm" style="margin-top: 10px;margin-left: 10px;">
			生产日期:<input id="from" type="text" data-toggle="topjui-datebox" style="width: 12%;"> 至
			<input id="to" type="text" data-toggle="topjui-datebox" style="width: 12%;">
			生产批号：<input id="batchNoSearch" data-toggle="topjui-textbox" style="width:9%">
			工件代码:<input id="inventoryCodeSearch" data-toggle="topjui-textbox" style="width:9%">
			工单单号:<input id="noSearch" data-toggle="topjui-textbox" style="width:9%">
			状态:<%--<input id="statusSearch" data-toggle="topjui-textbox" >--%>
			<input id="statusSearch" data-toggle="topjui-combobox" style="width:12%" data-options="
                            valueField: 'value',
                            textField: 'text',
                            data: [{
                                text: '计划',
                                value:'0'
                            },{
                                 text: '加工中',
                                value:'1'
                            },{
                                 text: '停工',
                                value:'2'
                            },{
                                 text: '完工',
                                value:'3'
                            }]">
		</form>
	</div>
	<div id="showProductionUnitsDialog"></div>
</body>
</html>