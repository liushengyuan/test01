package com.enation.app.shop.core.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.model.AllActivityProduct;
import com.enation.framework.database.Page;

/**
 * 类描述： 活动商品管理接口   
 * 创建人：limengnan   
 * 创建时间：2016-1-27 上午9:46:40    
 * 修改人：sks    
 * 修改时间：2016-1-27 上午9:46:40    
 * 修改备注：       
 *
 */
public interface IAllActivityProductManager {
    /**        
     * TODO 获取对应活动下的商品列表
     * @param   活动ID    
     * @return String   
     * @Exception 异常对象       
    */
    public Page get(Integer activity_id,Integer Page,Integer PageSize);
    /**        
     * TODO  获取对应活动下所有商品，用于添加
     * @param   name    
     * @return String   
     * @Exception 异常对象       
    */
    public Page getGoodsList(Map map,Integer page,Integer pageSize);
    /**        
     * TODO  增加activity_product表中数据     
     * @param   allactivityproduct实体    
     * @return String   
     * @Exception 异常对象       
    */
    public void add(AllActivityProduct aap);
    
    /**        
     * TODO  获取参加活动的商品list
     * @param   name    
     * @return String   
     * @Exception 异常对象       
    */
    public List getAll(Integer id);
    /**        
     * TODO     根据allactivity_productID删除对应数据
     * @param  活动从表ID
     * @return void   
     * @Exception 异常对象       
    */
    public void del(Integer allActivity_product_id);
    
    /**        
     * TODO  根据活动id删除从表中的所有数据     
     * @param  integer  id数组    
     * @return void   
     * @Exception 异常对象       
    */
    public void delForActivityId(Integer[] id);
    
    /**        
     * TODO  修改活动商品
     * @param   AllActivityProduct实体
     * @return void
     * @Exception 异常对象       
    */
    public void edit(AllActivityProduct allActivityProduct);
    
    /**        
     * TODO  根据ID获取activity_product_id获取数据
     * @param   name    
     * @return String   
     * @Exception 异常对象       
    */
    public AllActivityProduct get(Integer id);
    
    /**        
     * TODO 当活动开启或者关闭时修改商品价格
     * @param  活动ID，标识，1开启，0关闭
     * @return void
     * @Exception 异常对象       
    */
    public void editProductPrice(Integer id,Integer identification);
    public void editProductPriceByDiscount(Integer id,Integer identification);
    public List showGoodsId(Integer id);
    public void deteteGoodsPro(Integer[] id);
    public void deteteGoods(Integer id);
}
