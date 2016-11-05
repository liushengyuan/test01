package com.enation.app.tradeease.core.service.cordova.impl;

import java.util.List;

import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.mapper.CartItemMapper;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.tradeease.core.service.cordova.ICartManagerApp;
import com.enation.eop.sdk.database.BaseSupport;

public class CartManagerApp extends BaseSupport implements ICartManagerApp{

	@Override
	public String getSessionId(int member_id) {
		String session_id = null;
		String sql ="select * from es_cart where member_id = ?";
		List<Cart> cart = this.baseDaoSupport.queryForList(sql,Cart.class, member_id);
		if(cart.size()==0){
		}else{
		 session_id = cart.get(cart.size()-1).getSession_id();
		}
		return session_id;
	}
	@Override
	public List<CartItem> listGoods(String sessionid,Integer goods_id) {
		
		 
		StringBuffer sql = new StringBuffer();
//		李世琳加入积分折扣相关方法
//		sql.append("select g.cat_id as catid,g.goods_id,g.thumbnail,c.name ,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.currency,c.addon,c.weight  from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ");
//		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?");
		sql.append("select g.cat_id as catid,g.goods_id,g.thumbnail,g.name_ru as name_ru,c.name ,p.wholesale_volume,p.whprice,p.whprice_ru,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.currency,c.addon,c.weight  from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ");
			
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=? and c.goods_id=?");
		List<CartItem>  list  =this.daoSupport.queryForList(sql.toString(), new CartItemMapper(), sessionid,goods_id);
		return list;
	}

}
