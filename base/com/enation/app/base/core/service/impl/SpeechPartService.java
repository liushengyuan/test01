package com.enation.app.base.core.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
/**
 * 特殊词性服务，判断某个词是否是特殊词，无需判断关键词的
 * 
 * 中文词性大体上分为：
 * 	词性指作为划分词类的根据的词的特点。现代汉语的词可以分为12类。
		实词：名词、动词、形容词、数词、量词和代词。
		虚词：副词、介词、连词、助词、拟声词和叹词
		
	一般来说，虚词不会作为一篇文章的关键词，所以可以忽略，实词中的量词和代词也不会作为关键词。
 * @author zmm 2016-03-31
 *
 */
@Component
public class SpeechPartService implements ApplicationListener<ApplicationEvent> {
	private boolean inited = false;
	/**
	 * 虚词表
	 */
	private Map<String, Boolean> virtualWords = new HashMap<String, Boolean>();
	 
	
	
	
	/**
	 * 判断是否虚词
	 * @param word
	 * @return
	 */
	public boolean isVirtualWord(String word){
		return this.virtualWords.containsKey(word);
	}
	 
	
	public void onApplicationEvent(ApplicationEvent evt) {
		if(evt instanceof ContextStartedEvent || evt instanceof ContextRefreshedEvent){
			this.init();
		}
		
	}
	
	private void init(){
		if(inited){
			return;
		}
		inited = true;
		BufferedReader br = null;
		InputStream in = null;
		InputStreamReader r = null;
		try{
			in =this.getClass().getClassLoader().getResourceAsStream("com/enation/app/base/core/dic/virtualwords.txt");
			r = new InputStreamReader(in,"UTF-8");
			br = new BufferedReader(r);
			String line = null;
			while( (line=br.readLine())!=null ){
				String[] words = line.split(" ");
				for(String w:words){
					if(StringUtils.isNotBlank(w)){
						this.virtualWords.put(w.trim(), Boolean.TRUE);
					}
				}
			}
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}finally{
			try {
				br.close();
			} catch (Exception e) {
				 
			}
			try {
				r.close();
			} catch (Exception e) {
				 
			}
		}
		 
	}
	
	private  final boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }  
  
    public  final boolean isChinese(String strName) {  
        char[] ch = strName.toCharArray();  
        for (int i = 0; i < ch.length; i++) {  
            char c = ch[i];  
            if (isChinese(c)) {  
                return true;  
            }  
        }  
        return false;  
    }  

}
