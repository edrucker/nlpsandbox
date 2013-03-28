package controllers;

import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;
import play.libs.WS;
import play.libs.WS.Response;
import play.mvc.Http.Status;

public class TextProcessingNamedEntityRecognition implements NamedEntityRecognition
{

	@Override
	public ObjectNode getNamedEntities(String text)
	{
		ObjectNode namedEntities = null;
		
		Response response = WS.url("http://text-processing.com/api/phrases/")
				.setContentType("application/x-www-form-urlencoded")
				.setQueryParameter("text", text)
				.post("text="+text).get();
		
		if(response.getStatus() == Status.OK)
		{
			namedEntities = Json.newObject();
			namedEntities.put("text-processing-named-entities", response.asJson());	
		}
		
		return namedEntities;
	}

}
