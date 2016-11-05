package com.enation.app.shop.component.groupbuy.model;

import com.enation.app.shop.core.model.Goods;
import com.enation.framework.database.NotDbField;
import com.enation.framework.database.PrimaryKeyField;

/**
 * 团购商品dto
 *  @author jfw
 *2015-10-28上午11:44:58
 */
public class GroupBuyDTO {

	
	private int goods_id;	//商品Id	
	private double price;	//团购价格
	private double original_price;	//原始价格
	private double price_ru;	//团购卢布价格
	private double original_price_ru;	//原始卢布价格
	
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getOriginal_price() {
		return original_price;
	}
	public void setOriginal_price(double original_price) {
		this.original_price = original_price;
	}
	public double getPrice_ru() {
		return price_ru;
	}
	public void setPrice_ru(double price_ru) {
		this.price_ru = price_ru;
	}
	public double getOriginal_price_ru() {
		return original_price_ru;
	}
	public void setOriginal_price_ru(double original_price_ru) {
		this.original_price_ru = original_price_ru;
	}
	
	
}
