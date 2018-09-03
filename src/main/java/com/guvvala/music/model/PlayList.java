/**
 * 
 */
package com.guvvala.music.model;

import static org.springframework.data.elasticsearch.annotations.FieldType.Integer;
import static org.springframework.data.elasticsearch.annotations.FieldType.Keyword;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.core.completion.Completion;

/**
 * @author rajaguvvala
 *
 */
@Document(indexName = "music", type = "playlists")
public class PlayList {

	@Id
	private String id;
	@Field(type = Keyword)
	private String name;
	@Field(type = Keyword)
	private String author;
	@Field(type = Keyword)
	private List<String> tags;
	@CompletionField(maxInputLength = 100)
    private Completion suggestTags;

	@Field(type = Integer)
	private int views;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
		this.suggestTags = new Completion(tags.toArray(new String[tags.size()]));
	}
	
	public Completion getSuggestTags() {
		return suggestTags;
	}
	public void setSuggestTags(Completion suggestTags) {
		this.suggestTags = suggestTags;
	}
	public int getViews() {
		return views;
	}
	public void setViews(int views) {
		this.views = views;
	}
	
}
