package com.sz.lc.page;

import org.springframework.stereotype.Component;

/*
 * 分页类封装
 */
@Component
public class Page {
	
	/**
	 * @return the page
	 */
	public int getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(int page) {
		this.page = page;
	}
	/**
	 * @return the row
	 */
	public int getRows() {
		return rows;
	}
	/**
	 * @param row the row to set
	 */
	public void setRows(int row) {
		this.rows = row;
	}
	/**
	 * @return the offert
	 */
	public int getOffert() {
		
		this.offert = (page-1)*rows;
		return offert;
	}
	/**
	 * @param offert the offert to set
	 */
	public void setOffert(int offert) {
		//下面表示的是第一页从0到10，第二页技师从10到20开始
		  
		this.offert = (page-1)*rows;
	}
	private int page; //当前页面
	private int rows;   //每页几条
    private int offert;  //从第一页开始
    
}
