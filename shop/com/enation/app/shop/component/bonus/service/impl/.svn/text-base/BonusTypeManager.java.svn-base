package com.enation.app.shop.component.bonus.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.bonus.model.BonusType;
import com.enation.app.shop.component.bonus.model.RegisterBonus;
import com.enation.app.shop.component.bonus.model.RegisterBonusRel;
import com.enation.app.shop.component.bonus.service.IBonusTypeManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;


/**
 * 红包类型管理
 * @author kingapex
 *2013-8-13下午3:10:21
 */
@Component
public class BonusTypeManager extends BaseSupport  implements IBonusTypeManager {

	@Override
	public void add(BonusType bronusType) {
		this.baseDaoSupport.insert("bonus_type", bronusType);

	}

	@Override
	public void update(BonusType bronusType) {
		this.baseDaoSupport.update("bonus_type", bronusType," type_id="+bronusType.getType_id());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)  
	public void delete(Integer[] bonusTypeId) {
		
		for(int typeid:bonusTypeId){
			this.baseDaoSupport.execute("delete from member_bonus where bonus_type_id=?", typeid);
			this.baseDaoSupport.execute("delete from bonus_type where type_id=?",typeid);
		}
	}

	@Override
	public Page list(int page, int pageSize) {
		String sql ="select * from bonus_type order by type_id desc";
		return this.baseDaoSupport.queryForPage(sql, page, pageSize, BonusType.class);
	}

	@Override
	public BonusType get(int typeid) {
		String sql ="select * from bonus_type  where type_id =?";
		return (BonusType) this.baseDaoSupport.queryForObject(sql, BonusType.class, typeid);
	}

	@Override
	public BonusType getBonusType(Double type_money_ru) {
		String sql = "SELECT * FROM es_bonus_type WHERE type_money_ru = ?";
		return (BonusType) this.baseDaoSupport.queryForObject(sql, BonusType.class, type_money_ru);
	}

	@Override
	public Page listStoreBouns(int page, int pageSize) {
		String sql ="select b.* ,s.store_name from es_bonus_type b LEFT JOIN es_store s ON b.store_id = s.store_id WHERE b.store_id IS NOT NULL order by type_id desc";
		return this.baseDaoSupport.queryForPage(sql, page, pageSize);
	}

	/**
	 * 根据发放类型查询优惠券
	 */
	@Override
	public List<BonusType> queryForBonustype(int sentype) {
		List<BonusType> btlist=null;
		String str="select * from es_register_bonus where is_true=1";
		List<RegisterBonus> list=this.baseDaoSupport.queryForList(str, RegisterBonus.class);
		if(list.size()>0){
			RegisterBonus registerBonus=list.get(0);
		    List<RegisterBonusRel> bonusRels=this.baseDaoSupport.queryForList("select * from es_register_bonus_rel where registerid=?", RegisterBonusRel.class,registerBonus.getId());
		    if(bonusRels.size()>0){
		    	btlist=new ArrayList<BonusType>();
		    	for (RegisterBonusRel registerBonusRel : bonusRels) {
		    		Long cometime=DateUtil.getDateline();
				    BonusType bonusType=(BonusType) this.baseDaoSupport.queryForObject("select * from bonus_type  where send_type =? and send_start_date<=? and send_end_date>=? and type_id=?", BonusType.class,sentype,cometime,cometime,registerBonusRel.getType_id());
				    btlist.add(bonusType);
				}
		    }else{
		    	btlist=new ArrayList<BonusType>();
		    }
		}else{
			btlist=new ArrayList<BonusType>();
		}
		return btlist;
	}

	@Override
	public void addRigisterBonus(RegisterBonus registerBonus) {
	    this.baseDaoSupport.insert("es_register_bonus", registerBonus);
	}

	@Override
	public Page listRigister(int page, int pageSize) {
		String sql="SELECT *,(SELECT COUNT(1) from es_register_bonus_rel WHERE registerid=b.id) AS number from es_register_bonus AS b ";
		return this.baseDaoSupport.queryForPage(sql, page, pageSize);
	}

	@Override
	public void deleteRegisterBonus(Integer[] id) {
		String sql="delete from es_register_bonus where id=?";
		String sql1="delete from es_register_bonus_rel where registerid=?";
		for (Integer integer : id) {
			this.baseDaoSupport.execute(sql1, integer);
			this.baseDaoSupport.execute(sql, integer);
		}
		
	}

	@Override
	public int findRegisterBonus(Integer[] id) {
		Integer count=0;
		Long nowtime=DateUtil.getDateline();
		String sql="select * from es_register_bonus where id in("+StringUtil.arrayToString(id, ",")+")";
		List<RegisterBonus> list=this.baseDaoSupport.queryForList(sql, RegisterBonus.class);
		for (RegisterBonus registerBonus : list) {
			if(registerBonus.getActive_start_time()<nowtime && nowtime<registerBonus.getActive_end_time()){
				count++;
			}
		}
		return count;
	}
	@Override
	public int findBonus(Integer[] type_id) {
		Integer count=0;
		Long nowdata=DateUtil.getDateline();
		String sql="select * from bonus_type where type_id in("+StringUtil.arrayToString(type_id, ",")+")";
		List<BonusType> list=this.baseDaoSupport.queryForList(sql, BonusType.class);
		for (BonusType BonusType : list) {
			if(BonusType.getUse_start_date()<nowdata && nowdata<BonusType.getUse_end_date()){
				count++;
			}
		}
		return count;
	}

	@Override
	public RegisterBonus lookRegisterBonus(Integer id) {
	   String sql="select * from es_register_bonus where id=?";
		return (RegisterBonus) this.baseDaoSupport.queryForObject(sql, RegisterBonus.class, id);
	}

	@Override
	public void updateRegister(Integer id,RegisterBonus registerBonus) {
		String sql="update es_register_bonus set active_start_time=?,active_end_time=?,active_now_time=?,is_true=?,name=? where id=?";
		this.baseDaoSupport.execute(sql,registerBonus.getActive_start_time(),registerBonus.getActive_end_time(),registerBonus.getActive_now_time(),registerBonus.getIs_true(),registerBonus.getName(),registerBonus.getId());
	}

	@Override
	public Page listRigisterJson(Integer activeid,int page, int pageSize) {
		String sql="select * from es_register_bonus_rel AS l LEFT JOIN es_bonus_type AS e ON l.type_id=e.type_id WHERE l.registerid=? ";
		return this.baseDaoSupport.queryForPage(sql, page, pageSize,activeid);
	}

	@Override
	public Page listRigisterForAllJson(int page, int pageSize,Integer activitid) {
		String sql="select * from es_bonus_type where type_id not in (select type_id from es_register_bonus_rel where registerid=?)";
		return this.baseDaoSupport.queryForPage(sql, page, pageSize,activitid);
	}

	@Override
	public void addRegisterRel(Integer id, Integer activid) {
		RegisterBonusRel bonusRel=new RegisterBonusRel();
		bonusRel.setRegisterid(activid);
		bonusRel.setType_id(id);
		this.baseDaoSupport.insert("es_register_bonus_rel", bonusRel);
	}

	@Override
	public void deleteRegister(Integer id) {
		String sql="delete from es_register_bonus_rel where rel_id=?";
		this.baseDaoSupport.execute(sql, id);
	}
}
