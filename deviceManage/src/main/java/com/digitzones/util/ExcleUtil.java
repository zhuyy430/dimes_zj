package com.digitzones.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import com.digitzones.model.TraceParameter;
import com.digitzones.model.TracePositive;
import com.digitzones.model.TraceReverse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcleUtil {
	/**
	 * 导出操作
	 * @param list
	 * @param Sign
	 */
	public static void excleOut(List<?> list,String Sign,String path) {  
		WritableWorkbook book = null;  
		try {
			// 创建一个excle对象
			Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
			int year = c.get(Calendar.YEAR); 
			int month = c.get(Calendar.MONTH)+1; 
			int date = c.get(Calendar.DATE); 
			int hour = c.get(Calendar.HOUR_OF_DAY); 
			int minute = c.get(Calendar.MINUTE); 
			int second = c.get(Calendar.SECOND); 
			String time = year + "" + month + "" + date + "" +hour + "" +minute + "" + second; 
			System.out.println(path+"/"+Sign+time+".xls");
			File folder = new File(path);
			if( !folder.exists()){
				folder.mkdirs();
				folder.setReadable(true);
				folder.setWritable(true);
				folder.setExecutable(true);
			}
			book = Workbook.createWorkbook(new File(path+"/"+Sign+time+".xls"));  
			// 通过excle对象创建一个选项卡对象  
            WritableSheet sheet = book.createSheet("sheet1", 0);
            if(Sign.equals("reverse")){
            	Label lab1 =new Label(0, 0, "ID");
            	Label lab2 =new Label(1, 0, "设备站点代码");
            	Label lab3 =new Label(2, 0, "设备站点名称");
            	Label lab4 =new Label(3, 0, "工序代码");
            	Label lab5 =new Label(4, 0, "工序名称");
            	Label lab6 =new Label(5, 0, "参数代码");
            	Label lab7 =new Label(6, 0, "参数名称");
            	Label lab8 =new Label(7, 0, "控制线UL");
            	Label lab9 =new Label(8, 0, "控制线LL");
            	Label lab10 =new Label(9, 0, "标准值");
            	Label lab11 =new Label(10, 0, "参数值");
            	Label lab12 =new Label(11, 0, "状态");
            	Label lab13 =new Label(12, 0, "状态/故障代码");
            	Label lab14 =new Label(13, 0, "批号");
            	Label lab15 =new Label(14, 0, "材料编号");
            	
            	sheet.addCell(lab1); 
            	sheet.addCell(lab2); 
            	sheet.addCell(lab3); 
            	sheet.addCell(lab4); 
            	sheet.addCell(lab5); 
            	sheet.addCell(lab6); 
            	sheet.addCell(lab7); 
            	sheet.addCell(lab8); 
            	sheet.addCell(lab9); 
            	sheet.addCell(lab10); 
            	sheet.addCell(lab11); 
            	sheet.addCell(lab12); 
            	sheet.addCell(lab13); 
            	sheet.addCell(lab14); 
            	sheet.addCell(lab15); 
            	for (int i = 1; i <= list.size(); i++) {
	            	 TraceReverse tra = (TraceReverse) list.get(i-1);
	            	 Label label1;
	            	 if(null!=tra.getId()&&!"".equals(tra.getId())){
	            		 label1 = new Label(0, i, String.valueOf(tra.getId()));
	            	 }else{
	            		 label1 = new Label(0, i, "");
	            	 }
	            	 Label label2;
	            	 if(null!=tra.getDeviceSiteCode()&&!"".equals(tra.getDeviceSiteCode())){
	            		 label2 = new Label(1, i, tra.getDeviceSiteCode());
	            	 }else{
	            		 label2 = new Label(1, i, "");
	            	 }
	            	 Label label3;
	            	 if(null!=tra.getDeviceSiteName()&&!"".equals(tra.getDeviceSiteName())){
	            		 label3 = new Label(2, i, tra.getDeviceSiteName());
	            	 }else{
	            		 label3 = new Label(2, i, "");
	            	 }
	            	 Label label4;
	            	 if(null!=tra.getProcessCode()&&!"".equals(tra.getProcessCode())){
	            		 label4 = new Label(3, i, tra.getProcessCode());
	            	 }else{
	            		 label4 = new Label(3, i, "");
	            	 }
	            	 Label label5;
	            	 if(null!=tra.getProcessName()&&!"".equals(tra.getProcessName())){
	            		 label5 = new Label(4, i, tra.getProcessName());
	            	 }else{
	            		 label5 = new Label(4, i, "");
	            	 }
	            	 Label label6;
	            	 if(null!=tra.getParameterCode()&&!"".equals(tra.getParameterCode())){
	            		 label6 = new Label(5, i, tra.getParameterCode());
	            	 }else{
	            		 label6 = new Label(5, i, "");
	            	 }
	            	 Label label7;
	            	 if(null!=tra.getParameterName()&&!"".equals(tra.getParameterName())){
	            		 label7 = new Label(6, i, tra.getParameterName());
	            	 }else{
	            		 label7 = new Label(6, i, "");
	            	 }
	            	 Label label8;
	            	 if(null!=tra.getUpLine()&&!"".equals(tra.getUpLine())){
	            		 label8 = new Label(7, i, tra.getUpLine().toString());
	            	 }else{
	            		 label8 = new Label(7, i, "");
	            	 }
	            	 Label label9;
	            	 if(null!=tra.getLowLine()&&!"".equals(tra.getLowLine())){
	            		 label9 = new Label(8, i, tra.getLowLine().toString());
	            	 }else{
	            		 label9 = new Label(8, i, "");
	            	 }
	            	 Label label10;
	            	 if(null!=tra.getStandardValue()&&!"".equals(tra.getStandardValue())){
	            		 label10 = new Label(9, i, tra.getStandardValue());
	            	 }else{
	            		 label10 = new Label(9, i, "");
	            	 }
	            	 Label label11;
	            	 if(null!=tra.getParameterValue()&&!"".equals(tra.getParameterValue())){
	            		 label11 = new Label(10, i, tra.getParameterValue());
	            	 }else{
	            		 label11 = new Label(10, i, "");
	            	 }
	            	 Label label12;
	            	 if(null!=tra.getStatus()&&!"".equals(tra.getStatus())){
	            		 label12 = new Label(11, i, tra.getStatus());
	            	 }else{
	            		 label12 = new Label(11, i, "");
	            	 }
	            	 Label label13;
	            	 if(null!=tra.getStatusCode()&&!"".equals(tra.getStatusCode())){
	            		 label13 = new Label(12, i, tra.getStatusCode());
	            	 }else{
	            		 label13 = new Label(12, i, "");
	            	 }
	            	 Label label14;
	            	 if(null!=tra.getBatchNumber()&&!"".equals(tra.getBatchNumber())){
	            		 label14 = new Label(13, i, tra.getBatchNumber());
	            	 }else{
	            		 label14 = new Label(13, i, "");
	            	 }
	            	 Label label15;
	            	 if(null!=tra.getStoveNumber()&&!"".equals(tra.getStoveNumber())){
	            		 label15 = new Label(14, i, tra.getStoveNumber());
	            	 }else{
	            		 label15 = new Label(14, i, "");
	            	 }
	            	 
	            	 sheet.addCell(label1); 
	            	 sheet.addCell(label2); 
	            	 sheet.addCell(label3); 
	            	 sheet.addCell(label4); 
	            	 sheet.addCell(label5); 
	            	 sheet.addCell(label6); 
	            	 sheet.addCell(label7); 
	            	 sheet.addCell(label8); 
	            	 sheet.addCell(label9); 
	            	 sheet.addCell(label10); 
	            	 sheet.addCell(label11); 
	            	 sheet.addCell(label12); 
	            	 sheet.addCell(label13); 
	            	 sheet.addCell(label14); 
	            	 sheet.addCell(label15); 
            	}
            }
            if(Sign.equals("positive")){
            	Label lab1 =new Label(0, 0, "ID");
            	Label lab2 =new Label(1, 0, "二维码");
            	Label lab3 =new Label(2, 0, "工件代码");
            	Label lab4 =new Label(3, 0, "工件名称");
            	Label lab5 =new Label(4, 0, "工序代码");
            	Label lab6 =new Label(5, 0, "工序名称");
            	Label lab7 =new Label(6, 0, "参数代码");
            	Label lab8 =new Label(7, 0, "参数名称");
            	Label lab9 =new Label(8, 0, "控制线UL");
            	Label lab10 =new Label(9, 0, "控制线LL");
            	Label lab11 =new Label(10, 0, "标准值");
            	Label lab12 =new Label(11, 0, "参数值");
            	Label lab13 =new Label(12, 0, "状态");
            	Label lab14 =new Label(13, 0, "状态/故障代码");
            	
            	sheet.addCell(lab1); 
            	sheet.addCell(lab2); 
            	sheet.addCell(lab3); 
            	sheet.addCell(lab4); 
            	sheet.addCell(lab5); 
            	sheet.addCell(lab6); 
            	sheet.addCell(lab7); 
            	sheet.addCell(lab8); 
            	sheet.addCell(lab9); 
            	sheet.addCell(lab10); 
            	sheet.addCell(lab11); 
            	sheet.addCell(lab12); 
            	sheet.addCell(lab13); 
            	sheet.addCell(lab14); 
            	for (int i = 1; i <= list.size(); i++) {
            		TracePositive tra = (TracePositive) list.get(i-1);
            		Label label1;
            		if(null!=tra.getId()&&!"".equals(tra.getId())){
            			label1 = new Label(0, i, String.valueOf(tra.getId()));
            		}else{
            			label1 = new Label(0, i, "");
            		}
            		Label label2;
            		if(null!=tra.getOpcNo()&&!"".equals(tra.getOpcNo())){
            			label2 = new Label(1, i, tra.getOpcNo());
            		}else{
            			label2 = new Label(1, i, "");
            		}
            		Label label3;
            		if(null!=tra.getWorkPieceCode()&&!"".equals(tra.getWorkPieceCode())){
            			label3 = new Label(2, i, tra.getWorkPieceCode());
            		}else{
            			label3 = new Label(2, i, "");
            		}
            		Label label4;
            		if(null!=tra.getWorkPieceName()&&!"".equals(tra.getWorkPieceName())){
            			label4 = new Label(3, i, tra.getWorkPieceName());
            		}else{
            			label4 = new Label(3, i, "");
            		}
            		Label label5;
            		if(null!=tra.getProcessCode()&&!"".equals(tra.getProcessCode())){
            			label5 = new Label(4, i, tra.getProcessCode());
            		}else{
            			label5 = new Label(4, i, "");
            		}
            		Label label6;
            		if(null!=tra.getProcessName()&&!"".equals(tra.getProcessName())){
            			label6 = new Label(5, i, tra.getProcessName());
            		}else{
            			label6 = new Label(5, i, "");
            		}
            		Label label7;
            		if(null!=tra.getParameterCode()&&!"".equals(tra.getParameterCode())){
            			label7 = new Label(6, i, tra.getParameterCode());
            		}else{
            			label7 = new Label(6, i, "");
            		}
            		Label label8;
            		if(null!=tra.getParameterName()&&!"".equals(tra.getParameterName())){
            			label8 = new Label(7, i, tra.getParameterName());
            		}else{
            			label8 = new Label(7, i, "");
            		}
            		Label label9;
            		if(null!=tra.getUpLine()&&!"".equals(tra.getUpLine())){
            			label9 = new Label(8, i, tra.getUpLine().toString());
            		}else{
            			label9 = new Label(8, i, "");
            		}
            		Label label10;
            		if(null!=tra.getLowLine()&&!"".equals(tra.getLowLine())){
            			label10 = new Label(9, i, tra.getLowLine().toString());
            		}else{
            			label10 = new Label(9, i, "");
            		}
            		Label label11;
            		if(null!=tra.getStandardValue()&&!"".equals(tra.getStandardValue())){
            			label11 = new Label(10, i, tra.getStandardValue());
            		}else{
            			label11 = new Label(10, i, "");
            		}
            		Label label12;
            		if(null!=tra.getParameterValue()&&!"".equals(tra.getParameterValue())){
            			label12 = new Label(11, i, tra.getParameterValue());
            		}else{
            			label12 = new Label(11, i, "");
            		}
            		Label label13;
            		if(null!=tra.getStatus()&&!"".equals(tra.getStatus())){
            			label13 = new Label(12, i, tra.getStatus());
            		}else{
            			label13 = new Label(12, i, "");
            		}
            		Label label14;
            		if(null!=tra.getStatusCode()&&!"".equals(tra.getStatusCode())){
            			label14 = new Label(13, i, tra.getStatusCode());
            		}else{
            			label14 = new Label(13, i, "");
            		}
            		
            		sheet.addCell(label1); 
            		sheet.addCell(label2); 
            		sheet.addCell(label3); 
            		sheet.addCell(label4); 
            		sheet.addCell(label5); 
            		sheet.addCell(label6); 
            		sheet.addCell(label7); 
            		sheet.addCell(label8); 
            		sheet.addCell(label9); 
            		sheet.addCell(label10); 
            		sheet.addCell(label11); 
            		sheet.addCell(label12); 
            		sheet.addCell(label13); 
            		sheet.addCell(label14); 
            	}
            }
            if(Sign.equals("parameter")){
            	Label lab1 =new Label(0, 0, "ID");
            	Label lab2 =new Label(1, 0, "二维码");
            	Label lab3 =new Label(2, 0, "工件代码");
            	Label lab4 =new Label(3, 0, "工件名称");
            	Label lab5 =new Label(4, 0, "批号");
            	Label lab6 =new Label(5, 0, "材料编号");
            	Label lab7 =new Label(6, 0, "工序代码");
            	Label lab8 =new Label(7, 0, "工序名称");
            	Label lab9 =new Label(8, 0, "参数代码");
            	Label lab10 =new Label(9, 0, "参数名称");
            	Label lab11 =new Label(10, 0, "控制线UL");
            	Label lab12 =new Label(11, 0, "控制线LL");
            	Label lab13 =new Label(12, 0, "标准值");
            	Label lab14 =new Label(13, 0, "参数值");
            	Label lab15 =new Label(14, 0, "状态");
            	Label lab16 =new Label(15, 0, "状态/故障代码");
            	
            	sheet.addCell(lab1); 
            	sheet.addCell(lab2); 
            	sheet.addCell(lab3); 
            	sheet.addCell(lab4); 
            	sheet.addCell(lab5); 
            	sheet.addCell(lab6); 
            	sheet.addCell(lab7); 
            	sheet.addCell(lab8); 
            	sheet.addCell(lab9); 
            	sheet.addCell(lab10); 
            	sheet.addCell(lab11); 
            	sheet.addCell(lab12); 
            	sheet.addCell(lab13); 
            	sheet.addCell(lab14); 
            	sheet.addCell(lab15); 
            	sheet.addCell(lab16); 
            	for (int i = 1; i <= list.size(); i++) {
            		TraceParameter tra = (TraceParameter) list.get(i-1);
            		Label label1;
            		if(null!=tra.getId()&&!"".equals(tra.getId())){
            			label1 = new Label(0, i, String.valueOf(tra.getId()));
            		}else{
            			label1 = new Label(0, i, "");
            		}
            		Label label2;
            		if(null!=tra.getOpcNo()&&!"".equals(tra.getOpcNo())){
            			label2 = new Label(1, i, tra.getOpcNo());
            		}else{
            			label2 = new Label(1, i, "");
            		}
            		Label label3;
            		if(null!=tra.getWorkPieceCode()&&!"".equals(tra.getWorkPieceCode())){
            			label3 = new Label(2, i, tra.getWorkPieceCode());
            		}else{
            			label3 = new Label(2, i, "");
            		}
            		Label label4;
            		if(null!=tra.getWorkPieceName()&&!"".equals(tra.getWorkPieceName())){
            			label4 = new Label(3, i, tra.getWorkPieceName());
            		}else{
            			label4 = new Label(3, i, "");
            		}
            		Label label5;
            		if(null!=tra.getBatchNumber()&&!"".equals(tra.getBatchNumber())){
            			label5 = new Label(4, i, tra.getBatchNumber());
            		}else{
            			label5 = new Label(4, i, "");
            		}
            		Label label6;
            		if(null!=tra.getStoveNumber()&&!"".equals(tra.getStoveNumber())){
            			label6 = new Label(5, i, tra.getStoveNumber());
            		}else{
            			label6 = new Label(5, i, "");
            		}
            		Label label7;
            		if(null!=tra.getProcessCode()&&!"".equals(tra.getProcessCode())){
            			label7 = new Label(6, i, tra.getProcessCode());
            		}else{
            			label7 = new Label(6, i, "");
            		}
            		Label label8;
            		if(null!=tra.getProcessName()&&!"".equals(tra.getProcessName())){
            			label8 = new Label(7, i, tra.getProcessName());
            		}else{
            			label8 = new Label(7, i, "");
            		}
            		Label label9;
            		if(null!=tra.getParameterCode()&&!"".equals(tra.getParameterCode())){
            			label9 = new Label(8, i, tra.getParameterCode());
            		}else{
            			label9 = new Label(8, i, "");
            		}
            		Label label10;
            		if(null!=tra.getParameterName()&&!"".equals(tra.getParameterName())){
            			label10 = new Label(9, i, tra.getParameterName());
            		}else{
            			label10 = new Label(9, i, "");
            		}
            		Label label11;
            		if(null!=tra.getUpLine()&&!"".equals(tra.getUpLine())){
            			label11 = new Label(10, i, tra.getUpLine().toString());
            		}else{
            			label11 = new Label(10, i, "");
            		}
            		Label label12;
            		if(null!=tra.getLowLine()&&!"".equals(tra.getLowLine())){
            			label12 = new Label(11, i, tra.getLowLine().toString());
            		}else{
            			label12 = new Label(11, i, "");
            		}
            		Label label13;
            		if(null!=tra.getStandardValue()&&!"".equals(tra.getStandardValue())){
            			label13 = new Label(12, i, tra.getStandardValue());
            		}else{
            			label13 = new Label(12, i, "");
            		}
            		Label label14;
            		if(null!=tra.getParameterValue()&&!"".equals(tra.getParameterValue())){
            			label14 = new Label(13, i, tra.getParameterValue());
            		}else{
            			label14 = new Label(13, i, "");
            		}
            		Label label15;
            		if(null!=tra.getStatus()&&!"".equals(tra.getStatus())){
            			label15 = new Label(14, i, tra.getStatus());
            		}else{
            			label15 = new Label(14, i, "");
            		}
            		Label label16;
            		if(null!=tra.getStatusCode()&&!"".equals(tra.getStatusCode())){
            			label16 = new Label(15, i, tra.getStatusCode());
            		}else{
            			label16 = new Label(15, i, "");
            		}
            		
            		sheet.addCell(label1); 
            		sheet.addCell(label2); 
            		sheet.addCell(label3); 
            		sheet.addCell(label4); 
            		sheet.addCell(label5); 
            		sheet.addCell(label6); 
            		sheet.addCell(label7); 
            		sheet.addCell(label8); 
            		sheet.addCell(label9); 
            		sheet.addCell(label10); 
            		sheet.addCell(label11); 
            		sheet.addCell(label12); 
            		sheet.addCell(label13); 
            		sheet.addCell(label14); 
            		sheet.addCell(label15); 
            		sheet.addCell(label16); 
            	}
            }
            book.write();  
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {  
				book.close();
			}catch (WriteException | IOException e) {
				 e.printStackTrace();
			}
		}
	}
}