package com.enation.app.tradeease.core.service.translation;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class MicroTranslate {
//	public static void main(String[] args) throws Exception {
//		//System.out.println(translate("我不怕千万人阻挡，只怕自己投降", Language.CHINESE_SIMPLIFIED,
//				Language.ENGLISH));
//	}

	public static String translate(String text, Language from, Language to) {
		Translate.setClientId("wawj2");
		Translate
				.setClientSecret("JytvfNJpoH+1jEndYGYXOnTl16/C38vb1OVnArx7L1I=");
		String translateText = null;
		try {
			translateText = Translate.execute(text, from, to);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return translateText;
	}
}