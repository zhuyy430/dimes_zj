package com.digitzones.devmgr.dao;

import java.util.List;

import com.digitzones.dao.ICommonDao;
import com.digitzones.devmgr.model.Sparepart;
/**
 * 备品备件dao
 * @author Administrator
 */
public interface ISparepartDao extends ICommonDao<Sparepart> {
	
	/**
	 * 根据设备代码获取备品备件信息
	 * @param code
	 * @return
	 */
	public List<Sparepart> querySparepartsByDeviceCode(String code);
	/**
	 * 根据设备代码获取备品备件信息
	 * @param code
	 * @return
	 */
	public List<Sparepart> querySparepartsByDeviceCode(String code,Long devicerepairId);

}
