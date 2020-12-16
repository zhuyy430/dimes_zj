package com.digitzones.mc.service;

import java.util.List;

import com.digitzones.model.LostTimeRecord;

public interface IMCLostTimeService {
	/**
	 * MC端根据设备站点代码查找损时记录
	 */
	
	public List<LostTimeRecord> findLostTimelist(String hql,List<Object> param);
	/**
	 * MC端根据设备站点代码查找所有总损时时长
	 */
	
	public Double getAllLostTimelist(String untreated,String notSure,String historicalRecords,Long deviceSiteid);
	/**
	 * MC端根据设备站点代码查找所有损时数量
	 */
	
	public int getCountLostTimelist(String untreated,String notSure,String historicalRecords,Long deviceSiteid);
}
