package com.enation.app.shop.core.action.backend;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.AllActivity;
import com.enation.app.shop.core.service.IAllActivityManager;
import com.enation.app.shop.core.service.IAllActivityProductManager;
import com.enation.app.shop.core.service.IOrderFlowManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.utils.DateUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
@Component
@ParentPackage("eop_default")
@Namespace("/shop/admin")
@Results({
	 @Result(name="list",type="freemarker", location="/shop/admin/activity/activity_list.html"),
	 @Result(name="showSkillDetail",type="freemarker", location="/shop/admin/activity/showSkillDetail.html"),
	 @Result(name="showDiscountDetail",type="freemarker", location="/shop/admin/activity/showDiscountDetail.html"),
	 @Result(name="edit",type="freemarker", location="/shop/admin/activity/activity_edit.html"),
	 @Result(name="add",type="freemarker", location="/shop/admin/activity/add.html"),
	 @Result(name="goodsList",type="freemarker", location="/shop/admin/activity/goodsList.html")
})
@Action("allActivity")
public class AllActivityAction extends WWAction{
    private IAllActivityProductManager allActivityProductManager;
	private IAllActivityManager allActivityManager;
	private IOrderFlowManager orderFlowManager;
	private String name;
	private Integer index;
	private String start_date;
	private String end_date;
	private String start_time;
	private String end_time;
	private Integer is_show;
	private String line_color;
	private Long long_start_date;
	private Long long_end_date;
	private AllActivity allActivity;
	private Integer limitbuy;
	private Integer limitnumber;
	private String status_Json;
	private Map statusMap;
	private Map orderMap;
	private Map disMap;
	
