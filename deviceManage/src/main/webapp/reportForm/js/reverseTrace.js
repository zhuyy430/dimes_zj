//反向追溯
function reverseTrace() {
    var barCode = $("#barCode").iTextbox("getValue");
    if(!barCode){
        alert("请输入要追溯的条码!");
        return false;
    }
    showReverseTrace(barCode);
    showJobBookingForm(barCode);
}
//反向追溯(包装条码)
function packReverseTrace() {
    var barCode = $("#packBarCode").iTextbox("getValue");
    if(!barCode){
        alert("请输入要追溯的条码!");
        return false;
    }
    showPackReverseTrace(barCode);
    showJobBookingForm(barCode);
}
var canvas ;
var stage ;
var scene ;
$(function () {
    canvas = document.getElementById('myCanvas');
    stage = new JTopo.Stage(canvas);
    scene = new JTopo.Scene(stage);
});
//显示追溯轨迹
function  showPackReverseTrace(barCode){
    $.get("reportForm/reverseTraceByPackageCode.do",{packageCode:barCode},function(result){
        if(result){
            stage.clear();
            scene = new JTopo.Scene(stage);
            scene.mode = "select";
            var node = new JTopo.Node(result.text + "||" + result.extra);
            node.setLocation(result.x, result.y);
            node.setSize(result.height,result.width);
            node.fontColor='14,75,131';
            node.id = result.id;
            node.showSelected = true;
            node.setImage(result.img);
            node.click(function(event){
                showJobBookingForm(node.id);
            });
            wrap(node);
            scene.add(node);
            canvas.height=drawNodes(node,result)+200;
        }
    });
}
//显示追溯轨迹
function  showReverseTrace(barCode){
    $.get("reportForm/reverseTraceByBarCode.do",{barCode:barCode},function(result){
        if(result){
            stage.clear();
            scene = new JTopo.Scene(stage);
            scene.mode = "select";
            var node = new JTopo.Node(result.text + "||" + result.extra);
            node.setLocation(result.x, result.y);
            node.setSize(result.height,result.width);
            node.fontColor='14,75,131';
            node.id = result.id;
            node.showSelected = true;
            node.setImage(result.img);
            node.click(function(event){
                showJobBookingForm(node.id);
            });
            wrap(node);
            scene.add(node);
            canvas.height=drawNodes(node,result)+200;
        }
    });
}
//绘制图形
function drawNodes(node,result){
    var height = result.y;
    var children = result.childs;
    if(children!=null && children.length>0){
        for(var i = 0;i<children.length;i++){
            var child = children[i];
            var childNode=new JTopo.Node(child.text + "||" + child.extra);
            childNode.setLocation(child.x,child.y);
            childNode.setSize(child.height,child.width);
            childNode.fontColor='14,75,131';
            childNode.id = child.id;
            childNode.setImage(child.img);
            childNode.showSelected = true;
            addEvent(childNode,child);
            wrap(childNode);
            scene.add(childNode);
            //画线
          /*  var link = new JTopo.Link(node, childNode);
            link.lineWidth = 3; // 线宽
            link.strokeColor = '0,200,255';
            link.lineJoin  = 'round';*/
             var link = new JTopo.FoldLink(node, childNode);
              link.direction = 'vertical' || 'horizontal';
              link.lineWidth = 3; // 线宽
              link.bundleGap = 20; // 线条之间的间隔
              link.strokeColor = '0,200,255';
            scene.add(link);
            height = drawNodes(childNode,child);
        }
    }

    return height;
}
//为追溯节点添加事件
function addEvent(node,data){
    switch(data.type){
        case "WorkSheet":{
            //查询工单信息
            node.click(function(event){
                showWorkSheet(node.id)
            });
            break;
        }
        case "WarehousingApplicationForm":{
            //查找入库申请单信息
            node.click(function(event){
                showWarehousing(node.id)
            });
            break;
        }
        case "PO_Pomain":{
            //查询采购订单信息
            node.click(function(event){
                showPomain(node.id);
            });
            break;
        }
        case "JobBookingFormDetail":{
            //查找报工箱号条码信息
            node.click(function(event){
                showJobBookingBar(node.id);
            });
            break;
        }
        case "WarehousingApplicationFormDetail":{
            //查找材料箱条码
            node.click(function(event){
                showWarehousingBar(node.id);
            });
            break;
        }
    }
}

function wrap(node){
    node.paintText = function(a){
        a.beginPath(),
            a.font = this.font,
            a.wrapText(this.text,this.height/2,this.height);
        a.closePath()
    }
    CanvasRenderingContext2D.prototype.wrapText = function(str,x,y){

        var textArray =str.split('||');//文本以‘-’作为换行的标识符
        if(textArray==undefined||textArray==null)return false;
        var rowCnt = textArray.length;
        var i = 0,imax = rowCnt,maxLength = 0;maxText = textArray[0];
        for(;i<imax;i++){
            var nowText = textArray[i],textLength = nowText.length;
            if(textLength >=maxLength){
                maxLength = textLength;
                maxText = nowText;
            }
        }
        var maxWidth = this.measureText(maxText).width;
        var lineHeight = this.measureText("元").width;
        x-= str.length/rowCnt*lineHeight/2;
        for(var j= 0;j<textArray.length;j++){
            var words = textArray[j];
            this.fillText(words,x,y);
            this.fillStyle = '#0E4B83';//设置字体颜色
            this.font="12px Verdana";//设置字体大小
            y+= lineHeight+3;
        }
    };
}