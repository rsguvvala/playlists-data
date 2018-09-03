/**
 * 
 */
package com.guvvala.music.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.guvvala.music.model.PlayList;

/**
 * @author rajaguvvala
 *
 */

@Repository
public interface PlayListRepository extends ElasticsearchRepository<PlayList, String>, PlayListsRepositoryCustom {
	
	Page<PlayList> findByTags(String tag, Pageable page);
	Page<PlayList> findAll(Pageable page);
}