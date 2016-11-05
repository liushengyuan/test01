package com.enation.app.base.core.dic;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
public class ConfigurableContants {
	protected  Map<String, String> configItems = new LinkedHashMap<String, String>();
	protected   Logger logger = Logger
			.getLogger(this.getClass());


	public  void init(String propertyFileName) {
		InputStream ins = null;
		try {
			ins = Config.class.getClassLoader().getResourceAsStream(
					propertyFileName);

			ConfigXmlParser parser = new ConfigXmlParser();
			parser.doParse(ins);
			configItems = parser.getConfigItems();
		} catch (Exception e) {
			logger.error("load " + propertyFileName + " into Contants error");
			e.printStackTrace();
		} finally {
			if (ins != null)
				try {
					ins.close();
				} catch (IOException e) {
					logger.error(e);
				}
		}
	}

	public  String getConfigVal(String key, String defaultValue) {
		if (!configItems.containsKey(key))
			return defaultValue;
		else
			return configItems.get(key);

	}
	
	public  void setConfigVal(String key, String value) {
		 configItems.put(key,value);

	}


}
