package com.digitzones.dao.impl;
import org.springframework.stereotype.Repository;
import com.digitzones.dao.IQualityGradeDao;
import com.digitzones.model.QualityGrade;
@Repository
public class QualityGradeDaoImpl extends CommonDaoImpl<QualityGrade> implements IQualityGradeDao {
	public QualityGradeDaoImpl() {
		super(QualityGrade.class);
	}
}
