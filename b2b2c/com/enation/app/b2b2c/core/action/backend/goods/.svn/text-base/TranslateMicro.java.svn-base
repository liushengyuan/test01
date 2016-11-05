package com.enation.app.b2b2c.core.action.backend.goods;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class TranslateMicro {
	public String translate(String text, Language from, Language to) {
		Translate.setClientId("wawj3");
		Translate.setClientSecret("pB4FCbks8wgBAGDxKZKyAdVxQvD5bf2YM9R4ImWu+J4=");
		String translateText = null;
		try {
			translateText = Translate.execute(text, from, to);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(translateText.length()>22){
			translateText = translateText.subSequence(0, 21).equals("TranslateApiException")?"":translateText;
		}
		return translateText;
	}
}