package controllers;

import org.codehaus.jackson.node.ObjectNode;

public interface NamedEntityRecognition
{
	public ObjectNode getNamedEntities(String text);
}
