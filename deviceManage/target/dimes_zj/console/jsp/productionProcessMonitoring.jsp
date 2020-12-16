<%--生产过程监控--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
</head>
<script type="text/javascript">
    function queryJobBookingFormDetails(){

        $('#jobBookingFormDetailDg').iDatagrid('options').url = "workSheet/queryProductionProcessMonitoring.do";
        $("#jobBookingFormDetailDg").iDatagrid("load",{
            from:$("#from").val(),
            to:$("#to").val(),
            productionUnit:$("#productionUnitCodeSearch").val(),
            no:$("#noSearch").val(),
            workpiece:$("#workpieceSearch").val(),
            stoveNumber:$("#stoveNumberSearch").val(),
            batchNumber:$("#batchNoSearch").val()
        })
    }
    function reset(){
            $("#from").iDatebox('setValue', '');
            $("#to").iDatebox('setValue', '');
            $("#productionUnitCodeSearch").val("");
            $("#productionUnitNameSearch").iTextbox('setValue', "");
            $("#noSearch").iTextbox('setValue', "");
            $("#workpieceSearch").iTextbox('setValue', "");
            $("#stoveNumberSearch").iTextbox('setValue', "");
            $("#batchNoSearch").iTextbox('setValue', "");
    }

    function exportProductionProcessMonitoring(){
        var dg = $("#jobBookingFormDetailDg").iDatagrid('getRows');
        console.log(dg);
        if (!dg) {
            $.messager.alert("提示", "请查询信息!");
            return;
        }
        window.location.href = "workSheet/exportProductionProcessMonitoring.do?from="+$("#from").val()+"&to="+$("#to").val()+"&productionUnit="+$("#productionUnitCodeSearch").val()+"&no="+$("#noSearch").val()+"&workpiece="+$("#workpieceSearch").val()+"&stoveNumber="+$("#stoveNumberSearch").val()+"&batchNumber="+$("#batchNoSearch").val()
    }
