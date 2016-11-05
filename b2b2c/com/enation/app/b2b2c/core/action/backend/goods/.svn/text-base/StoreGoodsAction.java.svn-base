package com.enation.app.b2b2c.core.action.backend.goods;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.MetalKeywordsLogs;
import com.enation.app.shop.core.model.support.GoodsEditDTO;
import com.enation.app.shop.core.plugin.goods.GoodsPluginBundle;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.enation.tool.LuceneService;
import com.memetix.mst.language.Language;

@Component
@ParentPackage("eop_default")
@Namespace("/b2b2c/admin")
@Results({
		@Result(name = "list", type = "freemarker", location = "/b2b2c/admin/goods/goods_list.html"),
		@Result(name = "auditList", type = "freemarker", location = "/b2b2c/admin/goods/search_list.html"),
		@Result(name = "auditChinaList", type = "freemarker", location = "/b2b2c/admin/goods/search_china_list.html"),
		@Result(name = "auditStyle", type = "freemarker", location = "/b2b2c/admin/goods/style_list.html"),
		@Result(name = "input", type = "freemarker", location = "/b2b2c/admin/goods/goods_input.html"),
		@Result(name = "edit_ru", type = "freemarker", location = "/b2b2c/admin/goods/edit_ru.html"),
		@Result(name = "edit_zh", type = "freemarker", location = "/b2b2c/admin/goods/edit_zh.html"),
		
		@Result(name = "success", location = "/b2b2c/admin/goods/success.html") })
@Action("storeGoods")
public class StoreGoodsAction extends WWAction {
	private String optype = "no";
	protected Boolean is_edit;
	protected Integer goodsId;
	protected String actionName;
	protected Map goodsView;
	protected List catList; // 所有商品分类

	protected Map<Integer, String> pluginTabs;
	protected Map<Integer, String> pluginHtmls;

	protected IGoodsCatManager goodsCatManager;// 商品类别管理接口
	protected IGoodsManager goodsManager;// 商品管理接口
	private GoodsPluginBundle goodsPluginBundle;
	private MetalKeywordsLogs keywordsLogs;

	private String addTranslation;// 从页面获取的翻译信息
	private String china_keyword;// 中文关键字
	private String ru_keyword;// 俄文关键字
	private String ru_content;// 俄文商品详细内容
	private String ru_keyword_metal;
	private String name_ru;
	private String store_market;
	private LuceneService luceneService;
	private String storeStatus;
	public String getName_ru() {
		return name_ru;
	}

	public void setName_ru(String name_ru) {
		this.name_ru = name_ru;
	}

	public String getAddTranslation() {
		return addTranslation;
	}

	public void setAddTranslation(String addTranslation) {
		this.addTranslation = addTranslation;
	}

	public String getChina_keyword() {
		return china_keyword;
	}

	public void setChina_keyword(String china_keyword) {
		this.china_keyword = china_keyword;
	}

	public String getRu_keyword() {
		return ru_keyword;
	}

	public void setRu_keyword(String ru_keyword) {
		this.ru_keyword = ru_keyword;
	}

	public String getRu_content() {
		return ru_content;
	}

	public void setRu_content(String ru_content) {
		this.ru_content = ru_content;
	}

	private Goods goods;// 商品信息

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	/**
	 * 商品列表
	 * 
	 * @param brand_id
	 *            品牌Id,Integer
	 * @param catid
	 *            商品分类Id,Integer
	 * @param name
	 *            商品名称,String
	 * @param sn
	 *            商品编号,String
	 * @param tagids
	 *            商品标签Id,Integer[]
	 * @return 商品列表页
	 */
	public String list() {
		return "list";
	}

	/**
	 * 商品审核列表(出口：针对俄罗斯市场)
	 */
	public String auditList() {
		return "auditList";
	}

	/*
	 * 商品审核列表(进口：针对中国市场)
	 */
	public String auditChinaList() {
		return "auditChinaList";
	}
	/*
	 * 商品审核第一步，审查样式列表
	 */
	public String auditStyle() {
		return "auditStyle";
	}

