package com.enation.eop.processor;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.enation.framework.util.FileUtil;

/**
 * 安全的httprequest包装器
 * 
 * @author kingapex
 * 
 */
public class SafeHttpRequestWrapper extends HttpServletRequestWrapper {

	public SafeHttpRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	/**
	 * 对字符进行安全过滤
	 * 
	 * @param value
	 * @return
	 */
	private  String safeFilter(String value) {
		  if (value != null) {

	            // Avoid null characters
	            value = value.replaceAll("", "");

	            // Avoid anything between script tags
	            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid anything in a src='...' type of e­xpression
	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Remove any lonesome </script> tag
	            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Remove any lonesome <script ...> tag
	            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid eval(...) e­xpressions
	            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid e­xpression(...) e­xpressions
	            scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid javascript:... e­xpressions
	            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid vbscript:... e­xpressions
	            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid onload= e­xpressions
	            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	        }
	        return value;
	}

	/**
	 * 对字串数组进行安全过滤
	 * 
	 * @param values
	 */
	private void safeFilter(String[] values) {
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				values[i] = this.safeFilter(values[i]);
			}
		}
	}

	public String getParameter(String name) {
		String value = super.getParameter(name);
		value = this.safeFilter(value);
		return value;
	}

	public Map getParameterMap() {
		Map map = super.getParameterMap();
		Iterator keiter = map.keySet().iterator();
		while (keiter.hasNext()) {
			String name = keiter.next().toString();
			Object value = map.get(name);
			if (value instanceof String) {
				value = this.safeFilter(((String) value));
			}
			if (value instanceof String[]) {
				String[] values = (String[]) value;
				this.safeFilter(values);
			}
		}
		return map;
	}

	public String[] getParameterValues(String arg0) {
		String[] values = super.getParameterValues(arg0);
		this.safeFilter(values);
		return values;
	}

	

}
