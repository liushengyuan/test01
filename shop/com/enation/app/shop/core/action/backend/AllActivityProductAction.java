package com.enation.app.shop.core.action.backend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.AllActivity;
import com.enation.app.shop.core.model.AllActivityProduct;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.service.IAllActivityManager;
import com.enation.app.shop.core.service.IAllActivityProductManager;
import com.enation.app.shop.core.service.impl.ProductManager;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.StringUtil;
/**
 * 类描述：    活动对应商品从表管理action
 * 创建人：limengnan   
 * 创建时间：2016-1-26 下午5:10:19    
 * 修改人：sks    
 * 修改时间：2016-1-26 下午5:10:19    
 * 修改备注：       
 *
 */
@Component
@ParentPackage("eop_default")
@Namespace("/shop/admin")
@Results({
	 @Result(name="list",type="freemarker", location="/shop/admin/activity/goodsList.html"),
	 @Result(name="activityList",type="freemarker", location="/shop/admin/activity/activity_goodsList.html"),
	 @Result(name="addProduct",type="freemarker", location="/shop/admin/activity/addProduct.html"),
	 @Result(name="edit",type="freemarker", location="/shop/admin/activity/edit.html")
})
@Action("allActivityProduct")
public class AllActivityProductAction extends WWAction{
	private Integer allActivity_id;
    private IAllActivityProductManager allActivityProductManager;
    private String name;
    private String sn;
    private String cat_id;
    private Double activity_price;
    private Integer count;
    private Integer goods_id;
    private Double original_price;
    private Integer index;
    private Integer allActivity_product_id;
    private AllActivityProduct allActivityProduct;
    private ProductManager productManager;
    private IAllActivityManager allActivityManager;
    private Product product;
    private String catJson;
    private AllActivity activity;
    private Double cost_price;

    public String list(){
		return "list";
	}

  public String getActivityList(){
        return "activityList";
    }

    public String saveAdd(){
        try {
            AllActivityProduct aap = new AllActivityProduct(this.goods_id, this.original_price, this.activity_price, this.count, this.allActivity_id, this.index);
            this.allActivityProductManager.add(aap);
            this.showSuccessJson("添加成功");
        } catch (Exception e) {
            this.showErrorJson("添加失败");
            e.printStackTrace();
        }
        return this.JSON_MESSAGE;
    }
    
    public String goodsListJson(){
        HashMap map = new HashMap();
        map.put("name", this.name);
        map.put("sn", this.sn);
        map.put("cat_id", this.cat_id);
        map.put("allactivity_id",this.allActivity_id);
        AllActivity activitAllActivity=this.allActivityManager.getForId(this.allActivity_id);
        map.put("type",activitAllActivity.getType());
        this.webpage = this.allActivityProductManager.getGoodsList(map, this.getPage(), this.getPageSize());
        this.showGridJson(this.webpage);
        return this.JSON_MESSAGE;
    }
    /**        
     * TODO  删除推荐商品     
     * @param   name    
     * @return String   
     * @Exception 异常对象       
    */
    public String deleteProduct(){
        try {
            this.allActivityProductManager.del(this.allActivity_product_id);
            this.allActivityProductManager.deteteGoods(this.allActivity_product_id);
            this.showSuccessJson("删除成功");
        } catch (Exception e) {
            this.showErrorJson("删除失败");
            e.printStackTrace();
        }
        return this.JSON_MESSAGE;
    }
    
    /**        
     * TODO  修改活动商品     
     * @param   name    
     * @return String   
     * @Exception 异常对象       
    */
    public String edit(){
        this.allActivityProduct = this.allActivityProductManager.get(allActivity_product_id);
        this.product = this.productManager.getByGoodsId(this.allActivityProduct.getGoods_id());
        return "edit";
    }
    /**        
     * TODO 保存修改      
     * @param   name    
     * @return String   
     * @Exception 异常对象       
    */
    public String saveEdit(){
        try {
            this.allActivityProductManager.edit(this.allActivityProduct);
            this.showSuccessJson("修改成功");
        } catch (Exception e) {
            this.showErrorJson("修改失败");
            e.printStackTrace();
        }
        return this.JSON_MESSAGE;
        
    }
    @SuppressWarnings("unchecked")
    public String listJson(){
        this.webpage = this.allActivityProductManager.get(this.allActivity_id, this.getPage(), this.getPageSize());
        List<Map> list = (List) webpage.getResult();
        for(Map goodlist : list){
            if (goodlist.get("thumbnail") != null && !StringUtil.isEmpty((String)goodlist.get("thumbnail"))) {
                goodlist.put("thumbnail", UploadUtil.replacePath((String)goodlist.get("thumbnail")));
            }
        }
        this.showGridJson(this.webpage);
        return this.JSON_MESSAGE;
    }
    public String addProduct(){
    	this.activity=this.allActivityManager.getForId(allActivity_id);
        return "addProduct";
    }
    public IAllActivityManager getAllActivityManager() {
		return allActivityManager;
	}

	public void setAllActivityManager(IAllActivityManager allActivityManager) {
		this.allActivityManager = allActivityManager;
	}

	public AllActivity getActivity() {
		return activity;
	}

	public void setActivity(AllActivity activity) {
		this.activity = activity;
	}

	public Integer getAllActivity_id() {
        return allActivity_id;
    }

    public void setAllActivity_id(Integer allActivity_id) {
        this.allActivity_id = allActivity_id;
    }

    public IAllActivityProductManager getAllActivityProductManager() {
        return allActivityProductManager;
    }

    public void setAllActivityProductManager(IAllActivityProductManager allActivityProductManager) {
        this.allActivityProductManager = allActivityProductManager;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSn() {
        return sn;
    }
    
    public void setSn(String sn) {
        this.sn = sn;
    }
    
    public String getCat_id() {
        return cat_id;
    }
    
    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Integer getAllActivity_product_id() {
        return allActivity_product_id;
    }

    public void setAllActivity_product_id(Integer allActivity_product_id) {
        this.allActivity_product_id = allActivity_product_id;
    }

    public AllActivityProduct getAllActivityProduct() {
        return allActivityProduct;
    }

    public void setAllActivityProduct(AllActivityProduct allActivityProduct) {
        this.allActivityProduct = allActivityProduct;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


    public String getCatJson() {
        return catJson;
    }

    public void setCatJson(String catJson) {
        this.catJson = catJson;
    }

	public Double getCost_price() {
		return cost_price;
	}

	public void setCost_price(Double cost_price) {
		this.cost_price = cost_price;
	}
    
}
