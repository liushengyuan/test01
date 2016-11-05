package com.enation.app.base.core.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 分词后的文章结果
 * @author zmm 2016-03-30
 *
 */
public class TokenisedDoc implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6121496716731672237L;
	/**
	 * 分词结果
	 */
	private List<String> words = new ArrayList();
	/**
	 * 文章总词数
	 */
	private int totalWords = 0;
	/**
	 * 最大词出现次数
	 */
	private int maxWordCount = 0;
	/**
	 * 词出现次数
	 */
	private Map<String, Integer> wordCount = new LinkedHashMap<String, Integer>();

	public List<String> getWords() {
		return words;
	}

	

	public int getTotalWords() {
		return totalWords;
	}

	 

	public int getMaxWordCount() {
		return maxWordCount;
	}

	 

	public Map<String, Integer> getWordCount() {
		return wordCount;
	}


	public void addWord(String word){
		words.add(word);
		totalWords++;
		if(wordCount.containsKey(word)){
			wordCount.put(word, wordCount.get(word)+1);
		}else{
			wordCount.put(word, 1);
		}
		if(wordCount.get(word)>this.maxWordCount){
			maxWordCount = wordCount.get(word);
		}
	}
	
	

}
