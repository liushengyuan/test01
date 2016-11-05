package com.enation.app.cms.core.action;

import java.util.List;
import java.util.Map;

import com.enation.app.base.core.model.MultiSite;
import com.enation.app.base.core.service.IMultiSiteManager;
import com.enation.app.cms.core.model.DataCat;
import com.enation.app.cms.core.model.DataField;
import com.enation.app.cms.core.plugin.ArticlePluginBundle;
import com.enation.app.cms.core.service.IDataCatManager;
import com.enation.app.cms.core.service.IDataFieldManager;
import com.enation.app.cms.core.service.IDataManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.EopContext;
import com.enation.framework.action.WWAction;

/**
 * 文章管理action
 * 
 * @author kingapex 2010-7-5上午11:22:57
 */
public class DataAction extends WWAction {

	private IDataFieldManager dataFieldManager;
	private IDataCatManager dataCatManager;
	private IDataManager dataManager;
	private ArticlePluginBundle articlePluginBundle;
	private IMultiSiteManager multiSiteManager;
	private Integer dataid;
	private Integer catid;
	private Integer modelid;
	private List<DataField> fieldList;
	

	private DataCat cat;
	private String searchField;	//	要搜索的字段
	private String searchText;	//	要搜索的标题
	private List<DataCat> catList;
	private Map article;
	private boolean isEdit;

	private Integer siteid;
	private Integer if_public;// 文章表字段 是否发布
	

	private Integer[] dataids;
	private Integer[] sorts;
	private String contentForImg;
	private String descriptionForImg;

	public String updateSort() {
		try {
			this.dataManager.updateSort(dataids, sorts, catid);
			this.showSuccessJson("修改排序成功");
		} catch (Exception e) {
			this.logger.error(e);
			this.showErrorJson(e.getMessage());
		}
		return DataAction.JSON_MESSAGE;
	}

	

	/**
	 * 导入数据列表
	 * 
	 * @return
	 */
	public String implist() {
		Integer sitecode = 100000;
		if (siteid != null) {
			MultiSite site = this.multiSiteManager.get(siteid);
			sitecode = site.getCode();
		}
		this.webpage = this.dataManager
				.list(catid, this.getPage(), 5, sitecode);
		cat = this.dataCatManager.get(catid);
		fieldList = this.dataFieldManager.listIsShow(cat.getModel_id());
		return "implist";
	}

	public String importdata() {
		this.dataManager.importdata(catid, dataids);
		this.json = "{result:0}";
		return DataAction.JSON_MESSAGE;
	}
	
	//是否公开
	public String editPublic(){
		try {
			this.dataManager.editPublic(if_public,dataid,catid);
			this.showSuccessJson("文章发布状态修改成功");
		} catch (Exception e) {
			this.showErrorJson("文章发布状态修改失败");
		}
		return this.JSON_MESSAGE;
	}
		
	
//跳转到修改界面
	public String edit() {
		this.isEdit = true;
		this.article = this.dataManager.get(dataid, catid, false);
		if (owner(this.article.get("site_code"))) {
			this.cat = this.dataCatManager.get(catid);
			this.catList = this.dataCatManager.listAllChildren(0);
			// this.catList.add(0,cat);
			this.modelid = cat.getModel_id();
			this.fieldList = dataFieldManager.listByCatId(catid);
			for (DataField field : fieldList) {
				field.setInputHtml(articlePluginBundle.onDisplay(field, article.get(field.getEnglish_name())));
			}
			return "input";
		} else {
			this.showErrorJson("非本站内容，不能编辑！");
			return this.JSON_MESSAGE;
		}
	}
	//点击添加文章按钮后跳转回显方法
	public String add() {
		this.isEdit = false;
		this.cat = this.dataCatManager.get(catid);
		this.catList = this.dataCatManager.listAllChildren(catid);
		// this.catList.add(0,cat);
		this.modelid = cat.getModel_id();
		this.fieldList = dataFieldManager.listByCatId(catid);
		for (DataField field : fieldList) {
			field.setInputHtml(articlePluginBundle.onDisplay(field, null));
		}
		return "input";
	}
	//执行文章添加操作
	public String saveAdd() {
		try {
			//data!list.do?catid=" + catid
			this.dataManager.add(modelid, catid);
			this.showSuccessJson("文章添加成功");
		} catch (Exception e) {
			this.showErrorJson("文章添加失败");
		}
		return this.JSON_MESSAGE;
	}
  //修改
	public String saveEdit() {
		try {
			this.dataManager.edit(modelid, catid, dataid,contentForImg,descriptionForImg);
			this.showSuccessJson("文章修改成功");
		} catch (Exception e) {
			this.showErrorJson("文章修改失败");
		}
		return this.JSON_MESSAGE;
	}
	
