package com;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;
public class CNNFeatureVector{
	static ArrayList<String[]> reviews = new ArrayList<String[]>();
    static ArrayList<Integer> ratings = new ArrayList<Integer>();
    static ArrayList<String> uniquewords;
	static ArrayList<double[]> vector = new ArrayList<double[]>();
	
public static void clear(){
	reviews.clear();
	ratings.clear();
	if(uniquewords != null){
		uniquewords.clear();
	}
	vector.clear();
}

public static void readReviews(ArrayList<String[]> review){
	clear();
	HashSet<String> hs = new HashSet<String>();
	for(int i=0;i<review.size();i++){
		String arr[] = review.get(i);
		ratings.add(Integer.parseInt(arr[0].trim()));
		String[] tokens = arr[1].split("\\W+");
		for(String terms : tokens){
			if(!Stopwords.sw.contains(terms))
				hs.add(terms);
		}
		reviews.add(tokens);
	}
	uniquewords = new ArrayList<String>(hs);
	System.out.println(uniquewords.size());
}
public static double[] generateVector(String input){
	String arr[] = input.split("\\s+");
	double[] tfidf = new double[uniquewords.size()];
	for(int i=0;i<uniquewords.size();i++){
		tfidf[i] = getTF(arr,uniquewords.get(i)) * getIDF(reviews,uniquewords.get(i));
	}
	return tfidf;
}

public static void generateVector(){
	for(String[] tokens : reviews){
		double[] tfidf = new double[uniquewords.size()];
		for(int i=0;i<uniquewords.size();i++){
			tfidf[i] = getTF(tokens,uniquewords.get(i)) * getIDF(reviews,uniquewords.get(i));
		}
		vector.add(tfidf);
	}
}
public static double getTF(String[] document, String word){
	double count = 0;
	for(String term : document){
		if(term.equalsIgnoreCase(word))
			count++;
	}
    return count/document.length;
}
public static double getIDF(ArrayList<String[]> document, String word){
	double count = 0;
	for(String[] doc : document){
		for(String term : doc){
			if(term.equalsIgnoreCase(word)){
				count++;
				break;
			}
		}
	}
    return Math.log(document.size()/count);
}
//cosine sim
public static double euclideanDistance(double[] vector1, double[] vector2){
	double dot = 0, magnitude1 = 0, magnitude2=0;
	for(int i=0;i<vector1.length;i++){
		dot+=vector1[i]*vector2[i];
		magnitude1+=Math.pow(vector1[i],2);
    	magnitude2+=Math.pow(vector2[i],2);
	}
	magnitude1 = Math.sqrt(magnitude1);
    magnitude2 = Math.sqrt(magnitude2);
    double d = dot / (magnitude1 * magnitude2);
    return d == Double.NaN ? 0 : d;
}
}

    