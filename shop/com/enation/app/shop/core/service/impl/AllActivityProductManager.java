package com.enation.app.shop.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.model.AllActivity;
import com.enation.app.shop.core.model.AllActivityProduct;
import com.enation.app.shop.core.service.IAllActivityProductManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;

public class AllActivityProductManager extends BaseSupport<AllActivityProduct> implements IAllActivityProductManager {

    @Override
    public Page get(Integer activity_id, Integer Page, Integer PageSize) {
        String sql="SELECT p.id,g.goods_id,g.name,p.index,g.thumbnail,p.original_price,p.activity_price,p.count FROM es_goods AS g LEFT JOIN es_allactivity_product AS p ON g.goods_id=p.goods_id WHERE p.allactivity_id=? ORDER BY p.`index` DESC";
        return this.baseDaoSupport.queryForPage(sql, Page, PageSize, activity_id);
    }

    @Override
    public Page getGoodsList(Map map, Integer page, Integer pageSize) {
        String sql = this.createSql(map);
        return this.baseDaoSupport.queryForPage(sql, page, pageSize);
    }
    private String createSql(Map map){
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT g.goods_id,g.sn,g.name,c.name as cat_name,p.enable_store,g.price,g.is_discount,g.cost_price FROM es_goods AS g LEFT JOIN es_goods_cat AS c ON g.cat_id=c.cat_id LEFT JOIN es_product AS p ON g.goods_id=p.goods_id WHERE market_enable=1 ");
        String name = (String)map.get("name");
        String sn = (String)map.get("sn");
        String cat = (String) map.get("cat_id");
        Integer type=Integer.parseInt( map.get("type").toString());
        Integer allactivity_id = (Integer)map.get("allactivity_id");
        sql.append(" AND g.goods_id NOT IN (SELECT goods_id FROM es_allactivity_product where allactivity_id='"+allactivity_id+"')");
        if(cat!=null&&cat.length()!=0){
            Integer cat_id = Integer.parseInt(cat);
            sql.append(" AND g.cat_id="+cat_id);
        }
        if(name!=null){
            sql.append(" AND g.name LIKE '%"+name+"%' ");
        }
        if(sn!=null){
            sql.append(" AND g.sn LIKE '%"+sn+"%'");
        }
        if(type==0){
        	sql.append(" AND g.is_discount="+1 );
        }else{
        	sql.append(" AND g.is_discount!="+1 );
        }
        return sql.toString();
    }

    @Override
    public void add(AllActivityProduct aap) {
        String sql = "INSERT INTO es_allactivity_product VALUES(?,?,?,?,?,?,?)";
        this.baseDaoSupport.execute(sql,null,aap.getGoods_id(),aap.getOriginal_price(),aap.getActivity_price(),aap.getCount(),aap.getAllactivity_id(),aap.getIndex());
    }

    @Override
    public List getAll(Integer id) {
        String sql = "SELECT p.id,p.goods_id,p.original_price,p.activity_price,p.count,p.allactivity_id,g.name,g.thumbnail FROM es_allactivity_product AS p LEFT JOIN es_goods AS g ON p.goods_id=g.goods_id where p.allactivity_id=? ORDER BY p.`index` desc";
        return this.baseDaoSupport.queryForList(sql,id);
    }

    @Override
    public void del(Integer allActivity_product_id) {
        String sql = "DELETE FROM es_allactivity_product WHERE id="+allActivity_product_id;
        this.baseDaoSupport.execute(sql);
    }

    @Override
    public void delForActivityId(Integer[] id) {
        String sql = "DELETE FROM es_allactivity_product WHERE allactivity_id=?";
        for(int i=0;i<id.length;i++){
            this.baseDaoSupport.execute(sql, id[i]);
        }
    }

    @Override
    public void edit(AllActivityProduct allActivityProduct) {
        String sql = "UPDATE es_allactivity_product SET activity_price=?,`index`=?,count=? WHERE id=?";
        this.baseDaoSupport.execute(sql, allActivityProduct.getActivity_price(),allActivityProduct.getIndex(),allActivityProduct.getCount(),allActivityProduct.getId());
    }

