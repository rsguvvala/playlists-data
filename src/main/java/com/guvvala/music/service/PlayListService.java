/**
 * 
 */
package com.guvvala.music.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.guvvala.music.model.PlayList;
import com.guvvala.music.model.PlayListInfo;
import com.guvvala.music.model.PagedResult;

/**
 * @author rajaguvvala
 *
 */
public interface PlayListService {

	PagedResult<List<PlayList>> getAllMusicTracks(Pageable pageable);
	PagedResult<List<PlayList>> getMusicTracksByTags(String tag, Pageable pageable);
	List<PlayListInfo> suggestMusickTracksByTag(String tag);
	List<String> recommendMusicTracksByTag(String tag);
	void save(PlayList playList);
	void initialize();
}