	private EopSite site;

	public EopSite getSite() {
		return site;
	}

	public void setSite(EopSite site) {
		this.site = site;
	}
		//查找文章
	public String list() {
		cat = this.dataCatManager.get(catid);
		fieldList = this.dataFieldManager.listIsShow(cat.getModel_id());
		return "list";
	}
	public String listJson(){
		String term = null;
		if(this.searchText!=null)
			term = "and " + this.searchField + " like '%" + this.searchText + "%'"; 
		this.webpage = this.dataManager.listAll(catid, term, null, false, this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	public String dlgList() {
		this.webpage = this.dataManager.listAll(catid, null, this.getPage(), 15);
		cat = this.dataCatManager.get(catid);
		fieldList = this.dataFieldManager.listIsShow(cat.getModel_id());
		return "dlglist";
	}

	private boolean owner(Object site_code) {
			return true;
	 
	}

	public String delete() {
		this.article = this.dataManager.get(dataid, catid, false);
		if (this.article.get("sys_lock")!=null&&this.article.get("sys_lock").toString().equals("1")) {
			this.showErrorJson("此文章为系统文章，不能删除！");
		} else {
			if (owner(this.article.get("site_code"))) {
				this.dataManager.delete(catid, dataid);
				this.showSuccessJson("文章删除成功");
			} else {
				this.showErrorJson("非本站内容，不能删除！");
			}
		}
		return this.JSON_MESSAGE;
	}

	public Integer getCatid() {
		return catid;
	}

	public void setCatid(Integer catid) {
		this.catid = catid;
	}

	public IDataFieldManager getDataFieldManager() {
		return dataFieldManager;
	}

	public void setDataFieldManager(IDataFieldManager dataFieldManager) {
		this.dataFieldManager = dataFieldManager;
	}

	public List getFieldList() {
		return fieldList;
	}

	public Integer getModelid() {
		return modelid;
	}

	public void setModelid(Integer modelid) {
		this.modelid = modelid;
	}

	public IDataCatManager getDataCatManager() {
		return dataCatManager;
	}

	public void setDataCatManager(IDataCatManager dataCatManager) {
		this.dataCatManager = dataCatManager;
	}

	public DataCat getCat() {
		return cat;
	}

	public void setCat(DataCat cat) {
		this.cat = cat;
	}

	public IDataManager getDataManager() {
		return dataManager;
	}

	public void setDataManager(IDataManager dataManager) {
		this.dataManager = dataManager;
	}

	public ArticlePluginBundle getArticlePluginBundle() {
		return articlePluginBundle;
	}

	public void setArticlePluginBundle(ArticlePluginBundle articlePluginBundle) {
		this.articlePluginBundle = articlePluginBundle;
	}

	public void setFieldList(List<DataField> fieldList) {
		this.fieldList = fieldList;
	}

	public Map getArticle() {
		return article;
	}

	public void setArticle(Map article) {
		this.article = article;
	}

	public boolean getIsEdit() {
		return isEdit;
	}

	public void setIsEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public Integer getDataid() {
		return dataid;
	}

	public void setDataid(Integer dataid) {
		this.dataid = dataid;
	}

	public Integer[] getDataids() {
		return dataids;
	}

	public void setDataids(Integer[] dataids) {
		this.dataids = dataids;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public Integer[] getSorts() {
		return sorts;
	}

	public void setSorts(Integer[] sorts) {
		this.sorts = sorts;
	}

	public List<DataCat> getCatList() {
		return catList;
	}

	public void setCatList(List<DataCat> catList) {
		this.catList = catList;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	public IMultiSiteManager getMultiSiteManager() {
		return multiSiteManager;
	}

	public void setMultiSiteManager(IMultiSiteManager multiSiteManager) {
		this.multiSiteManager = multiSiteManager;
	}
	
	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}



	public String getContentForImg() {
		return contentForImg;
	}



	public void setContentForImg(String contentForImg) {
		this.contentForImg = contentForImg;
	}



	public String getDescriptionForImg() {
		return descriptionForImg;
	}



	public void setDescriptionForImg(String descriptionForImg) {
		this.descriptionForImg = descriptionForImg;
	}



	public Integer getIf_public() {
		return if_public;
	}



	public void setIf_public(Integer if_public) {
		this.if_public = if_public;
	}




}
