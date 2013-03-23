package controllers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.ArrayNode;

import static akka.pattern.Patterns.ask;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

import play.*;
import play.libs.Akka;
import play.libs.F.Function;
import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
	
	final static ActorRef songAnalyzer = SongAnalyzer.instance;
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result analyzeSong(String title, String artist) {
    	
    	String tempString = title + ":::" + artist; //TODO use JSON object and pass to actor
    	
    	return async(
    		    Akka.asPromise(ask(songAnalyzer, tempString, 1000)).map(	//using 1000ms timeout
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
    
    public static class SongAnalyzer extends UntypedActor {
    	static ActorRef instance = Akka.system().actorOf(new Props(SongAnalyzer.class));

		@Override
		public void onReceive(Object message) {
			if (!(message instanceof String))
			{
				getContext().sender().tell(TODO, instance);
			}
			else
			{
				String title = "Creep";
				String artist = "Radiohead";
				
				 Response response = WS.url("http://developer.echonest.com/api/v4/song/search")
				.setQueryParameter("api_key", "6QHT9QEQUS9A7BCPW")
				.setQueryParameter("title", title)
				.setQueryParameter("artist", artist)
				.get().get();
				
				getContext().sender().tell(response.asJson(), instance);
			}
			
		}
    }
  
}
