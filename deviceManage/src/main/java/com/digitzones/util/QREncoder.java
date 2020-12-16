package com.digitzones.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
/**
 * 生成二维码
 * @author zdq
 * 2018年7月16日
 */
public class QREncoder {
	/**宽度*/
	private int width = 300;
	/**高度*/
	private int height = 300;
	/**扩展名*/
	private String ext = "png";
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	
	public QREncoder() {}
	
	public QREncoder(int width, int height, String ext) {
		this(width,height);
		this.ext = ext;
	}
	public QREncoder(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public QREncoder(String ext) {
		this.ext = ext;
	}
	/**
	 * 生成二维码
	 * @param code 编码
	 * @return 二维码图片路径
	 */
	public  String generatePR(String code,String basePath ,String qrPath) {
	        String fileName = UUID.randomUUID() + "." + ext;  
	        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  
	        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
	        hints.put(EncodeHintType.MARGIN, 2);
	        BitMatrix bitMatrix;
			try {
				bitMatrix = new MultiFormatWriter().encode(code,  
				        BarcodeFormat.QR_CODE, width, height, hints);
				// 生成矩阵  
		       // bitMatrix.setRegion(2,2,2,2);
		        File qrPathFile = new File(basePath + qrPath);
		        if(!qrPathFile.exists()) {
		        	qrPathFile.mkdirs();
		        }
		        Path path = FileSystems.getDefault().getPath(basePath + qrPath, fileName);  
		        MatrixToImageWriter.writeToPath(bitMatrix, ext, path);// 输出图像  
		        return qrPath + "/"+fileName;
			} catch (WriterException | IOException e) {
				e.printStackTrace();
			}
			return "";
	}
	/**
	 * 生成工单二维码
	 * @param content 工单单号
	 * @return 二维码图片路径
	 */
	public  String generateQR(String content,String basePath ,String qrPath) {
	        String fileName = content + "." + ext;
	        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
	        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
	        hints.put(EncodeHintType.MARGIN, 2);
	        BitMatrix bitMatrix;
			try {
				bitMatrix = new MultiFormatWriter().encode(content,
				        BarcodeFormat.QR_CODE, width, height, hints);
				// 生成矩阵
		       // bitMatrix.setRegion(2,2,2,2);
		        File qrPathFile = new File(basePath + qrPath);
		        if(!qrPathFile.exists()) {
		        	qrPathFile.mkdirs();
		        }
		        File file = new File(qrPathFile,fileName);
		        if(!file.exists()){
					Path path = FileSystems.getDefault().getPath(basePath + qrPath, fileName);
					MatrixToImageWriter.writeToPath(bitMatrix, ext, path);// 输出图像
				}
		        return qrPath + "/"+fileName;
			} catch (WriterException | IOException e) {
				e.printStackTrace();
			}
			return "";
	}
}

