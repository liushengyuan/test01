package com.enation.app.shop.core.action.backend;

import java.io.File;
import java.util.List;

import net.sf.json.JSONArray;

import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.app.shop.core.service.IGoodsTypeManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.database.PermssionRuntimeException;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.database.DBRuntimeException;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

/**
 * 商品分类action
 * 
 * @author apexking
 * 
 * @author xulipeng—修改
 * 2014年4月9日17:54:39
 * 
 */
public class CatAction extends WWAction {

	private IGoodsCatManager goodsCatManager;
	private IGoodsTypeManager goodsTypeManager;

	protected List catList;
	private List typeList;
	private Cat cat;
	private File image;
	private String imageFileName;
	protected int cat_id;
	private int[] cat_ids; // 分类id 数组,用于保存排序
	private int[] cat_sorts; // 分类排序值
	private String imgPath;
	private Cat add_cat;
	private Integer cattype;
	private String font_color;

	// 检测类别是否重名
	public String checkname() {
		if (this.goodsCatManager.checkname(cat.getName(), cat.getCat_id())) {
			this.json = "{result:1}";
		} else {
			this.json = "{result:0}";
		}
		return this.JSON_MESSAGE;
	}

	// 显示列表
	public String list() {
		return "cat_list";
	}

	public String listJson() {
		catList = goodsCatManager.listAllChildren(0);
		String s = JSONArray.fromObject(catList).toString();
		this.json = s.replace("name", "text").replace("cat_id", "id");
		return JSON_MESSAGE;
	}

	public String addlistJson() {
		List addlist = goodsCatManager.listAllChildren(0);
		
		String s = JSONArray.fromObject(addlist).toString();
		this.json = s.replace("name", "text").replace("cat_id", "id");
		return JSON_MESSAGE;
	}

	public String typelistjson() {
		typeList = goodsTypeManager.listAll();
		String s = JSONArray.fromObject(typeList).toString();
		this.json = s.replace("name", "text").replace("type_id", "id");
		return JSON_MESSAGE;
	}

	// 到添加页面
	public String add() {
		return "cat_add";
	}

	public String addChildren() {
		cat = goodsCatManager.getById(cat_id);
		return "children_add";
	}

	// 编辑
	public String edit() {
		try {
			typeList = goodsTypeManager.listAll();
			catList = goodsCatManager.listAllChildren(0);
			cat = goodsCatManager.getById(cat_id);
			if (cat.getImage() != null && !StringUtil.isEmpty(cat.getImage())) {
				imgPath = UploadUtil.replacePath(cat.getImage());
			}
			return "cat_edit";
		} catch (DBRuntimeException ex) {
			this.showErrorJson("您查询的商品不存在");
			return JSON_MESSAGE;
		}
	}

	/**
	 * @author xulipeng
	 * 2014年4月9日17:53:13
	 */
	public String saveAdd() {
		
		if(EopSetting.IS_DEMO_SITE ){
			this.showErrorJson("抱歉，当前为演示站点，以不能添加这些示例数据，请下载安装包在本地体验这些功能！");
			return JSON_MESSAGE;
		}
		
		if (image != null) {
			if (FileUtil.isAllowUp(imageFileName)) {
				cat.setImage(UploadUtil
						.upload(image, imageFileName, "goodscat"));

			} else {
				this.showErrorJson("不允许上传的文件格式，请上传gif,jpg,bmp格式文件。");
				return JSON_MESSAGE;
			}
		}
		if(cattype==1){
			cat.setParent_id(0);
		}
		cat.setType_id(1);//商品类型默认为1
		cat.setGoods_count(0);
		goodsCatManager.saveAdd(cat);
		this.showSuccessJson("商品分类添加成功");
		return JSON_MESSAGE;
	}

