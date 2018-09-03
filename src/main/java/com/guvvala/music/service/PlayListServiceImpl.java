/**
 * 
 */
package com.guvvala.music.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import com.guvvala.music.model.PagedResult;
import com.guvvala.music.model.PlayList;
import com.guvvala.music.model.PlayListInfo;
import com.guvvala.music.repository.PlayListRepository;

/**
 * @author rajaguvvala
 *
 */
@Service
public class PlayListServiceImpl implements PlayListService{

	Random rand = new Random();
	
	@Autowired
	ElasticsearchTemplate elasticsearchTemplate;
	
	@Autowired
	PlayListRepository playListReository;

	/**
	 * 
	 * This method returns all the play lists available 
	 * 
	 * @param pageable
	 * @return paginated results of play lists
	 */
	@Override
	public PagedResult<List<PlayList>> getAllMusicTracks(Pageable pageable) {
		//TODO replace PageRequest with some repository method like findAllByOrderedByViewsDesc
		return PagedResult.from(playListReository.findAll(new PageRequest(pageable.getPageNumber(),
				pageable.getPageSize(), new Sort(Direction.DESC, "views"))));
	}

	/**
	 * 
	 * This method returns all the play lists matching tag
	 * 
	 * @param tag
	 * @param pageable
	 * @return all play lists matching tag ordered by no of views
	 */
	@Override
	public PagedResult<List<PlayList>> getMusicTracksByTags(String tag, Pageable pageable) {
		// TODO replace PageRequest with some repository method like findByTagsByOrderedByViewsDesc
		return PagedResult.from(playListReository.findByTags(tag, new PageRequest(pageable.getPageNumber(),
				pageable.getPageSize(), new Sort(Direction.DESC, "views"))));
	}

	/**
	 * 
	 * This method provides  search as you type functionality for tags
	 * 
	 * @param tag
	 * @return
	 */
	@Override
	public List<PlayListInfo> suggestMusickTracksByTag(String tag) {
		// find the suggested tags
		List<String> tags = playListReository.findSuggestedTagsByGivenTag(tag);

		List<PlayListInfo> playLists = new ArrayList<>();
		tags.forEach(x->{
			playLists.add(new PlayListInfo(x,playListReository.findTotalNumberOfPlaylistsByTag(x)));
		});
		return playLists;
	}

	/**
	 * 
	 * This method provides recommended tags
	 * recommendation is based on the appearance of the other tags along with the searched tag
	 * @param tag
	 * @return recommended tags
	 */
	@Override
	public List<String> recommendMusicTracksByTag(String tag) {
		// find recommended tags
		return playListReository.findRecommendedTagsByGivenTag(tag);
	}


	/**
	 * 
	 * This method saves the newly created play list
	 *
	 * @param playList
	 * @return 
	 */
	@Override
	public void save(PlayList playList) {
		// create the index in case if it's not present
		if(!elasticsearchTemplate.indexExists(PlayList.class)){
			elasticsearchTemplate.createIndex(PlayList.class);
			elasticsearchTemplate.putMapping(PlayList.class);
		}
		playListReository.save(playList);
	}
	
	@Override
	public void initialize(){
		for(int i=0 ; i < 2000000; i++){
            PlayList playList = new PlayList();
            playList.setId(Integer.valueOf(i).toString());
            playList.setAuthor(getAuthor());
            playList.setName(getName());
            playList.setTags(getTags());
            playList.setViews(getViews());
            playListReository.save(playList);
        }
	}
	
	private String getAuthor(){
        String[] authors = {"Devon Gallont", "Darksords", "Ayaan", "loop hole", "pure-witch", "cxmbria",
                "telthor","oniigi", "Shadow of doubt", "clizzy", "chunk of change"};
        return authors[rand.nextInt(11)];
    }

    private String getName(){
        String[] albums = {"Impulse", "Conquer", "World", "Playboy", "lifestyle", "Infinity", "Home", "Summer", "peggy", "Carter",
                "missing", "puzzle", "pieces", "songbird", "Watch", "Vibes", "paint me", "heaven", "Life is Good", "Cape Douches", "Deviant",
                "half", "man", "wanted", "liar", "bringer", "death", "Dancing", "myself", "mess", "Pillars", "Queer", "Halloween", "Sounds of",
                "sunshine", "my love", "creeps", "relaxing", "music", "Anyone else", "But you","sunshine", "Endless", "adventures", "shots",
                "one", "two", "three", "sewer", "rat", "Strolling", "Earth", "Stree", "Shadows", "Too short", "When 8tracks", "Entered my life",
                "hush", "now", "doctor", "engineer", "light", "mix", "demin", "yellin", "flirting", "starting", "laughing", "danger", "calling",
                "where", "been", "running", "feet", "fly", "sad", "addition", "Brassy", "Women", "these", "mortal", "beans", "nebual", "gamora",
                "swear", "lived", "Honey", "workout", "immer", "meine", "taube", "chirstmas", "Love", "hot", "Relaxing", "Mesmerizing",
                "Devotional", "within", "what ever", "night", "room", "uphill", "battle","some where", "carry", "Story", "ends", "Ocean", "Sounds",
                "Ghoul"};

        String albumName = albums[rand.nextInt(110)] + " " + albums[rand.nextInt(110)] + " " + albums[rand.nextInt(110)] + " "
                + albums[rand.nextInt(110)];
        return albumName;
    }

    private List<String> getTags(){
        String[] tags = {
                "indie", "chill","rock","love","pop","electronic","indie rock", "alternative", "happy", "alternative rock", "sad",
                "summer", "hip hop", "dance", "study", "relax", "party", "rap", "folk", "acoustic", "sleep",
                "upbeat", "calm", "fanmix", "indie pop", "instrumental", "soundtrack", "fun", "punk", "kpop", "jazz",
                "r&b", "dubstep", "workout", "house", "remix", "pop punk", "slow", "feel good",
                "country", "classic rock", "indie folk", "oldies", "ambient", "soul", "covers", "dark", "5sos", "edm", "blues",
                "electro", "mellow", "breakup", "cute", "night", "morning", "trap", "metal", "smoke",
                "weed", "love songs", "90s", "winter", "classical", "writing", "christmas", "random", "oc", "heartbreak",
                "80s", "punk rock", "alternative indie", "korean", "piano", "personal", "chillwave", "girl power", "soft", "rap & hip hop",
                "driving", "indie electronic", "reggage", "autumn", "road trip", "sexy", "female vocalists", "funk", "deep house",
                "good vibes", "beach", "bass", "singer/songwriter", "fall", "high", "experimental", "relaxing", "anime", "new",
                "original character", "mix", "motivation", "crush", "electric", "girls", "lo-fi", "harry styles", "teen wolf", "easy listening",
                "music", "throwback", "halloween", "supernatural", "rain", "melancholy", "psychedelic", "nostalgia", "hiphop", "downtempo",
                "running", "spring", "dressed", "techno", "lonely", "electronia", "beats", "homestuck", "marvel", "japanese", "romance",
                "halsey", "cry", "quiet", "dreamy", "mashup", "chillstep","dream pop", "late night", "friends"

        };

        List<String> tag = new ArrayList<>();
        int noOfTags = rand.nextInt(7);
        if(noOfTags == 0){
            noOfTags = 1;
        }
        while(noOfTags > 0){
            tag.add(tags[rand.nextInt(138)]);
            noOfTags--;
        }
        return tag;
    }

    private int getViews(){
        int views = rand.nextInt(100000);
        return views;
    }

}
