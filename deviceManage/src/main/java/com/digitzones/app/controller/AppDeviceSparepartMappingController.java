package com.digitzones.app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digitzones.app.vo.DeviceSparepartMappingVO;
import com.digitzones.devmgr.model.DeviceSparepartMapping;
import com.digitzones.devmgr.service.IDeviceSparepartMappingService;
import com.digitzones.model.Pager;

@Controller
@RequestMapping("/AppDeviceSparepartMapping")
public class AppDeviceSparepartMappingController {
	
	@Autowired
	private IDeviceSparepartMappingService deviceSparepartMappingService;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 根据设备code查找设备和备品备件关联实体
	 * 
	 */
	@RequestMapping("/queryDeviceSparepartMappingByDeviceId.do")
	@ResponseBody
	public List<DeviceSparepartMappingVO> queryDeviceSparepartMappingByDeviceId(Long deviceId) {
		List<DeviceSparepartMapping> dsmlist=deviceSparepartMappingService.queryDeviceSparepartMappingByDeviceId(deviceId);
		List<DeviceSparepartMappingVO> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dsmlist)) {
			for(DeviceSparepartMapping dsm:dsmlist) {
				list.add(model2vo(dsm));
			}
		}
		return list;
	}
	
	/**model 转vo*/
	public DeviceSparepartMappingVO model2vo(DeviceSparepartMapping deviceSparepartMapping) {
		if(deviceSparepartMapping==null) {
			return null;
		}
		DeviceSparepartMappingVO vo=new DeviceSparepartMappingVO();
		vo.setId(deviceSparepartMapping.getId());
		vo.setDevice(deviceSparepartMapping.getDevice());
		vo.setSparepart(deviceSparepartMapping.getSparepart());
		if(deviceSparepartMapping.getLastUseDate()!=null) {
			vo.setLastUseDate(sdf.format(deviceSparepartMapping.getLastUseDate()));
		}else {
			vo.setLastUseDate("");
		}
		
		return vo;
	}
}
