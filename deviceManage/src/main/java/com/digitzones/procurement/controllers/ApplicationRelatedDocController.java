package com.digitzones.procurement.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.digitzones.model.Pager;
import com.digitzones.procurement.model.ApplicationRelatedDocument;
import com.digitzones.procurement.service.IApplicationRelatedDocumentService;

/**
 * 相关文档
 * @author zdq
 * 2018年12月5日
 */
@Controller
@RequestMapping("/applicationRelatedDoc")
public class ApplicationRelatedDocController {
	@Autowired
	private IApplicationRelatedDocumentService applicationRelatedDocumentService;
	private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 下载文档
	 * @param id
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	@RequestMapping("/download.do")
	@ResponseBody
	public ResponseEntity<byte[]> download(Long id,HttpServletRequest request) throws UnsupportedEncodingException {
		ApplicationRelatedDocument doc = applicationRelatedDocumentService.queryObjById(Long.valueOf(id));
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
	 * 上传文档
	 * @param type
	 * @return
	 */
	@RequestMapping("/upload.do")
	@ResponseBody
	public ModelMap upload(@RequestParam("file")MultipartFile file,HttpServletRequest request,ApplicationRelatedDocument doc,Principal principal) {
		ModelMap modelMap = new ModelMap();
		//获取文件名称
		String filename = file.getOriginalFilename();
		String[] subName = filename.split("\\.");
		String prefix = "";
		if(subName!=null && subName.length>0) {
			prefix = subName[subName.length-1];
		}
		String newName = format.format(new Date()) + (StringUtils.isEmpty(prefix)?"":("."+prefix));
		//获取存储目录
		String dir = "procurement/doc";
		File dirFile = new File(request.getServletContext().getRealPath("/") + dir);
		if(!dirFile.exists()) {
			dirFile.mkdirs();
		}
		try {
			saveFile(file.getInputStream(), new File(dirFile,newName));
		} catch (IOException e) {
			e.printStackTrace();
			modelMap.addAttribute("success",false);
			modelMap.addAttribute("title","错误提示");
			modelMap.addAttribute("message","文件上传异常!");
			return modelMap;
		}
		String username = principal.getName();
		ApplicationRelatedDocument document = new ApplicationRelatedDocument();
		document.setContentType(file.getContentType());
		document.setFileSize(file.getSize());
		document.setSrcName(filename);
		document.setUploadDate(new Date());
		document.setRelatedId(doc.getRelatedId());
		document.setUploadUsername(username);
		document.setUrl(dir +"/"+ newName);
		document.setName(StringUtils.isEmpty(doc.getName())?filename:doc.getName());
		document.setNote(doc.getNote());
		applicationRelatedDocumentService.addObj(document);
		modelMap.addAttribute("success",true);
		modelMap.addAttribute("title","消息提示");
		modelMap.addAttribute("message","文件上传成功!");
		return modelMap;
	}
	@RequestMapping("/uploadfile.do")
	@ResponseBody
	public ModelMap uploadfile(Part file,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/")+"console/deviceImgs/";
		String realName = file.getSubmittedFileName();
		ModelMap modelMap = new ModelMap();
		String fileName = new Date().getTime()+ realName.substring(realName.lastIndexOf("."));
		InputStream is;
		try {
			is = file.getInputStream();
			File parentDir = new File(dir);
			if(!parentDir.exists()) {
				parentDir.mkdirs();
			}
			File out = new File(parentDir,fileName);
			FileCopyUtils.copy(is, new FileOutputStream(out));
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "文件上传成功！");
			modelMap.addAttribute("filePath", "console/deviceImgs/" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	@RequestMapping("/queryDocsByRelatedId.do")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public ModelMap queryDocsByRelatedId(String id,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<ApplicationRelatedDocument> pager = new Pager<ApplicationRelatedDocument>();
		pager = applicationRelatedDocumentService.queryObjs("from ApplicationRelatedDocument rd where rd.relatedId=?0", page, rows,new Object[] {id});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}
	
	@RequestMapping("/queryDocsByRelatedIdAndTypeCode.do")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public ModelMap queryDocsByRelatedIdAndTypeCode(Long recordId,String moduleCode,@RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<ApplicationRelatedDocument> pager = new Pager<ApplicationRelatedDocument>();
		pager = applicationRelatedDocumentService.queryObjs("from ApplicationRelatedDocument rd where rd.relatedDocumentType.code=?0 and rd.relatedId=?1", page, rows,new Object[] {moduleCode,recordId});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}
	/**
	 * 根据id 查找相关文档
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryById.do")
	@ResponseBody
	public ApplicationRelatedDocument queryById(Long id) {
		return applicationRelatedDocumentService.queryObjById(id);
	}
	/**
	 * 更新相关文档对象
	 * @param relatedDocument
	 * @return
	 */
	@RequestMapping("/updateRelatedDocument.do")
	@ResponseBody
	public ModelMap updateRelatedDocument(ApplicationRelatedDocument applicationRelatedDocument) {
		ModelMap modelMap = new ModelMap();
		ApplicationRelatedDocument doc = applicationRelatedDocumentService.queryObjById(applicationRelatedDocument.getId());
		doc.setName(applicationRelatedDocument.getName());
		doc.setNote(applicationRelatedDocument.getNote());
		applicationRelatedDocumentService.updateObj(doc);
		modelMap.addAttribute("msg","更新成功!");
		modelMap.addAttribute("success",true);
		return modelMap;
	}
	/**
	 * 删除相关文档对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteRelatedDocument.do")
	@ResponseBody
	public ModelMap deleteRelatedDocument(String id) {
		ModelMap modelMap = new ModelMap();
		if(id!=null && id.contains("'")) {
			id = id.replace("'", "");
		}
		ApplicationRelatedDocument doc = applicationRelatedDocumentService.queryObjById(Long.valueOf(id));
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
		applicationRelatedDocumentService.deleteObj(Long.valueOf(id));
		modelMap.addAttribute("statusCode",200);
		modelMap.addAttribute("title","消息提示");
		modelMap.addAttribute("message","成功删除!");
		return modelMap;
	}
	@RequestMapping("/uploadimg.do")
	@ResponseBody
	public ModelMap uploadimg(@RequestParam("file")Part file,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/")+"console/deviceImgs/";
		ModelMap modelMap = new ModelMap();
		String realName = file.getSubmittedFileName();
		String fileName = new Date().getTime()+ realName.substring(realName.lastIndexOf("."));
		InputStream is;
		try {
			is = file.getInputStream();
			File parentDir = new File(dir);
			if(!parentDir.exists()) {
				parentDir.mkdirs();
			}
			File out = new File(parentDir,fileName);
			FileCopyUtils.copy(is, new FileOutputStream(out));
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "文件上传成功！");
			modelMap.addAttribute("filePath", "console/deviceImgs/" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modelMap;
	}
	/**
	 * 查询相关文档对象
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryRelatedDocumentByDetailId.do")
	@ResponseBody
	public List<ApplicationRelatedDocument> queryApplicationRelatedDocumentByDetailId(String id) {
		return applicationRelatedDocumentService.queryApplicationRelatedDocumentByDetailId(id);
	}
	/**
	 * 下载模板
	 * @param id
	 * @return
		 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	@RequestMapping("/downloadTemplates.do")
	public ResponseEntity<byte[]> downloadTemplates(String name ,HttpServletRequest request) throws UnsupportedEncodingException {
		File file = new File(request.getServletContext().getRealPath("/") + "templates/"+name+".xlsx");
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
	 * 导入模板
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping("/uploadTemplates.do")
	@ResponseBody
	public ModelMap uploadTemplates(@RequestParam("file")Part file,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/")+"console/deviceImgs/";
		ModelMap modelMap = new ModelMap();
		String realName = file.getSubmittedFileName();
		String fileName = new Date().getTime()+ realName.substring(realName.lastIndexOf("."));
		InputStream is;
		try {
			is = file.getInputStream();
			File parentDir = new File(dir);
			if(!parentDir.exists()) {
				parentDir.mkdirs();
			}
			File out = new File(parentDir,fileName);
			FileCopyUtils.copy(is, new FileOutputStream(out));
			modelMap.addAttribute("statusCode", 200);
			modelMap.addAttribute("title", "操作提示");
			modelMap.addAttribute("message", "文件上传成功！");
			modelMap.addAttribute("filePath", "console/deviceImgs/" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modelMap;
	}
	@RequestMapping("/queryDocsByRelatedIdAndDocsTypeCodeAndModuleCode.do")
	@SuppressWarnings("unchecked")
	@ResponseBody
	public ModelMap queryDocsByRelatedIdAndDocsTypeCodeAndModuleCode(Long relatedId,String moduleCode,String docTypeCode, @RequestParam(value="rows",defaultValue="20")Integer rows,@RequestParam(defaultValue="1")Integer page) {
		Pager<ApplicationRelatedDocument> pager = new Pager<ApplicationRelatedDocument>();
		pager = applicationRelatedDocumentService.queryObjs("from ApplicationRelatedDocument doc where doc.relatedDocumentType.moduleCode=?0 and doc.relatedDocumentType.code=?1 and doc.relatedId=?2", page, rows,new Object[] {moduleCode,docTypeCode,relatedId});
		ModelMap mm = new ModelMap();
		mm.addAttribute("rows",pager.getData());
		mm.addAttribute("total", pager.getTotalCount());
		return mm;
	}
}
