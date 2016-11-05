package com.enation.app.shop.core.model;

import java.io.Serializable;
import com.enation.framework.database.PrimaryKeyField;

/**
 * 商品类别和商品规格关联表
 * @author liujisong
 *2015-7-1下午03:34:04
 */
public class GoodsCatSpec implements Serializable {
	
	private Integer id;//关联表的id
	private Integer cat_id;//商品分类id
	private Integer spec_id;//规格id

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSpec_id() {
		return spec_id;
	}

	public void setSpec_id(Integer spec_id) {
		this.spec_id = spec_id;
	}

	public Integer getCat_id() {
		return cat_id;
	}

	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
	}
	

}
