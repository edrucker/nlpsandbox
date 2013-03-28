package controllers;

import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Http.Status;

public class TextRazorNamedEntityRecognition implements NamedEntityRecognition
{

	@Override
	public ObjectNode getNamedEntities(String text)
	{
		ObjectNode namedEntities = null;
		
		Response response = WS.url("http://api.textrazor.com/")
				.setContentType("application/x-www-form-urlencoded")
				.post("apiKey=788449c98bc6e8c35acb08e9993118be4547021441074a592d4eece9&extractors=topics,entities&text=" + text).get();
		
		if(response.getStatus() == Status.OK)
		{
			namedEntities = Json.newObject();
			namedEntities.put("textrazor-named-entities", response.asJson());	
		}
		
		return namedEntities;
	}

}
