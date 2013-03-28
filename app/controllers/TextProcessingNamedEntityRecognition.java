package controllers;

import org.codehaus.jackson.node.ObjectNode;

import play.libs.WS;
import play.libs.WS.Response;

public class TextProcessingNamedEntityRecognition implements NamedEntityRecognition
{

	@Override
	public ObjectNode getNamedEntities(String text)
	{
		ObjectNode namedEntities = null;
		
		Response lyricResponse = WS.url("http://text-processing.com/api/phrases/")
				.post("text="+text).get();
		
		System.out.println("EDRUCKER: " + lyricResponse.getBody());
		
		return namedEntities;
	}

}
