<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp" %>
<script type="text/javascript">
    /**
     * 查询包装条码
     */
    function reloadPackageCodes(){
        $("#departmentDg").iDatagrid("reload",{
            from:$("#from").iDatebox("getValue"),
            to:$("#to").iDatebox("getValue"),
            saleOrderNo:$("#saleOrderNo").iTextbox("getValue"),
            customer:$("#customer").iTextbox("getValue"),
            batchNumber:$("#batchNumber").iTextbox("getValue"),
            inventoryCode:$("#inventoryCode").iTextbox("getValue")
        });
    }
    //打印条码
    function printQrCode(){
            var printRow = $("#departmentDg").iDatagrid("getSelected");
            if(!printRow){
                $.iMessager.alert("提示","请选择需要打印的记录!");
                return false;
            }
            var newWin = window.open("console/jsp/packageCode_print.jsp?ids="+printRow.id);
    }
</script>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table
                        data-toggle="topjui-datagrid"
                        data-options="id:'departmentDg',
                       url:'packageCode/queryPackageCodes.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true,
			           childTab: [{id:'southTabs'}]">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:false,width:'80px',hidden:true"></th>
                        <th data-options="field:'code',title:'包装条码',width:'180px',align:'center'"></th>
                        <th data-options="field:'inventoryCode',title:'物料代码',width:'150px',align:'center'"></th>
                        <th data-options="field:'inventoryName',title:'物料名称',width:'150px',align:'center'"></th>
                        <th data-options="field:'specificationType',title:'规格型号',width:'150px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'150px',align:'center'"></th>
                        <th data-options="field:'furnaceNumber',title:'材料编号',width:'150px',align:'center'"></th>
                        <th data-options="field:'boxamount',title:'数量',width:'100px',align:'center'"></th>
                        <th data-options="field:'boxnum',title:'箱号',width:'50px',align:'center'"></th>
                        <th data-options="field:'customer',title:'客户名称',width:'180px',align:'center'"></th>
                        <th data-options="field:'formNo',title:'计划单号',width:'150px',align:'center'"></th>
                        <th data-options="field:'saleNo',title:'销售订单号',width:'150px',align:'center'"></th>
                        <th data-options="field:'createDate',title:'生成时间',width:'120px',align:'center',formatter:function(value,row,index){
                            if(value){
                                return getDate(new Date(value));
                            }else{
                                return '';
                            }
                        }"></th>
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
                     parentGrid:{
                         type:'datagrid',
                         id:'departmentDg',
                         param:'packageCode:code'
                     }">
                    <div title="条码信息" data-options="id:'tab0',iconCls:'fa fa-th'">
                        <table
                                data-toggle="topjui-datagrid"
                                data-options="id:'position',
                               initCreate: false,
                               singleSelect:true,
                               fitColumns:true,
						       url:'jobBookingFormDetail/queryJobBookingFormDetailByPackageCode.do'">
                            <thead>
                            <tr>
                                <th data-options="field:'id',title:'id',checkbox:false,hidden:'true'"></th>
                                <th data-options="field:'barCode',title:'条码',width:'150px',align:'center'"></th>
                                <th data-options="field:'inventoryCode',title:'物料代码',width:'150px',align:'center'"></th>
                                <th data-options="field:'inventoryName',title:'物料名称',width:'150px',align:'center'"></th>
                                <th data-options="field:'specificationType',title:'规格型号',width:'150px',align:'center'"></th>
                                <th data-options="field:'batchNumber',title:'批号',width:'150px',align:'center'"></th>
                                <th data-options="field:'furnaceNumber',title:'材料编号',width:'150px',align:'center'"></th>
                                <th data-options="field:'number',title:'数量',width:'100px',align:'center'"></th>
                                <th data-options="field:'boxNum',title:'箱号',width:'50px',align:'center'"></th>
                                <th data-options="field:'employeeName',title:'员工',width:'150px',align:'center'"></th>
                                <th data-options="field:'productionUnitName',title:'产线',width:'150px',align:'center'"></th>
                                <th data-options="field:'className',title:'班次',width:'150px',align:'center'"></th>
                                <th data-options="field:'workSheetNo',title:'生产任务单',width:'180px',align:'center'"></th>
                            </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="departmentDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'departmentDg'
       }">