	/**
	 * 一键翻译
	 */
	public String zh_productTranslate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		System.err.println("一键翻译...开始..."+df.format(new Date())+"\n");// new Date()为获取当前系统时间
		Integer translationStatus = 0;//翻译状态，0为未翻译，1为已翻译
		String storeMarket1 = "RUS";//店铺投向市场，RUS为俄罗斯，CHN为中国
		String storeMarket2 = "CHN";
		translate(translationStatus, storeMarket1);
		translate(translationStatus, storeMarket2);
		this.showSuccessJson("一键翻译，完成！");
		System.err.println("一键翻译...结束..."+df.format(new Date())+"\n");
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 根据storeMarket翻译
	 * 当storeMarket为RUS时为中译俄，当storeMarket为CHN时为俄译中
	 */
	public void translate(Integer translationStatus, String storeMarket){
		Language languageFrom = null;
		Language languageTo = null;
		//获取goodsId
		List<String> list = goodsManager.getGoodsId(translationStatus, storeMarket);
		if(storeMarket.equals("RUS")){
			languageFrom = Language.CHINESE_SIMPLIFIED;
			languageTo = Language.RUSSIAN;
		}else if(storeMarket.equals("CHN")){
			languageFrom = Language.RUSSIAN;
			languageTo = Language.CHINESE_SIMPLIFIED;
		}
		for(Object s:list){
			String string = s.toString().replace("{goods_id=", "");
			String string2 = string.replace("}", "");
			int goods_id = Integer.parseInt(string2);
			this.goods = goodsManager.getGoods(goods_id);
			if(goods.getGoods_translation() == 0){
				try{
					if(goods.getName_ru() == null || goods.getName_ru().length() == 0){
						//获取商品名称并翻译
						goods.setName_ru(productNameTranslate(languageFrom, languageTo));
					}
				}catch (Exception e) {
					// TODO: handle exception
					this.showErrorJson("goods_id为："+goods_id+"的商品名称翻译出现异常，请重试！");
				}
				try{
					if(goods.getRu_keyword() == null || goods.getRu_keyword().length() == 0){
						//获取关键字并翻译
						goods.setRu_keyword(keywordsTranslate(languageFrom, languageTo));
					}
				}catch (Exception e) {
					// TODO: handle exception
					this.showErrorJson("goods_id为："+goods_id+"的商品关键字翻译出现异常，请重试！");
				}
				try{
					if(goods.getRu_content() == null || goods.getRu_content().length() == 0){
						//获取商品详情并翻译
						goods.setRu_content(productDetailTranslate(languageFrom, languageTo));
					}
				}catch (Exception e) {
					// TODO: handle exception
					this.showErrorJson("goods_id为："+goods_id+"的商品详情翻译出现异常，请重试！");
				}
				//存入数据库
				this.goodsManager.myEditGood(goods);
			}
		}
	}
	
	/**
	 * 返回中文翻译俄文修改页面
	 * 
	 * @return
	 */
	public String edit_ru() {
		this.goods = goodsManager.getGoods(goodsId);
		//检测翻译状态，若为0，则状态为未翻译，若为1，则状态为已翻译
		if(goods.getGoods_translation() == 0){
			//检测数据库中是否存在名称、关键字、详情的翻译内容，存在，直接取出并显示在页面上；不存在，翻译之后显示在页面上
			if(goods.getName_ru() == null || goods.getName_ru().length() == 0){
				goods.setName_ru(productNameTranslate(Language.CHINESE_SIMPLIFIED, Language.RUSSIAN));
			}
			if(goods.getRu_keyword() == null || goods.getRu_keyword().length() == 0){
				goods.setRu_keyword(keywordsTranslate(Language.CHINESE_SIMPLIFIED, Language.RUSSIAN));
			}
			if(goods.getRu_content() == null || goods.getRu_content().length() == 0){
				goods.setRu_content(productDetailTranslate(Language.CHINESE_SIMPLIFIED, Language.RUSSIAN));
			}
		}
		return "edit_ru";
	}
	
	/**
	 * 返回俄文翻译中文修改页面
	 * 
	 * @return
	 */
	public String edit_zh() {
		this.goods = goodsManager.getGoods(goodsId);
		this.keywordsLogs=goodsManager.showMetalKeywords(goodsId);
		//检测翻译状态，若为0，则状态为为翻译，若为1，则状态为已翻译
		if(goods.getGoods_translation() == 0){
			//检测数据库中是否存在名称、关键字、详情的翻译内容，存在，直接取出并显示在页面上；不存在，翻译之后显示在页面上
			if(goods.getName_ru() == null || goods.getName_ru().length() == 0){
				goods.setName_ru(productNameTranslate(Language.RUSSIAN, Language.CHINESE_SIMPLIFIED));
			}
			if(goods.getRu_keyword() == null || goods.getRu_keyword().length() == 0){
				goods.setRu_keyword(keywordsTranslate(Language.RUSSIAN, Language.CHINESE_SIMPLIFIED));
			}
			if(goods.getRu_content() == null || goods.getRu_content().length() == 0){
				goods.setRu_content(productDetailTranslate(Language.RUSSIAN, Language.CHINESE_SIMPLIFIED));
			}
		}
		return "edit_zh";
	}
	
