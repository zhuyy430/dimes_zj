	//全屏查看
	function showFullScreen(url){
		//显示文档内容的iframe对象
		var docUrl=url;
		var $previewDiv = $("#fullScreenDiv");
		$previewDiv.empty();
		var suffix = '';
		//截取后缀
		if(docUrl!=null && $.trim(docUrl)!=''){
			 var point = docUrl.lastIndexOf("."); 
			 suffix = docUrl.substr(point+1);
		}else{
			alert("没有预览文档!");
			return false;
		}
		var $docIframe=$("<iframe style='width:90%;height:90%;'>");
		switch(suffix){
		case "pdf":
		case "PDF":{
			$docIframe.attr("src",docUrl);
			$previewDiv.append($docIframe);
			break;
		}
		case "png":
		case "PNG":
		case "jpg":
		case "JPG":
		case "JPEG":
		case "jpeg":
		case "gif":
		case "GIF":
		case "bmp":
		case "BMP":{
			var $img = $("<img style='max-width:100%;max-height:100%;text-align:center;'>");
			$img.attr("src",docUrl);
			$previewDiv.append($img);
			break;
		}
		}
		$("#dialog-layer").css("display","block");
	}