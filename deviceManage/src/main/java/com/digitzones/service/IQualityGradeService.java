package com.digitzones.service;

import java.util.List;

import com.digitzones.model.QualityGrade;
/**
 * 质量等级service
 * @author zdq
 * 2018年7月19日
 */
public interface IQualityGradeService extends ICommonService<QualityGrade> {
	/**
	 * 查找所有质量等级
	 * @return
	 */
	public List<QualityGrade> queryAllQualityGrades();
}
