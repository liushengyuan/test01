package com.enation.app.shop.core.service.impl;


import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.core.model.BrandTag;
import com.enation.app.shop.core.model.BrandTagRel;
import com.enation.app.shop.core.model.Team;
import com.enation.app.shop.core.model.TeamProduct;
import com.enation.app.shop.core.service.ITeamManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 商品推荐类管理
 * @author ljs
 * 2010-1-18上午10:57:35
 */
@SuppressWarnings("rawtypes")
public class TeamManager extends BaseSupport implements ITeamManager {

	@Override
	public boolean checkname(String name, Integer teamid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Team getById(Integer teamid) {
		// TODO Auto-generated method stub
		String sql  ="select * from team where team_id=?";
		Team team = (Team)this.baseDaoSupport.queryForObject(sql, Team.class, teamid);
		return team;
	}

	@Override
	public void add(Team team) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.insert("es_team", team);
		team.setTeam_id(this.daoSupport.getLastId("es_team"));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Team team) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.update("team", team, "team_id="+team.getTeam_id());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)  
	public void delete(Integer[] teamids) {
		// TODO Auto-generated method stub
		String ids =StringUtil.implode(",", teamids);
		if(ids==null || ids.equals("")){return ;}
		//删除推荐，同时删除推荐类的引用的商品
		this.baseDaoSupport.execute("delete from team where team_id in("+ids+")");
		this.daoSupport.execute("delete from "+this.getTableName("team_product")+" where team_id in("+ids+")");
		
	}

	@Override
	public Page list(int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		return this.baseDaoSupport.queryForPage("select * from team order by team_index", pageNo, pageSize);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Team> list(Integer market) {
		// TODO Auto-generated method stub
		String sql  ="select * from team where market="+market+" and is_show=1 order by team_index";
		return this.baseDaoSupport.queryForList(sql,Team.class);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveRels(Integer productid, Integer[] teamids) {
		// TODO Auto-generated method stub
		//清空原有引用
	}

	@Override
	public TeamProduct getByIdForeTeamProduct(Integer id) {
		// TODO Auto-generated method stub
		String sql  ="select * from team_product where id=?";
		TeamProduct product = (TeamProduct)this.baseDaoSupport.queryForObject(sql, TeamProduct.class, id);
		return product;
	}

	@Override
	public void update(TeamProduct product) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.update("team_product", product, "id="+product.getId());
	}

	@Override
	public void delete(Integer id, Integer goodsId) {
		// TODO Auto-generated method stub
		this.daoSupport.execute("delete from "+this.getTableName("team_product")+" where id="+id+" and product_id="+goodsId);
	}

	@Override
	public List<TeamProduct> productList() {
		// TODO Auto-generated method stub
		String sql  ="select * from team_product order by product_index";
		return this.baseDaoSupport.queryForList(sql,TeamProduct.class);
	}
	@Override
	public List productListForApp() {
		// TODO Auto-generated method stub
		String sql  ="select g.name,g.price,g.view_count,g.buy_count,t.id,t.team_id,t.product_id,t.product_image,t.product_index from es_team_product t LEFT JOIN es_goods  g ON t.product_id=g.goods_id";
		List list=this.baseDaoSupport.queryForList(sql);
		return list;
	}
	@Override
	public Page listBrand(int pageNo, int pageSize) {
		return this.baseDaoSupport.queryForPage("select * from es_brand_tag order by brand_id", pageNo, pageSize);
	}

	@Override
	public void addBrand(BrandTag teamTag) {
		this.baseDaoSupport.insert("es_brand_tag", teamTag);
	}

	@Override
	public void deletebrand(Integer[] brand_id) {
		// TODO Auto-generated method stub
				String ids =StringUtil.implode(",", brand_id);
				if(ids==null || ids.equals("")){return ;}
				//删除推荐，同时删除推荐类的引用的商品
				this.baseDaoSupport.execute("delete from es_brand_tag where brand_id in("+ids+")");
				this.daoSupport.execute("delete from "+this.getTableName("es_brand_tag_rel")+" where brand_id in("+ids+")");
	}

	@Override
	public BrandTag getByIdBrand(Integer teamid) {
		String sql  ="select * from es_brand_tag where brand_id=?";
		BrandTag team = (BrandTag)this.baseDaoSupport.queryForObject(sql, BrandTag.class, teamid);
		return team;
	}

	@Override
	public void updatebrand(BrandTag team) {
		this.baseDaoSupport.update("es_brand_tag", team, "brand_id="+team.getBrand_id());
	}

	@Override
	public BrandTagRel getByIdForeTeamBrand(Integer id) {
		String sql  ="select * from es_brand_tag_rel where id=?";
		BrandTagRel product = (BrandTagRel)this.baseDaoSupport.queryForObject(sql, BrandTagRel.class, id);
		return product;
	}

	@Override
	public void updateBrand(BrandTagRel product) {
		this.baseDaoSupport.update("es_brand_tag_rel", product, "id="+product.getId());
	}

	@Override
	public void deleteBrand(Integer id, Integer goodsId) {
		this.daoSupport.execute("delete from "+this.getTableName("es_brand_tag_rel")+" where id="+id+" and brand_id="+goodsId);
	}
	
/*	private IDaoSupport  daoSupport;
	private JdbcTemplate jdbcTemplate;
	
	public boolean checkname(String name,Integer tagid){
		if(name!=null)name=name.trim();
		if(tagid==null) tagid=0;
		String sql ="select count(0) from tags where tag_name=? and tag_id!=?";
		int count  = this.baseDaoSupport.queryForInt(sql, name,tagid);
		if(count>0)
			return true;
		else
			return false;
	}
	
	
	public void add(Tag tag) {
		tag.setRel_count(0);
		this.baseDaoSupport.insert("tags", tag);
		
	}

	public boolean checkJoinGoods(Integer[] tagids) {
		if(tagids==null ) return false;
		String ids =StringUtil.implode(",", tagids);
		String sql ="select count(0)  from tag_rel where tag_id in("+ids+")";	 
		int count  = this.baseDaoSupport.queryForInt(sql);
		if(count>0)
			return true;
		else
			return false;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)  
	public void delete(Integer[] tagids) {
		String ids =StringUtil.implode(",", tagids);
		if(ids==null || ids.equals("")){return ;}
		//删除标签，同时删除标签的引用对照表
		this.baseDaoSupport.execute("delete from tags where tag_id in("+ids+")");
		this.daoSupport.execute("delete from "+this.getTableName("tag_rel")+" where tag_id in("+ids+")");
	}

	
	public Page list(int pageNo, int pageSize) {
		return this.baseDaoSupport.queryForPage("select * from tags order by tag_id", pageNo, pageSize);
	}

	
	public void update(Tag tag) {
		this.baseDaoSupport.update("tags", tag, "tag_id="+tag.getTag_id());
		
	}

	public IDaoSupport<Tag> getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport<Tag> daoSupport) {
		this.daoSupport = daoSupport;
	}

	
	public Tag getById(Integer tagid) {
		String sql  ="select * from tags where tag_id=?";
		Tag tag = this.baseDaoSupport.queryForObject(sql, Tag.class, tagid);
		return tag;
	}

	
	public List<Tag> list() {
		String sql  ="select * from tags";
		return this.baseDaoSupport.queryForList(sql,Tag.class);
	}

	
	@Transactional(propagation = Propagation.REQUIRED)  
	public void saveRels(Integer relid, Integer[] tagids) {
		//清空原有引用
		String sql = "delete from "+ this.getTableName("tag_rel") +" where rel_id=?";
		this.daoSupport.execute(sql, relid);
		if(tagids!=null ){
			
			//重新对照新的引用
			sql ="insert into " + this.getTableName("tag_rel") +"(tag_id,rel_id)values(?,?)";
			for(Integer tagid:tagids){
				this.daoSupport.execute(sql, tagid,relid);
			}
		}
	}

	
	public List<Integer> list(Integer relid) {
		String sql= "select tag_id from " + this.getTableName("tag_rel") + " where rel_id="+relid;
		List<Integer> tagids = this.jdbcTemplate.queryForList(sql, Integer.class);
		return tagids;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}*/



	
	 
}
