package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.aliasi.classify.Classification;
import com.aliasi.classify.LMClassifier;



public class SentimentAnalyzer
{
	public static String sentimentAnalysis(String text)
	{
		LMClassifier classifier = null;
		
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
		
		Classification classification = null;
        classification = classifier.classify(text);
        System.out.println("classification:  " + classification);
        return (classification.bestCategory());
	}
		
}
