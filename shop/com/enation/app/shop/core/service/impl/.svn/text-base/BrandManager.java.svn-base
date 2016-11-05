package com.enation.app.shop.core.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.Attration;
import com.enation.app.shop.core.model.AttrationPage;
import com.enation.app.shop.core.model.Brand;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.GoodsType;
import com.enation.app.shop.core.model.mapper.BrandMapper;
import com.enation.app.shop.core.service.IBrandManager;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.database.Page;
import com.enation.framework.database.StringMapper;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;
/**
 * 
 * @author LiFenLong 2014-4-1;4.0版本改造  clean delete checkUsed revert 方法参数String修改为Integer[]
 *
 */
public class BrandManager extends BaseSupport implements IBrandManager {
 
	private IGoodsCatManager goodsCatManager;
	
	/**
	 * 分页读取品牌
	 * 
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 */
	
	public Page list(String order, int page, int pageSize) {
		order = order == null ? " brand_id desc" : order;
		String sql = "select * from brand where disabled=0";
		sql += " order by  " + order;
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}


	/**
	 * 搜索所有的商品类型名字和id
	 * @return
	 */
	public List<Map> queryAllTypeNameAndId(){
//		String sql = "SELECT es_goods_type.type_id,es_goods_type.name FROM es_goods_type";
//		return this.daoSupport.queryForList(sql);
		String sql = "SELECT type_id,name FROM goods_type";
		return this.baseDaoSupport.queryForList(sql);
	}
	
	/**
	 * 品牌搜索
	 * 
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	
	public Page search(int page, int pageSize,String brandname,Integer type_id) {
		Page webpage = null;
		String sql = "SELECT b.* FROM " + this.getTableName("brand") + " b left  join " + this.getTableName("type_brand") + " tb on b.brand_id=tb.brand_id left  join " + this.getTableName("goods_type") + " gt on tb.type_id = gt.type_id where b.name  like '%"+brandname+"%' ";
		if(type_id!=-100){
			sql+= "  and gt.type_id = ? ";
			webpage = this.daoSupport.queryForPage(sql, page, pageSize,type_id);
		}else{
			webpage = this.daoSupport.queryForPage(sql, page, pageSize);
		}
		////System.out.println(sql);
		return webpage;
	}
	
	/**
	 * 分页读取回收站列表
	 * 
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 */
	
	public Page listTrash(String order, int page, int pageSize) {
		order = order == null ? " brand_id desc" : order;
		String sql = "select * from brand where disabled=1";
		sql += " order by  " + order;
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}

	/**
	 * 将回收站中的品牌还原
	 * 
	 * @param bid
	 */
	
	public void revert(Integer[] bid) {
		if (bid == null || bid.equals(""))
			return;
		String id_str = StringUtil.arrayToString(bid, ",");
		String sql = "update brand set disabled=0  where brand_id in (" + id_str
				+ ")";
		this.baseDaoSupport.execute(sql);
	}

	public boolean checkUsed(Integer[] ids){
		if (ids == null || ids.equals("")) return false;
		String id_str = StringUtil.arrayToString(ids, ",");
		
		String sql  ="select count(0) from goods where brand_id in (" + id_str + ")";;
		int count  = this.baseDaoSupport.queryForInt(sql);
		if(count>0)
			return true;
		else
			return false;
	}
	
	
	/**
	 * 将品牌放入回收站
	 * 
	 * @param bid
	 */
	
	public void delete(Integer[] bid) {

		
		
		
		if (bid == null || bid.equals(""))
			return;
		String id_str = StringUtil.arrayToString(bid, ",");
		String sql = "update brand set disabled=1  where brand_id in (" + id_str+ ")";
		this.baseDaoSupport.execute(sql);
	}

	/**
	 * 品牌删除,真正的删除。
	 * 
	 * @param bid
	 * @param files
	 */
	
	public void clean(Integer[] bid) {
		if (bid == null || bid.equals(""))
			return;
		// 删除附件
		for (int i = 0; i < bid.length; i++) {
			int brand_id = bid[i];
			Brand brand = this.get(brand_id);
			if (brand != null) {
				String f = brand.getLogo();
				if (f != null && !f.trim().equals("")) {
					File file = new File(StringUtil.getRootPath() + "/" + f);
					file.delete();
				}
			}
		}

		String sql = "delete  from  brand   where brand_id in (" + bid + ")";
		this.baseDaoSupport.execute(sql);
	}

	private String getThumbpath(String file) {
		String fStr = "";
		if (!file.trim().equals("")) {
			String[] arr = file.split("/");
			fStr = "/" + arr[0] + "/" + arr[1] + "/thumb/" + arr[2];
		}
		return fStr;
	}

	/**
	 * 读取所有品牌
	 * 
	 * @return
	 */
	
	public List<Brand> list() {
		String sql = "select * from brand where disabled=0";
		List list = this.baseDaoSupport.queryForList(sql,new BrandMapper());
		return list;
	}

	
	public List<Brand> listByTypeId(Integer typeid){
		String sql ="select b.* from "+this.getTableName("type_brand")+" tb inner join "+this.getTableName("brand")+" b  on    b.brand_id = tb.brand_id where tb.type_id=? and b.disabled=0";
		List list = this.daoSupport.queryForList(sql,new BrandMapper(),typeid);
		return list;
	}
	
	

