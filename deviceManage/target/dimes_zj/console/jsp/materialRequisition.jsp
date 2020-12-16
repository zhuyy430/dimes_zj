<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp" %>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="console/jsp/js/materialRequisition.js"></script>
<script type="text/javascript">
    $(function () {
    });
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table data-toggle="topjui-datagrid"
                       data-options="id:'materialRequisitionDg',
                       url:'materialRequisitionDetail/queryMaterialRequisitionDetails.do',
                       singleSelect:true,
                       fitColumns:true,
                       pagination:true">
                    <thead>
                    <tr>
                        <th data-options="field:'formNo',title:'领料单号',width:'150px',align:'center',
                        formatter:function(value,row,index){
                            if(row.materialRequisition){
                                return row.materialRequisition.formNo;
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'receivingDate',title:'领料日期',width:'100px',align:'center',
                        formatter:function(value,row,index){
                             if(row.materialRequisition){
                                return getDate(new Date(row.materialRequisition.pickingDate));
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'worksheetNo',title:'工单单号',width:'150px',align:'center',
                        formatter:function(value,row,index){
                            if(row.materialRequisition){
                                if(row.materialRequisition.workSheet){
                                    return row.materialRequisition.workSheet.no;
                                }
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'productionUnit',title:'生产单元',width:'100px',align:'center',
                        formatter:function(value,row,index){
                            if(row.materialRequisition){
                                if(row.materialRequisition.workSheet){
                                    return row.materialRequisition.workSheet.productionUnitName;
                                }
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'workpieceCode',title:'工件代码',width:'100px',align:'center',
                        formatter:function(value,row,index){
                            if(row.materialRequisition){
                                if(row.materialRequisition.workSheet){
                                    return row.materialRequisition.workSheet.workPieceCode;
                                }
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'workpieceName',title:'工件名称',width:'150px',align:'center',
                        formatter:function(value,row,index){
                            if(row.materialRequisition){
                                if(row.materialRequisition.workSheet){
                                    return row.materialRequisition.workSheet.workPieceName;
                                }
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'unitType',title:'规格型号',width:'100px',align:'center',
                        formatter:function(value,row,index){
                            if(row.materialRequisition){
                                if(row.materialRequisition.workSheet){
                                    return row.materialRequisition.workSheet.unitType;
                                }
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'batchNum',title:'批号',width:'100px',align:'center',
                        formatter:function(value,row,index){
                            if(row.materialRequisition){
                                if(row.materialRequisition.workSheet){
                                    return row.materialRequisition.workSheet.batchNumber;
                                }
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'furnaceNum',title:'材料编号',width:'100px',align:'center',
                        formatter:function(value,row,index){
                            if(row.materialRequisition){
                                if(row.materialRequisition.workSheet){
                                    return row.materialRequisition.workSheet.stoveNumber;
                                }
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'productCount',title:'数量',width:'100px',align:'center',
                        formatter:function(value,row,index){
                            if(row.materialRequisition){
                                if(row.materialRequisition.workSheet){
                                    return row.materialRequisition.workSheet.productCount;
                                }
                            }else{
                                return '';
                            }
                        }"></th>
                        <th data-options="field:'barCode',title:'箱条码',width:'100px',align:'center'"></th>
                        <th data-options="field:'inventoryCode',title:'物料代码',width:'100px',align:'center'"></th>
                        <th data-options="field:'inventoryName',title:'物料名称',width:'100px',align:'center'"></th>
                        <th data-options="field:'specificationType',title:'规格型号',width:'100px',align:'center'"></th>
                        <th data-options="field:'batchNumber',title:'批号',width:'100px',align:'center'"></th>
                        <th data-options="field:'furnaceNumber',title:'材料编号',width:'100px',align:'center'"></th>
                        <th data-options="field:'amount',title:'领用数量',width:'100px',align:'center'"></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="materialRequisitionDg-toolbar" class="topjui-toolbar"
     data-options="grid:{
           type:'datagrid',
           id:'materialRequisitionDg'
       }">
<sec:authorize access="hasAuthority('QUERY_MATERIALREQUISITION')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#materialRequisitionDg-toolbar',
       iconCls: 'fa fa-search'" onclick="queryMaterialRequisitionDetails()">查询</a>
</sec:authorize>
<sec:authorize access="hasAuthority('EXPORT_MATERIALREQUISITION')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#materialRequisitionDg-toolbar',
       iconCls: 'fa fa-sign-out',
      ">导出</a>
</sec:authorize>
<sec:authorize access="hasAuthority('ADD_MATERIALREQUISITION')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="extend: '#materialRequisitionDg-toolbar',
       iconCls: 'fa fa-plus'" onclick="addMateriaRequisition()">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('DEL_MATERIALREQUISITION')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"
       data-options="iconCls:'fa fa-trash'" onclick="deleteMateriaRequisition()">删除</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SEE_MATERIALREQUISITION')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton"  data-options="extend: '#materialRequisitionDg-toolbar',
       iconCls: 'fa fa-eye'" onclick="queryMaterialRequisitionDetailsByFormNo()">查看</a>
</sec:authorize>
    <form id="searchForm" style="margin-top: 10px;margin-left: 10px;">
        领料日期:<input id="from" type="text" data-toggle="topjui-datebox" style="width: 12%;"> 至
        <input id="to" type="text" data-toggle="topjui-datebox" style="width: 12%;">
       物料代码:<input id="inventoryCodeSearch" data-toggle="topjui-textbox" style="width:12%">
        工单单号:<input id="noSearch" data-toggle="topjui-textbox" style="width:12%">
        箱条码:<input id="barCodeSearch" data-toggle="topjui-textbox" style="width:12%">
        材料编号:<input id="furnaceNumSearch" data-toggle="topjui-textbox" style="width:12%">
    </form>
</div>
</body>
</html>