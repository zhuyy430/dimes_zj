package com.digitzones.procurement.service;
import com.digitzones.procurement.model.CraftsRoute;
import com.digitzones.service.ICommonService;
/**
 * 工艺路线service
 * @author zhuyy430
 *
 */
public interface ICraftsRouteService extends ICommonService<CraftsRoute> {
	
	public void deleteObjById(String id);
}
