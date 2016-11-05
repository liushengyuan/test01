package com.enation.app.shop.core.tag.detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.plugin.goods.GoodsDataFilterBundle;
import com.enation.app.shop.core.plugin.goods.GoodsPluginBundle;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.ObjectNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class BrandBaseDataTag extends BaseFreeMarkerTag {
	private IGoodsManager goodsManager;
	private GoodsDataFilterBundle goodsDataFilterBundle;
	private GoodsPluginBundle goodsPluginBundle;

	/**
	 * 商品详细标签,如果找不到商品，或商品下架了，会跳转至404页面。
	 * 
	 * @param goodsid
	 *            :商品id
	 *            ,int型，如果不指定此参数，则试图由地址栏获取商品id,如：goods-1.html则会得到商品id为1.或phone
	 *            -1.html也会得到商品id为1.
	 * @return 商品基本信息 {@link Goods} 注：这个goods里有small和big的属性，分别用于输出商品的大图和小图
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		    String brand_id = (String)params.get("brand_id");
			List<Goods> goodsMap = goodsManager.getBrand(Integer.parseInt(brand_id) );
			return goodsMap;

	}
	public GoodsDataFilterBundle getGoodsDataFilterBundle() {
		return goodsDataFilterBundle;
	}

	public void setGoodsDataFilterBundle(
			GoodsDataFilterBundle goodsDataFilterBundle) {
		this.goodsDataFilterBundle = goodsDataFilterBundle;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public GoodsPluginBundle getGoodsPluginBundle() {
		return goodsPluginBundle;
	}

	public void setGoodsPluginBundle(GoodsPluginBundle goodsPluginBundle) {
		this.goodsPluginBundle = goodsPluginBundle;
	}

}
