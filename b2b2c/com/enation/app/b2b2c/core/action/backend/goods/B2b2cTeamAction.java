package com.enation.app.b2b2c.core.action.backend.goods;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.goods.IB2b2cGoodsTagManager;
import com.enation.app.shop.core.service.ITeamManager;
import com.enation.framework.action.WWAction;
/**
 * 推荐类 action
 * @author ljs 
 *
 */
@Component
@ParentPackage("eop_default")
@Namespace("/b2b2c/admin")
@Results({
	 @Result(name="list",type="freemarker", location="/b2b2c/admin/team/team_list.html")
})
@Action("b2b2cTeam")
public class B2b2cTeamAction extends WWAction{
	private ITeamManager teamManager;
	/**
	 * 商品推荐类列表
	 * @return
	 */
	public String list(){
		return "list";
	}
	public String listJson(){
		this.webpage=teamManager.list(this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	public String listJsonBrand(){
		this.webpage=teamManager.listBrand(this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	public ITeamManager getTeamManager() {
		return teamManager;
	}
	public void setTeamManager(ITeamManager teamManager) {
		this.teamManager = teamManager;
	}
	
	
}
