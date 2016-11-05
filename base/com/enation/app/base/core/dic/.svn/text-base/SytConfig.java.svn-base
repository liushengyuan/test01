package com.enation.app.base.core.dic;

public class SytConfig {

	private static final String CONFIG_FILE="syt_config.xml";
	/**
	 * 从main/resources/syt_config.xml中获取配置值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static final String getProperty(String key,String defaultValue){
		return Config.getProperty(CONFIG_FILE, key, defaultValue);
	}
	public static final void setProperty(String key,String value){
		Config.setProperty(CONFIG_FILE, key, value);
	}
}
