package controllers;

import models.Track;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.ArrayNode;

import static akka.pattern.Patterns.ask;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import play.*;
import play.data.Form;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
	
	static Form<Track> trackForm = Form.form(Track.class);
	final static ActorRef trackAnalyzer = TrackAnalyzer.instance;
	final static ActorRef trackAdder = TrackAdder.instance;
  
    public static Result index() {
        return redirect(routes.Application.tracks());
    }
    
    public static Result tracks()
    {
    	return ok(views.html.tracks.render(Track.all()));
    }
    
    public static Result addTrackForm()
    {
    	return ok(views.html.addTrack.render(trackForm));
    }
    
    public static Result addTrack()
    {
    	Form<Track> filledForm = trackForm.bindFromRequest();
    	if(filledForm.hasErrors())
    	{
    		return badRequest(views.html.addTrack.render(filledForm));
    	}
    	else
    	{
    		return async(
    			Akka.asPromise(ask(trackAdder, filledForm.get(), 1000)).map(
    				new Function<Object, Result>() {
    					public Result apply(Object response) {
    						return redirect(routes.Application.tracks());
    					}
    				}
    			)
    		);
    	}
    }
    
    public static Result analyzeTrack(String title, String artist) {
    	
    	Track tempTrack = new Track();
    	tempTrack.title = title;
    	tempTrack.artist = artist;
    	
    	return async(
    		Akka.asPromise(ask(trackAnalyzer, tempTrack, 10000)).map(	//using 1000ms timeout
    		   	new Function<Object,Result>() {
    		   		public Result apply(Object response) {
    		   			return ok(response.toString());
    		   		}
    		   	}
    		)
    	);
    }
    
    //Convert current error to JSON node
    public static ObjectNode getErrorJSON(String error) //TODO standardize errors	
    {
    	ObjectNode errorNode = Json.newObject();
    	errorNode.put("error", error);
    	return errorNode;
    }
    
    public static class TrackAnalyzer extends UntypedActor {
    	static ActorRef instance = Akka.system().actorOf(new Props(TrackAnalyzer.class));

		@Override
		public void onReceive(Object message) {
			if (!(message instanceof Track))
			{
				getContext().sender().tell(TODO, instance);
			}
			else
			{
				Track track = (Track)message;
				
				// Find lyrics
				ObjectNode analysisNode = LyricFinder.findLyrics(track.title, track.artist);
				
				// Get sentiment of track
				ObjectNode sentimentNode = SentimentAnalyzer.sentimentAnalysis(analysisNode.get("lyrics").asText());
				if(sentimentNode != null)
				{
					analysisNode.putAll(sentimentNode);
				}
				
				// Get named entities from text-processing.com
				TextProcessingNamedEntityRecognition tpner = new TextProcessingNamedEntityRecognition();
				ObjectNode textProcessingNode = tpner.getNamedEntities(analysisNode.findValue("lyrics").asText());
				if(textProcessingNode != null)
				{
					analysisNode.putAll(textProcessingNode);
				}
				
				// Get named entities from TextRazor
				TextRazorNamedEntityRecognition trner = new TextRazorNamedEntityRecognition();
				ObjectNode textRazorNode = trner.getNamedEntities(analysisNode.findValue("lyrics").asText());
				if(textRazorNode != null)
				{
					analysisNode.putAll(textRazorNode);
				}
				
				getContext().sender().tell(analysisNode, instance);
			}
			
		}
    }
    
    public static class TrackAdder extends UntypedActor {
    	static ActorRef instance = Akka.system().actorOf(new Props(TrackAdder.class));

		@Override
		public void onReceive(Object message) {
			if (!(message instanceof Track))
			{
				getContext().sender().tell(TODO, instance);
			}
			else
			{
				Track track = (Track)message;
				Long id = LyricFinder.findTrackId(track.title, track.artist);
				if(id != null)
				{
					track.id = id;
					
					if(Track.find.byId(id) == null)
					{
						track.save();
					}
					else
					{
						track.update();
					}
					
					getContext().sender().tell(TODO, instance);
				}
				else
				{
					getContext().sender().tell(TODO, instance);
				}
			}
			
		}
    }
}
