package com.enation.app.tradeease.core.action.api.accont;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.framework.action.WWAction;

/**
 * 买家中心站内信
 * 
 * @author yanpeng 2015-6-12 下午6:11:49
 * 
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("accont")
@SuppressWarnings({ "rawtypes", "unchecked", "serial", "static-access" })
public class AccontDetailApiAction extends WWAction {
	
	
	
	
	@SuppressWarnings("unused")
	public String searchAccountDetails(){
		
		return this.JSON_MESSAGE;
	}

}
