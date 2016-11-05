package com.enation.app.shop.core.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.RowMapper;

import com.enation.app.b2b2c.core.service.cart.impl.StoreCartManager;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

public class CartItemMapper implements RowMapper {

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		CartItem cartItem = new CartItem();
		cartItem.setId(rs.getInt("cart_id"));
		cartItem.setProduct_id(rs.getInt("product_id"));
		cartItem.setGoods_id(rs.getInt("goods_id"));
		cartItem.setName(rs.getString("name"));
		cartItem.setName_ru(rs.getString("name_ru"));
		cartItem.setMktprice(rs.getDouble("mktprice"));
		cartItem.setPrice(rs.getDouble("price"));
		cartItem.setCoupPrice(rs.getDouble("price")); //优惠价格默认为销售价，则优惠规则去改变
		cartItem.setCatid(rs.getInt("catid"));
		cartItem.setCurrency(rs.getString("currency"));
		cartItem.setWholesale_volume(rs.getInt("wholesale_volume"));
		cartItem.setWhprice(rs.getDouble("whprice"));
		cartItem.setNum(rs.getInt("num"));
		cartItem.setWhprice_ru(rs.getDouble("whprice_ru"));
		if(cartItem.getWholesale_volume()!=null){
			if(cartItem.getNum()>=cartItem.getWholesale_volume() && cartItem.getWholesale_volume()>0){
				if(this.getLanguage().equalsIgnoreCase("zh")){
					cartItem.setCoupPrice(cartItem.getWhprice()*cartItem.getNum());
				}else{
					cartItem.setCoupPrice(cartItem.getWhprice_ru()*cartItem.getNum());
				}	
			}
		}
		
//		String image_default =  rs.getString("image_default");
//		
//		if(image_default!=null ){
//			image_default  =UploadUtil.replacePath(image_default);
//		}
//		cartItem.setImage_default(image_default);
		
		String thumbnail =  rs.getString("thumbnail");
		
		if(thumbnail!=null ){
			thumbnail  =UploadUtil.replacePath(thumbnail);
		}
		cartItem.setImage_default(thumbnail);
		
		cartItem.setNum(rs.getInt("num"));
		cartItem.setPoint(rs.getInt("point"));
		cartItem.setItemtype(rs.getInt("itemtype"));
		//if( cartItem.getItemtype().intValue() ==  0){
			cartItem.setAddon(rs.getString("addon"));
	//	}
		//赠品设置其限购数量
		if( cartItem.getItemtype().intValue() ==  2){
			cartItem.setLimitnum(rs.getInt("limitnum"));
		}
		 
		cartItem.setSn(rs.getString("sn"));
		
		if( cartItem.getItemtype().intValue() ==  0){
			String specs = rs.getString("specs");
			cartItem.setSpecs(specs);
//			if(StringUtil.isEmpty(specs)) 
//				cartItem.setName(  cartItem.getName() );
//			else
//				cartItem.setName(  cartItem.getName() +"("+ specs +")" );
		}
		
		cartItem.setUnit(rs.getString("unit"));
		cartItem.setWeight(rs.getDouble("weight"));
		cartItem.setFreight(rs.getDouble("freight"));
		cartItem.setFreightru(rs.getDouble("freightru"));
		return cartItem;
	}
	

	/**
	 * 获取当前情况下所用的语言
	 * @return
	 */
	public String getLanguage(){
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		return language;
	}
}
