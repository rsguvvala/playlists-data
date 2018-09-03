package com.guvvala.music.repository;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;

import com.guvvala.music.model.PlayList;

/**
 * @author rajaguvvala
 *
 * This is custom repository and uses native query builder to search 
 * against elastic search. Spring data repository is not ideal for aggregations
 * and suggestions.
 */
@Repository
public class PlayListsRepositoryCustomImpl implements PlayListsRepositoryCustom{

	private static final String PLAYLIST_INDEX = "music";
	private static final String PLAYLIST_INDEX_TYPE = "playlists";
	private static final String SUGGEST_TAGS_FIELD = "suggestTags";
	private static final String TAGS_FIELD = "tags";
	private static final String PLAYLIST_SUGGESTION = "suggest-playlists";
	private static final String RECOMMEND_TAGS = "recommendedTags";
	private static int SUGGEST_MAX_SIZE = 1000;
	
	@Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
	
	
	/**
	 * 
	 * This method provides  search as you type functionality for tags
	 * 
	 * @param tag
	 * @return
	 */
	public List<String> findSuggestedTagsByGivenTag(String tag){
	
		CompletionSuggestionBuilder suggestionBuilder = SuggestBuilders.completionSuggestion(SUGGEST_TAGS_FIELD).prefix(tag).size(SUGGEST_MAX_SIZE);

		final SearchResponse  suggestResponse = elasticsearchTemplate.suggest(new SuggestBuilder().addSuggestion(PLAYLIST_SUGGESTION,suggestionBuilder), PlayList.class);
		CompletionSuggestion completionSuggestion = suggestResponse.getSuggest().getSuggestion(PLAYLIST_SUGGESTION);
		List<CompletionSuggestion.Entry.Option> options = completionSuggestion.getEntries().get(0).getOptions();
		
		return options.stream().map(x-> x.getText().string()).distinct().collect(Collectors.toList());

	}
	
	/**
	 * 
	 * This method provides  total number of play lists matching the given tag
	 * 
	 * @param tag
	 * @return Total count of play lists matching this tag
	 */
	public long findTotalNumberOfPlaylistsByTag(String tag){
		final QueryBuilder builder = termQuery(TAGS_FIELD,tag);
		final SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(builder)
	            .build();
		long count = elasticsearchTemplate.count(searchQuery, PlayList.class);
		return count;
	}
	
	/**
	 * 
	 * This method provides recommended tags
	 * recommendation is based on the appearance of the other tags along with the searched tag
	 * @param tag
	 * @return recommended tags
	 */
	public List<String> findRecommendedTagsByGivenTag(String tag){
		final QueryBuilder builder = termQuery(TAGS_FIELD,tag);
    	SearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(builder)
				.withSearchType(SearchType.DEFAULT)
				.withIndices(PLAYLIST_INDEX).withTypes(PLAYLIST_INDEX_TYPE)
				.addAggregation(terms(RECOMMEND_TAGS).field(TAGS_FIELD))
				.build();
		
		Aggregations aggregations = elasticsearchTemplate.query(searchQuery, x->x.getAggregations());
		StringTerms  agr = (StringTerms) aggregations.asMap().get(RECOMMEND_TAGS);
		return agr.getBuckets().stream().skip(1).map(x->x.getKeyAsString()).collect(Collectors.toList());
	}

}
