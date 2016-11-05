package com.enation.app.shop.core.service;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.model.AllActivity;
import com.enation.app.shop.core.model.AllActivityProduct;
import com.enation.app.shop.core.model.CheckMemberLogin;
import com.enation.framework.database.Page;

/**
 * 类描述： 活动管理类   
 * 创建人：limengnan   
 * 创建时间：2016-1-26 下午7:45:55    
 * 修改人：sks    
 * 修改时间：2016-1-26 下午7:45:55    
 * 修改备注：       
 *
 */
public interface IAllActivityManager {
    /**        
     * TODO  新增活动     
     * @param   activity对象    
     * @return void   
     * @Exception 异常对象       
    */
    public void addActivity(AllActivity allActivity);
    /**        
     * TODO  获取活动
     * @param  Map查询条件集合  页数，每页大小 
     * @return String   
     * @Exception 异常对象       
    */
    public Page getAllActivity(Map map,Integer page,Integer pageSize);
    
    /**        
     * TODO  获取状态为显示的活动项list
     * @param   name    
     * @return List   
     * @Exception 异常对象       
    */
    public List get(Integer type);
    public List<AllActivity> getAllActivity();
    public List<AllActivity> getAllActivityDiscount();
    /**        
     * TODO   根据id获取活动
     * @param   id    
     * @return AllActivity   
     * @Exception 异常对象       
    */
    public AllActivity getForId(Integer id);
    /**        
     * TODO  修改活动
     * @param   allActivity实体    
     * @return void
     * @Exception 异常对象       
    */
    public void edit(AllActivity allActivity);
    /**        
     * TODO 删除活动
     * @param  id数组    
     * @return void   
     * @Exception 异常对象       
    */
    public void del(Integer[] id);
    /**        
     * TODO 修改开启状态
     * @param   id, 标识，1变更为开启，0变更为未开启
     * @return String   
     * @Exception 异常对象       
    */
    public void editOpen(Integer id,Integer identification);
    public void editOpenByDiscount(Integer id,Integer identification);
    public List<AllActivity> getGoodsById(Integer goods_id);
    public List checkGoodsIsExists(Integer goods_id);
    public List<CheckMemberLogin> isCheck(Integer active_id,String address_id);
    public void addMemberCheckLogin(CheckMemberLogin checkMemberLogin);
    public void delActivityId(Integer id,List<AllActivity> list);
    public List<Map> getOrderMap();
    public void getAllActivityById(int id);
    public void getAllActivityMarket(int id);
    public Page listSkillDetail(Map map, int page, int pageSize);
    public int listSkillDetailCount(Integer type);
    public int listSkillTotalCount(Integer type);
   
    public List<AllActivity> getAlltivity();
    public List<AllActivity> getListALL(Integer[] id);
	public void updagePiceUrl();
}
