package com.digitzones.dao;

import com.digitzones.model.NGReason;

import java.util.List;

/**
 * 不良原因
 * @author zdq
 * 2018年6月13日
 */
public interface INGReasonDao extends ICommonDao<NGReason> {
    /**
     * 查找所有不良原因大类
     * @return
     */
   public List<String> queryAllCategories();
}
