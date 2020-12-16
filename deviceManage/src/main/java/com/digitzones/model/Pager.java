package com.digitzones.model;

import java.util.ArrayList;
import java.util.List;
/**
 * 分页实体
 * @author zdq
 * 2018年6月5日
 * @param <T>
 */
public class Pager<T> {
	   private static int DEFAULT_PAGE_SIZED=20;
	    private int start;  //开始位置
	    private int pageSize=DEFAULT_PAGE_SIZED;//一页显示的个数
	    private int totalCount;//总记录数
	    private List<T> data;//当前页存放的记录

	    public Pager(){
	        this(0,DEFAULT_PAGE_SIZED,0,new ArrayList<T>());
	    }

	    public Pager(int start, int pageSize, int totalCount, List<T> data) {
	        this.start = start;
	        this.pageSize = pageSize;
	        this.totalCount = totalCount;
	        this.data = data;
	    }
	    /**
	     * 取总页数
	     */
	    public long getTotalPageCount(){
	        if(totalCount%pageSize==0)
	            return totalCount/pageSize;
	        else
	            return totalCount/pageSize+1;
	    }
	    /**
	     *获取当前页
	     */
	    public long getCurrentPageNo(){
	        return start/pageSize+1;
	    }

	    /**
	     *是否有下一页
	     */
	    public boolean isHasNextPage(){
	        return this.getCurrentPageNo()<this.getTotalPageCount();
	    }

	    /**
	     *是否有上一页
	     */
	    public boolean isHasPreviousPage(){
	        return this.getCurrentPageNo()>1;
	    }

	    /**
	     *
	     * @param pageNo    页数
	     * @param pageSize  一页显示记录数
	     * @return           该页第一条数据位置
	     */
	    public   static int getStartOfPage(int pageNo,int pageSize){
	        return (pageNo-1)*pageSize;
	    }

	    /**
	     * 任意一页第一条数据在数据集的位置
	     */
	    protected  static int getStartOfPage(int pageNo){
	        return getStartOfPage(pageNo,DEFAULT_PAGE_SIZED);
	    }

	    public int getStart() {
	        return start;
	    }

	    public void setStart(int start) {
	        this.start = start;
	    }

	    public int getPageSize() {
	        return pageSize;
	    }

	    public void setPageSize(int pageSize) {
	        this.pageSize = pageSize;
	    }

	    public int getTotalCount() {
	        return totalCount;
	    }

	    public void setTotalCount(int totalCount) {
	        this.totalCount = totalCount;
	    }

	    public List<T> getData() {
	        return data;
	    }

	    public void setData(List<T> data) {
	        this.data = data;
	    }
}
