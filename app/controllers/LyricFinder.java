package controllers;

import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;

import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;

public class LyricFinder {
	public static ObjectNode findLyrics(String title, String artist)
	{
		ObjectNode lyricNode = Json.newObject();	
		
		//find track
		Response trackResponse = WS.url("http://api.musixmatch.com/ws/1.1/track.search")
				.setQueryParameter("apikey", "3338eaab2d33ea4375e36657d137a9a7")
				.setQueryParameter("q_track", title)
				.setQueryParameter("q_artist", artist)
				.setQueryParameter("f_has_lyrics", "1")
				.get().get();
		
		// for demo purposes, just get the first track in response
		String trackId = trackResponse.asJson()
			.get("message")
			.get("body")
			.get("track_list")
			.get(0)
			.get("track")
			.get("track_id").asText();
		
		//find lyrics
		Response lyricResponse = WS.url("http://api.musixmatch.com/ws/1.1/track.lyrics.get")
				.setQueryParameter("apikey", "3338eaab2d33ea4375e36657d137a9a7")
				.setQueryParameter("track_id", trackId)
				.get().get();
				
		
		System.out.println("EDRUCKER: " + lyricResponse.getBody());
	//	for(Iterator<String> it = lyricResponse.asJson().get("message").get("body").get("track_list").get(0).get("track").get("track_id").getFieldNames(); it.hasNext();)
	//	{
	//		String s = it.next();
	//		System.out.println("EDRUCKER: "+s);
	//	}
		
		
		return lyricNode;
		
		
	}
}
