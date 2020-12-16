<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../common/jsp/head.jsp"%>
<script>
	$(function(){
		$.get("qualityCalendarRecord/queryQualityCalendar.do",function(data){
			qualityCalendar(data);
		});
	});
	var pathes;
	var layouts;
	var colors;
	
	function qualityCalendar(data){
		var myChart = echarts.init(document.getElementById("main"));

		 layouts = [
		    [[0, 0]],
		    [[-0.25, 0], [0.25, 0]],
		    [[0, -0.2], [-0.2, 0.2], [0.2, 0.2]],
		    [[-0.25, -0.25], [-0.25, 0.25], [0.25, -0.25], [0.25, 0.25]]
		];
		 pathes = 'M936.857805 523.431322c0 0-42.065715-68.89513-88.786739-68.89513-46.68416 0-95.732122 71.223091-95.732122 71.223091s-44.28544-72.503296-93.440922-71.152538c-35.565466 0.977306-62.89705 30.882406-79.124275 64.06615L579.773747 790.800797c-3.253248 37.391565-5.677568 50.904371-12.002816 69.63497-6.651802 19.698688-19.544883 35.227341-31.650099 45.909606-14.30231 12.621414-29.59831 22.066586-45.854208 27.424563-16.28969 5.362074-30.098739 6.496973-51.536794 6.496973-19.498906 0-36.95104-2.963456-52.395418-8.850534-15.410586-5.887078-28.420403-14.313984-39.034573-25.246003-10.613146-10.930995-18.757939-24.08151-24.435507-39.525171-5.676544-15.443763-8.532685-40.195482-8.532685-59.270963l0-26.232454 74.435273 0c0 24.644301-0.17705 64.452915 8.81408 77.006848 9.02697 12.515021 22.756147 18.092032 41.148826 18.791014 16.728678 0.636518 30.032179-8.061645 30.032179-8.061645s11.922022-10.5472 14.992077-19.756954c2.674995-8.025805 3.565363-22.180147 3.565363-22.180147s2.080461-21.789286 2.080461-34.234675L489.399906 514.299369c-16.678502-18.827776-43.801395-61.938688-82.756096-60.927693-54.699008 1.419366-100.422144 70.059622-100.422144 70.059622s-56.065126-70.059622-93.440922-70.059622c-37.376717 0-91.077939 70.059622-91.077939 70.059622S105.343488 156.737741 476.742042 119.363584l53.70327-74.714624 51.373056 74.714624C964.889395 142.740992 936.857805 523.431322 936.857805 523.431322z';
		colors = [
		     '#16B644', '#c4332b','#6862FD', '#FDC763'
		];
		
		var $names = $("#names");
		for(var i = 0;i<data.names.length;i++){
			var $text = $("<span style='display:inline-block;'>");
			$text.append(data.names[i]);
			var $color = $('<span style="display:inline-block;height:20px;width:40px;border-radius:3px;background-color:' + colors[i] +';margin-right:5px;"></span>');
			$names.append($color).append($text);
		}
		
		option = {
		    tooltip: {
		    },
		    calendar: [{
		        left: 'center',
		        top: 'middle',
		        cellSize: [120, 120],
		        yearLabel: {show: false},
		        orient: 'vertical',
		        dayLabel: {
		            firstDay: 1,
		            nameMap: 'cn'
		        },
		        monthLabel: {
		            show: false
		        },
		        range: data.month
		    }],
		    series: [{
		        type: 'custom',
		        coordinateSystem: 'calendar',
		        renderItem: renderItem,
		        dimensions: [null, {type: 'ordinal'}],
		        data: getData(data)
		    }]
		};

	    myChart.setOption(option);
	}
	function getData(data) {
		var date = +echarts.number.parseDate(data.month + '-01');
	    var end = +echarts.number.parseDate(data.nextMonth + '-01');
	    var dayTime = 3600 * 24 * 1000;
	    var d = [];
	    if(data.data.length<=0){
	    	return d;
	    }
	    var j = 0;
	    for (var time = date; time < end; time += dayTime) {
	        var items = [];
	        var counts = data.data[j++];
	        var eventCount = counts.length;
	        for (var i = 0; i < eventCount; i++) {
	        	 items.push(counts[i]);
	        }
	        d.push([
	            echarts.format.formatTime('yyyy-MM-dd', time),
	            items.join('|')
	        ]);
	    }
	    return d;
	}

	function renderItem(params, api) {
	    var cellPoint = api.coord(api.value(0));
	    var cellWidth = params.coordSys.cellWidth;
	    var cellHeight = params.coordSys.cellHeight;

	    var value = api.value(1);
	    var events = value && value.split('|');
	    if (isNaN(cellPoint[0]) || isNaN(cellPoint[1])) {
	        return;
	    }

	    var group = {
	        type: 'group',
	        children: echarts.util.map(layouts[events.length - 1], function (itemLayout, index) {
	        	var config = {
		                type: 'path',
		                shape: {
		                    pathData: pathes,
		                    x: -8,
		                    y: -8,
		                    width: 30,
		                    height: 30
		                },
		                position: [
		                    cellPoint[0] + echarts.number.linearMap(itemLayout[0], [-0.5, 0.5], [-cellWidth/2, cellWidth / 2]),
		                    cellPoint[1] + echarts.number.linearMap(itemLayout[1], [-0.5, 0.5], [-cellHeight / 2 + 20, cellHeight / 2])
		                ],
		                style: api.style({
 		                    //fill: colors[events[index] % colors.length]
 		                    fill: colors[index]
		                })
		            };
	            return  config;
	        }) || []
	    };

	    group.children.push({
	        type: 'text',
	        style: {
	            x: cellPoint[0],
	            y: cellPoint[1] - cellHeight / 2 + 15,
	            text: echarts.format.formatTime('dd', api.value(0)),
	            fill: '#777',
	            textFont: api.font({fontSize: 16})
	        }
	    });

	    return group;
	}
</script>
</head>
<body>
	<div id="main" style="width:1700px;height:1000px;">
	</div>
	<div id="names" style="margin: 0 auto;text-algin:center;text-size:20px;height:50px;"></div>
</body>
</html>