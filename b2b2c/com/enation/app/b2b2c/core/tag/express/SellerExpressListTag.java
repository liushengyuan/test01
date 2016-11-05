package com.enation.app.b2b2c.core.tag.express;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.SellerExpress;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.ISellerExpressManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取卖家自定义物流
 * @author WKZ
 * @date 2015-9-15 上午11:06:57
 */
@Component
public class SellerExpressListTag extends BaseFreeMarkerTag {
	
	@Autowired
	private ISellerExpressManager sellerExpressManager;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String ctx = this.getRequest().getContextPath();
		if("/".equals(ctx)){
			ctx="";
		}
		StoreMember member = storeMemberManager.getStoreMember();
		
		if (member == null) {
			HttpServletResponse response = ThreadContextHolder.getHttpResponse();
			try {
				response.sendRedirect(ctx+"/store/login.html");
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		StoreMember storeMember = storeMemberManager.getStoreMember();
		int storeid = storeMember.getStore_id();
		List<SellerExpress> list = sellerExpressManager.list(String.valueOf(storeid));
		if(list == null) {
			list = new ArrayList<SellerExpress>();
		}
		return list;
	}

}
