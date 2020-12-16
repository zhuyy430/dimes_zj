package com.digitzones.service.impl;

import com.digitzones.dao.IPackageCodeAndBoxBarMappingDao;
import com.digitzones.dao.IPackageCodeDao;
import com.digitzones.model.PackageCode;
import com.digitzones.model.PackageCodeAndBoxBarMapping;
import com.digitzones.model.Pager;
import com.digitzones.model.User;
import com.digitzones.procurement.model.BoxBar;
import com.digitzones.procurement.service.IBoxBarService;
import com.digitzones.service.IPackageCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Service
public class PackageCodeServiceImpl implements IPackageCodeService {
	@Autowired
	private IPackageCodeDao packageCodeDao;
	@Autowired
	private IBoxBarService boxBarService;
	@Autowired
	private IPackageCodeAndBoxBarMappingDao packageCodeAndBoxBarMappingDao;
	@Override
	public Pager<PackageCode> queryObjs(String hql, int pageNo, int pageSize, Object... values) {
		return packageCodeDao.findByPage(hql, pageNo, pageSize, values);
	}

	@Override
	public void updateObj(PackageCode obj) {
		packageCodeDao.update(obj);
	}

	@Override
	public PackageCode queryByProperty(String name, String value) {
		return packageCodeDao.findSingleByProperty(name, value);
	}

	@Override
	public Serializable addObj(PackageCode obj) {
		return packageCodeDao.save(obj);
	}

	@Override
	public PackageCode queryObjById(Serializable id) {
		return packageCodeDao.findById(id);
	}

	@Override
	public void deleteObj(Serializable id) {
		PackageCode packageCode = packageCodeDao.findById(id);
		packageCodeAndBoxBarMappingDao.deleteByPackageCode(packageCode.getCode());
		packageCodeDao.deleteById(id);
	}

	@Override
	public PackageCode queryMaxPackageCode() {
		List<PackageCode> plist = packageCodeDao.findByHQL("from PackageCode order by id DESC ", new Object[]{});
		if(CollectionUtils.isEmpty(plist))
			return null;
		else
			return plist.get(0);
	}

	@Override
	public List<PackageCode> queryPackageCodeByIds(List<Long> ids) {
		return packageCodeDao.findByHQL("from PackageCode p where p.id in(?0) ", new Object[]{ids});
	}

	/**
	 * 根据包装条码查找BoxBar
	 *
	 * @param packageCode
	 * @return
	 */
	@Override
	public List<BoxBar> queryBoxBarByPackageCode(String packageCode) {
		return packageCodeDao.queryBoxBarByPackageCode(packageCode);
	}

	@Override
	public void outWarehouse(String packNo,String[] barCodes, String[] ramounts, User user) {
		for(int i=0;i<barCodes.length;i++){
			BoxBar b=boxBarService.queryBoxBarBybarCode(Long.valueOf(barCodes[i]));
			if(b.getPackingNum()<Double.parseDouble(ramounts[i])){
				throw new RuntimeException(b.getBarCode()+"箱可包装余量改变,请修改后重新提交");
			}
			b.setPackingNum(b.getPackingNum()-Double.valueOf(ramounts[i]));
			PackageCodeAndBoxBarMapping p=new PackageCodeAndBoxBarMapping();
			PackageCode packageCode=queryByProperty("code",packNo);
			if(packageCode!=null){
				p.setCustomerProvisionPackageCode(false);
			}else{
				p.setCustomerProvisionPackageCode(true);
			}
			p.setBoxBar(b);
			if(user.getEmployee()!=null) {
				p.setEmployeeCode(user.getEmployee().getCode());
				p.setEmployeeName(user.getEmployee().getName());
			}else{
				p.setEmployeeCode(user.getId()+"");
				p.setEmployeeName(user.getUsername());
			}
			p.setNumber(Double.valueOf(ramounts[i]));
			p.setPackageCode(packNo);
			p.setTakeOutTime(new Date());
			packageCodeAndBoxBarMappingDao.save(p);
			boxBarService.updateObj(b);
		}
	}
}
