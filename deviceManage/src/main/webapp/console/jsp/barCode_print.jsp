<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script type="text/javascript" src="common/js/jQuery.print.js">
</script>
<script type="text/javascript">
    var formNo = '<%=request.getParameter("formNo")%>';
    function content( label, data){
        var row = /*'<div class="topjui-row row">'+*/
            '<div class="topjui-col-sm6 topjui-col-xs6">'+
            '<label class="topjui-form-label label" style="text-align: center;">' + label +':</label>' +
            '<div class="topjui-input-block data">' + data +
            '</div>'+
            '</div>'
        /*'</div>'*/;
        return row;
    }

    function content1( label, data){
        var row = /*'<div class="topjui-row row">'+*/
            '<div class="toplabel">'+
            '<label class="topjui-form-label label" style="text-align: center;">' + label +':</label>' +
            '<div class="topjui-input-block data1">' + data +
            '</div>'+
            '</div>'
            /*'</div>'*/;
        return row;
    }

    function content2( label, data){
        var row = /*'<div class="topjui-row row">'+*/
            '<div class="toplabel">'+
            '<label class="topjui-form-label label" style="text-align: center;">' + label +':</label>' +
            '<div class="topjui-input-block data2">' + data +
            '</div>'+
            '</div>'
            /*'</div>'*/;
        return row;
    }


    $(function(){
        $.get("boxBar/printJobBookingQr.do",{
            formNo:formNo
        },function(data){
            var $center = $("#center");
            var i = 0;
            while(i<data.length){
                var $row = $("<div class='topjui-row row-bottom'>");
                var data1 = data[i++];
                if(data1!=undefined){
                    var $col1 = $('<div class="topjui-col-sm4 topjui-col-xs4 col">');
                    //列里的div
                    var $innerDiv1 = $("<div class='innerDiv'>");
                    $col1.append($innerDiv1);

                    var $innerTop = $("<div class='innerTop'>");
                    var $innerBottom = $("<div class='innerBottom'>");

                    $innerDiv1.append($innerTop);
                    $innerDiv1.append($innerBottom);



                    $innerTop.append("<img style='float:left;' src='<%=basePath%>/" + data1.qrPath+"' />");




                    var boxBarType = content2('班次',(data1.classCode+" "+data1.className));
                    var formNo = content1('单据单号',(data1.formNo==null?" ":data1.formNo));
                    var warehousingDate = content1('报工时间',(data1.jobBookingDate==null?" ":data1.jobBookingDate));


                    var barcode = content('条码',(data1.barCode==null?" ":data1.barCode));
                    var inventoryCode = content('代码',(data1.inventoryCode==null?" ":data1.inventoryCode));
                    var inventoryName = content('名称',(data1.inventoryName==null?" ":data1.inventoryName.substring(0,9)));
                   // var inventoryName = content('名称',"");
                    var specificationType = content('规格',(data1.specificationType==null?" ":data1.specificationType));
                    var batchNumber = content('批号',(data1.batchNumber==null?" ":data1.batchNumber));
                    var furnaceNumber = content('材料编号',(data1.furnaceNumber==null?" ":data1.furnaceNumber));
                    var amount = content('数量',(data1.amountOfPerBox));
                    var boxNum = content('班次',(data1.classCode==null?" ":data1.classCode)+"/"+(data1.className==null?" ":data1.className));
                    var employeeName = content('员工',(data1.employeeName));
                    var productionLine = content('产线',(data1.productionLine));




                    $innerBottom.append(barcode);
                    $innerTop.append(boxBarType);
                    $innerTop.append(formNo);
                    $innerTop.append(warehousingDate);
                    $innerBottom.append(inventoryName);
                    $innerBottom.append(inventoryCode);
                    $innerBottom.append(specificationType);
                    $innerBottom.append(batchNumber);
                    $innerBottom.append(furnaceNumber);
                    $innerBottom.append(amount);
                    $innerBottom.append(boxNum);
                    $innerBottom.append(employeeName);
                    $innerBottom.append(productionLine);


                    $row.append($col1);
                }
                $center.append($row);
            }
        });
    });

    function print() {
        $("#center").print();
    }
</script>
<style>
    @media print {
        .data {
            border-bottom-style: solid;
            border-bottom-width: 1px;
            width:3cm;
            margin-left: 0.5cm;
            display: table-cell;
            vertical-align: bottom;
            height:0.5cm;
            font-size:6px;
        }
        .data1{
            border-bottom-style: solid;
            border-bottom-width: 1px;
            width:5cm;
            margin-left: 0.5cm;
            display: table-cell;
            vertical-align: bottom;
            height:0.5cm;
            font-size:6px;
        }

        .data2{
            font-size:0.5cm;
        }

        .row-bottom{
            margin-bottom: 0;
        }

        .label {
            width: 1.5cm;
            font-size:8pt;
        }
        .row{
            height:0.5cm;
        }
        img {
            height: 3cm;
            width: 3cm;
            vertical-align: middle;
            margin-top:-0.4cm;
            margin-left: 0.2cm;
        }
        .innerTop {
            height: 40%;
            line-height:2cm;
            text-align: center;
            overflow: hidden;
            padding-top: 0.6cm;
        }
        .innerDiv {
            height: 7.5cm;
            width: 10cm;
          /*  border: solid 1px gray;*/
            overflow: hidden;
        }
        .innerBottom {
            padding-top: -1cm;
            height: 60%;
            width:100%;
        }
        .col {
            text-align: center;
        }
        .toplabel{
            float: right;
            margin-right: 0.2cm;
            width: 6.6cm;
        }

    }

    @media screen {
        img {
            height: 100pt;
            width: 100pt;
            vertical-align: middle;
        }
        .label {
            width: 60pt;
        }
        .data {
            border-bottom-style: solid;
            margin-left: 70pt;
        }
        .data1 {
            border-bottom-style: solid;
            margin-left: 70pt;
        }
        .data2{
            font-size:20pt;
        }
        .innerTop {
            height: 50%;
            overflow: hidden;
        }
        .innerDiv {
            height: 300pt;
            width: 100%;
            border: solid 1px gray;
            padding:10pt;
        }
        .innerBottom {
            height: 50%;
            width:100%;
        }
        .col {
            padding: 10pt;
            text-align: center;
        }
        .toplabel{
            float: right;
            width: 70%;
        }
    }


</style>
<div data-toggle="topjui-layout" data-options="fit:true">
    <div data-options="region:'north',title:'',split:true"
         style="height: 50pt;">
        <div>
            <input type='button' value='打印' onclick='javascript:print()' />
            <input type='button' value='关闭' onclick='window.close()' />
        </div>
    </div>
    <div
            data-options="region:'center',title:'',fit:true,border:false,bodyCls:'border_right_bottom'">
        <div class=".container" id="center"></div>
    </div>
</div>