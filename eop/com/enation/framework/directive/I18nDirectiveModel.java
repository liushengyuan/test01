package com.enation.framework.directive;

import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.opensymphony.xwork2.ActionContext;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class I18nDirectiveModel implements TemplateDirectiveModel {


	private static final Log loger = LogFactory.getLog(I18nDirectiveModel.class);

	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		String text = params.get("text").toString();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = request.getSession();

		Locale locale = (Locale)session.getAttribute("locale");

		ResourceBundle bundle = ResourceBundle.getBundle("message", locale);
		String value = text;

		try{
			value = bundle.getString(text);
		}catch(Exception e){
			if(loger.isDebugEnabled()){
				loger.error("【获取国际化资源异常】"+ e.toString());
			}
		}
		String label = MessageFormat.format(value, new Object[]{});
		env.getOut().write(label);
	}
}
