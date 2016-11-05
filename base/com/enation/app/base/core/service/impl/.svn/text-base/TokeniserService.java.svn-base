package com.enation.app.base.core.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.TokenisedDoc;
import com.enation.app.base.core.service.IKSegmenter;
import com.enation.app.base.core.service.ITokeniser;
/**
 * 使用IKAnalylizer分词
 * @author zmm 2016-03-30
 *
 */
@Component
public class TokeniserService implements ITokeniser {
	
	private SpeechPartService speechPartService;
	

	public SpeechPartService getSpeechPartService() {
		return speechPartService;
	}

	@Autowired
	public void setSpeechPartService(SpeechPartService speechPartService) {
		this.speechPartService = speechPartService;
	}

	
	
	/**
	 * 
	 * @param line
	 * @param ignoreVirtual 是否忽略虚词
	 * @return
	 */
	public TokenisedDoc tokenise(String line,boolean ignoreVirtual){
		TokenisedDoc tokenisedDoc = new TokenisedDoc();
	 
		IKSegmenter ikseg = new IKSegmenter(new StringReader(line), true);
		//单个字
		List<Lexeme> singleWords = new ArrayList<Lexeme>();
		int lastSingleWordPos = -1;
		Lexeme l = null;
		try {
			while((l = ikseg.next()) != null){
				if(singleWords.size()>0 && l.getBeginPosition() >(lastSingleWordPos+1)){
					this.mergeToWord(tokenisedDoc, singleWords);
					lastSingleWordPos=-1;
				}
				String word = l.getLexemeText();
				if(StringUtils.isNotBlank(word)){
					if(this.speechPartService.isChinese(word)){
						int len = word.length();
						if(len==1){
							if(this.speechPartService.isVirtualWord(word)){
								if(singleWords.size()>0){
									this.mergeToWord(tokenisedDoc, singleWords);
									lastSingleWordPos=-1;
								}
								
							}else{
								singleWords.add(l);
								lastSingleWordPos = l.getBeginPosition();
							}
						}else{
							if(singleWords.size()>0){
								this.mergeToWord(tokenisedDoc, singleWords);
								lastSingleWordPos=-1;
							}
							if(this.speechPartService.isVirtualWord(word)){
								if(!ignoreVirtual){
									tokenisedDoc.addWord(l.getLexemeText());
								}
								
							}else{
								tokenisedDoc.addWord(l.getLexemeText());
							}
						}
						
					}else{
						if(singleWords.size()>0){
							this.mergeToWord(tokenisedDoc, singleWords);
							lastSingleWordPos=-1;
						}
					}
				}
		 
			
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tokenisedDoc;
	}
	
	private void mergeToWord(TokenisedDoc tokenisedDoc,List<Lexeme> singleWords){
		
		StringBuffer word = new StringBuffer();
		for(Lexeme lx:singleWords){
			word.append(lx.getLexemeText());
		}
		if(word.length()>0){
			tokenisedDoc.addWord(word.toString());
		}
		singleWords.clear();
	}
	 
}
