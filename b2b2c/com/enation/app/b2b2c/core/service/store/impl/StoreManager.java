package com.enation.app.b2b2c.core.service.store.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.component.plugin.store.StorePluginBundle;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.model.storeDTO.storeAndMemberDTO;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.member.impl.StoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.b2b2c.core.service.store.IStoreSildeManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.Withdrawal;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.core.model.Specification;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.app.tradeease.core.model.account.SellerCard;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
@Component
public class StoreManager  extends BaseSupport implements IStoreManager{
	private IStoreMemberManager storeMemberManager;
	private IStoreSildeManager storeSildeManager;
	private IRegionsManager regionsManager;
	private StorePluginBundle storePluginBundle;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#apply(com.enation.app.b2b2c.core.model.Store)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void apply(Store store) {
		//获取当前用户信息
		Member member=storeMemberManager.getStoreMember();
		if (member != null) {
			store.setMember_id(member.getMember_id());
			store.setMember_name(member.getUname());
		}
		//this.getStoreRegions(store);
		this.daoSupport.insert("es_store", store);
		store.setStore_id(this.daoSupport.getLastId("es_store"));
		storePluginBundle.onAfterApply(store);
	}
	/**
	 * 获取店铺地址
	 * @param store
	 */
	private void getStoreRegions(Store store){
		store.setStore_province(regionsManager.get(store.getStore_provinceid()).getLocal_name());
		store.setStore_city(regionsManager.get(store.getStore_cityid()).getLocal_name());
		store.setStore_region(regionsManager.get(store.getStore_regionid()).getLocal_name());
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#audit_pass(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void audit_pass(Integer member_id,Integer storeId,Integer pass,Integer name_auth,Integer store_auth,Double commission,String check_description) {
		if(pass==1){
			store_auth=store_auth==null?0:store_auth;
			name_auth=name_auth==null?0:name_auth;
			this.daoSupport.execute("update es_store set name_auth=?,store_auth=?,commission=?,check_time=?,check_description=? where store_id=?",name_auth,store_auth,commission,DateUtil.getDateline(),check_description,storeId);
			this.editStoredis(1, storeId);
			storePluginBundle.onAfterPass(this.getStore(storeId));
		}else{
			//审核未通过
			this.daoSupport.execute("update es_store set disabled=?,check_description=?,check_time=? where store_id=?",-1,check_description,DateUtil.getDateline(),storeId);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#store_list(java.util.Map, int, int)
	 */
	@Override
	public Page store_list(Map other,Integer disabled,int pageNo,int pageSize) {
		StringBuffer sql=new StringBuffer("");
		disabled=disabled==null?-2:disabled;
		String store_name=other.get("name")==null?"":other.get("name").toString();
		String searchType=other.get("searchType")==null?"":other.get("searchType").toString();
		Integer creditAccountStatus=(Integer) other.get("credit_account_status")==null?-2:(Integer) other.get("credit_account_status");
		
		//高级搜索字段
		String adv_storeName = (String) other.get("adv_storeName");
		String adv_memberName = (String) other.get("adv_memberName");
		String start_time = (String) other.get("start_time");
		String end_time = (String) other.get("end_time");
		String parentStore = (String) other.get("parentStore");
		String storeStatus = (String) other.get("storeStatus");
		String storeCountry = (String) other.get("storeCountry");
		Integer store_initiallist =(Integer) other.get("store_initiallist");
		
		

		//店铺状态
	
		if (!StringUtil.isEmpty(storeStatus)&&!storeStatus.equals("-2")) {
			sql.append("select s.* from es_store s   where disabled="+storeStatus );
		}else{
		if(disabled.equals(-2)){
			
				sql.append("select s.* from es_store s   where 1=1" );
//			}else {
//				sql.append("select s.* from es_store s   where disabled="+storeStatus );
//			}
			
		}
		else{
			sql.append("select s.* from es_store s   where  disabled="+disabled);
		}
		}
		if(creditAccountStatus!=-2){
			sql.append( "  and s.credit_account_status = " + creditAccountStatus );
		}
		if(StringUtil.isEmpty(adv_storeName)&&StringUtil.isEmpty(adv_memberName) ){
		if(!StringUtil.isEmpty(store_name)){
			sql.append( "  and (s.store_name like '%" + store_name + "%' or s.member_name like '%" + store_name + "%')");
		}
		}else if (!StringUtil.isEmpty(adv_storeName)) {
			sql.append(" AND s.store_name like '%" + adv_storeName + "%' ");
		}else if (!StringUtil.isEmpty(adv_memberName)) {
			sql.append(" AND s.member_name like '%" + adv_memberName + "%' " );
		}
		
	/*	if(!StringUtil.isEmpty(adv_storeName)) {
			sql.append(" AND s.store_name = '" + adv_storeName + "' " );
		}
		
		if(!StringUtil.isEmpty(adv_memberName)) {
			sql.append(" AND s.member_name = '" + adv_memberName + "' " );
		}*/
		
		if(!StringUtil.isEmpty(start_time)) {
			long startTime = DateUtil.getDateline(start_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			sql.append(" AND s.create_time > " + startTime );
		}
		
		if(!StringUtil.isEmpty(end_time)) {
			long endTime = DateUtil.getDateline(end_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			sql.append(" AND s.create_time < " + endTime );
		}
		
			if(!StringUtil.isEmpty(parentStore)) {
				if (!parentStore.equals("3")) {
				sql.append(" AND s.parent_store = " + parentStore );
			}
		}
		
		
			if(store_initiallist!=null) {
				if(store_initiallist!=0){
					sql.append(" AND s.store_initiallist = '" + store_initiallist + "' ");
				}
				
			}
	
			if(!StringUtil.isEmpty(storeCountry)) {
				if (!storeCountry.equals("1")) {
				sql.append(" AND s.store_country = '" + storeCountry + "' ");
		    }
		}

		if(!StringUtil.isEmpty(searchType)&&!searchType.equals("default")){
			sql.append(" order by "+searchType+" desc");
		}else{
			sql.append(" order by store_id"+" desc");
		}
		
		////System.out.println(sql.toString());
		return this.daoSupport.queryForPage(sql.toString(),pageNo ,pageSize);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#disStore(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void disStore(Integer storeId) {
		this.daoSupport.execute("update es_store set  end_time=? where store_id=?",DateUtil.getDateline(), storeId);
		this.editStoredis(2, storeId);
		this.daoSupport.execute("update es_member set is_store="+3+" where member_id="+this.getStore(storeId).getMember_id());
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#useStore(java.lang.Integer)
	 */
	@Override
	public void useStore(Integer storeId) {
		this.editStoredis(1, storeId);
		this.daoSupport.execute("update es_member set is_store="+1+" where member_id="+this.getStore(storeId).getMember_id());
	}
	/**
	 * 更改店铺状态
	 * @param disabled
	 * @param store_id
	 */
	private void editStoredis(Integer disabled,Integer store_id){
		this.daoSupport.execute("update es_store set disabled=? where store_id=?",disabled,store_id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#getStore(java.lang.Integer)
	 */
	@Override
	public Store getStore(Integer storeId) {
		String sql="select * from es_store where store_id="+storeId;
		List<Store> list = this.baseDaoSupport.queryForList(sql,Store.class);
		Store store = (Store) list.get(0);
		if(store.getId_img()!=null&&!StringUtil.isEmpty(store.getId_img())){			
			store.setId_img( UploadUtil.replacePath(store.getId_img()));
		}
		if(store.getLicense_img()!=null&&!StringUtil.isEmpty(store.getLicense_img())){			
			store.setLicense_img( UploadUtil.replacePath(store.getLicense_img()));
		}
		return store;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#editStore(com.enation.app.b2b2c.core.model.store.Store)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void editStore(Store store){
		StoreMember member=storeMemberManager.getStoreMember();
		this.daoSupport.update("es_store", store, "store_id="+member.getStore_id());
		if(store.getDisabled()==1){
			this.daoSupport.execute("update  es_member set is_store=1 where member_id=?",member.getMember_id() );
		}else{
			this.daoSupport.execute("update  es_member set is_store=2 where member_id=?",member.getMember_id() );
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#editStoreInfo(com.enation.app.b2b2c.core.model.store.Store)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void editStoreInfo(Store store){
		 this.daoSupport.update("es_store", store, " store_id="+store.getStore_id());
	}
	 public void editStore(Map store) {
		 this.daoSupport.update("es_store", store, " store_id="+store.get("store_id"));
	 }
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#checkStore()
	 */
	@Override
	public boolean checkStore() {
		Member member=storeMemberManager.getStoreMember();
		String sql="select count(store_id) from es_store where member_id=?";
		int isHas= this.daoSupport.queryForInt(sql, member.getMember_id());
		if(isHas>0)
			return true;
		else
			return false;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#save(com.enation.app.b2b2c.core.model.Store)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Store store) {
		store.setMember_id(this.storeMemberManager.getMember(store.getMember_name()).getMember_id());
		store.setCreate_time(DateUtil.getDateline());
		this.daoSupport.insert("es_store", store);
		store.setStore_id(this.daoSupport.getLastId("es_store"));
		
		storePluginBundle.onAfterPass(store);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#checkIdNumber(java.lang.String)
	 */
	@Override
	public Integer checkIdNumber(String idNumber,int member_id) {
		String sql = "select member_id from member where member_id!="+member_id+" and id_number =?";
		List result = this.baseDaoSupport.queryForList(sql, idNumber);
		return  result.size() > 0 ? 1 : 0;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#editStoreOnekey(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void editStoreOnekey(String key, String value) {
		StoreMember member=storeMemberManager.getStoreMember();
		Map map=new HashMap();
		map.put(key,value);
		this.daoSupport.update("es_store", map, "store_id="+member.getStore_id());
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#collect(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addcollectNum(Integer storeid) {
		String sql = "update es_store set store_collect = store_collect+1 where store_id=?";
		this.baseDaoSupport.execute(sql, storeid);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#getStore(java.lang.String)
	 */
	@Override
	public boolean checkStoreName(String storeName) {
		//Member member = UserConext.getCurrentMember();
		StoreMember storeMember=storeMemberManager.getStoreMember();
		String sql="select  count(store_id) from es_store where store_name=? and disabled=1";
		if(storeMember.getStore_id()!=null){
			sql=sql+" and store_id!="+storeMember.getStore_id();;
		}
		Integer count= this.daoSupport.queryForInt(sql, storeName);
		return count!=0?true:false;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#reduceCollectNum(java.lang.Integer)
	 */
	@Override
	public void reduceCollectNum(Integer storeid) {
		String sql = "update es_store set store_collect = store_collect-1 where store_id=?";
		this.baseDaoSupport.execute(sql, storeid);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#saveStoreLicense(java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveStoreLicense(Integer storeid, String id_img,
			String license_img, Integer store_auth, Integer name_auth) {
		if(store_auth==2){
			this.daoSupport.execute("update es_store set store_auth=?,license_img=? where store_id=?",store_auth,license_img,storeid);
		}
		if(name_auth==2){
			this.daoSupport.execute("update es_store set name_auth=?,id_img=? where store_id=?",name_auth,id_img,storeid);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#auth_list(java.util.Map, java.lang.String, int, int)
	 */
	@Override
	public Page auth_list(Map other, Integer disabled, int pageNo, int pageSize) {
		StringBuffer sql=new StringBuffer("select s.* from es_store s   where  disabled="+disabled);
		sql.append(" and (store_auth=2 or name_auth=2)");
		return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#auth_pass(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void auth_pass(Integer store_id, Integer name_auth,
			Integer store_auth) {
		if(store_auth!=null){
				this.daoSupport.execute("update es_store set store_auth=? where store_id=?",store_auth,store_id);
		}
		if(name_auth!=null){
			this.daoSupport.execute("update es_store set name_auth=? where store_id=?",name_auth,store_id);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#getStoreByMember(java.lang.Integer)
	 */
	@Override
	public Store getStoreByMember(Integer memberId) {
		return (Store)this.daoSupport.queryForObject("select * from es_store where member_id=?", Store.class, memberId);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#reApply(com.enation.app.b2b2c.core.model.store.Store)
	 */
	@Override
	public void reApply(Store store) {
		//获取当前用户信息
		Member member=storeMemberManager.getStoreMember();
		if (member != null) {
			store.setMember_id(member.getMember_id());
			store.setMember_name(member.getUname());
		}
		this.daoSupport.update("es_store", store, "store_id="+store.getStore_id());
		storePluginBundle.onAfterApply(store);
	}
	public StorePluginBundle getStorePluginBundle() {
		return storePluginBundle;
	}
	public void setStorePluginBundle(StorePluginBundle storePluginBundle) {
		this.storePluginBundle = storePluginBundle;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	public IStoreSildeManager getStoreSildeManager() {
		return storeSildeManager;
	}

	public void setStoreSildeManager(IStoreSildeManager storeSildeManager) {
		this.storeSildeManager = storeSildeManager;
	}
	public IRegionsManager getRegionsManager() {
		return regionsManager;
	}
	public void setRegionsManager(IRegionsManager regionsManager) {
		this.regionsManager = regionsManager;
	}
	@Override
	public void editSeller(Map map) {
		 this.daoSupport.update("es_member", map, " member_id="+map.get("member_id"));
		
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void insertWithdrawal(Withdrawal withdrawal) {
		//this.daoSupport.insert("es_withdrawal", withdrawal);
		//withdrawal.setId(this.daoSupport.getLastId("es_withdrawal"));
		withdrawal.setSerial_number(Integer.parseInt(String.valueOf(DateUtil
				.getDateline())));
		//System.out.println(withdrawal.getSerial_number());
		this.daoSupport.insert("es_withdrawal", withdrawal);
		
	}
	/*
	 * Map map=new HashMap(); map.put("id",withdrawal.getId());
	 * map.put("serial_number",withdrawal.getSerial_number());
	 * map.put("capital_channel",withdrawal.getCapital_channel());
	 * map.put("account",withdrawal.getAccount());
	 * map.put("note",withdrawal.getNote());
	 * map.put("member_id",withdrawal.getMember_id());
	 * map.put("create_date",withdrawal.getCreate_date());
	 * map.put("number",withdrawal.getNumber());
	 * map.put("details",withdrawal.getDetails());
	 * map.put("type",withdrawal.getType());
	 * map.put("state",withdrawal.getState());
	 * map.put("store_id",withdrawal.getStore_id());
	 * 
	 * this.daoSupport.update("es_withdrawal", map, "id="+map.get("id"));
	 */

	/*
	 * public Page getWithdrawalList(Integer pageNo, Integer pageSize,Integer
	 * store_id,Map map){ StringBuffer sql=new
	 * StringBuffer("select s.* from es_Withdrawal s   where  store_id="
	 * +store_id);
	 * 
	 * return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize); }
	 */
	@Override
	public Page getWithdrawalList(int pageNo, int pageSize, Map map,Integer store_id) {
		StringBuffer sql = new StringBuffer(
				"select s.* from es_withdrawal s where s.store_id=" +store_id);
		
		sql.append(" ORDER BY s.id DESC");
		return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
	}
	@Override
	public Page getAllWithdrawalList(int pageNo, int pageSize, Map map) {
		StringBuffer sql = new StringBuffer(
				"select s.* from es_withdrawal s ");
		sql.append(" ORDER BY s.id DESC");
		return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
	}
	@Override
	public void deleteWd(String id){
/*		Map map=new  HashMap();
		map.put("state", 3);
		this.daoSupport.update("es_withdrawal", map, " id="+id);*/
		int status=this.daoSupport.queryForInt("select state from es_withdrawal where id=?", id);
		if(status==0){
			this.daoSupport.execute("delete from es_withdrawal where id=?",id);
		}
		
		
	}
	@Override
	public Page getWithdrawalListAll(int pageNo, int pageSize, Map map) {
		StringBuffer sql = new StringBuffer(
				"select s.* from es_withdrawal s where s.state=0 ORDER BY s.id DESC" );

		return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
	}

	@Override
	public void wdAuditok(Map map) {
//		Member member = UserConext.getCurrentMember();
		AdminUser admin=UserConext.getCurrentAdminUser();
		//Store store=new Store();
		//System.out.println(map.get("store_id")+"kk");
		int storeid=(Integer) map.get("store_id");
		Store store=(Store)this.daoSupport.queryForObject("select * from es_store  where store_id=? ",Store.class,storeid);
		//System.out.println(store.toString()+"jii");
		double account=store.getAccount();
		double account2=account-(Double)map.get("number");
		if(account>0&&account2>0){
			Map map2=new HashMap();
			map2.put("state",1);
			map2.put("id", map.get("id"));
			//System.out.println(DateUtil.getDateline());
			map2.put("ok_date",DateUtil.getDateline());
			//System.out.println(map2.get("ok_date"));
			map2.put("operator_id",admin.getUserid());
			
			this.daoSupport.update("es_withdrawal", map2,
					" id=" + map2.get("id"));
		
		Map map3=new HashMap();
		map3.put("account",account2);
		String store_id=map.get("store_id")+"";
		this.daoSupport.update("es_store", map3,
				" store_id=" + store_id);
		Member member2=new Member();
		member2=(Member)daoSupport.queryForObject(
				"select * from es_member where member_id=?", Member.class,
				map.get("member_id"));
		
		Map map4=new HashMap();
		map4.put("member_id",map.get("member_id"));
		map4.put("store_id",map.get("store_id"));
		map4.put("unique_id",DateUtil.toString(new Date(), "yyMMddhhmmss")+UUID.randomUUID().toString().substring(0, 7));
		map4.put("member_name",member2.getName());//鍟嗘埛鍚?
		map4.put("store_name",store.getStore_name());//搴楅摵鍚?
		map4.put("amount_type",1);//閲戦绫诲瀷
	//	map4.put("Account_Type",1);//甯愭埛鍚嶆垚
		map4.put("outlay_amount",map.get("number"));//鏀嚭閲戦

		map4.put("balance",map3.get("account"));//骞冲彴璐︽埛浣欓
		map4.put("credit_balance",store.getCredit_account());//淇濊瘉閲戣处鎴蜂綑棰?
		map4.put("type",3);//浜ゆ槗绫诲瀷3

	//	map4.put("tx_number",Integer.parseInt(DateUtil.getDateline()));//浜ゆ槗搴忓垪鍙?
		map4.put("create_time",DateUtil.getDateline());//鐢熸垚鏃堕棿

		map4.put("oper_id",admin.getUserid());//鎿嶄綔鍛榠d

		map4.put("oper_name",admin.getUsername());//鎿嶄綔鍛樺鍚?
		
		this.adddetail(map4);
		}
		
	}
	public void adddetail(Map map) {
	
		this.baseDaoSupport.insert("es_account_detail", map);
		int rec_id = this.baseDaoSupport.getLastId("es_account_detail");
		map.put("rec_id",rec_id);
		Map map5=new HashMap();
		map5.put("create_time", map.get("create_time"));
		map5.put("store_name", map.get("store_name"));
		map5.put("type", map.get("type"));
		this.baseDaoSupport.update("es_account_detail", map, map5);
	}
	@Override
	public void Nowd(String id) {
		
		AdminUser admin=UserConext.getCurrentAdminUser();
		Map map2=new HashMap();
		map2.put("state",2);
		map2.put("ok_date", DateUtil.getDateline());
		map2.put("operator_id",admin.getUserid());
		this.daoSupport.update("es_withdrawal", map2,
				" id=" + id);
	}
public void okwd(String id) {
		
		AdminUser admin=UserConext.getCurrentAdminUser();
		Map map2=new HashMap();
		map2.put("state",4);
		map2.put("ok_date", DateUtil.getDateline());
		map2.put("operator_id",admin.getUserid());
		this.daoSupport.update("es_withdrawal", map2,
				" id=" + id);
	}
	@Override
	public Page searchWds(Map goodsMap, int page, int pageSize) {
			String sql = creatWdSql(goodsMap);
	StringBuffer _sql = new StringBuffer(sql);
	/*this.goodsPluginBundle.onSearchFilter(_sql);*/
	
	Page webpage = this.daoSupport.queryForPage(_sql.toString(), page,pageSize);
	return webpage;
	}
	@Override
	public  List<Withdrawal> getwdaa(){
		 String sql= "select s.* from es_withdrawal s where  s.state=4 ORDER BY s.id DESC";
		List<Withdrawal> list =this.daoSupport.queryForList(sql,Withdrawal.class);
		
		//System.out.println("aaaaaaaaaaaaaa"+list.size());
		return list;
		
	}
	private String creatWdSql(Map goodsMap){
	String	sql="select s.* from es_withdrawal s where s.state=0 or s.state=4 ORDER BY s.id DESC ";
		//System.out.println(sql);
		return sql;
	}
	@Override
	public Page searchAllWds(Map goodsMap, int page, int pageSize) {
		String sql = creatAllWdSql(goodsMap);
  StringBuffer _sql = new StringBuffer(sql);
   /*this.goodsPluginBundle.onSearchFilter(_sql);*/

   Page webpage = this.daoSupport.queryForPage(_sql.toString(), page,pageSize);
   return webpage;
}
	private String creatAllWdSql(Map goodsMap){
		String	sql="select s.* from es_withdrawal s ORDER BY s.id DESC ";
			//System.out.println(sql);
			return sql;
		}
	public SellerCard defaultCard(){
		StoreMember member=storeMemberManager.getStoreMember();
		String sql="select s.* from es_seller_card s where s.is_enable=1 and s.status=1 and s.store_id="+member.getStore_id();
	
		SellerCard sc = (SellerCard) this.daoSupport.queryForObject(sql, SellerCard.class);
	
		return sc;
		
	}
	@Override
	public String getStoreMarket(Integer storeId) {
		String sql = "SELECT store_market FROM store WHERE store_id = " + storeId;
		return this.baseDaoSupport.queryForString(sql);
	}
	@Override
	public List<Store> store_list(Map other, Integer disabled) {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer("");
		disabled=disabled==null?1:disabled;
		String store_name=other.get("name")==null?"":other.get("name").toString();
		String searchType=other.get("searchType")==null?"":other.get("searchType").toString();
		//Integer creditAccountStatus=(Integer) other.get("credit_account_status")==null?-2:(Integer) other.get("credit_account_status");
		
		//高级搜索字段
		String adv_storeName = (String) other.get("adv_storeName");
		String adv_memberName = (String) other.get("adv_memberName");
		String start_time = (String) other.get("start_time");
		String end_time = (String) other.get("end_time");
		String parentStore = (String) other.get("parentStore");
		String storeStatus = (String) other.get("storeStatus");
		String storeCountry = (String) other.get("storeCountry");
		
		//店铺状态
	
		if (!StringUtil.isEmpty(storeStatus)&&!storeStatus.equals("-2")) {
			sql.append("select s.* from es_store s LEFT JOIN es_member ON es_member.member_id = s.member_id  where disabled="+storeStatus );
		}else{
			if(disabled.equals(-2)){
					sql.append("select s.* from es_store s LEFT JOIN es_member ON es_member.member_id = s.member_id where 1=1" );
			}
			else{
				sql.append("select s.* from es_store s  LEFT JOIN es_member ON es_member.member_id = s.member_id  where  disabled="+disabled);
			}
		}

		if(StringUtil.isEmpty(adv_storeName)&&StringUtil.isEmpty(adv_memberName) ){
			if(!StringUtil.isEmpty(store_name)){
				sql.append( "  and (s.store_name like '%" + store_name + "%' or s.member_name like '%" + store_name + "%')");
			}
		}else if (!StringUtil.isEmpty(adv_storeName)) {
			sql.append(" AND s.store_name like '%" + adv_storeName + "%' ");
		}else if (!StringUtil.isEmpty(adv_memberName)) {
			sql.append(" AND s.member_name like '%" + adv_memberName + "%' " );
		}
		
		
		if(!StringUtil.isEmpty(start_time)) {
			long startTime = DateUtil.getDateline(start_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			sql.append(" AND s.create_time > " + startTime );
		}
		
		if(!StringUtil.isEmpty(end_time)) {
			long endTime = DateUtil.getDateline(end_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			sql.append(" AND s.create_time < " + endTime );
		}
		
		if(!StringUtil.isEmpty(parentStore)) {
			if (!parentStore.equals("3")) {
				sql.append(" AND s.parent_store = " + parentStore );
			}
		}
		
	
		if(!StringUtil.isEmpty(storeCountry)) {
			if (!storeCountry.equals("1")) {
				sql.append(" AND s.store_country = '" + storeCountry + "' ");
			}
		}
		if(!StringUtil.isEmpty(searchType)&&!searchType.equals("default")){
			sql.append(" order by "+searchType+" desc");
		}else{
			sql.append(" order by store_id"+" desc");
		}
		
		//System.out.println(sql.toString());
		return this.baseDaoSupport.queryForList(sql.toString(),Store.class);
	}
	@Override
	public List<storeAndMemberDTO> store_lists(Map other, Integer disabled) {
		// TODO Auto-generated method stub
				StringBuffer sql=new StringBuffer("");
				disabled=disabled==null?1:disabled;
				String store_name=other.get("name")==null?"":other.get("name").toString();
				String searchType=other.get("searchType")==null?"":other.get("searchType").toString();
				//Integer creditAccountStatus=(Integer) other.get("credit_account_status")==null?-2:(Integer) other.get("credit_account_status");
				
				//高级搜索字段
				String adv_storeName = (String) other.get("adv_storeName");
				String adv_memberName = (String) other.get("adv_memberName");
				String start_time = (String) other.get("start_time");
				String end_time = (String) other.get("end_time");
				String parentStore = (String) other.get("parentStore");
				String storeStatus = (String) other.get("storeStatus");
				String storeCountry = (String) other.get("storeCountry");
				
				//店铺状态
			
				if (!StringUtil.isEmpty(storeStatus)&&!storeStatus.equals("-2")) {
					sql.append("select m.member_id , m.uname,m.regtime,m.name as realname, s.* from es_store s LEFT JOIN es_member m ON m.member_id = s.member_id  where disabled="+storeStatus );
				}else{
					if(disabled.equals(-2)){
							sql.append("select m.member_id , m.uname,m.regtime,m.name as realname, s.* from es_store s LEFT JOIN es_member m ON m.member_id = s.member_id where 1=1" );
					}
					else{
						sql.append("select m.member_id , m.uname,m.regtime,m.name as realname,  s.* from es_store s  LEFT JOIN es_member m ON m.member_id = s.member_id  where  disabled="+disabled);
					}
				}

				if(StringUtil.isEmpty(adv_storeName)&&StringUtil.isEmpty(adv_memberName) ){
					if(!StringUtil.isEmpty(store_name)){
						sql.append( "  and (s.store_name like '%" + store_name + "%' or s.member_name like '%" + store_name + "%')");
					}
				}else if (!StringUtil.isEmpty(adv_storeName)) {
					sql.append(" AND s.store_name like '%" + adv_storeName + "%' ");
				}else if (!StringUtil.isEmpty(adv_memberName)) {
					sql.append(" AND s.member_name like '%" + adv_memberName + "%' " );
				}
				
				
				if(!StringUtil.isEmpty(start_time)) {
					long startTime = DateUtil.getDateline(start_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
					sql.append(" AND s.create_time > " + startTime );
				}
				
				if(!StringUtil.isEmpty(end_time)) {
					long endTime = DateUtil.getDateline(end_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
					sql.append(" AND s.create_time < " + endTime );
				}
				
				if(!StringUtil.isEmpty(parentStore)) {
					if (!parentStore.equals("3")) {
						sql.append(" AND s.parent_store = " + parentStore );
					}
				}
				
			
				if(!StringUtil.isEmpty(storeCountry)) {
					if (!storeCountry.equals("1")) {
						sql.append(" AND s.store_country = '" + storeCountry + "' ");
					}
				}
				if(!StringUtil.isEmpty(searchType)&&!searchType.equals("default")){
					sql.append(" order by "+searchType+" desc");
				}else{
					sql.append(" order by store_id"+" desc");
				}
				
				//System.out.println(sql.toString());
				return this.baseDaoSupport.queryForList(sql.toString(),storeAndMemberDTO.class);
	}

	/**
	 * 店铺销售量
	 */
	public Integer getSales(Integer store_id){
		String sql = "SELECT COUNT(1) AS sales FROM es_order o WHERE create_time >= UNIX_TIMESTAMP(date_sub(now(), INTERVAL 30 DAY)) AND o.store_id='"+store_id+"'";
		Integer sales = this.baseDaoSupport.queryForInt(sql);
		return sales;
	}

	/**
	 * 店铺评级
	 * @param store_id
	 * @return
	 */
	public String getStore_rate(Integer store_id){
		String sql = "SELECT ROUND((ifnull(AVG(m.store_desccredit),0) + ifnull(AVG(m.store_servicecredit),0) + ifnull(AVG(m.store_deliverycredit),0)) / 3, 2) AS store_rate FROM es_member_comment m WHERE m.store_id = '"+store_id+"'";
		String store_rate = this.baseDaoSupport.queryForString(sql);
		store_rate = (String) (store_rate == null ? "0.00" : store_rate);
		return store_rate;
	}
}

	

