package com.enation.app.shop.core.model;

import com.enation.framework.database.DynamicField;
import com.enation.framework.database.PrimaryKeyField;

/**
 * 商品推荐类实体
 * @author ljs
 *2010-3-23下午03:34:04
 */
public class Team implements java.io.Serializable {
	
	private Integer team_id;//id
	private String name_zh;//中文名称
	private String name_en;//英文名称
	private String name_ru;//俄文名称
	private String description;//描述
	private Integer is_show;//是否在前台显示
	private String team_image;//图片地址
	private String line_color;//分割线的颜色
	private Integer team_index;//排序序列
	private String pic_url;//图片链接到的地址
	private Integer market;//1，代表中国市场，2代表俄国市场,3代表新西兰馆,4代表韩国馆,5代表德国馆,6代表龙江物产,7代表俄罗斯馆,8代表澳大利亚馆
	
	@PrimaryKeyField
	public Integer getTeam_id() {
		return team_id;
	}
	public void setTeam_id(Integer team_id) {
		this.team_id = team_id;
	}
	public String getName_zh() {
		return name_zh;
	}
	public void setName_zh(String name_zh) {
		this.name_zh = name_zh;
	}
	public String getName_en() {
		return name_en;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	public String getName_ru() {
		return name_ru;
	}
	public void setName_ru(String name_ru) {
		this.name_ru = name_ru;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getIs_show() {
		return is_show;
	}
	public void setIs_show(Integer is_show) {
		this.is_show = is_show;
	}
	public String getTeam_image() {
		return team_image;
	}
	public void setTeam_image(String team_image) {
		this.team_image = team_image;
	}
	public String getLine_color() {
		return line_color;
	}
	public void setLine_color(String line_color) {
		this.line_color = line_color;
	}
	public Integer getTeam_index() {
		return team_index;
	}
	public void setTeam_index(Integer team_index) {
		this.team_index = team_index;
	}
	public String getPic_url() {
		return pic_url;
	}
	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}
	public Integer getMarket() {
		return market;
	}
	public void setMarket(Integer market) {
		this.market = market;
	}
	
	

}
