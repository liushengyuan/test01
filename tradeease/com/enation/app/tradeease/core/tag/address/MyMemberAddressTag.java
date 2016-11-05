package com.enation.app.tradeease.core.tag.address;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 订单详细地址标签
 * 
 * @author yanpeng 2015-6-30 下午8:48:57
 * 
 */
@Component
@Scope("prototype")
public class MyMemberAddressTag extends BaseFreeMarkerTag {
	private IMemberAddressManager memberAddressManager;

	/**
	 * 根据addr_id查询订单的地址
	 * 
	 */

	@Override
	public Object exec(Map args) throws TemplateModelException {
		Integer address_id = StringUtil.toInt((String) args.get("address_id"));

		if (address_id == null
				&& StringUtil.isEmpty((String) args.get("address_id"))) {
			throw new TemplateModelException("必须传递address_id参数");
		}
		MemberAddress memberAddress = null;

		if (address_id != null) {
			memberAddress = memberAddressManager.getAddress(address_id);
		}
		return memberAddress;
	}

	public IMemberAddressManager getMemberAddressManager() {
		return memberAddressManager;
	}

	public void setMemberAddressManager(
			IMemberAddressManager memberAddressManager) {
		this.memberAddressManager = memberAddressManager;
	}
}
