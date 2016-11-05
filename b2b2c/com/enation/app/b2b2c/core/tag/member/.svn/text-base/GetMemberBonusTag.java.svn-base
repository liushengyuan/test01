package com.enation.app.b2b2c.core.tag.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.IStoreBonusManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;

import freemarker.template.TemplateModelException;
@Component
public class GetMemberBonusTag extends BaseFreeMarkerTag{

	
	private IStoreMemberManager storeMemberManager;
	private IStoreBonusManager storeBonusManager;
	
	@SuppressWarnings("unchecked")
	@Override
	protected Object exec(Map param) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String member_id=param.get("member_id").toString();
		Map result = new HashMap();
		String page = request.getParameter("page");
		page = (page == null || page.equals("")) ? "1" : page;
		int pageSize = 10;
		
		Page webpage = this.storeBonusManager.getBonusListBymemberids(Integer.valueOf(page), pageSize,Integer.parseInt(member_id));
		Long totalCount = webpage.getTotalCount();
		
		List bonusList = (List) webpage.getResult();
		List<Map> list=(List<Map>)bonusList;
		for (Map map : list) {
			Integer data=0;
			Long use_end_time=Long.valueOf(map.get("active_end_time").toString()) ;
			Long time=DateUtil.getDateline();
			if(use_end_time-time<2*24*60*60){
				data=1;
			}else{
				data=0;
			}
			map.put("data", data);
		}
		bonusList = bonusList == null ? new ArrayList() : bonusList;

		result.put("totalCount", totalCount);
		result.put("pageSize", pageSize);
		result.put("page", page);
		result.put("bonusList", bonusList);
		
		return result;
	}
	
	

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}



	public IStoreBonusManager getStoreBonusManager() {
		return storeBonusManager;
	}



	public void setStoreBonusManager(IStoreBonusManager storeBonusManager) {
		this.storeBonusManager = storeBonusManager;
	}
}