	/**
	 * 读取品牌详细
	 * 会将本地文件存储的图片地址前缀替换为静态资源服务器地址。
	 * @param brand_id
	 * @return
	 */
	
	public Brand get(Integer brand_id) {
		String sql = "select * from brand where brand_id=?";
		Brand brand = (Brand) this.baseDaoSupport.queryForObject(sql, Brand.class,
				brand_id);// .queryForMap(sql, brand_id);
 
		String logo = brand.getLogo();
		if(logo!=null){
			logo  =UploadUtil.replacePath(logo);
		}
		brand.setLogo(logo);
		return brand;
	}

	/**
	 * 分页读取某个品牌下的商品
	 * 
	 * @param brand_id
	 * @return
	 */
	
	public Page getGoods(Integer brand_id, int pageNo, int pageSize) {
		String sql = "select * from goods where brand_id=? and disabled=0";
		Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize,
				brand_id);
		return page;

	}

	
	public void add(Brand brand) {		
		if(brand.getFile()!=null && brand.getFileFileName()!=null){
			brand.setLogo( UploadUtil.upload(brand.getFile(), brand.getFileFileName(), "brand") );
		}
		this.baseDaoSupport.insert("brand", brand);

	}
	
 
	
	private void deleteOldLogo(String logo){
		if(!logo.equals("http://static.enationsfot.com")){
			logo  =UploadUtil.replacePath(logo);
			FileUtil.delete(logo);
		}
		
	}  
	
	
	public void update(Brand brand) {
		
		if(brand.getLogo()!=null && "".equals(brand.getLogo())){
			this.deleteOldLogo(brand.getLogo());
		} 
		if(brand.getFile()!=null && brand.getFileFileName()!=null){
			brand.setLogo( UploadUtil.upload(brand.getFile(), brand.getFileFileName(), "brand") );
		}
		this.baseDaoSupport.update("brand", brand, "brand_id="
				+ brand.getBrand_id());
	}

	
	public List<Brand> listByCatId(Integer catid) {
		String sql ="select b.* from "+this.getTableName("brand")+" b ,"+this.getTableName("type_brand")+" tb,"+this.getTableName("goods_cat")+" c where tb.brand_id=b.brand_id and c.type_id=tb.type_id and c.cat_id=?";
		return this.daoSupport.queryForList(sql, Brand.class, catid);
	}
	
	public List groupByCat(){
		//取得商品分类的第一级列表
		List<Map> listCat = this.baseDaoSupport.queryForList("select * from goods_cat where parent_id = 0 order by cat_order");
		for(Map map:listCat){
			List list = this.baseDaoSupport.queryForList("select type_id from goods_cat where cat_path like '" + map.get("cat_path").toString() + "%'", new StringMapper());
			String types = StringUtil.listToString(list, ",");
			List listid = this.baseDaoSupport.queryForList("select brand_id from type_brand where type_id in (" + types + ")", new StringMapper());
			String ids = StringUtil.listToString(listid, ",");
			List<Brand> listBrand = this.baseDaoSupport.queryForList("select * from brand where brand_id in (" + ids + ")", Brand.class);
			map.put("listBrand", listBrand);
		}
		return listCat;
	}

	public boolean checkname(String name,Integer brandid) {
		if(name!=null)name=name.trim();
		String sql ="select count(0) from brand where name=? and brand_id!=?";
		if(brandid==null) brandid=0;
		
		int count =this.baseDaoSupport.queryForInt(sql, name,brandid);
		if(count>0)
			return true;
		else
			return false;
	}

	@Override
	public List<Brand> list(int count) {
		String sql = "select * from brand where disabled=0";
		Page page = this.baseDaoSupport.queryForPage(sql, 1, count, new BrandMapper());
		List list = (List<Brand>)page.getResult();
		return list;
	}


	@Override
	public Page searchBrand(Map brandMap, int page, int pageSize) {
		String keyword = (String) brandMap.get("keyword");
		
		String sql = "select * from brand where disabled=0";
		if(keyword!=null&&!StringUtil.isEmpty(keyword)){
			sql+=" and name like '%"+keyword+"%'";
		}
		sql+=" order by brand_id desc";
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}
	
	
	
	public IGoodsCatManager getGoodsCatManager() {
		return goodsCatManager;
	}

	public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
		this.goodsCatManager = goodsCatManager;
	}


	@Override
	public List listGoods(String tagid, String goodsnum) {
		int num = 10;
		if (!StringUtil.isEmpty(goodsnum)) {
			num = Integer.valueOf(goodsnum);
		}
		String sql="select * from es_brand_tag AS e INNER JOIN es_brand_tag_rel as l  on e.brand_id=l.brand_id WHERE e.brand_id="+Integer.valueOf(tagid);
		sql+=" and e.is_show=1 ORDER BY l.brand_index DESC";
		List list = this.daoSupport.queryForListPage(sql, 1, num);
		return list;
	}


	@Override
	public Brand getGoodBrand(Integer id) {
		String sql="select brand_id from es_goods where goods_id=?";
		Goods goods=(Goods) this.baseDaoSupport.queryForObject(sql, StoreGoods.class,id);
		String str="select * from es_brand where brand_id=?";
		Brand brand=(Brand) this.baseDaoSupport.queryForObject(str, Brand.class, goods.getBrand_id());
		return brand;
	}

	@Override
	public Page queryGoodsbyCatId(Integer pageNo, Integer pageSize,
			Integer catId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT g.brand_id, g.goods_id,g.name as 'goodsname',g.thumbnail,b.logo,b.name as 'brandname',b.attention from es_goods g,es_brand b, es_goods_cat c  where g.brand_id>0 and g.brand_id=b.brand_id ");
		sql.append(" and g.cat_id=c.cat_id and g.disabled=0 and g.market_enable=1 and g.audit_status=1 ");
		if(catId!=null){
			sql.append(" and c.cat_path like '%");
			sql.append(catId);
			sql.append("%'");
		}
		sql.append("group by g.brand_id ORDER BY g.create_time DESC  ");
		List list2 = this.baseDaoSupport.queryForList(sql.toString());
		List list1  = this.baseDaoSupport.queryForListPage(sql.toString(), pageNo, pageSize);
		Page webPage = new Page(0, list2.size(), pageSize, list1);
		return webPage;
		
	}


	@Override
	public void updateBrandCount(Brand brand) {
		String sql="update es_brand set attention=? where brand_id=?";
		this.baseDaoSupport.execute(sql, brand.getAttention(),brand.getBrand_id());
		
	}


	@Override
	public void addAttion(Attration attration) {
		this.baseDaoSupport.insert("es_attration", attration);
	}


	@Override
	public AttrationPage getPageView(Integer brand_id) {
		Member member = UserConext.getCurrentMember();
		Integer	member_id=member.getMember_id();
		List<Attration> list=this.baseDaoSupport.queryForList("select * from es_attration where memberid=?", Attration.class,member_id);
		Brand brand=this.get(brand_id);
		AttrationPage attrationPage=new AttrationPage();
		attrationPage.setCount(brand.getAttention());
		attrationPage.setViewcount(list.size());
		Attration attration=(Attration) this.baseDaoSupport.queryForObject("select * from es_attration where memberid=? and brandid=?", Attration.class, member_id,brand_id);
		attrationPage.setIs_true(attration.getIs_true());
		return attrationPage;
	}

	/**
	 * 判断关注表里是否存在此品牌的用户
	 */
	@Override
	public List<Attration> querForAtteation(Integer member_id, Integer brand_id) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from es_attration where memberid=? and brandid=? ");
		List<Attration> attlist= this.baseDaoSupport.queryForList(sql.toString(), Attration.class, member_id,brand_id);
		return attlist;
	}


	@Override
	public void updateatt(Attration atteation) {
		this.baseDaoSupport.update("es_attration", atteation, "id="
				+ atteation.getId());
		
	}


	@Override
	public Attration querymemberbrand(Integer brand_id) {
		Member member = UserConext.getCurrentMember();
		Integer	member_id=null;
		Attration att = null;
		if(member!=null){
			member_id=member.getMember_id();
			 StringBuffer sql=new StringBuffer("select * from es_attration a,es_brand b  where a.brandid=b.brand_id and b.disabled=0 and a.is_true=1 and a.brandid=? and a.memberid=?");
			att = (Attration) this.baseDaoSupport.queryForObject(sql.toString(), Attration.class, brand_id,member_id);
			return att;
		}else{
			return null;
		}
	}


	@Override
	public Page searchBrand(int pageNo, int pageSize) {
		int count = this.count();
		List list  = this.list(pageNo,pageSize);
		Page webPage = new Page(0, count, pageSize, list);
		return webPage;
	}
	public List list(int pageNo,int pageSize){
		Member member = UserConext.getCurrentMember();
		StringBuffer sql = new StringBuffer("select * from es_brand b ");
		sql.append(" ,es_attration a where b.disabled=0 and a.brandid=b.brand_id and a.is_true=1 and a.memberid=? ");
		List goodslist = this.daoSupport.queryForListPage(sql.toString(), pageNo, pageSize,member.getMember_id());
		return goodslist;
	}

	private int count() {
		Integer count=0;
		Member member = UserConext.getCurrentMember();
		StringBuffer sql = new StringBuffer("select count(*) from es_brand b ");
		sql.append(" ,es_attration a where b.disabled=0 and a.brandid=b.brand_id and a.is_true=1 and a.memberid=? ");
		count=this.baseDaoSupport.queryForInt(sql.toString(), member.getMember_id());
		return count;
	}


	@Override
	public List<Attration> getAtteationList() {
		Member member = UserConext.getCurrentMember();
        StringBuffer sql=new StringBuffer("select * from es_attration a,es_brand b  where b.brand_id=a.brandid and b.disabled=0 and is_true=1 and memberid=?");
        List<Attration> list=this.baseDaoSupport.queryForList(sql.toString(), Attration.class, member.getMember_id());
        return list;
	}
}
