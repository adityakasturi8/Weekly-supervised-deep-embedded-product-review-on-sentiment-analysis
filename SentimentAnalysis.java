package com;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import java.util.Properties;
import java.util.ArrayList;
public class SentimentAnalysis {
	static StanfordCoreNLP pipeline;
public static void main(String args[]){
	init();
	String result = SentimentAnalysis.findSentiment("The feature i do not like, but still trying to figure it out, is the loud sound it makes when i restart the phone");
	System.out.println(result);
}
public static void init(){
	if(pipeline == null){
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		pipeline = new StanfordCoreNLP(props);
	}
}
public static String findSentiment(String sentencedata) {
	int mainSentiment = 0;
	String result = "none";
	init();
	if(sentencedata != null && sentencedata.length() > 0){
		int longest = 0;
		Annotation annotation = pipeline.process(sentencedata);
		for(CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)){
			Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
			int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
            String partText = sentence.toString();
			if(partText.length() > longest){
				mainSentiment = sentiment;
                longest = partText.length();
			}
		}
	}
	if(mainSentiment == 4 || mainSentiment == 5  || mainSentiment == 3){
		result = "Positive";
	}
	if(mainSentiment == 0 || mainSentiment == 1 || mainSentiment == 2){
		result = "Negative";
	}
	return result;
}
}