	/**
	 * 商品名称翻译
	 * @param from
	 * 			源语言
	 * @param to
	 * 			目标语言
	 */
	public String productNameTranslate(Language from, Language to){
		String productName = null;
		if (goods.getGoods_translation() == 0) {
			TranslateMicro translateMicro = new TranslateMicro();
			productName = translateMicro.translate(goods.getName(), from, to);
			if(productName.length() == 0){
				TranslateBaidu translateBaidu = new TranslateBaidu();
				productName = translateBaidu.translate(goods.getName(), "zh", "ru");
			}
		}
		return productName;
	}
	
	/**
	 * 关键字翻译
	 * @param from
	 * 			源语言
	 * @param to
	 * 			目标语言
	 */
	public String keywordsTranslate(Language from, Language to){
		String keywords = null;
		if (goods.getGoods_translation() == 0) {
			TranslateMicro translateMicro = new TranslateMicro();
			keywords = translateMicro.translate(goods.getMeta_keywords(), from, to);
			if(keywords.length() == 0){
				TranslateBaidu translateBaidu = new TranslateBaidu();
				keywords = translateBaidu.translate(goods.getMeta_keywords(), "zh", "ru");
			}
		}
		return keywords;
	}
	
	/**
	 * 商品详情翻译
	 * @param from
	 * 			源语言
	 * @param to
	 * 			目标语言
	 */
	public String productDetailTranslate(Language from, Language to){
		String Ru_content = "";
		if (goods.getGoods_translation() == 0) {
			TranslateMicro translateMicro = new TranslateMicro();
			TranslateBaidu translateBaidu = new TranslateBaidu();
			String intro = goods.getIntro();
			String s = "<";
			String[] splitString = intro.split(s);
			String introDocText = "";
			Document introDoc;
			String ru_contentText = "";
			String changeString = "";
			String textFont1 = "<span style="+"font-family:arial,helvetica,sans-serif;"+"><span style="+"font-size:14px;"+">";
			String ru_contentTextTranslate = "";
			String textFont2 = "</font>";
			String titleFont1 = "<span style="+"font-family:arial,helvetica,sans-serif;"+"><span style="+"font-size:16px;"+">";
			String titleFont2 = "</font>";
			//很重要的变量
			String speSpace = " ";//特殊空格，区别于普通中英文状态下的空格
			String speVariable = "®";//商标符号
			for (int i = 0; i < splitString.length; i++) {
				changeString = (i>0) ? ("<" + splitString[i]) : splitString[i];
				introDoc = Jsoup.parse(changeString);
				introDocText = introDoc.body().text();
				if(introDocText.length()>0 && !introDocText.equals(speSpace)){
					try{
						ru_contentTextTranslate = translateMicro.translate(introDocText, from, to);
						if(ru_contentTextTranslate.length() == 0){
							ru_contentTextTranslate = translateBaidu.translate(introDocText, "zh", "ru");
						}
					}catch(Exception e){
						//System.out.println("翻译详情出错，连接超时");
					}
					Elements element = introDoc.getElementsByTag("p");
					Elements element2 = introDoc.getElementsByTag("em");
					String comment = element.text();
					String comment2 = element2.text();
					if(comment.length()>0 || comment2.length()>0){
						ru_contentText = titleFont1 + ru_contentTextTranslate + titleFont2;
					}else{
						ru_contentText = textFont1 + ru_contentTextTranslate + textFont2;
					}
				}
				if(introDocText.contains(speVariable) || introDocText.contains(speSpace)){
					introDocText = introDocText.replaceAll(speSpace, "&nbsp;");
					introDocText = introDocText.replaceAll(speVariable, "&reg;");
				}
				Ru_content += changeString.replace(introDocText, ru_contentText);
				ru_contentText = "";
			}
		}
		Document doc = Jsoup.parse(Ru_content);
		Ru_content = doc.body().text().length() == 0?"":Ru_content;
		return Ru_content;
	}
	
