package controllers;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.node.ArrayNode;

import play.*;
import play.libs.Json;
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
    		
    		result = ok(resultNode);
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
