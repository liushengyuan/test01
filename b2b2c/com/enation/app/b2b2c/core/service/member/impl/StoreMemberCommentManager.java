package com.enation.app.b2b2c.core.service.member.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.member.StoreMemberComment;
import com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
@Component
public class StoreMemberCommentManager extends BaseSupport implements IStoreMemberCommentManager {
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#getAllComments(int, int, java.util.Map, java.lang.Integer)
	 */
	@Override
	public Page getAllComments(int page, int pageSize, Map map, Integer store_id) {
		String sql=this.createTemlSql(map, store_id);
		return this.daoSupport.queryForPage(sql, page, pageSize,store_id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#get(java.lang.Integer)
	 */
	@Override
	public Map get(Integer comment_id) {
		return this.daoSupport.queryForMap("SELECT * FROM es_member_comment WHERE comment_id=?", comment_id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#edit(java.util.Map, java.lang.Integer)
	 */
	@Override
	public void edit(Map map,Integer comment_id) {
		this.daoSupport.update("es_member_comment", map, "comment_id="+comment_id);
	}
	
	private String createTemlSql(Map map,Integer store_id){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT m.uname as uname,g.name as goods_name,c.* FROM es_member_comment c LEFT JOIN es_goods g ON c.goods_id=g.goods_id" +
				" LEFT JOIN es_member m ON c.member_id=m.member_id WHERE g.store_id=? and c.type="+map.get("type"));
		if(map.get("stype")!=null){
			if(map.get("stype").equals("0")){
				if(map.get("keyword")!=null){
					sql.append(" and (m.uname like '%"+map.get("keyword")+"%'");
					sql.append(" or c.content like '%"+map.get("keyword")+"%'");
					sql.append(" or g.name like '%"+map.get("keyword")+"%')");
				}
			}else{
				if(map.get("mname")!=null){
					sql.append(" and m.uname like '%"+map.get("mname")+"%'");
				}
				if(map.get("gname")!=null){
					sql.append(" and g.name like '%"+map.get("gname")+"%'");
				}
				if(map.get("content")!=null){
					sql.append(" and c.content like '%"+map.get("content")+"%'");
				}
				
				if(map.get("replyStatus")!=null&&!map.get("replyStatus").equals("0")){
					if(map.get("replyStatus").equals("1")){
						sql.append(" and c.replystatus ="+map.get("replyStatus"));
					}else{
						sql.append(" and c.replystatus = 0");
					}
					
				}
			}
			if( map.get("status")!=null){
				sql.append(" and c.status="+ map.get("status"));
			}
			
		}
		if(map.get("grade")!=null)
			if(Integer.parseInt(map.get("grade").toString())!=-1){
				sql.append(" and c.grade="+map.get("grade"));
			}
		sql.append(" ORDER BY c.comment_id DESC");
		////System.out.println(sql);
		return sql.toString();
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#add(com.enation.app.b2b2c.core.model.member.StoreMemberComment)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(StoreMemberComment memberComment) {
		//添加评论、咨询
		this.daoSupport.insert("es_member_comment", memberComment);
		//修改店铺评价信息
		this.updateStoreComment(memberComment);
	}
	/**
	 * 修改店铺评价
	 * @param memberComment
	 */
	private void updateStoreComment(StoreMemberComment memberComment){
		
		String sql="select (SELECT COUNT(comment_id)from es_member_comment WHERE grade=3)as grade,sum(store_desccredit) as store_desccredit ,SUM(store_servicecredit)as store_servicecredit,SUM(store_deliverycredit) as store_deliverycredit ,COUNT(comment_id) as comment_num from es_member_comment WHERE store_id=?";
		Map map= this.daoSupport.queryForMap(sql, memberComment.getStore_id());
		Integer grade=Integer.parseInt(map.get("grade").toString());
		Double store_desccredit=Double.parseDouble(map.get("store_desccredit").toString());
		Double store_servicecredit=Double.parseDouble(map.get("store_servicecredit").toString());
		Double store_deliverycredit=Double.parseDouble(map.get("store_deliverycredit").toString());
		int comment_num=Integer.parseInt(map.get("comment_num").toString());
		
		Map storeInfo=new HashMap();
		
		storeInfo.put("praise_rate",grade/comment_num);
		storeInfo.put("store_desccredit", store_desccredit/comment_num);
		storeInfo.put("store_servicecredit", store_servicecredit/comment_num);
		storeInfo.put("store_deliverycredit", store_deliverycredit/comment_num);
		this.daoSupport.update("es_store", storeInfo, "store_id="+memberComment.getStore_id());
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#getCommentCount(java.lang.Integer)
	 */
	@Override
	public Integer getCommentCount(Integer type,Integer store_id) {
		String sql="SELECT count(0) from es_member_comment c WHERE c.type=? AND c.replystatus=0 and store_id=?";
		return this.daoSupport.queryForInt(sql, type,store_id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager#getGoodsStore_desccredit(java.lang.Integer)
	 */
	@Override
	public Double getGoodsStore_desccredit(Integer goods_id) {
		String sql="select AVG(store_deliverycredit) as store_deliverycredit  from es_member_comment WHERE type=1 AND goods_id=?";
		Map map=this.daoSupport.queryForMap(sql, goods_id);
		return Double.parseDouble(map.get("store_deliverycredit").toString());
	}
	@Override
	public Integer getCommentCount(Integer goods_id) {
		String sql = "select count(*) from es_member_comment WHERE goods_id = ? and grade !=0 ";
		return this.daoSupport.queryForInt(sql, goods_id);
	}
	@Override
	public Integer getCommentCountPraise(Integer goods_id) {
		String sql = "select count(*) from es_member_comment WHERE goods_id = ? and grade =3 or goods_id = ? and grade =2";
		return this.daoSupport.queryForInt(sql, goods_id,goods_id);
	}
	
}
