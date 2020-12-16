/**修改menubutton的文本和图标方法*/
function switchButton(btnId,disabled){
	var btnObj = $('#' + btnId);
	var options = btnObj.iMenubutton('options');
	if(disabled){
		options.text='启用';
		options.iconCls='fa fa-play';
	}else{
		options.text='停用';
		options.iconCls='fa fa-stop';
	}
	btnObj.iMenubutton(options);
}
//获取当前的日期，合适为：yyyy-MM-dd HH:mm:ss
function getDateTime(now){
	var ymd = getDate(now);
	var hms = getTime(now);
	return ymd + " " + hms;
}
//获取当前日期，格式为：yyyy-MM-dd
function getDate(now){
	var year = getYear(now);
	var month = getMonth(now);
	var date = getDateOfMonth(now);
	return year + "-" + month + "-" + date ;
}
//获取当前日期，格式为：yyyy-MM-dd
function getTime(now){
	var hour = getHour(now);
	var minute = getMinute(now);
	var second = getSecond(now);
	return hour + ":" + minute + ":" + second;
}

function getSecond(date){
	return date.getSeconds();
}

function getMinute(date){
	return date.getMinutes();
}

function getHour(date){
	return date.getHours();
}

function getDateOfMonth(date){
	return date.getDate();
}

function getYear(date){
	return date.getFullYear();
}

function getMonth(date){
	return date.getMonth()+1;
}
/**
 * 导出Excel
 * @param tableId  要导出的表格的id
 * @param excelFileName Excel文件名称
 */
function exportExcel(tableId,excelFileName){
    $("#" + tableId).table2excel({
        name: excelFileName,
        filename: excelFileName+".xls", // do include extension
        fileext: ".xls",
        preserveColors: true
    });
}

/**
 * 产生yyyy-MM格式的日期字符串
 * @param inputId 用于产生日期控件的input控件id
 * @param date 日期
 */
function generateYYYYMM(inputId,date){
    //月份查询条件组件初始化
    $('#' + inputId).datebox({
        //显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
        onShowPanel : function() {
            //触发click事件弹出月份层
            span.trigger('click');
            if (!tds)
            //延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                setTimeout(
                    function() {
                        tds = p.find('div.calendar-menu-month-inner td');
                        tds.click(function(e) {
                            //禁止冒泡执行easyui给月份绑定的事件
                            e.stopPropagation();
                            //得到年份
                            var year = /\d{4}/.exec(span.html())[0],
                                //月份
                                //之前是这样的month = parseInt($(this).attr('abbr'), 10) + 1;
                                month = parseInt($(this).attr('abbr'), 10);
                            //隐藏日期对象
                            $('#' + inputId).datebox('hidePanel')
                            //设置日期的值
                                .datebox( 'setValue',year+ '-'+ month);
                        });
                    }, 0);
        },
        //配置parser，返回选择的日期
        parser : function(s) {
            if (!s)
                return new Date();
            var arr = s.split('-');
            return new Date(parseInt(arr[0], 10), parseInt(
                arr[1], 10) - 1, 1);
        },
        //配置formatter，只返回年月 之前是这样的d.getFullYear() + '-' +(d.getMonth());
        formatter : function(d) {
            var currentMonth = (d.getMonth() + 1);
            var currentMonthStr = currentMonth < 10 ? ('0' + currentMonth)
                : (currentMonth + '');
            return d.getFullYear() + '-' + currentMonthStr;
        }
    });
    //日期选择对象
    var p = $('#' + inputId).datebox('panel'),
        //日期选择对象中月份
        tds = false,
        //显示月份层的触发控件
        span = p.find('span.calendar-text');
    //设置前当月
    $("#" + inputId).datebox("setValue",date.getFullYear() + "-" + (date.getMonth() + 1));
}
//生成UUID
function generateUUID(){
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";

    var uuid = s.join("");
    return uuid;
}

/**
 * 格式化浮点数
 * @param value 浮点数值
 * @param decimalDigit 小数位数
 */
function formatFloat(value,decimalDigit){
    if(isNaN(value)){
        throw new Error(value + " 不是数值！");
    }

    if(isNaN(decimalDigit) || decimalDigit<0 || decimalDigit%1!==0){
        throw new Error(decimalDigit + " 不是合法的小数位数!");
    }
    //四舍五入之后的值
    var val = parseFloat(value).toFixed(decimalDigit);
    return parseFloat(val);
}