package com.enation.app.shop.core.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.enation.app.shop.core.model.Brand;
import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.app.shop.core.service.IGoodsTagManager;
import com.enation.eop.SystemSetting;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;

public class GoodsTagManager extends BaseSupport implements
		IGoodsTagManager {

	private IGoodsCatManager goodsCatManager;

	@Override
	public Page getGoodsList(int tagid, int page, int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("select g.goods_id,g.name,r.tag_id,r.ordernum from " + this.getTableName("tag_rel") + " r LEFT JOIN " + this.getTableName("goods") + " g ON g.goods_id=r.rel_id where g.disabled=0 and g.market_enable=1");
		sql.append(" and r.tag_id = ?");
		sql.append(" order by r.ordernum desc");
		Page webpage = this.daoSupport.queryForPage(sql.toString(), page,
				pageSize, tagid);
		return webpage;
	}

	@Override
	public Page getGoodsList(int tagid, int catid, int page, int pageSize) {
		Cat cat = this.goodsCatManager.getById(Integer.valueOf(catid));
		if (cat == null) {
			return null;
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select g.goods_id,g.name,r.tag_id,r.ordernum from " + this.getTableName("tag_rel") + " r LEFT JOIN " + this.getTableName("goods") + " g ON g.goods_id=r.rel_id where g.disabled=0 and g.market_enable=1");
		sql.append(" and r.tag_id = ?");

		sql.append(" and  g.cat_id in(");
		sql.append("select c.cat_id from " + this.getTableName("goods_cat") + " ");
		sql.append(" c where c.cat_path like '" + cat.getCat_path() + "%')");

		sql.append(" order by r.ordernum desc");
		
		Page webpage = this.daoSupport.queryForPage(sql.toString(), page,
				pageSize, tagid);
		return webpage;
	}

	@Override
	public void addTag(int tagId, int goodsId) {
		if (this.baseDaoSupport.queryForInt(
				"SELECT COUNT(0) FROM tag_rel WHERE tag_id=? AND rel_id=?",
				tagId, goodsId) <= 0) {
			this.baseDaoSupport
					.execute(
							"INSERT INTO tag_rel(tag_id,rel_id,ordernum) VALUES(?,?,0)",
							tagId, goodsId);

		}
	}
	@Override
	public void addTeam(int teamId, int goodsId) {
		if (this.baseDaoSupport.queryForInt(
				"SELECT COUNT(0) FROM team_product WHERE team_id=? AND product_id=?",
				teamId, goodsId) <= 0) {
			String thumbNail = thumbNail(goodsId);
			this.baseDaoSupport
					.execute(
							"INSERT INTO team_product(team_id,product_id,product_image,product_index) VALUES(?,?,?,0)",
							teamId, goodsId,thumbNail);


		}
	}

	/**
	 * 关联首饰，童装，美发时查询出对应的图片
	 * @param goodsId
	 * @return
	 * @throws UnknownHostException 
	 */
	public String thumbNail(int goodsId) {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String static_server_domain = SystemSetting.getStatic_server_domain();
//		InetAddress address1;
//		String ipstr = null;
//		try {
//			address1 = InetAddress.getLocalHost();
//			ipstr = address1.getHostAddress();
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//		int port=request.getLocalPort(); //端口号
		String sql = "select thumbnail from es_goods where goods_id= "+goodsId;
		String thumbNail = this.daoSupport.queryForString(sql);
		if(thumbNail!=null){
			if(thumbNail.startsWith("fs:")){
				thumbNail = thumbNail.replaceAll(EopSetting.FILE_STORE_PREFIX, static_server_domain );
			}
		}
		
		return thumbNail;
	}
	@Override
	public void removeTag(int tagId, int goodsId) {
		this.baseDaoSupport.execute(
				"DELETE FROM tag_rel WHERE tag_id=? AND rel_id=?", tagId,
				goodsId);

	}

	@Override
	public void updateOrderNum(Integer[] goodsIds, Integer[] tagids,
			Integer[] ordernum) {
		if (goodsIds != null && goodsIds.length > 0) {
			for (int i = 0; i < goodsIds.length; i++) {
				if (goodsIds[i] != null && tagids[i] != null
						&& ordernum[i] != null) {
					try {
						this.baseDaoSupport.execute(
										"UPDATE tag_rel set ordernum=? WHERE tag_id=? AND rel_id=?",
										ordernum[i], tagids[i], goodsIds[i]);
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
		this.goodsCatManager = goodsCatManager;
	}

	@Override
	public List getGoodsList(int tagid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select g.goods_id,g.name,r.tag_id,r.ordernum,g.thumbnail,g.price from " + this.getTableName("tag_rel") + " r LEFT JOIN " + this.getTableName("goods") + " g ON g.goods_id=r.rel_id where g.disabled=0 and g.market_enable=1");
		sql.append(" and r.tag_id = ?");
		sql.append(" order by r.ordernum desc");
		//System.out.println(sql.toString());
		List list = this.baseDaoSupport.queryForList(sql.toString(), tagid);
		return list;
	}

	@Override
	public Page getGoodsListForTeam(int teamid, int page, int pageSize) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("select g.goods_id,g.name,r.id,r.team_id,r.product_image,r.product_index from " + this.getTableName("team_product") + " r LEFT JOIN " + this.getTableName("goods") + " g ON g.goods_id=r.product_id where g.disabled=0 ");
		sql.append(" and r.team_id = ?");
		sql.append(" order by r.product_index desc");
		Page webpage = this.daoSupport.queryForPage(sql.toString(), page,
				pageSize, teamid);
		return webpage;
	}

	/**
	 * 加入排序号可删除
	 */
	@Override
	public void deleteOrdernum(Integer[] goodsIds, Integer[] tagids,
			Integer[] ordernum) {
		if (goodsIds != null && goodsIds.length > 0) {
			for (int i = 0; i < goodsIds.length; i++) {
				if (goodsIds[i] != null && tagids[i] != null
						&& ordernum[i] != null) {
					try {
						this.baseDaoSupport.execute(
										" Delete from tag_rel  WHERE tag_id=? AND rel_id=?",
										tagids[i], goodsIds[i]);
					} catch (Exception ex) {
					}
				}
			}
		}
	}

	@Override
	public Page getGoodsListForBrand(int teamid, int page, int pageSize) {
		// TODO Auto-generated method stub
				StringBuffer sql = new StringBuffer();
				sql.append("select e.brand_id,l.id,l.rel_id,l.rel_logo,l.rel_name,l.brand_index from es_brand_tag AS e inner JOIN es_brand_tag_rel as l ON e.brand_id=l.brand_id where e.brand_id=? ORDER BY e.brand_id DESC");
				Page webpage = this.daoSupport.queryForPage(sql.toString(), page,
						pageSize, teamid);
				return webpage;
	}

	@Override
	public Page getGoodsListBrand( int page, int pageSize) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from es_brand where disabled=0");
		Page webpage = this.daoSupport.queryForPage(sql.toString(), page,
				pageSize);
		return webpage;
	}

	@Override
	public void addTeamBrand(int teamId, int googsId) {
		if (this.baseDaoSupport.queryForInt(
				"SELECT COUNT(0) FROM es_brand_tag_rel WHERE brand_id=? AND rel_id=?",
				teamId, googsId) <= 0) {
			String sql="select * from es_brand where brand_id=?";
			Brand brand=(Brand) this.baseDaoSupport.queryForObject(sql, Brand.class, googsId);
			this.baseDaoSupport
					.execute(
							"INSERT INTO es_brand_tag_rel(brand_id,rel_id,rel_name,rel_logo,brand_index) VALUES(?,?,?,?,0)",
							teamId, googsId,brand.getName(),brand.getLogo());
		}
	}

}
