package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.codehaus.jackson.node.ObjectNode;

import play.libs.Json;

import com.aliasi.classify.Classification;
import com.aliasi.classify.LMClassifier;


public class SentimentAnalyzer
{
	public static ObjectNode sentimentAnalysis(String text)
	{
		System.out.println("Analyzing sentiment of: " + text);
		ObjectNode sentimentNode = Json.newObject();
		LMClassifier classifier = null;
		
		// load serialized classifier -- TODO move to global
		try {	
			FileInputStream in = new FileInputStream("classifiers\\sentiment");
			ObjectInputStream ois = new ObjectInputStream(in);
			classifier = (LMClassifier)(ois.readObject());
			ois.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Classification classification = classifier.classify(text);
		sentimentNode.put("classification",classification.bestCategory());
		sentimentNode.put("classification-details", classification.toString());

        return (sentimentNode);
	}
		
}
