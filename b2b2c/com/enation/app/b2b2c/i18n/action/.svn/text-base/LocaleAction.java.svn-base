package com.enation.app.b2b2c.i18n.action;

import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

@Component
@ParentPackage("eop_default")
@Namespace("/i18n")
@Action("locale")
public class LocaleAction extends WWAction {

    private static final long serialVersionUID = 1L;

    private String localeStr;

    public String getLocaleStr() {
        return localeStr;
    }

    public void setLocaleStr(String localeStr) {
        this.localeStr = localeStr;
    }

    public String setLocale() throws Exception {
        HttpSession session = ServletActionContext.getRequest().getSession();

        this.localeStr = ServletActionContext.getRequest().getParameter("locale");

        Locale locale = (Locale)session.getAttribute("locale");

        if (!StringUtil.isEmpty(this.localeStr)) {
            locale = new Locale(this.localeStr);
            session.setAttribute("locale" , locale);
            session.setAttribute("WW_TRANS_I18N_LOCALE" , locale);//新增struts2国际化
        }
        this.showSuccessJson("");
        return this.JSON_MESSAGE;
    }
}