	/**
	 * @author xulipeng
	 * 2014年4月9日17:53:13
	 */
	public String saveEdit() {
		if (image != null) {
			if (FileUtil.isAllowUp(imageFileName)) {
				cat.setImage(UploadUtil
						.upload(image, imageFileName, "goodscat"));

			} else {
				this.showErrorJson("不允许上传的文件格式，请上传gif,jpg,bmp格式文件。");
				return JSON_MESSAGE;
			}
		}
		try {
			if (cat.getParent_id().intValue() == 0) {
				this.goodsCatManager.update(cat);
				this.showSuccessJson("商品分类修改成功");
				return JSON_MESSAGE;
			}
			Cat targetCat = goodsCatManager.getById(cat.getParent_id());// 将要修改为父分类的对象
			if (cat.getParent_id().intValue() == cat.getCat_id().intValue()
					|| targetCat.getParent_id().intValue() == cat.getCat_id()
							.intValue()) {
				this.showErrorJson("保存失败：上级分类不能选择当前分类或其子分类");
				return JSON_MESSAGE;
			} else {
				this.goodsCatManager.update(cat);
				this.showSuccessJson("商品分类修改成功");
				return JSON_MESSAGE;
			}
		} catch (PermssionRuntimeException ex) {
			this.showErrorJson("非法操作");
			return JSON_MESSAGE;
		}

	}

	// 删除
	public String delete() {

		if(EopSetting.IS_DEMO_SITE && cat_id <93){
			this.showErrorJson("抱歉，当前为演示站点，以不能修改这些示例数据，请下载安装包在本地体验这些功能！");
			return JSON_MESSAGE;
		}
		
		try {
			int r = this.goodsCatManager.delete(cat_id);

			if (r == 0) {
				this.showSuccessJson("删除成功");
			} else if (r == 1) {
				this.showErrorJson("此类别下存在子类别不能删除!");
			} else if (r == 2) {
				this.showErrorJson("此类别下存在商品不能删除!");
			}
		} catch (PermssionRuntimeException ex) {
			this.showErrorJson("非法操作!");
			return JSON_MESSAGE;
		}
		return JSON_MESSAGE;
	}

	/**
	 * 获取子分类的Json
	 */
	public String getChildJson() {

		try {
			this.catList = this.goodsCatManager.listChildren(this.cat_id);
			this.json = JsonMessageUtil.getListJson(catList);
		} catch (RuntimeException e) {
			this.showErrorJson(e.getMessage());
		}

		return this.JSON_MESSAGE;
	}

	public String saveSort() {
		this.goodsCatManager.saveSort(cat_ids, cat_sorts);
		this.showSuccessJson("保存成功");
		return this.JSON_MESSAGE;
	}

	public List getCatList() {
		return catList;
	}

	public void setCatList(List catList) {
		this.catList = catList;
	}

	public Cat getCat() {
		return cat;
	}

	public void setCat(Cat cat) {
		this.cat = cat;
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public int[] getCat_ids() {
		return cat_ids;
	}

	public void setCat_ids(int[] cat_ids) {
		this.cat_ids = cat_ids;
	}

	public int[] getCat_sorts() {
		return cat_sorts;
	}

	public void setCat_sorts(int[] cat_sorts) {
		this.cat_sorts = cat_sorts;
	}

	public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
		this.goodsCatManager = goodsCatManager;
	}

	public void setGoodsTypeManager(IGoodsTypeManager goodsTypeManager) {
		this.goodsTypeManager = goodsTypeManager;
	}

	public List getTypeList() {
		return typeList;
	}

	public void setTypeList(List typeList) {
		this.typeList = typeList;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public IGoodsCatManager getGoodsCatManager() {
		return goodsCatManager;
	}

	public IGoodsTypeManager getGoodsTypeManager() {
		return goodsTypeManager;
	}

	public Cat getAdd_cat() {
		return add_cat;
	}

	public void setAdd_cat(Cat add_cat) {
		this.add_cat = add_cat;
	}

	public Integer getCattype() {
		return cattype;
	}

	public void setCattype(Integer cattype) {
		this.cattype = cattype;
	}

	public String getFont_color() {
		return font_color;
	}

	public void setFont_color(String font_color) {
		this.font_color = font_color;
	}
	

}
