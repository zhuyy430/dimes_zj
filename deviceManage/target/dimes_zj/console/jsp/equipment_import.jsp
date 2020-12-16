<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp" %>
<script type="text/javascript">

    //搜索点检记录
    function searchData(){
        $("#departmentDg").iDatagrid("reload",{
            search_from:$("#search_from").val(),
            search_to:$("#search_to").val(),
            search_batchNo:$("#search_batchNo").val(),
            search_code:$("#search_code").val(),
            search_status:$("#search_status").val()
        });
    }


    function openWindow(){
        var checkedArray = $("#departmentDg").iDatagrid("getChecked");
        if(checkedArray.length>0){
            var codes = "";
            var batchNos = "";
            for(var i = 0;i<checkedArray.length;i++){
                var equipment = checkedArray[i];
                codes += equipment.code +",";
                batchNos += equipment.batchNo +",";
            }




            codes = codes.substring(0,codes.length-1);
            batchNos = batchNos.substring(0,batchNos.length-1);

            $.get('ERPEquipment/addPrintEquipment4ERPEquipment.do',{
                codes:codes,
                batchNos:batchNos
            },function(data){
                if(data.success){
                    //$("#ids").val(ids);
                    window.open("console/jsp/equipment_print.jsp?equipmentIds="+data.ids);
                }else{
                    alert(data.msg);
                }
            });



        }else{
            alert("请选择要打印二维码的记录!");
            return false;
        }
    }
</script>
</head>
<body>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 >
                <!-- datagrid表格 -->
                <table
                        data-toggle="topjui-datagrid"
                        data-options="id:'departmentDg',
                       url:'ERPEquipment/queryERPEquipments.do',
                       singleSelect:true,
                       selectOnCheck:false,
					   checkOnSelect:false,
                       fitColumns:true,
                       pagination:true,
			           queryParams: {
                          search_status: '未打印',
                       },
			           onSelect:function(index,row){
					           		switchButton('processSwitchBtn',row.disabled);
					           }">
                    <thead>
                    <tr>
                        <th data-options="field:'id',title:'id',checkbox:true"></th>
                        <th data-options="field:'code',title:'装备编码',width:'180px',align:'center'"></th>
                        <th data-options="field:'name',title:'装备名称',width:'180px',align:'center',sortable:false"></th>
                        <th data-options="field:'batchNo',title:'批号',width:'180px',align:'center',sortable:false"></th>
                        <th data-options="field:'unitType',title:'规格型号',width:'180px',align:'center',sortable:false"></th>
                        <th data-options="field:'qty',title:'数量',width:'180px',align:'center',sortable:false"></th>
                        <th data-options="field:'useLife',title:'模具寿命',width:'180px',align:'center',sortable:false"></th>
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
    <a href="javascript:void(0)" data-toggle="topjui-menubutton"
       data-options="iconCls: 'fa fa-print',
            modal:true" onClick="openWindow()">打印二维码</a>
        <form  id="search_form">
            <%--<div id="rb"></div>--%>
            <label style="text-align: center;">状态</label>
            <input type="text" data-toggle="topjui-combobox"  style="width:150px;" id="search_status" name="search_status"
                       data-options="valueField:'text',textField:'text',data:[{text:'未打印','selected':true},{text:'已打印'}]"/>
            <label style="text-align: center;">日期</label>
            <input type="text" name="search_from" data-toggle="topjui-datebox"
                   id="search_from" style="width:150px;">
            TO <input type="text" name="search_to" data-toggle="topjui-datebox"
                      id="search_to" style="width:150px;">
            <label style="text-align: center;">装备</label>
            <input type="text" data-toggle="topjui-textbox" id="search_code" name="search_code"
                   data-options="width:150,prompt:'装备编码或名称'" style="width:150px;"/>
            <label style="text-align: center;">批号</label>
            <input type="text" data-toggle="topjui-textbox" id="search_batchNo" name="search_batchNo"
                   data-options="width:150,prompt:'装备批号'" style="width:150px;"/>
            <%--<label style="text-align: center;">班次</label>
            <input type="text" data-toggle="topjui-textbox" id="search_class" name="search_class"
                   data-options="width:150,prompt:'班次编码或名称'" style="width:150px;"/>--%>
            <%--<label style="text-align: center;">状态</label>
            <input type="text" data-toggle="topjui-combobox"  style="width:150px;" id="search_status" name="search_status"
                   data-options="valueField:'text',textField:'text',data:[{text:'','selected':true},
			{text:'计划'},{text:'完成'},{text:'未完成'}]"/>--%>
            <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',plain:false"
               style="width:100px;" onclick="searchData()">搜索</a>
            <a href="javascript:void(0)" data-toggle="topjui-menubutton" data-options="iconCls:'fa fa-search',plain:false"
               style="width:100px;" onclick="$('#search_form')[0].reset();searchData();">重置</a>
        </form>
</div>
<!-- 部门表格工具栏结束 -->
</body>
</html>