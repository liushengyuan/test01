package com.enation.app.shop.core.action.backend;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.BrandTag;
import com.enation.app.shop.core.model.BrandTagRel;
import com.enation.app.shop.core.model.Tag;
import com.enation.app.shop.core.model.Team;
import com.enation.app.shop.core.model.TeamProduct;
import com.enation.app.shop.core.service.ITagManager;
import com.enation.app.shop.core.service.ITeamManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.framework.action.WWAction;

/**
 * 推荐类action
 * @author ljs
 * 2015-6-11上午11:54:15
 *
 */
@Component
@Scope("prototype")
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Action("team")
@Results({
	@Result(name="add", type="freemarker", location="/shop/admin/team/add.html"),
	@Result(name="addbrand", type="freemarker", location="/shop/admin/team/addbrand.html"),
	@Result(name="edit", type="freemarker", location="/shop/admin/team/edit.html"),
	@Result(name="editbrand", type="freemarker", location="/shop/admin/team/editbrand.html"),
	@Result(name="editTeamProduct", type="freemarker", location="/shop/admin/team/editTeamProduct.html"),
	@Result(name="editTeamBrand", type="freemarker", location="/shop/admin/team/editTeamBrand.html"),
	@Result(name="list", type="freemarker", location="/shop/admin/team/team_list.html"),
	@Result(name="showbrand", type="freemarker", location="/shop/admin/team/showbrand.html"),
})
public class TeamAction extends WWAction {
	
