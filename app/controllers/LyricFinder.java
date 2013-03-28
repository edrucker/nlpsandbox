package controllers;

import java.util.Iterator;

import models.Track;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.w3c.dom.Document;

import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Http.Status;

public class LyricFinder
{
	public static Long findTrackId(String title, String artist)
	{
		Long trackId = null;
		//find track
		Response trackResponse = WS.url("http://api.musixmatch.com/ws/1.1/track.search")
				.setQueryParameter("apikey", "3338eaab2d33ea4375e36657d137a9a7")
				.setQueryParameter("q_track", title)
				.setQueryParameter("q_artist", artist)
				.setQueryParameter("f_has_lyrics", "1")
				.get().get();
				
		if(trackResponse.getStatus() != Status.OK)
		{
			System.out.println(trackResponse.getStatusText());
		}
		else
		{
			// for demo purposes, just get the first track in response
			JsonNode trackListNode = trackResponse.asJson()
				.findPath("message")
				.findPath("body")
				.findPath("track_list");
				
			if(trackListNode.size() > 0)
			{
				JsonNode trackIdNode = trackListNode 
					.get(0)
					.findPath("track")
					.findPath("track_id");
			
				if(trackIdNode != null)
				{
					String trackIdString = trackIdNode.asText();					
					trackId = Long.valueOf(trackIdString);					
				}	
			}
		}		
		return trackId;
	}
	
	public static ObjectNode findLyrics(String title, String artist)
	{
		ObjectNode lyricNode = Json.newObject();	
		
		Long trackId = findTrackId(title, artist);
		
		//check if we already have lyrics
		Track currentTrack = Track.find.byId(trackId);
		if(currentTrack != null)
		{
			System.out.println(currentTrack.lyrics);
		}
		
		else
		{
			// if we don't already have lyrics, pull them from musixmatch (only gets first 30%)
		
			Response lyricResponse = WS.url("http://api.musixmatch.com/ws/1.1/track.lyrics.get")
					.setQueryParameter("apikey", "3338eaab2d33ea4375e36657d137a9a7")
					.setQueryParameter("track_id", String.valueOf(trackId.longValue()))
					.get().get();

			System.out.println("EDRUCKER: " + lyricResponse.getBody());
		}
		
		return lyricNode;
	}
	
}
