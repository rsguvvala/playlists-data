package com.guvvala.music.model;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * @author rajaguvvala
 *
 */
public class PagedResult<T> {
    
    private long totalElements;
    private int page;
    private int size;
    private List<T> elements;
    
    public PagedResult(long totalElements, int page, int size, List<T> elements) {
        this.elements = elements;
        this.totalElements = totalElements;
        this.page = page;
        this.size = size;
    }
    public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public List<T> getElements() {
		return elements;
	}
	public void setElements(List<T> elements) {
		this.elements = elements;
	}
	public static PagedResult from(Page source){
    	return new PagedResult(source.getTotalElements(), source.getNumber(), source.getSize(),source.getContent());
    }
}