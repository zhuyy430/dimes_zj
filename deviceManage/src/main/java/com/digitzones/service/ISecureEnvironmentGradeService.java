package com.digitzones.service;

import java.util.List;

import com.digitzones.model.SecureEnvironmentGrade;
/**
 * 安环等级service
 * @author zdq
 * 2018年7月19日
 */
public interface ISecureEnvironmentGradeService extends ICommonService<SecureEnvironmentGrade> {
	/**
	 * 查找所有安环等级
	 * @return
	 */
	public List<SecureEnvironmentGrade> queryAllSecureEnvironmentGrades();
}
