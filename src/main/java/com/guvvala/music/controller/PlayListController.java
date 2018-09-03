/**
 * 
 */
package com.guvvala.music.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guvvala.music.model.PagedResult;
import com.guvvala.music.model.PlayList;
import com.guvvala.music.model.PlayListInfo;
import com.guvvala.music.service.PlayListService;

/**
 * @author rajaguvvala
 *
 * 
 */
@RestController
@RequestMapping("/music")
public class PlayListController {

	@Autowired
	PlayListService playTrackService;
	
	/**
	 * 
	 * This method returns all the play lists available 
	 * 
	 * @param pageable
	 * @return paginated results of play lists
	 */
	@RequestMapping(value="/playlists", method=RequestMethod.GET)
	public PagedResult<List<PlayList>> playLists(@PageableDefault (value=10, page=0) Pageable pageable){
		return playTrackService.getAllMusicTracks(pageable); 
	}
	
	/**
	 * 
	 * This method returns all the play lists matching tag
	 * 
	 * @param tag
	 * @param pageable
	 * @return all play lists matching tag ordered by no of views
	 */
	@RequestMapping(value="/playlists/{tag}", method=RequestMethod.GET)
	public PagedResult<List<PlayList>> musicTracksByTag(@PathVariable(value="tag") String tag,
			@PageableDefault (value=10, page=0) Pageable pageable){
		return playTrackService.getMusicTracksByTags(tag, pageable); 
	}
	
	/**
	 * 
	 * This method provides  search as you type functionality for tags
	 * 
	 * @param tag
	 * @return
	 */
	@RequestMapping(value="/playlists/suggestions", method=RequestMethod.GET)
	public List<PlayListInfo> suggestPlaylists(@RequestParam(value="tag") String tag){
		return playTrackService.suggestMusickTracksByTag(tag);
	}
	
	/**
	 * 
	 * This method provides recommended tags
	 * recommendation is based on the appearance of the other tags along with the searched tag
	 * @param tag
	 * @return recommended tags
	 */
	@RequestMapping(value="/playlists/recommendations", method=RequestMethod.GET)
	public List<String> recommendPlaylists(@RequestParam(value="tag") String tag){
		return playTrackService.recommendMusicTracksByTag(tag);
	}
	
	/**
	 * 
	 * This method provides recommended tags
	 * recommendation is based on the appearance of the other tags along with the searched tag
	 * @param tag
	 * @return recommended tags
	 */
	@RequestMapping(value="/playlists", method=RequestMethod.POST)
	public ResponseEntity<HttpStatus> savePlaylist(@RequestBody PlayList playList){
		playTrackService.save(playList);
		return ResponseEntity.ok(HttpStatus.OK);
	}
	
	@RequestMapping(value="/playlists/init", method=RequestMethod.GET)
	public String recommendPlaylists(){
		playTrackService.initialize();
		return "Successful";
	}
}
