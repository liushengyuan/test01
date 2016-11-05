package com.enation.app.base.core.dic;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
public class Config extends ConfigurableContants {
	private static Map<String, ConfigurableContants> configs = Collections.synchronizedMap(new HashMap<String, ConfigurableContants>());
	private static final void initConfig(String configFile){
		ConfigurableContants cfg = new ConfigurableContants();
		cfg.init(configFile);
		configs.put(configFile, cfg);
	}

	public final static String getProperty(String configFile,String key, String defaultValue) {
		if(!(configs.containsKey(configFile))){
			initConfig(configFile);
		}
			
		return configs.get(configFile).getConfigVal(key, defaultValue);
		
	}
	
	public final static void setProperty(String configFile,String key, String value) {
		if(!(configs.containsKey(configFile))){
			initConfig(configFile);
		}
			
		configs.get(configFile).setConfigVal(key, value);
		
	}

	public static void reload(String configFile) {
		initConfig(configFile);
		
	}
	
	

}