	private String payStatus_Json;
	private String ship_Json;
	private Integer limitcount;
	private Integer counttotal;
	private String keyword;
	private Integer status;
	private Integer virtual;
    private Integer virtualcount;
    private Integer bonus_select;
    private Integer discountnumber;
	public Integer getVirtual() {
		return virtual;
	}
	public void setVirtual(Integer virtual) {
		this.virtual = virtual;
	}
	public Integer getVirtualcount() {
		return virtualcount;
	}
	public void setVirtualcount(Integer virtualcount) {
		this.virtualcount = virtualcount;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Map getOrderMap() {
		return orderMap;
	}
	public void setOrderMap(Map orderMap) {
		this.orderMap = orderMap;
	}
	public String getPayStatus_Json() {
		return payStatus_Json;
	}
	public void setPayStatus_Json(String payStatus_Json) {
		this.payStatus_Json = payStatus_Json;
	}
	public String getShip_Json() {
		return ship_Json;
	}
	public void setShip_Json(String ship_Json) {
		this.ship_Json = ship_Json;
	}
	public Map getPayStatusMap() {
		return payStatusMap;
	}
	public void setPayStatusMap(Map payStatusMap) {
		this.payStatusMap = payStatusMap;
	}
	public Map getShipMap() {
		return shipMap;
	}
	public void setShipMap(Map shipMap) {
		this.shipMap = shipMap;
	}
	private Map payStatusMap;
	private Map shipMap;
	public Map getStatusMap() {
		return statusMap;
	}
	public void setStatusMap(Map statusMap) {
		this.statusMap = statusMap;
	}
	public Integer getLimitbuy() {
		return limitbuy;
	}
	public void setLimitbuy(Integer limitbuy) {
		this.limitbuy = limitbuy;
	}
	public Integer getLimitnumber() {
		return limitnumber;
	}
	public void setLimitnumber(Integer limitnumber) {
		this.limitnumber = limitnumber;
	}
	public Map getDisMap() {
		return disMap;
	}
	public void setDisMap(Map disMap) {
		this.disMap = disMap;
	}
	private Integer[] id;
	private Integer type;
	public String goodsList(){
	    return "goodsList";
	}
    public String list(){
		return "list";
	}
	public String add(){
	    return "add";
	}
	public String edit(){
        this.allActivity = this.allActivityManager.getForId(this.id[0]);
	    return "edit";
	}
	/**        
	 * TODO  活动列表     
	 * @param   name    
	 * @return String   
	 * @Exception 异常对象       
	*/
	public String listJson(){
	    this.transformation();
	    HashMap<String, String> map = new HashMap<String,String>();
	    map.put("name",this.name);
	    if(this.long_start_date!=null){
	        map.put("start_date", this.long_start_date.toString());
	    }
	    if(this.long_end_date!=null){
	        map.put("end_date", this.long_end_date.toString());
	    }
		this.webpage = this.allActivityManager.getAllActivity(map, this.getPage(), this.getPageSize());
		this.showGridJson(this.webpage);
		return this.JSON_MESSAGE;
	}
	
	/**        
	 * TODO  添加活动     
	 * @param   name    
	 * @return String   
	 * @Exception 异常对象       
	*/
	public String saveAdd(){
	    this.transformation();
	    AllActivity allActivity = new AllActivity(this.name,this.index,this.long_start_date,this.long_end_date,this.is_show,this.line_color,this.type,this.limitbuy,this.limitnumber,this.virtual,this.virtualcount,this.bonus_select,this.discountnumber);
	    this.allActivityManager.addActivity(allActivity);
	    this.showSuccessJson("添加成功");
	    return this.JSON_MESSAGE;
	}
	/**        
	 * TODO  秒杀活动     
	 * @param       
	 * @return String   
	*/
	public String showSkillDetail(){
		if(statusMap==null){
			statusMap = new HashMap();
			statusMap = getStatusJson();
			String p= JSONArray.fromObject(statusMap).toString();
			setStatus_Json(p.replace("[", "").replace("]", ""));
		}
		if(payStatusMap==null){
			payStatusMap = new HashMap();
			payStatusMap = getpPayStatusJson();
			String p= JSONArray.fromObject(payStatusMap).toString();
			payStatus_Json=p.replace("[", "").replace("]", "");
			
		}
		
		if(shipMap ==null){
			shipMap = new HashMap();
			shipMap = getShipJson();
			String p= JSONArray.fromObject(shipMap).toString();
			ship_Json = p.replace("[", "").replace("]", "");
			
		}
		this.limitcount=this.allActivityManager.listSkillDetailCount(1);
		this.counttotal=this.allActivityManager.listSkillTotalCount(1);
		return "showSkillDetail";
	}
	/**        
	 * TODO  打折活动     
	 * @param       
	 * @return String   
	*/
	public String showDiscountDetail(){
		if(statusMap==null){
			statusMap = new HashMap();
			statusMap = getStatusJson();
			String p= JSONArray.fromObject(statusMap).toString();
			setStatus_Json(p.replace("[", "").replace("]", ""));
		}
		if(payStatusMap==null){
			payStatusMap = new HashMap();
			payStatusMap = getpPayStatusJson();
			String p= JSONArray.fromObject(payStatusMap).toString();
			payStatus_Json=p.replace("[", "").replace("]", "");
			
		}
		
		if(shipMap ==null){
			shipMap = new HashMap();
			shipMap = getShipJson();
			String p= JSONArray.fromObject(shipMap).toString();
			ship_Json = p.replace("[", "").replace("]", "");
			
		}
		this.limitcount=this.allActivityManager.listSkillDetailCount(0);
		this.counttotal=this.allActivityManager.listSkillTotalCount(0);
		return "showDiscountDetail";
	}
	public String listSkillJson(){
		orderMap = new HashMap();
		orderMap.put("keyword", keyword);
		orderMap.put("start_time", start_time);
		orderMap.put("end_time", end_time);
		orderMap.put("status", status);
		orderMap.put("type", 1);
		this.webpage = this.allActivityManager.listSkillDetail(orderMap,this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	/**
	 * 查询打折
	 * @return
	 */
	public String listDisJson(){
		disMap = new HashMap();
		disMap.put("keyword", keyword);
		disMap.put("start_time", start_time);
		disMap.put("end_time", end_time);
		disMap.put("status", status);
		disMap.put("type", 0);
		this.webpage = this.allActivityManager.listSkillDetail(disMap,this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	public Integer getLimitcount() {
		return limitcount;
	}
	public void setLimitcount(Integer limitcount) {
		this.limitcount = limitcount;
	}
	public Integer getCounttotal() {
		return counttotal;
	}
	public void setCounttotal(Integer counttotal) {
		this.counttotal = counttotal;
	}
	/**        
	 * TODO   保存修改
	 * @param   name    
	 * @return String   
	 * @Exception 异常对象       
	*/
	public String saveEdit(){
	    try {
            this.transformation();
            this.allActivity.setEnd_time(this.long_end_date);
            this.allActivity.setStart_time(this.long_start_date);
            this.allActivityManager.edit(this.allActivity);
            this.showSuccessJson("修改成功");
        } catch (Exception e) {
            this.showErrorJson("修改失败");
            e.printStackTrace();
        }
	    return this.JSON_MESSAGE;
	}
	/**        
	 * TODO  把从前台获取到的日期转换成long类型 
	 * @param   name    
	 * @return String   
	 * @Exception 异常对象       
	*/
	private void transformation(){
	    if(this.start_date!=null){
	        this.long_start_date = DateUtil.toDate(this.start_date,null).getTime();
	        if(this.start_time!=null){
	            String pattern = "yyyy-MM-dd HH:mm";
	            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	            Date newDate = new Date();
    	        try {
    	            String time = this.start_date+" "+this.start_time;
    	            newDate = sdf.parse(time);
    	            this.long_start_date = newDate.getTime()/1000;
    	        } catch (Exception ex) {
    	            ex.printStackTrace();
    	        }
	        }
	    }
	    if(this.end_date!=null){
	        this.long_end_date =  DateUtil.toDate(this.end_date,null).getTime();
	        if(this.end_time!=null){
                String pattern = "yyyy-MM-dd HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date newDate = new Date();
                try {
                    String time = this.end_date+" "+this.end_time;
                    newDate = sdf.parse(time);
                    this.long_end_date = newDate.getTime()/1000;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
	    }
	}
	
	/**        
	 * TODO 删除活动      
	 * @param   name    
	 * @return String   
	 * @Exception 异常对象       
	*/
	public String delete(){
	    try {
	      List<AllActivity> list=this.allActivityManager.getListALL(id);
	      if(list.size()>0){
	    	  showErrorJson("正在进行的商品不允许删除。请重新选择");
	      }else{
	    	  this.allActivityManager.del(id);//删除主表数据
	          this.allActivityProductManager.delForActivityId(id);//删除从表数据
	          this.allActivityProductManager.deteteGoodsPro(id);
	          this.showSuccessJson("删除成功");
	      }
        } catch (Exception e) {
            this.showErrorJson("删除失败");
            e.printStackTrace();
        }
	    return this.JSON_MESSAGE;
	}
	
	/**        
	 * TODO  每10秒执行方法     
	 * @param   name    
	 * @return String   
	 * @Exception 异常对象       
	*/
	public void update(){
	    Long date=new Date().getTime()/1000;
	    List<AllActivity> list = new ArrayList<AllActivity>(); 
	    list = this.allActivityManager.getAllActivity();
	    for(int i = 0;i<list.size();i++){
	        Long end_time = list.get(i).getEnd_time();
	        Long start_time = list.get(i).getStart_time();
	        if(date<=start_time){
	        	if(list.get(i).getOpen()>=0){
	        		 this.allActivityManager.editOpen(list.get(i).getId(), 2);
	        	}
	        }
	        if(date>=start_time&&date<end_time){
	            if(list.get(i).getOpen()>=0 && list.get(i).getOpen()<1){
	                this.allActivityManager.editOpen(list.get(i).getId(), 1);
	                this.allActivityProductManager.editProductPrice(list.get(i).getId(), 1);
	            }
	        }
	        if(date>=end_time){
	            if(list.get(i).getOpen()==1){
	                this.allActivityProductManager.editProductPrice(list.get(i).getId(), 0);
	                this.allActivityManager.editOpen(list.get(i).getId(), 0);
	            }
	        }
	    }
	}
	/**        
	 * TODO  每59秒执行方法     
	 * @param   name    
	 * @return String   
	 * @Exception 异常对象       
	*/
	public void updateDiscount(){
		 Long date=new Date().getTime()/1000;
		    List<AllActivity> list = new ArrayList<AllActivity>(); 
		    list = this.allActivityManager.getAllActivityDiscount();
		    for(int i = 0;i<list.size();i++){
		        Long end_time = list.get(i).getEnd_time();
		        Long start_time = list.get(i).getStart_time();
		        if(date<=start_time){
		        	if(list.get(i).getOpen()>=0){
		        		 this.allActivityManager.editOpenByDiscount(list.get(i).getId(), 2);
		        	}
		        }
		        if(date>=start_time&&date<end_time){
		            if(list.get(i).getOpen()>=0 && list.get(i).getOpen()<1){
		                this.allActivityManager.editOpenByDiscount(list.get(i).getId(), 1);
		                this.allActivityProductManager.editProductPriceByDiscount(list.get(i).getId(), 1);
		            }
		        }
		        if(date>=end_time){
		            if(list.get(i).getOpen()==1){
		                this.allActivityProductManager.editProductPriceByDiscount(list.get(i).getId(), 0);
		                this.allActivityManager.editOpenByDiscount(list.get(i).getId(), 0);
		            }
		        }
	      }
	}
	/**
	 * 获取订单状态的json
	 * @return
	 */
	private Map getStatusJson(){
		Map orderStatus = new  HashMap();
		
		orderStatus.put(""+OrderStatus.ORDER_NOT_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_PAY));
		orderStatus.put(""+OrderStatus.ORDER_NOT_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_CONFIRM));
		orderStatus.put(""+OrderStatus.ORDER_PAY_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY_CONFIRM));
		orderStatus.put(""+OrderStatus.ORDER_ALLOCATION_YES, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ALLOCATION_YES));
		orderStatus.put(""+OrderStatus.ORDER_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_SHIP));
		orderStatus.put(""+OrderStatus.ORDER_ROG, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ROG));
		orderStatus.put(""+OrderStatus.ORDER_CANCEL_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCEL_SHIP));
		orderStatus.put(""+OrderStatus.ORDER_COMPLETE, OrderStatus.getOrderStatusText(OrderStatus.ORDER_COMPLETE));
		orderStatus.put(""+OrderStatus.ORDER_CANCEL_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCEL_PAY));
		orderStatus.put(""+OrderStatus.ORDER_CANCELLATION, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCELLATION));
		orderStatus.put(""+OrderStatus.ORDER_CHANGED, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CHANGED));
		orderStatus.put(""+OrderStatus.ORDER_CHANGE_APPLY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CHANGE_APPLY));
		orderStatus.put(""+OrderStatus.ORDER_RETURN_APPLY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_RETURN_APPLY));
		orderStatus.put(""+OrderStatus.ORDER_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY));
		return orderStatus;
	}
