package com.enation.app.shop.core.model;

public class BrandTag {
	private Integer brand_id;
	private String name_zh;//中文名称
	private String name_ru;//英文名称
	private Integer is_show;//是否在前台显示
	private Integer market;//1，代表中国市场，2代表俄国市场,3代表新西兰馆,4代表韩国馆,5代表德国馆,6代表龙江物产,7代表俄罗斯馆,8代表澳大利亚馆
	public Integer getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(Integer brand_id) {
		this.brand_id = brand_id;
	}
	public String getName_zh() {
		return name_zh;
	}
	public void setName_zh(String name_zh) {
		this.name_zh = name_zh;
	}
	public String getName_ru() {
		return name_ru;
	}
	public void setName_ru(String name_ru) {
		this.name_ru = name_ru;
	}
	public Integer getIs_show() {
		return is_show;
	}
	public void setIs_show(Integer is_show) {
		this.is_show = is_show;
	}
	public Integer getMarket() {
		return market;
	}
	public void setMarket(Integer market) {
		this.market = market;
	}
}
