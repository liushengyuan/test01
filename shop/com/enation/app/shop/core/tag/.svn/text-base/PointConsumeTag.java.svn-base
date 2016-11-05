package com.enation.app.shop.core.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.model.PointConsume;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IPointConsumeManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
public class PointConsumeTag extends BaseFreeMarkerTag {
	private IPointConsumeManager pointConsumeManager;
	private IMemberManager memberManager;
	private IMemberLvManager memberLvManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map<String,Object>map = new HashMap();
		PointConsume pointconsumeCNY = this.pointConsumeManager.getCNYconsume();
		PointConsume pointconsumeRUB = this.pointConsumeManager.getRUBconsume();
		Double cny = (double) 0;
		Double rub = (double) 0;
		Member member = UserConext.getCurrentMember();
		MemberLv memberLv = new MemberLv();
		if(member!=null){
			member = this.memberManager.get(member.getMember_id());
			memberLv = memberLvManager.get(member.getLv_id());
		}
		if(pointconsumeCNY!=null){
			cny= pointconsumeCNY.getConsume_amount();
		}
		if(pointconsumeRUB!=null){
			rub = pointconsumeRUB.getConsume_amount();
		}
		map.put("CNY", cny);
		map.put("RUB", rub);
		if(member!=null){
			map.put("mp",member.getMp());
			map.put("lv",memberLv.getProportion());
		}else{
			map.put("mp",-1000);
			map.put("lv",-1000);
		}
		
		return map;
	}
	public IPointConsumeManager getPointConsumeManager() {
		return pointConsumeManager;
	}
	public void setPointConsumeManager(IPointConsumeManager pointConsumeManager) {
		this.pointConsumeManager = pointConsumeManager;
	}
	public IMemberManager getMemberManager() {
		return memberManager;
	}
	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}
	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}
	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	
	
	
}
