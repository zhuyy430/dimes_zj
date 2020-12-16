package com.digitzones.procurement.service;
import com.digitzones.procurement.model.PersonProductionUnitMapping;
import com.digitzones.service.ICommonService;
/**
 * 物料service
 * @author zhuyy430
 *
 */
public interface IPersonProductionUnitMappingService extends ICommonService<PersonProductionUnitMapping> {
	/**
	 * 根据员工code查询
	 * @param code
	 * @return
	 */
	public PersonProductionUnitMapping queryByPersonCode(String code);
}
