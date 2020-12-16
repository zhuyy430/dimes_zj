package com.digitzones.devmgr.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.digitzones.model.Pager;
import com.digitzones.model.RelatedDocument;
import com.digitzones.devmgr.model.MaintenanceRelatedDocument;
import com.digitzones.devmgr.service.IMaintenanceRelatedDocumentService;

/**
 * 维修单文档
 * @author Administrator
 *
 */
@RequestMapping("/maintenanceRelatedDocument")
@Controller
public class MaintenanceRelatedDocumentController {
	@Autowired
	private IMaintenanceRelatedDocumentService maintenanceRelatedDocumentService;
	
	
	/**
	 * 根据维修记录id查询维修单文档
	 * @param recordId
	 * @param rows
	 * @param page
	 * @return
	 */
	@RequestMapping("/queryMaintenanceRelatedDocumentByRecordId.do")
	@ResponseBody
	public ModelMap queryMaintenanceRelatedDocumentByRecordId(Long deviceRepairOrderId,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page){
		Pager<MaintenanceRelatedDocument> pager=new Pager<MaintenanceRelatedDocument>();
		String hql="from MaintenanceRelatedDocument m where m.maintenanceStaffRecord.id=?0";
		pager = maintenanceRelatedDocumentService.queryObjs(hql, page, rows, new Object[] {deviceRepairOrderId});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}
	
	/**
	 * 上传文档
	 */
	@RequestMapping("/upload.do")
	@ResponseBody
	public ModelMap upload(@RequestParam("file")MultipartFile file,HttpServletRequest request,MaintenanceRelatedDocument doc,Principal principal) {
		ModelMap modelMap = new ModelMap();
		//获取文件名称
		String filename = file.getOriginalFilename();
		//根据文件名称和type查找是否已 存在该文件
		if(maintenanceRelatedDocumentService.isExistFile(filename, doc.getMaintenanceStaffRecord().getId())) {
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("title","错误提示");
			modelMap.addAttribute("message","该文件已存在!");
			return modelMap;
		}
		//获取存储目录
		String dir = "MaintenanceRelatedDocument";
		File dirFile = new File(request.getServletContext().getRealPath("/") + dir);
		if(!dirFile.exists()) {
			dirFile.mkdirs();
		}
		try {
			saveFile(file.getInputStream(), new File(dirFile,filename));
		} catch (IOException e) {
			e.printStackTrace();
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("title","错误提示");
			modelMap.addAttribute("message","文件上传异常!");
			return modelMap;
		}
		
		String username = principal.getName();

		MaintenanceRelatedDocument document = new MaintenanceRelatedDocument();
		document.setContentType(file.getContentType());
		document.setFileSize(file.getSize());
		document.setSrcName(filename);
		document.setUploadDate(new Date());
		document.setMaintenanceStaffRecord(doc.getMaintenanceStaffRecord());
		document.setRelatedId(doc.getRelatedId());
		document.setUploadUsername(username);
		document.setUrl(dir +"/"+ filename);
		document.setName(StringUtils.isEmpty(doc.getName())?filename:doc.getName());
		document.setNote(doc.getNote());
		maintenanceRelatedDocumentService.addObj(document);
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("title","消息提示");
		modelMap.addAttribute("message","文件上传成功!");
		return modelMap;
		
	}
	/**
	 * 保存文件
	 * @param is 文件输入流
	 * @param path 存储路径
	 * @throws IOException 
	 */
	private void saveFile(InputStream is,File desc) throws IOException {
		FileUtils.copyInputStreamToFile(is, desc);
	}
	
	
	/**
	 * 下载文档
	 * @param id
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	@RequestMapping("/download.do")
	public ResponseEntity<byte[]> download(Long id,HttpServletRequest request) throws UnsupportedEncodingException {
		MaintenanceRelatedDocument doc = maintenanceRelatedDocumentService.queryObjById(Long.valueOf(id));
		File file = new File(request.getServletContext().getRealPath("/") + doc.getUrl());
		String filename = new String(file.getName().getBytes("UTF-8"),"iso8859-1");
		byte[] body = null;
		//获取文件
		InputStream is = null;
		//返回数据
		ResponseEntity<byte[]> entity = null;
		HttpHeaders headers = new HttpHeaders();
		try {
			headers.setContentDispositionFormData("attachment", filename); 
			headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			if(!file.exists()) {
				headers.add("Content-Length", "" + 0); 
				entity = new ResponseEntity<byte[]>(body, headers,HttpStatus.NOT_FOUND);
				return entity;
			}
			is = new FileInputStream(file);
			int available = is.available();
			
			body = new byte[available];
			is.read(body);
			headers.add("Content-Length", "" + body.length); 
			HttpStatus statusCode = HttpStatus.OK;
			entity = new ResponseEntity<byte[]>(body, headers, statusCode);
		} catch ( IOException e) {
			e.printStackTrace();
		}finally {
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return entity;
	}
	
	
	/**
	 * 删除相关文档对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteMaintenanceRelatedDocument.do")
	@ResponseBody
	public ModelMap deleteMaintenanceRelatedDocument(String id) {
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}

		MaintenanceRelatedDocument doc = maintenanceRelatedDocumentService.queryObjById(Long.valueOf(id));
		try {
			if(doc.getUrl()!=null) {
				File file = new File(doc.getUrl());
				if(file.exists()) {
					file.delete();
				}
			}
		}catch (RuntimeException e) {
			e.printStackTrace();
			modelMap.addAttribute("statusCode",300);
			modelMap.addAttribute("title","错误提示");
			modelMap.addAttribute("message","文件删除失败!");
			return modelMap;
		}
		maintenanceRelatedDocumentService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("statusCode",200);
		modelMap.addAttribute("title","消息提示");
		modelMap.addAttribute("message","成功删除!");
		return modelMap;
	}
}
