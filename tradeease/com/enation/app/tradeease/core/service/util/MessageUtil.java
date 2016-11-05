package com.enation.app.tradeease.core.service.util;

import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
public class MessageUtil {
	
	public final static String TYPE = "type";
	public final static String DATA = "data";
	
	public final static String MESSAGE = "message";
	
	public final static String USER = "user";
	
	public static HashMap<String,String> getMessage(CharBuffer msg) {
		HashMap<String,String> map = new HashMap<String,String>();
		String msgString  = msg.toString();
		String m[] = msgString.split("`,:");
		map.put("fromName", m[0]);
		map.put("toName", m[1]);
		map.put("content", m[2]);
		return map;
	}

	public static String sendContent(String type, String content) {
		Map<String,Object> userMap = new HashMap<String,Object>();
		userMap.put(MessageUtil.TYPE, type);
		content = content.trim();
		/*if(content.split(":").length!=1){
			String[] s = content.split(":");
			userMap.put(MessageUtil.DATA, "["+s[0]+","+s[1]+"]");
		}
		else{
			String a = null;
			if(content.substring(5).length()!=0){
				a = content.substring(5);
			}else{
				a = "111";
			}
		}*/
		userMap.put(MessageUtil.DATA, content);
		return content;
	}
}