    @Override
    public AllActivityProduct get(Integer id) {
        String sql = "SELECT id,goods_id,original_price,activity_price,count,allactivity_id,`index` FROM es_allactivity_product WHERE id=?";
        return this.baseDaoSupport.queryForObject(sql, AllActivityProduct.class, id);
    }

    @Override
    public void editProductPrice(Integer id, Integer identification) {
        if(identification==1){
            String sql = "update es_goods as g left join es_allactivity_product as p on g.goods_id=p.goods_id LEFT JOIN es_product as pro on pro.goods_id=p.goods_id LEFT JOIN es_cart as c on c.goods_id=p.goods_id set g.price=p.activity_price,pro.price=p.activity_price,c.price=p.activity_price where g.goods_id=p.goods_id AND p.allactivity_id=?";
            this.baseDaoSupport.execute(sql, id);
            System.out.println("秒杀活动开启并且价格修改成功");
        }
        if(identification==0){
            String sql = "update es_goods as g left join es_allactivity_product as p on g.goods_id=p.goods_id LEFT JOIN es_product as pro on pro.goods_id=p.goods_id LEFT JOIN es_cart as c on c.goods_id=p.goods_id set g.price=p.original_price,pro.price=p.original_price,c.price=p.original_price where g.goods_id=p.goods_id AND p.allactivity_id=?";
            this.baseDaoSupport.execute(sql, id);
            System.out.println("秒杀活动关闭并且价格修改成功");
        }
    }

	@Override
	public List showGoodsId(Integer id) {
		 String sql = "SELECT p.goods_id FROM es_allactivity_product AS p LEFT JOIN es_goods AS g ON p.goods_id=g.goods_id where p.allactivity_id=? ORDER BY p.`index` asc limit 0,2";
		return this.baseDaoSupport.queryForList(sql,id);
	}

	@Override
	public void deteteGoodsPro(Integer[] id) {
		 String sql = "update es_goods as g left join es_allactivity_product as p on g.goods_id=p.goods_id LEFT JOIN es_product as pro on pro.goods_id=p.goods_id LEFT JOIN es_cart as c on c.goods_id=p.goods_id set g.price=p.original_price,pro.price=p.original_price,c.price=p.original_price where g.goods_id=p.goods_id AND p.allactivity_id=?";
		 for (int i = 0; i < id.length; i++) {
			 this.baseDaoSupport.execute(sql, id[i]);
		 }
         System.out.println("删除活动价格修改成功");
	}

	@Override
	public void deteteGoods(Integer id) {
		 String sql = "update es_goods as g left join es_allactivity_product as p on g.goods_id=p.goods_id LEFT JOIN es_product as pro on pro.goods_id=p.goods_id LEFT JOIN es_cart as c on c.goods_id=p.goods_id set g.price=p.original_price,pro.price=p.original_price,c.price=p.original_price where g.goods_id=p.goods_id AND p.id=?";
		 this.baseDaoSupport.execute(sql, id);
		 System.out.println("删除活动商品价格修改成功");
	}

	@Override
	public void editProductPriceByDiscount(Integer id, Integer identification) {
		 if(identification==1){
	            String sql = "update es_goods as g left join es_allactivity_product as p on g.goods_id=p.goods_id LEFT JOIN es_product as pro on pro.goods_id=p.goods_id LEFT JOIN es_cart as c on c.goods_id=p.goods_id set g.price=p.activity_price,pro.price=p.activity_price,c.price=p.activity_price where g.goods_id=p.goods_id AND p.allactivity_id=?";
	            this.baseDaoSupport.execute(sql, id);
	            System.out.println("打折活动开启并且价格修改成功");
	        }
	        if(identification==0){
	            String sql = "update es_goods as g left join es_allactivity_product as p on g.goods_id=p.goods_id LEFT JOIN es_product as pro on pro.goods_id=p.goods_id LEFT JOIN es_cart as c on c.goods_id=p.goods_id set g.price=p.original_price,pro.price=p.original_price,c.price=p.original_price,g.is_discount=0 where g.goods_id=p.goods_id AND p.allactivity_id=?";
	            this.baseDaoSupport.execute(sql, id);
	            System.out.println("打折活动关闭并且价格修改成功");
	        }
	}
}
