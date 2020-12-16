package com.digitzones.service;

import java.util.List;

import com.digitzones.model.Pager;
import com.digitzones.model.WorkpieceProcessParameterMapping;
/**
 * 工件工序站点service
 * @author zdq
 * 2018年6月15日
 */
public interface IWorkpieceProcessParameterMappingService extends ICommonService<WorkpieceProcessParameterMapping> {
	/**
	 * 根据工件code查询'工件工序参数'
	 * @param workpieceCode
	 * @param rows
	 * @param page
	 * @return
	 */
	public Pager<WorkpieceProcessParameterMapping> queryWorkpieceProcessParameterMappingByWorkpieceCode(String workpieceCode,Integer rows,Integer page);
	/**
	 * 根据工件code查询'工件工序参数'
	 */
	public List<WorkpieceProcessParameterMapping> queryAllWorkpieceProcessParameterMappingByWorkpieceCode(String workpieceCode);
	/**
	 * 根据工件code查询'工件工序参数'
	 */
	public List<WorkpieceProcessParameterMapping> queryAllWorkpieceProcessParameterMappingByWorkpieceCodeAndProcessCode(String workpieceCode,String processCode);
	/**
	 * 根据工件编码和工序编码查找'工件工序参数关联对象'
	 * @param workpieceCode
	 * @param processCode
	 * @return
	 */
	public List<WorkpieceProcessParameterMapping> queryByWorkpieceCodeAndProcessCode(String workpieceCode,String processCode);
	/**
	 * 根据工单编号和工序代码查找工件工序参数信息
	 * @param no
	 * @param processCode
	 * @return
	 */
	public List<WorkpieceProcessParameterMapping> queryByNoAndProcessCode(String no, String processCode);

	/**
	 * 根据工件代码，工序代码和参数代码查找
	 * @param workpieceCode
	 * @param code
	 * @param parameterCode
	 * @return
	 */
    WorkpieceProcessParameterMapping queryByWorkpieceCodeAndProcessCodeAndParameterCode(String workpieceCode, String code, String parameterCode);
}
