package com.digitzones.service;

import java.util.List;

import com.digitzones.model.TraceParameter;
import com.digitzones.model.TracePositive;
import com.digitzones.model.TraceReverse;

/**
 * 角色管理service
 * @author zdq
 * 2018年6月11日
 */
public interface ITraceService {
	
		/**
		 * 反向追溯分页
		 * @param serialNo         二维码
		 * @param batchNumber      批号
		 * @param deviceSiteCode   设备站点
		 * @param start            开始
		 * @param end              结束
		 * @return
		 */
		public List<TraceReverse> queryTracereverse (String serialNo,String batchNumber,String deviceSiteCode,Integer start,Integer end);
		/**
		 * 反向追溯
		 * @param serialNo         二维码
		 * @param batchNumber      批号
		 * @param deviceSiteCode   设备站点
		 * @param start            开始
		 * @param end              结束
		 * @return
		 */
		public List<TraceReverse> queryTracereverse (String serialNo,String batchNumber,String deviceSiteCode);
		/**
		 * 
		 * @param serialNo         二维码
		 * @param batchNumber      批号
		 * @param deviceSiteCode   设备站点
		 * @return
		 */
		public int getTracereverseCount (String serialNo,String batchNumber,String deviceSiteCode);
		
		/**
		 * 正向追溯分页
		 * @param batchNumber     批号
		 * @param stoveNumber     材料编号
		 * @param deviceSiteCode  设备站点
		 * @param start           开始
		 * @param end             结束
		 * @return
		 */
		public List<TracePositive> queryTracepositive (String batchNumber,String stoveNumber,String deviceSiteCode,String ng,Integer start,Integer end);
		/**
		 * 正向追溯
		 * @param batchNumber     批号
		 * @param stoveNumber     材料编号
		 * @param deviceSiteCode  设备站点
		 * @param start           开始
		 * @param end             结束
		 * @return
		 */
		public List<TracePositive> queryTracepositive (String batchNumber,String stoveNumber,String deviceSiteCode,String ng);
		
		/**
		 * 
		 * @param batchNumber     批号
		 * @param stoveNumber     材料编号
		 * @param deviceSiteCode  设备站点
		 * @return
		 */
		public int getTracepositiveCount (String batchNumber,String stoveNumber,String deviceSiteCode, String ng);

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
		public List<TraceParameter> queryTraceparameter (String parameterCode,String worksheetNo,String minValue,String maxValue,Integer start,Integer end);
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
		public List<TraceParameter> queryTraceparameter (String parameterCode,String parameterValue,String ValueUp,String ValueDown,String workPieceName);
		
		/**
		 * 
		 * @param parameterCode   参数代码
		 * @param parameterValue  标准值
		 * @param ValueUp         上偏差值
		 * @param ValueDown       下偏差值
		 * @param workPieceName   工件名称
		 * @return
		 */
		public int getTraceparameterCount (String parameterCode,String worksheetNo,String batchNum,String minValue,String maxValue);
}
