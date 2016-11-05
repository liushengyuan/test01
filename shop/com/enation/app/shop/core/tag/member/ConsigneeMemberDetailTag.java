package com.enation.app.shop.core.tag.member;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 收货人详细标签
 * @author kingapex
 *2013-7-26下午2:26:12
 */
@Component
@Scope("prototype")
public class ConsigneeMemberDetailTag extends BaseFreeMarkerTag {
	private IMemberAddressManager memberAddressManager;
	
	/**
	 * 读取某个收货人的详细信息
	 * @param addressid：收货地址id,int型，必填项
	 * @return 会员地址，即MemberAddress
	 * {@link MemberAddress}
	 */
	@Override
	public Object exec(Map arg) throws TemplateModelException {
		Map<String,Object> addressDetail=new HashMap<String,Object>();
		Integer addressid = Integer.parseInt((String)arg.get("addressid"));
		if(addressid == null){
			throw new TemplateModelException("必须提供收货地址id参数");
		}
		MemberAddress address = memberAddressManager.getAddress( addressid);
		if(address==null){
			 return "0";
		}else{
			addressDetail.put("address", address);
			this.returnMobileAndTel(addressDetail,address);
		}
		return addressDetail;//memberAddressManager.getAddress( addressid);
	}
	public void returnMobileAndTel(Map map,MemberAddress address){
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		if(!StringUtil.isEmpty(address.getMobile())){
			if(language=="zh"){
				map.put("mobile", address.getMobile());  
			}else{
				String mobile1=(address.getMobile().split("\\(", 2)[1]).split("\\)", 2)[0];
				String mobile2=address.getMobile().split("\\)", 2)[1].split("\\-", 2)[0];
				String mobile3=address.getMobile().split("\\-")[1];
				String mobile4=address.getMobile().split("\\-")[2];
				map.put("mobile1", mobile1);
				map.put("mobile2", mobile2);
				map.put("mobile3", mobile3);
				map.put("mobile4", mobile4);
			}
		}
		if(!StringUtil.isEmpty(address.getTel())){
			if(language=="zh"){
				map.put("tel",address.getTel()); 
			}else{
				String tel1=address.getTel().split("\\(", 2)[1].split("\\)", 2)[0];
				String tel2=address.getTel().split("\\)", 2)[1];
				map.put("tel1",tel1);
				map.put("tel2",tel2);
			}
		}
	}
	public IMemberAddressManager getMemberAddressManager() {
		return memberAddressManager;
	}
	
	public void setMemberAddressManager(IMemberAddressManager memberAddressManager) {
		this.memberAddressManager = memberAddressManager;
	}

} 
