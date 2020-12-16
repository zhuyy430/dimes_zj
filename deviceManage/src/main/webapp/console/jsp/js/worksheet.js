var _result;
/**
 * 打印工单
 */
function printSheet(){
    var workSheetNo = $("#workSheetNo").val();
    if(!workSheetNo){
        alert("工单保存后才可打印!");
        return false;
    }
    console.log(_result)
    /*$('#printDD').iDialog({
        title: '提示',
        width: 400,
        height: 200,
        closed: true,
        cache: false,
        modal: true
    });*/
    $('#printDD').iDialog('open');
}


function printWorksheet(){
    if(_result){
        var result = _result;
        $("#no4Print").text(result.no);
        $("#qrCode").attr("src",contextPath + "/"+ result.qrCodeUrl);
        $("#workPieceCode4Print").text(result.workPieceCode);
        $("#workPieceName4Print").text(result.workPieceName);
        $("#unitType4Print").text(result.unitType);
        $("#graphNumber4Print").text(result.graphNumber);
        $("#batchNumber4Print").text(result.batchNumber);
        $("#stoveNumber4Print").text(result.stoveNumber);
        $("#productCount4Print").text(result.productCount);
        $("#productionUnitName4Print").text(result.productionUnitName);
        $("#moallocateInvcode").text(result.moallocateInvcode);
        $("#moallocateQty").text(result.moallocateQty);
        $("#manufactureDate4Print").text(getDate(new Date(result.manufactureDate)));
        $.get("workSheet/queryWorkSheetDetailByWorkSheetId4Print.do",{
            workSheetId:result.id
        },function(data){
            if(data){
                var tbody = $('#contentBody');
                tbody.empty();
                for(var i = 0;i<data.length;i++){
                    var workSheetDetail = data[i];
                    var tr = $("<tr style='height:30px'>");
                    var serialNo = $("<td>");
                    var processCode = $("<td>");
                    var processName = $("<td>");
                    var deviceCode = $("<td>");
                    var deviceName = $("<td>");
                    var productionCount = $("<td>");
                    var customCol1 = $("<td>");
                    var customCol2 = $("<td>");

                    serialNo.text(i+1);
                    processCode.text(workSheetDetail.processCode);
                    processName.text(workSheetDetail.processName);
                    deviceCode.text(workSheetDetail.deviceCode);
                    deviceName.text(workSheetDetail.deviceName);
                    productionCount.text(workSheetDetail.productionCount);

                    tr.append(serialNo);
                    tr.append(processCode);
                    tr.append(processName);
                    tr.append(deviceCode);
                    tr.append(deviceName);
                    tr.append(productionCount);
                    tr.append(customCol1);
                    tr.append(customCol2);

                    tbody.append(tr);
                }
                $("#printDiv").css("display","block");
                $("#printDiv").print();
            }
        });
    }
}

function printOrder(){
    var result = _result;
    $("#qrCode1").attr("src",contextPath + "/"+ result.qrCodeUrl);
    var tbody = $('#tbodyPrint');
    tbody.empty();
    $.get("workSheet/querySawingProductionPrintByWorkSheetId.do",{
        workSheetId:result.id
    },function(data){
        var positionCodes="";
        if(data.position!=null){
            positionCodes=data.position.split(",");
        }
        var phtml="";
        for(var i=0;i<positionCodes.length;i++){
            if(positionCodes[i]!=null){
                phtml=phtml+positionCodes[i]+"<br>"
            }

        }
        var html="<tr><th>"+result.batchNumber+"</th><th>"+result.workPieceCode+"</th><th>"+result.workPieceName+"</th><th>"+result.unitType+"</th><th>"+result.productCount+"</th><th>"+data.finishTime+"</th><th>"+result.stoveNumber+"</th><th>"+(data.cInvDefine14!=null?data.cInvDefine14:"")+"</th><th>"+(result.moallocateQty!=null?result.moallocateQty:"")+"</th><th>"+phtml+"</th></tr>";
        tbody.append(html);
        $('#makeDocumenter').empty();
        $('#makeDocumentDate').empty();
        $('#makeDocumenter').append(result.documentMaker);
        $('#makeDocumentDate').append(data.time);
        $("#printOrderDiv").css("display","block")
        $("#printOrderDiv").print();
    })
}