package com.digitzones.dao;

import java.util.List;

import com.digitzones.model.TraceParameter;
import com.digitzones.model.TracePositive;
import com.digitzones.model.TraceReverse;

/**
 * 角色操作数据访问接口
 * @author zdq
 * 2018年6月5日
 */
public interface ITraceDao {
	/**
	 * 反向追溯分页
	 * @param serialNo         二维码
	 * @param batchNumber      批号
	 * @param deviceSiteCode   设备站点
	 * @param start            开始
	 * @param end              结束
	 * @return
	 */
	public List<TraceReverse> findTracereverse (String serialNo,String batchNumber,String deviceSiteCode,Integer start,Integer end);
	/**
	 * 反向追溯
	 * @param serialNo         二维码
	 * @param batchNumber      批号
	 * @param deviceSiteCode   设备站点
	 * @return
	 */
	public List<TraceReverse> findTracereverse (String serialNo,String batchNumber,String deviceSiteCode);
	/**
	 * 
	 * @param serialNo         二维码
	 * @param batchNumber      批号
	 * @param deviceSiteCode   设备站点
	 * @return
	 */
	public int findTracereverseCount (String serialNo,String batchNumber,String deviceSiteCode);
	
	/**
	 * 正向追溯分页
	 * @param batchNumber     批号
	 * @param stoveNumber     材料编号
	 * @param deviceSiteCode  设备站点
	 * @param start           开始
	 * @param end             结束
	 * @return
	 */
	public List<TracePositive> findTracepositive (String batchNumber,String stoveNumber,String deviceSiteCode,String ng,Integer start,Integer end);
	/**
	 * 正向追溯
	 * @param batchNumber     批号
	 * @param stoveNumber     材料编号
	 * @param deviceSiteCode  设备站点
	 * @return
	 */
	public List<TracePositive> findTracepositive (String batchNumber,String stoveNumber,String deviceSiteCode,String ng);
	
	/**
	 * 
	 * @param batchNumber     批号
	 * @param stoveNumber     材料编号
	 * @param deviceSiteCode  设备站点
	 * @return
	 */
	public int findTracepositiveCount (String batchNumber,String stoveNumber,String deviceSiteCode ,String ng);

	/**
	 * 参数追溯分页
	 * @param parameterCode   参数代码
	 * @param parameterValue  标准值
	 * @param ValueUp         上偏差值
	 * @param ValueDown       下偏差值
	 * @param workPieceName   工件名称
	 * @param start           开始
	 * @param end             结束
	 * @return
	 */
	public List<TraceParameter> findTraceparameter (String parameterCode,String worksheetNo,String minValue,String maxValue, Integer start, Integer end);
	/**
	 * 参数追溯
	 * @param parameterCode   参数代码
	 * @param parameterValue  标准值
	 * @param ValueUp         上偏差值
	 * @param ValueDown       下偏差值
	 * @param workPieceName   工件名称
	 * @param start           开始
	 * @param end             结束
	 * @return
	 */
	public List<TraceParameter> findTraceparameter (String parameterCode, String worksheetNo, String batchNum,
			String minValue, String maxValue);
	
	/**
	 * 
	 * @param parameterCode   参数代码
	 * @param parameterValue  标准值
	 * @param ValueUp         上偏差值
	 * @param ValueDown       下偏差值
	 * @param workPieceName   工件名称
	 * @return
	 */
	public int findTraceparameterCount (String parameterCode,String worksheetNo,String batchNum,String minValue,String maxValue);
}
