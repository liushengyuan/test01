package com.enation.app.shop.core.model;

import com.enation.framework.database.PrimaryKeyField;

public class GoodsMove {
	private Integer goods_move_id;
	private String name;
	private String meta_keywords;
	private double price;
	private String big;
	private String small;
	private String intro;
	private Integer store_id;
	@PrimaryKeyField
	public Integer getGoods_move_id() {
		return goods_move_id;
	}
	public void setGoods_move_id(Integer goods_move_id) {
		this.goods_move_id = goods_move_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMeta_keywords() {
		return meta_keywords;
	}
	public void setMeta_keywords(String meta_keywords) {
		this.meta_keywords = meta_keywords;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getBig() {
		return big;
	}
	public void setBig(String big) {
		this.big = big;
	}
	public String getSmall() {
		return small;
	}
	public void setSmall(String small) {
		this.small = small;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	
}
