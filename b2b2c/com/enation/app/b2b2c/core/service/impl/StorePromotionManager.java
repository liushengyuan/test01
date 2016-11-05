package com.enation.app.b2b2c.core.service.impl;

import java.util.Date;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.StoreBonus;
import com.enation.app.b2b2c.core.service.IStorePromotionManager;
import com.enation.app.shop.component.bonus.service.IBonusTypeManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;

/**
 * 店铺促销管理 manager
 * @author xulipeng
 * 2015年1月12日23:14:29
 */
@Component
public class StorePromotionManager extends BaseSupport implements IStorePromotionManager {

	private IBonusTypeManager bonusTypeManager;
	
	@Override
	public void add_FullSubtract(StoreBonus bonus) {
		this.baseDaoSupport.insert("es_bonus_type", bonus);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void receive_bonus(Integer memberid, Integer storeid, Integer type_id) {
		StoreBonus bonus =	this.getBonus(type_id);
		int limit = bonus.getLimit_num();	//限领数量
		int num = this.getmemberBonus(type_id,memberid);
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage(); 
		if(num<limit){
			while(memberid!=null){
				String sn =this.createSn(bonus.getType_id()+"");
				int c= this.baseDaoSupport.queryForInt("select count(0) from member_bonus where bonus_sn=?", sn);
				if(c==0){
					this.baseDaoSupport.execute("insert into es_member_bonus(bonus_type_id,bonus_sn,type_name,bonus_type,create_time,member_id,type_name_ru)values(?,?,?,?,?,?,?)", type_id,sn,bonus.getType_name(),bonus.getSend_type(),DateUtil.getDateline(),memberid,bonus.getType_name_ru());
					return;
				}else{
					//System.out.println("有相同的sn码,在生成一个sn码");
				}
			}
		}else{
			if (language=="zh") {
				throw new RuntimeException("此优惠劵限领"+limit+"个.");
			}else {
				throw new RuntimeException("Этот купон ограничено получит"+limit+"шт.");
			}
			
		}
	}
	
	private String createSn(String prefix){
		
		StringBuffer sb = new StringBuffer();
		sb.append(prefix);
		sb.append( DateUtil.toString(new Date(), "yyMM"));
		sb.append( createRandom() );
		
		return sb.toString();
	}
	
	private String createRandom(){
		Random random  = new Random();
		StringBuffer pwd=new StringBuffer();
		for(int i=0;i<6;i++){
			pwd.append(random.nextInt(9));
			 
		}
		return pwd.toString();
	}
	
	@Override
	public StoreBonus getBonus(Integer type_id) {
		String sql ="select * from es_bonus_type  where type_id =?";
		return (StoreBonus) this.daoSupport.queryForObject(sql, StoreBonus.class, type_id);
	}
	
	@Override
	public int getmemberBonus(Integer type_id,Integer memberid) {
		String sql = "select count(0) from es_member_bonus where bonus_type_id=? and member_id=?";
		int num = this.daoSupport.queryForInt(sql, type_id,memberid);
		return num;
	}
	
	@Override
	public void edit_FullSubtract(StoreBonus bonus) {
		this.daoSupport.update("es_bonus_type", bonus, "type_id="+bonus.getType_id());
	}
	
	@Override
	public void deleteBonus(Integer type_id) {
		String sql ="select use_end_date from es_bonus_type where type_id="+type_id;
		long use_end_date = this.daoSupport.queryForLong(sql);
		if(DateUtil.getDateline()<use_end_date){
			throw new RuntimeException("此优惠劵未过期不能删除!");
		}else{
			this.daoSupport.execute("delete from es_bonus_type where type_id="+type_id);
		}
	}
	
	//set  get

	public IBonusTypeManager getBonusTypeManager() {
		return bonusTypeManager;
	}

	public void setBonusTypeManager(IBonusTypeManager bonusTypeManager) {
		this.bonusTypeManager = bonusTypeManager;
	}



	


}
