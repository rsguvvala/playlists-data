package com.guvvala.music;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.guvvala.music.es.config.Config;
import com.guvvala.music.model.PagedResult;
import com.guvvala.music.model.PlayList;
import com.guvvala.music.model.PlayListInfo;
import com.guvvala.music.service.PlayListService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class PlayListIntegrationTests {

	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	private PlayListService playListService;
	
	@Before
	public void before(){
		// delete the index in case if it's already present
		if(elasticsearchTemplate.indexExists(PlayList.class)){
			elasticsearchTemplate.deleteIndex(PlayList.class);
		}
		elasticsearchTemplate.createIndex(PlayList.class);
		elasticsearchTemplate.putMapping(PlayList.class);
		
		initializePlayList();
	}
	
	@Test
	public void givenPlayListService_whenSavePlayList_thenSaveIsSuccessful() {
		PlayList playlist = getPlayList();
		playListService.save(playlist);
		assertNotNull(playlist.getId());
	}
	
	@Test
	public void givenPersistedPlayLists_whenSearchByTags_thenRelevantFound(){
		PagedResult<List<PlayList>> playLists = playListService.getMusicTracksByTags("chill", PageRequest.of(0, 10));
		assertEquals(3L, playLists.getElements().size());
	}
	
	@Test
	public void givenPersistedPlayLists_whenfindAll_thenAllFound(){
		PagedResult<List<PlayList>> playLists = playListService.getAllMusicTracks(PageRequest.of(0, 3));
		assertEquals(3L, playLists.getElements().size());
	}
	
	@Test
	public void givenPersistedPlayLists_whenSearchByTagsforSuggestedTags_thenRightTagsAreFound(){
		List<PlayListInfo> playListInfo = playListService.suggestMusickTracksByTag("pop");
		assertEquals(2L, playListInfo.size());
		assertEquals("pop rock", playListInfo.get(1).getTag());
	}
	
	@Test
	public void givenPersistedPlayLists_whenSearchByTagsforRelevantTags_thenRightTagsAreFound(){
		List<String> recommendedTags = playListService.recommendMusicTracksByTag("chill");
		assertEquals(5L, recommendedTags.size());
		assertEquals("bass", recommendedTags.get(0));
	}
	
	private void initializePlayList(){
		PlayList shadowOfDoubt = new PlayList();
		shadowOfDoubt.setAuthor("Shadow of Doubt");
		shadowOfDoubt.setName("creeps mix night immer");
		shadowOfDoubt.setTags(asList("chill", "pop", "indie rock"));
		shadowOfDoubt.setViews(33983);
		playListService.save(shadowOfDoubt);
		
		PlayList devonGallont = new PlayList();
		devonGallont.setAuthor("Devon Gallont");
		devonGallont.setName("workout Sounds of laughing paint me");
		devonGallont.setTags(asList("chill", "pop rock", "bass"));
		devonGallont.setViews(33721);
		playListService.save(devonGallont);
		
		PlayList pureWitch = new PlayList();
		pureWitch.setAuthor("Pure Witch");
		pureWitch.setName("half music Brassy within");
		pureWitch.setTags(asList("feel good", "pop", "bass", "chill"));
		pureWitch.setViews(33154);
		playListService.save(pureWitch);
		
	}
	
	private PlayList getPlayList(){
		PlayList cxmbria = new PlayList();
		cxmbria.setAuthor("cxmbria");
		cxmbria.setName("calling chirstmas relaxing Strolling");
		cxmbria.setTags(asList("romance", "pop", "indie rock"));
		cxmbria.setViews(32798);
		return cxmbria;
	}
}
