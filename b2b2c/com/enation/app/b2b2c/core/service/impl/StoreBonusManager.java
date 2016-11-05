package com.enation.app.b2b2c.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.StoreBonus;
import com.enation.app.b2b2c.core.service.IStoreBonusManager;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺优惠卷查询
 * @author xulipeng
 *
 */
@Component
public class StoreBonusManager extends BaseSupport implements IStoreBonusManager {
	private ICartManager cartManager;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.IStoreBonusManager#getBonusList(java.lang.Integer)
	 */
	@Override
	public List getBonusList(Integer store_id) {
		List list = this.baseDaoSupport.queryForList("select * from es_bonus_type where store_id=?", store_id);
		return list;
	}

	@Override
	public List<Map> getMemberBonusList(Integer memberid, Integer store_id,Double min_goods_amount) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());
		long l = DateUtil.getDateline(date+" 23:59:59");
		
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		List<CartItem> cartItemList = cartManager.listGoods(sessionid);
		//获取购物车中的第一件商品的信息
		CartItem cartItem=cartItemList.get(0);
		String currency = cartItem.getCurrency();
		
		StringBuffer sql = new StringBuffer("select * from es_member_bonus m left join es_bonus_type b on b.type_id = m.bonus_type_id ");
		sql.append(" where m.member_id="+memberid);
		sql.append(" and b.store_id="+store_id);
		sql.append(" and m.used = 0 ");//0为未使用的优惠券
		if(currency.equals("RUB")){
			sql.append(" and b.min_goods_amount_ru<="+min_goods_amount);
		}else if (currency.equals("CNY")) {
			sql.append(" and b.min_goods_amount<="+min_goods_amount);
		}
		sql.append(" and b.use_end_date>="+l);
		
		List list = this.daoSupport.queryForList(sql.toString());
		return list;
	}

	@Override
	public StoreBonus get(Integer bonusid) {
		String sql ="select * from es_bonus_type where type_id=?";
		StoreBonus bonus = (StoreBonus) this.daoSupport.queryForObject(sql, StoreBonus.class, bonusid);
		return bonus;
	}

	@Override
	public Page getBonusListBymemberid(int pageNo,int pageSize,Integer memberid) {
		String sql = "select m.*,b.*,s.store_name from es_member_bonus m left join es_bonus_type b on b.type_id = m.bonus_type_id"
				+ " left join es_store s on b.store_id=s.store_id where m.member_id="+memberid+" and b.type_name IS NOT NULL";
		////System.out.println(sql);
		Page webPage = this.daoSupport.queryForPage(sql, pageNo, pageSize);
		return webPage;
	}
	
	@Override
	public Page getBonusListBymemberids(int pageNo,int pageSize,Integer memberid) {
		String sql = "select m.*,b.*,r.*,s.store_name from es_member_bonus m  join es_bonus_type b on b.type_id = m.bonus_type_id"
				+" left join es_store s on b.store_id=s.store_id"
				+"  join es_register_bonus_rel rel on b.type_id = rel.type_id " 
				+"  join es_register_bonus r on r.id = rel.registerid"
				+" where m.member_id="+memberid+" and b.type_name IS NOT NULL";
		////System.out.println(sql);
		Page webPage = this.daoSupport.queryForPage(sql, pageNo, pageSize);
		return webPage;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setBonusUsed(Integer bonus_id,Integer member_id) {
		this.daoSupport.execute("update es_member_bonus set used=1 where  used=0 and bonus_type_id="+bonus_id+" and member_id="+member_id+" ORDER BY bonus_id ASC LIMIT 1");
		this.daoSupport.execute("update es_bonus_type set use_num=(use_num+1) where type_id="+bonus_id);
	}
	
	
	@Override
	public Page getConditionBonusList(Integer pageNo, Integer pageSize,Integer store_id,Map map){
		String keyword=String.valueOf(map.get("keyword"));
		String add_time_from=String.valueOf(map.get("add_time_from"));
		String add_time_to=String.valueOf(map.get("add_time_to"));
		StringBuffer sql =new StringBuffer("select * from es_bonus_type where store_id= "+ store_id);
		
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		if(language=="zh"){
			if(!StringUtil.isEmpty(keyword)&&!keyword.equals("null")){
				sql.append(" AND type_name like '%" + keyword + "%'");
			}
		}else{
			if(!StringUtil.isEmpty(keyword)&&!keyword.equals("null")){
				sql.append(" AND type_name_ru like '%" + keyword + "%'");
			}
		}
		
		if(!StringUtil.isEmpty(add_time_from)&&!add_time_from.equals("null")){
			sql.append(" AND use_start_date <="+DateUtil.getDateline(add_time_from));
		}
		if(!StringUtil.isEmpty(add_time_to)&&!add_time_to.equals("null")){
			sql.append(" AND use_end_date >="+DateUtil.getDateline(add_time_to));
		}
		Page rpage = this.daoSupport.queryForPage(sql.toString(),pageNo, pageSize, StoreBonus.class);
		 
		return rpage;
		
	}

	public ICartManager getCartManager() {
		return cartManager;
	}

	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void setBonusUsed(Integer bonus_id, Integer member_id,
			long used_time, Integer order_id, String order_sn, String member_name) {
		String sql = "update es_member_bonus set used=1 ,used_time="+used_time+",order_id="+order_id+",order_sn="+order_sn+",member_name='"+member_name+"' where bonus_type_id="+bonus_id+" and member_id="+member_id;
		//System.out.println("使用优惠券的:"+sql);
		this.daoSupport.execute("update es_member_bonus set used=1 ,used_time="+used_time+",order_id="+order_id+",order_sn="+order_sn+",member_name="+member_name+" where bonus_type_id="+bonus_id+" and member_id="+member_id);
		this.daoSupport.execute("update es_bonus_type set use_num=(use_num+1) where type_id="+bonus_id);
		
	}
	
	

}
