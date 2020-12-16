<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp"%>
<style type="text/css">

</style>

<script type="text/javascript">
$(function(){
    $("#vendor").iTextbox({
        buttonIcon:'fa fa-search',
        onClickButton:function(){
            $('#showVendorsDialog').dialog("open");
        }
    });
    //弹出选择供应商
    $('#showVendorsDialog').dialog({
        title: '供应商信息',
        width: 800,
        height: 600,
        closed: true,
        cache: false,
        href: 'procurement/showVendors.jsp',
        modal: true,
        buttons:[{
            text:'确定',
            handler:function(){
                var vendor = $('#vendorTable').iDatagrid('getSelected');
                $("#vendor").iTextbox('setValue',vendor.cVenCode);
                $('#showVendorsDialog').dialog("close");
            }
        },{
            text:'关闭',
            handler:function(){
                $('#showVendorsDialog').dialog("close");
            }
        }]
    });
});
	//搜索采购订单记录
	function reloadRecord(){
		$("#productionUnitDg").iDatagrid("reload",{
			cVenCode:$("#cVenCode").val(),
			search_from:$("#search_from").val(),
			search_to:$("#search_to").val(),
			cPOID:$("#cPOID").val(),
			cBusType:$("#cBusType").val()
		});
	}
	
	//显示订单详情
	function showPomaingDetail() {
		var id = $("#productionUnitDg").iDatagrid("getSelected").cPOID;
		
		var params = {title:"订单详情",href:"procurement/reviewCenterDetail.jsp?id="+id}; 
		addParentTab(params);
	}
	
	function addParentTab(options) {
		var src, title;
		src = options.href;
		title = options.title;

		var iframe = '<iframe src="' + src + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>';
		parent.$('#index_tabs').iTabs("add", {
			title: title,
			content: iframe,
			closable: true,
			iconCls: 'fa fa-th',
			border: true
		});
	}
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
                        data-options="id:'productionUnitDg',
                       url:'PO_Pomain/queryPO_Pomain.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                        <thead>
                           <tr>
                                <th data-options="field:'id',title:'poID',checkbox:false,hidden:true"></th>
                                <th data-options="field:'cBusType',title:'业务类型',width:'150px',align:'center',sortable:false"></th>
                                <th data-options="field:'cPOID',width:'150px',align:'center',title:'采购订单号',sortable:false"></th>
                                <th
                                    data-options="field:'dPODate',title:'单据日期',width:'150px',align:'center',sortable:false, formatter:function(value,row,index){
                                    if (value) {
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
                                <th data-options="field:'cVenCode',width:'150px',align:'center',title:'供应商编码',sortable:false"></th>
                                <th data-options="field:'cVenName',width:'150px',align:'center',title:'供应商名称',sortable:false"></th>
                                
                                <th data-options="field:'cVenPerson',title:'联系人',width:'150px',align:'center',sortable:false"></th>
                                <th data-options="field:'cVenPhone',title:'联系电话',width:'150px',align:'center',sortable:false"></th>
                                <th
                                    data-options="field:'cMaker',width:'150px',align:'center',title:'制单人',sortable:false"></th>
                                <th data-options="field:'cVerifier',title:'审核人',width:'150px',align:'center',sortable:false"></th>
                                <th
                                    data-options="field:'cState',title:'订单状态',width:'150px',align:'center',sortable:false"></th>
                                <th data-options="field:'cMemo',title:'备注',width:'150px',align:'center',sortable:false"></th>
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
<sec:authorize access="hasAuthority('QUERY_PURCHASINGORDER')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search'" 
				onclick="reloadRecord()">查询</a>
</sec:authorize>
<sec:authorize access="hasAuthority('ADD_PURCHASINGORDER')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="iconCls: 'fa fa-plus'
      ">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_PURCHASINGORDER')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="
       iconCls:'fa fa-trash'
       ">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SEE_PURCHASINGORDER')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-edit'" 
				onclick="showPomaingDetail()">查看</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXAMINE_PURCHASINGORDER')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="
            iconCls: 'fa fa-check-circle-o'
            ">审核</a>
</sec:authorize>
<sec:authorize access="hasAuthority('REVERSEEXAMINE_PURCHASINGORDER')">
        <a href="javascript:void(0)" data-toggle="topjui-menubutton"
            data-options="
            iconCls: 'fa fa-history'
           ">反审核</a>
</sec:authorize>
        <div style="margin-top: 10px;margin-left: 10px;">
      	<label style="text-align: center;">单据日期:</label>
			<input type="text" name="search_from" data-toggle="topjui-datebox"
				id="search_from" style="width:150px;">
			至 <input type="text" name="search_to" data-toggle="topjui-datebox"
				id="search_to" style="width:150px;">
      <!-- 	供应商:<input id="cVenName" data-toggle="topjui-combobox" style="margin-bottom:5px;"
					name="cVenName"
					data-options="width:200,valueField:'cVenName',textField:'cVenName',url:'vendor/queryAllVendor.do'"> -->
		供应商:<input id="cVenCode" data-toggle="topjui-textbox" style="width:150px" data-options="prompt:'编码或名称'">
		采购订单号:<input type="text" id="cPOID" name="cPOID" 
      	data-toggle="topjui-textbox" data-options="prompt:'',width:150"/>
      	业务类型:<input id="cBusType" data-toggle="topjui-combobox" style="margin-bottom:5px;"
					name="cBusType"
					data-options="width:150,valueField:'text',textField:'text',
					data:[
					{text:'普通采购'},
					{text:'采购订单'},
					{text:'委外加工订单'}
					]">
      	</div>
    </div>
    <div id="showVendorsDialog"></div>
</body>
</html>