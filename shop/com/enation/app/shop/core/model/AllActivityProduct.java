package com.enation.app.shop.core.model;

import java.io.Serializable;
public class AllActivityProduct implements Serializable{
    private Integer id;
    private Integer goods_id;//商品ID
    private Double original_price;//原始价格
    private Double activity_price;//活动价格
    private Integer count;//参加活动的商品数量
    private Integer allactivity_id;//活动id
    private Integer index;
    public AllActivityProduct() {
        super();
    }
    public AllActivityProduct(Integer goods_id, Double original_price, Double activity_price, Integer count,Integer allactivity_id,Integer index) {
        super();
        this.goods_id = goods_id;
        this.original_price = original_price;
        this.activity_price = activity_price;
        this.count = count;
        this.allactivity_id = allactivity_id;
        this.index = index;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getGoods_id() {
        return goods_id;
    }
    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }
    public Double getOriginal_price() {
        return original_price;
    }
    public void setOriginal_price(Double original_price) {
        this.original_price = original_price;
    }
    public Double getActivity_price() {
        return activity_price;
    }
    public void setActivity_price(Double activity_price) {
        this.activity_price = activity_price;
    }
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public Integer getAllactivity_id() {
        return allactivity_id;
    }
    public void setAllactivity_id(Integer allactivity_id) {
        this.allactivity_id = allactivity_id;
    }
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
}
