package com.digitzones.dao;

import java.util.List;
import java.util.Map;

import com.digitzones.model.Pager;
/**
 * 报表汇总dao
 */
public interface IReportDao {
	
	
	/**
	 * 损时时间汇总表
	 * @param params
	 * @return
	 */
	public Pager<List<Object[]>> queryLostTimeCountReport(Map<String,String> params,int rows,int page);
	/**
	 * 产量批次汇总表
	 * @param params
	 * @return
	 */
	public Pager<List<Object[]>> queryOutputBatchCountReport(Map<String,String> params,int rows,int page);
	/**
	 * 生产单元产量汇总表
	 * @param params
	 * @return
	 */
	public Pager<List<Object[]>> queryProductionUnitOutputCountReport(Map<String,String> params,int rows,int page);
}
