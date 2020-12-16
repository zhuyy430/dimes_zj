<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../common/jsp/head.jsp" %>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript">
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'">
        <table  data-toggle="topjui-datagrid"
                data-options="id:'inspectionRecord',
                       url:'inspectionRecordDetail/queryByFormNo.do?formNo=<%=request.getParameter("formNo")%>',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
            <thead>
            <tr>
                <th data-options="field:'parameterCode',title:'参数代码',width:'180px',align:'center'"></th>
                <th data-options="field:'parameterName',title:'参数名称',width:'180px',align:'center'"></th>
                <th data-options="field:'upLine',title:'上限值',width:'180px',align:'center'"></th>
                <th data-options="field:'lowLine',title:'下限值',width:'180px',align:'center'"></th>
                <th data-options="field:'standardValue',title:'标准值',width:'180px',align:'center'"></th>
                <th data-options="field:'unit',title:'单位',width:'180px',align:'center'"></th>
                <th data-options="field:'parameterValue',title:'参数值',width:'180px',align:'center'"></th>
                <th data-options="field:'inspectionResult',title:'检验结果',width:'80px',align:'center'"></th>
                <th data-options="field:'note',title:'备注',width:'80px',align:'center'"></th>
            </tr>
            </thead>
        </table>
    </div>
</div>
</body>
</html>
