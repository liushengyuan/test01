package com.enation.app.b2b2c.core.tag.store;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 卖家发货地址下拉获取
 * @author WKZ
 * @date 2015-9-19 下午5:34:04
 */
@Component
public class SelectSellerAddressTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IMemberAddressManager memberAddressManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		Member member = UserConext.getCurrentMember();
		if(member==null){
			throw new TemplateModelException("未登录不能使用此标签[ConsigneeListTag]");
		}
		
		List<MemberAddress>  list = memberAddressManager.listAddressBySend();
		return list;
	}


}