	/**
	 * 添加翻译、关键字、俄语内容
	 * 
	 * @return
	 */
	public String add_translat() {
		try {
			//Goods goods=goodsManager.get(goodsId);
			Goods goods = goodsManager.getGoods(goodsId);
			String metal_keywords=goods.getMeta_keywords();
			//goods.setGoods_id(goodsId);
			goods.setName_ru(addTranslation);
			String keyword="";
			if(!StringUtil.isEmpty(ru_keyword)){
				String[] str=ru_keyword.split(",");
				for (String string : str) {
					if(!StringUtil.isEmpty(string)){
						keyword+=string+",";
					}
				}
			}
			goods.setMeta_keywords(keyword);
			goods.setRu_keyword(keyword);
			goods.setRu_content(ru_content);
			goods.setGoods_translation(1);
			//this.goodsManager.addTranslation(goods);
			/*---------------------------*/
			MetalKeywordsLogs keywordsLogs=new MetalKeywordsLogs();
			keywordsLogs.setGoods_id(goodsId);
			keywordsLogs.setName(goods.getName());
			keywordsLogs.setSn(goods.getSn());
		    AdminUser	adminUser = UserConext.getCurrentAdminUser();
		    keywordsLogs.setAdmin(adminUser.getUsername());
		    keywordsLogs.setUpdate_keywords_ru(keyword);
		    keywordsLogs.setOriginal_metal_keywords(metal_keywords);
		   /* keywordsLogs.setGoods_translation(1);*/
		    keywordsLogs.setOriginal_keywords_ru(ru_keyword_metal);
		    keywordsLogs.setNowtime(DateUtil.getDateline());
		    this.goodsManager.addMetalKeywords(keywordsLogs);
			this.goodsManager.myEditGood(goods);
			StoreGoods g = new StoreGoods();
			g.setGoods_id(goods.getGoods_id());
			g.setName(goods.getName());
			g.setName_ru(goods.getName_ru());
			
		   
			this.luceneService.updateInformation(g.getGoods_id().toString(),g.getClass().getName(), g, true);
			this.showSuccessJson("翻译成功");
		} catch (Exception e) {
			// TODO: handle exception
			this.showErrorJson("翻译失败，请重新操作！");
			logger.error("商品还原失败", e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 跳转到商品详细页
	 * 
	 * @param catList
	 *            商品分类列表,List
	 * @param actionName
	 *            修改商品方法,String
	 * @param is_edit
	 *            是否为修改商品,boolean
	 * @param goodsView
	 *            商品信息,Map
	 * @param pluginTabs
	 *            商品tab标题List,List
	 * @param pluginHtmls
	 *            商品添加内容List,List
	 * @return 商品详细页
	 */
	public String edit() {
		actionName = "goods!saveEdit.do";
		is_edit = true;

		catList = goodsCatManager.listAllChildren(0);
		//System.out.println(this.goodsManager);
		GoodsEditDTO editDTO = this.goodsManager.getGoodsEditData(goodsId);
		goodsView = editDTO.getGoods();

		this.pluginTabs = this.goodsPluginBundle.getTabList();
		this.pluginHtmls = editDTO.getHtmlMap();

		return this.INPUT;
	}

	public String getOptype() {
		return optype;
	}

	public void setOptype(String optype) {
		this.optype = optype;
	}

	public Boolean getIs_edit() {
		return is_edit;
	}

	public void setIs_edit(Boolean is_edit) {
		this.is_edit = is_edit;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Map getGoodsView() {
		return goodsView;
	}

	public void setGoodsView(Map goodsView) {
		this.goodsView = goodsView;
	}

	public List getCatList() {
		return catList;
	}

	public void setCatList(List catList) {
		this.catList = catList;
	}

	public Map<Integer, String> getPluginTabs() {
		return pluginTabs;
	}

	public void setPluginTabs(Map<Integer, String> pluginTabs) {
		this.pluginTabs = pluginTabs;
	}

	public Map<Integer, String> getPluginHtmls() {
		return pluginHtmls;
	}

	public void setPluginHtmls(Map<Integer, String> pluginHtmls) {
		this.pluginHtmls = pluginHtmls;
	}

	public IGoodsCatManager getGoodsCatManager() {
		return goodsCatManager;
	}

	public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
		this.goodsCatManager = goodsCatManager;
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

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getStore_market() {
		return store_market;
	}

	public void setStore_market(String store_market) {
		this.store_market = store_market;
	}

	public LuceneService getLuceneService() {
		return luceneService;
	}

	public void setLuceneService(LuceneService luceneService) {
		this.luceneService = luceneService;
	}

	public String getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(String storeStatus) {
		this.storeStatus = storeStatus;
	}

	public String getRu_keyword_metal() {
		return ru_keyword_metal;
	}

	public void setRu_keyword_metal(String ru_keyword_metal) {
		this.ru_keyword_metal = ru_keyword_metal;
	}

	public MetalKeywordsLogs getKeywordsLogs() {
		return keywordsLogs;
	}

	public void setKeywordsLogs(MetalKeywordsLogs keywordsLogs) {
		this.keywordsLogs = keywordsLogs;
	}

	
	
}
