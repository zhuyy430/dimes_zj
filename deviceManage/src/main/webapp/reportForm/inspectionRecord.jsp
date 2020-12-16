<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp" %>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript">
    function showInspectionRecordDetail(formNo){
        top.location.href='reportForm/inspectionRecordDetail.jsp?formNo=' + formNo;
    }
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                       data-options="id:'inspectionRecordDg',
                      <%-- url:'inspectionRecord/queryInspectionRecords.do?no=<%=request.getParameter("no")%>',--%>
                       url:'inspectionRecord/queryInspectionRecords.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                    <thead>
                    <tr>
                        <th data-options="field:'formNo',title:'检验单号',width:'150px',align:'center',formatter:function(value,row,index){
                            return '<a href=\'reportForm/inspectionRecordDetail.jsp?formNo='+row.formNo+'\'  target=\'_blank\'>'+row.formNo+'</a>';
                        }"></th>
                        <th data-options="field:'inspectionDate',title:'检验日期',width:'100px',align:'center',
                        formatter:function(value,row,index){
                             if(value){
                                return getDate(new Date(value));
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'productionUnitName',title:'生产单元名称',width:'150px',align:'center'"></th>
                        <th data-options="field:'no',title:'工单单号',width:'150px',align:'center'"></th>
                        <th data-options="field:'processCode',title:'工序代码',width:'100px',align:'center'"></th>
                        <th data-options="field:'processName',title:'工序名称',width:'100px',align:'center'"></th>
                        <th data-options="field:'inventoryCode',title:'工件代码',width:'100px',align:'center'"></th>
                        <th data-options="field:'inventoryName',title:'工件名称',width:'100px',align:'center'"></th>
                        <th data-options="field:'specificationType',title:'规格型号',width:'100px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'100px',align:'center'"></th>
                        <th data-options="field:'furnaceNumber',title:'材料编号',width:'100px',align:'center'"></th>
                        <th data-options="field:'inspectionResult',title:'检验结果',width:'100px',align:'center'"></th>
                        <th data-options="field:'inspectorName',title:'检验员',width:'100px',align:'center'"></th>
                        <th data-options="field:'className',title:'班次',width:'100px',align:'center'"></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
