package com.guvvala.music.repository;

import java.util.List;

public interface PlayListsRepositoryCustom {

	public List<String> findSuggestedTagsByGivenTag(String tag);
	public long findTotalNumberOfPlaylistsByTag(String tag);
	public List<String> findRecommendedTagsByGivenTag(String tag);
}
