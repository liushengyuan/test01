package com.enation.app.base.core.model;

import com.enation.framework.database.NotDbField;
import com.enation.framework.database.PrimaryKeyField;



/**
 * 会员级别
 * @author kingapex
 *
 */
public class MemberLv  implements java.io.Serializable {

    // Fields 
	
	 public MemberLv(){};
	 public MemberLv(Integer lv_id, String name){
		this.lv_id = lv_id;
		this.name = name;
	}
	 private Integer lv_id;
     private String name;
    
	 private Integer default_lv;
     private Integer discount;	
     private Double lvPrice;
     private int point;
     private boolean selected;
  
     private Integer validity;  //积分有效期
 	 private Integer con_ru;//消费次数（卢布）
     private Integer con_zh;//消费次数（人民币）
     private double proportion;//消费抵值比例

     public Integer getValidity() {
		return validity;
	}
	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	public double getProportion() {
		return proportion;
	}
	public void setProportion(double proportion) {
		this.proportion = proportion;
	}
	public Integer getCon_zh() {
		return con_zh;
	}
	public void setCon_zh(Integer con_zh) {
		this.con_zh = con_zh;
	}
	public Integer getDefault_lv() {
		return default_lv;
	}
	public void setDefault_lv(Integer default_lv) {
		this.default_lv = default_lv;
	}
	@PrimaryKeyField
	public Integer getLv_id() {
		return lv_id;
	}
	public void setLv_id(Integer lv_id) {
		this.lv_id = lv_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getDiscount() {
			return discount;
		}
    public void setDiscount(Integer discount) {
			this.discount = discount;
		}
	
	public Integer getCon_ru() {
		return con_ru;
	}
	public void setCon_ru(Integer con_ru) {
		this.con_ru = con_ru;
	}
	
	@NotDbField
	public boolean getSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
	@NotDbField
	public Double getLvPrice() {
		return lvPrice;
	}
	public void setLvPrice(Double lvPrice) {
		this.lvPrice = lvPrice;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	} 

}