package com.enation.app.b2b2c.core.service.impl;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.MemberCollect;
import com.enation.app.b2b2c.core.service.IStoreCollectManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;

/**
 * 收藏店铺	manager
 * @author xulipeng
 *
 */
@Component
public class StoreCollectManager extends BaseSupport implements IStoreCollectManager {

	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreCollectManager#addCollect(com.enation.app.b2b2c.core.model.MemberCollect)
	 */
	@Override
	public void addCollect(MemberCollect collect) {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		Integer num = this.baseDaoSupport.queryForInt("select count(0) from es_member_collect where member_id=? and store_id=?", collect.getMember_id(),collect.getStore_id());
		String alert = "";
		if(language=="zh"){
			 alert = "店铺已收藏!";
		}else{
			 alert = " магазин  уже  коллекции ";
		}
		
		if(num!=0){
			throw new RuntimeException(alert);
		}else{
			collect.setCreate_time(DateUtil.getDateline());
			this.baseDaoSupport.insert("es_member_collect", collect);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreCollectManager#delCollect(java.lang.Integer)
	 */
	@Override
	public void delCollect(Integer collect_id) {
		this.baseDaoSupport.execute("delete from es_member_collect where id=?",collect_id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreCollectManager#getList(java.lang.Integer, int, int)
	 */
	@Override
	public Page getList(Integer memberid,int page,int pageSize) {
		String sql = "select s.*,m.id as celloct_id,m.create_time as collect_time from es_store s LEFT JOIN es_member_collect m ON s.store_id=m.store_id where s.store_id in (select store_id from es_member_collect where m.member_id=?)";
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize, memberid);
		return webpage;
	}

}
