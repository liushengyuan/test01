package com.enation.app.tradeease.core.service.cordova.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.core.model.MemberCollect;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.Favorite;
import com.enation.app.shop.core.model.Goods;

import com.enation.app.shop.core.service.IFavoriteManager;
import com.enation.app.tradeease.core.model.student.History;
import com.enation.app.tradeease.core.service.cordova.IFavoriteManagerApp;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

/**
 * 我的收藏
 * 
 * @author lzf<br/>
 *         2010-3-24 下午02:54:26<br/>
 *         version 1.0<br/>
 */
public class FavoriteManagerApp extends BaseSupport implements IFavoriteManagerApp {

	
	public Page list(int pageNo, int pageSize) {
		Member member = UserConext.getCurrentMember();
		String sql = "select g.*, f.favorite_id from " + this.getTableName("favorite")
				+ " f left join " + this.getTableName("goods")
				+ " g on g.goods_id = f.goods_id";
		sql += " where f.member_id = ?";
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize, member
				.getMember_id());
		return page;
	}

	
	@Override
	public Page list(int memberid, int pageNo, int pageSize) {
		String sql = "select g.*, f.favorite_id from " + this.getTableName("favorite")
				+ " f left join " + this.getTableName("goods")
				+ " g on g.goods_id = f.goods_id";
		sql += " where f.member_id = ? order by f.favorite_id";
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize,memberid);
		return page;
	}
	
	
	
	public void deleteByStoreId(int storeCollectId) {
		this.baseDaoSupport.execute("delete from es_member_collect where id=?", storeCollectId);
	}
	public void deleteByGoodsIdAndMember(int goods_id,int member_id){
		this.baseDaoSupport.execute("delete from favorite where goods_id=? and member_id=?", goods_id,member_id);
	}
	public void deleteFavoriteByStoreId(int store_id,int member_id){
		this.baseDaoSupport.execute("delete from es_member_collect where store_id=? and member_id=?", store_id,member_id);
	}

	
	public void add(Integer goodsid) {
		Member member = UserConext.getCurrentMember();
		Favorite favorite = new Favorite();
		favorite.setGoods_id(goodsid);
		favorite.setMember_id(member.getMember_id());
		this.baseDaoSupport.insert("favorite", favorite);
	}
	//添加店铺
	public void addstore(Integer store_id){
		Member member = UserConext.getCurrentMember();
		MemberCollect favorite = new MemberCollect();
		favorite.setStore_id(store_id);
		favorite.setMember_id(member.getMember_id());
		long time = System.currentTimeMillis()/1000;
		favorite.setCreate_time(time);
		this.baseDaoSupport.insert("es_member_collect", favorite);
	}
	/**
	 * 根据商品ID和用户ID获取一个收藏
	 */
	public int getCount(Integer goodsid, Integer memeberid){
		return this.baseDaoSupport.queryForInt("SELECT COUNT(0) FROM favorite WHERE goods_id=? AND member_id=?", goodsid,memeberid);
	}
	
	public int getCount(Integer memberId){
		return this.baseDaoSupport.queryForInt("SELECT COUNT(0) FROM favorite WHERE  member_id=?", memberId);
	}
	public int getStoreCount(Integer store_id){
		Member member = UserConext.getCurrentMember();
		return this.baseDaoSupport.queryForInt("select count(0) from es_member_collect where store_id=? and member_id=?", store_id,member.getMember_id());
	}
	
	public List list( ) {
		Member member = UserConext.getCurrentMember();
		
		return this.baseDaoSupport.queryForList("select * from favorite where member_id=?", Favorite.class, member.getMember_id());
	}


	public List list1() {
		Member member = UserConext.getCurrentMember();
//		String sql = "select g.*, f.favorite_id from " + this.getTableName("favorite")
//				+ " f left join " + this.getTableName("goods")
//				+ " g on g.goods_id = f.goods_id";
//		sql += " where f.member_id = 10640 order by f.favorite_id";
//		List list = this.daoSupport.queryForList(sql);
//		return list;
		List<Goods> goods = new ArrayList<Goods>();
		String sql = "select goods_id from es_favorite where member_id = ?";
		List<Favorite> favorites = this.baseDaoSupport.queryForList(sql, Favorite.class, member.getMember_id());
		for(Favorite f : favorites){
			String sqls = "select * from es_goods where goods_id=?";
			Goods good = (Goods)this.baseDaoSupport.queryForObject(sqls, Goods.class, f.getGoods_id());
			goods.add(good);
		}
		return goods;
	}
	
	public List<Map> liststore(){
		Member member = UserConext.getCurrentMember();
		List<Map> stores = new ArrayList<Map>();
		
		String sql = "select store_id from es_member_collect where member_id = ?";
		List<MemberCollect>  membercollectr=this.baseDaoSupport.queryForList(sql, MemberCollect.class, member.getMember_id());
		for(MemberCollect f : membercollectr){
			String sqls = " select s.*,c.id as 'collectId' from es_store s, es_member_collect c where s.store_id=c.store_id  and s.store_id = ? and c.member_id= ? ";
			Map map = new HashMap();
			if(f.getStore_id()!=null && member.getMember_id()!=null){
				map = this.baseDaoSupport.queryForMap(sqls,f.getStore_id(),member.getMember_id());
				stores.add(map);
			}
			
		}
		return stores;
	}

	public List listStore2(){
		Member member = UserConext.getCurrentMember();
		List<Store> stores = new ArrayList<Store>();
		String sql = "select * from es_member_collect where member_id = ?";
		List<MemberCollect>  membercollectr=this.baseDaoSupport.queryForList(sql, MemberCollect.class, member.getMember_id());
		return membercollectr;
		
	}

	//删除历史纪录
	@Override
	public void deleteByHistory(int goods_id, int member_id) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.execute("delete from history where goods_id=? and member_id=?", goods_id,member_id);
	}

	//添加历史纪录
	@Override
	public void addHistory(Integer goodsid) {
				Member member = UserConext.getCurrentMember();
				History history = new History();
				history.setGoods_id(goodsid);
				history.setMember_id(member.getMember_id());
				Date currenttime=new Date();
				history.setCreatetime(currenttime.getTime());
				this.baseDaoSupport.insert("history", history);
	}

	//查看商品历史纪录
	@Override
	public List list7() {
		Member member = UserConext.getCurrentMember();
//		String sql = "select g.*, f.favorite_id from " + this.getTableName("favorite")
//				+ " f left join " + this.getTableName("goods")
//				+ " g on g.goods_id = f.goods_id";
//		sql += " where f.member_id = 10640 order by f.favorite_id";
//		List list = this.daoSupport.queryForList(sql);
//		return list;
		//System.out.println(member.getMember_id());
		List<Goods> goods = new ArrayList<Goods>();
		String sql = "select goods_id from es_history where member_id = ? order by id desc";
		
		List<History> history = this.baseDaoSupport.queryForList(sql, History.class, member.getMember_id());
		for(History f : history){
			String sqls = "select * from es_goods where goods_id=?";
			Goods good = (Goods)this.baseDaoSupport.queryForObject(sqls, Goods.class, f.getGoods_id());
			goods.add(good);
		}
		return goods;
	}

	//判断商品历史纪录是否存在
	@Override
	public int getNum(Integer goodsid, Integer memeberid) {
		// TODO Auto-generated method stub
	   return this.baseDaoSupport.queryForInt("SELECT COUNT(0) FROM history WHERE goods_id=? AND member_id=?", goodsid,memeberid);
	}
	
	
}
