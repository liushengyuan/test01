package com.enation.app.b2b2c.core.model.store;

import java.io.Serializable;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 店铺等级
 * @author LiFenLong
 *
 */
public class StoreFeature implements Serializable{
	private Integer id;
	private String name;//关键字名称或主营类别名称
	private Integer store_id;//关联的店铺Id
	private Integer feature_type;//1代表关键字，2代表主营类别
	private Integer feature_index;//序列
	
	@PrimaryKeyField
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public Integer getFeature_type() {
		return feature_type;
	}
	public void setFeature_type(Integer feature_type) {
		this.feature_type = feature_type;
	}
	public Integer getFeature_index() {
		return feature_index;
	}
	public void setFeature_index(Integer feature_index) {
		this.feature_index = feature_index;
	}
	
	

}