	private ITeamManager teamManager;
	private Team team;
	private Integer[] team_id;
	private Integer[] brand_id;
	private Integer brandid;
	private Integer teamId;
	private TeamProduct teamProduct;
	private Integer teamProductId;
	private Integer goodsid;
	private BrandTag brandtag;
	private BrandTag brand;
	private BrandTagRel brandTagRel;
	public BrandTagRel getBrandTagRel() {
		return brandTagRel;
	}
	public void setBrandTagRel(BrandTagRel brandTagRel) {
		this.brandTagRel = brandTagRel;
	}
	/**
	 * 检测标签是否有相关联的商品
	 * @param tag_id 标签Id数组,Integer[]
	 * @return json
	 * result 1.有.0.没有
	 */
	public String showBrand(){
		return "showbrand";
	}
/*	
 * public String checkJoinGoods(){
		if(this.tagManager.checkJoinGoods(tag_id)){
			this.json="{result:1}";
		}else{
			this.json="{result:0}";
		}
		return this.JSON_MESSAGE;
	}*/
	/**
	 * 检测标签名是否重名
	 * @param tag 标签,Tag
	 * @return json
	 * result 1.有.0.没有
	 */
/*	public String checkname(){
		if( this.tagManager.checkname(tag.getTag_name(), tag.getTag_id()) ){
			this.json="{result:1}";
		}else{
			this.json="{result:0}";
		}
		return this.JSON_MESSAGE;
	}*/
	/**
	 * 跳转至添加推荐类页面
	 * @return 添加推荐类页面
	 */
	public String add(){
		return "add";
	}
	public String addbrand(){
		return "addbrand";
	}
	/**
	 * 跳转至修改推荐类页面
	 * @param teamId 推荐类IdId,Integer
	 * @return 修改推荐类页面
	 */
	public String edit(){
		team = this.teamManager.getById(teamId);
		return "edit";
	}
	//编辑品牌
	public String editbrand(){
		brand = this.teamManager.getByIdBrand(brandid);
		return "editbrand";
	}
	/**
	 * 跳转至修改推荐商品页面
	 * @param teamProductId 推荐商品IdId,Integer
	 * @return 修改推荐商品页面
	 */
	public String editForTeamProduct(){
		teamProduct = this.teamManager.getByIdForeTeamProduct(teamProductId);
		return "editTeamProduct";
	}
	public String editForTeamBrand(){
		brandTagRel = this.teamManager.getByIdForeTeamBrand(brandid);
		return "editTeamBrand";
	}
	public String saveEditForBrand(){		
		this.teamManager.updateBrand(brandTagRel);
		this.showSuccessJson("推荐品牌修改成功");		
		return this.JSON_MESSAGE;
	}
	public String saveEditForProduct(){		
		this.teamManager.update(teamProduct);
		this.showSuccessJson("推荐商品修改成功");		
		return this.JSON_MESSAGE;
	}
	/**
	 * 删除推荐的商品
	 * @return
	 */
	public String deleteTeamProduct(){
	 	try {
			this.teamManager.delete(teamProductId,goodsid);
			this.showSuccessJson("删除成功");
		} catch (Exception e) {
			this.showErrorJson("删除失败");
			logger.error("删除失败", e);
		}
		return this.JSON_MESSAGE;	
	}
	public String deleteTeamBrand(){
		try {
			this.teamManager.deleteBrand(teamProductId,goodsid);
			this.showSuccessJson("删除成功");
		} catch (Exception e) {
			this.showErrorJson("删除失败");
			logger.error("删除失败", e);
		}
		return this.JSON_MESSAGE;	
	}
	/**
	 * 添加推荐类
	 * @param team 推荐类
	 * @return json
	 * result 1.操作成功.0.操作失败
	 */
	public String saveAdd(){
		try {
			this.teamManager.add(team);
			this.showSuccessJson("添加成功");
		} catch (Exception e) {
			this.showErrorJson("添加失败");
			logger.error("添加失败", e);
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	public String saveAddBrand(){
		try {
			this.teamManager.addBrand(brandtag);
			this.showSuccessJson("添加成功");
		} catch (Exception e) {
			this.showErrorJson("添加失败");
			logger.error("添加失败", e);
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	
	
	/**
	 * 保存修改
	 * @param team 推荐类,team
	 * @return
	 */
	public String saveEdit(){
		
/*		if(EopSetting.IS_DEMO_SITE){
			if(tag.getTag_id()<=3){
				this.showErrorJson("抱歉，当前为演示站点，以不能修改这些示例数据，请下载安装包在本地体验这些功能！");
				return JSON_MESSAGE;
			}
		}*/
		
		this.teamManager.update(team);
		this.showSuccessJson("推荐类修改成功");		
		return this.JSON_MESSAGE;
	}
	public String saveEditBrand(){
		this.teamManager.updatebrand(brandtag);
		this.showSuccessJson("推荐类修改成功");		
		return this.JSON_MESSAGE;
	}
	/**
	 * 删除推荐类
	 * @param team_id 推荐类Id数组,Integer[]
	 * @return result
	 * result 1.操作成功.0.操作失败
	 */
	public String delete(){
/*		if(EopSetting.IS_DEMO_SITE){
			for(Integer tid : tag_id){
				if(tid<=3){
					this.showErrorJson("抱歉，当前为演示站点，以不能修改这些示例数据，请下载安装包在本地体验这些功能！");
					return JSON_MESSAGE;
				}
			}
		}*/
		
	 	try {
			this.teamManager.delete(team_id);
			this.showSuccessJson("删除成功");
		} catch (Exception e) {
			this.showErrorJson("删除失败");
			logger.error("删除失败", e);
		}
		return this.JSON_MESSAGE;	
	}
	//删除品牌
	public String deletebrand(){
		try {
			this.teamManager.deletebrand(brand_id);
			this.showSuccessJson("删除成功");
		} catch (Exception e) {
			this.showErrorJson("删除失败");
			logger.error("删除失败", e);
		}
		return this.JSON_MESSAGE;	
	}
	/**
	 * 跳转至推荐类列表页
	 * @return 推荐类列表页
	 */
	public String list(){
		return "list";
	}
	/**
	 * 获取推荐类列表Json
	 * @author LiFenLong
	 * @return 推荐类列表Json
	 */
	public String listJson(){
		this.webpage = this.teamManager.list(this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return JSON_MESSAGE;
	}
	public ITeamManager getTeamManager() {
		return teamManager;
	}
	public void setTeamManager(ITeamManager teamManager) {
		this.teamManager = teamManager;
	}
	public Team getTeam() {
		return team;
	}
	public void setTeam(Team team) {
		this.team = team;
	}
	public Integer[] getTeam_id() {
		return team_id;
	}
	public void setTeam_id(Integer[] team_id) {
		this.team_id = team_id;
	}
	public Integer getTeamId() {
		return teamId;
	}
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}
	public TeamProduct getTeamProduct() {
		return teamProduct;
	}
	public void setTeamProduct(TeamProduct teamProduct) {
		this.teamProduct = teamProduct;
	}
	public Integer getTeamProductId() {
		return teamProductId;
	}
	public void setTeamProductId(Integer teamProductId) {
		this.teamProductId = teamProductId;
	}
	public Integer getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(Integer goodsid) {
		this.goodsid = goodsid;
	}
	public BrandTag getBrandtag() {
		return brandtag;
	}
	public void setBrandtag(BrandTag brandtag) {
		this.brandtag = brandtag;
	}
	public Integer[] getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(Integer[] brand_id) {
		this.brand_id = brand_id;
	}
	public Integer getBrandid() {
		return brandid;
	}
	public void setBrandid(Integer brandid) {
		this.brandid = brandid;
	}
	public BrandTag getBrand() {
		return brand;
	}
	public void setBrand(BrandTag brand) {
		this.brand = brand;
	}
	
	
	
}