<sec:authorize access="hasAuthority('DEL_PACKAGERECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#departmentDg-toolbar',
       iconCls:'fa fa-trash',
       url:'packageCode/deleteById.do',
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'departmentDg',param:'id:id'}">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('PRINT_PACKAGERECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#departmentDg-toolbar',
       iconCls:'fa fa-print'"  onclick="printQrCode()">打印</a>
</sec:authorize>
<sec:authorize access="hasAuthority('QUERY_PACKAGERECORD')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#departmentDg-toolbar',
       iconCls:'fa fa-search'"  onclick="reloadPackageCodes()">查询</a>
</sec:authorize>
    <div>
        <form id="searchForm">
        <label>包装日期</label>
            <input type="text" name="from" data-toggle="topjui-datebox"
                   data-options="required:false" id="from" style="width: 150px;">
            至
            <input type="text" name="to" data-toggle="topjui-datebox"
                   data-options="required:false" id="to" style="width: 150px;">
        <label>客户</label>
            <input type="text" name="customer" data-toggle="topjui-textbox"
                   data-options="required:false" id="customer" style="width: 150px;">
        <label>销售订单</label>
            <input type="text" name="customer" data-toggle="topjui-textbox"
                   data-options="required:false" id="saleOrderNo" style="width: 150px;">
        <label>物料代码</label>
            <input type="text" name="inventoryCode" data-toggle="topjui-textbox"
                   data-options="required:false" id="inventoryCode" style="width: 150px;">
        <label>批号</label>
            <input type="text" name="batchNumber" data-toggle="topjui-textbox"
                   data-options="required:false" id="batchNumber" style="width: 150px;">
        </form>
    </div>
</div>
<div id="position-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'position'
       }">
    <%--<a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'openDialog',
       extend: '#position-toolbar',
       iconCls: 'fa fa-plus',
       parentGrid:{
       	id:'departmentDg',
       	type:'datagrid',
       	param:'classesId:id'
       },
       dialog:{
           id:'deviceAddDialog',
            width:620,
           height:500,
           href:'console/jsp/classes_device_add.jsp',
           buttons:[
           	{text:'保存',handler:function(){
           			var ids = $('#deviceTable').iDatagrid('getSelections');
           		var idsArray = new Array();
           		if(ids.length<=0){
           			alert('请选择要添加的设备!');
           		}else{
           			for(var i = 0;i < ids.length;i++){
           				idsArray.push(ids[i].id);
           			}
           			$.get('classes/addDevice4Classes.do',{
           				classesId:$('#departmentDg').iDatagrid('getSelected').id,
           				deviceIds:JSON.stringify(idsArray)
           			},function(data){
           				if(data.success){
           					$('#position').iDatagrid('reload');
           					$('#deviceTable').iDatagrid('reload');
           				}else{
           					alert(data.msg);
           				}
           			});
           		}
           	},iconCls:'fa fa-plus',btnCls:'topjui-btn-normal'},
           	{text:'关闭',handler:function(){
           		$('#deviceAddDialog').iDialog('close');
           	},iconCls:'fa fa-close',btnCls:'topjui-btn-normal'},
           ]
       }">新增</a>
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="method:'doAjax',
       extend: '#position-toolbar',
       iconCls:'fa fa-trash',
       url:'classes/deleteDeviceFromClasses.do?classesId={parent.id}',
        parentGrid:{
        type:'datagrid',
        id:'departmentDg'
    },
       grid: {uncheckedMsg:'请先勾选要删除的数据',id:'position',param:'deviceSiteId:id'}">删除</a>--%>
</div>
</body>
</html>