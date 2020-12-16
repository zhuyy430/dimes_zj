<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp" %>
<script type="text/javascript" src="console/js/static/public/js/topjui.index.js"></script>
<script type="text/javascript" src="console/jsp/js/inspectionRecord.js"></script>
<script type="text/javascript">
    $(function () {
        var  inspectionRecordFormNo = '<%=request.getParameter("inspectionRecordFormNo")%>';
        if(inspectionRecordFormNo=='null'){
            inspectionRecordFormNo = '<%=session.getAttribute("inspectionRecordFormNo")%>';
            if(inspectionRecordFormNo=='null'){
                inspectionRecordFormNo='';
            }
        }
        if(!inspectionRecordFormNo){
            $("#inspectionDate").iDatebox("setValue",getDate(new Date()));
            $.get("user/queryLoginUser.do",function(result){
                if(result.employee){
                    $("#inspectorName").iTextbox("setValue",result.employee.name);
                }else{
                    $("#inspectorName").iTextbox("setValue",result.username);
                }
            });
            generateInspectionRecordFormNo();
            inspectionRecordFormNo='';
            //显示可编辑表格
            showInspectionRecordDetail(inspectionRecordFormNo);
        }else{//查看检验单
            queryInspectionRecordByFormNo(inspectionRecordFormNo);
            $("#formNo").iTextbox("readonly");
            $("#inspectionDate").iDatebox("readonly");
            $("#processCode").iCombobox("readonly");
            $("#classCode").iCombobox("readonly");
            $("#no").iTextbox("readonly");
        }

        $("#no").iTextbox({
            buttonIcon:'fa fa-search',
            onClickButton:function(){
                $('#showWorkSheetDialog').dialog({title:'工单信息'});
                $('#showWorkSheetDialog').dialog("open");
            }
        });
        //工单单号回车事件
        $("#no").iTextbox("textbox").bind("keydown",function(event){
            if(event.keyCode==13){
                $.get("workSheet/queryWorkSheetByNo.do",{No:$("#no").iTextbox("getValue")},function (result) {
                    fillWorkSheet(result);
                    $("#processCode").iCombobox("reload","workSheet/queryProcessCodeAndNameByNo.do?no="+$('#no').iTextbox('getValue'));
                });
            }
        });
        $('#showWorkSheetDialog').dialog({
            title: '工单信息',
            width: 1000,
            height: 700,
            closed: true,
            cache: false,
            href: 'console/jsp/showWorkSheets.jsp',
            modal: true,
            buttons:[{
                text:'确定',
                handler:function(){
                    var workSheet = $('#workSheetTable').iDatagrid('getSelected');
                    confirmWorkSheets(workSheet);
                }
            },{
                text:'关闭',
                handler:function(){
                    $('#showWorkSheetDialog').dialog("close");
                }
            }]
        });
    });
    //工单信息确定
    function confirmWorkSheets(workSheet) {
    	 if(workSheet){
             fillWorkSheet(workSheet);
             $("#processCode").iCombobox("clear");
             $("#processCode").iCombobox("reload","workSheet/queryProcessCodeAndNameByNo.do?no="+$('#no').iTextbox('getValue'));
         }
         $('#showWorkSheetDialog').dialog("close");
    }
</script>
</head>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'center',iconCls:'icon-reload',title:'',split:true,border:true,bodyCls:'border_top_none'">
        <div data-toggle="topjui-layout" data-options="fit:true">
            <div data-options="region:'center',title:'',fit:false,split:true,border:false,bodyCls:'border_bottom'"
                 style="height:60%;">
                <table id='inspectionRecordDetailDg'>
                    <thead>
                    <tr>
                        <th field='parameterCode' width='180px' align='center'>参数代码</th>
                        <th field='parameterName' width='180px' align='center'>参数名称</th>
                        <th field='upLine' width='180px' align='center'>上限值</th>
                        <th field='lowLine' width='180px' align='center'>下限值</th>
                        <th field='standardValue' width='80px' align='center' >标准值</th>
                        <th field='unit' width='180px' align='center'>单位</th>
                        <th field='parameterValue' width='180px' align='center' editor="{type:'numberbox',options:{precision:2}}">参数值</th>
                        <th field='inspectionResult' width='80px' align='center' editor="{type:'combobox',options:{valueField: 'text',textField: 'text',data:[
                        {text:'OK'},{text:'NG'}]}}">检验结果</th>
                        <th field='note' width='80px' align='center' editor="text">备注</th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
