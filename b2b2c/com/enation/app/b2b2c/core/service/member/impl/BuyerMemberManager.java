package com.enation.app.b2b2c.core.service.member.impl;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.member.IBuyerMemberManager;
import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.database.BaseSupport;
@Component
/**
 * 会员管理
 * 
 * @author yanpeng 2015-6-9 下午1:36:45
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BuyerMemberManager extends BaseSupport<Member> implements
		IBuyerMemberManager {

	public void updateEmail(Integer memberid, String email) {
		String sql = "update member set email = ? where member_id =? ";
		this.baseDaoSupport.execute(sql, email, memberid);
	}

}
