/**
 * 
 */
package com.guvvala.music.model;

/**
 * @author rajaguvvala
 *
 */
public class PlayListInfo {

	private String tag;
	private long numerOfPlayLists;
	
	
	public PlayListInfo(String tag, long numerOfPlayLists) {
		this.tag = tag;
		this.numerOfPlayLists = numerOfPlayLists;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public long getNumerOfPlayLists() {
		return numerOfPlayLists;
	}
	public void setNumerOfPlayLists(int numerOfPlayLists) {
		this.numerOfPlayLists = numerOfPlayLists;
	}
	
}
