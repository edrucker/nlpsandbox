package controllers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.ArrayNode;

import play.*;
import play.libs.F.Function;
import play.libs.Json;
import play.libs.WS;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result analyzeSong(String title, String artist) {
    	
    	Result result;
    	
    	if(title.equals(""))
    	{
    		ObjectNode errorNode = getErrorJSON("Missing parameter 'title'");
    		result = badRequest(errorNode);
    	}
    	else if (artist.equals(""))
    	{
    		ObjectNode errorNode = getErrorJSON("Missing parameter 'artist'");
    		result = badRequest(errorNode);
    	}	
    	else
    	{
    		ObjectNode resultNode = Json.newObject();
    		
    		//Get song id from EchoNest API
    		
    		return async(
    			WS.url("http://developer.echonest.com/api/v4/song/search")
    				.setQueryParameter("api_key", "6QHT9QEQUS9A7BCPW")
    				.setQueryParameter("title", title)
    				.setQueryParameter("artist", artist)
    				.get().map(
    					new Function<WS.Response, Result>() {
    						public Result apply(WS.Response response) {
    							return ok(response.asJson());
    						}
    					}
    				)
    			 
    		);
    		
    		//result = ok(resultNode);
    	}
    	
    	return result;
    }
    
    //Convert current error to JSON node
    public static ObjectNode getErrorJSON(String error) //TODO standardize errors	
    {
    	ObjectNode errorNode = Json.newObject();
    	errorNode.put("error", error);
    	return errorNode;
    }
  
}
