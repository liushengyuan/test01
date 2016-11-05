package com.enation.app.shop.core.action.backend;

/**
 * @author LiFenLong 4.0版本改造  2014-4-1
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;









import com.enation.app.shop.component.spec.service.ISpecManager;
import com.enation.app.shop.core.model.GoodsCatSpec;
import com.enation.app.shop.core.model.Specification;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IGoodsCatSpecManager;
import com.enation.framework.action.WWAction;

@Component
@Scope("prototype")
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Action("catspec")
@Results({
	@Result(name="catSpecList", type="freemarker", location="/shop/admin/goods/catspec_list.html"),
	@Result(name="catSpecAdd", type="freemarker", location="/shop/admin/goods/addCatSpec.html")
})
@SuppressWarnings({ "rawtypes", "unchecked", "serial","static-access" })
public class GoodsCatSpecAction extends WWAction {
	
	private Integer cat_id;
	private Integer spec_id;
	private Integer id;
	
	private ICartManager cartManager;
	private IGoodsCatSpecManager goodsCatSpecManager;
	private ISpecManager specManager;
	private List<Specification> list=new ArrayList<Specification>();

	/**
	 * 获取分类关联规格列表
	 * @return
	 */
	public String catSpecList(){
		return "catSpecList";
	}
	/**
	 * 跳转选择添加商品的分类
	 * @return 选择添加商品的分类页面
	 */
	public String listJson() {
		this.webpage =this.goodsCatSpecManager.findByCatId(cat_id, this.getPage(), this.getPageSize());
		String s=  JSONArray.fromObject(webpage.getResult()).toString();
		this.json=s;
		return JSON_MESSAGE;
		  //return "select_cat";
	}
	/**
	 * 打开添加关联规格弹出框页面
	 * @return
	 */
	public String add(){
		list=this.specManager.list();
		return "catSpecAdd";
	}
	
	public String saveAdd(){
		try {
			GoodsCatSpec catSpec = new GoodsCatSpec();
			catSpec.setCat_id(cat_id);
			catSpec.setSpec_id(spec_id);
			this.goodsCatSpecManager.delete(cat_id, spec_id);
			this.goodsCatSpecManager.saveAdd(catSpec);
			this.showSuccessJson("规格关联成功！");
		} catch (Exception e) {
			//e.printStackTrace();
			this.showErrorJson("规格关联失败");
			// TODO: handle exception
			logger.error("规格关联失败", e);
		}
		return this.JSON_MESSAGE;
	}
	
	public String delete(){
		try {
			this.goodsCatSpecManager.delete(id);
			this.showSuccessJson("删除成功！");
		} catch (Exception e) {
			// TODO: handle exception
			this.showErrorJson("删除失败");
		}
		return this.JSON_MESSAGE;
	}
	
	public Integer getCat_id() {
		return cat_id;
	}
	public void setCat_id(Integer cat_id) {
		this.cat_id = cat_id;
	}
	public Integer getSpec_id() {
		return spec_id;
	}
	public void setSpec_id(Integer spec_id) {
		this.spec_id = spec_id;
	}
	public ICartManager getCartManager() {
		return cartManager;
	}
	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}
	public ISpecManager getSpecManager() {
		return specManager;
	}
	public void setSpecManager(ISpecManager specManager) {
		this.specManager = specManager;
	}
	public List<Specification> getList() {
		return list;
	}
	public void setList(List<Specification> list) {
		this.list = list;
	}
	public IGoodsCatSpecManager getGoodsCatSpecManager() {
		return goodsCatSpecManager;
	}
	public void setGoodsCatSpecManager(IGoodsCatSpecManager goodsCatSpecManager) {
		this.goodsCatSpecManager = goodsCatSpecManager;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
}
