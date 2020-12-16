<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp" %>
<script type="text/javascript">
    $(function () {
    });
    
  //根据条件查询报工单详情
    function queryMom_recorddetail(){
    	var status=$("input[type='checkbox']").is(':checked');
        $("#Mom_recorddetailDg").iDatagrid("reload",{
        	search_from:$("#from").val(),
        	search_to:$("#to").val(),
        	MDeptCode:$("#MDeptCode").val(),
        	mocode:$("#mocode").val(),
        	moallocateInvcode:$("#moallocateInvcode").val(),
        	status:status
        })
    }
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                       data-options="id:'Mom_recorddetailDg',
                       url:'Mom_orderdetail/queryMom_recorddetail.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
                      ">
                    <thead>
                    <tr>
                        <th data-options="field:'ModId',title:'id',width:'150px',align:'center',hidden:true"></th>
                        <th data-options="field:'moId',title:'moid',width:'150px',align:'center',hidden:true"></th>
                        <th data-options="field:'createTime',title:'单据日期',width:'150px',align:'center',formatter:function(value,row,index){
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
                        <th data-options="field:'mocode',title:'生产订单号',width:'120px',align:'center'"></th>
                        <th data-options="field:'sortSeq',title:'行号',width:'150px',align:'center'"></th>
                        <th data-options="field:'startDate',title:'开工日期',width:'120px',align:'center',formatter:function(value,row,index){
                        	if(value){
                        				var date = new Date(value);
                                        var month = date.getMonth()+1;
                                        var monthStr = ((month>=10)?month:('0' + month));
                                        
                                        var day = date.getDate();
                                        var dayStr = ((day>=10)?day:('0'+day));
                                        
                                        var dateStr = date.getFullYear() + '-' + monthStr + '-' + dayStr;
                                        return dateStr;
                        	}else{
                        		return '';
                        	}
                        }"></th>
                        <!-- <th data-options="field:'mdeptCode',title:'生产部门',width:'120px',align:'center'"></th> -->
                        <th data-options="field:'detailInvCode',title:'工件代码',width:'120px',align:'center'"></th>
                        <th data-options="field:'cInvName',title:'工件名称',width:'120px',align:'center'"></th>
                        <th data-options="field:'cInvStd',title:'规格型号',width:'120px',align:'center'"></th>
                        <th data-options="field:'moLotCode',title:'批号',width:'120px',align:'center'"></th>
                        <th data-options="field:'detailQty',title:'生产数量',width:'120px',align:'center'"></th>
                        <th data-options="field:'moallocateQty',title:'数量',width:'120px',align:'center'"></th>
                        <th data-options="field:'define24',title:'材料编码',width:'120px',align:'center'"></th>
                        <th data-options="field:'define33',title:'材料数量',width:'120px',align:'center'"></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="Mom_recorddetailDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'Mom_recorddetailDg'
       }">
<sec:authorize access="hasAuthority('QUERY_ERPPRODUCTION')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#Mom_recorddetailDg-toolbar',
       iconCls: 'fa fa-search'" onclick="queryMom_recorddetail()">查询</a>
</sec:authorize>
   <!--  <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#Mom_recorddetailDg-toolbar',
       iconCls: 'fa fa-sign-out'" onclick="trun()">生成生产工单</a> -->
<sec:authorize access="hasAuthority('GENERATEWORKSHEET_ERPPRODUCTION')">
     <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method: 'openDialog',
            extend: '#Mom_recorddetailDg-toolbar',
            iconCls: 'fa fa-sign-out',
            dialog: {
            	id:'classEditDialog',
                width: 600,
                height: 500,
                href: 'console/jsp/erpProductionRecodeTrunWorksheet.jsp',
                url:'Mom_orderdetail/queryMom_recorddetailById.do?id={moDId}',
                 buttons:[
           	{text:'确定',handler:function(){
           			
           			var moDId=$('#moDId').val();
           			var moLotCode=$('#moLotCode').val();
           			var startDate=$('#startDate').val();
           			var productionUnitId=$('#productionUnitId').val();
           			var obj=$('#Mom_recorddetailDg').datagrid('getSelected');
           			var no = '';
           			if(obj.sortSeq<10){
           			    no = obj.mocode+'-0'+obj.sortSeq;
           			}else{
					    no = obj.mocode+'-'+obj.sortSeq;
					}

           			if(!productionUnitId){
           				$.iMessager.alert('提示','生产单元不能为空,请选择生产单元!');
           				return false;
           			}
           		
           		$.get('Mom_orderdetail/queryMom_recorddetailIsTrun.do',{
           			id:obj.moDId
           			 },function(result){
	           			 if(result){
	           			 	$.iMessager.alert('提示','该订单已经生成生产工单无法再次生成!');
	           			 	return false;
	           			 }else{
							$.get('workSheet/addWorkSheet.do',{
				            no: no,
				            manufactureDate:startDate,
				            workPieceName:obj.cInvName,
				            unitType:obj.cInvStd,
				            productCount:obj.detailQty,
				            batchNumber:moLotCode,
				            stoveNumber:obj.define24,
				            productionUnitId:productionUnitId,
				            productionUnitName:$('#productionUnitName').iCombobox('getText'),
				            productionUnitCode:$('#productionUnitCode').val(),
				            workSheetType:'common',
				            note:'',
				            workPieceCode:obj.detailInvCode,
				            fromErp:true,
				            Define33:obj.define33,
				            moallocateInvcode:obj.moallocateInvcode,
				            moallocateQty:obj.moallocateQty,
				            LotNo:obj.lotNo,
				            moDId:obj.moDId,
				            departmentCode:obj.mdeptCode,
				          moId:obj.moId,
				          mocode:obj.mocode
				        },function(result){
				            if(result.success){
				                if(result.changeNoMsg){
				                    $.iMessager.alert('提示',result.changeNoMsg,'messager-info',function () {
				                        location.reload();
				                    });
				
				                }else{
				                    $.iMessager.alert('提示','生成成功!','messager-info',function () {
				                        location.reload();
				                    });
				           			$('#classEditDialog').iDialog('close');
				                }
				            }else{
				                $.iMessager.alert('提示',result.msg);
				            }
				
				        });
	           			 
	           			 }
           			 })
				    
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'取消',handler:function(){
           		$('#classEditDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
            }">生成生产工单</a>
</sec:authorize>
    <form id="searchForm" style="margin-top: 10px;margin-left: 10px;">
        单据日期:<input id="from" type="text" data-toggle="topjui-datebox" style="width: 12%;"> 至
        <input id="to" type="text" data-toggle="topjui-datebox" style="width: 12%;">
       <!--  &nbsp;&nbsp;&nbsp;
        生产部门:<input id="MDeptCode" data-toggle="topjui-textbox" style="width:12%"> -->
        &nbsp;&nbsp;&nbsp;
       生产订单号:<input id="mocode" data-toggle="topjui-textbox" style="width:12%">
       &nbsp;&nbsp;&nbsp;
        工件代码:<input id="moallocateInvcode" data-toggle="topjui-textbox" style="width:12%">
        <p style="float: right;margin-right: 10%"><input type="checkbox" name="item"><label>显示已转</label></p>
    </form>
</div>
<div id="dd"></div>
</body>
</html>