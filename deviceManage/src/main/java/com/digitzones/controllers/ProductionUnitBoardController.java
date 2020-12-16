package com.digitzones.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.model.ProductionUnitBoard;
import com.digitzones.service.IProductionUnitBoardService;

/**
 * 生产单元(产线)和图片控制器
 * @author zdq
 * 2018年9月7日
 */
@RequestMapping("/productionUnitBoard")
@Controller
public class ProductionUnitBoardController {
	@Autowired
	private IProductionUnitBoardService productionUnitBoardService;
	/**
	 * 添加映射对象信息
	 * @param parameter
	 * @return
	 */
	@RequestMapping("/updateProductionUnitBoard.do")
	@ResponseBody
	public ModelMap updateProductionUnitBoard(ProductionUnitBoard p) {
		ModelMap modelMap = new ModelMap();
		ProductionUnitBoard productionUnitBoard = productionUnitBoardService.queryProductionUnitBoardByProductionUnitId(p.getProductionUnit().getId());
		if(productionUnitBoard==null){
			productionUnitBoardService.addObj(p);
		}else{
			if(p.getTitle_1()!=null){
				productionUnitBoard.setTitle_1(p.getTitle_1());
			}
			if(p.getTitle_2()!=null){
				productionUnitBoard.setTitle_2(p.getTitle_2());
			}
			if(p.getBody_1()!=null){
				productionUnitBoard.setBody_1(p.getBody_1());
			}
			if(p.getBody_2()!=null){
				productionUnitBoard.setBody_2(p.getBody_2());
			}
			productionUnitBoardService.updateObj(productionUnitBoard);
		}
		modelMap.addAttribute("success", true);
		modelMap.addAttribute("msg", "操作成功!");
		return modelMap;
	}

	/**
	 * 根据id查询映射对象信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/queryProductionUnitBoardByProductionUnitId.do")
	@ResponseBody
	public ProductionUnitBoard queryProductionUnitBoardByProductionUnitId(Long id) {
		ProductionUnitBoard c =  productionUnitBoardService.queryProductionUnitBoardByProductionUnitId(id);
		return c;
	}

	
	@RequestMapping("/uploadfile.do")
	@ResponseBody
	public ModelMap uploadfile(Part file,HttpServletRequest request) {
		String dir = request.getServletContext().getRealPath("/")+"front/imgs/";
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
			modelMap.addAttribute("filePath", "front/imgs/" + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return modelMap;
	}
}
