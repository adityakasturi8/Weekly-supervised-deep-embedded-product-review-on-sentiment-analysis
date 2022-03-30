package com;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
public class Stopwords{
	static ArrayList<String> sw = new ArrayList<String>();
public static void readStopword(){
	if(sw.size() == 0){
		try{
			BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"));
			String line = null;
			while((line = br.readLine()) != null){
				line = line.trim();
				if(line.length() > 0){
					line = line.toLowerCase();
					sw.add(line);
				}
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
}