</div>
<div id="inspectionRecordDetailDg-toolbar" class="topjui-toolbar">
<sec:authorize access="hasAuthority('ADD_INSPECTIONRECORDDETAIL')">
    <a href="javascript:void(0)"
       data-toggle="topjui-menubutton" data-options="iconCls: 'fa fa-plus'"
       onclick="addNew()">新增</a>
</sec:authorize>
<sec:authorize access="hasAuthority('SAVE_INSPECTIONRECORDDETAIL')">
    <a href="javascript:void(0)"  data-options="iconCls: 'fa fa-save'"
       data-toggle="topjui-menubutton" onclick="saveInspectionRecord()">保存</a>
</sec:authorize>
    <form id="ff" method="post">
        <div title="检验记录单" data-options="iconCls:'fa fa-th'">
            <div class="topjui-fluid">
                <div style="height: 30px;text-align: center;width: 100%;font-size: 24px;font-weight: bold;margin-bottom: 30px;margin-top: 30px;">
                    检验记录单
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">检验单号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="formNo" data-toggle="topjui-textbox"
                                   data-options="required:true" id="formNo">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">检验日期</label>
                        <div class="topjui-input-block">
                            <input type="text" name="inspectionDate" data-toggle="topjui-datebox"
                                   id="inspectionDate">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">生产单元</label>
                        <div class="topjui-input-block">
                            <input type="text" name="productionUnitName" readonly="readonly" data-toggle="topjui-textbox"
                                   data-options="required:false" id="productionUnitName">
                            <input type="hidden" id="productionUnitCode" name="productionUnitCode" />
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">检验人</label>
                        <div class="topjui-input-block">
                            <input type="text" name="inspectorName" data-toggle="topjui-textbox"
                                   readonly id="inspectorName" readonly="readonly">
                        </div>
                    </div>
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">工单单号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="no" data-toggle="topjui-textbox"
                                   data-options="required:false" id="no">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">物料代码</label>
                        <div class="topjui-input-block">
                            <input type="text" name="inventoryCode" data-toggle="topjui-textbox"
                                   data-options="required:false" readonly="readonly" id="inventoryCode">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">物料名称</label>
                        <div class="topjui-input-block">
                            <input type="text" name="inventoryName" readonly="readonly" data-toggle="topjui-textbox"
                                   id="inventoryName">
                        </div>
                    </div>

                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">规格型号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="specificationType" readonly="readonly" data-toggle="topjui-textbox"
                                   id="specificationType">
                        </div>
                    </div>
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">检验工序</label>
                        <div class="topjui-input-block">
                            <input type="text" name="processCode" data-toggle="topjui-combobox"
                                   data-options="
                            valueField: '0',
                            textField: '1',
                            url:'workSheet/queryProcessCodeAndNameByNo.do',onSelect:function(record){
                                $.get('workSheet/queryWorkSheetDetailParametersRecordByNoAndoProcessCode.do',{no:$('#no').iTextbox('getValue'),processCode:record[0]},function(result){
                                    showInspectionRecordDetail();
                                });
                                $('#processName').val(record[1]);
                            }" id="processCode">
                            <input type="hidden" name="processName"  id="processName">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">班次</label>
                        <div class="topjui-input-block">
                            <input id="classCode" name="classCode" data-toggle="topjui-combobox" data-options="
                            valueField: 'code',
                            textField: 'name',
                            url:'classes/queryAllClasses.do',onSelect:function(record){
                                $('#className').val(record.name);
                            },onLoadSuccess:function(){
                                var data = $('#classCode').iCombobox('getData');
                                $('#classCode').iCombobox('select',data[0].code);
                            }">
                            <input type="hidden" id="className" name="className">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">批号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="batchNumber" readonly="readonly" data-toggle="topjui-textbox"
                                   data-options="required:false" id="batchNumber">
                        </div>
                    </div>
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">材料编号</label>
                        <div class="topjui-input-block">
                            <input type="text" name="furnaceNumber" readonly="readonly" data-toggle="topjui-textbox"
                                   readonly id="furnaceNumber">
                        </div>
                    </div>
                </div>
                <div class="topjui-row">
                    <div class="topjui-col-sm3">
                        <label class="topjui-form-label">检验类别</label>
                        <div class="topjui-input-block">
                            <input type="text" name="inspectionType" data-toggle="topjui-combobox"
                                   data-options="
                            valueField: 'text',
                            textField: 'text',
                            data:[{text:'班检',value:'班检',selected:true},{text:'首检',value:'首检'},{text:'巡检',value:'巡检'},{text:'末检',value:'末检'}]" id="inspectionType">
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>
<div id="showWorkSheetDialog"></div>