</script>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                       data-options="id:'jobBookingFormDetailDg',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                    <thead>
                    <tr>
                        <th data-options="field:'batchNumber',title:'生产令/批号',width:'120px',align:'center',sortable:false"></th>
                        <th data-options="field:'no',title:'单号',width:'150px',align:'center',sortable:false"></th>
                        <th data-options="field:'productionUnitName',width:'120px',align:'center',title:'生产单元',sortable:false"></th>
                        <th data-options="field:'workPieceCode',width:'120px',align:'center',title:'工件代码',sortable:false"></th>
                        <th data-options="field:'workPieceName',width:'120px',align:'center',title:'工件名称',sortable:false"></th>
                        <th data-options="field:'unitType',title:'规格型号',width:'120px',align:'center',sortable:false"></th>
                        <th data-options="field:'stoveNumber',title:'材料编号',width:'120px',align:'center',sortable:false"></th>
                        <th data-options="field:'productCount',title:'计划生产数量',width:'120px',align:'center',sortable:false"></th>


                        <th data-options="field:'firstMaterialDate',title:'投产日期',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
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

                        <th data-options="field:'materialCount',title:'领料数量',width:'120px',align:'center',sortable:false"></th>

                        <th data-options="field:'materialBoxNum',title:'领料箱数',width:'80px',align:'center',sortable:false"></th>


                        <th data-options="field:'lastJobbookingDate',title:'报工日期',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
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


                        <th  data-options="field:'jobbookingCount',title:'报工数量',width:'80px',align:'center',sortable:false"></th>
                        <th  data-options="field:'jobbookingBoxNum',title:'报工箱数',width:'80px',align:'center',sortable:false"></th>
                        <th  data-options="field:'jobbookingInwarehouseCount',title:'入库数量',width:'80px',align:'center',sortable:false"></th>
                        <th  data-options="field:'unqualifiedCounts',title:'不合格数量',width:'80px',align:'center',sortable:false"></th>
                        <th  data-options="field:'differenceNumber',title:'差异数',width:'80px',align:'center',sortable:false,formatter:function(value,row,index){
                                    if (row.cInvDefine14) {
                                        return (Number(row.materialCount)/Number(row.cInvDefine14)-Number(row.jobbookingInwarehouseCount)).toFixed(2);
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
<div id="jobBookingFormDetailDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'jobBookingFormDetailDg'
       }">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="extend: '#jobBookingFormDetailDg-toolbar',
       iconCls: 'fa fa-search'" onclick="queryJobBookingFormDetails()">查询</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#jobBookingFormDetailDg-toolbar',
       iconCls: 'fa fa-spinner'" onclick="reset()">重置</a>
    <a href="javascript:void(0)" onclick="exportProductionProcessMonitoring()"
       data-toggle="topjui-menubutton"
       data-options="iconCls:'fa fa-sign-out'">导出</a>
    <%--<sec:authorize access="hasAuthority('EXPORT_JOBOOKING')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="iconCls: 'fa fa-sign-out'"
        >导出</a>
    </sec:authorize>
    &lt;%&ndash;<sec:authorize access="hasAuthority('ADD_JOBOOKING')">
            <a href="javascript:void(0)"
               data-toggle="topjui-menubutton"
               data-options="extend: '#jobBookingFormDetailDg-toolbar',
           iconCls: 'fa fa-plus'" onclick="addJobBookingForm()">新增</a>
    </sec:authorize>&ndash;%&gt;
    <sec:authorize access="hasAuthority('DEL_JOBOOKING')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"
           data-options="iconCls:'fa fa-trash'" onclick="deleteJobBookingForm()">删除</a>
    </sec:authorize>
    <sec:authorize access="hasAuthority('SEE_JOBOOKING')">
        <a href="javascript:void(0)"
           data-toggle="topjui-menubutton"  data-options="extend: '#jobBookingFormDetailDg-toolbar',
       iconCls: 'fa fa-eye'" onclick="queryJobBookingFormDetailsByFormNo()">查看</a>
    </sec:authorize>--%>
    <%--<a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls:'fa fa-check-square-o'" id="audit" onclick="auditAtForm()">审核</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
   extend: '#jobBookingFormDetailDg-toolbar',
   iconCls:'fa fa-history',
   url:'classes/disabledClasses.do',
   grid: {uncheckedMsg:'请选择操作的班次',id:'jobBookingFormDetailDg',param:'id:id'}" id="unaudit" onclick="unauditAtForm()">反审核</a>--%>
    <form id="searchForm" style="margin-top: 10px;margin-left: 10px;">
        投产日期:<input id="from" type="text" data-toggle="topjui-datebox" style="width: 12%;"> 至
        <input id="to" type="text" data-toggle="topjui-datebox" style="width: 12%;">
        批号：<input id="batchNoSearch" data-toggle="topjui-textbox" style="width:9%">
        生产单元:<input type="text" id="productionUnitNameSearch" style="width: 200px;"
                    name="productionUnitName" data-toggle="topjui-combotree"
                    data-options="required:false,
                       valueField:'code',
                       textField:'name',
                       panelWidth:'250px',
                       url:'productionUnit/queryProductionUnitSiteTree.do',
                       onSelect: function(rec){
                       	$('#productionUnitCodeSearch').val(rec.code);
                       }
                       ">
        <input type="hidden" name="productionUnitCodeSearch" id="productionUnitCodeSearch">
        工件代码:<input id="workpieceSearch" data-toggle="topjui-textbox" style="width:9%">
        工单单号:<input id="noSearch" data-toggle="topjui-textbox" style="width:9%">
        材料编号:<input id="stoveNumberSearch" data-toggle="topjui-textbox" style="width:12%">
    </form>
</div>
<div id="showVendorsDialog"></div>
<div id="showWarehousesDialog"></div>
</body>
</html>
