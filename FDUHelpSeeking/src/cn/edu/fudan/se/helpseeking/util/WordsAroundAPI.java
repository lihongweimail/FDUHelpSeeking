package cn.edu.fudan.se.helpseeking.util;

import java.util.ArrayList;
import java.util.List;

public class WordsAroundAPI {
	private String api;
	private List<String> words = new ArrayList<String>();
	
	public WordsAroundAPI(String api)
	{
		this.api = api;
	}

	public String getApi() {
		return api;
	}

	public void setApi(String api) {
		this.api = api;
	}

	public List<String> getWords() {
		return words;
	}

	public void setWords(List<String> words) {
		this.words = words;
	}
	
	public void addWord(String word)
	{
		words.add(word);
	}
	
}