private Map getpPayStatusJson(){
		
		Map pmap = new HashMap();
		pmap.put(""+OrderStatus.PAY_NO, OrderStatus.getPayStatusText(OrderStatus.PAY_NO));
	//	pmap.put(""+OrderStatus.PAY_YES, OrderStatus.getPayStatusText(OrderStatus.PAY_YES));
		pmap.put(""+OrderStatus.PAY_CONFIRM, OrderStatus.getPayStatusText(OrderStatus.PAY_CONFIRM));
		pmap.put(""+OrderStatus.PAY_CANCEL, OrderStatus.getPayStatusText(OrderStatus.PAY_CANCEL));
		pmap.put(""+OrderStatus.PAY_PARTIAL_PAYED, OrderStatus.getPayStatusText(OrderStatus.PAY_PARTIAL_PAYED));

		return pmap;
	}
	
	private Map getShipJson(){
		Map map = new HashMap();
		map.put(""+OrderStatus.SHIP_ALLOCATION_NO, OrderStatus.getShipStatusText(OrderStatus.SHIP_ALLOCATION_NO));
		map.put(""+OrderStatus.SHIP_ALLOCATION_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_ALLOCATION_YES));
		map.put(""+OrderStatus.SHIP_NO, OrderStatus.getShipStatusText(OrderStatus.SHIP_NO));
		map.put(""+OrderStatus.SHIP_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_YES));
		map.put(""+OrderStatus.SHIP_CANCEL, OrderStatus.getShipStatusText(OrderStatus.SHIP_CANCEL));
		map.put(""+OrderStatus.SHIP_PARTIAL_SHIPED, OrderStatus.getShipStatusText(OrderStatus.SHIP_PARTIAL_SHIPED));
		map.put(""+OrderStatus.SHIP_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_YES));
		map.put(""+OrderStatus.SHIP_CANCEL, OrderStatus.getShipStatusText(OrderStatus.SHIP_CANCEL));
		map.put(""+OrderStatus.SHIP_CHANED, OrderStatus.getShipStatusText(OrderStatus.SHIP_CHANED));
		map.put(""+OrderStatus.SHIP_ROG, OrderStatus.getShipStatusText(OrderStatus.SHIP_ROG));
		return map;
	}
	/**        
	 * TODO  每10秒钟执行方法     
	 * @param   name    
	 * @return String   
	 * @Exception 异常对象       
	*/
	public void changeOrder(){
		List<Map> list=allActivityManager.getOrderMap();
		for (Map map:list) {
			orderFlowManager.cancel(Integer.parseInt(map.get("order_id").toString()), "订单10天之内没有进行付款");
		}
	}
	public void updatePic(){
		this.allActivityManager.updagePiceUrl();
	}
    public IAllActivityManager getAllActivityManager() {
        return allActivityManager;
    }
    public void setAllActivityManager(IAllActivityManager allActivityManager) {
        this.allActivityManager = allActivityManager;
    }
	
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getIndex() {
        return index;
    }
    public void setIndex(Integer index) {
        this.index = index;
    }
    public String getStart_date() {
        return start_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    public String getEnd_date() {
        return end_date;
    }
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
    public Long getLong_start_date() {
        return long_start_date;
    }
    public void setLong_start_date(Long long_start_date) {
        this.long_start_date = long_start_date;
    }
    public Long getLong_end_date() {
        return long_end_date;
    }
    public void setLong_end_date(Long long_end_date) {
        this.long_end_date = long_end_date;
    }
    public Integer getIs_show() {
        return is_show;
    }
    public void setIs_show(Integer is_show) {
        this.is_show = is_show;
    }
    public String getLine_color() {
        return line_color;
    }
    public void setLine_color(String line_color) {
        this.line_color = line_color;
    }
    public String getStart_time() {
        return start_time;
    }
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
    public String getEnd_time() {
        return end_time;
    }
    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public AllActivity getAllActivity() {
        return allActivity;
    }
    public void setAllActivity(AllActivity allActivity) {
        this.allActivity = allActivity;
    }
    public Integer[] getId() {
        return id;
    }
    public void setId(Integer[] id) {
        this.id = id;
    }
    public IAllActivityProductManager getAllActivityProductManager() {
        return allActivityProductManager;
    }
    public void setAllActivityProductManager(IAllActivityProductManager allActivityProductManager) {
        this.allActivityProductManager = allActivityProductManager;
    }
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public IOrderFlowManager getOrderFlowManager() {
		return orderFlowManager;
	}
	public void setOrderFlowManager(IOrderFlowManager orderFlowManager) {
		this.orderFlowManager = orderFlowManager;
	}
	public String getStatus_Json() {
		return status_Json;
	}
	public void setStatus_Json(String status_Json) {
		this.status_Json = status_Json;
	}
	public Integer getBonus_select() {
		return bonus_select;
	}
	public void setBonus_select(Integer bonus_select) {
		this.bonus_select = bonus_select;
	}
	public Integer getDiscountnumber() {
		return discountnumber;
	}
	public void setDiscountnumber(Integer discountnumber) {
		this.discountnumber = discountnumber;
	}
	
}
