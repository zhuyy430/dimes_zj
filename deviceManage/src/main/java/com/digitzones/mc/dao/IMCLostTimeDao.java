package com.digitzones.mc.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.model.LostTimeRecord;


public interface IMCLostTimeDao extends ICommonDao<LostTimeRecord>{
	
	/**
	 * MC端根据设备站点代码查找损时记录
	 */
	
	public List<LostTimeRecord> findLostTimelist(String hql);
	/**
	 * MC端根据设备站点代码查找总损时时长
	 */
	
	public Double getAllLostTimelist(String untreated, String notSure, String historicalRecords, Long deviceSiteid);
	/**
	 * MC端根据设备站点代码查找损时数量
	 */
	
	public int getCountLostTimelist(String untreated, String notSure, String historicalRecords, Long deviceSiteid);
}
